/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.console;

import org.eclipse.iot.tiaki.cli.ConsoleWriter;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * This class defines a specific writer that writes on the console output streams.
 *
 */
public class DefaultConsoleWriter implements ConsoleWriter 
{

	private boolean verbose;


	public DefaultConsoleWriter () {}


	public DefaultConsoleWriter ( boolean verbose ) 
    {
		this.verbose = verbose;

		BasicConfigurator.configure();
		Logger.getLogger( "org.jitsi.dnssec" ).setLevel( Level.OFF );
	}


	@Override
	public void verbose ( String message ) 
    {
		if ( verbose ) {
			log( message );
		}
	}


	@Override
	public void error ( String message ) 
    {
		System.err.println( message );
	}


	@Override
	public void log ( String message ) 
    {
		// TODO: Add also logging into separate log files as well as on the console
		System.out.println( message );
	}

}
