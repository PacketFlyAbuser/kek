// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ModifyVariable {
    At at();
    
    int index() default -1;
    
    boolean argsOnly() default false;
    
    String[] method();
    
    boolean remap() default true;
    
    int ordinal() default -1;
    
    boolean print() default false;
    
    int allow() default -1;
    
    int expect() default 1;
    
    String[] name() default {};
    
    int require() default -1;
    
    String constraints() default "";
    
    Slice slice() default @Slice;
}
