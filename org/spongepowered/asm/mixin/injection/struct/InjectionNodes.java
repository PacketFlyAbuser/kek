// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import java.util.HashMap;
import org.spongepowered.asm.util.Bytecode;
import java.util.Map;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.ArrayList;

public class InjectionNodes extends ArrayList<InjectionNode>
{
    public InjectionNode get(final AbstractInsnNode abstractInsnNode) {
        for (final InjectionNode injectionNode : this) {
            if (injectionNode.matches(abstractInsnNode)) {
                return injectionNode;
            }
        }
        return null;
    }
    
    public void remove(final AbstractInsnNode abstractInsnNode) {
        final InjectionNode value = this.get(abstractInsnNode);
        if (value != null) {
            value.remove();
        }
    }
    
    public InjectionNode add(final AbstractInsnNode abstractInsnNode) {
        InjectionNode value = this.get(abstractInsnNode);
        if (value == null) {
            value = new InjectionNode(abstractInsnNode);
            this.add(value);
        }
        return value;
    }
    
    public void replace(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2) {
        final InjectionNode value = this.get(abstractInsnNode);
        if (value != null) {
            value.replace(abstractInsnNode2);
        }
    }
    
    public boolean contains(final AbstractInsnNode abstractInsnNode) {
        return this.get(abstractInsnNode) != null;
    }
    
    public static class InjectionNode implements Comparable<InjectionNode>
    {
        private static /* synthetic */ int nextId;
        private /* synthetic */ AbstractInsnNode currentTarget;
        private final /* synthetic */ int id;
        private /* synthetic */ Map<String, Object> decorations;
        private final /* synthetic */ AbstractInsnNode originalTarget;
        
        public InjectionNode replace(final AbstractInsnNode currentTarget) {
            this.currentTarget = currentTarget;
            return this;
        }
        
        public boolean isReplaced() {
            return this.originalTarget != this.currentTarget;
        }
        
        public boolean hasDecoration(final String s) {
            return this.decorations != null && this.decorations.get(s) != null;
        }
        
        @Override
        public String toString() {
            return String.format("InjectionNode[%s]", Bytecode.describeNode(this.currentTarget).replaceAll("\\s+", " "));
        }
        
        public InjectionNode remove() {
            this.currentTarget = null;
            return this;
        }
        
        public boolean isRemoved() {
            return this.currentTarget == null;
        }
        
        @Override
        public int compareTo(final InjectionNode injectionNode) {
            return (injectionNode == null) ? Integer.MAX_VALUE : (this.hashCode() - injectionNode.hashCode());
        }
        
        public <V> InjectionNode decorate(final String s, final V v) {
            if (this.decorations == null) {
                this.decorations = new HashMap<String, Object>();
            }
            this.decorations.put(s, v);
            return this;
        }
        
        static {
            InjectionNode.nextId = 0;
        }
        
        public AbstractInsnNode getOriginalTarget() {
            return this.originalTarget;
        }
        
        public InjectionNode(final AbstractInsnNode abstractInsnNode) {
            this.originalTarget = abstractInsnNode;
            this.currentTarget = abstractInsnNode;
            this.id = InjectionNode.nextId++;
        }
        
        public AbstractInsnNode getCurrentTarget() {
            return this.currentTarget;
        }
        
        public int getId() {
            return this.id;
        }
        
        public boolean matches(final AbstractInsnNode abstractInsnNode) {
            return this.originalTarget == abstractInsnNode || this.currentTarget == abstractInsnNode;
        }
        
        public <V> V getDecoration(final String s) {
            return (V)((this.decorations == null) ? null : this.decorations.get(s));
        }
    }
}
