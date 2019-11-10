/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicListHeaderIterator;
import org.apache.http.util.CharArrayBuffer;

public class HeaderGroup
implements Cloneable,
Serializable {
    private static final long serialVersionUID = 2608834160639271617L;
    private final Header[] EMPTY = new Header[0];
    private final List<Header> headers = new ArrayList<Header>(16);

    public void clear() {
        this.headers.clear();
    }

    public void addHeader(Header header) {
        if (header == null) {
            return;
        }
        this.headers.add(header);
    }

    public void removeHeader(Header header) {
        if (header == null) {
            return;
        }
        this.headers.remove(header);
    }

    public void updateHeader(Header header) {
        if (header == null) {
            return;
        }
        int i = 0;
        do {
            if (i >= this.headers.size()) {
                this.headers.add(header);
                return;
            }
            Header current = this.headers.get(i);
            if (current.getName().equalsIgnoreCase(header.getName())) {
                this.headers.set(i, header);
                return;
            }
            ++i;
        } while (true);
    }

    public void setHeaders(Header[] headers) {
        this.clear();
        if (headers == null) {
            return;
        }
        Collections.addAll(this.headers, headers);
    }

    public Header getCondensedHeader(String name) {
        Header[] hdrs = this.getHeaders(name);
        if (hdrs.length == 0) {
            return null;
        }
        if (hdrs.length == 1) {
            return hdrs[0];
        }
        CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
        valueBuffer.append(hdrs[0].getValue());
        int i = 1;
        while (i < hdrs.length) {
            valueBuffer.append(", ");
            valueBuffer.append(hdrs[i].getValue());
            ++i;
        }
        return new BasicHeader(name.toLowerCase(Locale.ROOT), valueBuffer.toString());
    }

    public Header[] getHeaders(String name) {
        Header[] arrheader;
        ArrayList<Header> headersFound = null;
        for (int i = 0; i < this.headers.size(); ++i) {
            Header header = this.headers.get(i);
            if (!header.getName().equalsIgnoreCase(name)) continue;
            if (headersFound == null) {
                headersFound = new ArrayList<Header>();
            }
            headersFound.add(header);
        }
        if (headersFound != null) {
            arrheader = headersFound.toArray(new Header[headersFound.size()]);
            return arrheader;
        }
        arrheader = this.EMPTY;
        return arrheader;
    }

    public Header getFirstHeader(String name) {
        int i = 0;
        while (i < this.headers.size()) {
            Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
            ++i;
        }
        return null;
    }

    public Header getLastHeader(String name) {
        int i = this.headers.size() - 1;
        while (i >= 0) {
            Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return header;
            }
            --i;
        }
        return null;
    }

    public Header[] getAllHeaders() {
        return this.headers.toArray(new Header[this.headers.size()]);
    }

    public boolean containsHeader(String name) {
        int i = 0;
        while (i < this.headers.size()) {
            Header header = this.headers.get(i);
            if (header.getName().equalsIgnoreCase(name)) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public HeaderIterator iterator() {
        return new BasicListHeaderIterator(this.headers, null);
    }

    public HeaderIterator iterator(String name) {
        return new BasicListHeaderIterator(this.headers, name);
    }

    public HeaderGroup copy() {
        HeaderGroup clone = new HeaderGroup();
        clone.headers.addAll(this.headers);
        return clone;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return this.headers.toString();
    }
}

