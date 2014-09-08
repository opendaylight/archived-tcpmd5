/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.nio;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.StandardProtocolFamily;

import org.junit.Before;
import org.junit.Test;
import org.opendaylight.tcpmd5.api.DummyKeyAccessFactory;

public class MD5SelectorProviderTest {
    private MD5SelectorProvider provider;

    @Before
    public void setUp() {
        provider = MD5SelectorProvider.getInstance(DummyKeyAccessFactory.getInstance());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testDatagram() throws IOException {
        provider.openDatagramChannel();
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testInetDatagram() throws IOException {
        provider.openDatagramChannel(StandardProtocolFamily.INET);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testPipe() throws IOException {
        provider.openPipe();
    }

    @Test
    public void testOpenSelector() throws IOException {
        assertNotNull(provider.openSelector());
    }

    @Test
    public void testOpenSocketChannel() throws IOException {
        try (final MD5SocketChannel s = provider.openSocketChannel()) {
            assertNotNull(s);
        }
    }

    @Test
    public void testOpenServerSocketChannel() throws IOException {
        try (final MD5ServerSocketChannel s = provider.openServerSocketChannel()) {
            assertNotNull(s);
        }
    }
}
