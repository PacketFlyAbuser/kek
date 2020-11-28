// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.Type;

public class BasicValue implements Value
{
    private final /* synthetic */ Type type;
    public static final /* synthetic */ BasicValue UNINITIALIZED_VALUE;
    public static final /* synthetic */ BasicValue RETURNADDRESS_VALUE;
    public static final /* synthetic */ BasicValue REFERENCE_VALUE;
    
    @Override
    public int hashCode() {
        return (this.type == null) ? 0 : this.type.hashCode();
    }
    
    @Override
    public String toString() {
        if (this == BasicValue.UNINITIALIZED_VALUE) {
            return ".";
        }
        if (this == BasicValue.RETURNADDRESS_VALUE) {
            return "A";
        }
        if (this == BasicValue.REFERENCE_VALUE) {
            return "R";
        }
        return this.type.getDescriptor();
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BasicValue)) {
            return false;
        }
        if (this.type == null) {
            return ((BasicValue)o).type == null;
        }
        return this.type.equals(((BasicValue)o).type);
    }
    
    public boolean isReference() {
        return this.type != null && (this.type.getSort() == 10 || this.type.getSort() == 9);
    }
    
    public BasicValue(final Type type) {
        this.type = type;
    }
    
    static {
        UNINITIALIZED_VALUE = new BasicValue(null);
        INT_VALUE = new BasicValue(Type.INT_TYPE);
        FLOAT_VALUE = new BasicValue(Type.FLOAT_TYPE);
        LONG_VALUE = new BasicValue(Type.LONG_TYPE);
        DOUBLE_VALUE = new BasicValue(Type.DOUBLE_TYPE);
        REFERENCE_VALUE = new BasicValue(Type.getObjectType("java/lang/Object"));
        RETURNADDRESS_VALUE = new BasicValue(Type.VOID_TYPE);
    }
    
    public int getSize() {
        return (this.type == Type.LONG_TYPE || this.type == Type.DOUBLE_TYPE) ? 2 : 1;
    }
    
    public Type getType() {
        return this.type;
    }
}
