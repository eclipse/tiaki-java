/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.iot.tiaki.commons.LookupContext;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.TextRecord;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import org.jitsi.dnssec.validator.ValidatingResolver;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.RRset;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.ResolverConfig;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

/**
 * A collection of static utility methods to deal with DNS.
 *
 */
public final class DnsUtil
{
    /** DNSSEC response insecure text string. */
    private static final String INSECURE = "insecure";
    /** DNSSEC response chain of trust related text string. */
    private static final String CHAIN_OF_TRUST = "chain of trust";
    /** DNSSEC response no data text string. */
    private static final String NO_DATA = "nodata";
    /** DNSSEC response missing signature text string. */
    private static final String NO_SIGNATURE = "missing signature";
    /** DNSSEC response missing DNSSKEY text string. */
    private static final String MISSING_KEY = "missing dnskey rrset";
    /** DNSSEC response NSEC3 related text string. */
    private static final String NSEC3_NO_DS = "nsec3s proved no ds";

    /** Regex to extract the description from a DNS-SD QName */
    private static final String DNS_SD_DESC_EXT_PATTERN = ".(_[a-z-]+.){2}.+";

    /** Translation table for double bytes UTF-8 characters */
    private static final Map<String, String> decodingTable;

    // TODO comprehensive escaping procedure
    // static fill up: UTF-8 double bytes (decimal) sequences to Unicode characters
    static {
        decodingTable = new HashMap<>();
        decodingTable.put("195160", "à");
        decodingTable.put("195161", "á");
        decodingTable.put("195162", "â");
        decodingTable.put("195163", "ã");
        decodingTable.put("195164", "ä");

        decodingTable.put("195166", "æ");
        decodingTable.put("195167", "ç");

        decodingTable.put("195168", "è");
        decodingTable.put("195169", "é");
        decodingTable.put("195170", "ê");
        decodingTable.put("195171", "ë");
        decodingTable.put("195172", "ì");
        decodingTable.put("195173", "í");
        decodingTable.put("195174", "î");
        decodingTable.put("195175", "ï");
        decodingTable.put("195177", "ñ");
        decodingTable.put("195178", "ò");
        decodingTable.put("195179", "ó");
        decodingTable.put("195180", "ô");
        decodingTable.put("195181", "õ");
        decodingTable.put("195182", "ö");
        decodingTable.put("195184", "ø");
        decodingTable.put("195185", "œ");
        decodingTable.put("195186", "ù");
        decodingTable.put("195187", "ú");
        decodingTable.put("195188", "û");
        decodingTable.put("195188", "ü");

        decodingTable.put("195132", "Ä");
        decodingTable.put("195150", "Ö");
        decodingTable.put("195156", "Ü");
        decodingTable.put("195159", "ß");

        decodingTable.put("226128153", "'");

        decodingTable.put("032", " ");
    }

    /**
     * Instantiate a DNS <code>Resolver</code> by the provided Server. In case of DNSSEC validation
     * is needed, a <code>ValidatingResolver</code> is instantiated.
     *
     * @param dnsSec <code>true</code> iff DNSSEC is enabled
     * @param trustAnchor Public cryptographic to validate against
     * @param server Server to use as DNS resolver
     *
     * @return An instance of <code>Resolver</code>
     *
     * @throws ConfigurationException
     *      Exceptional circumstances in which <code>Resolver</code> cannot be created.
     */
    public static Resolver getResolver(boolean dnsSec, String trustAnchor, String server)
                            throws ConfigurationException
    {
        Resolver resolver = instantiateResolver(dnsSec, trustAnchor, server);
        if (resolver == null) {
            throw new ConfigurationException(String.format("Unable to retrieve a Resolver from [%s]", server));
        }

        return resolver;
    }

    /**
     * Instantiate a set of default DNS <code>Resolver</code> by the provided Server. In case of
     * DNSSEC validation is needed, <code>ValidatingResolver</code> will be instantiated.
     *
     * @param dnsSec <code>true</code> iff DNSSEC is enabled
     * @param trustAnchor Public cryptographic to validate against
     *
     * @return A list of default <code>Resolver</code>
     *
     * @throws ConfigurationException
     *      Exceptional circumstances in which no default <code>Resolver</code> can be created.
     */
    public static Map<String, Resolver> getResolvers(boolean dnsSec, String trustAnchor)
                                            throws ConfigurationException
    {
        String[] servers = ResolverConfig.getCurrentConfig().servers();
        Map<String, Resolver> resolvers = new LinkedHashMap<>(servers.length);
        for (String server : servers) {
            Resolver resolver = instantiateResolver(dnsSec, trustAnchor, server);
            if (resolver != null) {
                resolvers.put(server, resolver);
            }
        }

        if (resolvers.isEmpty()) {
            throw new ConfigurationException("Unable to retrieve Default Resolvers");
        }

        return resolvers;
    }

