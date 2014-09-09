/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.nio;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.NetworkChannel;
import java.nio.channels.NoConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
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

public class MD5SocketChannelTest {
    @Mock
    private KeyAccessFactory keyAccessFactory;
    @Mock
    private KeyAccess keyAccess;

    private final ByteBuffer buf = ByteBuffer.allocate(0);

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(keyAccess).when(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.doReturn(null).when(keyAccess).getKeys();
        Mockito.doNothing().when(keyAccess).setKeys(any(KeyMapping.class));
    }

    @Test
    public void testCreate() throws IOException {
        try (final MD5SocketChannel sc = MD5SocketChannel.open(keyAccessFactory)) {
            assertNotNull(sc.getDelegate());
            assertNull(sc.getLocalAddress());
            assertNull(sc.getRemoteAddress());
            assertFalse(sc.isConnected());
            assertFalse(sc.isConnectionPending());
            assertNotNull(sc.socket());

            final Set<SocketOption<?>> s = sc.supportedOptions();
            assertNotNull(s);
            assertTrue(s.contains(MD5SocketOptions.TCP_MD5SIG));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
    }

    @Test
    public void testGetKey() throws IOException {
        try (final MD5SocketChannel sc = MD5SocketChannel.open(keyAccessFactory)) {

            assertNull(sc.getOption(MD5SocketOptions.TCP_MD5SIG));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.verify(keyAccess).getKeys();
    }

    @Test
    public void testSetKey() throws IOException {
        final KeyMapping map = new KeyMapping();
        map.put(InetAddress.getLoopbackAddress(), new byte[] { 1, 2, 3 });

        try (final MD5SocketChannel sc = MD5SocketChannel.open(keyAccessFactory)) {
            assertSame(sc, sc.setOption(MD5SocketOptions.TCP_MD5SIG, map));
        }

        Mockito.verify(keyAccessFactory).getKeyAccess(any(NetworkChannel.class));
        Mockito.verify(keyAccess).setKeys(map);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructor() throws IOException {
        new MD5SocketChannel(null).close();
    }

    @Test
    public void testOpen() throws IOException {
        MD5SocketChannel.open().close();
    }

    @Test
    public void testBind() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.bind(null);
        }
    }

    @Test
    public void testBlocking() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.implConfigureBlocking(true);
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testShutdownInput() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.shutdownOutput();
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testShutdownOutput() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.shutdownOutput();
        }
    }

    @Test(expected=NullPointerException.class)
    public void testConnect() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.connect(null);
        }
    }

    @Test(expected=NoConnectionPendingException.class)
    public void testFinishConnect() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.finishConnect();
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testRead() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.read(buf);
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testReadOffset() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.read(new ByteBuffer[0], 0, 0);
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testWrite() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.write(buf);
        }
    }

    @Test(expected=NotYetConnectedException.class)
    public void testWriteOffset() throws IOException {
        try (MD5SocketChannel sc = MD5SocketChannel.open()) {
            sc.write(new ByteBuffer[0], 0, 0);
        }
    }
}
