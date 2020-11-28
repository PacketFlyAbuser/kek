// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class VarInsnNode extends AbstractInsnNode
{
    public /* synthetic */ int var;
    
    @Override
    public int getType() {
        return 2;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitVarInsn(this.opcode, this.var);
        this.acceptAnnotations(methodVisitor);
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new VarInsnNode(this.opcode, this.var).cloneAnnotations(this);
    }
    
    public VarInsnNode(final int n, final int var) {
        super(n);
        this.var = var;
    }
}
