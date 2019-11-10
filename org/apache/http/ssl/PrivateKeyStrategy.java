/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.ssl;

import java.net.Socket;
import java.util.Map;
import org.apache.http.ssl.PrivateKeyDetails;

public interface PrivateKeyStrategy {
    public String chooseAlias(Map<String, PrivateKeyDetails> var1, Socket var2);
}

