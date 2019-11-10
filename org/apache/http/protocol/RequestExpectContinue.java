/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class RequestExpectContinue
implements HttpRequestInterceptor {
    private final boolean activeByDefault;

    @Deprecated
    public RequestExpectContinue() {
        this(false);
    }

    public RequestExpectContinue(boolean activeByDefault) {
        this.activeByDefault = activeByDefault;
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Args.notNull(request, "HTTP request");
        if (request.containsHeader("Expect")) return;
        if (!(request instanceof HttpEntityEnclosingRequest)) return;
        ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
        HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
        if (entity == null) return;
        if (entity.getContentLength() == 0L) return;
        if (ver.lessEquals(HttpVersion.HTTP_1_0)) return;
        boolean active = request.getParams().getBooleanParameter("http.protocol.expect-continue", this.activeByDefault);
        if (!active) return;
        request.addHeader("Expect", "100-continue");
    }
}

