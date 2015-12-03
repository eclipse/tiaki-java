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

public class ServiceInstanceTest {
	private static String serviceTypeCoap = "coap";
	private static String serviceTypeMqtt = "mqtt";


	@Test
	public void testEquals () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeCoap, svc2, txt2 );
		Assert.assertTrue( "Both service instances should be equal", svcInst1.equals( svcInst2 ) );
	}


	@Test
	public void testNotEquals () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeMqtt, svc2, txt2 );
		Assert.assertTrue( "Both service instances should not be equal", !svcInst1.equals( svcInst2 ) );
	}


	@Test
	public void testCompareToDifferentPriority () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 1, 10, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeCoap, svc2, txt2 );
		Assert.assertTrue( "Incorrect priority comparison", svcInst1.compareTo( svcInst2 ) < 1 );
		Assert.assertTrue( "Incorrect priority comparison", svcInst2.compareTo( svcInst1 ) > 0 );
	}


	@Test
	public void testCompareToSamePriorityDifferentWeight () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 20, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeCoap, svc2, txt2 );
		Assert.assertTrue( "Incorrect priority comparison", svcInst1.compareTo( svcInst2 ) > 0 );
		Assert.assertTrue( "Incorrect priority comparison", svcInst2.compareTo( svcInst1 ) < 0 );
	}


	@Test
	public void testCompareSamePriorityWeightAndDifferentHost () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host1.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host2.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc3 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host3.com.", 1800, 0, 10, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt3 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeCoap, svc2, txt2 );
		ServiceInstance svcInst3 = new ServiceInstance( serviceTypeCoap, svc3, txt3 );
		List<ServiceInstance> list = new ArrayList<ServiceInstance>( 3 );
		list.add( svcInst2 );
		list.add( svcInst3 );
		list.add( svcInst1 );

		Collections.sort( list );
		Assert.assertTrue( "First element in the list should be svcInst1", list.get( 0 ).equals( svcInst1 ) );
		Assert.assertTrue( "Second element in the list should be svcInst2", list.get( 1 ).equals( svcInst2 ) );
		Assert.assertTrue( "Third element in the list should be svcInst3", list.get( 2 ).equals( svcInst3 ) );
	}


	@Test
	public void testCompareSamePriorityWeightHostAndDifferentPort () throws TextParseException {
		ServiceRecord svc1 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1800, 0, 10, 3600 ) );
		ServiceRecord svc2 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1801, 0, 10, 3600 ) );
		ServiceRecord svc3 = ServiceRecord.build( DiscoveryRecordUtil.createSrvRecord( "host.com.", 1802, 0, 10, 3600 ) );
		TextRecord txt1 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt2 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		TextRecord txt3 = TextRecord.build( DiscoveryRecordUtil.createTxtRecord( "txt1", 3600 ) );
		ServiceInstance svcInst1 = new ServiceInstance( serviceTypeCoap, svc1, txt1 );
		ServiceInstance svcInst2 = new ServiceInstance( serviceTypeCoap, svc2, txt2 );
		ServiceInstance svcInst3 = new ServiceInstance( serviceTypeCoap, svc3, txt3 );
		List<ServiceInstance> list = new ArrayList<ServiceInstance>( 3 );
		list.add( svcInst2 );
		list.add( svcInst3 );
		list.add( svcInst1 );

		Collections.sort( list );
		Assert.assertTrue( "First element in the list should be svcInst1", list.get( 0 ).equals( svcInst1 ) );
		Assert.assertTrue( "Second element in the list should be svcInst2", list.get( 1 ).equals( svcInst2 ) );
		Assert.assertTrue( "Third element in the list should be svcInst3", list.get( 2 ).equals( svcInst3 ) );
	}
}
