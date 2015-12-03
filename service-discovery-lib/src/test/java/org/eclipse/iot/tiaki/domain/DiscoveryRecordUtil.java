/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import org.xbill.DNS.DClass;
import org.xbill.DNS.Name;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;

public class DiscoveryRecordUtil {
	
	public static SRVRecord createSrvRecord ( String host, int port, int priority, int weight, long ttl )
			throws TextParseException {
		return new SRVRecord( Name.fromString( "example.com." ), DClass.IN, ttl, priority, weight, port, Name.fromString( host ) );
	}


	public static TXTRecord createTxtRecord ( String rdata, long ttl ) throws TextParseException {
		return new TXTRecord( Name.fromString( "example.com." ), DClass.IN, ttl, rdata );
	}
}
