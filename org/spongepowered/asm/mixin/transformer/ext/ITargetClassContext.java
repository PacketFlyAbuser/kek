// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.ext;

import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.ClassNode;

public interface ITargetClassContext
{
    ClassNode getClassNode();
    
    ClassInfo getClassInfo();
}
