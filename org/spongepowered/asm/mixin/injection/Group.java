// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD })
public @interface Group {
    int min() default -1;
    
    String name() default "";
    
    int max() default -1;
}
