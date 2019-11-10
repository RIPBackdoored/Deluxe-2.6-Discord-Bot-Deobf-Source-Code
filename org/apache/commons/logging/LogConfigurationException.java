/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.logging;

public class LogConfigurationException
extends RuntimeException {
    private static final long serialVersionUID = 8486587136871052495L;
    protected Throwable cause = null;

    public LogConfigurationException() {
    }

    public LogConfigurationException(String message) {
        super(message);
    }

    public LogConfigurationException(Throwable cause) {
        this(cause == null ? null : cause.toString(), cause);
    }

    public LogConfigurationException(String message, Throwable cause) {
        super(new StringBuffer().append(message).append(" (Caused by ").append(cause).append(")").toString());
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }
}

