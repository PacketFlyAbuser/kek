// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.Iterator;
import java.util.HashMap;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import java.util.Map;

public class InjectorTarget
{
    private final /* synthetic */ ISliceContext context;
    private final /* synthetic */ Map<String, ReadOnlyInsnList> cache;
    private final /* synthetic */ Target target;
    
    public MethodNode getMethod() {
        return this.target.method;
    }
    
    public InsnList getSlice(final InjectionPoint injectionPoint) {
        return this.getSlice(injectionPoint.getSlice());
    }
    
    public Target getTarget() {
        return this.target;
    }
    
    public InjectorTarget(final ISliceContext context, final Target target) {
        this.cache = new HashMap<String, ReadOnlyInsnList>();
        this.context = context;
        this.target = target;
    }
    
    public InsnList getSlice(final String s) {
        ReadOnlyInsnList slice = this.cache.get(s);
        if (slice == null) {
            final MethodSlice slice2 = this.context.getSlice(s);
            if (slice2 != null) {
                slice = slice2.getSlice(this.target.method);
            }
            else {
                slice = new ReadOnlyInsnList(this.target.method.instructions);
            }
            this.cache.put(s, slice);
        }
        return slice;
    }
    
    public void dispose() {
        final Iterator<ReadOnlyInsnList> iterator = this.cache.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().dispose();
        }
        this.cache.clear();
    }
}
