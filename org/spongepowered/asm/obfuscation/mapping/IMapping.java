// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.obfuscation.mapping;

public interface IMapping<TMapping>
{
    TMapping copy();
    
    Type getType();
    
    String serialise();
    
    TMapping getSuper();
    
    String getOwner();
    
    String getDesc();
    
    TMapping transform(final String p0);
    
    String getName();
    
    String getSimpleName();
    
    TMapping remap(final String p0);
    
    TMapping move(final String p0);
    
    public enum Type
    {
        PACKAGE, 
        METHOD, 
        CLASS, 
        FIELD;
    }
}
