/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli;

import org.eclipse.iot.tiaki.cli.command.CheckDnsSecCommand;
import org.eclipse.iot.tiaki.cli.command.ListServiceInstanceCommand;
import org.eclipse.iot.tiaki.cli.command.ListServiceTypesCommand;
import org.eclipse.iot.tiaki.cli.command.ListTextRecordCommand;
import org.eclipse.iot.tiaki.cli.command.ShowHelpCommand;
import org.eclipse.iot.tiaki.cli.command.ListTlsaRecordsCommand;
import org.eclipse.iot.tiaki.cli.exception.CommandNotFoundException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.parser.Options;
import joptsimple.OptionSet;

/**
 * Define a <code>Command</code> factory. This class implements the factory
 * design pattern to create commands according to a set of options.
 *
 */
public class CommandFactory {

	/**
	 * This builds the command.
	 *
	 * @param optionSet     A set of options to be used
	 *
     * @return The so built <code>Command</code> instance.
	 *
     * @throws CommandNotFoundException
     *          In case the command cannot be built by the provided options
	 * @throws OptionsNotValidException
     *          In case the provided options are not valid
	 */
	public static Command buildCommand ( OptionSet optionSet )
                            throws CommandNotFoundException, OptionsNotValidException
    {

		String[] argsCommandOption = new String[] {
                                        Options.LIST_SERVICES, Options.LIST_INSTANCES,
                                        Options.TEXT_RECORD, Options.DNS_SEC_STATUS,
                                        Options.HELP, Options.TLSA_RECORD
		};

		Command command = null;

		for ( String cmdOption : argsCommandOption ) {

			if ( optionSet.has( cmdOption ) ) {

				// If a command has been created already, we return an error.
				if ( command != null ) {
					throw new OptionsNotValidException( "Conflicting mutually exclusive arguments detected" );
				}

				command = CommandFactory.buildCommand( cmdOption );
			}
		}

		if ( command == null ) {
			throw new OptionsNotValidException( "Missing command" );
		}

		return command;
	}

    /**
     * Helper method to parse the option and build the command accordingly.
     *
     * @param commandOption     A <code>String</code> representing the option
     *
     * @return      The <code>Command</code> built accordingly
     *
     * @throws CommandNotFoundException
     *          In case there is no command corresponding to the provided options
     */
	private static Command buildCommand ( String commandOption )
                            throws CommandNotFoundException
    {
		if ( Options.LIST_SERVICES.equals( commandOption ) ) {
			return new ListServiceTypesCommand();
		}
		else if ( Options.LIST_INSTANCES.equals( commandOption ) ) {
			return new ListServiceInstanceCommand();
		}
		else if ( Options.TEXT_RECORD.equals( commandOption ) ) {
			return new ListTextRecordCommand();
		}
		else if ( Options.DNS_SEC_STATUS.equals( commandOption ) ) {
			return new CheckDnsSecCommand();
		}
		else if ( Options.TLSA_RECORD.equals( commandOption ) ) {
			return new ListTlsaRecordsCommand();
		}
		else if ( Options.HELP.equals( commandOption ) ) {
			return new ShowHelpCommand();
		}
		else {
			throw new CommandNotFoundException( String.format( "No command registered for the argument: %s", commandOption ) );
		}
	}

}
