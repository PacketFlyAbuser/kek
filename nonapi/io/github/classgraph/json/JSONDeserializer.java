// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.lang.reflect.Constructor;
import java.lang.reflect.Array;
import io.github.classgraph.ClassGraphException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Type;
import java.util.ArrayList;
import nonapi.io.github.classgraph.types.ParseException;

public class JSONDeserializer
{
    private static <T> T deserializeObject(final Class<T> clazz, final String s, final ClassFieldCache classFieldCache) throws IllegalArgumentException {
        Object json;
        try {
            json = JSONParser.parseJSON(s);
        }
        catch (ParseException cause) {
            throw new IllegalArgumentException("Could not parse JSON", cause);
        }
        Object instance;
        try {
            instance = classFieldCache.getDefaultConstructorForConcreteTypeOf(clazz).newInstance(new Object[0]);
        }
        catch (ReflectiveOperationException | SecurityException ex) {
            final Throwable cause2;
            throw new IllegalArgumentException("Could not construct object of type " + clazz.getName(), cause2);
        }
        final ArrayList<Runnable> list = new ArrayList<Runnable>();
        populateObjectFromJsonObject(instance, clazz, json, classFieldCache, getInitialIdToObjectMap(instance, json), list);
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
        return (T)instance;
    }
    
    private static Map<CharSequence, Object> getInitialIdToObjectMap(final Object o, final Object o2) {
        final HashMap<CharSequence, Object> hashMap = new HashMap<CharSequence, Object>();
        if (o2 instanceof JSONObject) {
            final JSONObject jsonObject = (JSONObject)o2;
            if (!jsonObject.items.isEmpty()) {
                final Map.Entry<String, Object> entry = jsonObject.items.get(0);
                if (entry.getKey().equals("__ID")) {
                    final CharSequence value = entry.getValue();
                    if (value == null || !CharSequence.class.isAssignableFrom(value.getClass())) {
                        hashMap.put(value, o);
                    }
                }
            }
        }
        return hashMap;
    }
    
    public static void deserializeToField(final Object o, final String key, final String s, final ClassFieldCache classFieldCache) throws IllegalArgumentException {
        if (o == null) {
            throw new IllegalArgumentException("Cannot deserialize to a field of a null object");
        }
        Object json;
        try {
            json = JSONParser.parseJSON(s);
        }
        catch (ParseException cause) {
            throw new IllegalArgumentException("Could not parse JSON", cause);
        }
        final JSONObject jsonObject = new JSONObject(1);
        jsonObject.items.add(new AbstractMap.SimpleEntry<String, Object>(key, json));
        final ArrayList<Runnable> list = new ArrayList<Runnable>();
        populateObjectFromJsonObject(o, o.getClass(), jsonObject, classFieldCache, new HashMap<CharSequence, Object>(), list);
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
    }
    
