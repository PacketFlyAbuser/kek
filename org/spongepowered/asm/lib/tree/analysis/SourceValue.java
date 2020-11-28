// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Set;

public class SourceValue implements Value
{
    public final /* synthetic */ Set<AbstractInsnNode> insns;
    public final /* synthetic */ int size;
    
    @Override
    public int hashCode() {
        return this.insns.hashCode();
    }
    
    public int getSize() {
        return this.size;
    }
    
    public SourceValue(final int n) {
        this(n, SmallSet.emptySet());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof SourceValue)) {
            return false;
        }
        final SourceValue sourceValue = (SourceValue)o;
        return this.size == sourceValue.size && this.insns.equals(sourceValue.insns);
    }
    
    public SourceValue(final int size, final AbstractInsnNode abstractInsnNode) {
        this.size = size;
        this.insns = new SmallSet<AbstractInsnNode>(abstractInsnNode, null);
    }
    
    public SourceValue(final int size, final Set<AbstractInsnNode> insns) {
        this.size = size;
        this.insns = insns;
    }
}
