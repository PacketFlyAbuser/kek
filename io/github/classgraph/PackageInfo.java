// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.List;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Comparator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PackageInfo implements Comparable<PackageInfo>, HasName
{
    private /* synthetic */ PackageInfo parent;
    private /* synthetic */ AnnotationInfoList annotationInfo;
    private /* synthetic */ Set<AnnotationInfo> annotationInfoSet;
    private /* synthetic */ Set<PackageInfo> children;
    private /* synthetic */ Map<String, ClassInfo> memberClassNameToClassInfo;
    private /* synthetic */ String name;
    
    void addClassInfo(final ClassInfo classInfo) {
        if (this.memberClassNameToClassInfo == null) {
            this.memberClassNameToClassInfo = new HashMap<String, ClassInfo>();
        }
        this.memberClassNameToClassInfo.put(classInfo.getName(), classInfo);
    }
    
    public ClassInfoList getClassInfoRecursive() {
        final HashSet<ClassInfo> set = new HashSet<ClassInfo>();
        this.obtainClassInfoRecursive(set);
        return new ClassInfoList(set, true);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    public ClassInfo getClassInfo(final String s) {
        return (this.memberClassNameToClassInfo == null) ? null : this.memberClassNameToClassInfo.get(s);
    }
    
    public ClassInfoList getClassInfo() {
        return (this.memberClassNameToClassInfo == null) ? ClassInfoList.EMPTY_LIST : new ClassInfoList(new HashSet<ClassInfo>(this.memberClassNameToClassInfo.values()), true);
    }
    
    public PackageInfoList getChildren() {
        if (this.children == null) {
            return PackageInfoList.EMPTY_LIST;
        }
        final PackageInfoList list = new PackageInfoList(this.children);
        CollectionUtils.sortIfNotEmpty((List<Object>)list, (Comparator<? super Object>)new Comparator<PackageInfo>() {
            @Override
            public int compare(final PackageInfo packageInfo, final PackageInfo packageInfo2) {
                return packageInfo.name.compareTo(packageInfo2.name);
            }
        });
        return list;
    }
    
    private void obtainClassInfoRecursive(final Set<ClassInfo> set) {
        if (this.memberClassNameToClassInfo != null) {
            set.addAll(this.memberClassNameToClassInfo.values());
        }
        final Iterator iterator = this.getChildren().iterator();
        while (iterator.hasNext()) {
            iterator.next().obtainClassInfoRecursive(set);
        }
    }
    
    @Override
    public int compareTo(final PackageInfo packageInfo) {
        return this.name.compareTo(packageInfo.name);
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    static String getParentPackageName(final String s) {
        if (s.isEmpty()) {
            return null;
        }
        final int lastIndex = s.lastIndexOf(46);
        return (lastIndex < 0) ? "" : s.substring(0, lastIndex);
    }
    
    public boolean hasAnnotation(final String s) {
        return this.getAnnotationInfo().containsName(s);
    }
    
    void addAnnotations(final AnnotationInfoList list) {
        if (list != null && !list.isEmpty()) {
            if (this.annotationInfoSet == null) {
                this.annotationInfoSet = new LinkedHashSet<AnnotationInfo>();
            }
            this.annotationInfoSet.addAll(list);
        }
    }
    
    PackageInfo() {
    }
    
    PackageInfo(final String name) {
        this.name = name;
    }
    
    public AnnotationInfoList getAnnotationInfo() {
        if (this.annotationInfo == null) {
            if (this.annotationInfoSet == null) {
                this.annotationInfo = AnnotationInfoList.EMPTY_LIST;
            }
            else {
                this.annotationInfo = new AnnotationInfoList();
                this.annotationInfo.addAll(this.annotationInfoSet);
            }
        }
        return this.annotationInfo;
    }
    
    static PackageInfo getOrCreatePackage(final String s, final Map<String, PackageInfo> map) {
        final PackageInfo packageInfo = map.get(s);
        if (packageInfo != null) {
            return packageInfo;
        }
        final PackageInfo packageInfo2;
        map.put(s, packageInfo2 = new PackageInfo(s));
        if (!s.isEmpty()) {
            final PackageInfo orCreatePackage = getOrCreatePackage(getParentPackageName(packageInfo2.name), map);
            if (orCreatePackage != null) {
                if (orCreatePackage.children == null) {
                    orCreatePackage.children = new HashSet<PackageInfo>();
                }
                orCreatePackage.children.add(packageInfo2);
                packageInfo2.parent = orCreatePackage;
            }
        }
        return packageInfo2;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof PackageInfo && this.name.equals(((PackageInfo)o).name));
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public PackageInfo getParent() {
        return this.parent;
    }
}
