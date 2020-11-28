// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;

abstract class ScanResultObject
{
    private transient /* synthetic */ ClassInfo classInfo;
    protected transient /* synthetic */ Class<?> classRef;
    protected transient /* synthetic */ ScanResult scanResult;
    
     <T> Class<T> loadClass(final Class<T> clazz, final boolean b) {
        if (this.classRef == null) {
            final String classInfoNameOrClassName = this.getClassInfoNameOrClassName();
            if (this.scanResult != null) {
                this.classRef = this.scanResult.loadClass(classInfoNameOrClassName, (Class<?>)clazz, b);
            }
            else {
                try {
                    this.classRef = Class.forName(classInfoNameOrClassName);
                }
                catch (Throwable cause) {
                    if (!b) {
                        throw new IllegalArgumentException("Could not load class " + classInfoNameOrClassName, cause);
                    }
                }
            }
        }
        return (Class<T>)this.classRef;
    }
    
     <T> Class<T> loadClass(final Class<T> clazz) {
        return this.loadClass(clazz, false);
    }
    
    void setScanResult(final ScanResult scanResult) {
        this.scanResult = scanResult;
    }
    
    ClassInfo getClassInfo() {
        if (this.classInfo == null) {
            if (this.scanResult == null) {
                return null;
            }
            final String className = this.getClassName();
            if (className == null) {
                throw new IllegalArgumentException("Class name is not set");
            }
            this.classInfo = this.scanResult.getClassInfo(className);
        }
        return this.classInfo;
    }
    
    protected abstract String getClassName();
    
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final ClassInfo classInfo = this.getClassInfo();
        if (classInfo != null) {
            set.add(classInfo);
        }
    }
    
    private String getClassInfoNameOrClassName() {
        ClassInfo classInfo = null;
        try {
            classInfo = this.getClassInfo();
        }
        catch (IllegalArgumentException ex) {}
        if (classInfo == null) {
            classInfo = this.classInfo;
        }
        String s;
        if (classInfo != null) {
            s = classInfo.getName();
        }
        else {
            s = this.getClassName();
        }
        if (s == null) {
            throw new IllegalArgumentException("Class name is not set");
        }
        return s;
    }
    
    Class<?> loadClass() {
        return this.loadClass(false);
    }
    
    Class<?> loadClass(final boolean b) {
        if (this.classRef == null) {
            final String classInfoNameOrClassName = this.getClassInfoNameOrClassName();
            if (this.scanResult != null) {
                this.classRef = this.scanResult.loadClass(classInfoNameOrClassName, b);
            }
            else {
                try {
                    this.classRef = Class.forName(classInfoNameOrClassName);
                }
                catch (Throwable cause) {
                    if (!b) {
                        throw new IllegalArgumentException("Could not load class " + classInfoNameOrClassName, cause);
                    }
                }
            }
        }
        return this.classRef;
    }
    
    final Set<ClassInfo> findReferencedClassInfo() {
        final LinkedHashSet<ClassInfo> set = new LinkedHashSet<ClassInfo>();
        if (this.scanResult != null) {
            this.findReferencedClassInfo(this.scanResult.classNameToClassInfo, set);
        }
        return set;
    }
}
