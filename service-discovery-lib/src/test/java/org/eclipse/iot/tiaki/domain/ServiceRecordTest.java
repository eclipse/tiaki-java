/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.xbill.DNS.TextParseException;

public class ServiceRecordTest {

	@Test
	public void testEquals () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		Assert.assertTrue( "Both services should be equal", svc1.equals( svc2 ) );
	}


	@Test
	public void testCompareToDifferentPriority () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 1, 10, 3600 ) );
		Assert.assertTrue( "Incorrect priority comparison", svc1.compareTo( svc2 ) < 1 );
		Assert.assertTrue( "Incorrect priority comparison", svc2.compareTo( svc1 ) > 0 );
	}


	@Test
	public void testCompareToSamePriorityDifferentWeight () throws TextParseException {

		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 20, 3600 ) );
		Assert.assertTrue( "Incorrect weight comparison", svc1.compareTo( svc2 ) > 0 );
		Assert.assertTrue( "Incorrect weight comparison", svc2.compareTo( svc1 ) < 0 );
	}


	@Test
	public void testCompareSamePriorityWeightAndDifferentHost () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc3 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "eastcoastcoap._coap._udp.47zlpxulsrha.1.iotverisign.", 1800, 0, 10, 3600 ) );
		List<ServiceRecord> list = new ArrayList<ServiceRecord>( 3 );
		list.add( svc2 );
		list.add( svc3 );
		list.add( svc1 );

		Collections.sort( list );
		Assert.assertTrue( "First element in the list should be svc1", list.get( 0 ).equals( svc1 ) );
		Assert.assertTrue( "Second element in the list should be svc2", list.get( 1 ).equals( svc2 ) );
		Assert.assertTrue( "Third element in the list should be svc3", list.get( 2 ).equals( svc3 ) );
	}


	@Test
	public void testCompareSamePriorityWeightHostAndDifferentPort () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host1.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host2.com.", 1801, 0, 10, 3600 ) );
		ServiceRecord svc3 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host3.com.", 1802, 0, 10, 3600 ) );
		List<ServiceRecord> list = new ArrayList<ServiceRecord>( 3 );
		list.add( svc2 );
		list.add( svc3 );
		list.add( svc1 );

		Collections.sort( list );
		Assert.assertTrue( "First element in the list should be svc1", list.get( 0 ).equals( svc1 ) );
		Assert.assertTrue( "Second element in the list should be svc2", list.get( 1 ).equals( svc2 ) );
		Assert.assertTrue( "Third element in the list should be svc3", list.get( 2 ).equals( svc3 ) );
	}



}
