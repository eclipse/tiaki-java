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
 * Custom Exception. It is raised any time a parsing error occurs.
 */
public class ParsingException extends Exception 
{

	private static final long serialVersionUID = 1938828805076909222L;


	public ParsingException () 
    {
	}


	public ParsingException ( String message ) 
    {
		super( message );
	}


	public ParsingException ( String message, Throwable cause ) 
    {
		super( message, cause );
	}


	public ParsingException ( Throwable cause ) 
    {
		super( cause );
	}


	public ParsingException ( String message, Throwable cause, boolean enableSuppression, 
                              boolean writableStackTrace ) 
    {
		super( message, cause, enableSuppression, writableStackTrace );
	}
    
}
