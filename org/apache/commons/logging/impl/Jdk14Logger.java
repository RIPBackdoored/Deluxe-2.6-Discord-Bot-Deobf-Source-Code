/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk14Logger
implements Log,
Serializable {
    private static final long serialVersionUID = 4784713551416303804L;
    protected static final Level dummyLevel = Level.FINE;
    protected transient Logger logger = null;
    protected String name = null;

    public Jdk14Logger(String name) {
        this.name = name;
        this.logger = this.getLogger();
    }

    protected void log(Level level, String msg, Throwable ex) {
        Logger logger = this.getLogger();
        if (!logger.isLoggable(level)) return;
        Throwable dummyException = new Throwable();
        StackTraceElement[] locations = dummyException.getStackTrace();
        String cname = this.name;
        String method = "unknown";
        if (locations != null && locations.length > 2) {
            StackTraceElement caller = locations[2];
            method = caller.getMethodName();
        }
        if (ex == null) {
            logger.logp(level, cname, method, msg);
            return;
        }
        logger.logp(level, cname, method, msg, ex);
    }

    @Override
    public void debug(Object message) {
        this.log(Level.FINE, String.valueOf(message), null);
    }

    @Override
    public void debug(Object message, Throwable exception) {
        this.log(Level.FINE, String.valueOf(message), exception);
    }

    @Override
    public void error(Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }

    @Override
    public void error(Object message, Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }

    @Override
    public void fatal(Object message) {
        this.log(Level.SEVERE, String.valueOf(message), null);
    }

    @Override
    public void fatal(Object message, Throwable exception) {
        this.log(Level.SEVERE, String.valueOf(message), exception);
    }

    public Logger getLogger() {
        if (this.logger != null) return this.logger;
        this.logger = Logger.getLogger(this.name);
        return this.logger;
    }

    @Override
    public void info(Object message) {
        this.log(Level.INFO, String.valueOf(message), null);
    }

    @Override
    public void info(Object message, Throwable exception) {
        this.log(Level.INFO, String.valueOf(message), exception);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.getLogger().isLoggable(Level.FINE);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }

    @Override
    public boolean isFatalEnabled() {
        return this.getLogger().isLoggable(Level.SEVERE);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.getLogger().isLoggable(Level.INFO);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.getLogger().isLoggable(Level.FINEST);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.getLogger().isLoggable(Level.WARNING);
    }

    @Override
    public void trace(Object message) {
        this.log(Level.FINEST, String.valueOf(message), null);
    }

    @Override
    public void trace(Object message, Throwable exception) {
        this.log(Level.FINEST, String.valueOf(message), exception);
    }

    @Override
    public void warn(Object message) {
        this.log(Level.WARNING, String.valueOf(message), null);
    }

    @Override
    public void warn(Object message, Throwable exception) {
        this.log(Level.WARNING, String.valueOf(message), exception);
    }
}

