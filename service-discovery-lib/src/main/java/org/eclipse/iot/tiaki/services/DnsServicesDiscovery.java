/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.services;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.eclipse.iot.tiaki.DnsDiscovery;
import org.eclipse.iot.tiaki.commons.Configurable;
import org.eclipse.iot.tiaki.commons.Constants;
import org.eclipse.iot.tiaki.commons.LookupContext;
import org.eclipse.iot.tiaki.commons.StatusChangeEvent;
import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.domain.CertRecord;
import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.DnsCertPrefix;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.PointerRecord;
import org.eclipse.iot.tiaki.domain.RecordsContainer;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.domain.ServiceRecord;
import org.eclipse.iot.tiaki.domain.TextRecord;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import org.eclipse.iot.tiaki.utils.DnsUtil;
import org.eclipse.iot.tiaki.utils.ExceptionsUtil;
import org.eclipse.iot.tiaki.utils.FormattingUtil;
import org.eclipse.iot.tiaki.utils.ValidatorUtil;
import org.xbill.DNS.Cache;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.PTRRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TLSARecord;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.Type;

/**
 * Class encapsulating the DNS-SD Service Lookup facilities.
 *
 */
public class DnsServicesDiscovery extends Configurable implements DnsDiscovery
{

    /** Lookup Cache. */
    private Cache anyClassCache;
    /** Thread-owned Errors trace. */
    private ThreadLocal<Map<String, StatusCode>> errorsTrace;
    /** DNS Lookup helper. */
    private ServicesLookupHelper helper;

    public DnsServicesDiscovery() { this(Constants.CACHE_SIZE, Constants.CACHE_TIME_LIMIT); }

    /**
     * Overloaded constructor taking as argument Cache size and TTL.
     *
     * @param cacheSize Unsigned <code>int</code> defining the Cache size
     * @param cacheTTL Unsigned <code>int</code> defining the Cache TTL
     */
    public DnsServicesDiscovery(int cacheSize, int cacheTTL)
    {
        this.anyClassCache = new Cache(DClass.ANY);
        this.anyClassCache.setMaxEntries(cacheSize);
        this.anyClassCache.setMaxNCache(cacheTTL);
        this.helper = this.new ServicesLookupHelper();
        this.errorsTrace = new ThreadLocal<Map<String, StatusCode>>() {
            @Override
            protected Map<String, StatusCode> initialValue() {
                return new LinkedHashMap<>();
            }
        };
    }

    @Override
    public Set<String> listServiceTypes(Fqdn browsingDomain, boolean secValidation)
                        throws LookupException, ConfigurationException
    {
        try {
            ValidatorUtil.isValidDomainName(browsingDomain);
        } catch(IllegalArgumentException exception) {
            throw new LookupException(StatusCode.ILLEGAL_FQDN, browsingDomain.fqdn());
        }
        validatedConf();
        Set<String> result = null;
        try {
            result = new TreeSet<>();
            result.addAll(this.helper.serviceTypes(browsingDomain, secValidation));
            if (result.isEmpty() && !ExceptionsUtil.onlyNameResolutionTrace(this.errorsTrace.get())) {
                throw ExceptionsUtil.build(StatusCode.RESOURCE_LOOKUP_ERROR,
                                           FormattingUtil.unableToResolve(browsingDomain.fqdn()),
                                           errorsTrace.get());
            }
        } catch (LookupException | ConfigurationException exception) {
            throw exception;
        } finally {
            errorsTrace.remove();
        }

        return result;
    }

