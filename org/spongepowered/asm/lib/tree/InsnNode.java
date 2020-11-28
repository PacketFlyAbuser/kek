// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Map;

public class InsnNode extends AbstractInsnNode
{
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new InsnNode(this.opcode).cloneAnnotations(this);
    }
    
    public InsnNode(final int n) {
        super(n);
    }
    
    @Override
    public int getType() {
        return 0;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitInsn(this.opcode);
        this.acceptAnnotations(methodVisitor);
    }
}
