// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.throwables;

public class MixinException extends RuntimeException
{
    public MixinException(final String message) {
        super(message);
    }
    
    public MixinException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MixinException() {
    }
    
    public MixinException(final Throwable cause) {
        super(cause);
    }
}
