/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli;

import org.eclipse.iot.tiaki.cli.exception.ExecutionException;
import org.eclipse.iot.tiaki.cli.exception.OptionsNotValidException;
import joptsimple.OptionSet;

/**
 * Define a command to be executed. A command lifecycle spans over
 * the initialization (options are checked against the intended usage)
 * and execution (actual parameters are put together and the service is invoked).
 *
 */
public interface Command
{

	/**
	 * Initialize and validate the arguments.
	 *
	 * @param optionSet     A set of options
	 *
     * @throws ExecutionException
     *          In case runtime errors occur at init time
     * @throws OptionsNotValidException
     *          In case options are not valid
	 */
	void initialize ( OptionSet optionSet ) throws ExecutionException, OptionsNotValidException;


	/**
	 * Execute the command according to the given options.
	 *
	 * @throws ExecutionException
     *          In case of any execution problems
	 */
	void execute () throws ExecutionException;

}
