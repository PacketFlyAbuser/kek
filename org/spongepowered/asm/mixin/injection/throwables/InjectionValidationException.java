// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.throwables;

import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo;

public class InjectionValidationException extends Exception
{
    private final /* synthetic */ InjectorGroupInfo group;
    
    public InjectionValidationException(final InjectorGroupInfo group, final String message) {
        super(message);
        this.group = group;
    }
    
    public InjectorGroupInfo getGroup() {
        return this.group;
    }
}