    @Override
    public Set<ServiceInstance> listServiceInstances(Fqdn browsingDomain, CompoundLabel type, boolean secValidation)
            throws LookupException, ConfigurationException
    {
        try {
            ValidatorUtil.isValidDomainName(browsingDomain);
        } catch(IllegalArgumentException exception) {
            throw new LookupException(StatusCode.ILLEGAL_FQDN, browsingDomain.fqdn());
        }
        ValidatorUtil.isValidLabel(type);
        validatedConf();
        Set<ServiceInstance> result = null;
        try {
            result = new TreeSet<>();
            result.addAll(this.helper.serviceInstances(browsingDomain, type, secValidation));
            if (result.isEmpty() && !ExceptionsUtil.onlyNameResolutionTrace(this.errorsTrace.get())) {
                throw ExceptionsUtil.build(StatusCode.RESOURCE_LOOKUP_ERROR,
                        FormattingUtil.unableToResolve(browsingDomain.fqdnWithPrefix(type.prefixString())),
                        errorsTrace.get());
            }
        } catch (LookupException | ConfigurationException exception) {
            throw exception;
        } finally {
            errorsTrace.remove();
        }

        return result;
    }

    @Override
    public Set<TextRecord> listTextRecords(Fqdn browsingDomain, String label, boolean secValidation)
                                throws LookupException, ConfigurationException
    {
        try {
            ValidatorUtil.isValidDomainName(browsingDomain);
        } catch(IllegalArgumentException exception) {
            throw new LookupException(StatusCode.ILLEGAL_FQDN, browsingDomain.fqdn());
        }
        ValidatorUtil.isValidLabel(label);
        validatedConf();
        Set<TextRecord> result = null;
        try {
            result = new TreeSet<>();
            Fqdn txtFqdn = new Fqdn(label, browsingDomain.domain());
            result.addAll(this.helper.serviceTexts(txtFqdn, label, secValidation));
            if (result.isEmpty() && !ExceptionsUtil.onlyNameResolutionTrace(this.errorsTrace.get())) {
                throw ExceptionsUtil.build(StatusCode.RESOURCE_LOOKUP_ERROR,
                        FormattingUtil.unableToResolve(browsingDomain.fqdnWithPrefix(label)),
                        errorsTrace.get());
            }
        } catch (LookupException | ConfigurationException exception) {
            throw exception;
        } finally {
            errorsTrace.remove();
        }

        return result;
    }

    @Override
    public Set<CertRecord> listTLSARecords(Fqdn browsingDomain, DnsCertPrefix tlsaPrefix,
                                                    boolean secValidation)
                                        throws LookupException, ConfigurationException
    {
        try {
            ValidatorUtil.isValidDomainName(browsingDomain);
        } catch(IllegalArgumentException exception) {
            throw new LookupException(StatusCode.ILLEGAL_FQDN, browsingDomain.fqdn());
        }
        validatedConf();
        Set<CertRecord> result = null;
        try {
            result = new TreeSet<>();
            result.addAll(this.helper.tlsaRecords(browsingDomain, tlsaPrefix, secValidation));
            if (result.isEmpty() && !ExceptionsUtil.onlyNameResolutionTrace(this.errorsTrace.get())) {
                throw ExceptionsUtil.build(StatusCode.RESOURCE_LOOKUP_ERROR,
                        FormattingUtil.unableToResolve(browsingDomain.fqdn()),
                        errorsTrace.get());
            }
        } catch (LookupException exception) {
            throw exception;
        } finally {
            errorsTrace.remove();
        }

        return result;
    }

