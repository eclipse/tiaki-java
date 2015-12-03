/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli;

/**
 * Define an console writer. This interface specifies the behaviour of a generic 
 * console writers that has is responsible for logging out information, on the most
 * appropriated standard of output.
 */
public interface ConsoleWriter 
{
    
    /**
     * Logs out verbose information.
     * 
     * @param message   A <code>String</code> containing the message to be logged out
     */
	void verbose ( String message );

    /**
     * Logs out error information on the standard error.
     * 
     * @param message   A <code>String</code> containing the message to be logged out
     */
	void error ( String message );

    /**
     * Logs out information.
     * 
     * @param message   A <code>String</code> containing the message to be logged out
     */
	void log ( String message );

}
