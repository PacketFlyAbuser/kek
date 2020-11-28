// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.Map;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidInterfaceMixinException;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.lib.tree.MethodNode;

class MixinApplicatorInterface extends MixinApplicatorStandard
{
    @Override
    protected void applyInjections(final MixinTargetContext mixinTargetContext) {
    }
    
    @Override
    protected void prepareInjections(final MixinTargetContext mixinTargetContext) {
        for (final MethodNode methodNode : this.targetClass.methods) {
            try {
                final InjectionInfo parse = InjectionInfo.parse(mixinTargetContext, methodNode);
                if (parse != null) {
                    throw new InvalidInterfaceMixinException(mixinTargetContext, parse + " is not supported on interface mixin method " + methodNode.name);
                }
                continue;
            }
            catch (InvalidInjectionException ex) {
                throw new InvalidInterfaceMixinException(mixinTargetContext, ((ex.getInjectionInfo() != null) ? ex.getInjectionInfo().toString() : "Injection") + " is not supported in interface mixin");
            }
        }
    }
    
    @Override
    protected void applyFields(final MixinTargetContext mixinTargetContext) {
        final Iterator<Map.Entry<FieldNode, ClassInfo.Field>> iterator = mixinTargetContext.getShadowFields().iterator();
        while (iterator.hasNext()) {
            final FieldNode fieldNode = iterator.next().getKey();
            this.logger.error("Ignoring redundant @Shadow field {}:{} in {}", new Object[] { fieldNode.name, fieldNode.desc, mixinTargetContext });
        }
        this.mergeNewFields(mixinTargetContext);
    }
    
    @Override
    protected void applyInitialisers(final MixinTargetContext mixinTargetContext) {
    }
    
    MixinApplicatorInterface(final TargetClassContext targetClassContext) {
        super(targetClassContext);
    }
    
    @Override
    protected void applyInterfaces(final MixinTargetContext mixinTargetContext) {
        for (final String anObject : mixinTargetContext.getInterfaces()) {
            if (!this.targetClass.name.equals(anObject) && !this.targetClass.interfaces.contains(anObject)) {
                this.targetClass.interfaces.add(anObject);
                mixinTargetContext.getTargetClassInfo().addInterface(anObject);
            }
        }
    }
}
