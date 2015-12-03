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
import org.eclipse.iot.tiaki.cli.CommandFactory;
import org.eclipse.iot.tiaki.cli.TestUtils;
import org.eclipse.iot.tiaki.cli.parser.DefaultOptionParser;
import joptsimple.OptionSet;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CommandFactoryTest {

	@Test
	public void testDnsSecCommand () throws Exception {
		Command command = buildCommand( "-c" );
		assertTrue( command instanceof CheckDnsSecCommand );
	}


	@Test
	public void testServiceInstanceCommand () throws Exception {
		Command command = buildCommand( "-i -d domain -s serviceType" );
		assertTrue( command instanceof ListServiceInstanceCommand );
	}


	@Test
	public void testServiceTypesCommand () throws Exception {
		Command command = buildCommand( "-l -d domain" );
		assertTrue( command instanceof ListServiceTypesCommand );
	}


	@Test
	public void testTxtRecordCommand () throws Exception {
		Command command = buildCommand( "-t TEXT_LABEL -d domain -s serviceType" );
		assertTrue( command instanceof ListTextRecordCommand );
	}


	private Command buildCommand ( String arg ) throws Exception {
		String[] args = TestUtils.getArgs( arg );
		DefaultOptionParser parser = new DefaultOptionParser();

		OptionSet optionSet = parser.parse( args );

		return CommandFactory.buildCommand( optionSet );

	}

}
