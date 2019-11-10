/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.pool;

import java.io.IOException;

public interface ConnFactory<T, C> {
    public C create(T var1) throws IOException;
}

