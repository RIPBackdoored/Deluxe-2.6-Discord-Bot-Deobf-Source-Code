/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec.binary;

import java.io.OutputStream;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;

public class Base32OutputStream
extends BaseNCodecOutputStream {
    public Base32OutputStream(OutputStream out) {
        this(out, true);
    }

    public Base32OutputStream(OutputStream out, boolean doEncode) {
        super(out, new Base32(false), doEncode);
    }

    public Base32OutputStream(OutputStream out, boolean doEncode, int lineLength, byte[] lineSeparator) {
        super(out, new Base32(lineLength, lineSeparator), doEncode);
    }
}

