/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import org.xbill.DNS.TLSARecord;
import org.xbill.DNS.utils.base16;


public class CertRecord extends DiscoveryRecord
{

	private TLSARecord tlsaRecord;

	public CertRecord ( TLSARecord tlsaRecord )
    {
		super( tlsaRecord.getName().toString(),
               base16.toString( tlsaRecord.getCertificateAssociationData() ),
               tlsaRecord.getTTL());
		this.tlsaRecord = tlsaRecord;
	}


	@Override
	public String getServiceType ()
    {
		throw new UnsupportedOperationException( "Not supported by this record type" );
	}


	@Override
	public String getServiceZone ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported by this record type" );
	}


	@Override
	public String getServiceName ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported by this record type" );
	}

    /**
     * Retrieve the Certificate Usage.
     *
     * @return  An <code>int</code> representing this TLSA usage
     */
    public int certUsage() { return tlsaRecord.getCertificateUsage(); }

    /**
     * Retrieve the Certificate Matching Type.
     *
     * @return  An <code>int</code> representing this TLSA matching type
     */
    public int certMatchingType() { return tlsaRecord.getMatchingType(); }

    /**
     * Retrieve the Certificate Selector.
     *
     * @return  An <code>int</code> representing this TLSA selector
     */
    public int certSelector() { return tlsaRecord.getSelector(); }


	@Override
	public String toString()
    {
		return String.format("%d %s", ttl, rData);
	}

    @Override
	public String toDisplay()
    {
		return String.format("%d %s %d %d %d %s", ttl, "TLSA",
                                certMatchingType(), certUsage(), certSelector(), rData);
	}

}
