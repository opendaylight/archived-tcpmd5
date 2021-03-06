/*
 * Copyright (c) 2013 Robert Varga. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.api;

import java.net.SocketOption;

/**
 * Utility class holding the singleton socket options used by the TCP-MD5 support library.
 */
public final class MD5SocketOptions {
    /**
     * TCP MD5 Signature option, as defined in RFC 2385.
     */
    public static final SocketOption<KeyMapping> TCP_MD5SIG = new SocketOption<KeyMapping>() {
        @Override
        public String name() {
            return "TCP_MD5SIG";
        }

        @Override
        public Class<KeyMapping> type() {
            return KeyMapping.class;
        }
    };

    private MD5SocketOptions() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }
}
