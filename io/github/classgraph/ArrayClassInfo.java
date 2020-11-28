// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Set;
import java.util.Map;

public class ArrayClassInfo extends ClassInfo
{
    private /* synthetic */ ArrayTypeSignature arrayTypeSignature;
    private /* synthetic */ ClassInfo elementClassInfo;
    
    public Class<?> loadElementClass() {
        return this.arrayTypeSignature.loadElementClass();
    }
    
    @Override
    public Class<?> loadClass() {
        if (this.classRef == null) {
            this.classRef = this.arrayTypeSignature.loadClass();
        }
        return this.classRef;
    }
    
    public ClassInfo getElementClassInfo() {
        if (this.elementClassInfo == null && !(this.arrayTypeSignature.getElementTypeSignature() instanceof BaseTypeSignature)) {
            this.elementClassInfo = this.arrayTypeSignature.getElementTypeSignature().getClassInfo();
            if (this.elementClassInfo != null) {
                this.classpathElement = this.elementClassInfo.classpathElement;
                this.classfileResource = this.elementClassInfo.classfileResource;
                this.classLoader = this.elementClassInfo.classLoader;
                this.isScannedClass = this.elementClassInfo.isScannedClass;
                this.isExternalClass = this.elementClassInfo.isExternalClass;
                this.moduleInfo = this.elementClassInfo.moduleInfo;
                this.packageInfo = this.elementClassInfo.packageInfo;
            }
        }
        return this.elementClassInfo;
    }
    
    @Override
    public Class<?> loadClass(final boolean b) {
        if (this.classRef == null) {
            this.classRef = this.arrayTypeSignature.loadClass(b);
        }
        return this.classRef;
    }
    
    @Override
    public ClassTypeSignature getTypeSignature() {
        return null;
    }
    
    public ArrayTypeSignature getArrayTypeSignature() {
        return this.arrayTypeSignature;
    }
    
    public Class<?> loadElementClass(final boolean b) {
        return this.arrayTypeSignature.loadElementClass(b);
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
    }
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public TypeSignature getElementTypeSignature() {
        return this.arrayTypeSignature.getElementTypeSignature();
    }
    
    ArrayClassInfo(final ArrayTypeSignature arrayTypeSignature) {
        super(arrayTypeSignature.getClassName(), 0, null);
        this.arrayTypeSignature = arrayTypeSignature;
        this.getElementClassInfo();
    }
    
    @Override
    public String getTypeSignatureStr() {
        return this.arrayTypeSignature.getTypeSignatureStr();
    }
    
    ArrayClassInfo() {
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        super.findReferencedClassInfo(map, set);
    }
    
    public int getNumDimensions() {
        return this.arrayTypeSignature.getNumDimensions();
    }
}
