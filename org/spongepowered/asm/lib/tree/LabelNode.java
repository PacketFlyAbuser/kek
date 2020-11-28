// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Map;
import org.spongepowered.asm.lib.Label;

public class LabelNode extends AbstractInsnNode
{
    private /* synthetic */ Label label;
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return map.get(this);
    }
    
    public LabelNode(final Label label) {
        super(-1);
        this.label = label;
    }
    
    public LabelNode() {
        super(-1);
    }
    
    @Override
    public int getType() {
        return 8;
    }
    
    public Label getLabel() {
        if (this.label == null) {
            this.label = new Label();
        }
        return this.label;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLabel(this.getLabel());
    }
    
    public void resetLabel() {
        this.label = null;
    }
}
