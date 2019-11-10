/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.config;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.Lookup;

@Contract(threading=ThreadingBehavior.SAFE)
public final class Registry<I>
implements Lookup<I> {
    private final Map<String, I> map;

    Registry(Map<String, I> map) {
        this.map = new ConcurrentHashMap<String, I>(map);
    }

    @Override
    public I lookup(String key) {
        if (key != null) return this.map.get(key.toLowerCase(Locale.ROOT));
        return null;
    }

    public String toString() {
        return this.map.toString();
    }
}

