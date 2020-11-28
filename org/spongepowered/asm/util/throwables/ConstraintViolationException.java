// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util.throwables;

import org.spongepowered.asm.util.ConstraintParser;

public class ConstraintViolationException extends Exception
{
    private final /* synthetic */ ConstraintParser.Constraint constraint;
    private final /* synthetic */ String badValue;
    
    public ConstraintViolationException(final String message, final Throwable cause, final ConstraintParser.Constraint constraint) {
        super(message, cause);
        this.constraint = constraint;
        this.badValue = "UNRESOLVED";
    }
    
    public ConstraintViolationException(final String message, final Throwable cause, final ConstraintParser.Constraint constraint, final int i) {
        super(message, cause);
        this.constraint = constraint;
        this.badValue = String.valueOf(i);
    }
    
    public ConstraintViolationException(final String message, final ConstraintParser.Constraint constraint, final int i) {
        super(message);
        this.constraint = constraint;
        this.badValue = String.valueOf(i);
    }
    
    public String getBadValue() {
        return this.badValue;
    }
    
    public ConstraintViolationException(final Throwable cause, final ConstraintParser.Constraint constraint, final int i) {
        super(cause);
        this.constraint = constraint;
        this.badValue = String.valueOf(i);
    }
    
    public ConstraintViolationException(final String message, final ConstraintParser.Constraint constraint) {
        super(message);
        this.constraint = constraint;
        this.badValue = "UNRESOLVED";
    }
    
    public ConstraintViolationException(final Throwable cause, final ConstraintParser.Constraint constraint) {
        super(cause);
        this.constraint = constraint;
        this.badValue = "UNRESOLVED";
    }
    
    public ConstraintParser.Constraint getConstraint() {
        return this.constraint;
    }
    
    public ConstraintViolationException(final ConstraintParser.Constraint constraint, final int i) {
        this.constraint = constraint;
        this.badValue = String.valueOf(i);
    }
    
    static {
        MISSING_VALUE = "UNRESOLVED";
    }
    
    public ConstraintViolationException(final ConstraintParser.Constraint constraint) {
        this.constraint = constraint;
        this.badValue = "UNRESOLVED";
    }
}
