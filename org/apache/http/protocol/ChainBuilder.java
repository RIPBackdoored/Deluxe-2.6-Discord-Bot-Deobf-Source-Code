/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

final class ChainBuilder<E> {
    private final LinkedList<E> list = new LinkedList();
    private final Map<Class<?>, E> uniqueClasses = new HashMap();

    private void ensureUnique(E e) {
        E previous = this.uniqueClasses.remove(e.getClass());
        if (previous != null) {
            this.list.remove(previous);
        }
        this.uniqueClasses.put(e.getClass(), e);
    }

    public ChainBuilder<E> addFirst(E e) {
        if (e == null) {
            return this;
        }
        this.ensureUnique(e);
        this.list.addFirst(e);
        return this;
    }

    public ChainBuilder<E> addLast(E e) {
        if (e == null) {
            return this;
        }
        this.ensureUnique(e);
        this.list.addLast(e);
        return this;
    }

    public ChainBuilder<E> addAllFirst(Collection<E> c) {
        if (c == null) {
            return this;
        }
        Iterator<E> i$ = c.iterator();
        while (i$.hasNext()) {
            E e = i$.next();
            this.addFirst(e);
        }
        return this;
    }

    public ChainBuilder<E> addAllFirst(E ... c) {
        if (c == null) {
            return this;
        }
        E[] arr$ = c;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            E e = arr$[i$];
            this.addFirst(e);
            ++i$;
        }
        return this;
    }

    public ChainBuilder<E> addAllLast(Collection<E> c) {
        if (c == null) {
            return this;
        }
        Iterator<E> i$ = c.iterator();
        while (i$.hasNext()) {
            E e = i$.next();
            this.addLast(e);
        }
        return this;
    }

    public ChainBuilder<E> addAllLast(E ... c) {
        if (c == null) {
            return this;
        }
        E[] arr$ = c;
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            E e = arr$[i$];
            this.addLast(e);
            ++i$;
        }
        return this;
    }

    public LinkedList<E> build() {
        return new LinkedList<E>(this.list);
    }
}

