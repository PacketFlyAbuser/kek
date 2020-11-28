// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Iterator;
import java.util.Collection;

public class ModuleInfoList extends MappableInfoList<ModuleInfo>
{
    ModuleInfoList(final Collection<ModuleInfo> collection) {
        super(collection);
    }
    
    ModuleInfoList() {
    }
    
    ModuleInfoList(final int n) {
        super(n);
    }
    
    public ModuleInfoList filter(final ModuleInfoFilter moduleInfoFilter) {
        final ModuleInfoList list = new ModuleInfoList();
        for (final ModuleInfo moduleInfo : this) {
            if (moduleInfoFilter.accept(moduleInfo)) {
                list.add(moduleInfo);
            }
        }
        return list;
    }
    
    @FunctionalInterface
    public interface ModuleInfoFilter
    {
        boolean accept(final ModuleInfo p0);
    }
}
