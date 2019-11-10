/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.apache.http.MessageConstraintException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

public class SessionInputBufferImpl
implements SessionInputBuffer,
BufferInfo {
    private final HttpTransportMetricsImpl metrics;
    private final byte[] buffer;
    private final ByteArrayBuffer linebuffer;
    private final int minChunkLimit;
    private final MessageConstraints constraints;
    private final CharsetDecoder decoder;
    private InputStream instream;
    private int bufferpos;
    private int bufferlen;
    private CharBuffer cbuf;

    public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize, int minChunkLimit, MessageConstraints constraints, CharsetDecoder chardecoder) {
        Args.notNull(metrics, "HTTP transport metrcis");
        Args.positive(buffersize, "Buffer size");
        this.metrics = metrics;
        this.buffer = new byte[buffersize];
        this.bufferpos = 0;
        this.bufferlen = 0;
        this.minChunkLimit = minChunkLimit >= 0 ? minChunkLimit : 512;
        this.constraints = constraints != null ? constraints : MessageConstraints.DEFAULT;
        this.linebuffer = new ByteArrayBuffer(buffersize);
        this.decoder = chardecoder;
    }

    public SessionInputBufferImpl(HttpTransportMetricsImpl metrics, int buffersize) {
        this(metrics, buffersize, buffersize, null, null);
    }

    public void bind(InputStream instream) {
        this.instream = instream;
    }

    public boolean isBound() {
        if (this.instream == null) return false;
        return true;
    }

    @Override
    public int capacity() {
        return this.buffer.length;
    }

    @Override
    public int length() {
        return this.bufferlen - this.bufferpos;
    }

    @Override
    public int available() {
        return this.capacity() - this.length();
    }

    private int streamRead(byte[] b, int off, int len) throws IOException {
        Asserts.notNull(this.instream, "Input stream");
        return this.instream.read(b, off, len);
    }

    public int fillBuffer() throws IOException {
        int off;
        int len;
        int l;
        if (this.bufferpos > 0) {
            int len2 = this.bufferlen - this.bufferpos;
            if (len2 > 0) {
                System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, len2);
            }
            this.bufferpos = 0;
            this.bufferlen = len2;
        }
        if ((l = this.streamRead(this.buffer, off = this.bufferlen, len = this.buffer.length - off)) == -1) {
            return -1;
        }
        this.bufferlen = off + l;
        this.metrics.incrementBytesTransferred(l);
        return l;
    }

    public boolean hasBufferedData() {
        if (this.bufferpos >= this.bufferlen) return false;
        return true;
    }

    public void clear() {
        this.bufferpos = 0;
        this.bufferlen = 0;
    }

    @Override
    public int read() throws IOException {
        int noRead;
        do {
            if (this.hasBufferedData()) return this.buffer[this.bufferpos++] & 255;
        } while ((noRead = this.fillBuffer()) != -1);
        return -1;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int noRead;
        if (b == null) {
            return 0;
        }
        if (this.hasBufferedData()) {
            int chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        }
        if (len > this.minChunkLimit) {
            int read = this.streamRead(b, off, len);
            if (read <= 0) return read;
            this.metrics.incrementBytesTransferred(read);
            return read;
        }
        do {
            if (!this.hasBufferedData()) continue;
            int chunk = Math.min(len, this.bufferlen - this.bufferpos);
            System.arraycopy(this.buffer, this.bufferpos, b, off, chunk);
            this.bufferpos += chunk;
            return chunk;
        } while ((noRead = this.fillBuffer()) != -1);
        return -1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (b != null) return this.read(b, 0, b.length);
        return 0;
    }

    @Override
    public int readLine(CharArrayBuffer charbuffer) throws IOException {
        Args.notNull(charbuffer, "Char array buffer");
        int maxLineLen = this.constraints.getMaxLineLength();
        int noRead = 0;
        boolean retry = true;
        do {
            int currentLen;
            int len;
            if (!retry) {
                if (noRead != -1) return this.lineFromLineBuffer(charbuffer);
                if (!this.linebuffer.isEmpty()) return this.lineFromLineBuffer(charbuffer);
                return -1;
            }
            int pos = -1;
            for (int i = this.bufferpos; i < this.bufferlen; ++i) {
                if (this.buffer[i] != 10) continue;
                pos = i;
                break;
            }
            if (maxLineLen > 0 && (currentLen = this.linebuffer.length() + (pos >= 0 ? pos : this.bufferlen) - this.bufferpos) >= maxLineLen) {
                throw new MessageConstraintException("Maximum line length limit exceeded");
            }
            if (pos != -1) {
                if (this.linebuffer.isEmpty()) {
                    return this.lineFromReadBuffer(charbuffer, pos);
                }
                retry = false;
                len = pos + 1 - this.bufferpos;
                this.linebuffer.append(this.buffer, this.bufferpos, len);
                this.bufferpos = pos + 1;
                continue;
            }
            if (this.hasBufferedData()) {
                len = this.bufferlen - this.bufferpos;
                this.linebuffer.append(this.buffer, this.bufferpos, len);
                this.bufferpos = this.bufferlen;
            }
            if ((noRead = this.fillBuffer()) != -1) continue;
            retry = false;
        } while (true);
    }

    private int lineFromLineBuffer(CharArrayBuffer charbuffer) throws IOException {
        int len = this.linebuffer.length();
        if (len > 0) {
            if (this.linebuffer.byteAt(len - 1) == 10) {
                --len;
            }
            if (len > 0 && this.linebuffer.byteAt(len - 1) == 13) {
                --len;
            }
        }
        if (this.decoder == null) {
            charbuffer.append(this.linebuffer, 0, len);
        } else {
            ByteBuffer bbuf = ByteBuffer.wrap(this.linebuffer.buffer(), 0, len);
            len = this.appendDecoded(charbuffer, bbuf);
        }
        this.linebuffer.clear();
        return len;
    }

    private int lineFromReadBuffer(CharArrayBuffer charbuffer, int position) throws IOException {
        int pos = position;
        int off = this.bufferpos;
        this.bufferpos = pos + 1;
        if (pos > off && this.buffer[pos - 1] == 13) {
            --pos;
        }
        int len = pos - off;
        if (this.decoder == null) {
            charbuffer.append(this.buffer, off, len);
            return len;
        }
        ByteBuffer bbuf = ByteBuffer.wrap(this.buffer, off, len);
        return this.appendDecoded(charbuffer, bbuf);
    }

    private int appendDecoded(CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
        if (!bbuf.hasRemaining()) {
            return 0;
        }
        if (this.cbuf == null) {
            this.cbuf = CharBuffer.allocate(1024);
        }
        this.decoder.reset();
        int len = 0;
        do {
            CoderResult result;
            if (!bbuf.hasRemaining()) {
                result = this.decoder.flush(this.cbuf);
                this.cbuf.clear();
                return len += this.handleDecodingResult(result, charbuffer, bbuf);
            }
            result = this.decoder.decode(bbuf, this.cbuf, true);
            len += this.handleDecodingResult(result, charbuffer, bbuf);
        } while (true);
    }

    private int handleDecodingResult(CoderResult result, CharArrayBuffer charbuffer, ByteBuffer bbuf) throws IOException {
        if (result.isError()) {
            result.throwException();
        }
        this.cbuf.flip();
        int len = this.cbuf.remaining();
        do {
            if (!this.cbuf.hasRemaining()) {
                this.cbuf.compact();
                return len;
            }
            charbuffer.append(this.cbuf.get());
        } while (true);
    }

    @Override
    public String readLine() throws IOException {
        CharArrayBuffer charbuffer = new CharArrayBuffer(64);
        int l = this.readLine(charbuffer);
        if (l == -1) return null;
        return charbuffer.toString();
    }

    @Override
    public boolean isDataAvailable(int timeout) throws IOException {
        return this.hasBufferedData();
    }

    @Override
    public HttpTransportMetrics getMetrics() {
        return this.metrics;
    }
}

