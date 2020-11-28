// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.ListIterator;
import org.spongepowered.asm.mixin.struct.MemberRef;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.HashSet;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Set;

abstract class ClassContext
{
    private final /* synthetic */ Set<ClassInfo.Method> upgradedMethods;
    
    protected void upgradeMethods() {
        final Iterator<MethodNode> iterator = this.getClassNode().methods.iterator();
        while (iterator.hasNext()) {
            this.upgradeMethod(iterator.next());
        }
    }
    
    ClassContext() {
        this.upgradedMethods = new HashSet<ClassInfo.Method>();
    }
    
    void addUpgradedMethod(final MethodNode methodNode) {
        final ClassInfo.Method method = this.getClassInfo().findMethod(methodNode);
        if (method == null) {
            throw new IllegalStateException("Meta method for " + methodNode.name + " not located in " + this);
        }
        this.upgradedMethods.add(method);
    }
    
    private void upgradeMethod(final MethodNode methodNode) {
        for (final AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            if (!(abstractInsnNode instanceof MethodInsnNode)) {
                continue;
            }
            final MemberRef.Method method = new MemberRef.Method((MethodInsnNode)abstractInsnNode);
            if (!method.getOwner().equals(this.getClassRef())) {
                continue;
            }
            this.upgradeMethodRef(methodNode, method, this.getClassInfo().findMethod(method.getName(), method.getDesc(), 10));
        }
    }
    
    abstract ClassNode getClassNode();
    
    protected void upgradeMethodRef(final MethodNode methodNode, final MemberRef memberRef, final ClassInfo.Method method) {
        if (memberRef.getOpcode() != 183) {
            return;
        }
        if (this.upgradedMethods.contains(method)) {
            memberRef.setOpcode(182);
        }
    }
    
    abstract String getClassRef();
    
    abstract ClassInfo getClassInfo();
}
