// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.Map;
import java.util.List;

public class TableSwitchInsnNode extends AbstractInsnNode
{
    public /* synthetic */ List<LabelNode> labels;
    public /* synthetic */ LabelNode dflt;
    public /* synthetic */ int max;
    public /* synthetic */ int min;
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new TableSwitchInsnNode(this.min, this.max, AbstractInsnNode.clone(this.dflt, map), AbstractInsnNode.clone(this.labels, map)).cloneAnnotations(this);
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        final Label[] array = new Label[this.labels.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.labels.get(i).getLabel();
        }
        methodVisitor.visitTableSwitchInsn(this.min, this.max, this.dflt.getLabel(), array);
        this.acceptAnnotations(methodVisitor);
    }
    
    @Override
    public int getType() {
        return 11;
    }
    
    public TableSwitchInsnNode(final int min, final int max, final LabelNode dflt, final LabelNode... a) {
        super(170);
        this.min = min;
        this.max = max;
        this.dflt = dflt;
        this.labels = new ArrayList<LabelNode>();
        if (a != null) {
            this.labels.addAll(Arrays.asList(a));
        }
    }
}