/*
 * Decompiled with CFR <Could not determine version>.
 */
package org.apache.http.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.Args;

public class BasicFuture<T>
implements Future<T>,
Cancellable {
    private final FutureCallback<T> callback;
    private volatile boolean completed;
    private volatile boolean cancelled;
    private volatile T result;
    private volatile Exception ex;

    public BasicFuture(FutureCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return this.completed;
    }

    private T getResult() throws ExecutionException {
        if (this.ex != null) {
            throw new ExecutionException(this.ex);
        }
        if (!this.cancelled) return this.result;
        throw new CancellationException();
    }

    @Override
    public synchronized T get() throws InterruptedException, ExecutionException {
        while (!this.completed) {
            this.wait();
        }
        return this.getResult();
    }

    @Override
    public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Args.notNull(unit, "Time unit");
        long msecs = unit.toMillis(timeout);
        long startTime = msecs <= 0L ? 0L : System.currentTimeMillis();
        long waitTime = msecs;
        if (this.completed) {
            return this.getResult();
        }
        if (waitTime <= 0L) {
            throw new TimeoutException();
        }
        do {
            this.wait(waitTime);
            if (!this.completed) continue;
            return this.getResult();
        } while ((waitTime = msecs - (System.currentTimeMillis() - startTime)) > 0L);
        throw new TimeoutException();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean completed(T result) {
        BasicFuture basicFuture = this;
        synchronized (basicFuture) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.result = result;
            this.notifyAll();
        }
        if (this.callback == null) return true;
        this.callback.completed(result);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean failed(Exception exception) {
        BasicFuture basicFuture = this;
        synchronized (basicFuture) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.ex = exception;
            this.notifyAll();
        }
        if (this.callback == null) return true;
        this.callback.failed(exception);
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        BasicFuture basicFuture = this;
        synchronized (basicFuture) {
            if (this.completed) {
                return false;
            }
            this.completed = true;
            this.cancelled = true;
            this.notifyAll();
        }
        if (this.callback == null) return true;
        this.callback.cancelled();
        return true;
    }

    @Override
    public boolean cancel() {
        return this.cancel(true);
    }
}

