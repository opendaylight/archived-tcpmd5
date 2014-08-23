/*
/*
 * Copyright (c) 2013 Robert Varga. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.tcpmd5.jni;

import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.opendaylight.tcpmd5.api.KeyAccess;
import org.opendaylight.tcpmd5.api.KeyAccessFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NativeKeyAccessFactory implements KeyAccessFactory {
    private static final Logger LOG = LoggerFactory.getLogger(NativeKeyAccessFactory.class);
    private static final String LIBNAME = "libtcpmd5-jni.so";
    private static volatile NativeKeyAccessFactory instance = null;

    private final Map<Channel, KeyAccess> channels = new WeakHashMap<>();

    private NativeKeyAccessFactory() {

    }

    /**
     * Get the singleton instance.
     *
     * @return Singleton service instance.
     * @throws NativeSupportUnavailableException if run-time does not support the functions needed to instantiate an
     *         operational factory.
     */
    public static NativeKeyAccessFactory getInstance() throws NativeSupportUnavailableException {
        if (instance != null) {
            return instance;
        }

        synchronized (NativeKeyAccessFactory.class) {
            if (instance == null) {
                try {
                    loadLibrary();
                } catch (IOException | RuntimeException e) {
                    throw new NativeSupportUnavailableException("Failed to load native library. Is it present and are you running a supported operating system?", e);
                }

                int rt = NarSystem.runUnitTests();
                if (rt == 0) {
                    throw new NativeSupportUnavailableException("Run-time does not support required functionality. Is your operating system configured correcty?");
                }

                LOG.debug("Run-time found {} supported channel classes", rt);
                instance = new NativeKeyAccessFactory();
            }
            return instance;
        }
    }

    @Override
    public KeyAccess getKeyAccess(final Channel channel) {
        if (!NativeKeyAccess.isClassSupported0(channel.getClass())) {
            LOG.debug("No support available for class {}", channel.getClass());
            return null;
        }

        synchronized (channels) {
            KeyAccess e = channels.get(channel);
            if (e == null) {
                e = new NativeKeyAccess(channel);
                channels.put(channel, e);
            }

            return e;
        }
    }

    @Override
    public boolean canHandleChannelClass(final Class<? extends Channel> clazz) {
        if (!NativeKeyAccess.isClassSupported0(Preconditions.checkNotNull(clazz))) {
            LOG.debug("No support available for class {}", clazz);
            return false;
        }

        return true;
    }

    private static void loadStream(final InputStream is) throws IOException {
        final Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxr-x---");
        final Path p = Files.createTempFile(LIBNAME, null, PosixFilePermissions.asFileAttribute(perms));

        try {
            LOG.debug("Copying {} to {}", is, p);

            Files.copy(is, p, StandardCopyOption.REPLACE_EXISTING);

            try {
                Runtime.getRuntime().load(p.toString());
                LOG.info("Library {} loaded", p);
            } catch (UnsatisfiedLinkError e) {
                throw new IOException(String.format("Failed to load library %s", p), e);
            }
        } finally {
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                LOG.warn("Failed to remove temporary file {}", p, e);
            }
        }
    }

    private static void loadLibrary() throws IOException {
        try (final InputStream is = NativeKeyAccessFactory.class.getResourceAsStream('/' + LIBNAME)) {
            if (is == null) {
                throw new IOException(String.format("Failed to open library resource %s", LIBNAME));
            }

            loadStream(is);
        }
    }
}
