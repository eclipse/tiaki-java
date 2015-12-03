/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.exceptions;

import org.eclipse.iot.tiaki.commons.StatusCode;
import java.util.Map;

/**
 * Specific DNS Service Exception encapsulating a <code>StatusCode</code>
 * identifying uniquely the detected error, plus a <code>Map</code> of traced
 * recoverable errors caught till the exception raising time.
 *
 */
public class DnsServiceException extends Exception
{

	protected static final long serialVersionUID = 1997753363232807009L;

	/**
	 * A <code>StatusCode</code> identifying the detected error
	 */
	protected StatusCode raisingError;
	/**
	 * A <code>Map</code> containing a set of recoverable error(s) till this
	 * exception raising time
	 */
	protected Map<String, StatusCode> errorsTrace;

	/**
	 * Two arguments constructor: a message decorates the caught error.
	 *
	 * @param error Caught error
	 * @param message A decorating message
	 */
	public DnsServiceException(StatusCode error, String message)
    {
		super(error.toString() + " " + message);
		this.raisingError = error;
	}

    /**
	 * Three arguments constructor: no message decoration for the caught error
     * if <code>simple</code> is set to <code>true</code>.
	 *
	 * @param error Caught error
	 * @param message A decorating message
     * @param simple  <code>true</code> if no message decoration is needed
	 */
    public DnsServiceException(StatusCode error, String message, boolean simple)
    {
		super(message);
		this.raisingError = error;
	}

	public DnsServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Return the caught error associated to this instance.
	 *
	 * @return The error code and its brief
	 */
	public final StatusCode dnsError()
    {
		return this.raisingError;
	}

	/**
	 * Set the error(s) trace <code>Map</code>
	 *
	 * @param errors A <code>Map</code> containing caught recoverable errors
	 */
	public final void setErrorsTrace(Map<String, StatusCode> errors)
    {
		this.errorsTrace = errors;
	}

	/**
	 * Return the error(s) trace <code>Map</code>.
	 *
	 * @return A <code>Map</code> containing the error(s) trace
	 */
	public final Map<String, StatusCode> getErrorsTrace()
    {
		return this.errorsTrace;
	}

	/**
	 * Generate a printable version of the error(s) trace.
	 *
	 * @return A <code>String</code> with a printable version of the error(s)
	 * trace
	 */
	public final String printableErrorsTrace()
    {
		StringBuilder builder = new StringBuilder();
        builder.append(";; ERROR TRACE\n");
		if (this.errorsTrace != null) {
			for (String key : this.errorsTrace.keySet()) {
				builder.append(key)
                       .append(": ")
                       .append(this.errorsTrace.get(key).toString()
                           .substring(0, this.errorsTrace.get(key).toString().length() - 1))
                       .append("\n");
			}
		}

		return builder.toString();
	}

}
