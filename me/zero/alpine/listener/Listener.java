// 
// Decompiled by Procyon v0.5.36
// 

package me.zero.alpine.listener;

import net.jodah.typetools.TypeResolver;
import java.util.function.Predicate;

public final class Listener<T> implements EventHook<T>
{
    private final /* synthetic */ Predicate<T>[] filters;
    private final /* synthetic */ EventHook<T> hook;
    private final /* synthetic */ byte priority;
    private final /* synthetic */ Class<T> target;
    
    @SafeVarargs
    public Listener(final EventHook<T> hook, final byte priority, final Predicate<T>... filters) {
        this.hook = hook;
        this.priority = priority;
        this.target = (Class<T>)TypeResolver.resolveRawArgument(EventHook.class, hook.getClass());
        this.filters = filters;
    }
    
    public final Class<T> getTarget() {
        return this.target;
    }
    
    @SafeVarargs
    public Listener(final EventHook<T> eventHook, final Predicate<T>... array) {
        this(eventHook, (byte)3, (Predicate[])array);
    }
    
    @Override
    public final void invoke(final T t) {
        if (this.filters.length > 0) {
            final Predicate<T>[] filters = this.filters;
            for (int length = filters.length, i = 0; i < length; ++i) {
                if (!filters[i].test(t)) {
                    return;
                }
            }
        }
        this.hook.invoke(t);
    }
    
    public final byte getPriority() {
        return this.priority;
    }
}
