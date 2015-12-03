/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli;

import org.eclipse.iot.tiaki.cli.exception.ParsingException;
import joptsimple.OptionSet;


/**
 * Define an options parser. This interface specifies the behaviour of a generic
 * options parser that has to take in input command line arguments and transform
 * them in a <code>OptionSet</code>.
 * 
 */
public interface CommandOptionParser 
{
    /**
     * Parses the command line options.
     * 
     * @param args  An array of options passed in input.
     * 
     * @return  The <code>OptionSet</code> built accordingly
     * 
     * @throws ParsingException
     *          In case any problem occurs in parsing the passed options
     */
	OptionSet parse ( String[] args ) throws ParsingException;
}
