// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke.arg;

public class ArgumentIndexOutOfBoundsException extends IndexOutOfBoundsException
{
    public ArgumentIndexOutOfBoundsException(final int i) {
        super("Argument index is out of bounds: " + i);
    }
}
