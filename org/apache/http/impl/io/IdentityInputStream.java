/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

public class IdentityInputStream
extends InputStream {
    private final SessionInputBuffer in;
    private boolean closed = false;

    public IdentityInputStream(SessionInputBuffer in) {
        this.in = Args.notNull(in, "Session input buffer");
    }

    @Override
    public int available() throws IOException {
        if (!(this.in instanceof BufferInfo)) return 0;
        return ((BufferInfo)((Object)this.in)).length();
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
    }

    @Override
    public int read() throws IOException {
        if (!this.closed) return this.in.read();
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (!this.closed) return this.in.read(b, off, len);
        return -1;
    }
}

