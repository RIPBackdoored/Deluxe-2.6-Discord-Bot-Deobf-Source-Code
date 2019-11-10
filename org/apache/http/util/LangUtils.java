/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.util;

public final class LangUtils {
    public static final int HASH_SEED = 17;
    public static final int HASH_OFFSET = 37;

    private LangUtils() {
    }

    public static int hashCode(int seed, int hashcode) {
        return seed * 37 + hashcode;
    }

    public static int hashCode(int seed, boolean b) {
        int n;
        if (b) {
            n = 1;
            return LangUtils.hashCode(seed, n);
        }
        n = 0;
        return LangUtils.hashCode(seed, n);
    }

    public static int hashCode(int seed, Object obj) {
        int n;
        if (obj != null) {
            n = obj.hashCode();
            return LangUtils.hashCode(seed, n);
        }
        n = 0;
        return LangUtils.hashCode(seed, n);
    }

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 != null) {
            boolean bl = obj1.equals(obj2);
            return bl;
        }
        if (obj2 != null) return false;
        return true;
    }

    public static boolean equals(Object[] a1, Object[] a2) {
        if (a1 == null) {
            if (a2 != null) return false;
            return true;
        }
        if (a2 == null) return false;
        if (a1.length != a2.length) return false;
        int i = 0;
        while (i < a1.length) {
            if (!LangUtils.equals(a1[i], a2[i])) {
                return false;
            }
            ++i;
        }
        return true;
    }
}

