// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.ClassVisitor;

public class InnerClassNode
{
    public /* synthetic */ int access;
    public /* synthetic */ String name;
    public /* synthetic */ String innerName;
    public /* synthetic */ String outerName;
    
    public InnerClassNode(final String name, final String outerName, final String innerName, final int access) {
        this.name = name;
        this.outerName = outerName;
        this.innerName = innerName;
        this.access = access;
    }
    
    public void accept(final ClassVisitor classVisitor) {
        classVisitor.visitInnerClass(this.name, this.outerName, this.innerName, this.access);
    }
}
