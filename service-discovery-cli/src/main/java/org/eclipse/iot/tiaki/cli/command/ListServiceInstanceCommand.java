/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.command;

import java.util.Set;
import joptsimple.OptionSet;
import org.eclipse.iot.tiaki.cli.ConsoleWriter;
import org.eclipse.iot.tiaki.cli.common.ExitCodes;
import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.parser.Options;
import org.eclipse.iot.tiaki.cli.util.DisplayUtil;
import org.eclipse.iot.tiaki.cli.util.OptionUtil;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import org.eclipse.iot.tiaki.exceptions.LookupException;

/**
 * This class defines the listing Service Instances command.
 *
 */
public class ListServiceInstanceCommand extends DnsSdAbstractCommand
{

	private Fqdn domain;
	private CompoundLabel serviceType;


	@Override
	public void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException
    {
		super.initialize( optionSet );
		String domainStr = OptionUtil.getOptionValue( optionSet, Options.DOMAIN, true );
        String label = OptionUtil.getOptionValue(optionSet, Options.SUPPLEMENT, true);

        String[] parts = null;
        try {
            if(CompoundLabel.isCompound(label)) {
                parts = CompoundLabel.labelComponents(label);
                this.serviceType = new CompoundLabel(parts[0], parts[1], parts[2]);
            } else
                this.serviceType = new CompoundLabel(label);
        } catch(IllegalArgumentException iae) {
            throw new OptionsNotValidException(String.format("%s: %s",
                                DisplayUtil.INVALID_ARGUMENT, iae.getMessage()));
        }

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
        Set<ServiceInstance> serviceInstances = null;
        try {
			serviceInstances = this.dnsSd.listServiceInstances( this.domain, this.serviceType,
                                                                !super.insecureMode );
        } catch(LookupException le) {
            throw new DnsServiceException(le.dnsError(),
                                          String.format(DisplayUtil.map(le.dnsError()), domain.fqdn()),
                                          true);
        }
        for ( ServiceInstance instance : serviceInstances ) {
			consoleWriter.log( instance.toString() );
		}
	}

}
