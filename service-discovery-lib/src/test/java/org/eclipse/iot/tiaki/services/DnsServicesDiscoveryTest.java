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
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import org.eclipse.iot.tiaki.commons.Constants;
import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.domain.TextRecord;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DnsServicesDiscoveryTest implements Observer
{

	public static final String DNS_RESOLVER = "8.8.8.8";
    public static final String BAD_RESOLVER = "1.2.3.4";
    public static final String SERVICE_TYPE = "coapspecial";
    public static final String SERVICE_DOMAIN = "n67423p6tgxq.1.iotverisign.com";
    public static final String SERVICE_DOMAIN_1 = "kfjljohydgsa.1.iotqa.end-points.com";
    public static final String SERVICE_DOMAIN_2 = "jnm6cmzwsmyq.2.iotverisign.com";
    public static final String SERVICE_DOMAIN_3 = "dns-sd.org";
    public static final String SERVICE_DOMAIN_4 = "mcn366rzmd2a.1.iotverisign.com";
    public static final String TEST_DOMAIN = "com";
    public static final String SERVICE_LABEL = "coap";
    public static final String SERVICE_TYPE_1 = "mqft";
    public static final String SERVICE_TYPE_2 = "tcpmux";
    public static final String SERVICE_TYPE_3 = "http";
    public static final String SERVICE_TYPE_4 = "ssh";
    public static final String SERVICE_TYPE_5 = "pdl-datastream";
    public static final String SERVICE_TYPE_6 = "ftp";
    public static final String SERVICE_TYPE_7 = "ipp";
    public static final String SERVICE_TYPE_8 = "mqtt";
    public static final String SERVICE_SUBTYPE = "printer";
    public static final String SERVICE_NAME = "_coapspecial._udp.avu7unxcs7ia.1.iotverisign.com";
    public static final String SERVICE_TEXT = "f5j4pf5vaw1osjnj4nggdmy2ycl1axlm64knkrayhfsstcxe56ctwnxho1coap";
    public static final String BAD_SERVICE_DOMAIN = "google.totosdfgsdfgsdfgsdfgsdfgsdfgsdfgsdfg";

    private DnsServicesDiscovery discovery;


    public DnsServicesDiscoveryTest()
    {
    }

    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void listServiceInstances()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE, "", "udp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected successful lookup, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceMultipleTxts()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_3);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_7, "", "tcp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected successful lookup, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceInstancesBySubType()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_3);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_3, SERVICE_SUBTYPE, "tcp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected successful lookup, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceInstancesSpecialCharacters()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_3);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_6, "", "tcp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
            type = new CompoundLabel(SERVICE_TYPE_7, "", "tcp");
            inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected successful lookup, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listDnsSdStandardSetup()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_3);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_4, "", "tcp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
            type = new CompoundLabel(SERVICE_TYPE_5, "", "tcp");
            inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected an empty set");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceInstancesBothUdpAndTcp()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_4);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_8);
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected an empty set");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceInstancesBySubtypeBothUdpAndTcp()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN_3);
        try {
            CompoundLabel type = new CompoundLabel(SERVICE_TYPE_3, SERVICE_SUBTYPE);
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(inst.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected an empty set");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }


    @Test
    public void listServiceInstancesErrorDomainNotExistent()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn("habla.1.iotverisign.com");
        try {
            CompoundLabel type = new CompoundLabel("mqtt", "", "tcp");
            Set<ServiceInstance> inst = this.discovery.listServiceInstances(name, type, false);
            Assert.assertTrue(true);
        } catch (LookupException ex) {
            Assert.fail("Expected an empty set");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void checkDnsSecError()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
            this.discovery.isDnsSecValid(new Fqdn(BAD_SERVICE_DOMAIN));
            Assert.fail("Expected DNSSEC validation failure");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct retrieval of localhost, not " + ex.toString());
        } catch (LookupException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void checkDnsSecErrorNonExistentDNS() {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .dnsServer(InetAddress.getByName("127.1.1.1"))
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
            this.discovery.isDnsSecValid(new Fqdn("google.com"));
            Assert.fail("Expected DNSSEC validation failure");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct retrieval of localhost, not " + ex.toString());
        } catch (LookupException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void checkDnsSec()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                         .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                         .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                         .introspected(true)
                         .observer(this)
                         .checkConfiguration(true);
            this.discovery.isDnsSecValid(new Fqdn(SERVICE_NAME));
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct retrieval of localhost, not " + ex.toString());
        } catch (LookupException ex) {
            Assert.fail("Expected correct validation, not " + ex.toString());
        }
    }

    @Test
    public void checkDnsSecInvalid() {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .dnsServer(InetAddress.getByName("1.2.3.4"))
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
            this.discovery.isDnsSecValid(new Fqdn("google.coma"));
            Assert.fail("Expected a Lookup Error: non-existing Resolver");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        catch (UnknownHostException ex) {
            Assert.fail("Expected correct retrieval of localhost, not " + ex.toString());
        }
        catch (LookupException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void listServiceTypes()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                        .  introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN);
        try {
            Set<String> typ = this.discovery.listServiceTypes(name, false);
            Assert.assertTrue(typ.size() > 0);
        } catch (LookupException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

    }

    @Test
    public void listServiceTexts()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(DNS_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(SERVICE_DOMAIN);
        Set<TextRecord> rec = null;
        try {
            rec = this.discovery.listTextRecords(name, SERVICE_LABEL, false);
            Assert.assertTrue(rec.isEmpty());
        } catch (LookupException ex) {
            Assert.assertTrue(true);
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }

        try {
            name = new Fqdn(SERVICE_NAME);
            rec = this.discovery.listTextRecords(name, SERVICE_TEXT, false);
            Assert.assertTrue(rec.size() == 1);
        } catch (LookupException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
    }

    @Test
    public void listServiceTextsBadResolver()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .dnsServer(InetAddress.getByName(BAD_RESOLVER))
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (UnknownHostException ex) {
            Assert.fail("Expected correct initialization, not " + ex.toString());
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(TEST_DOMAIN);
        Set<TextRecord> rec = null;
        try {
            rec = this.discovery.listTextRecords(name, "example", true);
            Assert.fail("Expected a Lookup Exception");
        } catch (LookupException ex) {
            Assert.assertTrue(true);
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
    }

    @Test
    public void listServiceTextsDefaultResolever()
    {
        try {
            this.discovery = new DnsServicesDiscovery();
            this.discovery.dnsSecDomain(Constants.DEFAULT_DNSSEC_DOMAIN)
                          .trustAnchorDefault(Constants.DEFAULT_TRUST_ANCHOR)
                          .introspected(true)
                          .observer(this)
                          .checkConfiguration(true);
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
        Fqdn name = new Fqdn(TEST_DOMAIN);
        Set<TextRecord> rec = null;
        try {
            rec = this.discovery.listTextRecords(name, "example", false);
            Assert.assertTrue(rec.size() == 2);
        } catch (LookupException ex) {
            Assert.fail("Expected a successful lookup");
        } catch (ConfigurationException ex) {
            Assert.fail("Expected correct configuration, not " + ex.toString());
        }
    }

    @Override
    public void update(Observable o, Object o1)
    {
        System.out.println(o1.toString());
    }

}
