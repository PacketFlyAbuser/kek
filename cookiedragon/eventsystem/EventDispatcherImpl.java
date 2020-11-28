// 
// Decompiled by Procyon v0.5.36
// 

package cookiedragon.eventsystem;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.HashSet;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.Map;
import kotlin.Metadata;

@Metadata(mv = { 1, 1, 16 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001f\u0010\u000b\u001a\u0002H\f\"\b\b\u0000\u0010\f*\u00020\r2\u0006\u0010\u000e\u001a\u0002H\fH\u0016¢\u0006\u0002\u0010\u000fJ\u0014\u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\bH\u0016J\u001e\u0010\u0010\u001a\u00020\u00112\n\u0010\u0013\u001a\u0006\u0012\u0002\b\u00030\b2\b\u0010\u0014\u001a\u0004\u0018\u00010\rH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rH\u0016J\u001c\u0010\u0015\u001a\u00020\u00112\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\b2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u001a\u0010\u0015\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0014\u0010\u0018\u001a\u00020\u00112\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\bH\u0016J\u0010\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rH\u0016J\u0014\u0010\u0019\u001a\u00020\u00112\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\bH\u0016J\u0010\u0010\u0019\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\rH\u0016R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R(\u0010\u0006\u001a\u001c\u0012\b\u0012\u0006\u0012\u0002\b\u00030\b\u0012\u000e\u0012\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n0\t0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a" }, d2 = { "Lcookiedragon/eventsystem/EventDispatcherImpl;", "Lcookiedragon/eventsystem/EventDispatcher;", "()V", "lookup", "Ljava/lang/invoke/MethodHandles$Lookup;", "kotlin.jvm.PlatformType", "subscriptions", "", "Ljava/lang/Class;", "", "Lcookiedragon/eventsystem/SubscribingMethod;", "dispatch", "T", "", "event", "(Ljava/lang/Object;)Ljava/lang/Object;", "register", "", "subscriber", "clazz", "instance", "setActive", "active", "", "subscribe", "unsubscribe", "EventSystem" })
public final class EventDispatcherImpl implements EventDispatcher
{
    private static final /* synthetic */ Map<Class<?>, Set<SubscribingMethod<?>>> subscriptions;
    private static final /* synthetic */ MethodHandles.Lookup lookup;
    
