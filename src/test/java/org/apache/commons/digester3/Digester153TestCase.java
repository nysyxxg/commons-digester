package org.apache.commons.digester3;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.RulesModule;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.junit.Test;
import org.xml.sax.SAXParseException;

/**
 * {@link https://issues.apache.org/jira/browse/DIGESTER-153}
 */
public final class Digester153TestCase
{

    @Test
    public void basicConstructor()
        throws Exception
    {
        ObjectCreateRule createRule = new ObjectCreateRule( TestBean.class );
        createRule.addConstructorArgument( "boolean", boolean.class );
        createRule.addConstructorArgument( "double", double.class );

        Digester digester = new Digester();
        digester.addRule( "toplevel/bean", createRule );

        TestBean bean = digester.parse( getClass().getResourceAsStream( "BasicConstructor.xml" ) );

        assertTrue( bean.getBooleanProperty() );
        assertEquals( 9.99D, bean.getDoubleProperty(), 0 );
    }

    @Test
    public void basicConstructorViaBinder()
        throws Exception
    {
        succesfullConstructor( new AbstractRulesModule()
        {

            @Override
            protected void configure()
            {
                forPattern( "toplevel/bean" )
                    .createObject().ofType( TestBean.class )
                        .addConstructorArgument( "boolean" ).ofType( boolean.class )
                        .addConstructorArgument( "double" ).ofType( double.class );
            }

        } );
    }

    @Test
    public void basicConstructorViaAnnotations()
        throws Exception
    {
        succesfullConstructor( new FromAnnotationsRuleModule()
        {

            @Override
            protected void configureRules()
            {
                bindRulesFrom( TestBean.class );
            }

        } );
    }

    @Test
    public void basicConstructorViaXML()
        throws Exception
    {
        succesfullConstructor( new FromXmlRulesModule()
        {

            @Override
            protected void loadRules()
            {
                loadXMLRules( getClass().getResourceAsStream( "xmlrules/constructor-testrules.xml" ) );
            }

        } );
    }

    private void succesfullConstructor( RulesModule rulesModule )
        throws Exception
    {
        TestBean bean = newLoader( rulesModule )
                            .newDigester()
                            .parse( getClass().getResourceAsStream( "BasicConstructor.xml" ) );

        assertTrue( bean.getBooleanProperty() );
        assertEquals( 9.99D, bean.getDoubleProperty(), 0 );
    }

    @Test
    public void basicConstructorWithValuesNotFound()
        throws Exception
    {
        ObjectCreateRule createRule = new ObjectCreateRule( TestBean.class );
        createRule.addConstructorArgument( "notfound1", boolean.class );
        createRule.addConstructorArgument( "notfound2", double.class );

        Digester digester = new Digester();
        digester.addRule( "toplevel/bean", createRule );

        TestBean bean = digester.parse( getClass().getResourceAsStream( "BasicConstructor.xml" ) );

        assertFalse( bean.getBooleanProperty() );
        assertEquals( 0D, bean.getDoubleProperty(), 0 );
    }

    @Test( expected = SAXParseException.class )
    public void basicConstructorWithWrongParameters()
        throws Exception
    {
        ObjectCreateRule createRule = new ObjectCreateRule( TestBean.class );
        createRule.addConstructorArgument( "notfound1", boolean.class );

        Digester digester = new Digester();
        digester.addRule( "toplevel/bean", createRule );

        digester.parse( getClass().getResourceAsStream( "BasicConstructor.xml" ) );
    }

    @Test
    public void constructorWithClassDefinedInAttribute()
        throws Exception
    {
        ObjectCreateRule createRule = new ObjectCreateRule( null, "type" );
        createRule.addConstructorArgument( "boolean", boolean.class );
        createRule.addConstructorArgument( "double", double.class );

        Digester digester = new Digester();
        digester.addRule( "toplevel/bean", createRule );

        TestBean bean = digester.parse( getClass().getResourceAsStream( "AttributeDefinedConstructor.xml" ) );

        assertTrue( bean.getBooleanProperty() );
        assertEquals( 9.99D, bean.getDoubleProperty(), 0 );
    }

}