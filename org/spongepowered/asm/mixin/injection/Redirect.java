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
public @interface Redirect {
    boolean remap() default true;
    
    At at();
    
    int expect() default 1;
    
    Slice slice() default @Slice;
    
    String[] method();
    
    int allow() default -1;
    
    String constraints() default "";
    
    int require() default -1;
}
