/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.util;

import java.io.Serializable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public final class ByteArrayBuffer
implements Serializable {
    private static final long serialVersionUID = 4359112959524048036L;
    private byte[] buffer;
    private int len;

    public ByteArrayBuffer(int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new byte[capacity];
    }

    private void expand(int newlen) {
        byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public void append(byte[] b, int off, int len) {
        if (b == null) {
            return;
        }
        if (off < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off > b.length) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        }
        if (len == 0) {
            return;
        }
        int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        System.arraycopy(b, off, this.buffer, this.len, len);
        this.len = newlen;
    }

    public void append(int b) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        this.buffer[this.len] = (byte)b;
        this.len = newlen;
    }

    public void append(char[] b, int off, int len) {
        if (b == null) {
            return;
        }
        if (off < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off > b.length) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len < 0) throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b.length);
        }
        if (len == 0) {
            return;
        }
        int oldlen = this.len;
        int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        int i1 = off;
        int i2 = oldlen;
        do {
            if (i2 >= newlen) {
                this.len = newlen;
                return;
            }
            this.buffer[i2] = (byte)b[i1];
            ++i1;
            ++i2;
        } while (true);
    }

    public void append(CharArrayBuffer b, int off, int len) {
        if (b == null) {
            return;
        }
        this.append(b.buffer(), off, len);
    }

    public void clear() {
        this.len = 0;
    }

    public byte[] toByteArray() {
        byte[] b = new byte[this.len];
        if (this.len <= 0) return b;
        System.arraycopy(this.buffer, 0, b, 0, this.len);
        return b;
    }

    public int byteAt(int i) {
        return this.buffer[i];
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int length() {
        return this.len;
    }

    public void ensureCapacity(int required) {
        if (required <= 0) {
            return;
        }
        int available = this.buffer.length - this.len;
        if (required <= available) return;
        this.expand(this.len + required);
    }

    public byte[] buffer() {
        return this.buffer;
    }

    public void setLength(int len) {
        if (len < 0) throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
        if (len > this.buffer.length) {
            throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
        }
        this.len = len;
    }

    public boolean isEmpty() {
        if (this.len != 0) return false;
        return true;
    }

    public boolean isFull() {
        if (this.len != this.buffer.length) return false;
        return true;
    }

    public int indexOf(byte b, int from, int to) {
        int endIndex;
        int beginIndex = from;
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if ((endIndex = to) > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        int i = beginIndex;
        while (i < endIndex) {
            if (this.buffer[i] == b) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public int indexOf(byte b) {
        return this.indexOf(b, 0, this.len);
    }
}

