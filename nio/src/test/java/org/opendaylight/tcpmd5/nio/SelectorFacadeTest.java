/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.nio;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

import org.junit.Before;
import org.junit.Test;

public class SelectorFacadeTest {

    private AbstractSelector mock;

    @Before
    public void setUp() throws IOException {
        mock = mock(AbstractSelector.class);
        doReturn(null).when(mock).keys();
        doReturn(null).when(mock).selectedKeys();
        doReturn(0).when(mock).selectNow();
        doReturn(0).when(mock).select(any(Integer.class));
        doReturn(0).when(mock).select();
        doReturn(null).when(mock).wakeup();
    }

    @Test
    public void testSelectorFacade() throws IOException {
        final SelectorFacade f = new SelectorFacade(SelectorProvider.provider(), mock);

        f.keys();
        f.selectedKeys();
        f.selectNow();
        f.select(1L);
        f.select();
        f.wakeup();

        verify(mock).keys();
        verify(mock).selectedKeys();
        verify(mock).selectNow();
        verify(mock).keys();
        verify(mock).select(any(Integer.class));
        verify(mock).wakeup();
    }
}
