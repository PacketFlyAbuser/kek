// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Inject {
    At[] at();
    
    boolean remap() default true;
    
    int allow() default -1;
    
    String id() default "";
    
    String[] method();
    
    LocalCapture locals() default LocalCapture.NO_CAPTURE;
    
    boolean cancellable() default false;
    
    String constraints() default "";
    
    Slice[] slice() default {};
    
    int expect() default 1;
    
    int require() default -1;
}
