/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import java.io.IOException;
import org.apache.http.HttpException;

public class ConnectionClosedException
extends IOException {
    private static final long serialVersionUID = 617550366255636674L;

    public ConnectionClosedException(String message) {
        super(HttpException.clean(message));
    }
}

