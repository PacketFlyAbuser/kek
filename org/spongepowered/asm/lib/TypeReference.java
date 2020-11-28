// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib;

public class TypeReference
{
    private /* synthetic */ int value;
    
    public int getExceptionIndex() {
        return (this.value & 0xFFFF00) >> 8;
    }
    
    public static TypeReference newTryCatchReference(final int n) {
        return new TypeReference(0x42000000 | n << 8);
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getTypeParameterBoundIndex() {
        return (this.value & 0xFF00) >> 8;
    }
    
    public int getSuperTypeIndex() {
        return (short)((this.value & 0xFFFF00) >> 8);
    }
    
    static {
        METHOD_TYPE_PARAMETER_BOUND = 18;
        METHOD_INVOCATION_TYPE_ARGUMENT = 73;
        CAST = 71;
        FIELD = 19;
        METHOD_RETURN = 20;
        NEW = 68;
        METHOD_FORMAL_PARAMETER = 22;
        THROWS = 23;
        CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT = 72;
        INSTANCEOF = 67;
        LOCAL_VARIABLE = 64;
        METHOD_TYPE_PARAMETER = 1;
        CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT = 74;
        CLASS_EXTENDS = 16;
        EXCEPTION_PARAMETER = 66;
        RESOURCE_VARIABLE = 65;
        METHOD_REFERENCE = 70;
        METHOD_REFERENCE_TYPE_ARGUMENT = 75;
        CONSTRUCTOR_REFERENCE = 69;
        CLASS_TYPE_PARAMETER = 0;
        CLASS_TYPE_PARAMETER_BOUND = 17;
        METHOD_RECEIVER = 21;
    }
    
    public static TypeReference newSuperTypeReference(int n) {
        n &= 0xFFFF;
        return new TypeReference(0x10000000 | n << 8);
    }
    
    public int getTypeParameterIndex() {
        return (this.value & 0xFF0000) >> 16;
    }
    
    public int getTryCatchBlockIndex() {
        return (this.value & 0xFFFF00) >> 8;
    }
    
    public int getSort() {
        return this.value >>> 24;
    }
    
    public static TypeReference newTypeParameterReference(final int n, final int n2) {
        return new TypeReference(n << 24 | n2 << 16);
    }
    
    public static TypeReference newTypeParameterBoundReference(final int n, final int n2, final int n3) {
        return new TypeReference(n << 24 | n2 << 16 | n3 << 8);
    }
    
    public int getTypeArgumentIndex() {
        return this.value & 0xFF;
    }
    
    public static TypeReference newExceptionReference(final int n) {
        return new TypeReference(0x17000000 | n << 8);
    }
    
    public int getFormalParameterIndex() {
        return (this.value & 0xFF0000) >> 16;
    }
    
    public static TypeReference newTypeArgumentReference(final int n, final int n2) {
        return new TypeReference(n << 24 | n2);
    }
    
    public static TypeReference newTypeReference(final int n) {
        return new TypeReference(n << 24);
    }
    
    public TypeReference(final int value) {
        this.value = value;
    }
    
    public static TypeReference newFormalParameterReference(final int n) {
        return new TypeReference(0x16000000 | n << 16);
    }
}
