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
 * Custom Exception. It is raised any time passed options are not valid.
 *
 */
public class OptionsNotValidException extends Exception 
{

	private static final long serialVersionUID = 6227833313445446744L;


	public OptionsNotValidException ( String message ) 
    {
		super( message );
	}
    
}
