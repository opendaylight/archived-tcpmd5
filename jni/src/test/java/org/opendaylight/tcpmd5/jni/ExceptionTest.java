/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.jni;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/*
 * Dummy tests to increase coverage.
 */
public final class ExceptionTest {
    @Test
    public void testNativeSupportUnavailableException() {
        Exception e1 = new NativeSupportUnavailableException("foo");
        assertEquals("foo", e1.getMessage());

        Exception e2 = new NativeSupportUnavailableException("bar", e1);
        assertEquals("bar", e2.getMessage());
        assertEquals(e1, e2.getCause());
    }
}
