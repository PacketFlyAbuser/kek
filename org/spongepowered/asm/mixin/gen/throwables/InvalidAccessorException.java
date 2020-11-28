// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.gen.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;

public class InvalidAccessorException extends InvalidMixinException
{
    private final /* synthetic */ AccessorInfo info;
    
    public InvalidAccessorException(final IMixinContext mixinContext, final String s) {
        super(mixinContext, s);
        this.info = null;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final String s, final Throwable t) {
        super(info.getContext(), s, t);
        this.info = info;
    }
    
    public InvalidAccessorException(final IMixinContext mixinContext, final String s, final Throwable t) {
        super(mixinContext, s, t);
        this.info = null;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final Throwable t) {
        super(info.getContext(), t);
        this.info = info;
    }
    
    public InvalidAccessorException(final AccessorInfo info, final String s) {
        super(info.getContext(), s);
        this.info = info;
    }
    
    public InvalidAccessorException(final IMixinContext mixinContext, final Throwable t) {
        super(mixinContext, t);
        this.info = null;
    }
    
    public AccessorInfo getAccessorInfo() {
        return this.info;
    }
}
