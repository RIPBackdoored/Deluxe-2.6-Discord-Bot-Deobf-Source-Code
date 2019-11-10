/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnection;

public interface HttpConnectionFactory<T extends HttpConnection> {
    public T createConnection(Socket var1) throws IOException;
}

