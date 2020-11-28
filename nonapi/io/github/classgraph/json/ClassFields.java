// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import io.github.classgraph.ScanResult;
import java.lang.reflect.Type;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.lang.reflect.Field;
import java.util.Comparator;

class ClassFields
{
    private static final /* synthetic */ String SERIALIZATION_FORMAT_CLASS_NAME;
    private static final /* synthetic */ Comparator<Field> SERIALIZATION_FORMAT_FIELD_NAME_ORDER_COMPARATOR;
    final /* synthetic */ List<FieldTypeInfo> fieldOrder;
    /* synthetic */ Field idField;
    final /* synthetic */ Map<String, FieldTypeInfo> fieldNameToFieldTypeInfo;
    private static final /* synthetic */ Comparator<Field> FIELD_NAME_ORDER_COMPARATOR;
    
    public ClassFields(final Class<?> clazz, final boolean b, final boolean b2, final ClassFieldCache classFieldCache) {
        this.fieldOrder = new ArrayList<FieldTypeInfo>();
        this.fieldNameToFieldTypeInfo = new HashMap<String, FieldTypeInfo>();
        final HashSet<String> set = new HashSet<String>();
        final ArrayList<ArrayList<FieldTypeInfo>> list = new ArrayList<ArrayList<FieldTypeInfo>>();
        TypeResolutions typeResolutions = null;
        Object obj = clazz;
        while (obj != Object.class && obj != null) {
            Class<Object> clazz2;
            if (obj instanceof ParameterizedType) {
                clazz2 = (Class<Object>)((ParameterizedType)obj).getRawType();
            }
            else {
                if (!(obj instanceof Class)) {
                    throw new IllegalArgumentException("Illegal class type: " + obj);
                }
                clazz2 = (Class<Object>)obj;
            }
            final Field[] declaredFields = clazz2.getDeclaredFields();
            Arrays.sort(declaredFields, clazz.getName().equals(ClassFields.SERIALIZATION_FORMAT_CLASS_NAME) ? ClassFields.SERIALIZATION_FORMAT_FIELD_NAME_ORDER_COMPARATOR : ClassFields.FIELD_NAME_ORDER_COMPARATOR);
            final ArrayList<FieldTypeInfo> list2 = new ArrayList<FieldTypeInfo>();
            for (final Field idField : declaredFields) {
                if (set.add(idField.getName())) {
                    final boolean annotationPresent = idField.isAnnotationPresent(Id.class);
                    if (annotationPresent) {
                        if (this.idField != null) {
                            throw new IllegalArgumentException("More than one @Id annotation: " + this.idField.getDeclaringClass() + "." + this.idField + " ; " + clazz2.getName() + "." + idField.getName());
                        }
                        this.idField = idField;
                    }
                    if (JSONUtils.fieldIsSerializable(idField, b2)) {
                        final Type genericType = idField.getGenericType();
                        final FieldTypeInfo fieldTypeInfo = new FieldTypeInfo(idField, (typeResolutions != null && b) ? typeResolutions.resolveTypeVariables(genericType) : genericType, classFieldCache);
                        this.fieldNameToFieldTypeInfo.put(idField.getName(), fieldTypeInfo);
                        list2.add(fieldTypeInfo);
                    }
                    else if (annotationPresent) {
                        throw new IllegalArgumentException("@Id annotation field must be accessible, final, and non-transient: " + clazz2.getName() + "." + idField.getName());
                    }
                }
            }
            list.add(list2);
            final Type genericSuperclass = clazz2.getGenericSuperclass();
            if (b) {
                if (genericSuperclass instanceof ParameterizedType) {
                    final Type type = (typeResolutions == null) ? genericSuperclass : typeResolutions.resolveTypeVariables(genericSuperclass);
                    typeResolutions = ((type instanceof ParameterizedType) ? new TypeResolutions((ParameterizedType)type) : null);
                    obj = type;
                }
                else {
                    if (!(genericSuperclass instanceof Class)) {
                        throw new IllegalArgumentException("Got unexpected supertype " + genericSuperclass);
                    }
                    obj = genericSuperclass;
                    typeResolutions = null;
                }
            }
            else {
                obj = genericSuperclass;
            }
        }
        for (int j = list.size() - 1; j >= 0; --j) {
            this.fieldOrder.addAll((Collection<? extends FieldTypeInfo>)list.get(j));
        }
    }
    
    static {
        FIELD_NAME_ORDER_COMPARATOR = new Comparator<Field>() {
            @Override
            public int compare(final Field field, final Field field2) {
                return field.getName().compareTo(field2.getName());
            }
        };
        SERIALIZATION_FORMAT_FIELD_NAME_ORDER_COMPARATOR = new Comparator<Field>() {
            @Override
            public int compare(final Field field, final Field field2) {
                return field.getName().equals("format") ? -1 : (field2.getName().equals("format") ? 1 : field.getName().compareTo(field2.getName()));
            }
        };
        SERIALIZATION_FORMAT_CLASS_NAME = ScanResult.class.getName() + "$SerializationFormat";
    }
}
