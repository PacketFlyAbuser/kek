// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util.throwables;

public class InvalidConstraintException extends IllegalArgumentException
{
    public InvalidConstraintException() {
    }
    
    public InvalidConstraintException(final Throwable cause) {
        super(cause);
    }
    
    public InvalidConstraintException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InvalidConstraintException(final String s) {
        super(s);
    }
}
