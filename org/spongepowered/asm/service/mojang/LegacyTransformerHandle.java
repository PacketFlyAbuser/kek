// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service.mojang;

import javax.annotation.Resource;
import net.minecraft.launchwrapper.IClassTransformer;
import org.spongepowered.asm.service.ILegacyClassTransformer;

class LegacyTransformerHandle implements ILegacyClassTransformer
{
    private final /* synthetic */ IClassTransformer transformer;
    
    @Override
    public byte[] transformClassBytes(final String s, final String s2, final byte[] array) {
        return this.transformer.transform(s, s2, array);
    }
    
    @Override
    public String getName() {
        return this.transformer.getClass().getName();
    }
    
    @Override
    public boolean isDelegationExcluded() {
        return this.transformer.getClass().getAnnotation(Resource.class) != null;
    }
    
    LegacyTransformerHandle(final IClassTransformer transformer) {
        this.transformer = transformer;
    }
}
