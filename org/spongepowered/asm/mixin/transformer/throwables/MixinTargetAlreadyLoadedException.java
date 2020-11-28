// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinTargetAlreadyLoadedException extends InvalidMixinException
{
    private final /* synthetic */ String target;
    
    public MixinTargetAlreadyLoadedException(final IMixinInfo mixinInfo, final String s, final String target, final Throwable t) {
        super(mixinInfo, s, t);
        this.target = target;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public MixinTargetAlreadyLoadedException(final IMixinInfo mixinInfo, final String s, final String target) {
        super(mixinInfo, s);
        this.target = target;
    }
}
