/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import java.util.Objects;

/**
 * Base Class abstracting the commonalities of Discovery Data. A <code>DiscoveryRecord</code>
 * defines a @{link Comparable} instance.
 *
 */
public abstract class DiscoveryRecord implements Comparable<DiscoveryRecord>
{
    /**
     * A <code>String</code> containing the owner.
     */
    protected final String owner;
    /**
     * A <code>String</code> containing this resource record raw textual data.
     */
    protected final String rData;
    /**
     * Resource record specific TTL.
     */
    protected final long ttl;

    public DiscoveryRecord()
    {
        this("", 0L);
    }

    public DiscoveryRecord(String rData, long ttl)
    {
        this("N/A", rData, ttl);
    }

    public DiscoveryRecord(String owner, String rData, long ttl)
    {
        this.owner = owner;
        this.rData = rData;
        this.ttl = ttl;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public String getRData()
    {
        return this.rData;
    }

    public long getTtl()
    {
        return this.ttl;
    }

    /**
     * According to the specific implementation, it extract a <b>Service Type</b> and return it.
     *
     * @return A <code>String</code> containing a Service Type
     */
    public abstract String getServiceType();

    /**
     * According to the specific implementation, it extract a <b>Service Zone</b> and return it.
     *
     * @param dnsLabel A <code>String</code> defining the DNS Label
     * @return A <code>String</code> containing a Service Zone
     */
    public abstract String getServiceZone(String dnsLabel);

    /**
     * According to the specific implementation, it extract a <b>Service Name</b> and return it.
     *
     * @param dnsLabel A <code>String</code> defining the DNS Label
     * @return A <code>String</code> containing a Service Name
     */
    public abstract String getServiceName(String dnsLabel);

    /**
     * Serialize this Discovery Record into a display format.
     *
     * @return A <code>String</code> with a representable version of this
     * <code>DiscoveryRecord</code>
     */
    public abstract String toDisplay();

    @Override
    public int compareTo(DiscoveryRecord t)
    {
        return this.rData.compareTo(t.getRData());
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.rData);
        hash = 97 * hash + (int) (this.ttl ^ (this.ttl >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiscoveryRecord other = (DiscoveryRecord) obj;
        if (!Objects.equals(this.rData, other.rData)) {
            return false;
        }
        if (this.ttl != other.ttl) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return String.format("%d %s", this.ttl, this.rData);
    }

}
