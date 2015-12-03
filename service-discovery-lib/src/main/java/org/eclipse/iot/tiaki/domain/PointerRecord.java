/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import org.eclipse.iot.tiaki.commons.Constants;
import org.eclipse.iot.tiaki.utils.RDataUtil;
import org.xbill.DNS.PTRRecord;

/**
 * Class to parse {@link PTRRecord} and provide the relevant information for service lookup.
 *
 * @see <a href="http://en.wikipedia.org/wiki/List_of_DNS_record_types">Resource Records</a>
 * @see <a href="http://tools.ietf.org/html/rfc1035#page-12">RFC 1035</a>
  */
// TODO compute and store (extracting methods)
public class PointerRecord extends DiscoveryRecord
{

    /**
     * Static builder. It wraps out a {@link PTRRecord} by extracting relevant data.
     *
     * @param ptrRecord A {@link PTRRecord} instance to be worked out
     * @return An instance of <code>PointerRecor</code>
     */
    public final static PointerRecord build(PTRRecord ptrRecord)
    {
        return new PointerRecord(ptrRecord);
    }

    /**
     * Extract a DNS Label from RData, if any. It returns <code>null</code> in case the extraction
     * is unsuccessful.
     *
     * @return <code>null</code> in case DNS Label is not contained, the Label itself otherwise
     */
    public String getDnsLabel()
    {
        return RDataUtil.getDnsLabelFromRData( this.rData );
    }

    /**
     * Extract a Service type from RData, if any. It returns <code>null</code> in case the
     * extraction is unsuccessful.
     *
     * @return <code>null</code> in case the Service Type is not contained, the Type itself
     * otherwise
     */
    @Override
    public String getServiceType()
    {
        return this.rData.contains(Constants.DNS_LABEL_DELIMITER) ? this.rData.substring(1,
                this.rData.indexOf(Constants.DNS_LABEL_DELIMITER)) : null;
    }

    /**
     * Extract a Service Zone from RData, if any. It returns <code>null</code> in case the
     * extraction is unsuccessful.
     *
     * @return <code>null</code> in case the Service Zone is not contained, the Zone itself
     * otherwise
     */
    @Override
    public String getServiceZone(String dnsLabel)
    {
        return this.rData.contains(dnsLabel) ? this.rData.substring(dnsLabel.length() + 1) : null;
    }

    /**
     * Extract a Service Name from RData, if any. It returns <code>null</code> in case the
     * extraction is unsuccessful.
     *
     * @return <code>null</code> in case the Service Name is not contained, the Name itself
     * otherwise
     */
    @Override
    public String getServiceName(String dnsLabel)
    {
        return this.rData.contains(dnsLabel) ? this.rData : null;
    }

    private PointerRecord(PTRRecord ptrRecord)
    {
        super(ptrRecord.getName().toString(),
                ptrRecord.rdataToString(), ptrRecord.getTTL());
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    @Override
    public String toDisplay()
    {
        return String.format("%d PTR %s", ttl, rData);
    }

}
