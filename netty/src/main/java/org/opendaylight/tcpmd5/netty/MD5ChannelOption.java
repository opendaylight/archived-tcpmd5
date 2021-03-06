/*
 * Copyright (c) 2013 Robert Varga. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.netty;

import io.netty.channel.ChannelOption;

import org.opendaylight.tcpmd5.api.KeyMapping;

/**
 * TCP MD5 Signature {@link ChannelOption}.
 */
public final class MD5ChannelOption extends ChannelOption<KeyMapping> {
    /**
     * Singleton instance of TCP MD5 Signature ChannelOption.
     */
    public static final MD5ChannelOption TCP_MD5SIG = new MD5ChannelOption("TCP_MD5SIG");

    @SuppressWarnings("deprecation")
    private MD5ChannelOption(final String name) {
        super(name);
    }
}
