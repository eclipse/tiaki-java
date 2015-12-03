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
import org.eclipse.iot.tiaki.domain.TextRecord;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import java.util.Set;
import joptsimple.OptionSet;

/**
 * This class defines the listing Text Records command.
 *
 */
public class ListTextRecordCommand extends DnsSdAbstractCommand
{

	private Fqdn domain;
	private String textRecord;


	@Override
	public void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException
    {
		super.initialize( optionSet );
        try {
            this.domain = new Fqdn(OptionUtil.getOptionValue(optionSet, Options.DOMAIN, true));
        } catch(IllegalArgumentException iae) {
            throw new ExecutionException(DisplayUtil.map(StatusCode.ILLEGAL_FQDN),
                                         ExitCodes.INVALID_FQDN.getExitCode());
        }
        this.textRecord = optionSet.valueOf( Options.TEXT_RECORD ).toString();
	}


	@Override
	public void doExecute ( ConsoleWriter consoleWriter )
                    throws DnsServiceException
    {
		Set<TextRecord> textRecords = null;
        try {
            textRecords = this.dnsSd.listTextRecords( this.domain, this.textRecord,
                    !super.insecureMode );
        } catch(LookupException le) {
            throw new DnsServiceException(le.dnsError(),
                                          String.format(DisplayUtil.map(le.dnsError()), domain.fqdn()),
                                          true);
        }
        for ( TextRecord textRecord : textRecords ) {
			consoleWriter.log( textRecord.toString() );
		}
	}

}
