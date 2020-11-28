// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.Iterator;
import io.github.classgraph.ClassGraphException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.Comparator;

public final class JSONSerializer
{
    private static final /* synthetic */ Comparator<Object> SET_COMPARATOR;
    
    static {
        SET_COMPARATOR = new Comparator<Object>() {
            @Override
            public int compare(final Object o, final Object o2) {
                if (o == null || o2 == null) {
                    return ((o != null) - (o2 != null)) ? 1 : 0;
                }
                if (Comparable.class.isAssignableFrom(o.getClass()) && Comparable.class.isAssignableFrom(o2.getClass())) {
                    return ((Comparable)o).compareTo(o2);
                }
                return o.toString().compareTo(o2.toString());
            }
        };
    }
    
    static void jsonValToJSONString(final Object o, final Map<ReferenceEqualityKey<JSONReference>, CharSequence> map, final boolean b, final int n, final int n2, final StringBuilder sb) {
        if (o == null) {
            sb.append("null");
        }
        else if (o instanceof JSONObject) {
            ((JSONObject)o).toJSONString(map, b, n, n2, sb);
        }
        else if (o instanceof JSONArray) {
            ((JSONArray)o).toJSONString(map, b, n, n2, sb);
        }
        else if (o instanceof JSONReference) {
            jsonValToJSONString(map.get(new ReferenceEqualityKey(o)), map, b, n, n2, sb);
        }
        else if (o instanceof CharSequence || o instanceof Character || o.getClass().isEnum()) {
            sb.append('\"');
            JSONUtils.escapeJSONString(o.toString(), sb);
            sb.append('\"');
        }
        else {
            sb.append(o.toString());
        }
    }
    
