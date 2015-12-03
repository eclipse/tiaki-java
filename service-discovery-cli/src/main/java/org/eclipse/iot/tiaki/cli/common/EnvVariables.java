/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.common;

/**
 * Utility class. I encapsulates the any interactions with the running Environement.
 *
 */
public final class EnvVariables 
{
    /** Environment variable that allows to deactivate the DNSSEC check */
	public static final String INSECURE_SYSTEM_ENV = "INSECURE";

	private EnvVariables () 
    {
		throw new AssertionError( String.format( "Class %s not instantiable", this.getClass().getName() ) );
	}

}
