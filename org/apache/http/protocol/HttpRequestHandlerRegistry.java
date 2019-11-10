/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.util.Map;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerResolver;
import org.apache.http.protocol.UriPatternMatcher;
import org.apache.http.util.Args;

@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class HttpRequestHandlerRegistry
implements HttpRequestHandlerResolver {
    private final UriPatternMatcher<HttpRequestHandler> matcher = new UriPatternMatcher();

    public void register(String pattern, HttpRequestHandler handler) {
        Args.notNull(pattern, "URI request pattern");
        Args.notNull(handler, "Request handler");
        this.matcher.register(pattern, handler);
    }

    public void unregister(String pattern) {
        this.matcher.unregister(pattern);
    }

    public void setHandlers(Map<String, HttpRequestHandler> map) {
        this.matcher.setObjects(map);
    }

    public Map<String, HttpRequestHandler> getHandlers() {
        return this.matcher.getObjects();
    }

    @Override
    public HttpRequestHandler lookup(String requestURI) {
        return this.matcher.lookup(requestURI);
    }
}

