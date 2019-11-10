/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RequestUserAgent
implements HttpRequestInterceptor {
    private final String userAgent;

    public RequestUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public RequestUserAgent() {
        this(null);
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (request.containsHeader("User-Agent")) return;
        String s = null;
        HttpParams params = request.getParams();
        if (params != null) {
            s = (String)params.getParameter("http.useragent");
        }
        if (s == null) {
            s = this.userAgent;
        }
        if (s == null) return;
        request.addHeader("User-Agent", s);
    }
}

