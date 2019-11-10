/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.net.InetAddress;
import org.apache.http.HttpConnection;

public interface HttpInetConnection
extends HttpConnection {
    public InetAddress getLocalAddress();

    public int getLocalPort();

    public InetAddress getRemoteAddress();

    public int getRemotePort();
}

