// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.throwables;

public class MixinTransformerError extends Error
{
    public MixinTransformerError(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MixinTransformerError(final String message) {
        super(message);
    }
    
    public MixinTransformerError(final Throwable cause) {
        super(cause);
    }
}
