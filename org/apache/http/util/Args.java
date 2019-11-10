/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.util;

import java.util.Collection;
import org.apache.http.util.TextUtils;

public class Args {
    public static void check(boolean expression, String message) {
        if (expression) return;
        throw new IllegalArgumentException(message);
    }

    public static void check(boolean expression, String message, Object ... args) {
        if (expression) return;
        throw new IllegalArgumentException(String.format(message, args));
    }

    public static void check(boolean expression, String message, Object arg) {
        if (expression) return;
        throw new IllegalArgumentException(String.format(message, arg));
    }

    public static <T> T notNull(T argument, String name) {
        if (argument != null) return argument;
        throw new IllegalArgumentException(name + " may not be null");
    }

    public static <T extends CharSequence> T notEmpty(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (!TextUtils.isEmpty(argument)) return argument;
        throw new IllegalArgumentException(name + " may not be empty");
    }

    public static <T extends CharSequence> T notBlank(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (!TextUtils.isBlank(argument)) return argument;
        throw new IllegalArgumentException(name + " may not be blank");
    }

    public static <T extends CharSequence> T containsNoBlanks(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (argument.length() == 0) {
            throw new IllegalArgumentException(name + " may not be empty");
        }
        if (!TextUtils.containsBlanks(argument)) return argument;
        throw new IllegalArgumentException(name + " may not contain blanks");
    }

    public static <E, T extends Collection<E>> T notEmpty(T argument, String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        if (!argument.isEmpty()) return argument;
        throw new IllegalArgumentException(name + " may not be empty");
    }

    public static int positive(int n, String name) {
        if (n > 0) return n;
        throw new IllegalArgumentException(name + " may not be negative or zero");
    }

    public static long positive(long n, String name) {
        if (n > 0L) return n;
        throw new IllegalArgumentException(name + " may not be negative or zero");
    }

    public static int notNegative(int n, String name) {
        if (n >= 0) return n;
        throw new IllegalArgumentException(name + " may not be negative");
    }

    public static long notNegative(long n, String name) {
        if (n >= 0L) return n;
        throw new IllegalArgumentException(name + " may not be negative");
    }
}

