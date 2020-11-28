// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.List;

public class LookupSwitchInsnNode extends AbstractInsnNode
{
    public /* synthetic */ List<LabelNode> labels;
    public /* synthetic */ LabelNode dflt;
    public /* synthetic */ List<Integer> keys;
    
    @Override
    public int getType() {
        return 12;
    }
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        final int[] array = new int[this.keys.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.keys.get(i);
        }
        final Label[] array2 = new Label[this.labels.size()];
        for (int j = 0; j < array2.length; ++j) {
            array2[j] = this.labels.get(j).getLabel();
        }
        methodVisitor.visitLookupSwitchInsn(this.dflt.getLabel(), array, array2);
        this.acceptAnnotations(methodVisitor);
    }
    
    public LookupSwitchInsnNode(final LabelNode dflt, final int[] array, final LabelNode[] a) {
        super(171);
        this.dflt = dflt;
        this.keys = new ArrayList<Integer>((array == null) ? 0 : array.length);
        this.labels = new ArrayList<LabelNode>((a == null) ? 0 : a.length);
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                this.keys.add(array[i]);
            }
        }
        if (a != null) {
            this.labels.addAll(Arrays.asList(a));
        }
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        final LookupSwitchInsnNode lookupSwitchInsnNode = new LookupSwitchInsnNode(AbstractInsnNode.clone(this.dflt, map), null, AbstractInsnNode.clone(this.labels, map));
        lookupSwitchInsnNode.keys.addAll(this.keys);
        return lookupSwitchInsnNode.cloneAnnotations(this);
    }
}
