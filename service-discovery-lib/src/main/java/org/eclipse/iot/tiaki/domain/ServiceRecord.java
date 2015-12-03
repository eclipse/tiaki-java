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
import org.eclipse.iot.tiaki.commons.Constants;
import org.eclipse.iot.tiaki.utils.DnsUtil;
import org.xbill.DNS.SRVRecord;

/**
 * Class to capture service related information from DNS i.e. SRV record. This class implements
 * {@link Comparable} to sort the service instance related information based on priority.
 *
 * @see <a href="http://en.wikipedia.org/wiki/SRV_record">SRV Resource Record</a>
 * @see <a href="http://tools.ietf.org/html/rfc2782">RFC 2782</a>
  */
public final class ServiceRecord extends DiscoveryRecord
{

    /**
     * Service host.
     */
    private final String host;
    /** Service protocol. */
    private final String proto;
    /**
     * Service port.
     */
    private final int port;
    /**
     * Service overall priority (i.e. with reference to other instances).
     */
    private final int priority;
    /**
     * Service overall weight (i.e. with reference to other instances).
     */
    private final int weight;

    /**
     * Static builder. It wraps out a {@link SRVRecord} by extracting relevant data.
     *
     * @param srvRecord A {@link SRVRecord} instance to be worked out
     * @return An instance of <code>ServiceRecord</code>
     */
    public static ServiceRecord build(SRVRecord srvRecord)
    {
        String proto = "N/A";
        String owner = srvRecord.getName().toString();
        if(owner.contains(Constants.TCP))
            proto = Constants.TCP.replace("_", "").toUpperCase();
        else if(owner.contains(Constants.UDP))
            proto = Constants.UDP.replace("_", "").toUpperCase();

        return new ServiceRecord(srvRecord.getName().toString(),
                                 srvRecord.getTarget().toString(),
                                 proto,
                                 srvRecord.getPort(), srvRecord.getPriority(),
                                 srvRecord.getWeight(), srvRecord.getTTL());
    }

    /**
     * Returns the host
     *
     * @return the host
     */
    public String getHost()
    {
        return this.host;
    }

    /**
     * Returns the port
     *
     * @return the port
     */
    public int getPort()
    {
        return this.port;
    }

    /**
     * Returns the priority
     *
     * @return the priority
     */
    public int getPriority()
    {
        return this.priority;
    }

    /**
     * Returns the weight
     *
     * @return the weight
     */
    public int getWeight()
    {
        return this.weight;
    }

    /**
     * Returns the ttl
     *
     * @return the ttl
     */
    @Override
    public long getTtl()
    {
        return this.ttl;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.host, this.port, this.priority, this.weight, this.ttl);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final ServiceRecord that = (ServiceRecord) obj;

        return compareTo(that) == 0;

    }

    @Override
    public String toString()
    {
        return String.format("%d \"%s\" %s %s:%d", ttl, DnsUtil.extractDnsSdDescription(owner),
                (host.endsWith(".") ? host.substring(0, host.length() - 1) : host), proto, port);
    }

    @Override
    public int compareTo(DiscoveryRecord other)
    {
        if (other instanceof ServiceRecord) {
            ServiceRecord real = (ServiceRecord) other;
            if (this.priority < real.priority) {
                return -1;
            } else if (this.priority > real.priority) {
                return 1;
            } else if (this.weight > real.weight) {
                return -1;
            } else if (this.weight < real.weight) {
                return 1;
            } else if (!this.host.equals(real.host)) {
                return this.host.compareTo(real.host);
            } else if (this.port > real.port) {
                return 1;
            } else if (this.port < real.port) {
                return -1;
            } else if (this.ttl < real.ttl) {
                return -1;
            } else if (this.ttl > real.ttl) {
                return 1;
            }
        } else {
            return this.compareTo(other);
        }

        return 0;
    }

    private ServiceRecord(String host, String proto, int port, int priority, int weight, long ttl)
    {
        super(String.format("%s %s:%d %d %d %d", host, proto, port, priority, weight, ttl), ttl);
        this.host = host;
        this.proto = proto;
        this.port = port;
        this.priority = priority;
        this.weight = weight;
    }

    private ServiceRecord(String owner, String host, String proto, int port, int priority, int weight, long ttl)
    {
        super(owner, String.format("\"%s\" %s %s:%d %d %d %d",
                        DnsUtil.extractDnsSdDescription(owner), host, proto, port, priority, weight, ttl), ttl);
        this.host = host;
        this.proto = proto;
        this.port = port;
        this.priority = priority;
        this.weight = weight;
    }

    @Override
    public String getServiceType()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServiceZone(String dnsLabel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServiceName(String dnsLabel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toDisplay() {
        return String.format("%d SRV %d %d %s:%d", ttl, priority, weight,
                (host.endsWith(".") ? host.substring(0, host.length() - 1) : host), port);
    }

}
