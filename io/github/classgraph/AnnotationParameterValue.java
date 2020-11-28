// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Set;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.Objects;

public class AnnotationParameterValue extends ScanResultObject implements HasName, Comparable<AnnotationParameterValue>
{
    private /* synthetic */ ObjectTypedValueWrapper value;
    private /* synthetic */ String name;
    
    private String toStringParamValueOnly() {
        final StringBuilder sb = new StringBuilder();
        this.toStringParamValueOnly(sb);
        return sb.toString();
    }
    
    @Override
    protected String getClassName() {
        throw new IllegalArgumentException("getClassName() cannot be called here");
    }
    
    protected ClassInfo getClassInfo() {
        throw new IllegalArgumentException("getClassInfo() cannot be called here");
    }
    
    void toString(final StringBuilder sb) {
        sb.append(this.name);
        sb.append("=");
        this.toStringParamValueOnly(sb);
    }
    
    AnnotationParameterValue(final String name, final Object o) {
        this.name = name;
        this.value = new ObjectTypedValueWrapper(o);
    }
    
    void setValue(final Object o) {
        this.value = new ObjectTypedValueWrapper(o);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.value);
    }
    
    public Object getValue() {
        return (this.value == null) ? null : this.value.get();
    }
    
    Object instantiate(final ClassInfo classInfo) {
        return this.value.instantiateOrGet(classInfo, this.name);
    }
    
    @Override
    public int compareTo(final AnnotationParameterValue annotationParameterValue) {
        if (annotationParameterValue == this) {
            return 0;
        }
        final int compareTo = this.name.compareTo(annotationParameterValue.getName());
        if (compareTo != 0) {
            return compareTo;
        }
        if (this.value.equals(annotationParameterValue.value)) {
            return 0;
        }
        final Object value = this.getValue();
        final Object value2 = annotationParameterValue.getValue();
        return (value == null || value2 == null) ? ((value != null) - (value2 != null)) : this.toStringParamValueOnly().compareTo(annotationParameterValue.toStringParamValueOnly());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.toString(sb);
        return sb.toString();
    }
    
    AnnotationParameterValue() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AnnotationParameterValue)) {
            return false;
        }
        final AnnotationParameterValue annotationParameterValue = (AnnotationParameterValue)o;
        return this.name.equals(annotationParameterValue.name) && this.value == null == (annotationParameterValue.value == null) && (this.value == null || this.value.equals(annotationParameterValue.value));
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    void toStringParamValueOnly(final StringBuilder sb) {
        if (this.value == null) {
            sb.append("null");
        }
        else {
            final Object value = this.value.get();
            if (value.getClass().isArray()) {
                sb.append('{');
                for (int i = 0; i < Array.getLength(value); ++i) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    final Object value2 = Array.get(value, i);
                    sb.append((value2 == null) ? "null" : value2.toString());
                }
                sb.append('}');
            }
            else if (value instanceof String) {
                sb.append('\"');
                sb.append(value.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r"));
                sb.append('\"');
            }
            else if (value instanceof Character) {
                sb.append('\'');
                sb.append(value.toString().replace("'", "\\'").replace("\n", "\\n").replace("\r", "\\r"));
                sb.append('\'');
            }
            else {
                sb.append(value.toString());
            }
        }
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.value != null) {
            this.value.setScanResult(scanResult);
        }
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        if (this.value != null) {
            this.value.findReferencedClassInfo(map, set);
        }
    }
    
    void convertWrapperArraysToPrimitiveArrays(final ClassInfo classInfo) {
        if (this.value != null) {
            this.value.convertWrapperArraysToPrimitiveArrays(classInfo, this.name);
        }
    }
}
