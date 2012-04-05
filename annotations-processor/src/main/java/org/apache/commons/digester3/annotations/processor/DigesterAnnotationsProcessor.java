package org.apache.commons.digester3.annotations.processor;

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

import static com.sun.codemodel.JMod.FINAL;
import static com.sun.codemodel.JMod.PROTECTED;
import static com.sun.codemodel.JMod.PUBLIC;

import static java.lang.String.format;

import static javax.tools.Diagnostic.Kind.*;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.CallMethod;
import org.apache.commons.digester3.annotations.rules.CallParam;
import org.apache.commons.digester3.annotations.rules.CreationRule;
import org.apache.commons.digester3.annotations.rules.FactoryCreate;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.PathCallParam;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;
import org.apache.commons.digester3.annotations.rules.SetRoot;
import org.apache.commons.digester3.annotations.rules.SetTop;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JPackage;

/**
 * @since 3.3
 */
public class DigesterAnnotationsProcessor
    extends AbstractProcessor
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        return new HashSet<String>( asList( BeanPropertySetter.class.getName(),
                                            CallMethod.class.getName(),
                                            CallParam.class.getName(),
                                            CreationRule.class.getName(),
                                            FactoryCreate.class.getName(),
                                            ObjectCreate.class.getName(),
                                            PathCallParam.class.getName(),
                                            SetNext.class.getName(),
                                            SetProperty.class.getName(),
                                            SetRoot.class.getName(),
                                            SetTop.class.getName() ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean process( Set<? extends TypeElement> annotations, RoundEnvironment environment )
    {
        // processingEnv is a predefined member in AbstractProcessor class
        // Messager allows the processor to output messages to the environment
        Messager messager = processingEnv.getMessager();

        // TODO get these values from -A parameters
        String packageName = "com.acme";
        String className = "MyRules";

        JCodeModel codeModel = new JCodeModel();

        JPackage modulePackage = codeModel._package( packageName );

        boolean success = false;

        try
        {
            JDefinedClass definedClass = modulePackage._class( FINAL | PUBLIC, className );
            definedClass.javadoc().add( format( "Generated by Apache Commons Digester at %s", new Date() ) );

            JMethod method = definedClass.method( PROTECTED, Void.class, "configure" );
            method.annotate( Override.class );

            // Loop through the annotations that we are going to process
            for ( TypeElement annotation : annotations )
            {
                // Get the members
                for ( Element element : environment.getElementsAnnotatedWith( annotation ) )
                {
                    System.out.println( format( "Processing @%s %s", annotation, element ) );
                    messager.printMessage( OTHER, format( "Processing @%s %s", annotation, element ) );
                }
            }

            codeModel.build( new FilerCodeWriter( processingEnv.getFiler() ) );

            success = true;
        }
        catch ( JClassAlreadyExistsException e )
        {
            messager.printMessage( ERROR, format( "Class %s.%s has been already defined", packageName, className ) );
        }
        catch ( IOException e )
        {
            messager.printMessage( ERROR, format( "Impossible to generate class %s.%s: %s",
                                                  packageName, className, e.getMessage() ) );
        }

        return success;
    }

}
