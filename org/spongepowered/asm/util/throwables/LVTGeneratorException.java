// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util.throwables;

import org.spongepowered.asm.mixin.throwables.MixinException;

public class LVTGeneratorException extends MixinException
{
    public LVTGeneratorException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public LVTGeneratorException(final String s) {
        super(s);
    }
}
