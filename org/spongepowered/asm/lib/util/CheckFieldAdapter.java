// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.Attribute;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;

public class CheckFieldAdapter extends FieldVisitor
{
    private /* synthetic */ boolean end;
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        this.checkEnd();
        CheckMethodAdapter.checkDesc(s, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(s, b));
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        this.checkEnd();
        final int i = n >>> 24;
        if (i != 19) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i));
        }
        CheckClassAdapter.checkTypeRefAndPath(n, typePath);
        CheckMethodAdapter.checkDesc(s, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(n, typePath, s, b));
    }
    
    @Override
    public void visitEnd() {
        this.checkEnd();
        this.end = true;
        super.visitEnd();
    }
    
    protected CheckFieldAdapter(final int n, final FieldVisitor fieldVisitor) {
        super(n, fieldVisitor);
    }
    
    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
    
    public CheckFieldAdapter(final FieldVisitor fieldVisitor) {
        this(327680, fieldVisitor);
        if (this.getClass() != CheckFieldAdapter.class) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    public void visitAttribute(final Attribute attribute) {
        this.checkEnd();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }
}
