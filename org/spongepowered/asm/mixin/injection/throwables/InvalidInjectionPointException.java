// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

public class InvalidInjectionPointException extends InvalidInjectionException
{
    public InvalidInjectionPointException(final InjectionInfo injectionInfo, final String format, final Object... args) {
        super(injectionInfo, String.format(format, args));
    }
    
    public InvalidInjectionPointException(final InjectionInfo injectionInfo, final Throwable t, final String format, final Object... args) {
        super(injectionInfo, String.format(format, args), t);
    }
    
    public InvalidInjectionPointException(final IMixinContext mixinContext, final Throwable t, final String format, final Object... args) {
        super(mixinContext, String.format(format, args), t);
    }
    
    public InvalidInjectionPointException(final IMixinContext mixinContext, final String format, final Object... args) {
        super(mixinContext, String.format(format, args));
    }
}
