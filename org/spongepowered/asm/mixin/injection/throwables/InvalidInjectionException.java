// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;

public class InvalidInjectionException extends InvalidMixinException
{
    private final /* synthetic */ InjectionInfo info;
    
    public InvalidInjectionException(final IMixinContext mixinContext, final String s) {
        super(mixinContext, s);
        this.info = null;
    }
    
    public InvalidInjectionException(final InjectionInfo info, final String s, final Throwable t) {
        super(info.getContext(), s, t);
        this.info = info;
    }
    
    public InvalidInjectionException(final InjectionInfo info, final String s) {
        super(info.getContext(), s);
        this.info = info;
    }
    
    public InvalidInjectionException(final InjectionInfo info, final Throwable t) {
        super(info.getContext(), t);
        this.info = info;
    }
    
    public InvalidInjectionException(final IMixinContext mixinContext, final Throwable t) {
        super(mixinContext, t);
        this.info = null;
    }
    
    public InjectionInfo getInjectionInfo() {
        return this.info;
    }
    
    public InvalidInjectionException(final IMixinContext mixinContext, final String s, final Throwable t) {
        super(mixinContext, s, t);
        this.info = null;
    }
}