    @Override
    public boolean isDnsSecValid(Fqdn name) throws LookupException, ConfigurationException
    {
        try {
            ValidatorUtil.isValidDomainName(name);
        } catch(IllegalArgumentException exception) {
            throw new LookupException(StatusCode.ILLEGAL_FQDN, name.fqdn());
        }
        validatedConf();
        if (name == null || name.fqdn().isEmpty()) {
            name = new Fqdn(this.dnsSecDomain);
        }

        Map<String, Resolver> resolvers = retrieveResolvers(true);
        Iterator<String> itrResolvers = resolvers.keySet().iterator();
        boolean validated = false;
        String server = null;
        do {
            server = itrResolvers.next();
            statusChange(FormattingUtil.server(server));
            statusChange(FormattingUtil.query(name, "", "SOA"));
            try {
                validated = DnsUtil.checkDnsSec(name, resolvers.get(server), Type.SOA);
                if (validated) {
                    statusChange(FormattingUtil.response(FormattingUtil.authenticData(name.fqdn())));
                } else {
                    statusChange(FormattingUtil.response(FormattingUtil.networkError(name.fqdn())));
                }
            } catch (LookupException le) {
                if (le.dnsError() == StatusCode.RESOURCE_LOOKUP_ERROR) {
                    statusChange(FormattingUtil.response(FormattingUtil.unableToResolve(name.fqdn())));
                } else {
                    statusChange(FormattingUtil.response(FormattingUtil.unableToValidate(name.fqdn())));
                }

                if (le.dnsError() == StatusCode.RESOURCE_INSECURE_ERROR) {
                    throw ExceptionsUtil.build(StatusCode.DNSSEC_STATUS_ERROR,
                      "DNSSEC Validation Failed",
                      new LinkedHashMap<String, StatusCode>());
                }
                throw le;
            }
        } while (itrResolvers.hasNext() && !validated);

        return validated;
    }

    /**
     * Private helper to retrieve a set of one or more instances of <code>Resolver</code> to carry
     * out the lookup.
     *
     * @param secValidation <code>true</code> iff DNSSEC validation id needed
     * @return Instance(s) of <code>Resolver</code>
     *
     * @throws ConfigurationException
     *      In case instance(s) of <code>Resolver</code> cannot he instantiated.
     */
    private Map<String, Resolver> retrieveResolvers(boolean secValidation)
                                    throws ConfigurationException
    {
        Map<String, Resolver> resolvers = new LinkedHashMap<>();
        for(InetAddress dnsServer: this.dnsServers) {
            if (dnsServer != null && (!dnsServer.getHostAddress().isEmpty()
                    || !dnsServer.getCanonicalHostName().isEmpty())) {
                String server = ((dnsServer.getHostAddress().isEmpty())
                        ? dnsServer.getCanonicalHostName() : dnsServer.getHostAddress());
                resolvers.put(server, DnsUtil.getResolver(secValidation, this.trustAnchorDefault, server));
            } else {
                resolvers.putAll(DnsUtil.getResolvers(secValidation, this.trustAnchorDefault));
            }
        }

        return resolvers;
    }

    /**
     * Resource Record holder type enumeration. It enumerates the types hold by DNS RRs.
     */
    private enum RrHolderType { NAMES, ZONES, TYPES, OTHER; }

    /**
     * Private inner helper class to implement DNS-specific lookup operations.
     *
     * @author pmaresca <pmaresca@verisign.com>
     * @version 1.0
     * @since 2015/05/02
     */
    // TODO Deal with DNSSECJava validation problems to avoid a double resolving process
    private final class ServicesLookupHelper
    {

        public ServicesLookupHelper() { super(); }

