// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.json;

import java.util.Arrays;
import java.util.Objects;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;

class ParameterizedTypeImpl implements ParameterizedType
{
    private final /* synthetic */ Type ownerType;
    private final /* synthetic */ Class<?> rawType;
    private final /* synthetic */ Type[] actualTypeArguments;
    
    @Override
    public Class<?> getRawType() {
        return this.rawType;
    }
    
    @Override
    public Type getOwnerType() {
        return this.ownerType;
    }
    
    static {
        MAP_OF_UNKNOWN_TYPE = new ParameterizedTypeImpl(Map.class, new Type[] { Object.class, Object.class }, null);
        LIST_OF_UNKNOWN_TYPE = new ParameterizedTypeImpl(List.class, new Type[] { Object.class }, null);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParameterizedType)) {
            return false;
        }
        final ParameterizedType parameterizedType = (ParameterizedType)o;
        return Objects.equals(this.ownerType, parameterizedType.getOwnerType()) && Objects.equals(this.rawType, parameterizedType.getRawType()) && Arrays.equals(this.actualTypeArguments, parameterizedType.getActualTypeArguments());
    }
    
    ParameterizedTypeImpl(final Class<?> rawType, final Type[] actualTypeArguments, final Type type) {
        this.actualTypeArguments = actualTypeArguments;
        this.rawType = rawType;
        this.ownerType = ((type != null) ? type : rawType.getDeclaringClass());
        if (rawType.getTypeParameters().length != actualTypeArguments.length) {
            throw new IllegalArgumentException("Argument length mismatch");
        }
    }
    
    @Override
    public Type[] getActualTypeArguments() {
        return this.actualTypeArguments.clone();
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.actualTypeArguments) ^ Objects.hashCode(this.ownerType) ^ Objects.hashCode(this.rawType);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.ownerType == null) {
            sb.append(this.rawType.getName());
        }
        else {
            if (this.ownerType instanceof Class) {
                sb.append(((Class)this.ownerType).getName());
            }
            else {
                sb.append(this.ownerType.toString());
            }
            sb.append('$');
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl)this.ownerType).rawType.getName() + "$", ""));
            }
            else {
                sb.append(this.rawType.getSimpleName());
            }
        }
        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            sb.append('<');
            int n = 1;
            for (final Type type : this.actualTypeArguments) {
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append(", ");
                }
                sb.append(type.toString());
            }
            sb.append('>');
        }
        return sb.toString();
    }
}
