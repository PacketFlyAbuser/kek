// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public final class ConstraintParser
{
    public static Constraint parse(final AnnotationNode annotationNode) {
        return parse(Annotations.getValue(annotationNode, "constraints", ""));
    }
    
    public static Constraint parse(final String s) {
        if (s == null || s.length() == 0) {
            return Constraint.NONE;
        }
        final String[] split = s.replaceAll("\\s", "").toUpperCase().split(";");
        Constraint constraint = null;
        final String[] array = split;
        for (int length = array.length, i = 0; i < length; ++i) {
            final Constraint constraint2 = new Constraint(array[i]);
            if (constraint == null) {
                constraint = constraint2;
            }
            else {
                constraint.append(constraint2);
            }
        }
        return (constraint != null) ? constraint : Constraint.NONE;
    }
    
    private ConstraintParser() {
    }
    
    public static class Constraint
    {
        private /* synthetic */ String[] constraint;
        private /* synthetic */ String token;
        private /* synthetic */ int max;
        private final /* synthetic */ String expr;
        private /* synthetic */ int min;
        private static final /* synthetic */ Pattern pattern;
        private /* synthetic */ Constraint next;
        public static final /* synthetic */ Constraint NONE;
        
        public void check(final ITokenProvider obj) throws ConstraintViolationException {
            if (this != Constraint.NONE) {
                final Integer token = obj.getToken(this.token);
                if (token == null) {
                    throw new ConstraintViolationException("The token '" + this.token + "' could not be resolved in " + obj, this);
                }
                if (token < this.min) {
                    throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + token + ") which is less than the minimum value " + this.min + " in " + obj, this, token);
                }
                if (token > this.max) {
                    throw new ConstraintViolationException("Token '" + this.token + "' has a value (" + token + ") which is greater than the maximum value " + this.max + " in " + obj, this, token);
                }
            }
            if (this.next != null) {
                this.next.check(obj);
            }
        }
        
        private void parse() {
            if (!this.has(1)) {
                return;
            }
            final int val = this.val(1);
            this.min = val;
            this.max = val;
            final boolean has = this.has(0);
            if (this.has(4)) {
                if (has) {
                    throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
                }
                this.max = this.val(4);
                if (this.max < this.min) {
                    throw new InvalidConstraintException("Invalid range specified '" + this.max + "' is less than " + this.min + " in " + this.expr);
                }
            }
            else {
                if (!this.has(6)) {
                    if (has) {
                        if (this.has(3)) {
                            throw new InvalidConstraintException("Unexpected trailing modifier '" + this.elem(3) + "' in " + this.expr);
                        }
                        final String elem = this.elem(0);
                        if (">".equals(elem)) {
                            ++this.min;
                            this.max = Integer.MAX_VALUE;
                        }
                        else if (">=".equals(elem)) {
                            this.max = Integer.MAX_VALUE;
                        }
                        else if ("<".equals(elem)) {
                            final int n = this.min - 1;
                            this.min = n;
                            this.max = n;
                            this.min = Integer.MIN_VALUE;
                        }
                        else if ("<=".equals(elem)) {
                            this.max = this.min;
                            this.min = Integer.MIN_VALUE;
                        }
                    }
                    else if (this.has(2)) {
                        if ("<".equals(this.elem(2))) {
                            this.max = this.min;
                            this.min = Integer.MIN_VALUE;
                        }
                        else {
                            this.max = Integer.MAX_VALUE;
                        }
                    }
                    return;
                }
                if (has) {
                    throw new InvalidConstraintException("Unexpected modifier '" + this.elem(0) + "' in " + this.expr + " parsing range");
                }
                this.max = this.min + this.val(6);
            }
        }
        
        private String elem(final int n) {
            return this.constraint[n];
        }
        
        private Constraint() {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.expr = null;
            this.token = "*";
            this.constraint = new String[0];
        }
        
        private int val(final int n) {
            return (this.constraint[n] != null) ? Integer.parseInt(this.constraint[n]) : 0;
        }
        
        public String getRangeHumanReadable() {
            if (this.min == Integer.MIN_VALUE && this.max == Integer.MAX_VALUE) {
                return "ANY VALUE";
            }
            if (this.min == Integer.MIN_VALUE) {
                return String.format("less than or equal to %d", this.max);
            }
            if (this.max == Integer.MAX_VALUE) {
                return String.format("greater than or equal to %d", this.min);
            }
            if (this.min == this.max) {
                return String.format("%d", this.min);
            }
            return String.format("between %d and %d", this.min, this.max);
        }
        
        @Override
        public String toString() {
            return String.format("Constraint(%s [%d-%d])", this.token, this.min, this.max);
        }
        
        private boolean has(final int n) {
            return this.constraint[n] != null;
        }
        
        static {
            NONE = new Constraint();
            pattern = Pattern.compile("^([A-Z0-9\\-_\\.]+)\\((?:(<|<=|>|>=|=)?([0-9]+)(<|(-)([0-9]+)?|>|(\\+)([0-9]+)?)?)?\\)$");
        }
        
        public String getToken() {
            return this.token;
        }
        
        public int getMax() {
            return this.max;
        }
        
        public int getMin() {
            return this.min;
        }
        
        Constraint(final String s) {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.expr = s;
            final Matcher matcher = Constraint.pattern.matcher(s);
            if (!matcher.matches()) {
                throw new InvalidConstraintException("Constraint syntax was invalid parsing: " + this.expr);
            }
            this.token = matcher.group(1);
            this.constraint = new String[] { matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5), matcher.group(6), matcher.group(7), matcher.group(8) };
            this.parse();
        }
        
        void append(final Constraint next) {
            if (this.next != null) {
                this.next.append(next);
                return;
            }
            this.next = next;
        }
    }
}
