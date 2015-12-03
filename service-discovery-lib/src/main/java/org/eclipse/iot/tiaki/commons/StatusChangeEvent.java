/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.commons;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.eclipse.iot.tiaki.domain.DiscoveryRecord;
import org.eclipse.iot.tiaki.utils.FormattingUtil;
import org.xbill.DNS.Record;

/**
 * This class wraps in a status change event, intended as data referring the pair
 * <Query, Response> .
 *
 */
public class StatusChangeEvent implements Serializable
{
	private static final long serialVersionUID = -5316142743420677708L;

	/** A <code>String</code> defining the Header's format. */
	private final String HEADER_FORMAT = " %-64s %-6s  %-64s\n";
	/** A <code>String</code> defining the Body's row format. */
	private final String BODY_FORMAT = "%-64s %-6s %-64s\n";
	/** A <code>String</code> defining the row format (rows formatted output). */
	private final String ROW_FORMAT = "%-3s %-128s\n";
	/** A <code>String</code> defining the row format without a Type
	 * (rows formatted output. */
	private final String ROW_FORMAT_NO_TYPE = "%-128s\n";
    /** A <code>String</code> defining a simple row format (rows formatted output). */
	private final String ROW_FORMAT_SIMPLE = "%s %s\n";
	/** A <code>String</code> defining a simple row format without a Type
	 * (rows formatted output. */
	private final String ROW_FORMAT_NO_TYPE_SIMPLE = "%s\n";

	 /** Performed DNS Query, expressed as the FQDN to be looked up. */
	private final String query;
	/** Type of Resource Record. */
	private final String type;
	/** Received DNS Response, expressed as a list of obtained result(s). */
	private final List<String> results;


	/**
	 * Build a status change event up.
	 *
	 * @param query		A <code>String</code> containing the DNS Query
	 * @param type		A <code>String</code> containing the Resource Record Type
	 * @param results	A <code>List</code> containing the list of results
	 * @return	An instance of <code>StatusChangeEvent</code>
	 */
	public static StatusChangeEvent build(final String query, final String type, final List<String> results)
	{
		return new StatusChangeEvent(query, type, results);
	}

	/**
	 * Build a <code>String</code> casted <code>List</code> from a <code>Collection</code>
	 * of Object's children.
	 *
	 * @param src	Source <code>Collection</code> to be casted/converted
	 *
     * @return	A <code>List</code> of <code>String</code> represented objects
	 */
	public static List<String> castedList(Collection<? extends Object> src)
	{
		List<String> dst = new LinkedList<>();
		for(Object obj: src) {
			if(obj instanceof DiscoveryRecord)
                dst.add(((DiscoveryRecord) obj).toDisplay());
            else
                dst.add(obj.toString());
        }

		return dst;
	}

    /**
	 * Build a <code>String</code> casted <code>List</code> from an <code>Array</code>
	 * of objects
	 *
	 * @param src	Array to be casted/converted
	 *
     * @return	A <code>List</code> of <code>String</code> represented objects
	 */
    public static List<String> castedArray(Object[] src)
    {
        List<String> dst = new LinkedList<>();
        for(Object obj: src) {
            if(obj instanceof Record)
                dst.add(((Record) obj).rdataToString());
            else
                dst.add(obj.toString());
        }

        return dst;
    }

	/**
	 * Build a <code>String</code> casted <code>List</code> from a given <code>Object</code>
	 *
	 * @param src	<code>Object</code> to be casted/converted
	 * @return	A <code>List</code> of <code>String</code> represented objects
	 */
	public static List<String> castedValue(Object src)
	{
		List<String> dst = new LinkedList<>();
		if(src instanceof DiscoveryRecord)
            dst.add(((DiscoveryRecord) src).toDisplay());
        else
            dst.add(src.toString());

		return dst;
	}

	/**
	 * Format this class into a table, having the Triplet <Query,Type,Result(s)> as
	 * header.
	 *
	 * @return A tabular column-formatted <code>String</code>
	 */
	public String columnFormatted()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(String.format(HEADER_FORMAT, "Query", "Type", "Result(s)"));
		for(int i = 0; i < results.size(); i++) {
			if(i > 0)
				builder.append(String.format(BODY_FORMAT, "", type, results.get(i)));
			else
				builder.append(String.format(BODY_FORMAT, query, type, results.get(i)));
		}

		return builder.toString();
	}

	/**
	 * Format this class into a table without a specific header, organizing the
	 * Resource Records into contiguous rows.
	 *
	 * @return A tabular column-formatted <code>String</code>
	 */
	public String rowFormatted()
	{
		StringBuilder builder = new StringBuilder();
        if(results.size() > 0) {
            for(String result: results)
                builder.append((type.isEmpty()?String.format(ROW_FORMAT_NO_TYPE_SIMPLE, result):
                                               String.format(ROW_FORMAT_SIMPLE, type, result)));
        } else builder.append(String.format("No Record for [%s]\n", query));

		return FormattingUtil.response(builder.toString());
	}

	public String getQuery() { return query; }

	public List<String> getResults() { return results; }

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 83 * hash + Objects.hashCode(this.query);
		hash = 83 * hash + Objects.hashCode(this.results);
		return hash;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final StatusChangeEvent other = (StatusChangeEvent) obj;
		if (!Objects.equals(this.query, other.query)) {
			return false;
		}
		if (!Objects.equals(this.results, other.results)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return rowFormatted();
	}

	/**
	 * Two arguments constructor for building a status change event up.
	 *
	 * @param query		A <code>String</code> containing the DNS Query
	 * @param results	A <code>List</code> containing the list of results
	 */
	private StatusChangeEvent(String query, String type,  List<String> results)
	{
		this.query = query;
		this.type = type;
		this.results = results;
	}

}
