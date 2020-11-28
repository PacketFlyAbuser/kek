// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.throwables;

public class MixinPrepareError extends Error
{
    public MixinPrepareError(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MixinPrepareError(final Throwable cause) {
        super(cause);
    }
    
    public MixinPrepareError(final String message) {
        super(message);
    }
}