        /**
         * Retrieve a set of Service Types from the browsing domain.
         *
         * @param browsingDomain <code>Fqdn</code> representing the browsing domain
         * @param secValidation  <code>true</code> in case secure browsing is needed
         *
         * @return A set of <code>String</code> identifying the retrieved Service Types.
         *
         * @throws LookupException
         *      In case of any unrecoverable error during the lookup process.
         *
         * @throws ConfigurationException
         *      In case of wrong/faulty static and/or runtime configuration.
         */
        public Set<String> serviceTypes(Fqdn browsingDomain, boolean secValidation)
                                throws LookupException, ConfigurationException
        {
            statusChange(FormattingUtil.info((secValidation?"Secure Resolving mode":"Insecure Resolving mode")));
            Map<String, Resolver> resolvers = retrieveResolvers(false);
            Map<String, Resolver> valResolvers = retrieveResolvers(true);

            RecordsContainer set = new RecordsContainer();
            errorsTrace.get().clear();

            Iterator<String> itrResolvers = resolvers.keySet().iterator();
            LookupContext ctx = DnsUtil.context(browsingDomain, Constants.SERVICES_DNS_SD_UDP, "", "",
                                                Type.PTR, secValidation);
            String server = null;
            do {
                server = itrResolvers.next();
                Resolver resolver = resolvers.get(server);
                ctx.setResolver(resolver);
                Resolver valResolver = valResolvers.get(server);
                ctx.setValResolver(valResolver);
                statusChange(FormattingUtil.server(server));

                try {
                    Record[] records = lookup(ctx);
                    set.getLabels().addAll(DnsUtil.extractNamesFromRecords(records));
                    statusChange(StatusChangeEvent.build(browsingDomain.fqdn(), Type.string(Type.PTR),
                                    StatusChangeEvent.castedArray(records)));
                } catch (LookupException le) {
                    if (le.dnsError().equals(StatusCode.NETWORK_ERROR) && !itrResolvers.hasNext()) {
                        throw  le;
                    } else if (le.dnsError().equals(StatusCode.SERVER_ERROR)
                                || le.dnsError().equals(StatusCode.RESOURCE_INSECURE_ERROR)) {
                        throw le;
                    } else {
                        errorsTrace.get().put(
                                ExceptionsUtil.traceKey(resolver, browsingDomain.fqdn(),
                                        "Retrieving-Types"), le.dnsError());
                    }
                }
            } while (itrResolvers.hasNext() && set.getLabels().isEmpty());
            statusChange(FormattingUtil.answer());

            return set.getLabels();
        }

        /**
         * Retrieve a set of Text Resource Records from the browsing domain for the specified
         * <i>label</i>.
         *
         * @param browsingDomain <code>Fqdn</code> representing the browsing domain
         * @param label A label to be looked up
         * @param secValidation  <code>true</code> in case secure browsing is needed
         *
         * @return A set of <code>String</code> identifying the retrieved Text records
         *
         * @throws LookupException
         *      In case of any unrecoverable error during the lookup process.
         * @throws ConfigurationException
         *      In case of wrong/faulty static and/or runtime configuration.
         */
        public Set<TextRecord> serviceTexts(Fqdn browsingDomain, String label, boolean secValidation)
                                    throws LookupException, ConfigurationException
        {
            statusChange(FormattingUtil.info((secValidation?"Secure Resolving mode":"Insecure Resolving mode")));
            Map<String, Resolver> resolvers = retrieveResolvers(false);
            Map<String, Resolver> valResolvers = retrieveResolvers(true);

            RecordsContainer set = new RecordsContainer();
            errorsTrace.get().clear();

            Iterator<String> itrResolvers = resolvers.keySet().iterator();
            LookupContext ctx = DnsUtil.context(browsingDomain, label, label, "", Type.TXT, secValidation);
            String server = null;
            do {
                server = itrResolvers.next();
                Resolver resolver = resolvers.get(server);
                ctx.setResolver(resolver);
                Resolver valResolver = valResolvers.get(server);
                ctx.setValResolver(valResolver);
                statusChange(FormattingUtil.server(server));

                try {
                    Record[] records = lookup(ctx);
                    parseRecords(records, set, RrHolderType.OTHER);
                    statusChange(StatusChangeEvent.build(browsingDomain.fqdnWithPrefix(label),
                                    "", StatusChangeEvent.castedList(set.getTexts())));
                } catch (LookupException le) {
                    if (le.dnsError().equals(StatusCode.NETWORK_ERROR) && !itrResolvers.hasNext()) {
                        throw  le;
                    } else if (le.dnsError().equals(StatusCode.SERVER_ERROR)
                                    || le.dnsError().equals(StatusCode.RESOURCE_INSECURE_ERROR)) {
                        throw le;
                    } else {
                        errorsTrace.get().put(
                                ExceptionsUtil.traceKey(resolver, browsingDomain.fqdnWithPrefix(label),
                                        "Retrieving-Texts"), le.dnsError());
                    }
                }
            } while (itrResolvers.hasNext() && set.getTexts().isEmpty());
            statusChange(FormattingUtil.answer());

            return set.getTexts();
        }

