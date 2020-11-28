// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Iterator;
import java.util.Collection;

public class PackageInfoList extends MappableInfoList<PackageInfo>
{
    PackageInfoList(final int n) {
        super(n);
    }
    
    PackageInfoList(final Collection<PackageInfo> collection) {
        super(collection);
    }
    
    public PackageInfoList filter(final PackageInfoFilter packageInfoFilter) {
        final PackageInfoList list = new PackageInfoList();
        for (final PackageInfo packageInfo : this) {
            if (packageInfoFilter.accept(packageInfo)) {
                list.add(packageInfo);
            }
        }
        return list;
    }
    
    PackageInfoList() {
    }
    
    static {
        EMPTY_LIST = new PackageInfoList() {
            @Override
            public boolean addAll(final int n, final Collection<? extends PackageInfo> collection) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public boolean remove(final Object o) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public void clear() {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public PackageInfo remove(final int n) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public boolean retainAll(final Collection<?> collection) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public boolean removeAll(final Collection<?> collection) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public boolean addAll(final Collection<? extends PackageInfo> collection) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public void add(final int n, final PackageInfo packageInfo) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public PackageInfo set(final int n, final PackageInfo packageInfo) {
                throw new IllegalArgumentException("List is immutable");
            }
            
            @Override
            public boolean add(final PackageInfo packageInfo) {
                throw new IllegalArgumentException("List is immutable");
            }
        };
    }
    
    @FunctionalInterface
    public interface PackageInfoFilter
    {
        boolean accept(final PackageInfo p0);
    }
}
