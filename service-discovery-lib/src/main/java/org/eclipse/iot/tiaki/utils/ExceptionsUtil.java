/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.utils;

import org.eclipse.iot.tiaki.commons.StatusCode;
import org.eclipse.iot.tiaki.exceptions.LookupException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of utility methods to build <code>DnsServiceException</code> up.
 *
 */
public final class ExceptionsUtil
{

    /** Date format pattern. */
    private static final String DATET_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** Date formatter. */
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATET_FORMAT);

	/**
	 * Build a <code>LookupException</code> and return its instance.
	 *
	 * @param error A <code>StatusCode</code> related to this error
	 * @param message A <code>String</code> further specifying the runtime error
	 * @param trace A <code>Map</code> containing error traces collected till
	 * this exception building
	 * @return An instance of <code>LookupException</code>
	 */
	public static LookupException build(StatusCode error, String message,
                                        Map<String, StatusCode> trace)
    {
		LookupException exception = new LookupException(error, message);
		exception.setErrorsTrace(new HashMap<>(trace));

		return exception;
	}

	/**
	 * Build a Key to be used into errors trace log.
	 *
	 * @param who An <code>Object</code> to be used
	 * @param forWhich A message to be combined
	 * @param where A <code>String</code> containing the location where the
	 * error is raised
	 * @return A <code>String</code> containing the so built Key
	 */
	public static String traceKey(Object who, String forWhich, String where)
    {
		StringBuilder builder = new StringBuilder();
		builder.append(FORMATTER.format(new Date(System.currentTimeMillis())))
				.append(":<")
				.append(who.getClass().getSimpleName())
				.append(">:[")
				.append(forWhich.replace(who.getClass().getName()+"@", ""))
				.append("]@<")
				.append(where)
                .append(">");

		return builder.toString();
	}

    /**
     *
     * @param trace
     * @return
     */
    public static boolean onlyNameResolutionTrace(Map<String, StatusCode> trace)
    {
        int cntr = 0;
        for(StatusCode status: trace.values())
            if(status.equals(StatusCode.RESOLUTION_NAME_ERROR) ||
               status.equals(StatusCode.RESOLUTION_RR_TYPE_ERROR))  
                cntr += 1;

        return (cntr == trace.size());
    }

	private ExceptionsUtil()
    {
		throw new AssertionError(String.format("No instances of %s for you!", this.getClass().getName()));
	}

}
