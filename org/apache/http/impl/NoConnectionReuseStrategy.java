/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.protocol.HttpContext;

@Contract(threading=ThreadingBehavior.IMMUTABLE)
public class NoConnectionReuseStrategy
implements ConnectionReuseStrategy {
    public static final NoConnectionReuseStrategy INSTANCE = new NoConnectionReuseStrategy();

    @Override
    public boolean keepAlive(HttpResponse response, HttpContext context) {
        return false;
    }
}

