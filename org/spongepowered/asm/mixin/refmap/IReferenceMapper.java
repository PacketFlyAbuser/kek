// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.refmap;

public interface IReferenceMapper
{
    void setContext(final String p0);
    
    String getStatus();
    
    boolean isDefault();
    
    String getResourceName();
    
    String remapWithContext(final String p0, final String p1, final String p2);
    
    String remap(final String p0, final String p1);
    
    String getContext();
}
