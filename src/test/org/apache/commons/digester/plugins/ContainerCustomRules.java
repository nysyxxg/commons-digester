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

import org.apache.commons.digester.Digester;

public class ContainerCustomRules {
    public static void addRules(Digester digester, String pattern) {
        // A Container object can have subtags called "widget" which
        // define any object of type Widget. Because a Container is
        // itself a widget, this allows us to build trees of objects.
        PluginCreateRule pcr = new PluginCreateRule(Widget.class);
        digester.addRule(pattern+"/widget", pcr);
        digester.addSetNext(pattern+"/widget", "addChild");
        
        // allow users to declare plugins under a container as well
        PluginDeclarationRule pdr = new PluginDeclarationRule();
        digester.addRule(pattern+"/plugin", pdr);
    }
}
