/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.pool;

import org.apache.http.pool.PoolEntry;

public interface PoolEntryCallback<T, C> {
    public void process(PoolEntry<T, C> var1);
}