    /**
     * Validate the DNSSEC trust chain against the provided domain name (i.e. <code>Fqdn</code>).
     *
     * @param name A <code>Fqdn</code> representing the validating domain
     * @param resolver A DNS <code>Resovler</code> to be used in this validation
     * @param rType An integer representing the record type
     *
     * @return <code>true</code> iff the DNSSEC is valid
     *
     * @throws LookupException
     *      Containing the specific <code>StatusCode</code> defining the error that has been raised.
     */
    public static boolean checkDnsSec(Fqdn name, Resolver resolver, int rType)
                            throws LookupException
    {
        try {
            ValidatingResolver validating = (ValidatingResolver) resolver;
            Record toValidate = Record.newRecord(Name.fromConstantString(name.fqdn()), rType, DClass.IN);
            Message dnsResponse = validating.send(Message.newQuery(toValidate));
            RRset[] rrSets = dnsResponse.getSectionRRsets(Section.ADDITIONAL);
            StringBuilder reason = new StringBuilder("");
            for (RRset rrset : rrSets) {
                if (rrset.getName().equals(Name.root) && rrset.getType() == Type.TXT
                        && rrset.getDClass() == ValidatingResolver.VALIDATION_REASON_QCLASS) {
                    reason.append(TextRecord.build((TXTRecord) rrset.first()).getRData());
                }
            }
            StatusCode outcome = StatusCode.SUCCESSFUL_OPERATION;
            if (dnsResponse.getRcode() == Rcode.SERVFAIL) {
                if (reason.toString().toLowerCase().contains(CHAIN_OF_TRUST)
                        || reason.toString().toLowerCase().contains(INSECURE)) {
                    outcome = StatusCode.RESOURCE_INSECURE_ERROR;
                } else if (reason.toString().toLowerCase().contains(NO_DATA)) {
                    outcome = StatusCode.NETWORK_ERROR;
                } else if (reason.toString().toLowerCase().contains(NO_SIGNATURE)
                        || reason.toString().toLowerCase().contains(MISSING_KEY)) {
                    outcome = StatusCode.RESOLUTION_NAME_ERROR;
                }
            } else if (dnsResponse.getRcode() == Rcode.NXDOMAIN) {
                if (reason.toString().toLowerCase().contains(NSEC3_NO_DS)) {
                    outcome = StatusCode.RESOURCE_INSECURE_ERROR;
                } else {
                    outcome = StatusCode.RESOLUTION_NAME_ERROR;
                }
            } else if (dnsResponse.getRcode() == Rcode.NOERROR
                    && !dnsResponse.getHeader().getFlag(Flags.AD)) {
                outcome = StatusCode.RESOURCE_INSECURE_ERROR;
            }

            if (outcome != StatusCode.SUCCESSFUL_OPERATION) {
                throw ExceptionsUtil.build(outcome,
                        "DNSSEC Validation Failed",
                        new LinkedHashMap<String, StatusCode>());
            }
        } catch (IOException e) {
            // it might be a transient error network: retry with next Resolver
            return false;
        }

        return true;
    }

    /**
     * Validate the DNS <code>Lookup</code>, catching any transient or blocking issue.
     *
     * @param lookup A <code>Lookup</code> used to pull Resource Records
     *
     * @return A <code>StatusCode</code> with the check outcome
     *
     * @throws LookupException
     *      Containing the specific <code>StatusCode</code> defining the error that has been raised.
     */
    public static StatusCode checkLookupStatus(Lookup lookup)
            throws LookupException
    {
        StatusCode outcome = null;
        if (lookup.getResult() == Lookup.TRY_AGAIN) {
            outcome = StatusCode.NETWORK_ERROR;
        } else if (lookup.getResult() == Lookup.UNRECOVERABLE) {
            outcome = StatusCode.SERVER_ERROR;
        } else if (lookup.getResult() == Lookup.HOST_NOT_FOUND) {
            // Domain Name not found
            outcome = StatusCode.RESOLUTION_NAME_ERROR;
        } else if (lookup.getResult() == Lookup.TYPE_NOT_FOUND) {
            // RR set not found
            outcome = StatusCode.RESOLUTION_RR_TYPE_ERROR;
        } else {
            outcome = StatusCode.SUCCESSFUL_OPERATION;
        }

        return outcome;
    }

