/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import java.util.Set;
import org.xbill.DNS.TXTRecord;

/**
 * Class to capture TXT record information from DNS.
 *
 * @see <a href="http://en.wikipedia.org/wiki/TXT_Record">TXT Resource Record</a>
  */
public final class TextRecord extends DiscoveryRecord
{

    /**
     * Takes in input a <code>Set</code> of strings and produces a value corresponding to the max TTL
     *
     * @param txtsRec  A <code>Set</code> of strings
     *
     * @return  A properly built compound <code>TextRecord</code>
     */
    public final static TextRecord build( Set<TextRecord> txtsRec )
    {
        StringBuilder flattened = new StringBuilder();
        String owner = null;
        long max = Long.MIN_VALUE;
        for(TextRecord txt: txtsRec) {
            flattened.append(txt.getRData());
            if(txt.getTtl() > max)
                max = txt.getTtl();
            if(owner == null)
                owner = txt.getOwner();
        }

        return new TextRecord(owner, flattened.toString(), max);
    }

	/**
	 * Static builder. It wraps out a {@link TXTRecord} by extracting relevant data.
	 *
	 * @param txtRec
	 *        A {@link TXTRecord} instance to be worked out
	 * @return An instance of <code>TextRecord</code>
	 */
	public final static TextRecord build ( TXTRecord txtRec )
    {
		return new TextRecord( txtRec.rdataToString(), txtRec.getTTL() );
	}


	private TextRecord ( String txtData, long ttl )
    {
		super( txtData, ttl );
	}

    private TextRecord ( String owner, String txtData, long ttl )
    {
		super( owner, txtData, ttl );
	}

	@Override
	public String getServiceType ()
    {
		throw new UnsupportedOperationException( "Not supported by TextRecord" );
	}


	@Override
	public String getServiceZone ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported by TextRecord" );
	}


	@Override
	public String getServiceName ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported by TextRecord" );
	}

	@Override
	public String toString()
	{
		return super.toString();
	}

    @Override
    public String toDisplay()
    {
        return String.format("%d TXT %s", ttl, rData);
    }

}
