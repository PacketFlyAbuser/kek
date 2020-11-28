// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.lang.reflect.Field;

public class AnnotationEnumValue extends ScanResultObject implements Comparable<AnnotationEnumValue>
{
    private /* synthetic */ String valueName;
    private /* synthetic */ String className;
    
    AnnotationEnumValue() {
    }
    
    public Object loadClassAndReturnEnumValue() throws IllegalArgumentException {
        return this.loadClassAndReturnEnumValue(false);
    }
    
    @Override
    public int hashCode() {
        return this.className.hashCode() * 11 + this.valueName.hashCode();
    }
    
    public String getName() {
        return this.className + "." + this.valueName;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public Object loadClassAndReturnEnumValue(final boolean b) throws IllegalArgumentException {
        final Class<?> loadClass = super.loadClass(b);
        if (loadClass == null) {
            if (b) {
                return null;
            }
            throw new IllegalArgumentException("Enum class " + this.className + " could not be loaded");
        }
        else {
            if (!loadClass.isEnum()) {
                throw new IllegalArgumentException("Class " + this.className + " is not an enum");
            }
            Field declaredField;
            try {
                declaredField = loadClass.getDeclaredField(this.valueName);
            }
            catch (ReflectiveOperationException | SecurityException ex) {
                final Object cause;
                throw new IllegalArgumentException("Could not find enum constant " + this.toString(), (Throwable)cause);
            }
            if (!declaredField.isEnumConstant()) {
                throw new IllegalArgumentException("Field " + this.toString() + " is not an enum constant");
            }
            try {
                return declaredField.get(null);
            }
            catch (ReflectiveOperationException | SecurityException ex2) {
                final Object cause2;
                throw new IllegalArgumentException("Field " + this.toString() + " is not accessible", (Throwable)cause2);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.className + "." + this.valueName;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof AnnotationEnumValue && this.compareTo((AnnotationEnumValue)o) == 0);
    }
    
    public String getValueName() {
        return this.valueName;
    }
    
    AnnotationEnumValue(final String className, final String valueName) {
        this.className = className;
        this.valueName = valueName;
    }
    
    @Override
    public int compareTo(final AnnotationEnumValue annotationEnumValue) {
        final int compareTo = this.className.compareTo(annotationEnumValue.className);
        return (compareTo == 0) ? this.valueName.compareTo(annotationEnumValue.valueName) : compareTo;
    }
}
