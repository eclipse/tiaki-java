/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.iot.tiaki.cli.TestUtils;
import org.eclipse.iot.tiaki.cli.exception.ParsingException;
import org.eclipse.iot.tiaki.cli.parser.DefaultOptionParser;
import org.junit.Test;

public class ListTxtRecordParsingTest {

	@Test (expected = ParsingException.class)
	public void testRequiredArguments() throws Exception {

		String[] args = TestUtils.getArgs("-t TEXT_LABEL");

		DefaultOptionParser parser = new DefaultOptionParser();
		try
		{
			parser.parse( args );
		}
		catch (Exception e)
		{
			assertTrue(e instanceof ParsingException);
			assertEquals("Missing required option(s) [d/domain]", e.getMessage());
			throw e;
		}
	}
}
