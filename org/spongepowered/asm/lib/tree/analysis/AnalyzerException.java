// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree.analysis;

import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class AnalyzerException extends Exception
{
    public final /* synthetic */ AbstractInsnNode node;
    
    public AnalyzerException(final AbstractInsnNode node, final String str, final Object obj, final Value obj2) {
        super(((str == null) ? "Expected " : (str + ": expected ")) + obj + ", but found " + obj2);
        this.node = node;
    }
    
    public AnalyzerException(final AbstractInsnNode node, final String message) {
        super(message);
        this.node = node;
    }
    
    public AnalyzerException(final AbstractInsnNode node, final String message, final Throwable cause) {
        super(message, cause);
        this.node = node;
    }
}
