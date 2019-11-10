/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.codec;

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.EncoderException;

public interface BinaryEncoder
extends Encoder {
    public byte[] encode(byte[] var1) throws EncoderException;
}

