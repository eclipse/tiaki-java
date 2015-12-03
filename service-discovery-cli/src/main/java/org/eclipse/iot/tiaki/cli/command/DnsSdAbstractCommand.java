/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.command;

import org.eclipse.iot.tiaki.cli.Command;
import org.eclipse.iot.tiaki.cli.ConsoleWriter;
import org.eclipse.iot.tiaki.cli.common.ExitCodes;
import org.eclipse.iot.tiaki.cli.console.DefaultConsoleWriter;
import org.eclipse.iot.tiaki.cli.console.LibraryObserver;
import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import org.eclipse.iot.tiaki.cli.parser.Options;
import org.eclipse.iot.tiaki.cli.util.DisplayUtil;
import org.eclipse.iot.tiaki.cli.util.EnvironmentUtil;
import org.eclipse.iot.tiaki.cli.util.ExitCodeMapper;
import org.eclipse.iot.tiaki.cli.util.OptionUtil;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.exceptions.DnsServiceException;
import org.eclipse.iot.tiaki.services.DnsServicesDiscovery;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import joptsimple.OptionSet;


/**
 * DNS-SD Command Abstract Class. This class implements the abstract method pattern,
 * implementing the basic behaviour of a DNS-SD command and delegating to the
 * specific instances the instantiation and execution details.
 *
 */
public abstract class DnsSdAbstractCommand implements Command
{

	protected DnsServicesDiscovery dnsSd;
	protected ConsoleWriter consoleWriter;

	protected boolean insecureMode = false;
	protected boolean verboseMode = false;

	protected String dnsServer = null;
	protected String trustAnchorFileLocation = null;


	public DnsSdAbstractCommand () {}


	@Override
	public void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException
    {

		// Initialize the insecure mode from the arguments or from the environment
		this.insecureMode = this.insecureMode || optionSet.has( Options.INSECURE );
		this.insecureMode = this.insecureMode || EnvironmentUtil.isInsecureEnvironment();

		// Initialize the verbose mode
		this.verboseMode = optionSet.has( Options.VERBOSE );

		// Initialize the DNS Server
		if ( optionSet.has( Options.DNS_SERVER ) ) {
			this.dnsServer = optionSet.valueOf( Options.DNS_SERVER ).toString();
		}

		// Initialize the trust anchor file
		if ( optionSet.has( Options.TRUST_ANCHOR ) ) {
			this.trustAnchorFileLocation = optionSet.valueOf( Options.TRUST_ANCHOR ).toString();
		}

		this.consoleWriter = new DefaultConsoleWriter( verboseMode );

		// Initialize the library
		this.dnsSd = new DnsServicesDiscovery();

		// Configure the trusted file
		if ( this.trustAnchorFileLocation != null ) {
			this.dnsSd.trustAnchorFile( new File( this.trustAnchorFileLocation ) );
		}

		LibraryObserver libraryObserver = new LibraryObserver( this.consoleWriter );

		this.dnsSd.introspected( true );
		this.dnsSd.observer( libraryObserver );

	}


	@Override
	public void execute () throws ExecutionException
    {

		// Set the DNS server
		if ( this.dnsServer != null && !this.dnsServer.trim().isEmpty() ) {
			try {
				InetAddress inetAddress = InetAddress.getByName( this.dnsServer );
				this.dnsSd.dnsServer( inetAddress );
                if(OptionUtil.checkResolverAddress(dnsServer))
                    throw new UnknownHostException("Invalid Resolver Address");
			}
			catch ( UnknownHostException e ) {
				throw new ExecutionException( String.format( DisplayUtil.INVALID_DNS_HOST,
                        this.dnsServer ),
						ExitCodes.UNKNOWN_DNS_SRV_HOST.getExitCode() );
			}
		}

		try {
			this.dnsSd.checkConfiguration( true );
			// Execute the specific command
			doExecute( this.consoleWriter );
		}
		catch ( DnsServiceException e ) {

			StatusCode statusCode = e.dnsError();

			// Map the library error code to the application exit code
			ExitCodes exitCode = ExitCodeMapper.map( statusCode );

			this.consoleWriter.verbose( e.printableErrorsTrace() );
			throw new ExecutionException( e.getMessage(), exitCode.getExitCode() );
		}
	}


	public abstract void doExecute ( ConsoleWriter consoleWriter )
                            throws DnsServiceException;
}
