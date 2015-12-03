/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.parser;

/**
 * Utility Class. It defines constants and usage display.
 *
 */
public final class Options
{

    public static final String HELP = "help";
    public static final String HELP_S = "h";

    public static final String VERBOSE = "verbose";
    public static final String VERBOSE_S = "v";

    public static final String INSECURE = "insecure";
    public static final String INSECURE_S = "e";

    public static final String LIST_INSTANCES = "list-instances";
    public static final String LIST_INSTANCES_S = "i";

    public static final String LIST_SERVICES = "list-services";
    public static final String LIST_SERVICES_S = "l";

    public static final String TEXT_RECORD = "text-record";
    public static final String TEXT_RECORD_S = "t";

    public static final String SUPPLEMENT = "supplement";
    public static final String SUPPLEMENT_S = "s";

    public static final String DNS_SERVER = "servers";
    public static final String DNS_SERVER_S = "n";

    public static final String DOMAIN = "domain";
    public static final String DOMAIN_S = "d";

    public static final String TRUST_ANCHOR = "trust-anchor";
    public static final String TRUST_ANCHOR_S = "u";

    public static final String DNS_SEC_STATUS = "dnssec-status";
    public static final String DNS_SEC_STATUS_S = "c";

    public static final String TLSA_RECORD = "tlsa";
    public static final String TLSA_RECORD_S = "x";

    public static final String LONG_OPTION_ERROR = "unrecognized option '--%s'";
    public static final String SHORT_OPTION_ERROR = "invalid option -- '%s'";


    public static String getUsage()
    {

        StringBuilder sb = new StringBuilder();

        sb.append("Usage: sd-lookup [<command>[<arg>]] [options]").append("\n");

        sb.append("Commands:").append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + HELP_S + ", --" + HELP, "Display this usage and quit.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + LIST_INSTANCES_S + ", --" + LIST_INSTANCES,
                                "Detailed display of service instances; -s and -d are required.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + LIST_SERVICES_S + ", --" + LIST_SERVICES,
                                "Display the service types; -d is required.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + DNS_SEC_STATUS_S + " [domain], --" + DNS_SEC_STATUS +" [domain]",
                                "Check the DNSSEC status of 'domain'; if not specified, check against the default one.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + TLSA_RECORD_S +" [port:protocol], --" + TLSA_RECORD +" [port:protocol]",
                                "Display the TLSA records referring to the couple 'port:protocol' (default ones if not specified); -d and -s (only with required 'label') are required.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + TEXT_RECORD_S + " <label>, --" + TEXT_RECORD +" <label>",
                                    "Display the text records having 'label'; -d is required.")).append("\n");

        sb.append("Options:").append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + INSECURE_S + ", --" + INSECURE, "Inhibit the "
                                + "DNSSEC validation upon lookup.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + VERBOSE_S + ", --" + VERBOSE, "Display a verbose "
                                + "output of the resolution.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + DNS_SERVER_S + " <resolvers>, --" + DNS_SERVER + " <resolvers>",
                                "Comma-separated list of resolver servers, overriding the default ones.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + TRUST_ANCHOR_S + " <filename>, --" + TRUST_ANCHOR + " <filename>",
                                "Specify the file containing the trust anchor.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + DOMAIN_S + " <domain>, --" + DOMAIN + " <domain>",
                                "Specify the domain name to use upon resolution process.")).append("\n");
        sb.append(String.format("  %-40s\t%-40s", "-" + SUPPLEMENT_S + " <label>, --" + SUPPLEMENT + " <label>",
                                "Specify a supplementary 'label' to concatenate/use to query for; the 'label' has to follow the pattern: 'label[:sublabel:proto>|:proto]', e.g. 'http:printer:tcp', or only 'http', or 'http:tcp'.")).append("\n");
        sb.append("\n");

        return sb.toString();

    }

    private Options()
    {
        throw new AssertionError(String.format("Class %s not instantiable", this.getClass().getName()));
    }

}
