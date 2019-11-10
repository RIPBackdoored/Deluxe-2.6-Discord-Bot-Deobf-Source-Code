/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.pool;

import java.io.IOException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.pool.PoolEntry;

@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class BasicPoolEntry
extends PoolEntry<HttpHost, HttpClientConnection> {
    public BasicPoolEntry(String id, HttpHost route, HttpClientConnection conn) {
        super(id, route, conn);
    }

    @Override
    public void close() {
        try {
            ((HttpClientConnection)this.getConnection()).close();
            return;
        }
        catch (IOException ignore) {
            // empty catch block
        }
    }

    @Override
    public boolean isClosed() {
        if (((HttpClientConnection)this.getConnection()).isOpen()) return false;
        return true;
    }
}

