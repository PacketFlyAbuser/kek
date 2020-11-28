// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.invoke.ModifyArgInjector;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public class ModifyArgInjectionInfo extends InjectionInfo
{
    @Override
    protected String getDescription() {
        return "Argument modifier method";
    }
    
    @Override
    protected Injector parseInjector(final AnnotationNode annotationNode) {
        return new ModifyArgInjector(this, Annotations.getValue(annotationNode, "index", -1));
    }
    
    public ModifyArgInjectionInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final AnnotationNode annotationNode) {
        super(mixinTargetContext, methodNode, annotationNode);
    }
}
