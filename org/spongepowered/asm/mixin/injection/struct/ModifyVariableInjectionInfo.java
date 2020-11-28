// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.modify.ModifyVariableInjector;
import org.spongepowered.asm.mixin.injection.modify.LocalVariableDiscriminator;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public class ModifyVariableInjectionInfo extends InjectionInfo
{
    @Override
    protected Injector parseInjector(final AnnotationNode annotationNode) {
        return new ModifyVariableInjector(this, LocalVariableDiscriminator.parse(annotationNode));
    }
    
    public ModifyVariableInjectionInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final AnnotationNode annotationNode) {
        super(mixinTargetContext, methodNode, annotationNode);
    }
    
    @Override
    protected String getDescription() {
        return "Variable modifier method";
    }
}