    /**
     * Instantiate a DNS <code>Lookup</code> object.
     *
     * @param domainName A domain name to lookup
     * @param resolver A <code>Resolver</code> to be used for lookup
     * @param rrType The Resource Record <code>Type</code>
     * @param cache The Resource Record <code>Cache</code>
     *
     * @return An instance of <code>Lookup</code>
     *
     * @throws LookupException
     *      Containing the specific <code>StatusCode</code> defining the error that has been raised.
     */
    public static Lookup instantiateLookup(String domainName, Resolver resolver, int rrType, Cache cache)
                    throws LookupException
    {
        Lookup lookup = null;
        try {
            lookup = new Lookup(domainName, rrType);
            lookup.setResolver(resolver);
            lookup.setCache(cache);
        } catch (TextParseException ex) {
            throw new LookupException(StatusCode.RESOURCE_LOOKUP_ERROR, String.format("Unable to crea a Lookup for [%s]",
                    domainName));
        }

        return lookup;
    }

    /**
     * Private helper to instantiate a DNS <code>Resolver</code> by the provided Server.
     *
     * @param dnsSec <code>true</code> iff DNSSEC is enabled
     * @param trustAnchor Public cryptographic to validate against
     * @param server Server to use as DNS resolver
     *
     * @return <code>null</code> in case the <code>Resolver</code> cannot be instantiated
     */
    private static Resolver instantiateResolver(boolean dnsSec, String trustAnchor, String server)
    {
        try {
            Resolver resolver = new SimpleResolver(server);
            if (!dnsSec) {
                return resolver;
            }

            ValidatingResolver validating = new ValidatingResolver(resolver);
            validating.loadTrustAnchors(new ByteArrayInputStream(trustAnchor.getBytes(StandardCharsets.UTF_8)));

            return validating;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Extract service names from input pointer records.
     *
     * @param records Pointer records having the names in their RData
     *
     * @return A list of extracted names
     */
    public static Set<String> extractNamesFromRecords(Record[] records)
    {
        Set<String> serviceTypeNames = new HashSet<>();
        for (Record record : records) {
            serviceTypeNames.add(RDataUtil.getServiceTypeRData(record.rdataToString()));
        }

        return serviceTypeNames;
    }

    /**
     * Selects the pointer records to browse in order to retrieve the instances.
     *
     * @param compType A composite <code>CompoundLabel</code>
     * @param ptrs Retrieved pointer records
     *
     * @return A list of selected pointer records from the <code>CompoundLabel</code>
     */
    public static Set<String> filterByType(String compType, Set<String> ptrs)
    {
        Set<String> filtered = new TreeSet<>();
        for (String ptr : ptrs) {
            if (ptr.contains(compType)) {
                filtered.add(ptr);
            }
        }

        return filtered;
    }

    /**
     * Create a Lookup Context to be passed over the nested calls.
     *
     * @param name   A browsing domain
     * @param prefix The prefix label to be used
     * @param label  Specific DNS label
     * @param type   Specific DNS-SD type
     * @param rrType DNS Resource Record Type
     * @param sec    <code>true</code> iff DNSSEC validation is needed
     *
     * @return A <code>LookupContext</code> created accordingly
     */
    // TODO RecordsContainer might be handled by the Context
    public static LookupContext context(Fqdn name, String prefix, String label, String type,
                                        int rrType, boolean sec)
    {
        LookupContext ctx = new LookupContext();
        ctx.setDomainName(name);
        ctx.setPrefix(prefix);
        ctx.setLabel(label);
        ctx.setType(type);
        ctx.setRrType(rrType);
        ctx.setSecure(sec);

        return ctx;
    }

    /**
     * Extracts an unescaped description from a DNS-SD QName
     *
     * @param qname     Domain name to be used for this extraction process
     * @return      Unescaped DNS-SD description
     */
    public static String extractDnsSdDescription(String qname)
    {
        String escaped = qname.replaceAll(DNS_SD_DESC_EXT_PATTERN, "").
                                replaceAll("\\\\", "").
                                replaceAll("032", " ");
        for(String key: decodingTable.keySet())
            escaped = escaped.replaceAll(key, decodingTable.get(key));

        return escaped;
    }

    private DnsUtil()
    {
        throw new AssertionError(String.format("No instances of %s for you!", this.getClass().getName()));
    }

}
