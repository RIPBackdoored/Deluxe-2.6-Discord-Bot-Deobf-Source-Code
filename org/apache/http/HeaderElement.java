/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http;

import org.apache.http.NameValuePair;

public interface HeaderElement {
    public String getName();

    public String getValue();

    public NameValuePair[] getParameters();

    public NameValuePair getParameterByName(String var1);

    public int getParameterCount();

    public NameValuePair getParameter(int var1);
}

