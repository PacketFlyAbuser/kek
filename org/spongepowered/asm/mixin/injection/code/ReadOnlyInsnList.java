// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;

class ReadOnlyInsnList extends InsnList
{
    private /* synthetic */ InsnList insnList;
    
    @Override
    public AbstractInsnNode[] toArray() {
        return this.insnList.toArray();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode abstractInsnNode, final InsnList list) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator() {
        return this.insnList.iterator();
    }
    
    @Override
    public final void set(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2) {
        throw new UnsupportedOperationException();
    }
    
    void dispose() {
        this.insnList = null;
    }
    
    @Override
    public AbstractInsnNode get(final int n) {
        return this.insnList.get(n);
    }
    
    @Override
    public final void insert(final AbstractInsnNode abstractInsnNode) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int size() {
        return this.insnList.size();
    }
    
    @Override
    public final void resetLabels() {
        this.insnList.resetLabels();
    }
    
    public ReadOnlyInsnList(final InsnList insnList) {
        this.insnList = insnList;
    }
    
    @Override
    public final void insert(final AbstractInsnNode abstractInsnNode, final InsnList list) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final InsnList list) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void add(final AbstractInsnNode abstractInsnNode) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void insert(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void remove(final AbstractInsnNode abstractInsnNode) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int indexOf(final AbstractInsnNode abstractInsnNode) {
        return this.insnList.indexOf(abstractInsnNode);
    }
    
    @Override
    public final void insert(final InsnList list) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<AbstractInsnNode> iterator(final int n) {
        return this.insnList.iterator(n);
    }
    
    @Override
    public AbstractInsnNode getLast() {
        return this.insnList.getLast();
    }
    
    @Override
    public final void insertBefore(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean contains(final AbstractInsnNode abstractInsnNode) {
        return this.insnList.contains(abstractInsnNode);
    }
    
    @Override
    public AbstractInsnNode getFirst() {
        return this.insnList.getFirst();
    }
}
