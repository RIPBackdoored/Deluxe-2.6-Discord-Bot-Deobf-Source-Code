/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.ChainBuilder;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.ImmutableHttpProcessor;

public class HttpProcessorBuilder {
    private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
    private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;

    public static HttpProcessorBuilder create() {
        return new HttpProcessorBuilder();
    }

    HttpProcessorBuilder() {
    }

    private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder() {
        if (this.requestChainBuilder != null) return this.requestChainBuilder;
        this.requestChainBuilder = new ChainBuilder();
        return this.requestChainBuilder;
    }

    private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder() {
        if (this.responseChainBuilder != null) return this.responseChainBuilder;
        this.responseChainBuilder = new ChainBuilder();
        return this.responseChainBuilder;
    }

    public HttpProcessorBuilder addFirst(HttpRequestInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addFirst(e);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpRequestInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addLast(e);
        return this;
    }

    public HttpProcessorBuilder add(HttpRequestInterceptor e) {
        return this.addLast(e);
    }

    public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor ... e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllFirst(e);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpRequestInterceptor ... e) {
        if (e == null) {
            return this;
        }
        this.getRequestChainBuilder().addAllLast(e);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpRequestInterceptor ... e) {
        return this.addAllLast(e);
    }

    public HttpProcessorBuilder addFirst(HttpResponseInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addFirst(e);
        return this;
    }

    public HttpProcessorBuilder addLast(HttpResponseInterceptor e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addLast(e);
        return this;
    }

    public HttpProcessorBuilder add(HttpResponseInterceptor e) {
        return this.addLast(e);
    }

    public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor ... e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllFirst(e);
        return this;
    }

    public HttpProcessorBuilder addAllLast(HttpResponseInterceptor ... e) {
        if (e == null) {
            return this;
        }
        this.getResponseChainBuilder().addAllLast(e);
        return this;
    }

    public HttpProcessorBuilder addAll(HttpResponseInterceptor ... e) {
        return this.addAllLast(e);
    }

    public HttpProcessor build() {
        LinkedList<HttpResponseInterceptor> linkedList;
        LinkedList<HttpRequestInterceptor> linkedList2 = this.requestChainBuilder != null ? this.requestChainBuilder.build() : null;
        if (this.responseChainBuilder != null) {
            linkedList = this.responseChainBuilder.build();
            return new ImmutableHttpProcessor(linkedList2, linkedList);
        }
        linkedList = null;
        return new ImmutableHttpProcessor(linkedList2, linkedList);
    }
}

