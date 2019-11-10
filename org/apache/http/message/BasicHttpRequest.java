/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import org.apache.http.HttpRequest;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.BasicRequestLine;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.Args;

public class BasicHttpRequest
extends AbstractHttpMessage
implements HttpRequest {
    private final String method;
    private final String uri;
    private RequestLine requestline;

    public BasicHttpRequest(String method, String uri) {
        this.method = Args.notNull(method, "Method name");
        this.uri = Args.notNull(uri, "Request URI");
        this.requestline = null;
    }

    public BasicHttpRequest(String method, String uri, ProtocolVersion ver) {
        this(new BasicRequestLine(method, uri, ver));
    }

    public BasicHttpRequest(RequestLine requestline) {
        this.requestline = Args.notNull(requestline, "Request line");
        this.method = requestline.getMethod();
        this.uri = requestline.getUri();
    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return this.getRequestLine().getProtocolVersion();
    }

    @Override
    public RequestLine getRequestLine() {
        if (this.requestline != null) return this.requestline;
        this.requestline = new BasicRequestLine(this.method, this.uri, HttpVersion.HTTP_1_1);
        return this.requestline;
    }

    public String toString() {
        return this.method + ' ' + this.uri + ' ' + this.headergroup;
    }
}

