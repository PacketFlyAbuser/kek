// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.invoke.ModifyArgsInjector;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public class ModifyArgsInjectionInfo extends InjectionInfo
{
    @Override
    protected Injector parseInjector(final AnnotationNode annotationNode) {
        return new ModifyArgsInjector(this);
    }
    
    @Override
    protected String getDescription() {
        return "Multi-argument modifier method";
    }
    
    public ModifyArgsInjectionInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final AnnotationNode annotationNode) {
        super(mixinTargetContext, methodNode, annotationNode);
    }
}