    private static void populateObjectFromJsonObject(final Object o, final Type type, final Object o2, final ClassFieldCache classFieldCache, final Map<CharSequence, Object> map, final List<Runnable> list) {
        if (o2 == null) {
            return;
        }
        final boolean b = o2 instanceof JSONObject;
        final boolean b2 = o2 instanceof JSONArray;
        if (!b2 && !b) {
            throw new IllegalArgumentException("Expected JSONObject or JSONArray, got " + o2.getClass().getSimpleName());
        }
        final JSONObject jsonObject = b ? ((JSONObject)o2) : null;
        final JSONArray jsonArray = b2 ? ((JSONArray)o2) : null;
        final Class<?> class1 = o.getClass();
        final boolean assignable = Map.class.isAssignableFrom(class1);
        final Map map2 = assignable ? ((Map)o) : null;
        final boolean assignable2 = Collection.class.isAssignableFrom(class1);
        final Collection collection = assignable2 ? ((Collection)o) : null;
        final boolean array = class1.isArray();
        final boolean b3 = !assignable && !assignable2 && !array;
        if ((assignable || b3) != b || (assignable2 || array) != b2) {
            throw new IllegalArgumentException("Wrong JSON type for class " + o.getClass().getName());
        }
        Type obj = type;
        if (type instanceof Class) {
            final Class clazz = (Class)type;
            if (Map.class.isAssignableFrom(clazz)) {
                if (!assignable) {
                    throw new IllegalArgumentException("Got an unexpected map type");
                }
                obj = clazz.getGenericSuperclass();
            }
            else if (Collection.class.isAssignableFrom(clazz)) {
                if (!assignable2) {
                    throw new IllegalArgumentException("Got an unexpected map type");
                }
                obj = clazz.getGenericSuperclass();
            }
        }
        TypeResolutions typeResolutions;
        Type type2;
        Class<Object> componentType;
        boolean b4;
        Type type3;
        if (obj instanceof Class) {
            typeResolutions = null;
            type2 = null;
            final Class clazz2 = (Class)obj;
            if (array) {
                componentType = (Class<Object>)clazz2.getComponentType();
                b4 = !componentType.isArray();
            }
            else {
                componentType = null;
                b4 = false;
            }
            type3 = null;
        }
        else {
            if (!(obj instanceof ParameterizedType)) {
                throw new IllegalArgumentException("Got illegal type: " + obj);
            }
            typeResolutions = new TypeResolutions((ParameterizedType)obj);
            final int length = typeResolutions.resolvedTypeArguments.length;
            if (assignable && length != 2) {
                throw new IllegalArgumentException("Wrong number of type arguments for Map: got " + length + "; expected 2");
            }
            if (assignable2 && length != 1) {
                throw new IllegalArgumentException("Wrong number of type arguments for Collection: got " + length + "; expected 1");
            }
            type2 = (assignable ? typeResolutions.resolvedTypeArguments[0] : null);
            type3 = (assignable ? typeResolutions.resolvedTypeArguments[1] : (assignable2 ? typeResolutions.resolvedTypeArguments[0] : null));
            b4 = false;
            componentType = null;
        }
        final Class<?> clazz3 = (type3 == null) ? null : JSONUtils.getRawType(type3);
        Constructor<?> constructorWithSizeHintForConcreteType;
        if (assignable || assignable2 || (b4 && !JSONUtils.isBasicValueType(componentType))) {
            constructorWithSizeHintForConcreteType = classFieldCache.getConstructorWithSizeHintForConcreteTypeOf(b4 ? componentType : clazz3);
            if (constructorWithSizeHintForConcreteType != null) {
                final Constructor<?> defaultConstructorForConcreteType = null;
            }
            else {
                final Constructor<?> defaultConstructorForConcreteType = classFieldCache.getDefaultConstructorForConcreteTypeOf(b4 ? componentType : clazz3);
            }
        }
        else {
            constructorWithSizeHintForConcreteType = null;
            final Constructor<?> defaultConstructorForConcreteType = null;
        }
        final ClassFields classFields = b3 ? classFieldCache.get(class1) : null;
        ArrayList<ObjectInstantiation> list2 = null;
        for (int n = (jsonObject != null) ? jsonObject.items.size() : ((jsonArray != null) ? jsonArray.items.size() : 0), i = 0; i < n; ++i) {
            String str;
            Object obj2;
            if (jsonObject != null) {
                final Map.Entry<String, Object> entry = jsonObject.items.get(i);
                str = entry.getKey();
                obj2 = entry.getValue();
            }
            else {
                if (jsonArray == null) {
                    throw ClassGraphException.newClassGraphException("This exception should not be thrown");
                }
                str = null;
                obj2 = jsonArray.items.get(i);
            }
            final boolean b5 = obj2 instanceof JSONObject;
            final boolean b6 = obj2 instanceof JSONArray;
            final JSONObject jsonObject2 = b5 ? ((JSONObject)obj2) : null;
            final JSONArray jsonArray2 = b6 ? ((JSONArray)obj2) : null;
            FieldTypeInfo fieldTypeInfo;
            if (classFields != null) {
                fieldTypeInfo = classFields.fieldNameToFieldTypeInfo.get(str);
                if (fieldTypeInfo == null) {
                    throw new IllegalArgumentException("Field " + class1.getName() + "." + str + " does not exist or is not accessible, non-final, and non-transient");
                }
            }
            else {
                fieldTypeInfo = null;
            }
            final Type obj3 = (fieldTypeInfo != null) ? fieldTypeInfo.getFullyResolvedFieldType(typeResolutions) : (array ? componentType : type3);
            Object o3;
            if (obj2 == null) {
                o3 = null;
            }
            else if (obj3 == Object.class) {
                if (b5) {
                    o3 = new HashMap();
                    if (list2 == null) {
                        list2 = new ArrayList<ObjectInstantiation>();
                    }
                    list2.add(new ObjectInstantiation(o3, ParameterizedTypeImpl.MAP_OF_UNKNOWN_TYPE, obj2));
                }
                else if (b6) {
                    o3 = new ArrayList();
                    if (list2 == null) {
                        list2 = new ArrayList<ObjectInstantiation>();
                    }
                    list2.add(new ObjectInstantiation(o3, ParameterizedTypeImpl.LIST_OF_UNKNOWN_TYPE, obj2));
                }
                else {
                    o3 = jsonBasicValueToObject(obj2, obj3, false);
                }
            }
            else if (JSONUtils.isBasicValueType(obj3)) {
                if (b5 || b6) {
                    throw new IllegalArgumentException("Got JSONObject or JSONArray type when expecting a simple value type");
                }
                o3 = jsonBasicValueToObject(obj2, obj3, false);
            }
            else if (CharSequence.class.isAssignableFrom(((JSONArray)obj2).getClass())) {
                final Object value = map.get(obj2);
                if (value == null) {
                    throw new IllegalArgumentException("Object id not found: " + obj2);
                }
                o3 = value;
            }
            else {
                if (!b5 && !b6) {
                    throw new IllegalArgumentException("Got simple value type when expecting a JSON object or JSON array");
                }
                try {
                    final int n2 = (jsonObject2 != null) ? jsonObject2.items.size() : ((jsonArray2 != null) ? jsonArray2.items.size() : 0);
                    if (obj3 instanceof Class && ((Class<Object>)obj3).isArray()) {
                        if (!b6) {
                            throw new IllegalArgumentException("Expected JSONArray, got " + ((JSONArray)obj2).getClass().getName());
                        }
                        o3 = Array.newInstance(((Class<Object>)obj3).getComponentType(), n2);
                    }
                    else if (assignable2 || assignable || b4) {
                        final Constructor<?> defaultConstructorForConcreteType;
                        o3 = ((constructorWithSizeHintForConcreteType != null) ? constructorWithSizeHintForConcreteType.newInstance(n2) : ((defaultConstructorForConcreteType != null) ? defaultConstructorForConcreteType.newInstance(new Object[0]) : null));
                    }
                    else if (fieldTypeInfo != null) {
                        final Constructor<?> constructorForFieldTypeWithSizeHint = fieldTypeInfo.getConstructorForFieldTypeWithSizeHint(obj3, classFieldCache);
                        if (constructorForFieldTypeWithSizeHint != null) {
                            o3 = constructorForFieldTypeWithSizeHint.newInstance(n2);
                        }
                        else {
                            o3 = fieldTypeInfo.getDefaultConstructorForFieldType(obj3, classFieldCache).newInstance(new Object[0]);
                        }
                    }
                    else {
                        if (!array || b4) {
                            throw new IllegalArgumentException("Got illegal type");
                        }
                        o3 = Array.newInstance(class1.getComponentType(), n2);
                    }
                }
                catch (ReflectiveOperationException | SecurityException ex) {
                    final Throwable cause;
                    throw new IllegalArgumentException("Could not instantiate type " + obj3, cause);
                }
                if (obj2 instanceof JSONObject) {
                    final JSONObject jsonObject3 = (JSONObject)obj2;
                    if (jsonObject3.objectId != null) {
                        map.put(jsonObject3.objectId, o3);
                    }
                }
                if (list2 == null) {
                    list2 = new ArrayList<ObjectInstantiation>();
                }
                list2.add(new ObjectInstantiation(o3, obj3, obj2));
            }
            if (fieldTypeInfo != null) {
                fieldTypeInfo.setFieldValue(o, o3);
            }
            else if (map2 != null) {
                map2.put(jsonBasicValueToObject(str, type2, true), o3);
            }
            else if (array) {
                Array.set(o, i, o3);
            }
            else if (collection != null) {
                list.add(new Runnable() {
                    @Override
                    public void run() {
                        collection.add(o3);
                    }
                });
            }
        }
        if (list2 != null) {
            for (final ObjectInstantiation objectInstantiation : list2) {
                populateObjectFromJsonObject(objectInstantiation.objectInstance, objectInstantiation.type, objectInstantiation.jsonVal, classFieldCache, map, list);
            }
        }
    }
    
