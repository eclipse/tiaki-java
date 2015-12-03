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
import org.xbill.DNS.Name;
import org.xbill.DNS.TextParseException;

/**
 * DNS RData Util. It deals with any specific text extraction or validation policy of RData.
 *
 * @author tjmurphy
 * @version 1.0
 * @since Mar 30, 2015
 */
public final class RDataUtil
{

	/**
     * Extracts a service type label (pointing to a DNS-SD name) from the pointer RData.
     *
     * @param rData     Pointer record RData
     *
     * @return  A <code>String</code> containing the service type label.
     */
	public static String getDnsLabelFromRData(String rData)
    {
		if(rData == null || rData.trim().isEmpty()) {
			throw new IllegalArgumentException( "rData cannot be null, empty, or blank" );
		}

		if(rData.contains( Constants.LABEL )) {
			return rData.substring(0, rData.indexOf(Constants.LABEL));
		}

		Name nameInRData = null;
		try{
			nameInRData = new Name( rData.trim() );
		} catch(TextParseException tpe){
			throw new IllegalArgumentException( "rData must be valid domain name" );
		}

		if(nameInRData.labels() < 2) {
			throw new IllegalArgumentException( "rData does not have enough labels to return dns label for a service type" );
		}

		String transportProtocolLabel = nameInRData.getLabelString( 1 );
		if(transportProtocolLabel.equals( Constants.TCP ) || transportProtocolLabel.equals( Constants.UDP )) {
			return nameInRData.getLabelString( 0 ) + Constants.DNS_LABEL_DELIMITER + transportProtocolLabel;
		}

		throw new IllegalArgumentException( "Could not extract DNS Label from rData" );
	}

    /**
     * Extracts a service type name (pointed by a DNS-SD label) from the pointer RData.
     *
     * @param rData     Pointer record RData
     *
     * @return  A <code>String</code> containing the service type name.
     */
	public static String getServiceTypeNameFromRData(String rData)
    {
		if(rData == null || rData.trim().isEmpty()) {
			throw new IllegalArgumentException( "rData cannot be null, empty, or blank" );
		}

		if(rData.contains( Constants.NAME )) {
			String serviceTypeName = rData.substring(0, rData.indexOf(Constants.NAME)).trim();
			if(!serviceTypeName.isEmpty()) {
				return serviceTypeName;
			}
		}
		throw new IllegalArgumentException( "Could not extract Service Type name from rData" );
	}

     /**
     * Extracts a service type name (pointed by a DNS-SD label) from the pointer RData.
     *
     * @param rData     Pointer record RData
     *
     * @return  A <code>String</code> containing the service type name.
     */
	public static String getServiceTypeRData(String rData)
    {
		if(rData == null || rData.trim().isEmpty()) {
			throw new IllegalArgumentException( "rData cannot be null, empty, or blank" );
		}
		String[] splitted = rData.split("\\.");
        if(splitted.length < 3)
            throw new IllegalArgumentException( "Invalid RData for a service type PTR: " +rData );

        return splitted[0].replaceFirst("_", "");
	}



    private RDataUtil()
    {
        throw new AssertionError(String.format("No instances of %s for you!", this.getClass().getCanonicalName()));
    }

}
