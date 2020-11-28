// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE })
public @interface Interface {
    Class<?> iface();
    
    boolean unique() default false;
    
    Remap remap() default Remap.ALL;
    
    String prefix();
    
    public enum Remap
    {
        NONE, 
        ONLY_PREFIXED, 
        ALL, 
        FORCE(true);
        
        private final /* synthetic */ boolean forceRemap;
        
        private Remap() {
            this(false);
        }
        
        public boolean forceRemap() {
            return this.forceRemap;
        }
        
        private Remap(final boolean forceRemap) {
            this.forceRemap = forceRemap;
        }
    }
}
