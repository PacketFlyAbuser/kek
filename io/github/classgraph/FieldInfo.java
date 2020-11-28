// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Map;
import nonapi.io.github.classgraph.types.ParseException;
import java.lang.reflect.Modifier;
import java.util.Set;
import nonapi.io.github.classgraph.types.TypeUtils;
import java.lang.reflect.Field;
import java.util.Iterator;

public class FieldInfo extends ScanResultObject implements Comparable<FieldInfo>, HasName
{
    private transient /* synthetic */ TypeSignature typeDescriptor;
    private /* synthetic */ String typeDescriptorStr;
    private /* synthetic */ String typeSignatureStr;
    private /* synthetic */ ObjectTypedValueWrapper constantInitializerValue;
    private transient /* synthetic */ TypeSignature typeSignature;
    private /* synthetic */ String name;
    private /* synthetic */ int modifiers;
    /* synthetic */ AnnotationInfoList annotationInfo;
    private /* synthetic */ String declaringClassName;
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
        if (this.typeDescriptor != null) {
            this.typeDescriptor.setScanResult(scanResult);
        }
        if (this.annotationInfo != null) {
            final Iterator iterator = this.annotationInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
    }
    
    public String getTypeDescriptorStr() {
        return this.typeDescriptorStr;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public TypeSignature getTypeSignatureOrTypeDescriptor() {
        final TypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            return typeSignature;
        }
        return this.getTypeDescriptor();
    }
    
    public Field loadClassAndGetField() throws IllegalArgumentException {
        try {
            return this.loadClass().getField(this.getName());
        }
        catch (NoSuchFieldException ex) {
            try {
                return this.loadClass().getDeclaredField(this.getName());
            }
            catch (NoSuchFieldException ex2) {
                throw new IllegalArgumentException("No such field: " + this.getClassName() + "." + this.getName());
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.annotationInfo != null) {
            for (final AnnotationInfo annotationInfo : this.annotationInfo) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(annotationInfo.toString());
            }
        }
        if (this.modifiers != 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.FIELD, false, sb);
        }
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(this.getTypeSignatureOrTypeDescriptor().toString());
        sb.append(' ');
        sb.append(this.name);
        if (this.constantInitializerValue != null) {
            final Object value = this.constantInitializerValue.get();
            sb.append(" = ");
            if (value instanceof String) {
                sb.append('\"').append(((String)value).replace("\\", "\\\\").replace("\"", "\\\"")).append('\"');
            }
            else if (value instanceof Character) {
                sb.append('\'').append(((Character)value).toString().replace("\\", "\\\\").replaceAll("'", "\\'")).append('\'');
            }
            else {
                sb.append(value.toString());
            }
        }
        return sb.toString();
    }
    
    void handleRepeatableAnnotations(final Set<String> set) {
        if (this.annotationInfo != null) {
            this.annotationInfo.handleRepeatableAnnotations(set, this.getClassInfo(), ClassInfo.RelType.FIELD_ANNOTATIONS, ClassInfo.RelType.CLASSES_WITH_FIELD_ANNOTATION, ClassInfo.RelType.CLASSES_WITH_NONPRIVATE_FIELD_ANNOTATION);
        }
    }
    
    public AnnotationInfoList getAnnotationInfoRepeatable(final String s) {
        return this.getAnnotationInfo().getRepeatable(s);
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }
    
    public String getModifierStr() {
        final StringBuilder sb = new StringBuilder();
        TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.FIELD, false, sb);
        return sb.toString();
    }
    
    public ClassInfo getClassInfo() {
        return super.getClassInfo();
    }
    
    @Override
    public int compareTo(final FieldInfo fieldInfo) {
        final int compareTo = this.declaringClassName.compareTo(fieldInfo.declaringClassName);
        if (compareTo != 0) {
            return compareTo;
        }
        return this.name.compareTo(fieldInfo.name);
    }
    
    public TypeSignature getTypeSignature() {
        if (this.typeSignatureStr == null) {
            return null;
        }
        if (this.typeSignature == null) {
            try {
                this.typeSignature = TypeSignature.parse(this.typeSignatureStr, this.declaringClassName);
                this.typeSignature.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeSignature;
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(this.modifiers);
    }
    
    public boolean isTransient() {
        return Modifier.isTransient(this.modifiers);
    }
    
    FieldInfo() {
    }
    
    public String getTypeSignatureOrTypeDescriptorStr() {
        if (this.typeSignatureStr != null) {
            return this.typeSignatureStr;
        }
        return this.typeDescriptorStr;
    }
    
    public boolean isFinal() {
        return Modifier.isFinal(this.modifiers);
    }
    
    @Override
    protected String getClassName() {
        return this.declaringClassName;
    }
    
    public int getModifiers() {
        return this.modifiers;
    }
    
    public Object getConstantInitializerValue() {
        if (!this.scanResult.scanSpec.enableStaticFinalFieldConstantInitializerValues) {
            throw new IllegalArgumentException("Please call ClassGraph#enableStaticFinalFieldConstantInitializerValues() before #scan()");
        }
        return (this.constantInitializerValue == null) ? null : this.constantInitializerValue.get();
    }
    
    FieldInfo(final String declaringClassName, final String name, final int modifiers, final String typeDescriptorStr, final String typeSignatureStr, final Object o, final AnnotationInfoList list) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.declaringClassName = declaringClassName;
        this.name = name;
        this.modifiers = modifiers;
        this.typeDescriptorStr = typeDescriptorStr;
        this.typeSignatureStr = typeSignatureStr;
        this.constantInitializerValue = ((o == null) ? null : new ObjectTypedValueWrapper(o));
        this.annotationInfo = ((list == null || list.isEmpty()) ? null : list);
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final TypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            typeSignature.findReferencedClassInfo(map, set);
        }
        final TypeSignature typeDescriptor = this.getTypeDescriptor();
        if (typeDescriptor != null) {
            typeDescriptor.findReferencedClassInfo(map, set);
        }
        if (this.annotationInfo != null) {
            final Iterator iterator = this.annotationInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().findReferencedClassInfo(map, set);
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldInfo)) {
            return false;
        }
        final FieldInfo fieldInfo = (FieldInfo)o;
        return this.declaringClassName.equals(fieldInfo.declaringClassName) && this.name.equals(fieldInfo.name);
    }
    
    public String getTypeSignatureStr() {
        return this.typeSignatureStr;
    }
    
    public boolean hasAnnotation(final String s) {
        return this.getAnnotationInfo().containsName(s);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.declaringClassName.hashCode() * 11;
    }
    
    public AnnotationInfoList getAnnotationInfo() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        return (this.annotationInfo == null) ? AnnotationInfoList.EMPTY_LIST : AnnotationInfoList.getIndirectAnnotations(this.annotationInfo, null);
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    public TypeSignature getTypeDescriptor() {
        if (this.typeDescriptorStr == null) {
            return null;
        }
        if (this.typeDescriptor == null) {
            try {
                this.typeDescriptor = TypeSignature.parse(this.typeDescriptorStr, this.declaringClassName);
                this.typeDescriptor.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeDescriptor;
    }
}
