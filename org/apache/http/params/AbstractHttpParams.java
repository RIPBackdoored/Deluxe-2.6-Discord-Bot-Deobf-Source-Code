/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.params;

import java.util.Set;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpParamsNames;

@Deprecated
public abstract class AbstractHttpParams
implements HttpParams,
HttpParamsNames {
    protected AbstractHttpParams() {
    }

    @Override
    public long getLongParameter(String name, long defaultValue) {
        Object param = this.getParameter(name);
        if (param != null) return (Long)param;
        return defaultValue;
    }

    @Override
    public HttpParams setLongParameter(String name, long value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public int getIntParameter(String name, int defaultValue) {
        Object param = this.getParameter(name);
        if (param != null) return (Integer)param;
        return defaultValue;
    }

    @Override
    public HttpParams setIntParameter(String name, int value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public double getDoubleParameter(String name, double defaultValue) {
        Object param = this.getParameter(name);
        if (param != null) return (Double)param;
        return defaultValue;
    }

    @Override
    public HttpParams setDoubleParameter(String name, double value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public boolean getBooleanParameter(String name, boolean defaultValue) {
        Object param = this.getParameter(name);
        if (param != null) return (Boolean)param;
        return defaultValue;
    }

    @Override
    public HttpParams setBooleanParameter(String name, boolean value) {
        this.setParameter(name, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    @Override
    public boolean isParameterTrue(String name) {
        return this.getBooleanParameter(name, false);
    }

    @Override
    public boolean isParameterFalse(String name) {
        if (this.getBooleanParameter(name, false)) return false;
        return true;
    }

    @Override
    public Set<String> getNames() {
        throw new UnsupportedOperationException();
    }
}

