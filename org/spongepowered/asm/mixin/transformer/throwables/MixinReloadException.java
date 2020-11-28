// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.throwables.MixinException;

public class MixinReloadException extends MixinException
{
    private final /* synthetic */ IMixinInfo mixinInfo;
    
    public IMixinInfo getMixinInfo() {
        return this.mixinInfo;
    }
    
    public MixinReloadException(final IMixinInfo mixinInfo, final String s) {
        super(s);
        this.mixinInfo = mixinInfo;
    }
}
