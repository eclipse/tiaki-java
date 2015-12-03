/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.cli.console;

import org.eclipse.iot.tiaki.cli.ConsoleWriter;
import java.util.Observable;
import java.util.Observer;

/**
 * This class an <code>Observer</code> of the Discovery Core Library 
 * (main implementation is <code>DnsServicesDiscovery</code>). It logs out
 * the information that the library notifies asynchrnously.
 *
 * 
 * @see <a href="https://github.com/verisign/iot-discovery-services">IoT Services Discovery</a>
 */
public class LibraryObserver implements Observer 
{

	private ConsoleWriter consoleWriter;
    
    
	public LibraryObserver ( ConsoleWriter consoleWriter ) 
    {
		this.consoleWriter = consoleWriter;
	}


	@Override
	public void update ( Observable o, Object object ) 
    {
		consoleWriter.verbose( object.toString() );
	}
    
}
