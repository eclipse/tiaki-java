/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.parsing;

import org.eclipse.iot.tiaki.cli.TestUtils;
import org.eclipse.iot.tiaki.cli.exception.ParsingException;
import org.eclipse.iot.tiaki.cli.parser.DefaultOptionParser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class GeneralParserTest {

	@Test (expected = ParsingException.class)
	public void testUnknownArguments () throws Exception {

		String[] args = TestUtils.getArgs("-p");

		DefaultOptionParser parser = new DefaultOptionParser();
		try
		{
			parser.parse( args );
		}
		catch (Exception e)
		{
			assertTrue(e instanceof ParsingException);
			assertEquals("invalid option -- 'p'", e.getMessage());
			throw e;
		}
	}

	@Test (expected = ParsingException.class)
	public void testMissingArguments () throws Exception {

		// The -i argument requires also the -d and -s
		String[] args = TestUtils.getArgs( "-i" );

		DefaultOptionParser parser = new DefaultOptionParser();
		try
		{
			parser.parse( args );
		}
		catch (Exception e)
		{
			assertTrue(e instanceof ParsingException);
//			assertEquals(e.getMessage(), "Missing required option(s) [s/service, d/domain]");
			throw e;
		}
	}

}
