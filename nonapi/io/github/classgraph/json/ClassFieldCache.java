// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import io.github.classgraph.ClassGraphException;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.AbstractQueue;
import java.util.Queue;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.AbstractSet;
import java.util.Set;
import java.util.LinkedList;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;
import java.util.HashMap;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.util.Map;

class ClassFieldCache
{
    private final /* synthetic */ Map<Class<?>, Constructor<?>> defaultConstructorForConcreteType;
    private final /* synthetic */ Map<Class<?>, Constructor<?>> constructorForConcreteTypeWithSizeHint;
    private final /* synthetic */ boolean onlySerializePublicFields;
    private final /* synthetic */ boolean resolveTypes;
    private static final /* synthetic */ Constructor<?> NO_CONSTRUCTOR;
    private final /* synthetic */ Map<Class<?>, ClassFields> classToClassFields;
    
    Constructor<?> getConstructorWithSizeHintForConcreteTypeOf(final Class<?> clazz) {
        final Constructor<?> constructor = this.constructorForConcreteTypeWithSizeHint.get(clazz);
        if (constructor == ClassFieldCache.NO_CONSTRUCTOR) {
            return null;
        }
        if (constructor != null) {
            return constructor;
        }
        final Class<?> concreteType = getConcreteType(clazz, true);
        if (concreteType != null) {
            Class<?> superclass = concreteType;
            while (superclass != null) {
                if (superclass == Object.class) {
                    if (clazz != Object.class) {
                        break;
                    }
                }
                try {
                    final Constructor<Object> declaredConstructor = superclass.getDeclaredConstructor(Integer.TYPE);
                    JSONUtils.isAccessibleOrMakeAccessible(declaredConstructor);
                    this.constructorForConcreteTypeWithSizeHint.put(clazz, declaredConstructor);
                    return declaredConstructor;
                }
                catch (ReflectiveOperationException | SecurityException ex) {
                    superclass = superclass.getSuperclass();
                    continue;
                }
                break;
            }
        }
        this.constructorForConcreteTypeWithSizeHint.put(clazz, ClassFieldCache.NO_CONSTRUCTOR);
        return null;
    }
    
    ClassFieldCache(final boolean resolveTypes, final boolean b) {
        this.classToClassFields = new HashMap<Class<?>, ClassFields>();
        this.defaultConstructorForConcreteType = new HashMap<Class<?>, Constructor<?>>();
        this.constructorForConcreteTypeWithSizeHint = new HashMap<Class<?>, Constructor<?>>();
        this.resolveTypes = resolveTypes;
        this.onlySerializePublicFields = (!resolveTypes && b);
    }
    
    ClassFields get(final Class<?> clazz) {
        ClassFields classFields = this.classToClassFields.get(clazz);
        if (classFields == null) {
            this.classToClassFields.put(clazz, classFields = new ClassFields(clazz, this.resolveTypes, this.onlySerializePublicFields, this));
        }
        return classFields;
    }
    
    private static Class<?> getConcreteType(final Class<?> clazz, final boolean b) {
        if (clazz == Map.class || clazz == AbstractMap.class || clazz == HashMap.class) {
            return HashMap.class;
        }
        if (clazz == ConcurrentMap.class || clazz == ConcurrentHashMap.class) {
            return ConcurrentHashMap.class;
        }
        if (clazz == SortedMap.class || clazz == NavigableMap.class || clazz == TreeMap.class) {
            return TreeMap.class;
        }
        if (clazz == ConcurrentNavigableMap.class || clazz == ConcurrentSkipListMap.class) {
            return ConcurrentSkipListMap.class;
        }
        if (clazz == List.class || clazz == AbstractList.class || clazz == ArrayList.class || clazz == Collection.class) {
            return ArrayList.class;
        }
        if (clazz == AbstractSequentialList.class || clazz == LinkedList.class) {
            return LinkedList.class;
        }
        if (clazz == Set.class || clazz == AbstractSet.class || clazz == HashSet.class) {
            return HashSet.class;
        }
        if (clazz == SortedSet.class || clazz == TreeSet.class) {
            return TreeSet.class;
        }
        if (clazz == Queue.class || clazz == AbstractQueue.class || clazz == Deque.class || clazz == ArrayDeque.class) {
            return ArrayDeque.class;
        }
        if (clazz == BlockingQueue.class || clazz == LinkedBlockingQueue.class) {
            return LinkedBlockingQueue.class;
        }
        if (clazz == BlockingDeque.class || clazz == LinkedBlockingDeque.class) {
            return LinkedBlockingDeque.class;
        }
        if (clazz == TransferQueue.class || clazz == LinkedTransferQueue.class) {
            return LinkedTransferQueue.class;
        }
        return b ? null : clazz;
    }
    
    static {
        try {
            NO_CONSTRUCTOR = NoConstructor.class.getDeclaredConstructor((Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException | SecurityException ex) {
            final Object o;
            throw ClassGraphException.newClassGraphException("Could not find or access constructor for " + NoConstructor.class.getName(), (Throwable)o);
        }
    }
    
    Constructor<?> getDefaultConstructorForConcreteTypeOf(final Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class reference cannot be null");
        }
        final Constructor<?> constructor = this.defaultConstructorForConcreteType.get(clazz);
        if (constructor != null) {
            return constructor;
        }
        Class<?> clazz2 = getConcreteType(clazz, false);
        while (clazz2 != null) {
            if (clazz2 == Object.class) {
                if (clazz != Object.class) {
                    break;
                }
            }
            try {
                final Constructor<Object> declaredConstructor = clazz2.getDeclaredConstructor((Class<?>[])new Class[0]);
                JSONUtils.isAccessibleOrMakeAccessible(declaredConstructor);
                this.defaultConstructorForConcreteType.put(clazz, declaredConstructor);
                return declaredConstructor;
            }
            catch (ReflectiveOperationException | SecurityException ex) {
                clazz2 = clazz2.getSuperclass();
                continue;
            }
            break;
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " does not have an accessible default (no-arg) constructor");
    }
    
    private static class NoConstructor
    {
        public NoConstructor() {
        }
    }
}
