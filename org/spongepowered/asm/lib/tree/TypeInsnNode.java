// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class TypeInsnNode extends AbstractInsnNode
{
    public /* synthetic */ String desc;
    
    @Override
    public int getType() {
        return 3;
    }
    
    public TypeInsnNode(final int n, final String desc) {
        super(n);
        this.desc = desc;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitTypeInsn(this.opcode, this.desc);
        this.acceptAnnotations(methodVisitor);
    }
    
    public void setOpcode(final int opcode) {
        this.opcode = opcode;
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new TypeInsnNode(this.opcode, this.desc).cloneAnnotations(this);
    }
}
