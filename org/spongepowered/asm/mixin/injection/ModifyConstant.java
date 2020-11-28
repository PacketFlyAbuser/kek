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
public @interface ModifyConstant {
    Constant[] constant() default {};
    
    boolean remap() default true;
    
    String[] method();
    
    int require() default -1;
    
    int allow() default -1;
    
    String constraints() default "";
    
    Slice[] slice() default {};
    
    int expect() default 1;
}
