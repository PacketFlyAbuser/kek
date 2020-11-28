// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ModifyArgs {
    At at();
    
    int allow() default -1;
    
    Slice slice() default @Slice;
    
    int require() default -1;
    
    String constraints() default "";
    
    String[] method();
    
    int expect() default 1;
    
    boolean remap() default true;
}
