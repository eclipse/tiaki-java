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
 * Custom Exception. It is used any time a error occurs at runtime.
 *
 */
public class ExecutionException extends Exception 
{

	private static final long serialVersionUID = 4455353859446551777L;
	private int exitCode;


	public ExecutionException ( String message, int exitCode ) 
    {
		super( message );
		this.exitCode = exitCode;
	}


	public int getExitCode () 
    {
		return this.exitCode;
	}
    
}
