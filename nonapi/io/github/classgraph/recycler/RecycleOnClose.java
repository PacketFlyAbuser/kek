// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.recycler;

public class RecycleOnClose<T, E extends Exception> implements AutoCloseable
{
    private final /* synthetic */ Recycler<T, E> recycler;
    private final /* synthetic */ T instance;
    
    public T get() {
        return this.instance;
    }
    
    RecycleOnClose(final Recycler<T, E> recycler, final T instance) {
        this.recycler = recycler;
        this.instance = instance;
    }
    
    @Override
    public void close() {
        this.recycler.recycle(this.instance);
    }
}
