// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

public class MethodInfoList extends InfoList<MethodInfo>
{
    static final /* synthetic */ MethodInfoList EMPTY_LIST;
    
    public boolean containsName(final String anObject) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(anObject)) {
                return true;
            }
        }
        return false;
    }
    
    public MethodInfoList get(final String s) {
        boolean b = false;
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(s)) {
                b = true;
                break;
            }
        }
        if (!b) {
            return MethodInfoList.EMPTY_LIST;
        }
        final MethodInfoList list = new MethodInfoList(2);
        for (final MethodInfo methodInfo : this) {
            if (methodInfo.getName().equals(s)) {
                list.add(methodInfo);
            }
        }
        return list;
    }
    
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().findReferencedClassInfo(map, set);
        }
    }
    
    public Map<String, MethodInfoList> asMap() {
        final HashMap<String, MethodInfoList> hashMap = (HashMap<String, MethodInfoList>)new HashMap<Object, MethodInfoList>();
        for (final MethodInfo methodInfo : this) {
            final String name = methodInfo.getName();
            MethodInfoList list = hashMap.get(name);
            if (list == null) {
                list = new MethodInfoList(1);
                hashMap.put(name, list);
            }
            list.add(methodInfo);
        }
        return hashMap;
    }
    
    public MethodInfoList(final Collection<MethodInfo> collection) {
        super(collection);
    }
    
    public MethodInfoList() {
    }
    
    static {
        (EMPTY_LIST = new MethodInfoList()).makeUnmodifiable();
    }
    
    public MethodInfoList(final int n) {
        super(n);
    }
    
    public MethodInfoList filter(final MethodInfoFilter methodInfoFilter) {
        final MethodInfoList list = new MethodInfoList();
        for (final MethodInfo methodInfo : this) {
            if (methodInfoFilter.accept(methodInfo)) {
                list.add(methodInfo);
            }
        }
        return list;
    }
    
    public MethodInfo getSingleMethod(final String s) {
        int n = 0;
        MethodInfo methodInfo = null;
        for (final MethodInfo methodInfo2 : this) {
            if (methodInfo2.getName().equals(s)) {
                ++n;
                methodInfo = methodInfo2;
            }
        }
        if (n == 0) {
            return null;
        }
        if (n == 1) {
            return methodInfo;
        }
        throw new IllegalArgumentException("There are multiple methods named \"" + s + "\" in class " + this.iterator().next().getName());
    }
    
    public static MethodInfoList emptyList() {
        return MethodInfoList.EMPTY_LIST;
    }
    
    @FunctionalInterface
    public interface MethodInfoFilter
    {
        boolean accept(final MethodInfo p0);
    }
}
