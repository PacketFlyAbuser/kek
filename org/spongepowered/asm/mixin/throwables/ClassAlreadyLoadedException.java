// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.throwables;

public class ClassAlreadyLoadedException extends MixinException
{
    public ClassAlreadyLoadedException(final String s) {
        super(s);
    }
    
    public ClassAlreadyLoadedException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ClassAlreadyLoadedException(final Throwable t) {
        super(t);
    }
}
