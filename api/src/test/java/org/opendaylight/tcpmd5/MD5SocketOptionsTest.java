/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.opendaylight.tcpmd5.api.KeyMapping;
import org.opendaylight.tcpmd5.api.MD5SocketOptions;

public class MD5SocketOptionsTest {

    @Test
    public void testMd5SigName() {
        assertEquals("TCP_MD5SIG", MD5SocketOptions.TCP_MD5SIG.name());
    }

    @Test
    public void testMd5SigType() {
        assertEquals(KeyMapping.class, MD5SocketOptions.TCP_MD5SIG.type());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testConstructor() throws Throwable {
        final Constructor<MD5SocketOptions> c = MD5SocketOptions.class.getDeclaredConstructor();
        c.setAccessible(true);

        try {
            c.newInstance();
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
