// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class IntInsnNode extends AbstractInsnNode
{
    public /* synthetic */ int operand;
    
    @Override
    public int getType() {
        return 1;
    }
    
    public IntInsnNode(final int n, final int operand) {
        super(n);
        this.operand = operand;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitIntInsn(this.opcode, this.operand);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new IntInsnNode(this.opcode, this.operand).cloneAnnotations(this);
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
}
