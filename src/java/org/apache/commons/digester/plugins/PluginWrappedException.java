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

/**
 * Thrown when some other exception needs to be wrapped with an explanatory
 * message, and none of the other exception types are appropriate.
 *
 * @author Simon Kitching
 */
public class PluginWrappedException extends Exception {

    private Throwable cause = null;

    /**
     * @param cause underlying exception that caused this to be thrown
     */
    public PluginWrappedException(Throwable cause) {
        this(cause.getMessage());
        this.cause = cause;
    }

    /**
     * @param msg describes the reason this exception is being thrown.
     */
    public PluginWrappedException(String msg) {
        super(msg);
    }

    /**
     * @param msg describes the reason this exception is being thrown.
     * @param cause underlying exception that caused this to be thrown
     */
    public PluginWrappedException(String msg, Throwable cause) {
        this(msg);
        this.cause = cause;
    }
    
    /**
     * @return the underlying exception that caused this to be thrown
     */
    public Throwable getCause() {
        return cause;
    }
}
