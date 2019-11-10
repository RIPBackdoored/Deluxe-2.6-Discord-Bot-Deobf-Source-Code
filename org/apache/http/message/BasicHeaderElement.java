/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.message;

import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

public class BasicHeaderElement
implements HeaderElement,
Cloneable {
    private final String name;
    private final String value;
    private final NameValuePair[] parameters;

    public BasicHeaderElement(String name, String value, NameValuePair[] parameters) {
        this.name = Args.notNull(name, "Name");
        this.value = value;
        if (parameters != null) {
            this.parameters = parameters;
            return;
        }
        this.parameters = new NameValuePair[0];
    }

    public BasicHeaderElement(String name, String value) {
        this(name, value, null);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public NameValuePair[] getParameters() {
        return (NameValuePair[])this.parameters.clone();
    }

    @Override
    public int getParameterCount() {
        return this.parameters.length;
    }

    @Override
    public NameValuePair getParameter(int index) {
        return this.parameters[index];
    }

    @Override
    public NameValuePair getParameterByName(String name) {
        Args.notNull(name, "Name");
        NameValuePair found = null;
        NameValuePair[] arr$ = this.parameters;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            NameValuePair current = arr$[i$];
            if (current.getName().equalsIgnoreCase(name)) {
                return current;
            }
            ++i$;
        }
        return found;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HeaderElement)) return false;
        BasicHeaderElement that = (BasicHeaderElement)object;
        if (!this.name.equals(that.name)) return false;
        if (!LangUtils.equals(this.value, that.value)) return false;
        if (!LangUtils.equals(this.parameters, that.parameters)) return false;
        return true;
    }

    public int hashCode() {
        int hash = 17;
        hash = LangUtils.hashCode(hash, this.name);
        hash = LangUtils.hashCode(hash, this.value);
        NameValuePair[] arr$ = this.parameters;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            NameValuePair parameter = arr$[i$];
            hash = LangUtils.hashCode(hash, parameter);
            ++i$;
        }
        return hash;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.name);
        if (this.value != null) {
            buffer.append("=");
            buffer.append(this.value);
        }
        NameValuePair[] arr$ = this.parameters;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            NameValuePair parameter = arr$[i$];
            buffer.append("; ");
            buffer.append(parameter);
            ++i$;
        }
        return buffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

