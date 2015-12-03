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

/**
 * Created by tjmurphy on 6/5/15.
 */
public class DnsCertPrefixTest {

	private final String DEFAULT_VALUES_PREFIX = "_0._tcp";

	/////////////////////////////////////////////
	/////////////    Default values   ///////////
	/////////////////////////////////////////////


	@Test
	public void defaultValuesDefaultConstructor () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix();
		Assert.assertTrue( tlsaPrefix.toString().equals( DEFAULT_VALUES_PREFIX ) );
	}


	@Test
	public void defaultValuesNullInit () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( null );
		Assert.assertTrue( tlsaPrefix.toString().equals( DEFAULT_VALUES_PREFIX ) );
	}


	@Test
	public void defaultValuesEmptyInit () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "" );
		Assert.assertTrue( tlsaPrefix.toString().equals( DEFAULT_VALUES_PREFIX ) );
	}


	@Test
	public void defaultValuesWhitespaceInit () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "         " );
		Assert.assertTrue( tlsaPrefix.toString().equals( DEFAULT_VALUES_PREFIX ) );
	}

	/////////////////////////////////////////////
	/////////////    Default port    ////////////
	/////////////////////////////////////////////


	@Test
	public void defaultPort () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( ":udp" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_0._udp" ) );
	}


	@Test
	public void defaultPortWithWhitespace () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "       :udp" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_0._udp" ) );
	}

	/////////////////////////////////////////////
	///////////    Default protocol    //////////
	/////////////////////////////////////////////


	@Test
	public void defaultProtocol () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "123" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_123._tcp" ) );
	}


	@Test
	public void defaultProtocolWithDelimiter () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "123:" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_123._tcp" ) );
	}


	@Test
	public void defaultProtocolZeroPort () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "0" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_0._tcp" ) );
	}


	@Test
	public void defaultProtocol65534Port () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "65534" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_65534._tcp" ) );
	}


	@Test
	public void defaultProtocol65535Port () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "65535" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_65535._tcp" ) );
	}


	@Test(expected = IllegalArgumentException.class)
	public void defaultProtocolInvalidPortNegativeValue () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "-1" );
	}


	@Test(expected = IllegalArgumentException.class)
	public void defaultProtocolInvalidPortValueExceedsMax () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "65536" );
	}

	/////////////////////////////////////////////
	///////////    No Default Values    /////////
	/////////////////////////////////////////////


	@Test
	public void giveStringOfDefaultValues () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "0:tcp" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_0._tcp" ) );
	}


	@Test
	public void port1ProtocolTCP () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "1:tcp" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_1._tcp" ) );
	}


	@Test
	public void port1ProtocolUDP () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "1:udp" );
		Assert.assertTrue( tlsaPrefix.toString().equals( "_1._udp" ) );
	}


	@Test(expected = IllegalArgumentException.class)
	public void portNegative1ProtocolUDP () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "-1:udp" );
	}


	@Test(expected = IllegalArgumentException.class)
	public void port65536ProtocolUDP () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "65536:udp" );
	}


	@Test
	public void port65535ProtocolUDP () {
		DnsCertPrefix tlsaPrefix = new DnsCertPrefix( "65535:udp" );
	}

}
