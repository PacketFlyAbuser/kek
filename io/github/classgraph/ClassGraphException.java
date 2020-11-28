// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

public class ClassGraphException extends IllegalArgumentException
{
    public static ClassGraphException newClassGraphException(final String s, final Throwable t) throws ClassGraphException {
        return new ClassGraphException(s, t);
    }
    
    private ClassGraphException(final String s) {
        super(s);
    }
    
    public static ClassGraphException newClassGraphException(final String s) {
        return new ClassGraphException(s);
    }
    
    private ClassGraphException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
