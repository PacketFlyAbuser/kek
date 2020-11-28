// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib;

public abstract class MethodVisitor
{
    protected final /* synthetic */ int api;
    protected /* synthetic */ MethodVisitor mv;
    
    public void visitInsn(final int n) {
        if (this.mv != null) {
            this.mv.visitInsn(n);
        }
    }
    
    public AnnotationVisitor visitAnnotationDefault() {
        if (this.mv != null) {
            return this.mv.visitAnnotationDefault();
        }
        return null;
    }
    
    public void visitMultiANewArrayInsn(final String s, final int n) {
        if (this.mv != null) {
            this.mv.visitMultiANewArrayInsn(s, n);
        }
    }
    
    public void visitFrame(final int n, final int n2, final Object[] array, final int n3, final Object[] array2) {
        if (this.mv != null) {
            this.mv.visitFrame(n, n2, array, n3, array2);
        }
    }
    
    public AnnotationVisitor visitInsnAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitInsnAnnotation(n, typePath, s, b);
        }
        return null;
    }
    
    public AnnotationVisitor visitTryCatchAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitTryCatchAnnotation(n, typePath, s, b);
        }
        return null;
    }
    
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3, final boolean b) {
        if (this.api >= 327680) {
            if (this.mv != null) {
                this.mv.visitMethodInsn(n, s, s2, s3, b);
            }
            return;
        }
        if (b != (n == 185)) {
            throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM 5");
        }
        this.visitMethodInsn(n, s, s2, s3);
    }
    
    public AnnotationVisitor visitParameterAnnotation(final int n, final String s, final boolean b) {
        if (this.mv != null) {
            return this.mv.visitParameterAnnotation(n, s, b);
        }
        return null;
    }
    
    public void visitParameter(final String s, final int n) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            this.mv.visitParameter(s, n);
        }
    }
    
    public MethodVisitor(final int n) {
        this(n, null);
    }
    
    public void visitAttribute(final Attribute attribute) {
        if (this.mv != null) {
            this.mv.visitAttribute(attribute);
        }
    }
    
    public AnnotationVisitor visitLocalVariableAnnotation(final int n, final TypePath typePath, final Label[] array, final Label[] array2, final int[] array3, final String s, final boolean b) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitLocalVariableAnnotation(n, typePath, array, array2, array3, s, b);
        }
        return null;
    }
    
    public void visitVarInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitVarInsn(n, n2);
        }
    }
    
    public void visitIntInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitIntInsn(n, n2);
        }
    }
    
    public void visitMaxs(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitMaxs(n, n2);
        }
    }
    
    public void visitIincInsn(final int n, final int n2) {
        if (this.mv != null) {
            this.mv.visitIincInsn(n, n2);
        }
    }
    
    public void visitCode() {
        if (this.mv != null) {
            this.mv.visitCode();
        }
    }
    
    public MethodVisitor(final int api, final MethodVisitor mv) {
        if (api != 262144 && api != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.mv = mv;
    }
    
    public void visitLocalVariable(final String s, final String s2, final String s3, final Label label, final Label label2, final int n) {
        if (this.mv != null) {
            this.mv.visitLocalVariable(s, s2, s3, label, label2, n);
        }
    }
    
    public void visitInvokeDynamicInsn(final String s, final String s2, final Handle handle, final Object... array) {
        if (this.mv != null) {
            this.mv.visitInvokeDynamicInsn(s, s2, handle, array);
        }
    }
    
    @Deprecated
    public void visitMethodInsn(final int n, final String s, final String s2, final String s3) {
        if (this.api >= 327680) {
            this.visitMethodInsn(n, s, s2, s3, n == 185);
            return;
        }
        if (this.mv != null) {
            this.mv.visitMethodInsn(n, s, s2, s3);
        }
    }
    
    public void visitTryCatchBlock(final Label label, final Label label2, final Label label3, final String s) {
        if (this.mv != null) {
            this.mv.visitTryCatchBlock(label, label2, label3, s);
        }
    }
    
    public void visitLdcInsn(final Object o) {
        if (this.mv != null) {
            this.mv.visitLdcInsn(o);
        }
    }
    
    public void visitFieldInsn(final int n, final String s, final String s2, final String s3) {
        if (this.mv != null) {
            this.mv.visitFieldInsn(n, s, s2, s3);
        }
    }
    
    public void visitJumpInsn(final int n, final Label label) {
        if (this.mv != null) {
            this.mv.visitJumpInsn(n, label);
        }
    }
    
    public AnnotationVisitor visitTypeAnnotation(final int n, final TypePath typePath, final String s, final boolean b) {
        if (this.api < 327680) {
            throw new RuntimeException();
        }
        if (this.mv != null) {
            return this.mv.visitTypeAnnotation(n, typePath, s, b);
        }
        return null;
    }
    
    public void visitEnd() {
        if (this.mv != null) {
            this.mv.visitEnd();
        }
    }
    
    public void visitLookupSwitchInsn(final Label label, final int[] array, final Label[] array2) {
        if (this.mv != null) {
            this.mv.visitLookupSwitchInsn(label, array, array2);
        }
    }
    
    public void visitTypeInsn(final int n, final String s) {
        if (this.mv != null) {
            this.mv.visitTypeInsn(n, s);
        }
    }
    
    public void visitLabel(final Label label) {
        if (this.mv != null) {
            this.mv.visitLabel(label);
        }
    }
    
    public AnnotationVisitor visitAnnotation(final String s, final boolean b) {
        if (this.mv != null) {
            return this.mv.visitAnnotation(s, b);
        }
        return null;
    }
    
    public void visitTableSwitchInsn(final int n, final int n2, final Label label, final Label... array) {
        if (this.mv != null) {
            this.mv.visitTableSwitchInsn(n, n2, label, array);
        }
    }
    
    public void visitLineNumber(final int n, final Label label) {
        if (this.mv != null) {
            this.mv.visitLineNumber(n, label);
        }
    }
}
