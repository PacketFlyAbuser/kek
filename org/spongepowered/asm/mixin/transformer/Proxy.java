// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.List;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

public final class Proxy implements IClassTransformer, ILegacyClassTransformer
{
    private /* synthetic */ boolean isActive;
    private static /* synthetic */ List<Proxy> proxies;
    private static /* synthetic */ MixinTransformer transformer;
    
    public String getName() {
        return this.getClass().getName();
    }
    
    public boolean isDelegationExcluded() {
        return true;
    }
    
    public byte[] transformClassBytes(final String s, final String s2, final byte[] array) {
        if (this.isActive) {
            return Proxy.transformer.transformClassBytes(s, s2, array);
        }
        return array;
    }
    
    static {
        Proxy.proxies = new ArrayList<Proxy>();
        Proxy.transformer = new MixinTransformer();
    }
    
    public Proxy() {
        this.isActive = true;
        final Iterator<Proxy> iterator = Proxy.proxies.iterator();
        while (iterator.hasNext()) {
            iterator.next().isActive = false;
        }
        Proxy.proxies.add(this);
        LogManager.getLogger("mixin").debug("Adding new mixin transformer proxy #{}", new Object[] { Proxy.proxies.size() });
    }
    
    public byte[] transform(final String s, final String s2, final byte[] array) {
        if (this.isActive) {
            return Proxy.transformer.transformClassBytes(s, s2, array);
        }
        return array;
    }
}
