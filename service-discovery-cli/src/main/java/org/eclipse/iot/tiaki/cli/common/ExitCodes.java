/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.common;

/**
 * This class defines a set of exit codes to be used in case of any error.
 *
 */
public enum ExitCodes
{

	// General errors
	GENERIC_ERROR(1, "General unexpected error"),
	// !! The error code 2 is reserved by Linux for Bash builtin errors : DO NOT USE IT HERE !!
	INVALID_ARGS(3, "Invalid argument usage"),
	UNKNOWN_DNS_SRV_HOST(4, "Unknown DNS server host"),

	// Discovery Service Errors
	LIB_CONFIGURATION_ERROR(5, "Inconsistent secured DNS settings"),
	NETWORK_ERROR(6, "Unreachable DNS or timeout expired"),
	RESOURCE_LOOKUP_ERROR(7, "Generic Lookup error"),
	RESOURCE_INSECURE_ERROR(8, "Insecure DNS packet"),
	RESOURCE_UNEXPECTED(9, "Unexpected Resource Record Type"),
	DNS_SERVER_ERROR(10, "DNS server failure"),
	RESOLUTION_NAME_ERROR(11, "DNS name does not exist"),
	RESOLUTION_RR_TYPE_ERROR(12, "RR Type does not exist"),
    INVALID_FQDN(13, "Invalid fully qualified domain name"),
    DNSSEC_STATUS_ERROR(14, "DNSSEC status check failed");


	int exitCode;
	String description;


	ExitCodes ( int exitCode, String description )
    {
		this.exitCode = exitCode;
		this.description = description;
	}


	public int getExitCode ()
    {
		return this.exitCode;
	}


	public String getDescription ()
    {
		return this.description;
	}

}
