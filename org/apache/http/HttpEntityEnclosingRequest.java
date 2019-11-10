/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;

public interface HttpEntityEnclosingRequest
extends HttpRequest {
    public boolean expectContinue();

    public void setEntity(HttpEntity var1);

    public HttpEntity getEntity();
}

