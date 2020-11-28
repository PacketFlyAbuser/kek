// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.util.Collection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;

public final class JSONUtils
{
    private static /* synthetic */ MethodHandle isAccessibleMethodHandle;
    private static /* synthetic */ Method trySetAccessibleMethod;
    private static /* synthetic */ Method isAccessibleMethod;
    private static final /* synthetic */ String[] INDENT_LEVELS;
    private static /* synthetic */ MethodHandle trySetAccessibleMethodHandle;
    private static final /* synthetic */ String[] JSON_CHAR_REPLACEMENTS;
    
    static void escapeJSONString(final String str, final StringBuilder sb) {
        if (str == null) {
            return;
        }
        boolean b = false;
        for (int i = 0; i < str.length(); ++i) {
            final char char1 = str.charAt(i);
            if (char1 > '\u00ff' || JSONUtils.JSON_CHAR_REPLACEMENTS[char1] != null) {
                b = true;
                break;
            }
        }
        if (!b) {
            sb.append(str);
            return;
        }
        for (int j = 0; j < str.length(); ++j) {
            final char char2 = str.charAt(j);
            if (char2 > '\u00ff') {
                sb.append("\\u");
                final int n = (char2 & '\uf000') >> 12;
                sb.append((n <= 9) ? ((char)(48 + n)) : ((char)(65 + n - 10)));
                final int n2 = (char2 & '\u0f00') >> 8;
                sb.append((n2 <= 9) ? ((char)(48 + n2)) : ((char)(65 + n2 - 10)));
                final int n3 = (char2 & '\u00f0') >> 4;
                sb.append((n3 <= 9) ? ((char)(48 + n3)) : ((char)(65 + n3 - 10)));
                final int n4 = char2 & '\u000f';
                sb.append((n4 <= 9) ? ((char)(48 + n4)) : ((char)(65 + n4 - 10)));
            }
            else {
                final String str2 = JSONUtils.JSON_CHAR_REPLACEMENTS[char2];
                if (str2 == null) {
                    sb.append(char2);
                }
                else {
                    sb.append(str2);
                }
            }
        }
    }
    
    static boolean fieldIsSerializable(final Field field, final boolean b) {
        final int modifiers = field.getModifiers();
        return (!b || Modifier.isPublic(modifiers)) && !Modifier.isTransient(modifiers) && !Modifier.isFinal(modifiers) && (modifiers & 0x1000) == 0x0 && isAccessibleOrMakeAccessible(field);
    }
    
    static boolean isBasicValueType(final Type type) {
        if (type instanceof Class) {
            return isBasicValueType((Class<?>)type);
        }
        return type instanceof ParameterizedType && isBasicValueType(((ParameterizedType)type).getRawType());
    }
    
    static Class<?> getRawType(final Type obj) {
        if (obj instanceof Class) {
            return (Class<?>)obj;
        }
        if (obj instanceof ParameterizedType) {
            return (Class<?>)((ParameterizedType)obj).getRawType();
        }
        throw new IllegalArgumentException("Illegal type: " + obj);
    }
    
    static {
        ID_SUFFIX = "]";
        ID_PREFIX = "[#";
        ID_KEY = "__ID";
        JSON_CHAR_REPLACEMENTS = new String[256];
        JSONUtils.isAccessibleMethodHandle = null;
        JSONUtils.isAccessibleMethod = null;
        JSONUtils.trySetAccessibleMethodHandle = null;
        JSONUtils.trySetAccessibleMethod = null;
        for (int i = 0; i < 256; ++i) {
            if (i == 32) {
                i = 127;
            }
            final int n = i >> 4;
            final char c = (n <= 9) ? ((char)(48 + n)) : ((char)(65 + n - 10));
            final int n2 = i & 0xF;
            JSONUtils.JSON_CHAR_REPLACEMENTS[i] = "\\u00" + Character.toString(c) + Character.toString((n2 <= 9) ? ((char)(48 + n2)) : ((char)(65 + n2 - 10)));
        }
        JSONUtils.JSON_CHAR_REPLACEMENTS[34] = "\\\"";
        JSONUtils.JSON_CHAR_REPLACEMENTS[92] = "\\\\";
        JSONUtils.JSON_CHAR_REPLACEMENTS[10] = "\\n";
        JSONUtils.JSON_CHAR_REPLACEMENTS[13] = "\\r";
        JSONUtils.JSON_CHAR_REPLACEMENTS[9] = "\\t";
        JSONUtils.JSON_CHAR_REPLACEMENTS[8] = "\\b";
        JSONUtils.JSON_CHAR_REPLACEMENTS[12] = "\\f";
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            JSONUtils.isAccessibleMethodHandle = lookup.findVirtual(AccessibleObject.class, "isAccessible", MethodType.methodType(Boolean.TYPE));
        }
        catch (NoSuchMethodException ex) {}
        catch (IllegalAccessException ex2) {}
        try {
            JSONUtils.isAccessibleMethod = AccessibleObject.class.getDeclaredMethod("isAccessible", Object.class);
        }
        catch (NoSuchMethodException ex3) {}
        catch (SecurityException ex4) {}
        try {
            JSONUtils.trySetAccessibleMethodHandle = lookup.findVirtual(AccessibleObject.class, "trySetAccessible", MethodType.methodType(Boolean.TYPE));
        }
        catch (NoSuchMethodException ex5) {}
        catch (IllegalAccessException ex6) {}
        try {
            JSONUtils.trySetAccessibleMethod = AccessibleObject.class.getDeclaredMethod("trySetAccessible", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex7) {}
        catch (SecurityException ex8) {}
        INDENT_LEVELS = new String[17];
        final StringBuilder sb = new StringBuilder();
        for (int j = 0; j < JSONUtils.INDENT_LEVELS.length; ++j) {
            JSONUtils.INDENT_LEVELS[j] = sb.toString();
            sb.append(' ');
        }
    }
    
