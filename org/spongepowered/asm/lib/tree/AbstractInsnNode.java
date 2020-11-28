// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Map;
import org.spongepowered.asm.lib.AnnotationVisitor;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInsnNode
{
    /* synthetic */ int index;
    /* synthetic */ AbstractInsnNode next;
    public /* synthetic */ List<TypeAnnotationNode> visibleTypeAnnotations;
    public /* synthetic */ List<TypeAnnotationNode> invisibleTypeAnnotations;
    /* synthetic */ AbstractInsnNode prev;
    protected /* synthetic */ int opcode;
    
    public int getOpcode() {
        return this.opcode;
    }
    
    protected AbstractInsnNode(final int opcode) {
        this.opcode = opcode;
        this.index = -1;
    }
    
    protected final AbstractInsnNode cloneAnnotations(final AbstractInsnNode abstractInsnNode) {
        if (abstractInsnNode.visibleTypeAnnotations != null) {
            this.visibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            for (int i = 0; i < abstractInsnNode.visibleTypeAnnotations.size(); ++i) {
                final TypeAnnotationNode typeAnnotationNode = abstractInsnNode.visibleTypeAnnotations.get(i);
                final TypeAnnotationNode typeAnnotationNode2 = new TypeAnnotationNode(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc);
                typeAnnotationNode.accept(typeAnnotationNode2);
                this.visibleTypeAnnotations.add(typeAnnotationNode2);
            }
        }
        if (abstractInsnNode.invisibleTypeAnnotations != null) {
            this.invisibleTypeAnnotations = new ArrayList<TypeAnnotationNode>();
            for (int j = 0; j < abstractInsnNode.invisibleTypeAnnotations.size(); ++j) {
                final TypeAnnotationNode typeAnnotationNode3 = abstractInsnNode.invisibleTypeAnnotations.get(j);
                final TypeAnnotationNode typeAnnotationNode4 = new TypeAnnotationNode(typeAnnotationNode3.typeRef, typeAnnotationNode3.typePath, typeAnnotationNode3.desc);
                typeAnnotationNode3.accept(typeAnnotationNode4);
                this.invisibleTypeAnnotations.add(typeAnnotationNode4);
            }
        }
        return this;
    }
    
    public AbstractInsnNode getPrevious() {
        return this.prev;
    }
    
    public abstract int getType();
    
    public abstract AbstractInsnNode clone(final Map<LabelNode, LabelNode> p0);
    
    static LabelNode clone(final LabelNode labelNode, final Map<LabelNode, LabelNode> map) {
        return map.get(labelNode);
    }
    
    public AbstractInsnNode getNext() {
        return this.next;
    }
    
    static {
        INSN = 0;
        LABEL = 8;
        FIELD_INSN = 4;
        METHOD_INSN = 5;
        TABLESWITCH_INSN = 11;
        TYPE_INSN = 3;
        INT_INSN = 1;
        LDC_INSN = 9;
        LINE = 15;
        MULTIANEWARRAY_INSN = 13;
        FRAME = 14;
        JUMP_INSN = 7;
        LOOKUPSWITCH_INSN = 12;
        INVOKE_DYNAMIC_INSN = 6;
        IINC_INSN = 10;
        VAR_INSN = 2;
    }
    
    public abstract void accept(final MethodVisitor p0);
    
    static LabelNode[] clone(final List<LabelNode> list, final Map<LabelNode, LabelNode> map) {
        final LabelNode[] array = new LabelNode[list.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = map.get(list.get(i));
        }
        return array;
    }
    
    protected final void acceptAnnotations(final MethodVisitor methodVisitor) {
        for (int n = (this.visibleTypeAnnotations == null) ? 0 : this.visibleTypeAnnotations.size(), i = 0; i < n; ++i) {
            final TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i);
            typeAnnotationNode.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        for (int n2 = (this.invisibleTypeAnnotations == null) ? 0 : this.invisibleTypeAnnotations.size(), j = 0; j < n2; ++j) {
            final TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(j);
            typeAnnotationNode2.accept(methodVisitor.visitInsnAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
    }
}
