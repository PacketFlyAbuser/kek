// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import com.google.common.base.Strings;
import org.spongepowered.asm.mixin.injection.callback.CallbackInjector;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.AnnotationNode;

public class CallbackInjectionInfo extends InjectionInfo
{
    @Override
    protected Injector parseInjector(final AnnotationNode annotationNode) {
        return new CallbackInjector(this, Annotations.getValue(annotationNode, "cancellable", Boolean.FALSE), Annotations.getValue(annotationNode, "locals", LocalCapture.class, LocalCapture.NO_CAPTURE), Annotations.getValue(annotationNode, "id", ""));
    }
    
    @Override
    public String getSliceId(final String s) {
        return Strings.nullToEmpty(s);
    }
    
    protected CallbackInjectionInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final AnnotationNode annotationNode) {
        super(mixinTargetContext, methodNode, annotationNode);
    }
}
