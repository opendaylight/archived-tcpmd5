/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.nio;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketOption;
import java.nio.channels.NetworkChannel;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.opendaylight.tcpmd5.api.KeyAccess;
import org.opendaylight.tcpmd5.api.KeyAccessFactory;
import org.opendaylight.tcpmd5.api.KeyMapping;
import org.opendaylight.tcpmd5.api.MD5SocketOptions;

public class MD5ServerSocketChannelTest {
    @Mock
    private KeyAccessFactory keyAccessFactory;
    @Mock
    private KeyAccess keyAccess;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(keyAccess).when(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.doReturn(null).when(keyAccess).getKeys();
        Mockito.doNothing().when(keyAccess).setKeys(any(KeyMapping.class));
    }

    @Test
    public void testCreate() throws IOException {
        try (final MD5ServerSocketChannel sc = MD5ServerSocketChannel.open(keyAccessFactory)) {
            assertNotNull(sc.getDelegate());
            assertNull(sc.getLocalAddress());
            assertNotNull(sc.socket());

            final Set<SocketOption<?>> s = sc.supportedOptions();
            assertNotNull(s);
            assertTrue(s.contains(MD5SocketOptions.TCP_MD5SIG));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
    }

    @Test
    public void testGetKey() throws IOException {
        try (final MD5ServerSocketChannel sc = MD5ServerSocketChannel.open(keyAccessFactory)) {

            assertNull(sc.getOption(MD5SocketOptions.TCP_MD5SIG));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.verify(keyAccess).getKeys();
    }

    @Test
    public void testSetKey() throws IOException {
        final KeyMapping map = new KeyMapping();
        map.put(InetAddress.getLoopbackAddress(), new byte[] { 1, 2, 3 });

        try (final MD5ServerSocketChannel sc = MD5ServerSocketChannel.open(keyAccessFactory)) {
            assertSame(sc, sc.setOption(MD5SocketOptions.TCP_MD5SIG, map));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.verify(keyAccess).setKeys(map);
    }
}
