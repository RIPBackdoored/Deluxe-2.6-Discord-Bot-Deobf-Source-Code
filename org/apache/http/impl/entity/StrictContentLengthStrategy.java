/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class StrictContentLengthStrategy
implements ContentLengthStrategy {
    public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
    private final int implicitLen;

    public StrictContentLengthStrategy(int implicitLen) {
        this.implicitLen = implicitLen;
    }

    public StrictContentLengthStrategy() {
        this(-1);
    }

    @Override
    public long determineLength(HttpMessage message) throws HttpException {
        Args.notNull(message, "HTTP message");
        Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
        if (transferEncodingHeader != null) {
            String s = transferEncodingHeader.getValue();
            if ("chunked".equalsIgnoreCase(s)) {
                if (!message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) return -2L;
                throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
            }
            if (!"identity".equalsIgnoreCase(s)) throw new ProtocolException("Unsupported transfer encoding: " + s);
            return -1L;
        }
        Header contentLengthHeader = message.getFirstHeader("Content-Length");
        if (contentLengthHeader == null) return this.implicitLen;
        String s = contentLengthHeader.getValue();
        try {
            long len = Long.parseLong(s);
            if (len >= 0L) return len;
            throw new ProtocolException("Negative content length: " + s);
        }
        catch (NumberFormatException e) {
            throw new ProtocolException("Invalid content length: " + s);
        }
    }
}

