/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public final class HttpHost
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -7529410654042457626L;
    public static final String DEFAULT_SCHEME_NAME = "http";
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;
    protected final InetAddress address;

    public HttpHost(String hostname, int port, String scheme) {
        this.hostname = Args.containsNoBlanks(hostname, "Host name");
        this.lcHostname = hostname.toLowerCase(Locale.ROOT);
        this.schemeName = scheme != null ? scheme.toLowerCase(Locale.ROOT) : DEFAULT_SCHEME_NAME;
        this.port = port;
        this.address = null;
    }

    public HttpHost(String hostname, int port) {
        this(hostname, port, null);
    }

    public static HttpHost create(String s) {
        Args.containsNoBlanks(s, "HTTP Host");
        String text = s;
        String scheme = null;
        int schemeIdx = text.indexOf("://");
        if (schemeIdx > 0) {
            scheme = text.substring(0, schemeIdx);
            text = text.substring(schemeIdx + 3);
        }
        int port = -1;
        int portIdx = text.lastIndexOf(":");
        if (portIdx <= 0) return new HttpHost(text, port, scheme);
        try {
            port = Integer.parseInt(text.substring(portIdx + 1));
        }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid HTTP host: " + text);
        }
        text = text.substring(0, portIdx);
        return new HttpHost(text, port, scheme);
    }

    public HttpHost(String hostname) {
        this(hostname, -1, null);
    }

    public HttpHost(InetAddress address, int port, String scheme) {
        this(Args.notNull(address, "Inet address"), address.getHostName(), port, scheme);
    }

    public HttpHost(InetAddress address, String hostname, int port, String scheme) {
        this.address = Args.notNull(address, "Inet address");
        this.hostname = Args.notNull(hostname, "Hostname");
        this.lcHostname = this.hostname.toLowerCase(Locale.ROOT);
        this.schemeName = scheme != null ? scheme.toLowerCase(Locale.ROOT) : DEFAULT_SCHEME_NAME;
        this.port = port;
    }

    public HttpHost(InetAddress address, int port) {
        this(address, port, null);
    }

    public HttpHost(InetAddress address) {
        this(address, -1, null);
    }

    public HttpHost(HttpHost httphost) {
        Args.notNull(httphost, "HTTP host");
        this.hostname = httphost.hostname;
        this.lcHostname = httphost.lcHostname;
        this.schemeName = httphost.schemeName;
        this.port = httphost.port;
        this.address = httphost.address;
    }

    public String getHostName() {
        return this.hostname;
    }

    public int getPort() {
        return this.port;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public InetAddress getAddress() {
        return this.address;
    }

    public String toURI() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.schemeName);
        buffer.append("://");
        buffer.append(this.hostname);
        if (this.port == -1) return buffer.toString();
        buffer.append(':');
        buffer.append(Integer.toString(this.port));
        return buffer.toString();
    }

    public String toHostString() {
        if (this.port == -1) return this.hostname;
        StringBuilder buffer = new StringBuilder(this.hostname.length() + 6);
        buffer.append(this.hostname);
        buffer.append(":");
        buffer.append(Integer.toString(this.port));
        return buffer.toString();
    }

    public String toString() {
        return this.toURI();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HttpHost)) return false;
        HttpHost that = (HttpHost)obj;
        if (!this.lcHostname.equals(that.lcHostname)) return false;
        if (this.port != that.port) return false;
        if (!this.schemeName.equals(that.schemeName)) return false;
        if (this.address == null) {
            if (that.address != null) return false;
            return true;
        } else if (!this.address.equals(that.address)) return false;
        return true;
    }

    public int hashCode() {
        int hash = 17;
        hash = LangUtils.hashCode(hash, this.lcHostname);
        hash = LangUtils.hashCode(hash, this.port);
        hash = LangUtils.hashCode(hash, this.schemeName);
        if (this.address == null) return hash;
        return LangUtils.hashCode(hash, this.address);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

