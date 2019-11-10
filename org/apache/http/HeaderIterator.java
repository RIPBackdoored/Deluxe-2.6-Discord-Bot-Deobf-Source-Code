/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.util.Iterator;
import org.apache.http.Header;

public interface HeaderIterator
extends Iterator<Object> {
    @Override
    public boolean hasNext();

    public Header nextHeader();
}

