// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.throwables;

public class InjectionError extends Error
{
    public InjectionError(final Throwable cause) {
        super(cause);
    }
    
    public InjectionError() {
    }
    
    public InjectionError(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InjectionError(final String message) {
        super(message);
    }
}
