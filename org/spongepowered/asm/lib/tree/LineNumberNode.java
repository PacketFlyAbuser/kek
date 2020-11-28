// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Map;
import org.spongepowered.asm.lib.MethodVisitor;

public class LineNumberNode extends AbstractInsnNode
{
    public /* synthetic */ int line;
    public /* synthetic */ LabelNode start;
    
    @Override
    public void accept(final MethodVisitor methodVisitor) {
        methodVisitor.visitLineNumber(this.line, this.start.getLabel());
    }
    
    public LineNumberNode(final int line, final LabelNode start) {
        super(-1);
        this.line = line;
        this.start = start;
    }
    
    @Override
    public AbstractInsnNode clone(final Map<LabelNode, LabelNode> map) {
        return new LineNumberNode(this.line, AbstractInsnNode.clone(this.start, map));
    }
    
    @Override
    public int getType() {
        return 15;
    }
}
