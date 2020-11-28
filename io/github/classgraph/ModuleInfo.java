// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.List;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.Iterator;
import java.net.URI;
import java.util.Set;

public class ModuleInfo implements Comparable<ModuleInfo>, HasName
{
    private transient /* synthetic */ ClasspathElement classpathElement;
    private /* synthetic */ AnnotationInfoList annotationInfo;
    private /* synthetic */ Set<AnnotationInfo> annotationInfoSet;
    private transient /* synthetic */ URI locationURI;
    private /* synthetic */ Set<ClassInfo> classInfoSet;
    private /* synthetic */ Set<PackageInfo> packageInfoSet;
    private /* synthetic */ String name;
    private transient /* synthetic */ ModuleRef moduleRef;
    
    public URI getLocation() {
        if (this.locationURI == null) {
            this.locationURI = ((this.moduleRef != null) ? this.moduleRef.getLocation() : null);
            if (this.locationURI == null) {
                this.locationURI = this.classpathElement.getURI();
            }
        }
        return this.locationURI;
    }
    
    public ModuleRef getModuleRef() {
        return this.moduleRef;
    }
    
    public ClassInfo getClassInfo(final String anObject) {
        for (final ClassInfo classInfo : this.classInfoSet) {
            if (classInfo.getName().equals(anObject)) {
                return classInfo;
            }
        }
        return null;
    }
    
    ModuleInfo() {
    }
    
    public PackageInfo getPackageInfo(final String anObject) {
        if (this.packageInfoSet == null) {
            return null;
        }
        for (final PackageInfo packageInfo : this.packageInfoSet) {
            if (packageInfo.getName().equals(anObject)) {
                return packageInfo;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int compareTo(final ModuleInfo moduleInfo) {
        final int compareTo = this.name.compareTo(moduleInfo.name);
        if (compareTo != 0) {
            return compareTo;
        }
        final URI location = this.getLocation();
        final URI location2 = moduleInfo.getLocation();
        if (location != null && location2 != null) {
            return location.compareTo(location2);
        }
        return 0;
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
    
    ModuleInfo(final ModuleRef moduleRef, final ClasspathElement classpathElement) {
        this.moduleRef = moduleRef;
        this.classpathElement = classpathElement;
        this.name = classpathElement.getModuleName();
    }
    
    public PackageInfoList getPackageInfo() {
        if (this.packageInfoSet == null) {
            return new PackageInfoList(1);
        }
        final PackageInfoList list = new PackageInfoList(this.packageInfoSet);
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
        return list;
    }
    
    void addClassInfo(final ClassInfo classInfo) {
        if (this.classInfoSet == null) {
            this.classInfoSet = new HashSet<ClassInfo>();
        }
        this.classInfoSet.add(classInfo);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    void addPackageInfo(final PackageInfo packageInfo) {
        if (this.packageInfoSet == null) {
            this.packageInfoSet = new HashSet<PackageInfo>();
        }
        this.packageInfoSet.add(packageInfo);
    }
    
    public ClassInfoList getClassInfo() {
        return new ClassInfoList(this.classInfoSet, true);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ModuleInfo && this.compareTo((ModuleInfo)o) == 0);
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
}
