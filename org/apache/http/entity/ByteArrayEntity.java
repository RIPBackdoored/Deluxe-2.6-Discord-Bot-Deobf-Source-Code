/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class ByteArrayEntity
extends AbstractHttpEntity
implements Cloneable {
    @Deprecated
    protected final byte[] content;
    private final byte[] b;
    private final int off;
    private final int len;

    public ByteArrayEntity(byte[] b, ContentType contentType) {
        Args.notNull(b, "Source byte array");
        this.content = b;
        this.b = b;
        this.off = 0;
        this.len = this.b.length;
        if (contentType == null) return;
        this.setContentType(contentType.toString());
    }

    public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType) {
        Args.notNull(b, "Source byte array");
        if (off < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off > b.length) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        }
        this.content = b;
        this.b = b;
        this.off = off;
        this.len = len;
        if (contentType == null) return;
        this.setContentType(contentType.toString());
    }

    public ByteArrayEntity(byte[] b) {
        this(b, null);
    }

    public ByteArrayEntity(byte[] b, int off, int len) {
        this(b, off, len, null);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return this.len;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(this.b, this.off, this.len);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        outstream.write(this.b, this.off, this.len);
        outstream.flush();
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return Object.super.clone();
    }
}

