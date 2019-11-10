/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import org.apache.http.Header;
import org.apache.http.util.CharArrayBuffer;

public interface FormattedHeader
extends Header {
    public CharArrayBuffer getBuffer();

    public int getValuePos();
}