    @Override
    public void unsubscribe(@NotNull final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "subscriber");
        this.setActive(o, false);
    }
    
    @NotNull
    @Override
    public <T> T dispatch(@NotNull final T t) {
        Intrinsics.checkParameterIsNotNull((Object)t, "event");
        Class<?> class1 = t.getClass();
        while (true) {
            final Set<SubscribingMethod<?>> set = EventDispatcherImpl.subscriptions.get(class1);
            if (set != null) {
                for (final SubscribingMethod<?> subscribingMethod : set) {
                    if (subscribingMethod.getActive()) {
                        subscribingMethod.invoke(t);
                    }
                }
            }
            if (Intrinsics.areEqual((Object)class1, (Object)Object.class)) {
                break;
            }
            final Class<?> superclass = class1.getSuperclass();
            Intrinsics.checkExpressionValueIsNotNull((Object)superclass, "clazz.superclass");
            class1 = superclass;
        }
        return t;
    }
    
    private final void setActive(final Class<?> clazz, final boolean active) {
        final Iterator<Set<SubscribingMethod<?>>> iterator = EventDispatcherImpl.subscriptions.values().iterator();
        while (iterator.hasNext()) {
            for (final SubscribingMethod<?> subscribingMethod : iterator.next()) {
                if (Intrinsics.areEqual((Object)subscribingMethod.getClazz(), (Object)clazz)) {
                    subscribingMethod.setActive(active);
                }
            }
        }
    }
    
    @Override
    public void subscribe(@NotNull final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "subscriber");
        this.setActive(o, true);
    }
    
    private final void register(final Class<?> clazz, final Object o) {
        for (final Method m : clazz.getDeclaredMethods()) {
            Label_0322: {
                if (o == null) {
                    final Method method = m;
                    Intrinsics.checkExpressionValueIsNotNull((Object)method, "method");
                    if (!EventDispatcherImplKt.access$isStatic(method)) {
                        break Label_0322;
                    }
                }
                if (o != null) {
                    final Method method2 = m;
                    Intrinsics.checkExpressionValueIsNotNull((Object)method2, "method");
                    if (EventDispatcherImplKt.access$isStatic(method2)) {
                        break Label_0322;
                    }
                }
                if (m.isAnnotationPresent(Subscriber.class)) {
                    final Method method3 = m;
                    Intrinsics.checkExpressionValueIsNotNull((Object)method3, "method");
                    if (Intrinsics.areEqual((Object)method3.getReturnType(), (Object)Void.TYPE) ^ true) {
                        new IllegalArgumentException("Subscriber " + clazz + '.' + m.getName() + " cannot return type").printStackTrace();
                    }
                    else if (m.getParameterCount() != 1) {
                        new IllegalArgumentException("Expected only 1 parameter for " + clazz + '.' + m.getName()).printStackTrace();
                    }
                    else {
                        m.setAccessible(true);
                        final Class<?> clazz2 = m.getParameterTypes()[0];
                        if (clazz2 == null) {
                            Intrinsics.throwNpe();
                        }
                        final Class<?> clazz3 = clazz2;
                        final MethodHandle unreflect = EventDispatcherImpl.lookup.unreflect(m);
                        final Map<Class<?>, Set<SubscribingMethod<?>>> subscriptions = EventDispatcherImpl.subscriptions;
                        final Set<SubscribingMethod<?>> value = subscriptions.get(clazz3);
                        Set<SubscribingMethod<?>> set2;
                        if (value == null) {
                            final HashSet<SubscribingMethod<?>> set = new HashSet<SubscribingMethod<?>>();
                            subscriptions.put(clazz3, set);
                            set2 = set;
                        }
                        else {
                            set2 = value;
                        }
                        final HashSet<SubscribingMethod<?>> set3 = (HashSet<SubscribingMethod<?>>)set2;
                        final boolean access$isStatic = EventDispatcherImplKt.access$isStatic(m);
                        final MethodHandle methodHandle = unreflect;
                        Intrinsics.checkExpressionValueIsNotNull((Object)methodHandle, "methodHandle");
                        set3.add(new SubscribingMethod<Object>(clazz, o, access$isStatic, methodHandle, false, 16, null));
                    }
                }
            }
        }
    }
    
    @Override
    public void register(@NotNull final Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull((Object)clazz, "subscriber");
        this.register(clazz, null);
    }
    
    private EventDispatcherImpl() {
    }
    
    static {
        INSTANCE = new EventDispatcherImpl();
        lookup = MethodHandles.lookup();
        subscriptions = new ConcurrentHashMap<Class<?>, Set<SubscribingMethod<?>>>();
    }
    
    @Override
    public void subscribe(@NotNull final Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull((Object)clazz, "subscriber");
        this.setActive(clazz, true);
    }
    
    @Override
    public void unsubscribe(@NotNull final Class<?> clazz) {
        Intrinsics.checkParameterIsNotNull((Object)clazz, "subscriber");
        this.setActive(clazz, false);
    }
    
    @Override
    public void register(@NotNull final Object o) {
        Intrinsics.checkParameterIsNotNull(o, "subscriber");
        this.register(o.getClass(), o);
    }
    
    private final void setActive(final Object o, final boolean active) {
        final Iterator<Set<SubscribingMethod<?>>> iterator = EventDispatcherImpl.subscriptions.values().iterator();
        while (iterator.hasNext()) {
            for (final SubscribingMethod<Object> subscribingMethod : iterator.next()) {
                if (Intrinsics.areEqual(subscribingMethod.getInstance(), o)) {
                    subscribingMethod.setActive(active);
                }
            }
        }
    }
}
