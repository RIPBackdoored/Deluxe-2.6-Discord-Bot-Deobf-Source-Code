/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public final class EntityUtils {
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private EntityUtils() {
    }

    public static void consumeQuietly(HttpEntity entity) {
        try {
            EntityUtils.consume(entity);
            return;
        }
        catch (IOException ignore) {
            // empty catch block
        }
    }

    public static void consume(HttpEntity entity) throws IOException {
        if (entity == null) {
            return;
        }
        if (!entity.isStreaming()) return;
        InputStream instream = entity.getContent();
        if (instream == null) return;
        instream.close();
    }

    public static void updateEntity(HttpResponse response, HttpEntity entity) throws IOException {
        Args.notNull(response, "Response");
        EntityUtils.consume(response.getEntity());
        response.setEntity(entity);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] toByteArray(HttpEntity entity) throws IOException {
        Args.notNull(entity, "Entity");
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            int l;
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
            int capacity = (int)entity.getContentLength();
            if (capacity < 0) {
                capacity = 4096;
            }
            ByteArrayBuffer buffer = new ByteArrayBuffer(capacity);
            byte[] tmp = new byte[4096];
            while ((l = instream.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            byte[] arrby = buffer.toByteArray();
            return arrby;
        }
        finally {
            instream.close();
        }
    }

    @Deprecated
    public static String getContentCharSet(HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String charset = null;
        if (entity.getContentType() == null) return charset;
        HeaderElement[] values = entity.getContentType().getElements();
        if (values.length <= 0) return charset;
        NameValuePair param = values[0].getParameterByName("charset");
        if (param == null) return charset;
        return param.getValue();
    }

    @Deprecated
    public static String getContentMimeType(HttpEntity entity) throws ParseException {
        Args.notNull(entity, "Entity");
        String mimeType = null;
        if (entity.getContentType() == null) return mimeType;
        HeaderElement[] values = entity.getContentType().getElements();
        if (values.length <= 0) return mimeType;
        return values[0].getName();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String toString(HttpEntity entity, ContentType contentType) throws IOException {
        InputStream instream = entity.getContent();
        if (instream == null) {
            return null;
        }
        try {
            int l;
            Args.check(entity.getContentLength() <= Integer.MAX_VALUE, "HTTP entity too large to be buffered in memory");
            int capacity = (int)entity.getContentLength();
            if (capacity < 0) {
                capacity = 4096;
            }
            Charset charset = null;
            if (contentType != null && (charset = contentType.getCharset()) == null) {
                ContentType defaultContentType = ContentType.getByMimeType(contentType.getMimeType());
                Charset charset2 = charset = defaultContentType != null ? defaultContentType.getCharset() : null;
            }
            if (charset == null) {
                charset = HTTP.DEF_CONTENT_CHARSET;
            }
            InputStreamReader reader = new InputStreamReader(instream, charset);
            CharArrayBuffer buffer = new CharArrayBuffer(capacity);
            char[] tmp = new char[1024];
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
            String string = buffer.toString();
            return string;
        }
        finally {
            instream.close();
        }
    }

    public static String toString(HttpEntity entity, Charset defaultCharset) throws IOException, ParseException {
        ContentType contentType;
        block3 : {
            Args.notNull(entity, "Entity");
            contentType = null;
            try {
                contentType = ContentType.get(entity);
            }
            catch (UnsupportedCharsetException ex) {
                if (defaultCharset != null) break block3;
                throw new UnsupportedEncodingException(ex.getMessage());
            }
        }
        if (contentType != null) {
            if (contentType.getCharset() != null) return EntityUtils.toString(entity, contentType);
            contentType = contentType.withCharset(defaultCharset);
            return EntityUtils.toString(entity, contentType);
        }
        contentType = ContentType.DEFAULT_TEXT.withCharset(defaultCharset);
        return EntityUtils.toString(entity, contentType);
    }

    public static String toString(HttpEntity entity, String defaultCharset) throws IOException, ParseException {
        Charset charset;
        if (defaultCharset != null) {
            charset = Charset.forName(defaultCharset);
            return EntityUtils.toString(entity, charset);
        }
        charset = null;
        return EntityUtils.toString(entity, charset);
    }

    public static String toString(HttpEntity entity) throws IOException, ParseException {
        Args.notNull(entity, "Entity");
        return EntityUtils.toString(entity, ContentType.get(entity));
    }
}

