/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.exception;

/**
 * Custom Exception. It is used any time a specific instance of <code>Command</code> 
 * cannot be found.
 *
 */
public class CommandNotFoundException extends Exception 
{

	private static final long serialVersionUID = 7036623471905877757L;


	public CommandNotFoundException () {}


	public CommandNotFoundException ( String message ) 
    {
		super( message );
	}


	public CommandNotFoundException ( String message, Throwable cause ) 
    {
		super( message, cause );
	}


	public CommandNotFoundException ( Throwable cause ) 
    {
		super( cause );
	}


	public CommandNotFoundException ( String message, Throwable cause, boolean enableSuppression, 
                                      boolean writableStackTrace ) 
    {
		super( message, cause, enableSuppression, writableStackTrace );
	}
}
