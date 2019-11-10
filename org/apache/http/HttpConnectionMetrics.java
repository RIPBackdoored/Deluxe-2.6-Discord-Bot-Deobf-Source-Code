/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

public interface HttpConnectionMetrics {
    public long getRequestCount();

    public long getResponseCount();

    public long getSentBytesCount();

    public long getReceivedBytesCount();

    public Object getMetric(String var1);

    public void reset();
}

