/*
 * Decompiled with CFR <Could not determine version>.
 * 
 * Could not load the following classes:
 *  org.apache.log.Hierarchy
 *  org.apache.log.Logger
 */
package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

public class LogKitLogger
implements Log,
Serializable {
    private static final long serialVersionUID = 3768538055836059519L;
    protected volatile transient Logger logger = null;
    protected String name = null;

    public LogKitLogger(String name) {
        this.name = name;
        this.logger = this.getLogger();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Logger getLogger() {
        Logger result = this.logger;
        if (result != null) return result;
        LogKitLogger logKitLogger = this;
        synchronized (logKitLogger) {
            result = this.logger;
            if (result != null) return result;
            this.logger = result = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name);
            return result;
        }
    }

    @Override
    public void trace(Object message) {
        this.debug(message);
    }

    @Override
    public void trace(Object message, Throwable t) {
        this.debug(message, t);
    }

    @Override
    public void debug(Object message) {
        if (message == null) return;
        this.getLogger().debug(String.valueOf(message));
    }

    @Override
    public void debug(Object message, Throwable t) {
        if (message == null) return;
        this.getLogger().debug(String.valueOf(message), t);
    }

    @Override
    public void info(Object message) {
        if (message == null) return;
        this.getLogger().info(String.valueOf(message));
    }

    @Override
    public void info(Object message, Throwable t) {
        if (message == null) return;
        this.getLogger().info(String.valueOf(message), t);
    }

    @Override
    public void warn(Object message) {
        if (message == null) return;
        this.getLogger().warn(String.valueOf(message));
    }

    @Override
    public void warn(Object message, Throwable t) {
        if (message == null) return;
        this.getLogger().warn(String.valueOf(message), t);
    }

    @Override
    public void error(Object message) {
        if (message == null) return;
        this.getLogger().error(String.valueOf(message));
    }

    @Override
    public void error(Object message, Throwable t) {
        if (message == null) return;
        this.getLogger().error(String.valueOf(message), t);
    }

    @Override
    public void fatal(Object message) {
        if (message == null) return;
        this.getLogger().fatalError(String.valueOf(message));
    }

    @Override
    public void fatal(Object message, Throwable t) {
        if (message == null) return;
        this.getLogger().fatalError(String.valueOf(message), t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return this.getLogger().isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return this.getLogger().isFatalErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return this.getLogger().isWarnEnabled();
    }
}

