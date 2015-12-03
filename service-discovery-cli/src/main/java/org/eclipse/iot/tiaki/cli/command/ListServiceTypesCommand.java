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
import org.eclipse.iot.tiaki.cli.common.ExitCodes;
import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.parser.Options;
import org.eclipse.iot.tiaki.cli.util.DisplayUtil;
import org.eclipse.iot.tiaki.cli.util.OptionUtil;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import java.util.Set;
import joptsimple.OptionSet;


/**
 * This class defines the listing Service Types command.
 *
 */
public class ListServiceTypesCommand extends DnsSdAbstractCommand
{

	private Fqdn domain;


	@Override
	public void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException
    {
		super.initialize( optionSet );

		String domainStr = OptionUtil.getOptionValue(optionSet, Options.DOMAIN, true);
        try {
            this.domain = new Fqdn(domainStr);
        } catch(IllegalArgumentException iae) {
            throw new ExecutionException(DisplayUtil.map(StatusCode.ILLEGAL_FQDN),
                                         ExitCodes.INVALID_FQDN.getExitCode());
        }
	}


	@Override
	public void doExecute ( ConsoleWriter consoleWriter )
                    throws DnsServiceException
    {
		Set<String> serviceTypes = null;
        try {
            serviceTypes = this.dnsSd.listServiceTypes( this.domain, !super.insecureMode );
        } catch(LookupException le) {
            throw new DnsServiceException(le.dnsError(),
                                          String.format(DisplayUtil.map(le.dnsError()), domain),
                                          true);
        }
        for ( String serviceType : serviceTypes ) {
			consoleWriter.log( serviceType );
		}
	}

}
