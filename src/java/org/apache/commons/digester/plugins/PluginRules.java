/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package org.apache.commons.digester.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Comparator;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.Rules;
import org.apache.commons.digester.RulesBase;
import org.apache.commons.logging.Log;

/**
 * A custom digester Rules manager which must be used as the Rules object
 * when using the plugins module functionality.
 * </p>
 * @author Simon Kitching
 */

public class PluginRules implements Rules {
                                               
    /** 
     * The rules implementation that we are "enhancing" with plugins
     * functionality, as per the Decorator pattern.
     */
    private Rules decoratedRules;

    /**
     * The Digester instance with which this Rules instance is associated.
     */
    protected Digester digester = null;

    /**
     * The currently active PluginCreateRule. When the begin method of a
     * PluginCreateRule is encountered, this is set. When the end method is
     * encountered, this is cleared. Any attempt to call match() while this
     * attribute is set just causes this single rule to be returned.
     */
    private PluginCreateRule currPluginCreateRule = null;
    
    /** Object which contains information about all known plugins. */
    private PluginManager pluginManager;

    /** The parent rules object for this object. */
    private Rules parent;
    
    // ------------------------------------------------------------- Constructor
    
    /**
     * Constructor for top-level Rules objects. Exactly one of these must
     * be created and installed into the Digester instance as the Rules
     * object before parsing starts.
     */
    public PluginRules() {
        decoratedRules = new RulesBase();
        pluginManager = new PluginManager();
    }

    /**
     * Constructor for top-level Rules object which handles rule-matching
     * using the specified implementation.
     */
    public PluginRules(Rules decoratedRules) {
        this.decoratedRules = decoratedRules;
        pluginManager = new PluginManager();
    }

    /**
     * Constructs a Rules instance which has a parent Rules object 
     * (not a delegate rules object). One of these is created
     * each time a PluginCreateRule's begin method fires, in order to
     * manage the custom rules associated with whatever concrete plugin
     * class the user has specified.
     * <p>
     * The first parameter is not actually used; it is required solely
     * because a constructor with a single Rules parameter already
     * exists.
     * <p>
     * The parent is recorded so that lookups of Declarations can
     * "inherit" declarations from further up the tree.
     */
     PluginRules(PluginCreateRule pcr, PluginRules parent) {
        decoratedRules = new RulesBase();
        this.parent = parent;
        pluginManager = new PluginManager(parent.pluginManager);
    }
    
    // ------------------------------------------------------------- Properties

    /**
     * Return the parent Rules object.
     */
    public Rules getParent() {
        return parent;
    }
    
    /**
     * Return the Digester instance with which this instance is associated.
     */
    public Digester getDigester() {
        return digester;
    }

    /**
     * Set the Digester instance with which this Rules instance is associated.
     *
     * @param digester The newly associated Digester instance
     */
    public void setDigester(Digester digester) {
        this.digester = digester;
        decoratedRules.setDigester(digester);
    }

    /**
     * Return the namespace URI that will be applied to all subsequently
     * added <code>Rule</code> objects.
     */
    public String getNamespaceURI() {
        return decoratedRules.getNamespaceURI();
    }

    /**
     * Set the namespace URI that will be applied to all subsequently
     * added <code>Rule</code> objects.
     *
     * @param namespaceURI Namespace URI that must match on all
     *  subsequently added rules, or <code>null</code> for matching
     *  regardless of the current namespace URI
     */
    public void setNamespaceURI(String namespaceURI) {
        decoratedRules.setNamespaceURI(namespaceURI);
    }

    /**
     * Return the object which "knows" about all declared plugins.
     * 
     * @return The pluginManager value
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Return the list of rules registered with this object, in the order
     * they were registered with this object.
     * <p>
     * Note that Rule objects stored in parent Rules objects are not
     * returned by this method.
     * 
     * @return list of all Rule objects known to this Rules instance.
     */
    public List rules() {
        return decoratedRules.rules();
    }

