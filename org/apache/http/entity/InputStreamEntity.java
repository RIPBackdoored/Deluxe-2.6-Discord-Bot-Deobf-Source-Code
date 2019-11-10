/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class InputStreamEntity
extends AbstractHttpEntity {
    private final InputStream content;
    private final long length;

    public InputStreamEntity(InputStream instream) {
        this(instream, -1L);
    }

    public InputStreamEntity(InputStream instream, long length) {
        this(instream, length, null);
    }

    public InputStreamEntity(InputStream instream, ContentType contentType) {
        this(instream, -1L, contentType);
    }

    public InputStreamEntity(InputStream instream, long length, ContentType contentType) {
        this.content = Args.notNull(instream, "Source input stream");
        this.length = length;
        if (contentType == null) return;
        this.setContentType(contentType.toString());
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return this.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.content;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        InputStream instream = this.content;
        try {
            byte[] buffer = new byte[4096];
            if (this.length < 0L) {
                int l;
                while ((l = instream.read(buffer)) != -1) {
                    outstream.write(buffer, 0, l);
                }
                return;
            }
            long remaining = this.length;
            while (remaining > 0L) {
                int l = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
                if (l == -1) {
                    return;
                }
                outstream.write(buffer, 0, l);
                remaining -= (long)l;
            }
            return;
        }
        finally {
            instream.close();
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
}

