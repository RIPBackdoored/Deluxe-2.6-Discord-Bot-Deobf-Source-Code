/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

public class StringEntity
extends AbstractHttpEntity
implements Cloneable {
    protected final byte[] content;

    public StringEntity(String string, ContentType contentType) throws UnsupportedCharsetException {
        Charset charset;
        Args.notNull(string, "Source string");
        Charset charset2 = charset = contentType != null ? contentType.getCharset() : null;
        if (charset == null) {
            charset = HTTP.DEF_CONTENT_CHARSET;
        }
        this.content = string.getBytes(charset);
        if (contentType == null) return;
        this.setContentType(contentType.toString());
    }

    @Deprecated
    public StringEntity(String string, String mimeType, String charset) throws UnsupportedEncodingException {
        Args.notNull(string, "Source string");
        String mt = mimeType != null ? mimeType : "text/plain";
        String cs = charset != null ? charset : "ISO-8859-1";
        this.content = string.getBytes(cs);
        this.setContentType(mt + "; charset=" + cs);
    }

    public StringEntity(String string, String charset) throws UnsupportedCharsetException {
        this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
    }

    public StringEntity(String string, Charset charset) {
        this(string, ContentType.create(ContentType.TEXT_PLAIN.getMimeType(), charset));
    }

    public StringEntity(String string) throws UnsupportedEncodingException {
        this(string, ContentType.DEFAULT_TEXT);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return this.content.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        outstream.write(this.content);
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

