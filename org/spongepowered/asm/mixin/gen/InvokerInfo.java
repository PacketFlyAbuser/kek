// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.Type;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;

public class InvokerInfo extends AccessorInfo
{
    @Override
    protected MemberInfo initTarget() {
        return new MemberInfo(this.getTargetName(), null, this.method.desc);
    }
    
    public InvokerInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        super(mixinTargetContext, methodNode, Invoker.class);
    }
    
    @Override
    protected Type initTargetFieldType() {
        return null;
    }
    
    @Override
    protected AccessorType initType() {
        return AccessorType.METHOD_PROXY;
    }
    
    @Override
    public void locate() {
        this.targetMethod = this.findTargetMethod();
    }
    
    private MethodNode findTargetMethod() {
        return this.findTarget(this.classNode.methods);
    }
}
