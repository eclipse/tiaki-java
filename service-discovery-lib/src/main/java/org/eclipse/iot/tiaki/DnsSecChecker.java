/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki;

import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;

/**
 * Abstraction of DNSSEC-related facilities to deal with cryptographic secure DNS providers.
 * 
 */
public interface DnsSecChecker 
{

	/**
	 * Check whether the addressed DNS is secured by DNSSEC.
	 * 
	 * @see <a href="https://tools.ietf.org/html/rfc4035">DNSSEC</a>
	 * @param name  Fully Qualified Domain Name
	 * 
     * @return <code>true</code> iff the DNS is secured by DNSSEC
	 * 
     * @throws LookupException
	 *          In case DNSSEC is broken
	 * @throws ConfigurationException
	 *          Unproper setup for DNS Lookups
	 */
	boolean isDnsSecValid ( Fqdn name ) throws LookupException, ConfigurationException;

}
