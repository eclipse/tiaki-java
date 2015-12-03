# Secure Discovery Services Java Library

Welcome to the service-discovery-lib project.
The main purpose of the discovery library is to allow its clients (device, gateway, etc.) to securely discover services (eg. a message broker) and configuration (eg. topic names) for a specific service type (eg. MQTT) in a given domain name.
It implements the [DNS-SD IETF RFC 7673 ](https://tools.ietf.org/html/rfc6763) which specifies how DNS resource records are named and structured to facilitate service discovery.

## Build
This is a Gradle project. 

```
$ cd $PROJECT_HOME
$ ./gradlew clean fatJar
```


##Service Discovery workflow

In order to perform service discovery, the first thing to do is to add the relevant records to DNS. As per the RFC, you need to provision a <service.domain> PTR record, which will point to the corresponding <instance.service.domain> SRV record(s).
After the provisioning is done, you can lookup service instance names by service types for the domain name.

On the image below, a user is provisioning a mqtt service with the URL "iot.eclipse.org:1883" under domain name "example.com".

Using the iot-discovery-services library, the device then looks up services of type "mqtt" under domain name "example.com" and finds "iot.eclipse.org:1883".


![Provisioning and resolution workflow](https://github.com/rpiccand/iot-discovery-services/blob/master/img/dns-sd%20workflow.png)

## Using the Library
Here is a simple example which retrieves the "printer" service instances from "dns-sd.org". In the code below, we use the default DNS Resolver and don't check for DNSSEC (as dns-sd.org is not dnssec-enabled).

```
package org.eclipse.iot.tiaki;

import java.net.UnknownHostException;
import java.util.Set;

import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import org.eclipse.iot.tiaki.services.DnsServicesDiscovery;

public class Discoverer {


		public static void main(String[] args) throws LookupException, ConfigurationException, UnknownHostException {

		Fqdn fullyQualifiedDomainName = new Fqdn("dns-sd.org");
		boolean checkDnssec = false;

		DnsServicesDiscovery discoverer = new DnsServicesDiscovery();
		CompoundLabel serviceType = new CompoundLabel("printer");
		Set<ServiceInstance> discoveryResult = discoverer.listServiceInstances(fullyQualifiedDomainName, serviceType,
				checkDnssec);
		for (ServiceInstance instance : discoveryResult) {
			System.out.println(instance);
		}
	}

}
```
In this other example, we retrieve the mqtt services configured at 7pqg77uhvroq.1.iotverisign.com. As this domain name is dnssec enabled, we don't disable the dnssec validation as in the previous example. We also set a dns resolver that supports DNSSEC signatures.
```
package org.eclipse.iot.tiaki;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

import org.eclipse.iot.tiaki.domain.CompoundLabel;
import org.eclipse.iot.tiaki.domain.Fqdn;
import org.eclipse.iot.tiaki.domain.ServiceInstance;
import org.eclipse.iot.tiaki.exceptions.ConfigurationException;
import org.eclipse.iot.tiaki.exceptions.LookupException;
import org.eclipse.iot.tiaki.services.DnsServicesDiscovery;

public class Discoverer {

	public static void main(String[] args) throws LookupException, ConfigurationException, UnknownHostException {

		Fqdn fullyQualifiedDomainName = new Fqdn("7pqg77uhvroq.1.iotverisign.com");
		boolean checkDnssec = true;
		InetAddress dnsResolver = InetAddress.getByName("64.6.64.6");

		DnsServicesDiscovery discoverer = new DnsServicesDiscovery();
		discoverer.dnsServer(dnsResolver);

		CompoundLabel serviceType = new CompoundLabel("mqtt");
		Set<ServiceInstance> discoveryResult = discoverer.listServiceInstances(fullyQualifiedDomainName, serviceType,
				checkDnssec);
		for (ServiceInstance instance : discoveryResult) {
			System.out.println(instance);
		}
	}

}
```

## Using DNSSEC to secure the discovery process
The Domain Name System Security Extensions (DNSSec) is a technology designed to ensure the authenticity of DNS records  by applying PKI principles. The iot-discovery-services library validates the DNSSec records, which ensures that they are trustworthy.

## Going further into DNS-SD DNS Records
The iot-discovery-services library abstracts the complexity of using raw DNS records. For the sake of completeness, this section describes a concrete example of provisioned DNS Records for service discovery. We use the command line [bind utility dig](https://www.isc.org/downloads/bind/) to retrieve the DNS Records. The domain name used to host this example is dns-sd.org.

### Listing service types
After new service instances have been provisioned, this command lists the DNS labels for which a service type was provisioned. It is a query for PTR records for the "_services._dns-sd._udp" label.

```
$ dig +noall +answer _services._dns-sd._udp.dns-sd.org PTR

_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_http._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_ssh._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_ftp._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_afpovertcp._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_ipp._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_printer._tcp.dns-sd.org.
_services._dns-sd._udp.dns-sd.org. 5 IN	PTR	_pdl-datastream._tcp.dns-sd.org.

```
The response indicates that there are seven service types; http, ssh, ftp, afpovertcp, ipp, printer and pdl-datastream.

### Listing service names
Using the service type label, this command will list the existing names for "printer" services. It is a query for PTR records for the "_printer._tcp" label.

```
$ dig +noall +answer _printer._tcp.dns-sd.org. PTR

_printer._tcp.dns-sd.org. 5	IN	PTR	sales._printer._tcp.dns-sd.org.
_printer._tcp.dns-sd.org. 5	IN	PTR	marketing._printer._tcp.dns-sd.org.
_printer._tcp.dns-sd.org. 5	IN	PTR	3rd.\032floor\032copy\032room._printer._tcp.dns-sd.org.
_printer._tcp.dns-sd.org. 5	IN	PTR	engineering._printer._tcp.dns-sd.org.

```

The response indicates that there are four services of type "printer". Sales, marketing, 3rd floor copy room and engineering.

### Listing service instances
Using the service name label, this command will list the existing services instances. It is a query for ANY record associated with the service named "sales".

```
dig +noall +answer sales._printer._tcp.dns-sd.org. ANY
sales._printer._tcp.dns-sd.org.	5 IN	TXT	"rp=lpt1"
sales._printer._tcp.dns-sd.org.	5 IN	SRV	0 0 49152 pretend-server.stuartcheshire.org.
```

The response indicates that there are two records:
- one SRV that specifies that the service URL is "pretend-server.stuartcheshire.org:49152"
- one TXT which gives some configuration information, typically "rp=lpt1"

Instead of using ANY, we could restrict the query to either SRV or TXT records.

# License
Eclipse Public License - v 1.0
https://www.eclipse.org/legal/epl-v10.html
