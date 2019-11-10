/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.binary;

import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

public class Base64InputStream
extends BaseNCodecInputStream {
    public Base64InputStream(InputStream in) {
        this(in, false);
    }

    public Base64InputStream(InputStream in, boolean doEncode) {
        super(in, new Base64(false), doEncode);
    }

    public Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(in, new Base64(lineLength, lineSeparator), doEncode);
    }
}

