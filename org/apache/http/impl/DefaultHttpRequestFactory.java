/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl;

import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class DefaultHttpRequestFactory
implements HttpRequestFactory {
    public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
    private static final String[] RFC2616_COMMON_METHODS = new String[]{"GET"};
    private static final String[] RFC2616_ENTITY_ENC_METHODS = new String[]{"POST", "PUT"};
    private static final String[] RFC2616_SPECIAL_METHODS = new String[]{"HEAD", "OPTIONS", "DELETE", "TRACE", "CONNECT"};
    private static final String[] RFC5789_ENTITY_ENC_METHODS = new String[]{"PATCH"};

    private static boolean isOneOf(String[] methods, String method) {
        String[] arr$ = methods;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            String method2 = arr$[i$];
            if (method2.equalsIgnoreCase(method)) {
                return true;
            }
            ++i$;
        }
        return false;
    }

    @Override
    public HttpRequest newHttpRequest(RequestLine requestline) throws MethodNotSupportedException {
        Args.notNull(requestline, "Request line");
        String method = requestline.getMethod();
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_COMMON_METHODS, method)) {
            return new BasicHttpRequest(requestline);
        }
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_ENTITY_ENC_METHODS, method)) {
            return new BasicHttpEntityEnclosingRequest(requestline);
        }
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_SPECIAL_METHODS, method)) {
            return new BasicHttpRequest(requestline);
        }
        if (!DefaultHttpRequestFactory.isOneOf(RFC5789_ENTITY_ENC_METHODS, method)) throw new MethodNotSupportedException(method + " method not supported");
        return new BasicHttpEntityEnclosingRequest(requestline);
    }

    @Override
    public HttpRequest newHttpRequest(String method, String uri) throws MethodNotSupportedException {
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_COMMON_METHODS, method)) {
            return new BasicHttpRequest(method, uri);
        }
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_ENTITY_ENC_METHODS, method)) {
            return new BasicHttpEntityEnclosingRequest(method, uri);
        }
        if (DefaultHttpRequestFactory.isOneOf(RFC2616_SPECIAL_METHODS, method)) {
            return new BasicHttpRequest(method, uri);
        }
        if (!DefaultHttpRequestFactory.isOneOf(RFC5789_ENTITY_ENC_METHODS, method)) throw new MethodNotSupportedException(method + " method not supported");
        return new BasicHttpEntityEnclosingRequest(method, uri);
    }
}

