/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class BasicHttpEntity
extends AbstractHttpEntity {
    private InputStream content;
    private long length = -1L;

    @Override
    public long getContentLength() {
        return this.length;
    }

    @Override
    public InputStream getContent() throws IllegalStateException {
        Asserts.check(this.content != null, "Content has not been provided");
        return this.content;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    public void setContentLength(long len) {
        this.length = len;
    }

    public void setContent(InputStream instream) {
        this.content = instream;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        InputStream instream = this.getContent();
        try {
            int l;
            byte[] tmp = new byte[4096];
            while ((l = instream.read(tmp)) != -1) {
                outstream.write(tmp, 0, l);
            }
            return;
        }
        finally {
            instream.close();
        }
    }

    @Override
    public boolean isStreaming() {
        if (this.content == null) return false;
        if (this.content == EmptyInputStream.INSTANCE) return false;
        return true;
    }
}

