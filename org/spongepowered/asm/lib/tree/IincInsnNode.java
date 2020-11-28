// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class IincInsnNode extends AbstractInsnNode
{
    public /* synthetic */ int var;
    public /* synthetic */ int incr;
    
    public IincInsnNode(final int var, final int incr) {
        super(132);
        this.var = var;
        this.incr = incr;
    }
    
    @Override
    public int getType() {
        return 10;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitIincInsn(this.var, this.incr);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new IincInsnNode(this.var, this.incr).cloneAnnotations(this);
    }
}
