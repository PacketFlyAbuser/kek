// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service;

public interface IMixinServiceBootstrap
{
    String getServiceClassName();
    
    String getName();
    
    void bootstrap();
}
