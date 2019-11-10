/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;

public final class ConnSupport {
    public static CharsetDecoder createDecoder(ConnectionConfig cconfig) {
        CodingErrorAction codingErrorAction;
        if (cconfig == null) {
            return null;
        }
        Charset charset = cconfig.getCharset();
        CodingErrorAction malformed = cconfig.getMalformedInputAction();
        CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
        if (charset == null) return null;
        CodingErrorAction codingErrorAction2 = malformed != null ? malformed : CodingErrorAction.REPORT;
        if (unmappable != null) {
            codingErrorAction = unmappable;
            return charset.newDecoder().onMalformedInput(codingErrorAction2).onUnmappableCharacter(codingErrorAction);
        }
        codingErrorAction = CodingErrorAction.REPORT;
        return charset.newDecoder().onMalformedInput(codingErrorAction2).onUnmappableCharacter(codingErrorAction);
    }

    public static CharsetEncoder createEncoder(ConnectionConfig cconfig) {
        CodingErrorAction codingErrorAction;
        if (cconfig == null) {
            return null;
        }
        Charset charset = cconfig.getCharset();
        if (charset == null) return null;
        CodingErrorAction malformed = cconfig.getMalformedInputAction();
        CodingErrorAction unmappable = cconfig.getUnmappableInputAction();
        CodingErrorAction codingErrorAction2 = malformed != null ? malformed : CodingErrorAction.REPORT;
        if (unmappable != null) {
            codingErrorAction = unmappable;
            return charset.newEncoder().onMalformedInput(codingErrorAction2).onUnmappableCharacter(codingErrorAction);
        }
        codingErrorAction = CodingErrorAction.REPORT;
        return charset.newEncoder().onMalformedInput(codingErrorAction2).onUnmappableCharacter(codingErrorAction);
    }
}

