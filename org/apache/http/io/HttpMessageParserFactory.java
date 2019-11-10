/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.io;

import org.apache.http.HttpMessage;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;

public interface HttpMessageParserFactory<T extends HttpMessage> {
    public HttpMessageParser<T> create(SessionInputBuffer var1, MessageConstraints var2);
}

