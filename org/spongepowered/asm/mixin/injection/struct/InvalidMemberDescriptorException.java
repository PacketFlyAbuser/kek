// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.throwables.MixinException;

public class InvalidMemberDescriptorException extends MixinException
{
    public InvalidMemberDescriptorException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public InvalidMemberDescriptorException(final Throwable t) {
        super(t);
    }
    
    public InvalidMemberDescriptorException(final String s) {
        super(s);
    }
}
