// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class InvalidInterfaceMixinException extends InvalidMixinException
{
    public InvalidInterfaceMixinException(final IMixinInfo mixinInfo, final String s) {
        super(mixinInfo, s);
    }
    
    public InvalidInterfaceMixinException(final IMixinInfo mixinInfo, final String s, final Throwable t) {
        super(mixinInfo, s, t);
    }
    
    public InvalidInterfaceMixinException(final IMixinInfo mixinInfo, final Throwable t) {
        super(mixinInfo, t);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext mixinContext, final Throwable t) {
        super(mixinContext, t);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext mixinContext, final String s, final Throwable t) {
        super(mixinContext, s, t);
    }
    
    public InvalidInterfaceMixinException(final IMixinContext mixinContext, final String s) {
        super(mixinContext, s);
    }
}
