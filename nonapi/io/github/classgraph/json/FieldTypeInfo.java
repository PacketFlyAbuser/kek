// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Collection;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

class FieldTypeInfo
{
    private final /* synthetic */ boolean isTypeVariable;
    private final /* synthetic */ Type fieldTypePartiallyResolved;
    private final /* synthetic */ boolean hasUnresolvedTypeVariables;
    private /* synthetic */ Constructor<?> constructorForFieldTypeWithSizeHint;
    private final /* synthetic */ PrimitiveType primitiveType;
    private /* synthetic */ Constructor<?> defaultConstructorForFieldType;
    final /* synthetic */ Field field;
    
    @Override
    public String toString() {
        return this.fieldTypePartiallyResolved + " " + this.field.getDeclaringClass().getName() + "." + this.field.getDeclaringClass().getName();
    }
    
    public Constructor<?> getConstructorForFieldTypeWithSizeHint(final Type type, final ClassFieldCache classFieldCache) {
        if (!this.isTypeVariable) {
            return this.constructorForFieldTypeWithSizeHint;
        }
        final Class<?> rawType = JSONUtils.getRawType(type);
        if (!Collection.class.isAssignableFrom(rawType) && !Map.class.isAssignableFrom(rawType)) {
            return null;
        }
        return classFieldCache.getConstructorWithSizeHintForConcreteTypeOf(rawType);
    }
    
