/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.BaseNCodec;

public class BaseNCodecInputStream
extends FilterInputStream {
    private final BaseNCodec baseNCodec;
    private final boolean doEncode;
    private final byte[] singleByte = new byte[1];
    private final BaseNCodec.Context context = new BaseNCodec.Context();

    protected BaseNCodecInputStream(InputStream in, BaseNCodec baseNCodec, boolean doEncode) {
        super(in);
        this.doEncode = doEncode;
        this.baseNCodec = baseNCodec;
    }

    @Override
    public int available() throws IOException {
        if (!this.context.eof) return 1;
        return 0;
    }

    @Override
    public synchronized void mark(int readLimit) {
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        int n;
        int r = this.read(this.singleByte, 0, 1);
        while (r == 0) {
            r = this.read(this.singleByte, 0, 1);
        }
        if (r <= 0) return -1;
        int b = this.singleByte[0];
        if (b < 0) {
            n = 256 + b;
            return n;
        }
        n = b;
        return n;
    }

    @Override
    public int read(byte[] b, int offset, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if (offset < 0) throw new IndexOutOfBoundsException();
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (offset > b.length) throw new IndexOutOfBoundsException();
        if (offset + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        int readLen = 0;
        while (readLen == 0) {
            if (!this.baseNCodec.hasData(this.context)) {
                byte[] buf = new byte[this.doEncode ? 4096 : 8192];
                int c = this.in.read(buf);
                if (this.doEncode) {
                    this.baseNCodec.encode(buf, 0, c, this.context);
                } else {
                    this.baseNCodec.decode(buf, 0, c, this.context);
                }
            }
            readLen = this.baseNCodec.readResults(b, offset, len, this.context);
        }
        return readLen;
    }

    @Override
    public synchronized void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override
    public long skip(long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("Negative skip length: " + n);
        }
        byte[] b = new byte[512];
        long todo = n;
        while (todo > 0L) {
            int len = (int)Math.min((long)b.length, todo);
            if ((len = this.read(b, 0, len)) == -1) {
                return n - todo;
            }
            todo -= (long)len;
        }
        return n - todo;
    }
}

