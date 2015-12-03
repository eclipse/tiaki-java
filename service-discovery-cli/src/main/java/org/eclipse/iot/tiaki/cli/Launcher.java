/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli;

import joptsimple.OptionSet;
import org.eclipse.iot.tiaki.cli.common.ExitCodes;
import org.eclipse.iot.tiaki.cli.console.DefaultConsoleWriter;
import org.eclipse.iot.tiaki.cli.exception.CommandNotFoundException;
import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.exception.ParsingException;
import org.eclipse.iot.tiaki.cli.parser.DefaultOptionParser;
import org.eclipse.iot.tiaki.cli.parser.Options;

/**
 * Launcher application. This class contains the main method to start up the
 * command line application.
 */
public class Launcher
{

	public static void main ( String[] args )
    {
		ConsoleWriter consoleWriter = new DefaultConsoleWriter();
		CommandOptionParser parser = new DefaultOptionParser();
		try {
			// Parse the arguments
			OptionSet optionSet = parser.parse( args );

			// Build the specific command from the parsed arguments
			Command command = CommandFactory.buildCommand( optionSet );

			// Initialize the command
			command.initialize( optionSet );

			// Execute the command
			command.execute();
		}
		catch ( ParsingException e ) {
			consoleWriter.error("ERROR: Argument parsing error: " + e.getMessage());
			consoleWriter.error(Options.getUsage());
			System.exit(ExitCodes.INVALID_ARGS.getExitCode());
		}
		catch ( CommandNotFoundException e ) {
			consoleWriter.error( "ERROR: Command not found" );
			consoleWriter.error(Options.getUsage());
			System.exit( ExitCodes.INVALID_ARGS.getExitCode() );
		}
		catch ( OptionsNotValidException e ) {
			consoleWriter.error("ERROR: Invalid command arguments: " + e.getMessage());
			consoleWriter.error(Options.getUsage());
			System.exit( ExitCodes.INVALID_ARGS.getExitCode() );
		}
		catch ( ExecutionException e ) {
			// Handles specific command exceptions
			consoleWriter.error("ERROR: " + e.getMessage());
			System.exit( e.getExitCode() );
		}
		catch ( RuntimeException e ) {
			// Handles generic exceptions
			consoleWriter.error("ERROR: " + e.getMessage());
			System.exit( ExitCodes.GENERIC_ERROR.getExitCode() );
		}
	}

}