    void setFieldValue(final Object obj, final Object o) {
        try {
            if (o == null) {
                if (this.primitiveType != PrimitiveType.NON_PRIMITIVE) {
                    throw new IllegalArgumentException("Tried to set primitive-typed field " + this.field.getDeclaringClass().getName() + "." + this.field.getName() + " to null value");
                }
                this.field.set(obj, null);
            }
            else {
                switch (this.primitiveType) {
                    case NON_PRIMITIVE: {
                        this.field.set(obj, o);
                        break;
                    }
                    case CLASS_REF: {
                        if (!(o instanceof Class)) {
                            throw new IllegalArgumentException("Expected value of type Class<?>; got " + o.getClass().getName());
                        }
                        this.field.set(obj, o);
                        break;
                    }
                    case INTEGER: {
                        if (!(o instanceof Integer)) {
                            throw new IllegalArgumentException("Expected value of type Integer; got " + o.getClass().getName());
                        }
                        this.field.setInt(obj, (int)o);
                        break;
                    }
                    case LONG: {
                        if (!(o instanceof Long)) {
                            throw new IllegalArgumentException("Expected value of type Long; got " + o.getClass().getName());
                        }
                        this.field.setLong(obj, (long)o);
                        break;
                    }
                    case SHORT: {
                        if (!(o instanceof Short)) {
                            throw new IllegalArgumentException("Expected value of type Short; got " + o.getClass().getName());
                        }
                        this.field.setShort(obj, (short)o);
                        break;
                    }
                    case DOUBLE: {
                        if (!(o instanceof Double)) {
                            throw new IllegalArgumentException("Expected value of type Double; got " + o.getClass().getName());
                        }
                        this.field.setDouble(obj, (double)o);
                        break;
                    }
                    case FLOAT: {
                        if (!(o instanceof Float)) {
                            throw new IllegalArgumentException("Expected value of type Float; got " + o.getClass().getName());
                        }
                        this.field.setFloat(obj, (float)o);
                        break;
                    }
                    case BOOLEAN: {
                        if (!(o instanceof Boolean)) {
                            throw new IllegalArgumentException("Expected value of type Boolean; got " + o.getClass().getName());
                        }
                        this.field.setBoolean(obj, (boolean)o);
                        break;
                    }
                    case BYTE: {
                        if (!(o instanceof Byte)) {
                            throw new IllegalArgumentException("Expected value of type Byte; got " + o.getClass().getName());
                        }
                        this.field.setByte(obj, (byte)o);
                        break;
                    }
                    case CHARACTER: {
                        if (!(o instanceof Character)) {
                            throw new IllegalArgumentException("Expected value of type Character; got " + o.getClass().getName());
                        }
                        this.field.setChar(obj, (char)o);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        catch (IllegalArgumentException | IllegalAccessException ex) {
            final Object cause;
            throw new IllegalArgumentException("Could not set field " + this.field.getDeclaringClass().getName() + "." + this.field.getName(), (Throwable)cause);
        }
    }
    
    public Constructor<?> getDefaultConstructorForFieldType(final Type type, final ClassFieldCache classFieldCache) {
        if (!this.isTypeVariable) {
            return this.defaultConstructorForFieldType;
        }
        return classFieldCache.getDefaultConstructorForConcreteTypeOf(JSONUtils.getRawType(type));
    }
    
    public FieldTypeInfo(final Field field, final Type fieldTypePartiallyResolved, final ClassFieldCache classFieldCache) {
        this.field = field;
        this.fieldTypePartiallyResolved = fieldTypePartiallyResolved;
        this.isTypeVariable = (fieldTypePartiallyResolved instanceof TypeVariable);
        this.hasUnresolvedTypeVariables = (this.isTypeVariable || hasTypeVariables(fieldTypePartiallyResolved));
        if (fieldTypePartiallyResolved instanceof GenericArrayType || (fieldTypePartiallyResolved instanceof Class && ((Class)fieldTypePartiallyResolved).isArray()) || this.isTypeVariable) {
            this.primitiveType = PrimitiveType.NON_PRIMITIVE;
        }
        else {
            final Class<?> rawType = JSONUtils.getRawType(fieldTypePartiallyResolved);
            if (rawType == Integer.TYPE) {
                this.primitiveType = PrimitiveType.INTEGER;
            }
            else if (rawType == Long.TYPE) {
                this.primitiveType = PrimitiveType.LONG;
            }
            else if (rawType == Short.TYPE) {
                this.primitiveType = PrimitiveType.SHORT;
            }
            else if (rawType == Double.TYPE) {
                this.primitiveType = PrimitiveType.DOUBLE;
            }
            else if (rawType == Float.TYPE) {
                this.primitiveType = PrimitiveType.FLOAT;
            }
            else if (rawType == Boolean.TYPE) {
                this.primitiveType = PrimitiveType.BOOLEAN;
            }
            else if (rawType == Byte.TYPE) {
                this.primitiveType = PrimitiveType.BYTE;
            }
            else if (rawType == Character.TYPE) {
                this.primitiveType = PrimitiveType.CHARACTER;
            }
            else if (rawType == Class.class) {
                this.primitiveType = PrimitiveType.CLASS_REF;
            }
            else {
                this.primitiveType = PrimitiveType.NON_PRIMITIVE;
            }
            if (!JSONUtils.isBasicValueType(rawType)) {
                if (Collection.class.isAssignableFrom(rawType) || Map.class.isAssignableFrom(rawType)) {
                    this.constructorForFieldTypeWithSizeHint = classFieldCache.getConstructorWithSizeHintForConcreteTypeOf(rawType);
                }
                if (this.constructorForFieldTypeWithSizeHint == null) {
                    this.defaultConstructorForFieldType = classFieldCache.getDefaultConstructorForConcreteTypeOf(rawType);
                }
            }
        }
    }
    
    private static boolean hasTypeVariables(final Type type) {
        if (type instanceof TypeVariable || type instanceof GenericArrayType) {
            return true;
        }
        if (type instanceof ParameterizedType) {
            final Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
            for (int length = actualTypeArguments.length, i = 0; i < length; ++i) {
                if (hasTypeVariables(actualTypeArguments[i])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Type getFullyResolvedFieldType(final TypeResolutions typeResolutions) {
        if (!this.hasUnresolvedTypeVariables) {
            return this.fieldTypePartiallyResolved;
        }
        return typeResolutions.resolveTypeVariables(this.fieldTypePartiallyResolved);
    }
    
    private enum PrimitiveType
    {
        DOUBLE, 
        CHARACTER, 
        BYTE, 
        CLASS_REF, 
        SHORT, 
        INTEGER, 
        LONG, 
        FLOAT, 
        NON_PRIMITIVE, 
        BOOLEAN;
    }
}
