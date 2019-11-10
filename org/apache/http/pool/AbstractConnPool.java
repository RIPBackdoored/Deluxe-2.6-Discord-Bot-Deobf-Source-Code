/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.pool;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPool;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolEntry;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.pool.RouteSpecificPool;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public abstract class AbstractConnPool<T, C, E extends PoolEntry<T, C>>
implements ConnPool<T, E>,
ConnPoolControl<T> {
    private final Lock lock;
    private final Condition condition;
    private final ConnFactory<T, C> connFactory;
    private final Map<T, RouteSpecificPool<T, C, E>> routeToPool;
    private final Set<E> leased;
    private final LinkedList<E> available;
    private final LinkedList<Future<E>> pending;
    private final Map<T, Integer> maxPerRoute;
    private volatile boolean isShutDown;
    private volatile int defaultMaxPerRoute;
    private volatile int maxTotal;
    private volatile int validateAfterInactivity;

    public AbstractConnPool(ConnFactory<T, C> connFactory, int defaultMaxPerRoute, int maxTotal) {
        this.connFactory = Args.notNull(connFactory, "Connection factory");
        this.defaultMaxPerRoute = Args.positive(defaultMaxPerRoute, "Max per route value");
        this.maxTotal = Args.positive(maxTotal, "Max total value");
        this.lock = new ReentrantLock();
        this.condition = this.lock.newCondition();
        this.routeToPool = new HashMap<T, RouteSpecificPool<T, C, E>>();
        this.leased = new HashSet();
        this.available = new LinkedList();
        this.pending = new LinkedList();
        this.maxPerRoute = new HashMap<T, Integer>();
    }

    protected abstract E createEntry(T var1, C var2);

    protected void onLease(E entry) {
    }

    protected void onRelease(E entry) {
    }

    protected void onReuse(E entry) {
    }

    protected boolean validate(E entry) {
        return true;
    }

    public boolean isShutdown() {
        return this.isShutDown;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void shutdown() throws IOException {
        if (this.isShutDown) {
            return;
        }
        this.isShutDown = true;
        this.lock.lock();
        try {
            for (PoolEntry entry : this.available) {
                entry.close();
            }
            for (PoolEntry entry : this.leased) {
                entry.close();
            }
            for (RouteSpecificPool pool : this.routeToPool.values()) {
                pool.shutdown();
            }
            this.routeToPool.clear();
            this.leased.clear();
            this.available.clear();
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    private RouteSpecificPool<T, C, E> getPool(final T route) {
        RouteSpecificPool<T, C, E> pool = this.routeToPool.get(route);
        if (pool != null) return pool;
        pool = new RouteSpecificPool<T, C, E>(route){

            @Override
            protected E createEntry(C conn) {
                return AbstractConnPool.this.createEntry(route, conn);
            }
        };
        this.routeToPool.put(route, pool);
        return pool;
    }

    @Override
    public Future<E> lease(final T route, final Object state, final FutureCallback<E> callback) {
        Args.notNull(route, "Route");
        Asserts.check(!this.isShutDown, "Connection pool shut down");
        return new Future<E>(){
            private final AtomicBoolean cancelled = new AtomicBoolean(false);
            private final AtomicBoolean done = new AtomicBoolean(false);
            private final AtomicReference<E> entryRef = new AtomicReference<Object>(null);

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (!this.cancelled.compareAndSet(false, true)) return false;
                this.done.set(true);
                AbstractConnPool.this.lock.lock();
                try {
                    AbstractConnPool.this.condition.signalAll();
                }
                finally {
                    AbstractConnPool.this.lock.unlock();
                }
                if (callback == null) return true;
                callback.cancelled();
                return true;
            }

            @Override
            public boolean isCancelled() {
                return this.cancelled.get();
            }

            @Override
            public boolean isDone() {
                return this.done.get();
            }

            @Override
            public E get() throws InterruptedException, ExecutionException {
                try {
                    return (E)this.get(0L, TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException ex) {
                    throw new ExecutionException(ex);
                }
            }

            @Override
            public E get(long timeout, TimeUnit tunit) throws InterruptedException, ExecutionException, TimeoutException {
                PoolEntry entry = (PoolEntry)this.entryRef.get();
                if (entry != null) {
                    return (E)entry;
                }
                2 var5_4 = this;
                synchronized (var5_4) {
                    try {
                        PoolEntry leasedEntry;
                        do {
                            leasedEntry = AbstractConnPool.this.getPoolEntryBlocking(route, state, timeout, tunit, this);
                            if (AbstractConnPool.this.validateAfterInactivity <= 0 || leasedEntry.getUpdated() + (long)AbstractConnPool.this.validateAfterInactivity > System.currentTimeMillis() || AbstractConnPool.this.validate(leasedEntry)) break;
                            leasedEntry.close();
                            AbstractConnPool.this.release(leasedEntry, false);
                        } while (true);
                        this.entryRef.set(leasedEntry);
                        this.done.set(true);
                        AbstractConnPool.this.onLease(leasedEntry);
                        if (callback == null) return (E)leasedEntry;
                        callback.completed(leasedEntry);
                        return (E)leasedEntry;
                    }
                    catch (IOException ex) {
                        this.done.set(true);
                        if (callback == null) throw new ExecutionException(ex);
                        callback.failed(ex);
                        throw new ExecutionException(ex);
                    }
                }
            }
        };
    }

    public Future<E> lease(T route, Object state) {
        return this.lease(route, state, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private E getPoolEntryBlocking(T route, Object state, long timeout, TimeUnit tunit, Future<E> future) throws IOException, InterruptedException, TimeoutException {
        Date deadline = null;
        if (timeout > 0L) {
            deadline = new Date(System.currentTimeMillis() + tunit.toMillis(timeout));
        }
        this.lock.lock();
        try {
            boolean success;
            RouteSpecificPool<T, C, E> pool = this.getPool(route);
            do {
                int totalUsed;
                int freeCapacity;
                E entry;
                Asserts.check(!this.isShutDown, "Connection pool shut down");
                while ((entry = pool.getFree(state)) != null) {
                    if (((PoolEntry)entry).isExpired(System.currentTimeMillis())) {
                        ((PoolEntry)entry).close();
                    }
                    if (!((PoolEntry)entry).isClosed()) break;
                    this.available.remove(entry);
                    pool.free(entry, false);
                }
                if (entry != null) {
                    this.available.remove(entry);
                    this.leased.add(entry);
                    this.onReuse(entry);
                    E e = entry;
                    return e;
                }
                int maxPerRoute = this.getMax(route);
                int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
                if (excess > 0) {
                    E lastUsed;
                    for (int i = 0; i < excess && (lastUsed = pool.getLastUsed()) != null; ++i) {
                        ((PoolEntry)lastUsed).close();
                        this.available.remove(lastUsed);
                        pool.remove(lastUsed);
                    }
                }
                if (pool.getAllocatedCount() < maxPerRoute && (freeCapacity = Math.max(this.maxTotal - (totalUsed = this.leased.size()), 0)) > 0) {
                    RouteSpecificPool<T, C, PoolEntry> otherpool;
                    int totalAvailable = this.available.size();
                    if (totalAvailable > freeCapacity - 1 && !this.available.isEmpty()) {
                        PoolEntry lastUsed = (PoolEntry)this.available.removeLast();
                        lastUsed.close();
                        otherpool = this.getPool(lastUsed.getRoute());
                        otherpool.remove(lastUsed);
                    }
                    C conn = this.connFactory.create(route);
                    entry = pool.add(conn);
                    this.leased.add(entry);
                    otherpool = entry;
                    return (E)otherpool;
                }
                success = false;
                try {
                    if (future.isCancelled()) {
                        throw new InterruptedException("Operation interrupted");
                    }
                    pool.queue(future);
                    this.pending.add(future);
                    if (deadline != null) {
                        success = this.condition.awaitUntil(deadline);
                    } else {
                        this.condition.await();
                        success = true;
                    }
                    if (future.isCancelled()) {
                        throw new InterruptedException("Operation interrupted");
                    }
                }
                finally {
                    pool.unqueue(future);
                    this.pending.remove(future);
                }
            } while (success || deadline == null || deadline.getTime() > System.currentTimeMillis());
            throw new TimeoutException("Timeout waiting for connection");
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void release(E entry, boolean reusable) {
        this.lock.lock();
        try {
            if (!this.leased.remove(entry)) return;
            RouteSpecificPool<T, C, E> pool = this.getPool(((PoolEntry)entry).getRoute());
            pool.free(entry, reusable);
            if (reusable && !this.isShutDown) {
                this.available.addFirst(entry);
            } else {
                ((PoolEntry)entry).close();
            }
            this.onRelease(entry);
            Future<E> future = pool.nextPending();
            if (future != null) {
                this.pending.remove(future);
            } else {
                future = this.pending.poll();
            }
            if (future == null) return;
            this.condition.signalAll();
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    private int getMax(T route) {
        Integer v = this.maxPerRoute.get(route);
        if (v == null) return this.defaultMaxPerRoute;
        return v;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setMaxTotal(int max) {
        Args.positive(max, "Max value");
        this.lock.lock();
        try {
            this.maxTotal = max;
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getMaxTotal() {
        this.lock.lock();
        try {
            int n = this.maxTotal;
            return n;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setDefaultMaxPerRoute(int max) {
        Args.positive(max, "Max per route value");
        this.lock.lock();
        try {
            this.defaultMaxPerRoute = max;
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getDefaultMaxPerRoute() {
        this.lock.lock();
        try {
            int n = this.defaultMaxPerRoute;
            return n;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void setMaxPerRoute(T route, int max) {
        Args.notNull(route, "Route");
        this.lock.lock();
        try {
            if (max > -1) {
                this.maxPerRoute.put(route, max);
                return;
            }
            this.maxPerRoute.remove(route);
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int getMaxPerRoute(T route) {
        Args.notNull(route, "Route");
        this.lock.lock();
        try {
            int n = this.getMax(route);
            return n;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public PoolStats getTotalStats() {
        this.lock.lock();
        try {
            PoolStats poolStats = new PoolStats(this.leased.size(), this.pending.size(), this.available.size(), this.maxTotal);
            return poolStats;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public PoolStats getStats(T route) {
        Args.notNull(route, "Route");
        this.lock.lock();
        try {
            RouteSpecificPool<T, C, E> pool = this.getPool(route);
            PoolStats poolStats = new PoolStats(pool.getLeasedCount(), pool.getPendingCount(), pool.getAvailableCount(), this.getMax(route));
            return poolStats;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Set<T> getRoutes() {
        this.lock.lock();
        try {
            HashSet<T> hashSet = new HashSet<T>(this.routeToPool.keySet());
            return hashSet;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void enumAvailable(PoolEntryCallback<T, C> callback) {
        this.lock.lock();
        try {
            Iterator<E> it = this.available.iterator();
            while (it.hasNext()) {
                PoolEntry entry = (PoolEntry)it.next();
                callback.process(entry);
                if (!entry.isClosed()) continue;
                RouteSpecificPool<T, C, PoolEntry> pool = this.getPool(entry.getRoute());
                pool.remove(entry);
                it.remove();
            }
            this.purgePoolMap();
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void enumLeased(PoolEntryCallback<T, C> callback) {
        this.lock.lock();
        try {
            Iterator<E> it = this.leased.iterator();
            while (it.hasNext()) {
                PoolEntry entry = (PoolEntry)it.next();
                callback.process(entry);
            }
            return;
        }
        finally {
            this.lock.unlock();
        }
    }

    private void purgePoolMap() {
        Iterator<Map.Entry<T, RouteSpecificPool<T, C, E>>> it = this.routeToPool.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, RouteSpecificPool<T, C, E>> entry = it.next();
            RouteSpecificPool<T, C, E> pool = entry.getValue();
            if (pool.getPendingCount() + pool.getAllocatedCount() != 0) continue;
            it.remove();
        }
    }

    public void closeIdle(long idletime, TimeUnit tunit) {
        Args.notNull(tunit, "Time unit");
        long time = tunit.toMillis(idletime);
        if (time < 0L) {
            time = 0L;
        }
        final long deadline = System.currentTimeMillis() - time;
        this.enumAvailable(new PoolEntryCallback<T, C>(){

            @Override
            public void process(PoolEntry<T, C> entry) {
                if (entry.getUpdated() > deadline) return;
                entry.close();
            }
        });
    }

    public void closeExpired() {
        final long now = System.currentTimeMillis();
        this.enumAvailable(new PoolEntryCallback<T, C>(){

            @Override
            public void process(PoolEntry<T, C> entry) {
                if (!entry.isExpired(now)) return;
                entry.close();
            }
        });
    }

    public int getValidateAfterInactivity() {
        return this.validateAfterInactivity;
    }

    public void setValidateAfterInactivity(int ms) {
        this.validateAfterInactivity = ms;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[leased: ");
        buffer.append(this.leased);
        buffer.append("][available: ");
        buffer.append(this.available);
        buffer.append("][pending: ");
        buffer.append(this.pending);
        buffer.append("]");
        return buffer.toString();
    }

}

