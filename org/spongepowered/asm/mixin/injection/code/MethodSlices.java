// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.Iterator;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import java.util.HashMap;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import java.util.Map;

public final class MethodSlices
{
    private final /* synthetic */ Map<String, MethodSlice> slices;
    private final /* synthetic */ InjectionInfo info;
    
    private void add(final MethodSlice obj) {
        final String sliceId = this.info.getSliceId(obj.getId());
        if (this.slices.containsKey(sliceId)) {
            throw new InvalidSliceException((ISliceContext)this.info, obj + " has a duplicate id, '" + sliceId + "' was already defined");
        }
        this.slices.put(sliceId, obj);
    }
    
    @Override
    public String toString() {
        return String.format("MethodSlices%s", this.slices.keySet());
    }
    
    private MethodSlices(final InjectionInfo info) {
        this.slices = new HashMap<String, MethodSlice>(4);
        this.info = info;
    }
    
    public MethodSlice get(final String s) {
        return this.slices.get(s);
    }
    
    public static MethodSlices parse(final InjectionInfo injectionInfo) {
        final MethodSlices methodSlices = new MethodSlices(injectionInfo);
        final AnnotationNode annotation = injectionInfo.getAnnotation();
        if (annotation != null) {
            final Iterator<AnnotationNode> iterator = Annotations.getValue(annotation, "slice", true).iterator();
            while (iterator.hasNext()) {
                methodSlices.add(MethodSlice.parse(injectionInfo, iterator.next()));
            }
        }
        return methodSlices;
    }
}
