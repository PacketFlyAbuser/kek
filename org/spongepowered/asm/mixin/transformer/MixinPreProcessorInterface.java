// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;

class MixinPreProcessorInterface extends MixinPreProcessorStandard
{
    @Override
    protected void prepareMethod(final MixinInfo.MixinMethodNode mixinMethodNode, final ClassInfo.Method obj) {
        if (!Bytecode.hasFlag(mixinMethodNode, 1) && !Bytecode.hasFlag(mixinMethodNode, 4096)) {
            throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains a non-public method! Found " + obj + " in " + this.mixin);
        }
        super.prepareMethod(mixinMethodNode, obj);
    }
    
    MixinPreProcessorInterface(final MixinInfo mixinInfo, final MixinInfo.MixinClassNode mixinClassNode) {
        super(mixinInfo, mixinClassNode);
    }
    
    @Override
    protected boolean validateField(final MixinTargetContext mixinTargetContext, final FieldNode fieldNode, final AnnotationNode annotationNode) {
        if (!Bytecode.hasFlag(fieldNode, 8)) {
            throw new InvalidInterfaceMixinException(this.mixin, "Interface mixin contains an instance field! Found " + fieldNode.name + " in " + this.mixin);
        }
        return super.validateField(mixinTargetContext, fieldNode, annotationNode);
    }
}
