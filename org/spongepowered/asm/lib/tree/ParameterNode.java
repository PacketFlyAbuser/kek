// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;

public class ParameterNode
{
    public /* synthetic */ String name;
    public /* synthetic */ int access;
    
    public ParameterNode(final String name, final int access) {
        this.name = name;
        this.access = access;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitParameter(this.name, this.access);
    }
}
