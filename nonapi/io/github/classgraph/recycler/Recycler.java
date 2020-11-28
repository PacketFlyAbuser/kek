// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.recycler;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Queue;

public abstract class Recycler<T, E extends Exception> implements AutoCloseable
{
    private final /* synthetic */ Queue<T> unusedInstances;
    private final /* synthetic */ Set<T> usedInstances;
    
    public Recycler() {
        this.usedInstances = Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
        this.unusedInstances = new ConcurrentLinkedQueue<T>();
    }
    
    public abstract T newInstance() throws E, Exception;
    
    public RecycleOnClose<T, E> acquireRecycleOnClose() throws E, Exception {
        return new RecycleOnClose<T, E>(this, this.acquire());
    }
    
    public T acquire() throws E, Exception {
        final T poll = this.unusedInstances.poll();
        T t;
        if (poll == null) {
            final T instance = this.newInstance();
            if (instance == null) {
                throw new NullPointerException("Failed to allocate a new recyclable instance");
            }
            t = instance;
        }
        else {
            t = poll;
        }
        this.usedInstances.add(t);
        return t;
    }
    
    public void forceClose() {
        for (final T next : new ArrayList<T>((Collection<? extends T>)this.usedInstances)) {
            if (this.usedInstances.remove(next)) {
                this.unusedInstances.add(next);
            }
        }
        this.close();
    }
    
    public final void recycle(final T t) {
        if (t != null) {
            if (!this.usedInstances.remove(t)) {
                throw new IllegalArgumentException("Tried to recycle an instance that was not in use");
            }
            if (t instanceof Resettable) {
                ((Resettable)t).reset();
            }
            if (!this.unusedInstances.add(t)) {
                throw new IllegalArgumentException("Tried to recycle an instance twice");
            }
        }
    }
    
    @Override
    public void close() {
        T poll;
        while ((poll = this.unusedInstances.poll()) != null) {
            if (poll instanceof AutoCloseable) {
                try {
                    ((AutoCloseable)poll).close();
                }
                catch (Exception ex) {}
            }
        }
    }
}
