/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestInterceptorList;
import org.apache.http.protocol.HttpResponseInterceptorList;

@Contract(threading=ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public final class ImmutableHttpProcessor
implements HttpProcessor {
    private final HttpRequestInterceptor[] requestInterceptors;
    private final HttpResponseInterceptor[] responseInterceptors;

    public ImmutableHttpProcessor(HttpRequestInterceptor[] requestInterceptors, HttpResponseInterceptor[] responseInterceptors) {
        int l;
        if (requestInterceptors != null) {
            l = requestInterceptors.length;
            this.requestInterceptors = new HttpRequestInterceptor[l];
            System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
        } else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null) {
            l = responseInterceptors.length;
            this.responseInterceptors = new HttpResponseInterceptor[l];
            System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
            return;
        }
        this.responseInterceptors = new HttpResponseInterceptor[0];
    }

    public ImmutableHttpProcessor(List<HttpRequestInterceptor> requestInterceptors, List<HttpResponseInterceptor> responseInterceptors) {
        int l;
        if (requestInterceptors != null) {
            l = requestInterceptors.size();
            this.requestInterceptors = requestInterceptors.toArray(new HttpRequestInterceptor[l]);
        } else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null) {
            l = responseInterceptors.size();
            this.responseInterceptors = responseInterceptors.toArray(new HttpResponseInterceptor[l]);
            return;
        }
        this.responseInterceptors = new HttpResponseInterceptor[0];
    }

    @Deprecated
    public ImmutableHttpProcessor(HttpRequestInterceptorList requestInterceptors, HttpResponseInterceptorList responseInterceptors) {
        int count;
        int i;
        if (requestInterceptors != null) {
            count = requestInterceptors.getRequestInterceptorCount();
            this.requestInterceptors = new HttpRequestInterceptor[count];
            for (i = 0; i < count; ++i) {
                this.requestInterceptors[i] = requestInterceptors.getRequestInterceptor(i);
            }
        } else {
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors == null) {
            this.responseInterceptors = new HttpResponseInterceptor[0];
            return;
        }
        count = responseInterceptors.getResponseInterceptorCount();
        this.responseInterceptors = new HttpResponseInterceptor[count];
        i = 0;
        while (i < count) {
            this.responseInterceptors[i] = responseInterceptors.getResponseInterceptor(i);
            ++i;
        }
    }

    public ImmutableHttpProcessor(HttpRequestInterceptor ... requestInterceptors) {
        this(requestInterceptors, (HttpResponseInterceptor[])null);
    }

    public ImmutableHttpProcessor(HttpResponseInterceptor ... responseInterceptors) {
        this((HttpRequestInterceptor[])null, responseInterceptors);
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws IOException, HttpException {
        HttpRequestInterceptor[] arr$ = this.requestInterceptors;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            HttpRequestInterceptor requestInterceptor = arr$[i$];
            requestInterceptor.process(request, context);
            ++i$;
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws IOException, HttpException {
        HttpResponseInterceptor[] arr$ = this.responseInterceptors;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            HttpResponseInterceptor responseInterceptor = arr$[i$];
            responseInterceptor.process(response, context);
            ++i$;
        }
    }
}

