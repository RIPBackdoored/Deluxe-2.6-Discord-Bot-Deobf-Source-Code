/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class ResponseServer
implements HttpResponseInterceptor {
    private final String originServer;

    public ResponseServer(String originServer) {
        this.originServer = originServer;
    }

    public ResponseServer() {
        this(null);
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Args.notNull(response, "HTTP response");
        if (response.containsHeader("Server")) return;
        if (this.originServer == null) return;
        response.addHeader("Server", this.originServer);
    }
}

