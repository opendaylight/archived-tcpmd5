/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5;

import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;
import org.opendaylight.tcpmd5.api.KeyMapping;

public class KeyMappingTest {
    @Test
    void instantiationTest() {
        Map<?, ?> k1 = new KeyMapping();
        Map<?, ?> k2 = new KeyMapping(2);
        Map<?, ?> k3 = new KeyMapping(2, (float)0.6);
        Map<?, ?> k4 = new KeyMapping(Collections.<InetAddress,byte[]>emptyMap());

        assertTrue(k1.isEmpty());
        assertTrue(k2.isEmpty());
        assertTrue(k3.isEmpty());
        assertTrue(k4.isEmpty());
    }
}