    static boolean isBasicValueType(final Class<?> clazz) {
        return clazz == String.class || clazz == Integer.class || clazz == Integer.TYPE || clazz == Long.class || clazz == Long.TYPE || clazz == Short.class || clazz == Short.TYPE || clazz == Float.class || clazz == Float.TYPE || clazz == Double.class || clazz == Double.TYPE || clazz == Byte.class || clazz == Byte.TYPE || clazz == Character.class || clazz == Character.TYPE || clazz == Boolean.class || clazz == Boolean.TYPE || clazz.isEnum() || clazz == Class.class;
    }
    
    static void indent(final int n, final int n2, final StringBuilder sb) {
        final int b = JSONUtils.INDENT_LEVELS.length - 1;
        int min;
        for (int i = n * n2; i > 0; i -= min) {
            min = Math.min(i, b);
            sb.append(JSONUtils.INDENT_LEVELS[min]);
        }
    }
    
    static boolean isBasicValueType(final Object o) {
        return o == null || o instanceof String || o instanceof Integer || o instanceof Boolean || o instanceof Long || o instanceof Float || o instanceof Double || o instanceof Short || o instanceof Byte || o instanceof Character || o.getClass().isEnum() || o instanceof Class;
    }
    
    static boolean isAccessibleOrMakeAccessible(final AccessibleObject accessibleObject) {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (!atomicBoolean.get()) {
            if (JSONUtils.isAccessibleMethodHandle != null) {
                try {
                    atomicBoolean.set((boolean)JSONUtils.isAccessibleMethodHandle.invoke(accessibleObject));
                }
                catch (Throwable t) {}
            }
            else if (JSONUtils.isAccessibleMethod != null) {
                try {
                    atomicBoolean.set((boolean)JSONUtils.isAccessibleMethod.invoke(accessibleObject, new Object[0]));
                }
                catch (Throwable t2) {}
            }
        }
        if (!atomicBoolean.get()) {
            if (JSONUtils.trySetAccessibleMethodHandle != null) {
                try {
                    atomicBoolean.set((boolean)JSONUtils.trySetAccessibleMethodHandle.invoke(accessibleObject));
                }
                catch (Throwable t3) {}
            }
            else if (JSONUtils.trySetAccessibleMethod != null) {
                try {
                    atomicBoolean.set((boolean)JSONUtils.trySetAccessibleMethod.invoke(accessibleObject, new Object[0]));
                }
                catch (Throwable t4) {}
            }
            if (!atomicBoolean.get()) {
                try {
                    accessibleObject.setAccessible(true);
                    atomicBoolean.set(true);
                }
                catch (Throwable t5) {}
            }
            if (!atomicBoolean.get()) {
                AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        if (JSONUtils.trySetAccessibleMethodHandle != null) {
                            try {
                                atomicBoolean.set((boolean)JSONUtils.trySetAccessibleMethodHandle.invoke(accessibleObject));
                            }
                            catch (Throwable t) {}
                        }
                        else if (JSONUtils.trySetAccessibleMethod != null) {
                            try {
                                atomicBoolean.set((boolean)JSONUtils.trySetAccessibleMethod.invoke(accessibleObject, new Object[0]));
                            }
                            catch (Throwable t2) {}
                        }
                        if (!atomicBoolean.get()) {
                            try {
                                accessibleObject.setAccessible(true);
                                atomicBoolean.set(true);
                            }
                            catch (Throwable t3) {}
                        }
                        return null;
                    }
                });
            }
        }
        return atomicBoolean.get();
    }
    
    public static String escapeJSONString(final String s) {
        final StringBuilder sb = new StringBuilder(s.length() * 2);
        escapeJSONString(s, sb);
        return sb.toString();
    }
    
    static boolean isCollectionOrArray(final Object o) {
        final Class<?> class1 = o.getClass();
        return Collection.class.isAssignableFrom(class1) || class1.isArray();
    }
    
    private JSONUtils() {
    }
    
    static Object getFieldValue(final Object o, final Field field) throws IllegalArgumentException, IllegalAccessException {
        final Class<?> type = field.getType();
        if (type == Integer.TYPE) {
            return field.getInt(o);
        }
        if (type == Long.TYPE) {
            return field.getLong(o);
        }
        if (type == Short.TYPE) {
            return field.getShort(o);
        }
        if (type == Double.TYPE) {
            return field.getDouble(o);
        }
        if (type == Float.TYPE) {
            return field.getFloat(o);
        }
        if (type == Boolean.TYPE) {
            return field.getBoolean(o);
        }
        if (type == Byte.TYPE) {
            return field.getByte(o);
        }
        if (type == Character.TYPE) {
            return field.getChar(o);
        }
        if (type == Class.class) {
            return field.get(o);
        }
        return field.get(o);
    }
}
