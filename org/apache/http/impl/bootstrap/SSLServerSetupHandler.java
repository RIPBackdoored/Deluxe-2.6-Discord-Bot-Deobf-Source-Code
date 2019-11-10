/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.bootstrap;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;

public interface SSLServerSetupHandler {
    public void initialize(SSLServerSocket var1) throws SSLException;
}

