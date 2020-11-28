// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class MultiANewArrayInsnNode extends AbstractInsnNode
{
    public /* synthetic */ String desc;
    public /* synthetic */ int dims;
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitMultiANewArrayInsn(this.desc, this.dims);
        this.acceptAnnotations(methodVisitor);
    }
    
    public MultiANewArrayInsnNode(final String desc, final int dims) {
        super(197);
        this.desc = desc;
        this.dims = dims;
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new MultiANewArrayInsnNode(this.desc, this.dims).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 13;
    }
}
