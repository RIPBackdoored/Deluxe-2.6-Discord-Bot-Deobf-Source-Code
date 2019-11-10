/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.Utils;

public class QuotedPrintableCodec
implements BinaryEncoder,
BinaryDecoder,
StringEncoder,
StringDecoder {
    private final Charset charset;
    private static final BitSet PRINTABLE_CHARS;
    private static final byte ESCAPE_CHAR = 61;
    private static final byte TAB = 9;
    private static final byte SPACE = 32;

    public QuotedPrintableCodec() {
        this(Charsets.UTF_8);
    }

    public QuotedPrintableCodec(Charset charset) {
        this.charset = charset;
    }

    public QuotedPrintableCodec(String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
        this(Charset.forName(charsetName));
    }

    private static final void encodeQuotedPrintable(int b, ByteArrayOutputStream buffer) {
        buffer.write(61);
        char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 15, 16));
        char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
        buffer.write(hex1);
        buffer.write(hex2);
    }

    public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (printable == null) {
            printable = PRINTABLE_CHARS;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] arr$ = bytes;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            int c = arr$[i$];
            int b = c;
            if (b < 0) {
                b = 256 + b;
            }
            if (printable.get(b)) {
                buffer.write(b);
            } else {
                QuotedPrintableCodec.encodeQuotedPrintable(b, buffer);
            }
            ++i$;
        }
        return buffer.toByteArray();
    }

    public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < bytes.length) {
            byte b = bytes[i];
            if (b == 61) {
                try {
                    int u = Utils.digit16(bytes[++i]);
                    int l = Utils.digit16(bytes[++i]);
                    buffer.write((char)((u << 4) + l));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid quoted-printable encoding", e);
                }
            } else {
                buffer.write(b);
            }
            ++i;
        }
        return buffer.toByteArray();
    }

    @Override
    public byte[] encode(byte[] bytes) {
        return QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
    }

    @Override
    public byte[] decode(byte[] bytes) throws DecoderException {
        return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
    }

    @Override
    public String encode(String str) throws EncoderException {
        return this.encode(str, this.getCharset());
    }

    public String decode(String str, Charset charset) throws DecoderException {
        if (str != null) return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
        return null;
    }

    public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
        if (str != null) return new String(this.decode(StringUtils.getBytesUsAscii(str)), charset);
        return null;
    }

    @Override
    public String decode(String str) throws DecoderException {
        return this.decode(str, this.getCharset());
    }

    @Override
    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.encode((byte[])obj);
        }
        if (!(obj instanceof String)) throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable encoded");
        return this.encode((String)obj);
    }

    @Override
    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return this.decode((byte[])obj);
        }
        if (!(obj instanceof String)) throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be quoted-printable decoded");
        return this.decode((String)obj);
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public String encode(String str, Charset charset) {
        if (str != null) return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
        return null;
    }

    public String encode(String str, String charset) throws UnsupportedEncodingException {
        if (str != null) return StringUtils.newStringUsAscii(this.encode(str.getBytes(charset)));
        return null;
    }

    static {
        int i;
        PRINTABLE_CHARS = new BitSet(256);
        for (i = 33; i <= 60; ++i) {
            PRINTABLE_CHARS.set(i);
        }
        i = 62;
        do {
            if (i > 126) {
                PRINTABLE_CHARS.set(9);
                PRINTABLE_CHARS.set(32);
                return;
            }
            PRINTABLE_CHARS.set(i);
            ++i;
        } while (true);
    }
}

