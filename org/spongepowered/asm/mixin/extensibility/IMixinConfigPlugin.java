// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.extensibility;

import java.util.Set;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.List;

public interface IMixinConfigPlugin
{
    List<String> getMixins();
    
    String getRefMapperConfig();
    
    void preApply(final String p0, final ClassNode p1, final String p2, final IMixinInfo p3);
    
    void postApply(final String p0, final ClassNode p1, final String p2, final IMixinInfo p3);
    
    void acceptTargets(final Set<String> p0, final Set<String> p1);
    
    void onLoad(final String p0);
    
    boolean shouldApplyMixin(final String p0, final String p1);
}
