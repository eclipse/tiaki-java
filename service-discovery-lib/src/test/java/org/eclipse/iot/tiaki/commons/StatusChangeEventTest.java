/*
 * Copyright (c) 2015, Verisign, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package org.eclipse.iot.tiaki.commons;

import java.util.LinkedList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StatusChangeEventTest {

    public StatusChangeEventTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void rowColumnFormatting() {
        List<String> results = new LinkedList<>();
        results.add("Test result");
        results.add("Test result1");
        StatusChangeEvent event = StatusChangeEvent.build("_0._tcp.test.com", "PTR", results);
        System.out.println(event.rowFormatted());

        Assert.assertTrue(!event.columnFormatted().isEmpty());
        Assert.assertTrue(!event.rowFormatted().isEmpty());
    }
}
