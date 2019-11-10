/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import org.apache.http.ProtocolVersion;

public interface RequestLine {
    public String getMethod();

    public ProtocolVersion getProtocolVersion();

    public String getUri();
}

