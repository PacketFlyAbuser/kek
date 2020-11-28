// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;

public abstract class SpecialMethodInfo implements IInjectionPointContext
{
    protected final /* synthetic */ MixinTargetContext mixin;
    protected final /* synthetic */ AnnotationNode annotation;
    protected final /* synthetic */ MethodNode method;
    protected final /* synthetic */ ClassNode classNode;
    
    @Override
    public final MethodNode getMethod() {
        return this.method;
    }
    
    public SpecialMethodInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
        this.mixin = mixin;
        this.method = method;
        this.annotation = annotation;
        this.classNode = mixin.getTargetClassNode();
    }
    
    public final ClassNode getClassNode() {
        return this.classNode;
    }
    
    @Override
    public final IMixinContext getContext() {
        return this.mixin;
    }
    
    @Override
    public final AnnotationNode getAnnotation() {
        return this.annotation;
    }
}
