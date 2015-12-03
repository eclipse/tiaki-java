/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import org.junit.Assert;
import org.junit.Test;
import org.xbill.DNS.TextParseException;

public class TextRecordTest {

	@Test
	public void testEqualsObject () throws TextParseException {
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );

		Assert.assertTrue( "Both text records should be equal", txt1.equals( txt2 ) );
	}


	@Test
	public void testNotEqualsObject () throws TextParseException {
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt2", 3600 ) );

		Assert.assertTrue( "Both text records should not be equal", !txt1.equals( txt2 ) );
	}



}
