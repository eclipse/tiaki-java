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
import org.eclipse.iot.tiaki.commons.Constants;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import joptsimple.OptionSet;


/**
 * This class defines the DNSSEC status check command.
 *
 */
public class CheckDnsSecCommand extends DnsSdAbstractCommand
{

    private Fqdn domain;


    @Override
    public void initialize(OptionSet optionSet) throws ExecutionException, OptionsNotValidException
    {
        super.initialize(optionSet);

        String domainStr = null;
        if (optionSet.hasArgument(Options.DNS_SEC_STATUS)) {
            domainStr = optionSet.valueOf(Options.DNS_SEC_STATUS).toString();
        }

        if (domainStr == null) {
            domainStr = Constants.DEFAULT_DNSSEC_DOMAIN;
        }
        try{
            this.domain = new Fqdn(domainStr);
        } catch(IllegalArgumentException iae) {
            throw new ExecutionException(DisplayUtil.map(StatusCode.ILLEGAL_FQDN),
                                         ExitCodes.INVALID_FQDN.getExitCode());
        }
    }

    @Override
    public void doExecute(ConsoleWriter consoleWriter) throws DnsServiceException
    {

        try {
            this.dnsSd.isDnsSecValid(this.domain);
            consoleWriter.log(String.format(DisplayUtil.SECURE_DNS_RESPONSE,
                                            this.domain.domain()));
        } catch (LookupException e) {
            throw new DnsServiceException(e.dnsError(),
                                          String.format(DisplayUtil.map(e.dnsError()), domain.fqdn()),
                                          true);
        }
    }

}
