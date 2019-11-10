/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import org.apache.http.ProtocolVersion;

public interface StatusLine {
    public ProtocolVersion getProtocolVersion();

    public int getStatusCode();

    public String getReasonPhrase();
}

