// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.List;
import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IMixinInfo
{
    MixinEnvironment.Phase getPhase();
    
    int getPriority();
    
    String getName();
    
    List<String> getTargetClasses();
    
    boolean isDetachedSuper();
    
    String getClassName();
    
    byte[] getClassBytes();
    
    String getClassRef();
    
    ClassNode getClassNode(final int p0);
    
    IMixinConfig getConfig();
}
