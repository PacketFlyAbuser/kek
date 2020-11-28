// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.RUNTIME)
public @interface Constant {
    int ordinal() default -1;
    
    long longValue() default 0L;
    
    int intValue() default 0;
    
    double doubleValue() default 0.0;
    
    boolean nullValue() default false;
    
    Condition[] expandZeroConditions() default {};
    
    Class<?> classValue() default Object.class;
    
    boolean log() default false;
    
    float floatValue() default 0.0f;
    
    String stringValue() default "";
    
    String slice() default "";
    
    public enum Condition
    {
        LESS_THAN_ZERO(new int[] { 155, 156 }), 
        LESS_THAN_OR_EQUAL_TO_ZERO(new int[] { 158, 157 }), 
        GREATER_THAN_ZERO(Condition.LESS_THAN_OR_EQUAL_TO_ZERO);
        
        private final /* synthetic */ int[] opcodes;
        private final /* synthetic */ Condition equivalence;
        
        GREATER_THAN_OR_EQUAL_TO_ZERO(Condition.LESS_THAN_ZERO);
        
        public int[] getOpcodes() {
            return this.opcodes;
        }
        
        private Condition(final Condition condition, final int[] opcodes) {
            this.equivalence = ((condition != null) ? condition : this);
            this.opcodes = opcodes;
        }
        
        public Condition getEquivalentCondition() {
            return this.equivalence;
        }
        
        private Condition(final Condition condition) {
            this(condition, condition.opcodes);
        }
        
        private Condition(final int[] array) {
            this(null, array);
        }
    }
}
