// 
// Decompiled by Procyon v0.5.36
// 

package me.zero.alpine;

import java.lang.annotation.Annotation;
import me.zero.alpine.listener.EventHandler;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Objects;
import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.function.Consumer;
import me.zero.alpine.listener.Listener;
import java.util.List;
import java.util.Map;

public class EventManager implements EventBus
{
    private final /* synthetic */ Map<Class<?>, List<Listener>> SUBSCRIPTION_MAP;
    private final /* synthetic */ Map<Object, List<Listener>> SUBSCRIPTION_CACHE;
    private final /* synthetic */ List<EventBus> ATTACHED_BUSES;
    
    @Override
    public void subscribe(final Iterable<Object> iterable) {
        iterable.forEach(this::subscribe);
    }
    
    @Override
    public void attach(final EventBus eventBus) {
        if (!this.ATTACHED_BUSES.contains(eventBus)) {
            this.ATTACHED_BUSES.add(eventBus);
        }
    }
    
    @Override
    public void unsubscribe(final Object... array) {
        Arrays.stream(array).forEach(this::unsubscribe);
    }
    
    private static Listener asListener(final Object obj, final Field field) {
        try {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            final Listener listener = (Listener)field.get(obj);
            field.setAccessible(accessible);
            if (listener == null) {
                return null;
            }
            if (listener.getPriority() > 5 || listener.getPriority() < 1) {
                throw new RuntimeException("Event Priority out of bounds! %s");
            }
            return listener;
        }
        catch (IllegalAccessException ex) {
            return null;
        }
    }
    
    private void subscribe(final Listener listener) {
        List<Listener> list;
        int n;
        for (list = this.SUBSCRIPTION_MAP.computeIfAbsent(listener.getTarget(), p0 -> new ArrayList()), n = 0; n < list.size() && listener.getPriority() >= list.get(n).getPriority(); ++n) {}
        list.add(n, listener);
    }
    
    @Override
    public void subscribe(final Object... array) {
        Arrays.stream(array).forEach(this::subscribe);
    }
    
    public EventManager() {
        this.SUBSCRIPTION_CACHE = new HashMap<Object, List<Listener>>();
        this.SUBSCRIPTION_MAP = new HashMap<Class<?>, List<Listener>>();
        this.ATTACHED_BUSES = new ArrayList<EventBus>();
    }
    
    @Override
    public void subscribe(final Object key) {
        this.SUBSCRIPTION_CACHE.computeIfAbsent(key, o -> Arrays.stream(o.getClass().getDeclaredFields()).filter(EventManager::isValidField).map(field -> asListener(o, field)).filter(Objects::nonNull).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())).forEach(this::subscribe);
        if (!this.ATTACHED_BUSES.isEmpty()) {
            this.ATTACHED_BUSES.forEach(eventBus -> eventBus.subscribe(key));
        }
    }
    
    @Override
    public void unsubscribe(final Object o) {
        if (this.SUBSCRIPTION_CACHE.get(o) == null) {
            return;
        }
        final List list2;
        this.SUBSCRIPTION_MAP.values().forEach(list -> list.removeIf(list2::contains));
        if (!this.ATTACHED_BUSES.isEmpty()) {
            this.ATTACHED_BUSES.forEach(eventBus -> eventBus.unsubscribe(o));
        }
    }
    
    @Override
    public void post(final Object o) {
        final List<Listener> list = this.SUBSCRIPTION_MAP.get(o.getClass());
        if (list != null) {
            list.forEach(listener -> listener.invoke(o));
        }
        if (!this.ATTACHED_BUSES.isEmpty()) {
            this.ATTACHED_BUSES.forEach(eventBus -> eventBus.post(o));
        }
    }
    
    @Override
    public void detach(final EventBus eventBus) {
        if (this.ATTACHED_BUSES.contains(eventBus)) {
            this.ATTACHED_BUSES.remove(eventBus);
        }
    }
    
    @Override
    public void unsubscribe(final Iterable<Object> iterable) {
        iterable.forEach(this::unsubscribe);
    }
    
    private static boolean isValidField(final Field field) {
        return field.isAnnotationPresent(EventHandler.class) && Listener.class.isAssignableFrom(field.getType());
    }
}
