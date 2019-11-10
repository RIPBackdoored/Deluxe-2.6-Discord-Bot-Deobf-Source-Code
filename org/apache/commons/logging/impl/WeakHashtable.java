/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.commons.logging.impl;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class WeakHashtable
extends Hashtable {
    private static final long serialVersionUID = -1546036869799732453L;
    private static final int MAX_CHANGES_BEFORE_PURGE = 100;
    private static final int PARTIAL_PURGE_COUNT = 10;
    private final ReferenceQueue queue = new ReferenceQueue();
    private int changeCount = 0;

    @Override
    public boolean containsKey(Object key) {
        Referenced referenced = new Referenced(key);
        return super.containsKey(referenced);
    }

    @Override
    public Enumeration elements() {
        this.purge();
        return super.elements();
    }

    @Override
    public Set entrySet() {
        this.purge();
        Set referencedEntries = super.entrySet();
        HashSet<Entry> unreferencedEntries = new HashSet<Entry>();
        Iterator it = referencedEntries.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            Referenced referencedKey = (Referenced)entry.getKey();
            Object key = referencedKey.getValue();
            Object value = entry.getValue();
            if (key == null) continue;
            Entry dereferencedEntry = new Entry(key, value);
            unreferencedEntries.add(dereferencedEntry);
        }
        return unreferencedEntries;
    }

    @Override
    public Object get(Object key) {
        Referenced referenceKey = new Referenced(key);
        return super.get(referenceKey);
    }

    @Override
    public Enumeration keys() {
        this.purge();
        final Enumeration enumer = super.keys();
        return new Enumeration(){

            @Override
            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }

            public Object nextElement() {
                Referenced nextReference = (Referenced)enumer.nextElement();
                return nextReference.getValue();
            }
        };
    }

    @Override
    public Set keySet() {
        this.purge();
        Set referencedKeys = super.keySet();
        HashSet<Object> unreferencedKeys = new HashSet<Object>();
        Iterator it = referencedKeys.iterator();
        while (it.hasNext()) {
            Referenced referenceKey = (Referenced)it.next();
            Object keyValue = referenceKey.getValue();
            if (keyValue == null) continue;
            unreferencedKeys.add(keyValue);
        }
        return unreferencedKeys;
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not allowed");
        }
        if (value == null) {
            throw new NullPointerException("Null values are not allowed");
        }
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
        } else if (this.changeCount % 10 == 0) {
            this.purgeOne();
        }
        Referenced keyRef = new Referenced(key, this.queue);
        return super.put(keyRef, value);
    }

    @Override
    public void putAll(Map t) {
        if (t == null) return;
        Set entrySet = t.entrySet();
        Iterator it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry entry = it.next();
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Collection values() {
        this.purge();
        return super.values();
    }

    @Override
    public synchronized Object remove(Object key) {
        if (this.changeCount++ > 100) {
            this.purge();
            this.changeCount = 0;
            return super.remove(new Referenced(key));
        }
        if (this.changeCount % 10 != 0) return super.remove(new Referenced(key));
        this.purgeOne();
        return super.remove(new Referenced(key));
    }

    @Override
    public boolean isEmpty() {
        this.purge();
        return super.isEmpty();
    }

    @Override
    public int size() {
        this.purge();
        return super.size();
    }

    @Override
    public String toString() {
        this.purge();
        return super.toString();
    }

    @Override
    protected void rehash() {
        this.purge();
        super.rehash();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void purge() {
        ArrayList<Referenced> toRemove = new ArrayList<Referenced>();
        ReferenceQueue referenceQueue = this.queue;
        synchronized (referenceQueue) {
            WeakKey key;
            while ((key = (WeakKey)this.queue.poll()) != null) {
                toRemove.add(key.getReferenced());
            }
        }
        int size = toRemove.size();
        int i = 0;
        while (i < size) {
            super.remove(toRemove.get(i));
            ++i;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void purgeOne() {
        ReferenceQueue referenceQueue = this.queue;
        synchronized (referenceQueue) {
            WeakKey key = (WeakKey)this.queue.poll();
            if (key == null) return;
            super.remove(key.getReferenced());
            return;
        }
    }

    private static final class WeakKey
    extends WeakReference {
        private final Referenced referenced;

        private WeakKey(Object key, ReferenceQueue queue, Referenced referenced) {
            super(key, queue);
            this.referenced = referenced;
        }

        private Referenced getReferenced() {
            return this.referenced;
        }
    }

    private static final class Referenced {
        private final WeakReference reference;
        private final int hashCode;

        private Referenced(Object referant) {
            this.reference = new WeakReference<Object>(referant);
            this.hashCode = referant.hashCode();
        }

        private Referenced(Object key, ReferenceQueue queue) {
            this.reference = new WeakKey(key, queue, this);
            this.hashCode = key.hashCode();
        }

        public int hashCode() {
            return this.hashCode;
        }

        private Object getValue() {
            return this.reference.get();
        }

        public boolean equals(Object o) {
            boolean result = false;
            if (!(o instanceof Referenced)) return result;
            Referenced otherKey = (Referenced)o;
            Object thisKeyValue = this.getValue();
            Object otherKeyValue = otherKey.getValue();
            if (thisKeyValue != null) {
                return thisKeyValue.equals(otherKeyValue);
            }
            if (otherKeyValue != null) return false;
            boolean bl = true;
            result = bl;
            if (!result) return false;
            if (this.hashCode() != otherKey.hashCode()) return false;
            return true;
        }
    }

    private static final class Entry
    implements Map.Entry {
        private final Object key;
        private final Object value;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            boolean result = false;
            if (o == null) return result;
            if (!(o instanceof Map.Entry)) return result;
            Map.Entry entry = (Map.Entry)o;
            if (this.getKey() == null) {
                if (entry.getKey() != null) return false;
            } else if (!this.getKey().equals(entry.getKey())) return false;
            if (this.getValue() == null) {
                if (entry.getValue() != null) return false;
                return true;
            } else if (!this.getValue().equals(entry.getValue())) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int n;
            int n2 = this.getKey() == null ? 0 : this.getKey().hashCode();
            if (this.getValue() == null) {
                n = 0;
                return n2 ^ n;
            }
            n = this.getValue().hashCode();
            return n2 ^ n;
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException("Entry.setValue is not supported.");
        }

        public Object getValue() {
            return this.value;
        }

        public Object getKey() {
            return this.key;
        }
    }

}

