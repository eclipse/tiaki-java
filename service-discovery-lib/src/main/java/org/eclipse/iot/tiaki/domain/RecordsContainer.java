
/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.domain;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Container of records.
 *
 */
public class RecordsContainer
{

	/** A set of <code>String</code> containing generic labels. */
	private final Set<String> labels;
	/** A set of <code>TextRecord</code> containing Service Discovery records. */
	private final Set<TextRecord> texts;
	/** A set of <code>ServiceRecord</code> containing Service Discovery records. */
	private final Set<ServiceRecord> records;


	public RecordsContainer ()
    {
		this.labels = new LinkedHashSet<>();
		this.texts = new TreeSet<>();
		this.records = new TreeSet<>();
	}


	public Set<String> getLabels ()
    {
		return this.labels;
	}


	public Set<TextRecord> getTexts ()
    {
		return this.texts;
	}


	public Set<ServiceRecord> getRecords ()
    {
		return this.records;
	}

}
