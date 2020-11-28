// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.concurrent.ConcurrentMap;

public abstract class SingletonMap<K, V, E extends Exception>
{
    private final /* synthetic */ ConcurrentMap<K, SingletonHolder<V>> map;
    
    public V remove(final K k) throws InterruptedException {
        final SingletonHolder<Object> singletonHolder = this.map.remove(k);
        return (V)((singletonHolder == null) ? null : singletonHolder.get());
    }
    
    public abstract V newInstance(final K p0, final LogNode p1) throws E, InterruptedException, Exception;
    
    public List<Map.Entry<K, V>> entries() throws InterruptedException {
        final ArrayList<AbstractMap.SimpleEntry<Object, Object>> list = (ArrayList<AbstractMap.SimpleEntry<Object, Object>>)new ArrayList<Map.Entry<Object, Object>>(this.map.size());
        for (final Map.Entry<Object, Object> entry : this.map.entrySet()) {
            list.add(new AbstractMap.SimpleEntry<Object, Object>(entry.getKey(), entry.getValue().get()));
        }
        return (List<Map.Entry<K, V>>)list;
    }
    
    public List<V> values() throws InterruptedException {
        final ArrayList<V> list = new ArrayList<V>(this.map.size());
        final Iterator<Map.Entry<Object, Object>> iterator = this.map.entrySet().iterator();
        while (iterator.hasNext()) {
            final Object value = iterator.next().getValue().get();
            if (value != null) {
                list.add((V)value);
            }
        }
        return list;
    }
    
    public SingletonMap() {
        this.map = new ConcurrentHashMap<K, SingletonHolder<V>>();
    }
    
    public V get(final K k, final LogNode logNode) throws E, InterruptedException, NullSingletonException, Exception {
        final SingletonHolder<Object> singletonHolder = this.map.get(k);
        V v = null;
        if (singletonHolder != null) {
            v = singletonHolder.get();
        }
        else {
            final SingletonHolder<V> singletonHolder2 = new SingletonHolder<V>();
            final SingletonHolder<V> singletonHolder3 = this.map.putIfAbsent(k, singletonHolder2);
            if (singletonHolder3 != null) {
                v = singletonHolder3.get();
            }
            else {
                try {
                    v = this.newInstance(k, logNode);
                }
                finally {
                    singletonHolder2.set(v);
                }
            }
        }
        if (v == null) {
            throw new NullSingletonException((K)k);
        }
        return v;
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public void clear() {
        this.map.clear();
    }
    
    public static class NullSingletonException extends Exception
    {
        public <K> NullSingletonException(final K obj) {
            super("newInstance returned null for key " + obj);
        }
    }
    
    private static class SingletonHolder<V>
    {
        private final /* synthetic */ CountDownLatch initialized;
        private volatile /* synthetic */ V singleton;
        
        private SingletonHolder() {
            this.initialized = new CountDownLatch(1);
        }
        
        V get() throws InterruptedException {
            this.initialized.await();
            return this.singleton;
        }
        
        void set(final V singleton) throws IllegalArgumentException {
            if (this.initialized.getCount() < 1L) {
                throw new IllegalArgumentException("Singleton already initialized");
            }
            this.singleton = singleton;
            this.initialized.countDown();
            if (this.initialized.getCount() != 0L) {
                throw new IllegalArgumentException("Singleton initialized more than once");
            }
        }
    }
}
