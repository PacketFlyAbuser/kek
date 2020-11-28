// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface At {
    int opcode() default -1;
    
    int by() default 0;
    
    String[] args() default {};
    
    String value();
    
    boolean remap() default true;
    
    String id() default "";
    
    int ordinal() default -1;
    
    String target() default "";
    
    Shift shift() default Shift.NONE;
    
    String slice() default "";
    
    public enum Shift
    {
        BY, 
        NONE, 
        AFTER, 
        BEFORE;
    }
}
