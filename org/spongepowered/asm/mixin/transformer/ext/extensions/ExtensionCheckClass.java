// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.ext.extensions;

import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.util.CheckClassAdapter;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionCheckClass implements IExtension
{
    @Override
    public void export(final MixinEnvironment mixinEnvironment, final String s, final boolean b, final byte[] array) {
    }
    
    @Override
    public void postApply(final ITargetClassContext targetClassContext) {
        try {
            targetClassContext.getClassNode().accept(new CheckClassAdapter(new MixinClassWriter(2)));
        }
        catch (RuntimeException ex) {
            throw new ValidationFailedException(ex.getMessage(), ex);
        }
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment mixinEnvironment) {
        return mixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_VERIFY);
    }
    
    @Override
    public void preApply(final ITargetClassContext targetClassContext) {
    }
    
    public static class ValidationFailedException extends MixinException
    {
        public ValidationFailedException(final Throwable t) {
            super(t);
        }
        
        public ValidationFailedException(final String s, final Throwable t) {
            super(s, t);
        }
        
        public ValidationFailedException(final String s) {
            super(s);
        }
    }
}
