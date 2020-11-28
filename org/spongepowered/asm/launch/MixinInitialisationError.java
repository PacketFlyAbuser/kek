// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch;

public class MixinInitialisationError extends Error
{
    public MixinInitialisationError(final String message) {
        super(message);
    }
    
    public MixinInitialisationError() {
    }
    
    public MixinInitialisationError(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public MixinInitialisationError(final Throwable cause) {
        super(cause);
    }
}
