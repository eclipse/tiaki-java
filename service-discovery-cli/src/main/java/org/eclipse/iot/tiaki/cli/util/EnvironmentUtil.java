/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.util;

import org.eclipse.iot.tiaki.cli.common.EnvVariables;

/**
 * Utility Class. It defines a set of utility methods to aid in dealing with 
 * environment variables.
 *
 */
public final class EnvironmentUtil 
{

	public static boolean isInsecureEnvironment () 
    {
		String insecureEnvVarString = EnvironmentUtil.getEnv( EnvVariables.INSECURE_SYSTEM_ENV );

		if ( insecureEnvVarString != null && !insecureEnvVarString.trim().isEmpty() ) {
			try {
				return Integer.parseInt( insecureEnvVarString ) == 1;
			}
			catch ( NumberFormatException e ) {
				// Just ignore the variable in that case
				return false;
			}
		}
        
		return false;
	}


	public static String getEnv ( String envVariableName ) 
    {
		return System.getenv( envVariableName );
	}


	private EnvironmentUtil () 
    {
		throw new AssertionError( String.format( "Class %s not instantiable", this.getClass().getName() ) );
	}
    
}
