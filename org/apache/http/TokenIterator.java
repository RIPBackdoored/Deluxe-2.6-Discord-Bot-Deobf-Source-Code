/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.util.Iterator;

public interface TokenIterator
extends Iterator<Object> {
    @Override
    public boolean hasNext();

    public String nextToken();
}

