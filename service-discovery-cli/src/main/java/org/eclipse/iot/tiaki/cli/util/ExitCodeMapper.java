/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.util;

import org.eclipse.iot.tiaki.cli.common.ExitCodes;
import org.eclipse.iot.tiaki.commons.StatusCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility Class. It has a set of definitions and facilities to deal with
 * <code>ExitCodes</code> (mapped to <code>StatusCode</code>,
 * see <a href="https://github.com/verisign/iot-discovery-services">IoT Services Discovery</a>).
 *
 */
public final class ExitCodeMapper
{

    private static Map<StatusCode, ExitCodes> exitCodeMap = null;

    static
    {
        exitCodeMap = new HashMap<>();

        exitCodeMap.put(StatusCode.CONFIGURATION_ERROR, ExitCodes.LIB_CONFIGURATION_ERROR);
        exitCodeMap.put(StatusCode.NETWORK_ERROR, ExitCodes.NETWORK_ERROR);
        exitCodeMap.put(StatusCode.RESOURCE_LOOKUP_ERROR, ExitCodes.RESOURCE_LOOKUP_ERROR);
        exitCodeMap.put(StatusCode.RESOURCE_INSECURE_ERROR, ExitCodes.RESOURCE_INSECURE_ERROR);
        exitCodeMap.put(StatusCode.DNSSEC_STATUS_ERROR, ExitCodes.DNSSEC_STATUS_ERROR);
        exitCodeMap.put(StatusCode.RESOURCE_UNEXPECTED, ExitCodes.RESOURCE_UNEXPECTED);
        exitCodeMap.put(StatusCode.ILLEGAL_FQDN, ExitCodes.INVALID_FQDN);
        exitCodeMap.put(StatusCode.SERVER_ERROR, ExitCodes.DNS_SERVER_ERROR);
        exitCodeMap.put(StatusCode.RESOLUTION_NAME_ERROR, ExitCodes.RESOLUTION_NAME_ERROR);
        exitCodeMap.put(StatusCode.RESOLUTION_RR_TYPE_ERROR, ExitCodes.RESOLUTION_RR_TYPE_ERROR);
    }

    /**
     * Returns the ExitCode corresponding to the library StatusCode.
     *
     * @param code the library status code
     * @return the corresponding ExitCode if found, else the Generic ExitCode if null provided or status code not found
     */
    public static ExitCodes map (StatusCode code)
    {
        if (code == null)
        {
            // Safely returns the generic error if a null is provided
            return ExitCodes.GENERIC_ERROR;
        }
        else
        {
            ExitCodes exitCode = exitCodeMap.get(code);

            // Returns the generic error as well if the code is not found in the map
            if (exitCode == null)
            {
                return ExitCodes.GENERIC_ERROR;
            }

            return exitCode;
        }
    }

    private ExitCodeMapper() {
        throw new AssertionError( String.format( "Class %s not instantiable", this.getClass().getName() ) );
    }
}
