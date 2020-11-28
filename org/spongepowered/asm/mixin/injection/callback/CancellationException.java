// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.callback;

public class CancellationException extends RuntimeException
{
    public CancellationException(final Throwable cause) {
        super(cause);
    }
    
    public CancellationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CancellationException(final String message) {
        super(message);
    }
    
    public CancellationException() {
    }
}
