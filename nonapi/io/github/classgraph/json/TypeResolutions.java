// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import io.github.classgraph.ClassGraphException;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

class TypeResolutions
{
    private final /* synthetic */ TypeVariable<?>[] typeVariables;
    /* synthetic */ Type[] resolvedTypeArguments;
    
    Type resolveTypeVariables(final Type obj) {
        if (obj instanceof Class) {
            return obj;
        }
        if (obj instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)obj;
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] array = null;
            for (int i = 0; i < actualTypeArguments.length; ++i) {
                final Type resolveTypeVariables = this.resolveTypeVariables(actualTypeArguments[i]);
                if (array == null) {
                    if (!resolveTypeVariables.equals(actualTypeArguments[i])) {
                        array = new Type[actualTypeArguments.length];
                        System.arraycopy(actualTypeArguments, 0, array, 0, i);
                        array[i] = resolveTypeVariables;
                    }
                }
                else {
                    array[i] = resolveTypeVariables;
                }
            }
            if (array == null) {
                return obj;
            }
            return new ParameterizedTypeImpl((Class<?>)parameterizedType.getRawType(), array, parameterizedType.getOwnerType());
        }
        else {
            if (obj instanceof TypeVariable) {
                final TypeVariable typeVariable = (TypeVariable)obj;
                for (int j = 0; j < this.typeVariables.length; ++j) {
                    if (this.typeVariables[j].getName().equals(typeVariable.getName())) {
                        return this.resolvedTypeArguments[j];
                    }
                }
                return obj;
            }
            if (obj instanceof GenericArrayType) {
                int length = 0;
                Type genericComponentType;
                for (genericComponentType = obj; genericComponentType instanceof GenericArrayType; genericComponentType = ((GenericArrayType)genericComponentType).getGenericComponentType()) {
                    ++length;
                }
                final Type resolveTypeVariables2 = this.resolveTypeVariables(genericComponentType);
                if (!(resolveTypeVariables2 instanceof Class)) {
                    throw new IllegalArgumentException("Could not resolve generic array type " + obj);
                }
                return Array.newInstance((Class<?>)resolveTypeVariables2, (int[])Array.newInstance(Integer.TYPE, length)).getClass();
            }
            else {
                if (obj instanceof WildcardType) {
                    throw ClassGraphException.newClassGraphException("WildcardType not yet supported: " + obj);
                }
                throw ClassGraphException.newClassGraphException("Got unexpected type: " + obj);
            }
        }
    }
    
    TypeResolutions(final ParameterizedType parameterizedType) {
        this.typeVariables = (TypeVariable<?>[])((Class)parameterizedType.getRawType()).getTypeParameters();
        this.resolvedTypeArguments = parameterizedType.getActualTypeArguments();
        if (this.resolvedTypeArguments.length != this.typeVariables.length) {
            throw new IllegalArgumentException("Type parameter count mismatch");
        }
    }
    
    @Override
    public String toString() {
        if (this.typeVariables.length == 0) {
            return "{ }";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (int i = 0; i < this.typeVariables.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.typeVariables[i]).append(" => ").append(this.resolvedTypeArguments[i]);
        }
        sb.append(" }");
        return sb.toString();
    }
}
