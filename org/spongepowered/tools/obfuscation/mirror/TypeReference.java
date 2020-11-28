// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.TypeElement;
import javax.annotation.processing.ProcessingEnvironment;
import java.io.Serializable;

public class TypeReference implements Serializable, Comparable<TypeReference>
{
    private transient /* synthetic */ TypeHandle handle;
    private final /* synthetic */ String name;
    
    public TypeReference(final TypeHandle handle) {
        this.name = handle.getName();
        this.handle = handle;
    }
    
    public String getName() {
        return this.name;
    }
    
    public TypeHandle getHandle(final ProcessingEnvironment processingEnvironment) {
        if (this.handle == null) {
            final TypeElement typeElement = processingEnvironment.getElementUtils().getTypeElement(this.getClassName());
            try {
                this.handle = new TypeHandle(typeElement);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this.handle;
    }
    
    public TypeReference(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof TypeReference && this.compareTo((TypeReference)o) == 0;
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public String getClassName() {
        return this.name.replace('/', '.');
    }
    
    @Override
    public String toString() {
        return String.format("TypeReference[%s]", this.name);
    }
    
    @Override
    public int compareTo(final TypeReference typeReference) {
        return (typeReference == null) ? -1 : this.name.compareTo(typeReference.name);
    }
}
