// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Map;
import org.spongepowered.asm.lib.Handle;

public class InvokeDynamicInsnNode extends AbstractInsnNode
{
    public /* synthetic */ String desc;
    public /* synthetic */ Handle bsm;
    public /* synthetic */ Object[] bsmArgs;
    public /* synthetic */ String name;
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new InvokeDynamicInsnNode(this.name, this.desc, this.bsm, this.bsmArgs).cloneAnnotations(this);
    }
    
    @Override
    public int getType() {
        return 6;
    }
    
    public InvokeDynamicInsnNode(final String name, final String desc, final Handle bsm, final Object... bsmArgs) {
        super(186);
        this.name = name;
        this.desc = desc;
        this.bsm = bsm;
        this.bsmArgs = bsmArgs;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitInvokeDynamicInsn(this.name, this.desc, this.bsm, this.bsmArgs);
        this.acceptAnnotations(methodVisitor);
    }
}
