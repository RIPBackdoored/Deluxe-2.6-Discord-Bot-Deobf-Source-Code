/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;

@Contract(threading=ThreadingBehavior.SAFE)
public class UriPatternMatcher<T> {
    private final Map<String, T> map = new HashMap<String, T>();

    public synchronized Set<Map.Entry<String, T>> entrySet() {
        return new HashSet<Map.Entry<String, T>>(this.map.entrySet());
    }

    public synchronized void register(String pattern, T obj) {
        Args.notNull(pattern, "URI request pattern");
        this.map.put(pattern, obj);
    }

    public synchronized void unregister(String pattern) {
        if (pattern == null) {
            return;
        }
        this.map.remove(pattern);
    }

    @Deprecated
    public synchronized void setHandlers(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized void setObjects(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized Map<String, T> getObjects() {
        return this.map;
    }

    public synchronized T lookup(String path) {
        Args.notNull(path, "Request path");
        T obj = this.map.get(path);
        if (obj != null) return obj;
        String bestMatch = null;
        Iterator<String> i$ = this.map.keySet().iterator();
        while (i$.hasNext()) {
            String pattern = i$.next();
            if (!this.matchUriRequestPattern(pattern, path) || bestMatch != null && bestMatch.length() >= pattern.length() && (bestMatch.length() != pattern.length() || !pattern.endsWith("*"))) continue;
            obj = this.map.get(pattern);
            bestMatch = pattern;
        }
        return obj;
    }

    protected boolean matchUriRequestPattern(String pattern, String path) {
        if (pattern.equals("*")) {
            return true;
        }
        if (pattern.endsWith("*")) {
            if (path.startsWith(pattern.substring(0, pattern.length() - 1))) return true;
        }
        if (!pattern.startsWith("*")) return false;
        if (!path.endsWith(pattern.substring(1, pattern.length()))) return false;
        return true;
    }

    public String toString() {
        return this.map.toString();
    }
}

