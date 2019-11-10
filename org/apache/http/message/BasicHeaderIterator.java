/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.util.Args;

public class BasicHeaderIterator
implements HeaderIterator {
    protected final Header[] allHeaders;
    protected int currentIndex;
    protected String headerName;

    public BasicHeaderIterator(Header[] headers, String name) {
        this.allHeaders = Args.notNull(headers, "Header array");
        this.headerName = name;
        this.currentIndex = this.findNext(-1);
    }

    protected int findNext(int pos) {
        int from = pos;
        if (from < -1) {
            return -1;
        }
        int to = this.allHeaders.length - 1;
        boolean found = false;
        while (!found && from < to) {
            found = this.filterHeader(++from);
        }
        if (!found) return -1;
        int n = from;
        return n;
    }

    protected boolean filterHeader(int index) {
        if (this.headerName == null) return true;
        if (this.headerName.equalsIgnoreCase(this.allHeaders[index].getName())) return true;
        return false;
    }

    @Override
    public boolean hasNext() {
        if (this.currentIndex < 0) return false;
        return true;
    }

    @Override
    public Header nextHeader() throws NoSuchElementException {
        int current = this.currentIndex;
        if (current < 0) {
            throw new NoSuchElementException("Iteration already finished.");
        }
        this.currentIndex = this.findNext(current);
        return this.allHeaders[current];
    }

    @Override
    public final Object next() throws NoSuchElementException {
        return this.nextHeader();
    }

    @Override
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removing headers is not supported.");
    }
}

