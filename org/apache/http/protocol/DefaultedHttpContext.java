/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public final class DefaultedHttpContext
implements HttpContext {
    private final HttpContext local;
    private final HttpContext defaults;

    public DefaultedHttpContext(HttpContext local, HttpContext defaults) {
        this.local = Args.notNull(local, "HTTP context");
        this.defaults = defaults;
    }

    @Override
    public Object getAttribute(String id) {
        Object obj = this.local.getAttribute(id);
        if (obj != null) return obj;
        return this.defaults.getAttribute(id);
    }

    @Override
    public Object removeAttribute(String id) {
        return this.local.removeAttribute(id);
    }

    @Override
    public void setAttribute(String id, Object obj) {
        this.local.setAttribute(id, obj);
    }

    public HttpContext getDefaults() {
        return this.defaults;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[local: ").append(this.local);
        buf.append("defaults: ").append(this.defaults);
        buf.append("]");
        return buf.toString();
    }
}

