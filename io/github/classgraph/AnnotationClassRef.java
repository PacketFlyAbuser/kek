// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.types.ParseException;

public class AnnotationClassRef extends ScanResultObject
{
    private /* synthetic */ String typeDescriptorStr;
    private transient /* synthetic */ String className;
    private transient /* synthetic */ TypeSignature typeSignature;
    
    public ClassInfo getClassInfo() {
        this.getTypeSignature();
        return this.typeSignature.getClassInfo();
    }
    
    @Override
    protected String getClassName() {
        if (this.className == null) {
            this.getTypeSignature();
            if (this.typeSignature instanceof BaseTypeSignature) {
                this.className = ((BaseTypeSignature)this.typeSignature).getTypeStr();
            }
            else if (this.typeSignature instanceof ClassRefTypeSignature) {
                this.className = ((ClassRefTypeSignature)this.typeSignature).getFullyQualifiedClassName();
            }
            else {
                if (!(this.typeSignature instanceof ArrayTypeSignature)) {
                    throw new IllegalArgumentException("Got unexpected type " + this.typeSignature.getClass().getName() + " for ref type signature: " + this.typeDescriptorStr);
                }
                this.className = ((ArrayTypeSignature)this.typeSignature).getClassName();
            }
        }
        return this.className;
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
    }
    
    public String getName() {
        return this.getClassName();
    }
    
    private TypeSignature getTypeSignature() {
        if (this.typeSignature == null) {
            try {
                this.typeSignature = TypeSignature.parse(this.typeDescriptorStr, null);
                this.typeSignature.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeSignature;
    }
    
    AnnotationClassRef(final String typeDescriptorStr) {
        this.typeDescriptorStr = typeDescriptorStr;
    }
    
    public Class<?> loadClass() {
        return this.loadClass(false);
    }
    
    public Class<?> loadClass(final boolean b) {
        this.getTypeSignature();
        if (this.typeSignature instanceof BaseTypeSignature) {
            return ((BaseTypeSignature)this.typeSignature).getType();
        }
        if (this.typeSignature instanceof ClassRefTypeSignature) {
            return ((ClassRefTypeSignature)this.typeSignature).loadClass(b);
        }
        if (this.typeSignature instanceof ArrayTypeSignature) {
            return ((ArrayTypeSignature)this.typeSignature).loadClass(b);
        }
        throw new IllegalArgumentException("Got unexpected type " + this.typeSignature.getClass().getName() + " for ref type signature: " + this.typeDescriptorStr);
    }
    
    @Override
    public String toString() {
        if (this.scanResult != null) {
            final ClassInfo classInfo = this.getClassInfo();
            if (classInfo == null || classInfo.isInterfaceOrAnnotation()) {}
        }
        return this.getTypeSignature().toString() + ".class";
    }
    
    @Override
    public int hashCode() {
        return this.getTypeSignature().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AnnotationClassRef && this.getTypeSignature().equals(((AnnotationClassRef)o).getTypeSignature()));
    }
    
    AnnotationClassRef() {
    }
}
