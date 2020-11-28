// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;

public class FieldInfoList extends MappableInfoList<FieldInfo>
{
    static final /* synthetic */ FieldInfoList EMPTY_LIST;
    
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassInfo(map, set);
        }
    }
    
    public FieldInfoList(final int n) {
        super(n);
    }
    
    static {
        (EMPTY_LIST = new FieldInfoList()).makeUnmodifiable();
    }
    
    public FieldInfoList filter(final FieldInfoFilter fieldInfoFilter) {
        final FieldInfoList list = new FieldInfoList();
        for (final FieldInfo fieldInfo : this) {
            if (fieldInfoFilter.accept(fieldInfo)) {
                list.add(fieldInfo);
            }
        }
        return list;
    }
    
    public FieldInfoList(final Collection<FieldInfo> collection) {
        super(collection);
    }
    
    public static FieldInfoList emptyList() {
        return FieldInfoList.EMPTY_LIST;
    }
    
    public FieldInfoList() {
    }
    
    @FunctionalInterface
    public interface FieldInfoFilter
    {
        boolean accept(final FieldInfo p0);
    }
}
