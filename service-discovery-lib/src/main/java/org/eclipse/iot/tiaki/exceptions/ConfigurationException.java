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
 * A specific <code>DnsServiceException</code> raised whenever a configuration error raises.
 *
 */
// TODO Booleans to be put into class variables
public class ConfigurationException extends DnsServiceException
{

    private static final long serialVersionUID = -570005026279195680L;

    public ConfigurationException()
    {
        this("", null, false, true);
    }

    public ConfigurationException(String message)
    {
        this(message, null, false, true);
    }

    public ConfigurationException(Throwable cause)
    {
        this("", cause, false, true);
    }

    public ConfigurationException(String message, Throwable cause)
    {
        this(message, cause, false, true);
    }

    public ConfigurationException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace)
    {
        super(StatusCode.CONFIGURATION_ERROR.toString() + message, cause, enableSuppression, writableStackTrace);
        this.raisingError = StatusCode.CONFIGURATION_ERROR;
    }

}