    private static Object jsonBasicValueToObject(final Object obj, final Type obj2, final boolean b) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof JSONArray || obj instanceof JSONObject) {
            throw ClassGraphException.newClassGraphException("Expected a basic value type");
        }
        if (obj2 instanceof ParameterizedType) {
            if (((ParameterizedType)obj2).getRawType().getClass() == Class.class) {
                final String string = obj.toString();
                final int index = string.indexOf(60);
                final String substring = string.substring(0, (index < 0) ? string.length() : index);
                try {
                    return Class.forName(substring);
                }
                catch (ClassNotFoundException cause) {
                    throw new IllegalArgumentException("Could not deserialize class reference " + obj, cause);
                }
            }
            throw new IllegalArgumentException("Got illegal ParameterizedType: " + obj2);
        }
        if (!(obj2 instanceof Class)) {
            throw new IllegalArgumentException("Got illegal basic value type: " + obj2);
        }
        final Class enumType = (Class)obj2;
        if (enumType == String.class) {
            if (!(obj instanceof CharSequence)) {
                throw new IllegalArgumentException("Expected string; got " + obj.getClass().getName());
            }
            return obj.toString();
        }
        else if (enumType == CharSequence.class) {
            if (!(obj instanceof CharSequence)) {
                throw new IllegalArgumentException("Expected CharSequence; got " + obj.getClass().getName());
            }
            return obj;
        }
        else if (enumType == Integer.class || enumType == Integer.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Integer.parseInt(obj.toString());
            }
            if (!(obj instanceof Integer)) {
                throw new IllegalArgumentException("Expected integer; got " + obj.getClass().getName());
            }
            return obj;
        }
        else if (enumType == Long.class || enumType == Long.TYPE) {
            final boolean b2 = obj instanceof Long;
            final boolean b3 = obj instanceof Integer;
            if (b && obj instanceof CharSequence) {
                return b2 ? Long.parseLong(obj.toString()) : Integer.parseInt(obj.toString());
            }
            if (!b2 && !b3) {
                throw new IllegalArgumentException("Expected long; got " + obj.getClass().getName());
            }
            if (b2) {
                return obj;
            }
            return obj;
        }
        else if (enumType == Short.class || enumType == Short.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Short.parseShort(obj.toString());
            }
            if (!(obj instanceof Integer)) {
                throw new IllegalArgumentException("Expected short; got " + obj.getClass().getName());
            }
            final int intValue = (int)obj;
            if (intValue < -32768 || intValue > 32767) {
                throw new IllegalArgumentException("Expected short; got out-of-range value " + intValue);
            }
            return (short)intValue;
        }
        else if (enumType == Float.class || enumType == Float.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Float.parseFloat(obj.toString());
            }
            if (!(obj instanceof Double)) {
                throw new IllegalArgumentException("Expected float; got " + obj.getClass().getName());
            }
            final double doubleValue = (double)obj;
            if (doubleValue < -3.4028234663852886E38 || doubleValue > 3.4028234663852886E38) {
                throw new IllegalArgumentException("Expected float; got out-of-range value " + doubleValue);
            }
            return (float)doubleValue;
        }
        else if (enumType == Double.class || enumType == Double.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Double.parseDouble(obj.toString());
            }
            if (!(obj instanceof Double)) {
                throw new IllegalArgumentException("Expected double; got " + obj.getClass().getName());
            }
            return obj;
        }
        else if (enumType == Byte.class || enumType == Byte.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Byte.parseByte(obj.toString());
            }
            if (!(obj instanceof Integer)) {
                throw new IllegalArgumentException("Expected byte; got " + obj.getClass().getName());
            }
            final int intValue2 = (int)obj;
            if (intValue2 < -128 || intValue2 > 127) {
                throw new IllegalArgumentException("Expected byte; got out-of-range value " + intValue2);
            }
            return (byte)intValue2;
        }
        else if (enumType == Character.class || enumType == Character.TYPE) {
            if (!(obj instanceof CharSequence)) {
                throw new IllegalArgumentException("Expected character; got " + obj.getClass().getName());
            }
            final CharSequence charSequence = (CharSequence)obj;
            if (charSequence.length() != 1) {
                throw new IllegalArgumentException("Expected single character; got string");
            }
            return charSequence.charAt(0);
        }
        else if (enumType == Boolean.class || enumType == Boolean.TYPE) {
            if (b && obj instanceof CharSequence) {
                return Boolean.parseBoolean(obj.toString());
            }
            if (!(obj instanceof Boolean)) {
                throw new IllegalArgumentException("Expected boolean; got " + obj.getClass().getName());
            }
            return obj;
        }
        else if (Enum.class.isAssignableFrom(enumType)) {
            if (!(obj instanceof CharSequence)) {
                throw new IllegalArgumentException("Expected string for enum value; got " + obj.getClass().getName());
            }
            return Enum.valueOf((Class<Enum>)enumType, obj.toString());
        }
        else {
            if (JSONUtils.getRawType(obj2).isAssignableFrom(obj.getClass())) {
                return obj;
            }
            throw new IllegalArgumentException("Got type " + obj.getClass() + "; expected " + obj2);
        }
    }
    
    public static <T> T deserializeObject(final Class<T> clazz, final String s) throws IllegalArgumentException {
        return deserializeObject(clazz, s, new ClassFieldCache(true, false));
    }
    
    public static void deserializeToField(final Object o, final String s, final String s2) throws IllegalArgumentException {
        deserializeToField(o, s, s2, new ClassFieldCache(true, false));
    }
    
    private JSONDeserializer() {
    }
    
    private static class ObjectInstantiation
    {
        /* synthetic */ Object objectInstance;
        /* synthetic */ Type type;
        /* synthetic */ Object jsonVal;
        
        public ObjectInstantiation(final Object objectInstance, final Type type, final Object jsonVal) {
            this.jsonVal = jsonVal;
            this.objectInstance = objectInstance;
            this.type = type;
        }
    }
}
