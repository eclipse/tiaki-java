
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
 * Class to capture service instance related information. This includes both the SRV and TXT record information. It implements
 * {@link Comparable} to sort services based on priority of {@link ServiceRecord}.
 *
 */
public final class ServiceInstance extends DiscoveryRecord
{

	/** A <code>String</code> defining the Service Type of this Service Instance. */
	private final String serviceType;
	/** The <code>ServiceRecord</code> of this Service Instance. */
	private final ServiceRecord serviceRecord;
	/** The <code>TextRecord</code> of this Service Instance. */
	private final TextRecord textRecord;


	/**
	 * Three arguments constructor building up a Service Instance by its own type, record and textual information.
	 *
	 * @param serviceType
	 *        A <code>String</code> defining the Service Type of this Service Instance
	 * @param serviceRecord
	 *        The <code>ServiceRecord</code> of this Service Instance
	 * @param textRecord
	 *        The <code>TextRecord</code> of this Service Instance
	 */
	public ServiceInstance ( String serviceType, ServiceRecord serviceRecord, TextRecord textRecord )
    {
		super( String.format( "%s %s %d %d %d %d %s", serviceType, serviceRecord.getHost(), serviceRecord.getPort(),
				serviceRecord.getPriority(), serviceRecord.getWeight(), serviceRecord.getTtl(), textRecord.getRData() ),
				serviceRecord.getTtl() );
		this.serviceType = serviceType;
		this.serviceRecord = serviceRecord;
		this.textRecord = textRecord;
	}


	/**
	 * Returns the serviceRecord
	 *
	 * @return the serviceRecord
	 */
	public ServiceRecord getServiceRecord ()
    {
		return this.serviceRecord;
	}


	/**
	 * Returns the textRecord
	 *
	 * @return the textRecord
	 */
	public TextRecord getTextRecord ()
    {
		return this.textRecord;
	}


	@Override
	public int hashCode ()
    {
		return Objects.hash( this.serviceType, this.serviceRecord, this.textRecord );
	}


	@Override
	public boolean equals ( Object obj )
    {
		if ( obj == null ) {
			return false;
		}

		if ( this.getClass() != obj.getClass() ) {
			return false;
		}

		final ServiceInstance that = (ServiceInstance) obj;

		return Objects.equals( this.serviceType, that.getServiceType() )
				&& Objects.equals( this.serviceRecord, that.getServiceRecord() )
				&& Objects.equals( this.textRecord, that.getTextRecord() );
	}


	@Override
	public int compareTo ( DiscoveryRecord other )
    {
		if ( other instanceof ServiceInstance ) {
			ServiceInstance real = (ServiceInstance) other;
			if ( this.serviceRecord != null && real.serviceRecord != null ) {
				return this.serviceRecord.compareTo( real.serviceRecord );
			}
		} else {
			return super.compareTo( other );
		}

		return 0;
	}


	@Override
	public String toString ()
    {
		StringBuilder sb = new StringBuilder();
		sb.append( this.serviceRecord );
		if ( this.textRecord != null ) {
			sb.append( " " )
              .append( this.textRecord.getRData() );
		}

		return sb.toString();
	}


	@Override
	public String getServiceType ()
    {
		return this.serviceType;
	}


	@Override
	public String getServiceZone ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported yet." );
	}


	@Override
	public String getServiceName ( String dnsLabel )
    {
		throw new UnsupportedOperationException( "Not supported yet." );
	}

    @Override
    public String toDisplay()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
