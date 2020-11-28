// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.MethodVisitor;

public class LocalVariableNode
{
    public /* synthetic */ LabelNode start;
    public /* synthetic */ String desc;
    public /* synthetic */ String signature;
    public /* synthetic */ LabelNode end;
    public /* synthetic */ String name;
    public /* synthetic */ int index;
    
    public LocalVariableNode(final String name, final String desc, final String signature, final LabelNode start, final LabelNode end, final int index) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
        this.start = start;
        this.end = end;
        this.index = index;
    }
    
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLocalVariable(this.name, this.desc, this.signature, this.start.getLabel(), this.end.getLabel(), this.index);
    }
}
