/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.command;

import org.eclipse.iot.tiaki.cli.ConsoleWriter;
import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.parser.Options;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import joptsimple.OptionSet;

/**
 * This class defines the Help/Usage command.
 *
 */
public class ShowHelpCommand extends DnsSdAbstractCommand
{

	@Override
	public void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException
    {
		super.initialize( optionSet );
	}


	@Override
	public void doExecute ( ConsoleWriter consoleWriter )
                    throws DnsServiceException
    {
		consoleWriter.log( Options.getUsage() );
	}

}
