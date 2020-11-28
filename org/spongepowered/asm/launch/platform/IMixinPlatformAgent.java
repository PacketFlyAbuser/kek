// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch.platform;

public interface IMixinPlatformAgent
{
    void initPrimaryContainer();
    
    String getPhaseProvider();
    
    void inject();
    
    String getLaunchTarget();
    
    void prepare();
}
