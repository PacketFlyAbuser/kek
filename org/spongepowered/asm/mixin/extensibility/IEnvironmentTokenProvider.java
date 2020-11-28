// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.extensibility;

import org.spongepowered.asm.mixin.MixinEnvironment;

public interface IEnvironmentTokenProvider
{
    Integer getToken(final String p0, final MixinEnvironment p1);
    
    int getPriority();
}
