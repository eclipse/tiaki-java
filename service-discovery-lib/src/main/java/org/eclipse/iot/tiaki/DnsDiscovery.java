/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki;

import java.util.Set;
import org.eclipse.iot.tiaki.domain.CertRecord;
import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.DnsCertPrefix;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.domain.TextRecord;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;

/**
 * Define a set of facilities to discover Services registered on DNS. This interface can be
 * considered as a DAO for DNS Service Discovery, using DNS as Data Source.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6763">DNS-SD</a>
 */
public interface DnsDiscovery extends DnsSecChecker
{

	/**
	 * List the Service Types under the provided domain.
	 *
	 * @param browsingDomain A <code>Fqdn</code> referring the browsing domain
	 * @param secValidation  <code>true</code> iff DNSSEC trust chain has to be validated
	 *
     * @return A set of <code>String</code> referring the registered Service Types
	 *
     * @throws LookupException        In case of any unrecoverable error during the lookup process.
	 * @throws ConfigurationException In case of wrong/faulty static and/or runtime configuration.
	 */
	Set<String> listServiceTypes ( Fqdn browsingDomain, boolean secValidation )
                    throws LookupException, ConfigurationException;

	/**
	 * List the Service Instances under the provided domain, of the defined <i>type</i>.
	 *
	 * @param browsingDomain A <code>Fqdn</code> referring the browsing domain
	 * @param type           A compound data structure identifying the Service Type
     * @param secValidation  <code>true</code> iff DNSSEC trust chain has to be validated
	 *
     * @return A set of <code>ServiceInstance</code> objects
     *
	 * @throws LookupException        In case of any unrecoverable error during the lookup process.
	 * @throws ConfigurationException In case of wrong/faulty static and/or runtime configuration.
	 */
	Set<ServiceInstance> listServiceInstances ( Fqdn browsingDomain, CompoundLabel type, boolean secValidation )
                            throws LookupException, ConfigurationException;

	/**
	 * List the Text Resource Records under the provided domain for the specified <i>label</i>.
	 *
	 * @param browsingDomain A <code>Fqdn</code> referring the browsing domain
	 * @param label          A <code>String</code> identifying the label to be looked up
     * @param secValidation  <code>true</code> iff DNSSEC trust chain has to be validated
     *
	 * @return A set of <code>ServiceInstance</code> objects
	 *
     * @throws LookupException        In case of any unrecoverable error during the lookup process.
	 * @throws ConfigurationException In case of wrong/faulty static and/or runtime configuration.
	 */
	Set<TextRecord> listTextRecords ( Fqdn browsingDomain, String label, boolean secValidation )
                        throws LookupException, ConfigurationException;

	/**
	 * List the TLSA Resource Records under the provided domain for the specified <i>label</i>.
	 *
	 * @param browsingDomain A <code>Fqdn</code> referring the browsing domain
	 * @param tlsaPrefix          An object which provides the TLSA record prefix based on port and protocol
	 * @param secValidation  <code>true</code> iff DNSSEC trust chain has to be validated
     *
     * @return A set of <code>CertRecord</code> objects
	 *
     * @throws LookupException        In case of any unrecoverable error during the lookup process.
	 * @throws ConfigurationException In case of wrong/faulty static and/or runtime configuration.
     *
     * @see <a href="https://tools.ietf.org/html/rfc6698">DNS-Based Authentication of Named Entities (DANE)</a>
	 */
	Set<CertRecord> listTLSARecords ( Fqdn browsingDomain, DnsCertPrefix tlsaPrefix,
                                               boolean secValidation )
                                throws LookupException, ConfigurationException;
}