    private static void assignObjectIds(final Object o, final Map<ReferenceEqualityKey<Object>, JSONObject> map, final ClassFieldCache classFieldCache, final Map<ReferenceEqualityKey<JSONReference>, CharSequence> map2, final AtomicInteger atomicInteger, final boolean b) {
        if (o instanceof JSONObject) {
            final Iterator<Map.Entry<String, Object>> iterator = ((JSONObject)o).items.iterator();
            while (iterator.hasNext()) {
                assignObjectIds(iterator.next().getValue(), map, classFieldCache, map2, atomicInteger, b);
            }
        }
        else if (o instanceof JSONArray) {
            final Iterator<Object> iterator2 = ((JSONArray)o).items.iterator();
            while (iterator2.hasNext()) {
                assignObjectIds(iterator2.next(), map, classFieldCache, map2, atomicInteger, b);
            }
        }
        else if (o instanceof JSONReference) {
            final Object idObject = ((JSONReference)o).idObject;
            if (idObject == null) {
                throw ClassGraphException.newClassGraphException("Internal inconsistency");
            }
            final JSONObject jsonObject = map.get(new ReferenceEqualityKey(idObject));
            if (jsonObject == null) {
                throw ClassGraphException.newClassGraphException("Internal inconsistency");
            }
            final Field idField = classFieldCache.get(idObject.getClass()).idField;
            CharSequence charSequence = null;
            if (idField != null) {
                try {
                    final Object value = idField.get(idObject);
                    if (value != null) {
                        charSequence = value.toString();
                        jsonObject.objectId = charSequence;
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException ex) {
                    final Throwable cause;
                    throw new IllegalArgumentException("Could not access @Id-annotated field " + idField, cause);
                }
            }
            if (charSequence == null) {
                if (jsonObject.objectId == null) {
                    charSequence = "[#" + atomicInteger.getAndIncrement() + "]";
                    jsonObject.objectId = charSequence;
                }
                else {
                    charSequence = jsonObject.objectId;
                }
            }
            map2.put(new ReferenceEqualityKey<JSONReference>((JSONReference)o), charSequence);
        }
    }
    
    private JSONSerializer() {
    }
    
    public static String serializeFromField(final Object o, final String str, final int n, final boolean b, final ClassFieldCache classFieldCache) {
        final FieldTypeInfo fieldTypeInfo = classFieldCache.get(o.getClass()).fieldNameToFieldTypeInfo.get(str);
        if (fieldTypeInfo == null) {
            throw new IllegalArgumentException("Class " + o.getClass().getName() + " does not have a field named \"" + str + "\"");
        }
        final Field field = fieldTypeInfo.field;
        if (!JSONUtils.fieldIsSerializable(field, false)) {
            throw new IllegalArgumentException("Field " + o.getClass().getName() + "." + str + " needs to be accessible, non-transient, and non-final");
        }
        Object fieldValue;
        try {
            fieldValue = JSONUtils.getFieldValue(o, field);
        }
        catch (IllegalAccessException cause) {
            throw new IllegalArgumentException("Could get value of field " + str, cause);
        }
        return serializeObject(fieldValue, n, b, classFieldCache);
    }
    
    public static String serializeFromField(final Object o, final String s, final int n, final boolean b) {
        return serializeFromField(o, s, n, b, new ClassFieldCache(false, b));
    }
    
    public static String serializeObject(final Object o) {
        return serializeObject(o, 0, false);
    }
    
    public static String serializeObject(final Object o, final int n, final boolean b) {
        return serializeObject(o, n, b, new ClassFieldCache(false, false));
    }
    
    public static String serializeObject(final Object o, final int n, final boolean b, final ClassFieldCache classFieldCache) {
        final HashMap<ReferenceEqualityKey<Object>, JSONObject> hashMap = new HashMap<ReferenceEqualityKey<Object>, JSONObject>();
        final Object jsonGraph = toJSONGraph(o, new HashSet<ReferenceEqualityKey<Object>>(), new HashSet<ReferenceEqualityKey<Object>>(), classFieldCache, hashMap, b);
        final HashMap<ReferenceEqualityKey<JSONReference>, CharSequence> hashMap2 = new HashMap<ReferenceEqualityKey<JSONReference>, CharSequence>();
        assignObjectIds(jsonGraph, hashMap, classFieldCache, hashMap2, new AtomicInteger(0), b);
        final StringBuilder sb = new StringBuilder(32768);
        jsonValToJSONString(jsonGraph, hashMap2, false, 0, n, sb);
        return sb.toString();
    }
    
    private static Object toJSONGraph(final Object o, final Set<ReferenceEqualityKey<Object>> set, final Set<ReferenceEqualityKey<Object>> set2, final ClassFieldCache classFieldCache, final Map<ReferenceEqualityKey<Object>, JSONObject> map, final boolean b) {
        if (o instanceof Class) {
            return ((Class)o).getName();
        }
        if (JSONUtils.isBasicValueType(o)) {
            return o;
        }
        final ReferenceEqualityKey<Object> referenceEqualityKey = new ReferenceEqualityKey<Object>(o);
        if (set.add(referenceEqualityKey)) {
            final Class<?> class1 = o.getClass();
            final boolean array = class1.isArray();
            Object o2;
            if (Map.class.isAssignableFrom(class1)) {
                final Map map2 = (Map)o;
                final ArrayList list = new ArrayList<Comparable>(map2.keySet());
                final int size = list.size();
                boolean b2 = false;
                Object value = null;
                for (int index = 0; index < size && value == null; value = list.get(index), ++index) {}
                if (value != null && Comparable.class.isAssignableFrom(value.getClass())) {
                    CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
                    b2 = true;
                }
                final String[] a = new String[size];
                for (int i = 0; i < size; ++i) {
                    final Comparable value2 = list.get(i);
                    if (value2 != null && !JSONUtils.isBasicValueType(value2)) {
                        throw new IllegalArgumentException("Map key of type " + value2.getClass().getName() + " is not a basic type (String, Integer, etc.), so can't be easily serialized as a JSON associative array key");
                    }
                    a[i] = JSONUtils.escapeJSONString((value2 == null) ? "null" : value2.toString());
                }
                if (!b2) {
                    Arrays.sort(a);
                }
                final Object[] array2 = new Object[size];
                for (int j = 0; j < size; ++j) {
                    array2[j] = map2.get(list.get(j));
                }
                convertVals(array2, set, set2, classFieldCache, map, b);
                final ArrayList list2 = new ArrayList<Map.Entry<String, Object>>(size);
                for (int k = 0; k < size; ++k) {
                    list2.add(new AbstractMap.SimpleEntry<String, Object>(a[k], array2[k]));
                }
                o2 = new JSONObject((List<Map.Entry<String, Object>>)list2);
            }
            else if (array || List.class.isAssignableFrom(class1)) {
                final List list3 = List.class.isAssignableFrom(class1) ? ((List)o) : null;
                final int n = (list3 != null) ? list3.size() : (array ? Array.getLength(o) : 0);
                final Object[] a2 = new Object[n];
                for (int l = 0; l < n; ++l) {
                    a2[l] = ((list3 != null) ? list3.get(l) : (array ? Array.get(o, l) : Integer.valueOf(0)));
                }
                convertVals(a2, set, set2, classFieldCache, map, b);
                o2 = new JSONArray(Arrays.asList(a2));
            }
            else if (Collection.class.isAssignableFrom(class1)) {
                final ArrayList<Object> list4 = new ArrayList<Object>((Collection<?>)o);
                if (Set.class.isAssignableFrom(class1)) {
                    CollectionUtils.sortIfNotEmpty(list4, JSONSerializer.SET_COMPARATOR);
                }
                final Object[] array3 = list4.toArray();
                convertVals(array3, set, set2, classFieldCache, map, b);
                o2 = new JSONArray(Arrays.asList(array3));
            }
            else {
                try {
                    final List<FieldTypeInfo> fieldOrder = classFieldCache.get(class1).fieldOrder;
                    final int size2 = fieldOrder.size();
                    final String[] array4 = new String[size2];
                    final Object[] array5 = new Object[size2];
                    for (int n2 = 0; n2 < size2; ++n2) {
                        final Field field = fieldOrder.get(n2).field;
                        array4[n2] = field.getName();
                        array5[n2] = JSONUtils.getFieldValue(o, field);
                    }
                    convertVals(array5, set, set2, classFieldCache, map, b);
                    final ArrayList list5 = new ArrayList<Map.Entry<String, Object>>(size2);
                    for (int n3 = 0; n3 < size2; ++n3) {
                        list5.add(new AbstractMap.SimpleEntry<String, Object>(array4[n3], array5[n3]));
                    }
                    o2 = new JSONObject((List<Map.Entry<String, Object>>)list5);
                }
                catch (IllegalArgumentException | IllegalAccessException ex) {
                    final Object o3;
                    throw ClassGraphException.newClassGraphException("Could not get value of field in object: " + o, (Throwable)o3);
                }
            }
            set.remove(referenceEqualityKey);
            return o2;
        }
        if (JSONUtils.isCollectionOrArray(o)) {
            throw new IllegalArgumentException("Cycles involving collections cannot be serialized, since collections are not assigned object ids. Reached cycle at: " + o);
        }
        return new JSONReference(o);
    }
    
    private static void convertVals(final Object[] array, final Set<ReferenceEqualityKey<Object>> set, final Set<ReferenceEqualityKey<Object>> set2, final ClassFieldCache classFieldCache, final Map<ReferenceEqualityKey<Object>, JSONObject> map, final boolean b) {
        final ReferenceEqualityKey[] array2 = new ReferenceEqualityKey[array.length];
        final boolean[] array3 = new boolean[array.length];
        for (int i = 0; i < array.length; ++i) {
            final Object o = array[i];
            array3[i] = !JSONUtils.isBasicValueType(o);
            if (array3[i] && !JSONUtils.isCollectionOrArray(o)) {
                final ReferenceEqualityKey referenceEqualityKey = new ReferenceEqualityKey<Object>(o);
                array2[i] = referenceEqualityKey;
                if (!set2.add((ReferenceEqualityKey<Object>)referenceEqualityKey)) {
                    array[i] = new JSONReference(o);
                    array3[i] = false;
                }
            }
            if (o instanceof Class) {
                array[i] = ((Class)o).getName();
            }
        }
        for (int j = 0; j < array.length; ++j) {
            if (array3[j]) {
                final Object o2 = array[j];
                array[j] = toJSONGraph(o2, set, set2, classFieldCache, map, b);
                if (!JSONUtils.isCollectionOrArray(o2)) {
                    map.put(array2[j], (JSONObject)array[j]);
                }
            }
        }
    }
}
