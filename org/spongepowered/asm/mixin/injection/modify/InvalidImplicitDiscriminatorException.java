// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.modify;

import org.spongepowered.asm.mixin.throwables.MixinException;

public class InvalidImplicitDiscriminatorException extends MixinException
{
    public InvalidImplicitDiscriminatorException(final String s) {
        super(s);
    }
    
    public InvalidImplicitDiscriminatorException(final String s, final Throwable t) {
        super(s, t);
    }
}
