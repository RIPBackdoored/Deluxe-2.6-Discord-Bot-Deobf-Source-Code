/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public interface TrustStrategy {
    public boolean isTrusted(X509Certificate[] var1, String var2) throws CertificateException;
}