    /**
     * Register a new Rule instance matching the specified pattern.
     * 
     * @param pattern Nesting pattern to be matched for this Rule.
     * This parameter treats equally patterns that begin with and without
     * a leading slash ('/').
     * @param rule Rule instance to be registered
     */
    public void add(String pattern, Rule rule) {
        Log log = LogUtils.getLogger(digester);
        boolean debug = log.isDebugEnabled();
        
        if (debug) {
            log.debug("add entry" + ": mapping pattern [" + pattern + "]" + 
                  " to rule of type [" + rule.getClass().getName() + "]");
        }
        
        // allow patterns with a leading slash character
        if (pattern.startsWith("/"))
        {
            pattern = pattern.substring(1);
        }
        
        decoratedRules.add(pattern, rule);

        if (rule instanceof InitializableRule) {
            try {
                ((InitializableRule)rule).postRegisterInit(pattern);
            } catch (PluginConfigurationException e) {
                // Currently, Digester doesn't handle exceptions well
                // from the add method. The workaround is for the
                // initialisable rule to remember that its initialisation
                // failed, and to throw the exception when begin is
                // called for the first time.
                if (debug) {
                    log.debug("Rule initialisation failed", e);
                }
                // throw e; -- alas, can't do this
                return;
            }
        }
        
        if (debug) {
            log.debug("add exit" + ": mapped pattern [" + pattern + "]" + 
                  " to rule of type [" + rule.getClass().getName() + "]");
        }
    }

    /**
     * Clear all rules.
     */
    public void clear() {
        decoratedRules.clear();
    }
    
    /**
     * Return a List of all registered Rule instances that match the specified
     * nesting pattern, or a zero-length List if there are no matches.  If more
     * than one Rule instance matches, they <strong>must</strong> be returned
     * in the order originally registered through the <code>add()</code>
     * method.
     *
     * @param pattern Nesting pattern to be matched
     *
     * @deprecated Call match(namespaceURI,pattern) instead.
     */
    public List match(String pattern) {
        return (match(null, pattern));
    }

    /**
     * Return a List of all registered Rule instances that match the specified
     * nesting pattern, or a zero-length List if there are no matches.  If more
     * than one Rule instance matches, they <strong>must</strong> be returned
     * in the order originally registered through the <code>add()</code>
     * method.
     * <p>
     * If we have encountered the start of a PluginCreateRule and have not
     * yet encountered the end tag, then the currPluginCreateRule attribute
     * will be non-null. In this case, we just return this rule object as the
     * sole match. The calling Digester will then invoke the begin/body/end
     * methods on this rule, which are responsible for invoking all rules
     * matching nodes below itself.
     *
     * @param namespaceURI Namespace URI for which to select matching rules,
     *  or <code>null</code> to match regardless of namespace URI
     * @param pattern Nesting pattern to be matched
     */
    public List match(String namespaceURI, String pattern) {
        Log log = LogUtils.getLogger(digester);
        boolean debug = log.isDebugEnabled();
        
        if (debug) {
            log.debug(
                "Matching pattern [" + pattern +
                "] on rules object " + this.toString());
        }

        List matches;
        if ((currPluginCreateRule != null) && 
            (pattern.length() > currPluginCreateRule.getPattern().length())) {
            // assert pattern.startsWith(currPluginCreateRule.getPattern())
            if (debug) {
                log.debug(
                    "Pattern [" + pattern + "] matching PluginCreateRule " +
                    currPluginCreateRule.toString());
            }
            matches = new ArrayList(1);
            matches.add(currPluginCreateRule);
        }
        else {
            matches = decoratedRules.match(namespaceURI, pattern); 
        }

        return matches;
    }
    
    /**
     * Called when a pattern matches a PluginCreateRule, to indicate that
     * any attempt to match any following XML elements should simply
     * return a single match: this PluginCreateRule.
     * <p>
     * In other words, once the plugin element starts, all following 
     * subelements cause the rule object to be "matched", until the
     * endPlugin method is called.
     */
    public void beginPlugin(PluginCreateRule pcr) {
        Log log = LogUtils.getLogger(digester);
        boolean debug = log.isDebugEnabled();

        if (currPluginCreateRule != null) {
            throw new PluginAssertionFailure(
                "endPlugin called when currPluginCreateRule is not null.");
        }

        if (debug) {
            log.debug(
                "Entering PluginCreateRule " + pcr.toString() +
                " on rules object " + this.toString());
        }

        currPluginCreateRule = pcr;
    }
    
    /**
     * Called when a pattern matches the end of a PluginCreateRule.
     * See {@link #beginPlugin}.
     */
    public void endPlugin(PluginCreateRule pcr) {
        Log log = LogUtils.getLogger(digester);
        boolean debug = log.isDebugEnabled();

        if (currPluginCreateRule == null) {
            throw new PluginAssertionFailure(
                "endPlugin called when currPluginCreateRule is null.");
        }
        
        if (currPluginCreateRule != pcr) {
            throw new PluginAssertionFailure(
                "endPlugin called with unexpected PluginCreateRule instance.");
        }
        
        currPluginCreateRule = null;
        if (debug) {
            log.debug(
                "Leaving PluginCreateRule " + pcr.toString() +
                " on rules object " + this.toString());
        }
    }
}