        /**
         * Retrieve a set of Service Resource Records from the browsing domain, according to the
         * specified <i>type</i>.
         *
         * @param browsingDomain <code>Fqdn</code> representing the browsing domain
         * @param type A <code>String</code> defining the Service Type to be looked up
         * @param secValidation  <code>true</code> in case secure browsing is needed
         *
         * @return A set of <code>String</code> identifying the retrieve Service records.
         *
         * @throws LookupException
         *      In case of any unrecoverable error during the lookup process.
         * @throws ConfigurationException
         *      In case of wrong/faulty static and/or runtime configuration.
         */
        public Set<ServiceInstance> serviceInstances(Fqdn browsingDomain, CompoundLabel type, boolean secValidation)
                                        throws LookupException, ConfigurationException
        {
            statusChange(FormattingUtil.info((secValidation?"Secure Resolving mode":"Insecure Resolving mode")));
            Map<String, Resolver> resolvers = retrieveResolvers(false);
            Map<String, Resolver> valResolvers = retrieveResolvers(true);

            Set<ServiceInstance> instances = new TreeSet<>();
            errorsTrace.get().clear();
            Iterator<String> itrResolvers = resolvers.keySet().iterator();

            // retrive instances by subtype, skip the types listing step
            boolean bySubType = type.hasSubType(), byProto = type.hasProtocol();
            LookupContext ctx = DnsUtil.context(browsingDomain, "", "", type.getType(),
                                                Type.PTR, secValidation);
            String server = null;
            do {
                server = itrResolvers.next();
                Resolver resolver = resolvers.get(server);
                ctx.setResolver(resolver);
                Resolver valResolver = valResolvers.get(server);
                ctx.setValResolver(valResolver);
                statusChange(FormattingUtil.server(server));

                try {
                    Set<String> types = new TreeSet<>();
                    ctx.setDomainName(browsingDomain);
                    if(!bySubType) {
                        if(!byProto) {
                            ctx.setLabel(type.prefixString(Constants.TCP));
                            types.addAll(DnsUtil.filterByType(type.prefixString(Constants.TCP),
                                            retrieveDnsSdTypes(ctx)));  // TCP services

                            ctx.setLabel(type.prefixString(Constants.UDP));
                            types.addAll(DnsUtil.filterByType(type.prefixString(Constants.UDP),
                                            retrieveDnsSdTypes(ctx)));  // UDP services
                        } else {
                            ctx.setLabel(type.prefixString());
                            types.addAll(DnsUtil.filterByType(type.prefixString(), retrieveDnsSdTypes(ctx)));  // service types
                        }
                        statusChange(StatusChangeEvent.build(ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()),
                                    Type.string(ctx.getRrType()), StatusChangeEvent.castedList(types)));
                    } else {    // browsing by subtype
                        if(!byProto) {
                            types.add(browsingDomain.fqdnWithPrefix(type.prefixString(Constants.TCP)));
                            types.add(browsingDomain.fqdnWithPrefix(type.prefixString(Constants.UDP)));
                        } else
                            types.add(browsingDomain.fqdnWithPrefix(type.prefixString()));
                    }

                    Set<String> names = retrieveDnsNames(ctx, types);   // service names
                    ctx.setDomainName(browsingDomain);
                    statusChange(StatusChangeEvent.build(ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()),
                                    Type.string(ctx.getRrType()), StatusChangeEvent.castedList(names)));
                    instances.addAll(retrieveDnsInstances(ctx, names)); // service instances
                } catch (LookupException le) {
                    if (le.dnsError().equals(StatusCode.NETWORK_ERROR) && !itrResolvers.hasNext()) {
                        throw  le;
                    } else if (le.dnsError().equals(StatusCode.SERVER_ERROR)
                                || le.dnsError().equals(StatusCode.RESOURCE_INSECURE_ERROR)) {
                        throw le;
                    } else {
                        errorsTrace.get().put(
                                ExceptionsUtil.traceKey(resolver, browsingDomain.fqdnWithPrefix(type.prefixString()),
                                        "Retrieving-Instances"), le.dnsError());
                    }
                }
            } while (itrResolvers.hasNext() && instances.isEmpty());
            statusChange(FormattingUtil.answer());

