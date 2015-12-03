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

/**
 * A specific <code>DnsServiceException</code> raised whenever a runtime lookup error raises.
 *
 */
// TODO Booleans to be put into class variables
public class LookupException extends DnsServiceException
{

    private static final long serialVersionUID = -7221387354272680529L;

    public LookupException(StatusCode error, String message)
    {
        this(error, message, null, false, false);
    }

    public LookupException(StatusCode error, Throwable cause)
    {
        this(error, "", cause, false, false);
    }

    public LookupException(StatusCode error, String message, Throwable cause)
    {
        this(error, message, cause, false, false);
    }

    public LookupException(StatusCode error, String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(error.toString() + message, cause, enableSuppression, writableStackTrace);
        this.raisingError = error;
    }

}
