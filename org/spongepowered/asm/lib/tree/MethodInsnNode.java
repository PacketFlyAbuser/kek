// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class MethodInsnNode extends AbstractInsnNode
{
    public /* synthetic */ String name;
    public /* synthetic */ String desc;
    public /* synthetic */ String owner;
    public /* synthetic */ boolean itf;
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitMethodInsn(this.opcode, this.owner, this.name, this.desc, this.itf);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Deprecated
    public MethodInsnNode(final int n, final String s, final String s2, final String s3) {
        this(n, s, s2, s3, n == 185);
    }
    
    @Override
    public int getType() {
        return 5;
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new MethodInsnNode(this.opcode, this.owner, this.name, this.desc, this.itf);
    }
    
    public MethodInsnNode(final int n, final String owner, final String name, final String desc, final boolean itf) {
        super(n);
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.itf = itf;
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
}
