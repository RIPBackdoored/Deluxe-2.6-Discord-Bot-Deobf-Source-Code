/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;

public class BufferedHttpEntity
extends HttpEntityWrapper {
    private final byte[] buffer;

    public BufferedHttpEntity(HttpEntity entity) throws IOException {
        super(entity);
        if (entity.isRepeatable() && entity.getContentLength() >= 0L) {
            this.buffer = null;
            return;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeTo(out);
        out.flush();
        this.buffer = out.toByteArray();
    }

    @Override
    public long getContentLength() {
        if (this.buffer == null) return super.getContentLength();
        return this.buffer.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        if (this.buffer == null) return super.getContent();
        return new ByteArrayInputStream(this.buffer);
    }

    @Override
    public boolean isChunked() {
        if (this.buffer != null) return false;
        if (!super.isChunked()) return false;
        return true;
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        if (this.buffer != null) {
            outstream.write(this.buffer);
            return;
        }
        super.writeTo(outstream);
    }

    @Override
    public boolean isStreaming() {
        if (this.buffer != null) return false;
        if (!super.isStreaming()) return false;
        return true;
    }
}

