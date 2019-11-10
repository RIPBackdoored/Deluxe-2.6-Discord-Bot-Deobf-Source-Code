/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.entity;

import java.io.IOException;
import java.io.OutputStream;

public interface ContentProducer {
    public void writeTo(OutputStream var1) throws IOException;
}

