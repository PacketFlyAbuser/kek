// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke.arg;

public class ArgumentCountException extends IllegalArgumentException
{
    public ArgumentCountException(final int i, final int j, final String str) {
        super("Invalid number of arguments for setAll, received " + i + " but expected " + j + ": " + str);
    }
}
