// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.ClassVisitor;

public class ClassRemapper extends ClassVisitor
{
    protected final /* synthetic */ Remapper remapper;
    protected /* synthetic */ String className;
    
    protected FieldVisitor createFieldRemapper(final FieldVisitor fieldVisitor) {
        return (FieldVisitor)new FieldRemapper(fieldVisitor, this.remapper);
    }
    
    public ClassRemapper(final ClassVisitor classVisitor, final Remapper remapper) {
        this(327680, classVisitor, remapper);
    }
    
    @Override
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        final AnnotationVisitor visitAnnotation = super.visitAnnotation(this.remapper.mapDesc(s), b);
        return (visitAnnotation == null) ? null : this.createAnnotationRemapper(visitAnnotation);
    }
    
    @Override
    public void visitOuterClass(final String s, final String s2, final String s3) {
        super.visitOuterClass(this.remapper.mapType(s), (s2 == null) ? null : this.remapper.mapMethodName(s, s2, s3), (s3 == null) ? null : this.remapper.mapMethodDesc(s3));
    }
    
    protected ClassRemapper(final int n, final ClassVisitor classVisitor, final Remapper remapper) {
        super(n, classVisitor);
        this.remapper = remapper;
    }
    
    @Override
    public FieldVisitor visitField(final int n, final String s, final String s2, final String s3, final Object o) {
        final FieldVisitor visitField = super.visitField(n, this.remapper.mapFieldName(this.className, s, s2), this.remapper.mapDesc(s2), this.remapper.mapSignature(s3, true), this.remapper.mapValue(o));
        return (visitField == null) ? null : this.createFieldRemapper(visitField);
    }
    
    @Override
    public void visit(final int n, final int n2, final String className, final String s, final String s2, final String[] array) {
        this.className = className;
        super.visit(n, n2, this.remapper.mapType(className), this.remapper.mapSignature(s, false), this.remapper.mapType(s2), (String[])((array == null) ? null : this.remapper.mapTypes(array)));
    }
    
    protected MethodVisitor createMethodRemapper(final MethodVisitor methodVisitor) {
        return (MethodVisitor)new MethodRemapper(methodVisitor, this.remapper);
    }
    
    @Override
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        final AnnotationVisitor visitTypeAnnotation = super.visitTypeAnnotation(n, typePath, this.remapper.mapDesc(s), b);
        return (visitTypeAnnotation == null) ? null : this.createAnnotationRemapper(visitTypeAnnotation);
    }
    
    @Override
    public void visitInnerClass(final String s, final String s2, final String s3, final int n) {
        super.visitInnerClass(this.remapper.mapType(s), (s2 == null) ? null : this.remapper.mapType(s2), s3, n);
    }
    
    protected AnnotationVisitor createAnnotationRemapper(final AnnotationVisitor annotationVisitor) {
        return (AnnotationVisitor)new AnnotationRemapper(annotationVisitor, this.remapper);
    }
    
    @Override
    public MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
        final MethodVisitor visitMethod = super.visitMethod(n, this.remapper.mapMethodName(this.className, s, s2), this.remapper.mapMethodDesc(s2), this.remapper.mapSignature(s3, false), (String[])((array == null) ? null : this.remapper.mapTypes(array)));
        return (visitMethod == null) ? null : this.createMethodRemapper(visitMethod);
    }
}
