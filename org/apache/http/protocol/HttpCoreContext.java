/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import org.apache.http.HttpConnection;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class HttpCoreContext
implements HttpContext {
    public static final String HTTP_CONNECTION = "http.connection";
    public static final String HTTP_REQUEST = "http.request";
    public static final String HTTP_RESPONSE = "http.response";
    public static final String HTTP_TARGET_HOST = "http.target_host";
    public static final String HTTP_REQ_SENT = "http.request_sent";
    private final HttpContext context;

    public static HttpCoreContext create() {
        return new HttpCoreContext(new BasicHttpContext());
    }

    public static HttpCoreContext adapt(HttpContext context) {
        Args.notNull(context, "HTTP context");
        if (!(context instanceof HttpCoreContext)) return new HttpCoreContext(context);
        return (HttpCoreContext)context;
    }

    public HttpCoreContext(HttpContext context) {
        this.context = context;
    }

    public HttpCoreContext() {
        this.context = new BasicHttpContext();
    }

    @Override
    public Object getAttribute(String id) {
        return this.context.getAttribute(id);
    }

    @Override
    public void setAttribute(String id, Object obj) {
        this.context.setAttribute(id, obj);
    }

    @Override
    public Object removeAttribute(String id) {
        return this.context.removeAttribute(id);
    }

    public <T> T getAttribute(String attribname, Class<T> clazz) {
        Args.notNull(clazz, "Attribute class");
        Object obj = this.getAttribute(attribname);
        if (obj != null) return clazz.cast(obj);
        return null;
    }

    public <T extends HttpConnection> T getConnection(Class<T> clazz) {
        return (T)((HttpConnection)this.getAttribute(HTTP_CONNECTION, clazz));
    }

    public HttpConnection getConnection() {
        return this.getAttribute(HTTP_CONNECTION, HttpConnection.class);
    }

    public HttpRequest getRequest() {
        return this.getAttribute(HTTP_REQUEST, HttpRequest.class);
    }

    public boolean isRequestSent() {
        Boolean b = this.getAttribute(HTTP_REQ_SENT, Boolean.class);
        if (b == null) return false;
        if (b == false) return false;
        return true;
    }

    public HttpResponse getResponse() {
        return this.getAttribute(HTTP_RESPONSE, HttpResponse.class);
    }

    public void setTargetHost(HttpHost host) {
        this.setAttribute(HTTP_TARGET_HOST, host);
    }

    public HttpHost getTargetHost() {
        return this.getAttribute(HTTP_TARGET_HOST, HttpHost.class);
    }
}