            return instances;
        }

        /**
         *
         * Retrieve a set of TLSA Records from the browsing domain, according to the
         * specified <i>options</i>.
         *
         * @param browsingDomain <code>Fqdn</code> representing the browsing domain
         * @param tlsaPrefix A <code>String</code> defining the TLSA prefix as couple
         *                   <code>port:protocol</code>
         * @param secValidation  <code>true</code> in case secure browsing is needed
         *
         * @return A set of <code>String</code> identifying the retrieve Service records.
         *
         * @throws LookupException
         *      In case of any unrecoverable error during the lookup process.
         * @throws ConfigurationException
         *      In case of wrong/faulty static and/or runtime configuration.
         */
        public Set<CertRecord> tlsaRecords(Fqdn browsingDomain, DnsCertPrefix tlsaPrefix,
                                           boolean secValidation)
                                throws LookupException, ConfigurationException
        {
            statusChange(FormattingUtil.info((secValidation?"Secure Resolving mode":"Insecure Resolving mode")));
            Map<String, Resolver> resolvers = retrieveResolvers(false);
            Map<String, Resolver> valResolvers = retrieveResolvers(true);

            Set<CertRecord> tlsaDiscoveryRecords = new TreeSet<>();
            errorsTrace.get().clear();

            Iterator<String> itrResolvers = resolvers.keySet().iterator();
            String tlsaFqdn = tlsaPrefix.toString() + Constants.DNS_LABEL_DELIMITER + browsingDomain.fqdn();
            Fqdn browsingDomainWithTLSAPrefix = new Fqdn(tlsaFqdn);
            LookupContext ctx = DnsUtil.context(browsingDomainWithTLSAPrefix, "", "", "", Type.TLSA, secValidation);
            String server;
            do {
                server = itrResolvers.next();
                Resolver resolver = resolvers.get(server);
                ctx.setResolver(resolver);
                Resolver valResolver = valResolvers.get(server);
                ctx.setValResolver(valResolver);
                statusChange(FormattingUtil.server(server));

                try {
                    Record[] records = lookup(ctx);
                    for (Record record : records) {
                        if (record instanceof TLSARecord) {
                            tlsaDiscoveryRecords.add(new CertRecord((TLSARecord) record));
                        }
                    }
                    statusChange(StatusChangeEvent.build(ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()),
                                    "", StatusChangeEvent.castedList(tlsaDiscoveryRecords)));
                } catch (LookupException le) {
                    if (le.dnsError().equals(StatusCode.NETWORK_ERROR) && !itrResolvers.hasNext()) {
                        throw  le;
                    } else if (le.dnsError().equals(StatusCode.SERVER_ERROR)
                                || le.dnsError().equals(StatusCode.RESOURCE_INSECURE_ERROR)) {
                        throw le;
                    } else {
                        errorsTrace.get().put(
                                ExceptionsUtil.traceKey(resolver, browsingDomain.domain(),
                                        "Retrieving-Instances"),
                                le.dnsError());
                    }
                }
            } while (itrResolvers.hasNext() && tlsaDiscoveryRecords.isEmpty());
            statusChange(FormattingUtil.answer());

            return tlsaDiscoveryRecords;
        }

        /**
         * Instantiate and trigger a DNS lookup according to the defined input parameters.
         *
         * @param ctx A <code>LookupContext</code> defining this lookup parameters
         *
         * @return A set of one or more Resource <code>Record</code>
         *
         * @throws LookupException
         *      In case of unsuccessful DNS lookup; the <code>StatusCode</code> is returned as part of this error.
         */
        private Record[] lookup(LookupContext ctx) throws LookupException
        {
            Lookup lookup = DnsUtil.instantiateLookup(ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()),
                                                      ctx.getResolver(),
                                                      ctx.getRrType(),
                                                      anyClassCache);
            ctx.setLookup(lookup);
            Record[] records = lookup.run();
            // double attemp without quotes
            if(records == null && ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()).contains("\"")) {
                lookup = DnsUtil.instantiateLookup(ctx.getDomainName().fqdnWithPrefix(ctx.getPrefix()).replaceAll("\"", ""),
                                                   ctx.getResolver(),
                                                   ctx.getRrType(),
                                                   anyClassCache);
                ctx.setLookup(lookup);
                records = lookup.run();
            }

            statusChange(FormattingUtil.query(ctx.getDomainName(), ctx.getPrefix(),
                         Type.string(ctx.getRrType())));
            StatusCode outcome = DnsUtil.checkLookupStatus(lookup);
            if(outcome == StatusCode.SUCCESSFUL_OPERATION) {
                if(records != null && records.length > 0 && ctx.isSecure()) {
                    Fqdn toCheck = new Fqdn(ctx.getPrefix(), ctx.getDomainName().domain());
                    DnsUtil.checkDnsSec(toCheck, ctx.getValResolver(), ctx.getRrType());
                }
            }else if (outcome.equals(StatusCode.SERVER_ERROR) ||
                      outcome.equals(StatusCode.NETWORK_ERROR)) {
                throw ExceptionsUtil.build(outcome,
                                           FormattingUtil.unableToResolve(ctx.getDomainName().fqdn()),
                                           errorsTrace.get());
            } else {
                errorsTrace.get().put(
                        ExceptionsUtil.traceKey(ctx.getResolver(), ctx.getResolver().toString() + ctx.getDomainName(),
                                "Checking-Lookup-Status"), outcome);
            }

            return (records == null?new Record[0]:records);
        }

        /**
         * Retrieve the DNS Service's Zones.
         *
         * @param ctx A <code>LookupContext</code> defining this lookup parameters
         *
         * @return A set of <code>String</code> containing the DNS zones
         *
         * @throws LookupException
         *      In case of unsuccessful DNS lookup; the <code>StatusCode</code> is returned as part of this error.
         */
        private Set<String> retrieveDnsSdTypes(LookupContext ctx) throws LookupException
        {
            ctx.setPrefix(Constants.SERVICES_DNS_SD_UDP);
            ctx.setRrType(Type.PTR);
            Record[] records = lookup(ctx);
            RecordsContainer set = new RecordsContainer();
            parseRecords(records, set, RrHolderType.ZONES);

            return set.getLabels();
        }

        /**
         * Retrieve the DNS Service's Names.
         *
         * @param ctx A <code>LookupContext</code> defining this lookup parameters
         *
         * @return A set of <code>String</code> containing the DNS service names
         *
         * @throws LookupException
         *      In case of unsuccessful DNS lookup; the <code>StatusCode</code> is returned as part of this error.
         */
        private Set<String> retrieveDnsNames(LookupContext ctx, Set<String> zones)
                                throws LookupException
        {
            ctx.setPrefix("");
            ctx.setRrType(Type.PTR);
            RecordsContainer set = new RecordsContainer();
            for (String zone : zones) {
                ctx.setDomainName(new Fqdn(zone));
                Record[] records = lookup(ctx);
                parseRecords(records, set, RrHolderType.NAMES);
            }

            return set.getLabels();
        }

        /**
         * Retrieve the Service's records.
         *
         * @param ctx A <code>LookupContext</code> defining this lookup parameters
         *
         * @return A set of <code>String</code> containing the service's records
         *
         * @throws LookupException
         *      In case of unsuccessful DNS lookup; the <code>StatusCode</code> is returned as part of this error.
         */
        private Set<ServiceRecord> retrieveDnsRecords(LookupContext ctx, Set<String> svcNames)
                                    throws LookupException
        {
            RecordsContainer set = new RecordsContainer();
            ctx.setPrefix("");
            ctx.setRrType(Type.SRV);
            for (String svcName : svcNames) {
                ctx.setDomainName(new Fqdn(svcName));
                Record[] records = lookup(ctx);
                parseRecords(records, set, RrHolderType.OTHER);
            }

            return set.getRecords();
        }

        /**
         * Retrieve the Service's instances.
         *
         * @param ctx A <code>LookupContext</code> defining this lookup parameters
         *
         * @return A set of <code>String</code> containing the service's records
         *
         * @throws LookupException
         *      In case of unsuccessful DNS lookup; the <code>StatusCode</code> is returned as part of this error.
         */
        private Set<ServiceInstance> retrieveDnsInstances(LookupContext ctx, Set<String> svcNames)
                                        throws LookupException
        {
            Set<ServiceInstance> svcInstances = new TreeSet<>();
            Set<String> aName = new LinkedHashSet<>();
            RecordsContainer set = new RecordsContainer();
            for (String svcName : svcNames) {
                aName.clear();
                set.getTexts().clear();
                aName.add(svcName);

                Set<ServiceRecord> svcRecords = retrieveDnsRecords(ctx, aName);
                statusChange(StatusChangeEvent.build(svcName, "", StatusChangeEvent.castedList(svcRecords)));
                if (svcRecords.isEmpty()) {
                    continue;
                }

                ctx.setPrefix("");
                ctx.setRrType(Type.TXT);
                ctx.setDomainName(new Fqdn(svcName));
                Record[] records = lookup(ctx);
                parseRecords(records, set, RrHolderType.OTHER);
                statusChange(StatusChangeEvent.build(svcName, "", StatusChangeEvent.castedList(set.getTexts())));
                if (set.getTexts().isEmpty()) {
                    continue;
                }

                svcInstances.add(new ServiceInstance(ctx.getType(), svcRecords.iterator().next(),
                                    TextRecord.build(set.getTexts())));
            }

            return svcInstances;
        }

        /**
         * Scrapes the Discovery Service Records according to their nature.
         *
         * @param records An array of <code>Record</code> retrieve upon a lookup
         * @param set A <code>ResourcesContainer</code>
         * @param pht A Resource Record Type holder
         */
        private void parseRecords(Record[] records, final RecordsContainer set, RrHolderType pht)
        {
            if (records != null) {
                for (Record record : records) {
                    if ((record instanceof PTRRecord && pht == RrHolderType.ZONES) ||
                            (record instanceof PTRRecord && pht == RrHolderType.NAMES)) {
                        String zone = PointerRecord.build((PTRRecord) record).getRData();
                        if (zone != null) {
                            set.getLabels().add(zone);
                        }
                    } else if (record instanceof PTRRecord && pht == RrHolderType.TYPES) {
                        set.getLabels().add(PointerRecord.build((PTRRecord) record).getServiceType());
                    } else if (record instanceof SRVRecord) {
                        ServiceRecord svcRecord = ServiceRecord.build((SRVRecord) record);
                        if (svcRecord != null) {
                            set.getRecords().add(svcRecord);
                        }
                    } else if (record instanceof TXTRecord) {
                        set.getTexts().add(TextRecord.build((TXTRecord) record));
                    } else {
                        errorsTrace.get().put(
                                ExceptionsUtil.traceKey(record.toString(), "",
                                        "Parsing-Service-Records"),
                                StatusCode.RESOURCE_UNEXPECTED);
                    }
                }
            }
        }

    }

}
