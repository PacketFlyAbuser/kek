// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class LdcInsnNode extends AbstractInsnNode
{
    public /* synthetic */ Object cst;
    
    public LdcInsnNode(final Object cst) {
        super(18);
        this.cst = cst;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLdcInsn(this.cst);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new LdcInsnNode(this.cst).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 9;
    }
}
