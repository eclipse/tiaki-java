/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.utils;

import org.eclipse.iot.tiaki.commons.Constants;
import org.junit.Assert;
import org.junit.Test;

public class RDataUtilTest {

	/////////////////////////////////////////////
	/////////////    Positive Tests   ///////////
	/////////////////////////////////////////////

	@Test
	public void getDnsFromLabelFromRDataTest () {
		String testRData = "";
		String nullString = null;

		testRData = "_0._tcp";
		Assert.assertTrue("_0._tcp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._tcp" + Constants.LABEL;
		Assert.assertTrue("_0._tcp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._tcp" + Constants.NAME;
		Assert.assertTrue("_0._tcp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._tcp.asodweasdfj.com";
		Assert.assertTrue("_0._tcp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._udp.asodweasdfj.com";
		Assert.assertTrue("_0._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._udp.asodweasdfj.1.com";
		Assert.assertTrue("_0._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._udp.asodweasdfj.1.aa.23.asasfd.com";
		Assert.assertTrue("_0._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_0._udp.asodweasdfj";
		Assert.assertTrue("_0._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_65535._tcp.asodweasdfj.com";
		Assert.assertTrue("_65535._tcp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_65535._udp.asodweasdfj.com";
		Assert.assertTrue("_65535._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_65535._udp.asodweasdfj.1.com";
		Assert.assertTrue("_65535._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_65535._udp.asodweasdfj.1.aa.23.asasfd.com";
		Assert.assertTrue("_65535._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
		testRData = "_65535._udp.asodweasdfj";
		Assert.assertTrue("_65535._udp".equals( RDataUtil.getDnsLabelFromRData( testRData ) ));
	}

	/////////////////////////////////////////////
	/////////////    Negative Tests   ///////////
	/////////////////////////////////////////////

	@Test(expected = IllegalArgumentException.class)
	public void nullStringTest(){
		RDataUtil.getDnsLabelFromRData( null );
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyStringTest(){
		RDataUtil.getDnsLabelFromRData( "" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void blankStringTest(){
		RDataUtil.getDnsLabelFromRData( "         " );
	}

	@Test(expected = IllegalArgumentException.class)
	public void oneLabelStringTest(){
		RDataUtil.getDnsLabelFromRData( ".com" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void oneLabelTCPStringTest(){
		RDataUtil.getDnsLabelFromRData( "._tcp" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void oneLabelUDPStringTest(){
		RDataUtil.getDnsLabelFromRData( "._udp" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyLabelUDPStringTest(){
		RDataUtil.getDnsLabelFromRData( "     ._udp" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyLabelTLDStringTest(){
		RDataUtil.getDnsLabelFromRData( "     .com" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void validDomainNoDNSLabelTest(){
		RDataUtil.getDnsLabelFromRData( "abc.verisign.com" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void longerValidDomainNoDNSLabelTest(){
		RDataUtil.getDnsLabelFromRData( "1.abc.xyz.verisign.com" );
	}

	/////////////////////////////////////////////
	/////////////    Positive Tests   ///////////
	/////////////////////////////////////////////


	@Test
	public void getServiceTypeNameFromRDataTest () {
		String testRData = "";

		testRData = "cool service type" + Constants.NAME;
		Assert.assertTrue("cool service type".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
		testRData = "mqtt" + Constants.NAME;
		Assert.assertTrue("mqtt".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
		testRData = "mqtt" + Constants.NAME + "                 ";
		Assert.assertTrue("mqtt".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
		testRData = "mqtt" + Constants.NAME + ".asdja898.asd232l.asdflk        ";
		Assert.assertTrue("mqtt".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
		testRData = "something-with-some-hyphens" + Constants.NAME;
		Assert.assertTrue("something-with-some-hyphens".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
		testRData = "iot's awesome service type!" + Constants.NAME;
		Assert.assertTrue("iot's awesome service type!".equals( RDataUtil.getServiceTypeNameFromRData( testRData ) ));
	}

	/////////////////////////////////////////////
	/////////////    Negative Tests   ///////////
	/////////////////////////////////////////////

	@Test(expected = IllegalArgumentException.class)
	public void nullStringSTTest(){
		RDataUtil.getServiceTypeNameFromRData( null );
	}

	@Test(expected = IllegalArgumentException.class)
	public void emptyStringSTTest(){
		RDataUtil.getServiceTypeNameFromRData( "" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void blankStringSTTest(){
		RDataUtil.getServiceTypeNameFromRData( "         " );
	}

	@Test(expected = IllegalArgumentException.class)
	public void nameWithSpacesNoNameLabelTest(){
		RDataUtil.getServiceTypeNameFromRData( "cool service type" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void nameWithHyphensNoNameLabelTest(){
		RDataUtil.getServiceTypeNameFromRData( "something-with-some-hyphens" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void mqttNoNameLabelTest(){
		RDataUtil.getServiceTypeNameFromRData( "mqtt" );
	}

	@Test(expected = IllegalArgumentException.class)
	public void onlyNameLabelTest(){
		RDataUtil.getServiceTypeNameFromRData( Constants.NAME );
	}

	@Test(expected = IllegalArgumentException.class)
	public void onlyNameAndWhitespaceInFrontLabelTest(){
		RDataUtil.getServiceTypeNameFromRData( "         " + Constants.NAME );
	}

}
