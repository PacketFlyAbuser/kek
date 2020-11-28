// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils
{
    private static List<Class<?>> getReverseMethodAttemptOrder(final Class<?> clazz) {
        final ArrayList<Class<?>> list = new ArrayList<Class<?>>();
        for (Class<? super Object> superclass = (Class<? super Object>)clazz; superclass != null && superclass != Object.class; superclass = superclass.getSuperclass()) {
            list.add(superclass);
        }
        final HashSet<Class<Object>> set = new HashSet<Class<Object>>();
        final LinkedList<Class<Object>> list2 = new LinkedList<Class<Object>>();
        for (Class<? super Object> superclass2 = (Class<? super Object>)clazz; superclass2 != null; superclass2 = superclass2.getSuperclass()) {
            if (superclass2.isInterface() && set.add((Class<Object>)superclass2)) {
                list2.add((Class<Object>)superclass2);
            }
            for (final Class<?> e : superclass2.getInterfaces()) {
                if (set.add((Class<Object>)e)) {
                    list2.add((Class<Object>)e);
                }
            }
        }
        while (!list2.isEmpty()) {
            final Class<Object> clazz2 = list2.remove();
            list.add(clazz2);
            final Class<?>[] interfaces2 = clazz2.getInterfaces();
            if (interfaces2.length > 0) {
                for (final Class<?> e2 : interfaces2) {
                    if (set.add((Class<Object>)e2)) {
                        list2.add((Class<Object>)e2);
                    }
                }
            }
        }
        return list;
    }
    
    public static Object getStaticFieldVal(final Class<?> clazz, final String s, final boolean b) throws IllegalArgumentException {
        if (clazz != null && s != null) {
            return getFieldVal(clazz, null, s, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    public static Object invokeMethod(final Object o, final String s, final Class<?> clazz, final Object o2, final boolean b) throws IllegalArgumentException {
        if (o != null && s != null) {
            return invokeMethod(o.getClass(), o, s, true, clazz, o2, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    public static Class<?> classForNameOrNull(final String className) {
        try {
            return Class.forName(className);
        }
        catch (ReflectiveOperationException | LinkageError ex) {
            return null;
        }
    }
    
    public static Object invokeMethod(final Object o, final String s, final boolean b) throws IllegalArgumentException {
        if (o != null && s != null) {
            return invokeMethod(o.getClass(), o, s, false, null, null, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    public static Object invokeStaticMethod(final Class<?> clazz, final String s, final Class<?> clazz2, final Object o, final boolean b) throws IllegalArgumentException {
        if (clazz != null && s != null) {
            return invokeMethod(clazz, null, s, true, clazz2, o, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    public static Object invokeStaticMethod(final Class<?> clazz, final String s, final boolean b) throws IllegalArgumentException {
        if (clazz != null && s != null) {
            return invokeMethod(clazz, null, s, false, null, null, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    public static Object getFieldVal(final Object o, final String s, final boolean b) throws IllegalArgumentException {
        if (o != null && s != null) {
            return getFieldVal(o.getClass(), o, s, b);
        }
        if (b) {
            throw new NullPointerException();
        }
        return null;
    }
    
    private ReflectionUtils() {
    }
    
    private static Object invokeMethod(final Class<?> clazz, final Object o, final String str, final boolean b, final Class<?> clazz2, final Object o2, final boolean b2) throws IllegalArgumentException {
        Method method = null;
        final List<Class<?>> reverseMethodAttemptOrder = getReverseMethodAttemptOrder(clazz);
        int i = reverseMethodAttemptOrder.size() - 1;
        while (i >= 0) {
            final Class<?> clazz3 = reverseMethodAttemptOrder.get(i);
            try {
                method = (b ? clazz3.getDeclaredMethod(str, clazz2) : clazz3.getDeclaredMethod(str, (Class<?>[])new Class[0]));
            }
            catch (ReflectiveOperationException | SecurityException ex2) {
                --i;
                continue;
            }
            break;
        }
        if (method == null) {
            if (b2) {
                throw new IllegalArgumentException(((o == null) ? "Static method " : "Method ") + "\"" + str + "\" not found or not accesible");
            }
        }
        else {
            try {
                method.setAccessible(true);
            }
            catch (RuntimeException ex3) {}
            try {
                return b ? method.invoke(o, o2) : method.invoke(o, new Object[0]);
            }
            catch (IllegalAccessException | SecurityException ex4) {
                final SecurityException ex;
                final SecurityException obj = ex;
                if (b2) {
                    throw new IllegalArgumentException("Can't call " + ((o == null) ? "static " : "") + "method \"" + str + "\": " + obj);
                }
            }
            catch (InvocationTargetException cause) {
                if (b2) {
                    throw new IllegalArgumentException("Exception while invoking " + ((o == null) ? "static " : "") + "method \"" + str + "\"", cause);
                }
            }
        }
        return null;
    }
    
    private static Object getFieldVal(final Class<?> clazz, final Object obj, final String str, final boolean b) throws IllegalArgumentException {
        Field declaredField = null;
        Class<?> superclass = clazz;
        while (superclass != null) {
            try {
                declaredField = superclass.getDeclaredField(str);
            }
            catch (ReflectiveOperationException | SecurityException ex) {
                superclass = superclass.getSuperclass();
                continue;
            }
            break;
        }
        if (declaredField == null) {
            if (b) {
                throw new IllegalArgumentException(((obj == null) ? "Static field " : "Field ") + "\"" + str + "\" not found or not accessible");
            }
        }
        else {
            try {
                declaredField.setAccessible(true);
            }
            catch (RuntimeException ex2) {}
            try {
                return declaredField.get(obj);
            }
            catch (IllegalAccessException obj2) {
                if (b) {
                    throw new IllegalArgumentException("Can't read " + ((obj == null) ? "static " : "") + " field \"" + str + "\": " + obj2);
                }
            }
        }
        return null;
    }
}
