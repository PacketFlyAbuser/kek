// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.refmap;

import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;

public interface IMixinContext
{
    String getClassRef();
    
    Extensions getExtensions();
    
    boolean getOption(final MixinEnvironment.Option p0);
    
    String getTargetClassRef();
    
    IMixinInfo getMixin();
    
    int getPriority();
    
    Target getTargetMethod(final MethodNode p0);
    
    IReferenceMapper getReferenceMapper();
}
