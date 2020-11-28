// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.obfuscation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public class RemapperChain implements IRemapper
{
    private final /* synthetic */ List<IRemapper> remappers;
    
    @Override
    public String toString() {
        return String.format("RemapperChain[%d]", this.remappers.size());
    }
    
    @Override
    public String mapMethodName(final String s, String anObject, final String s2) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String mapMethodName = iterator.next().mapMethodName(s, anObject, s2);
            if (mapMethodName != null && !mapMethodName.equals(anObject)) {
                anObject = mapMethodName;
            }
        }
        return anObject;
    }
    
    @Override
    public String mapFieldName(final String s, String anObject, final String s2) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String mapFieldName = iterator.next().mapFieldName(s, anObject, s2);
            if (mapFieldName != null && !mapFieldName.equals(anObject)) {
                anObject = mapFieldName;
            }
        }
        return anObject;
    }
    
    @Override
    public String unmap(String anObject) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String unmap = iterator.next().unmap(anObject);
            if (unmap != null && !unmap.equals(anObject)) {
                anObject = unmap;
            }
        }
        return anObject;
    }
    
    @Override
    public String map(String anObject) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String map = iterator.next().map(anObject);
            if (map != null && !map.equals(anObject)) {
                anObject = map;
            }
        }
        return anObject;
    }
    
    public RemapperChain() {
        this.remappers = new ArrayList<IRemapper>();
    }
    
    @Override
    public String mapDesc(String anObject) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String mapDesc = iterator.next().mapDesc(anObject);
            if (mapDesc != null && !mapDesc.equals(anObject)) {
                anObject = mapDesc;
            }
        }
        return anObject;
    }
    
    @Override
    public String unmapDesc(String anObject) {
        final Iterator<IRemapper> iterator = this.remappers.iterator();
        while (iterator.hasNext()) {
            final String unmapDesc = iterator.next().unmapDesc(anObject);
            if (unmapDesc != null && !unmapDesc.equals(anObject)) {
                anObject = unmapDesc;
            }
        }
        return anObject;
    }
    
    public RemapperChain add(final IRemapper remapper) {
        this.remappers.add(remapper);
        return this;
    }
}
