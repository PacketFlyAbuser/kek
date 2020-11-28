// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Set;

public interface IMixinConfig
{
    String getName();
    
    boolean isRequired();
    
    int getPriority();
    
    String getMixinPackage();
    
    IMixinConfigPlugin getPlugin();
    
    Set<String> getTargets();
    
    MixinEnvironment getEnvironment();
}
