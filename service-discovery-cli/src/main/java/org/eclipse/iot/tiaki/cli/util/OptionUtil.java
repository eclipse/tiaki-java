/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.util;

import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import joptsimple.OptionSet;

/**
 * Utility Class. It defines and implements a set of utility methods to deal with 
 * command line options parsing.
 *
 */
public final class OptionUtil 
{

    private static final String checkAddress = "^([0-9]{1,}\\.){0,2}[0-9]{1,}$";


	public static String getOptionValue ( OptionSet optionSet, String option, boolean mandatory )
			throws OptionsNotValidException
    {
		boolean hasOption = optionSet.has( option );
		if ( !hasOption && mandatory ) {
			throw new OptionsNotValidException( String.format( "A mandatory option \"%s\" is missing", option ) );
		}else{
			String value = null;
			try{
				value = optionSet.valueOf( option ).toString();
			} catch (NullPointerException npe){

			}

			if(hasOption && mandatory){
				if ( value == null || value.trim().isEmpty() ) {
					throw new OptionsNotValidException( String.format( "Null or empty value for the option \"%s\"", option ) );
				}
			}
			return value;
		}
	}

    public static boolean checkResolverAddress(String srvAddress)
    {
        return srvAddress.matches(checkAddress);
    }


	private OptionUtil () 
    {
		throw new AssertionError( String.format( "Class %s not instantiable", this.getClass().getName() ) );
	}
    
}
