// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.List;
import java.util.Collection;
import java.util.Map;
import nonapi.io.github.classgraph.types.ParseException;
import java.util.Set;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import nonapi.io.github.classgraph.types.TypeUtils;
import java.util.Iterator;

public class MethodInfo extends ScanResultObject implements Comparable<MethodInfo>, HasName
{
    private transient /* synthetic */ MethodParameterInfo[] parameterInfo;
    private /* synthetic */ int modifiers;
    private /* synthetic */ String typeDescriptorStr;
    /* synthetic */ AnnotationInfoList annotationInfo;
    private transient /* synthetic */ MethodTypeSignature typeDescriptor;
    private transient /* synthetic */ MethodTypeSignature typeSignature;
    private /* synthetic */ String name;
    private /* synthetic */ String typeSignatureStr;
    private /* synthetic */ int[] parameterModifiers;
    /* synthetic */ AnnotationInfo[][] parameterAnnotationInfo;
    private /* synthetic */ String[] parameterNames;
    private /* synthetic */ String declaringClassName;
    private /* synthetic */ boolean hasBody;
    
    public String getTypeSignatureOrTypeDescriptorStr() {
        if (this.typeSignatureStr != null) {
            return this.typeSignatureStr;
        }
        return this.typeDescriptorStr;
    }
    
    @Override
    public int compareTo(final MethodInfo methodInfo) {
        final int compareTo = this.declaringClassName.compareTo(methodInfo.declaringClassName);
        if (compareTo != 0) {
            return compareTo;
        }
        final int compareTo2 = this.name.compareTo(methodInfo.name);
        if (compareTo2 != 0) {
            return compareTo2;
        }
        return this.typeDescriptorStr.compareTo(methodInfo.typeDescriptorStr);
    }
    
    public int getModifiers() {
        return this.modifiers;
    }
    
    @Override
    void setScanResult(final ScanResult scanResult) {
        super.setScanResult(scanResult);
        if (this.typeDescriptor != null) {
            this.typeDescriptor.setScanResult(scanResult);
        }
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
        if (this.annotationInfo != null) {
            final Iterator iterator = this.annotationInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().setScanResult(scanResult);
            }
        }
        if (this.parameterAnnotationInfo != null) {
            for (final AnnotationInfo[] array : this.parameterAnnotationInfo) {
                if (array != null) {
                    final AnnotationInfo[] array2 = array;
                    for (int length2 = array2.length, j = 0; j < length2; ++j) {
                        array2[j].setScanResult(scanResult);
                    }
                }
            }
        }
        if (this.parameterInfo != null) {
            final MethodParameterInfo[] parameterInfo = this.parameterInfo;
            for (int length3 = parameterInfo.length, k = 0; k < length3; ++k) {
                parameterInfo[k].setScanResult(scanResult);
            }
        }
    }
    
    public String getModifiersStr() {
        final StringBuilder sb = new StringBuilder();
        TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.METHOD, this.isDefault(), sb);
        return sb.toString();
    }
    
    public String getTypeDescriptorStr() {
        return this.typeDescriptorStr;
    }
    
    public boolean isPublic() {
        return Modifier.isPublic(this.modifiers);
    }
    
    public boolean isDefault() {
        final ClassInfo classInfo = this.getClassInfo();
        return classInfo != null && classInfo.isInterface() && this.hasBody;
    }
    
    public Method loadClassAndGetMethod() throws IllegalArgumentException {
        final MethodParameterInfo[] parameterInfo = this.getParameterInfo();
        final ArrayList list = new ArrayList<Class<?>>(parameterInfo.length);
        final MethodParameterInfo[] array = parameterInfo;
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(array[i].getTypeSignatureOrTypeDescriptor().loadClass());
        }
        final Class<?>[] array2 = list.toArray(new Class[0]);
        try {
            return this.loadClass().getMethod(this.getName(), array2);
        }
        catch (NoSuchMethodException ex) {
            try {
                return this.loadClass().getDeclaredMethod(this.getName(), array2);
            }
            catch (NoSuchMethodException ex2) {
                throw new IllegalArgumentException("No such method: " + this.getClassName() + "." + this.getName());
            }
        }
    }
    
    public boolean isNative() {
        return Modifier.isNative(this.modifiers);
    }
    
    public boolean isSynchronized() {
        return Modifier.isSynchronized(this.modifiers);
    }
    
    public boolean isSynthetic() {
        return (this.modifiers & 0x1000) != 0x0;
    }
    
    public boolean isBridge() {
        return (this.modifiers & 0x40) != 0x0;
    }
    
    public boolean hasAnnotation(final String s) {
        return this.getAnnotationInfo().containsName(s);
    }
    
    public ClassInfo getClassInfo() {
        return super.getClassInfo();
    }
    
    public boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }
    
    void handleRepeatableAnnotations(final Set<String> set) {
        if (this.annotationInfo != null) {
            this.annotationInfo.handleRepeatableAnnotations(set, this.getClassInfo(), ClassInfo.RelType.METHOD_ANNOTATIONS, ClassInfo.RelType.CLASSES_WITH_METHOD_ANNOTATION, ClassInfo.RelType.CLASSES_WITH_NONPRIVATE_METHOD_ANNOTATION);
        }
        if (this.parameterAnnotationInfo != null) {
            for (int i = 0; i < this.parameterAnnotationInfo.length; ++i) {
                final AnnotationInfo[] array = this.parameterAnnotationInfo[i];
                if (array != null && array.length > 0) {
                    boolean b = false;
                    final AnnotationInfo[] array2 = array;
                    for (int length = array2.length, j = 0; j < length; ++j) {
                        if (set.contains(array2[j].getName())) {
                            b = true;
                            break;
                        }
                    }
                    if (b) {
                        final AnnotationInfoList list = new AnnotationInfoList(array.length);
                        final AnnotationInfo[] array3 = array;
                        for (int length2 = array3.length, k = 0; k < length2; ++k) {
                            list.add(array3[k]);
                        }
                        list.handleRepeatableAnnotations(set, this.getClassInfo(), ClassInfo.RelType.METHOD_PARAMETER_ANNOTATIONS, ClassInfo.RelType.CLASSES_WITH_METHOD_PARAMETER_ANNOTATION, ClassInfo.RelType.CLASSES_WITH_NONPRIVATE_METHOD_PARAMETER_ANNOTATION);
                        this.parameterAnnotationInfo[i] = list.toArray(new AnnotationInfo[0]);
                    }
                }
            }
        }
    }
    
    public MethodTypeSignature getTypeDescriptor() {
        if (this.typeDescriptor == null) {
            try {
                this.typeDescriptor = MethodTypeSignature.parse(this.typeDescriptorStr, this.declaringClassName);
                this.typeDescriptor.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeDescriptor;
    }
    
    @Override
    protected void findReferencedClassInfo(final Map<String, ClassInfo> map, final Set<ClassInfo> set) {
        final MethodTypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            typeSignature.findReferencedClassInfo(map, set);
        }
        final MethodTypeSignature typeDescriptor = this.getTypeDescriptor();
        if (typeDescriptor != null) {
            typeDescriptor.findReferencedClassInfo(map, set);
        }
        if (this.annotationInfo != null) {
            final Iterator iterator = this.annotationInfo.iterator();
            while (iterator.hasNext()) {
                iterator.next().findReferencedClassInfo(map, set);
            }
        }
        final MethodParameterInfo[] parameterInfo = this.getParameterInfo();
        for (int length = parameterInfo.length, i = 0; i < length; ++i) {
            final AnnotationInfo[] annotationInfo = parameterInfo[i].annotationInfo;
            if (annotationInfo != null) {
                final AnnotationInfo[] array = annotationInfo;
                for (int length2 = array.length, j = 0; j < length2; ++j) {
                    array[j].findReferencedClassInfo(map, set);
                }
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MethodInfo)) {
            return false;
        }
        final MethodInfo methodInfo = (MethodInfo)o;
        return this.declaringClassName.equals(methodInfo.declaringClassName) && this.typeDescriptorStr.equals(methodInfo.typeDescriptorStr) && this.name.equals(methodInfo.name);
    }
    
    public boolean isVarArgs() {
        return (this.modifiers & 0x80) != 0x0;
    }
    
    public AnnotationInfoList getAnnotationInfo() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        return (this.annotationInfo == null) ? AnnotationInfoList.EMPTY_LIST : AnnotationInfoList.getIndirectAnnotations(this.annotationInfo, null);
    }
    
    @Override
    protected String getClassName() {
        return this.declaringClassName;
    }
    
    public AnnotationInfoList getAnnotationInfoRepeatable(final String s) {
        return this.getAnnotationInfo().getRepeatable(s);
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode() + this.typeDescriptorStr.hashCode() * 11 + this.declaringClassName.hashCode() * 57;
    }
    
    public MethodTypeSignature getTypeSignature() {
        if (this.typeSignature == null && this.typeSignatureStr != null) {
            try {
                this.typeSignature = MethodTypeSignature.parse(this.typeSignatureStr, this.declaringClassName);
                this.typeSignature.setScanResult(this.scanResult);
            }
            catch (ParseException cause) {
                throw new IllegalArgumentException(cause);
            }
        }
        return this.typeSignature;
    }
    
    public boolean isConstructor() {
        return "<init>".equals(this.name);
    }
    
    public boolean hasParameterAnnotation(final String s) {
        final MethodParameterInfo[] parameterInfo = this.getParameterInfo();
        for (int length = parameterInfo.length, i = 0; i < length; ++i) {
            if (parameterInfo[i].hasAnnotation(s)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public boolean isFinal() {
        return Modifier.isFinal(this.modifiers);
    }
    
    public MethodTypeSignature getTypeSignatureOrTypeDescriptor() {
        final MethodTypeSignature typeSignature = this.getTypeSignature();
        if (typeSignature != null) {
            return typeSignature;
        }
        return this.getTypeDescriptor();
    }
    
    public MethodParameterInfo[] getParameterInfo() {
        if (this.parameterInfo == null) {
            final List<TypeSignature> parameterTypeSignatures = this.getTypeDescriptor().getParameterTypeSignatures();
            final List<TypeSignature> list = (this.getTypeSignature() != null) ? this.getTypeSignature().getParameterTypeSignatures() : null;
            final int size = parameterTypeSignatures.size();
            if (list != null && list.size() > size) {
                throw ClassGraphException.newClassGraphException("typeSignatureParamTypes.size() > typeDescriptorParamTypes.size() for method " + this.declaringClassName + "." + this.name);
            }
            if (Math.max((this.parameterNames == null) ? 0 : this.parameterNames.length, Math.max((this.parameterModifiers == null) ? 0 : this.parameterModifiers.length, (this.parameterAnnotationInfo == null) ? 0 : this.parameterAnnotationInfo.length)) > size) {
                throw ClassGraphException.newClassGraphException("Type descriptor for method " + this.declaringClassName + "." + this.name + " has insufficient parameters");
            }
            String[] parameterNames = null;
            if (this.parameterNames != null && size > 0) {
                if (this.parameterNames.length == size) {
                    parameterNames = this.parameterNames;
                }
                else {
                    parameterNames = new String[size];
                    int i = 0;
                    final int n = size - this.parameterNames.length;
                    while (i < this.parameterNames.length) {
                        parameterNames[n + i] = this.parameterNames[i];
                        ++i;
                    }
                }
            }
            int[] parameterModifiers = null;
            if (this.parameterModifiers != null && size > 0) {
                if (this.parameterModifiers.length == size) {
                    parameterModifiers = this.parameterModifiers;
                }
                else {
                    parameterModifiers = new int[size];
                    int j = 0;
                    final int n2 = size - this.parameterModifiers.length;
                    while (j < this.parameterModifiers.length) {
                        parameterModifiers[n2 + j] = this.parameterModifiers[j];
                        ++j;
                    }
                }
            }
            AnnotationInfo[][] parameterAnnotationInfo = null;
            if (this.parameterAnnotationInfo != null && size > 0) {
                if (this.parameterAnnotationInfo.length == size) {
                    parameterAnnotationInfo = this.parameterAnnotationInfo;
                }
                else {
                    parameterAnnotationInfo = new AnnotationInfo[size][];
                    int k = 0;
                    final int n3 = size - this.parameterAnnotationInfo.length;
                    while (k < this.parameterAnnotationInfo.length) {
                        parameterAnnotationInfo[n3 + k] = this.parameterAnnotationInfo[k];
                        ++k;
                    }
                }
            }
            Object o = null;
            if (list != null && size > 0) {
                if (list.size() == parameterTypeSignatures.size()) {
                    o = list;
                }
                else {
                    o = new ArrayList<Object>(size);
                    for (int l = 0; l < size - list.size(); ++l) {
                        ((List<TypeSignature>)o).add(null);
                    }
                    ((List<Object>)o).addAll(list);
                }
            }
            this.parameterInfo = new MethodParameterInfo[size];
            for (int n4 = 0; n4 < size; ++n4) {
                (this.parameterInfo[n4] = new MethodParameterInfo(this, (AnnotationInfo[])((parameterAnnotationInfo == null) ? null : parameterAnnotationInfo[n4]), (parameterModifiers == null) ? 0 : parameterModifiers[n4], parameterTypeSignatures.get(n4), (o == null) ? null : ((List<TypeSignature>)o).get(n4), (parameterNames == null) ? null : parameterNames[n4])).setScanResult(this.scanResult);
            }
        }
        return this.parameterInfo;
    }
    
    MethodInfo() {
    }
    
    MethodInfo(final String declaringClassName, final String name, final AnnotationInfoList list, final int modifiers, final String typeDescriptorStr, final String typeSignatureStr, final String[] parameterNames, final int[] parameterModifiers, final AnnotationInfo[][] parameterAnnotationInfo, final boolean hasBody) {
        this.declaringClassName = declaringClassName;
        this.name = name;
        this.modifiers = modifiers;
        this.typeDescriptorStr = typeDescriptorStr;
        this.typeSignatureStr = typeSignatureStr;
        this.parameterNames = parameterNames;
        this.parameterModifiers = parameterModifiers;
        this.parameterAnnotationInfo = parameterAnnotationInfo;
        this.annotationInfo = ((list == null || list.isEmpty()) ? null : list);
        this.hasBody = hasBody;
    }
    
    public boolean hasBody() {
        return this.hasBody;
    }
    
    @Override
    public String toString() {
        final MethodTypeSignature typeSignatureOrTypeDescriptor = this.getTypeSignatureOrTypeDescriptor();
        final StringBuilder sb = new StringBuilder();
        if (this.annotationInfo != null) {
            for (final AnnotationInfo annotationInfo : this.annotationInfo) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                annotationInfo.toString(sb);
            }
        }
        if (this.modifiers != 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            TypeUtils.modifiersToString(this.modifiers, TypeUtils.ModifierType.METHOD, this.isDefault(), sb);
        }
        final List<TypeParameter> typeParameters = typeSignatureOrTypeDescriptor.getTypeParameters();
        if (!typeParameters.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append('<');
            for (int i = 0; i < typeParameters.size(); ++i) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(typeParameters.get(i).toString());
            }
            sb.append('>');
        }
        if (!this.isConstructor()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(typeSignatureOrTypeDescriptor.getResultType().toString());
        }
        sb.append(' ');
        if (this.name != null) {
            sb.append(this.name);
        }
        final MethodParameterInfo[] parameterInfo = this.getParameterInfo();
        boolean b = false;
        final MethodParameterInfo[] array = parameterInfo;
        for (int length = array.length, j = 0; j < length; ++j) {
            if (array[j].getName() != null) {
                b = true;
                break;
            }
        }
        int n = -1;
        if (this.isVarArgs()) {
            for (int k = parameterInfo.length - 1; k >= 0; --k) {
                final int modifiers = parameterInfo[k].getModifiers();
                if ((modifiers & 0x1000) == 0x0 && (modifiers & 0x8000) == 0x0 && parameterInfo[k].getTypeSignatureOrTypeDescriptor() instanceof ArrayTypeSignature) {
                    n = k;
                    break;
                }
            }
        }
        sb.append('(');
        for (int l = 0; l < parameterInfo.length; ++l) {
            final MethodParameterInfo methodParameterInfo = parameterInfo[l];
            if (l > 0) {
                sb.append(", ");
            }
            if (methodParameterInfo.annotationInfo != null) {
                final AnnotationInfo[] annotationInfo2 = methodParameterInfo.annotationInfo;
                for (int length2 = annotationInfo2.length, n2 = 0; n2 < length2; ++n2) {
                    annotationInfo2[n2].toString(sb);
                    sb.append(' ');
                }
            }
            MethodParameterInfo.modifiersToString(methodParameterInfo.getModifiers(), sb);
            final TypeSignature typeSignatureOrTypeDescriptor2 = methodParameterInfo.getTypeSignatureOrTypeDescriptor();
            if (l == n) {
                if (!(typeSignatureOrTypeDescriptor2 instanceof ArrayTypeSignature)) {
                    throw new IllegalArgumentException("Got non-array type for last parameter of varargs method " + this.name);
                }
                final ArrayTypeSignature arrayTypeSignature = (ArrayTypeSignature)typeSignatureOrTypeDescriptor2;
                if (arrayTypeSignature.getNumDimensions() == 0) {
                    throw new IllegalArgumentException("Got a zero-dimension array type for last parameter of varargs method " + this.name);
                }
                sb.append(new ArrayTypeSignature(arrayTypeSignature.getElementTypeSignature(), arrayTypeSignature.getNumDimensions() - 1, null).toString());
                sb.append("...");
            }
            else {
                sb.append(typeSignatureOrTypeDescriptor2.toString());
            }
            if (b) {
                final String name = methodParameterInfo.getName();
                if (name != null) {
                    sb.append(' ');
                    sb.append(name);
                }
            }
        }
        sb.append(')');
        if (!typeSignatureOrTypeDescriptor.getThrowsSignatures().isEmpty()) {
            sb.append(" throws ");
            for (int n3 = 0; n3 < typeSignatureOrTypeDescriptor.getThrowsSignatures().size(); ++n3) {
                if (n3 > 0) {
                    sb.append(", ");
                }
                sb.append(typeSignatureOrTypeDescriptor.getThrowsSignatures().get(n3).toString());
            }
        }
        return sb.toString();
    }
    
    public String getTypeSignatureStr() {
        return this.typeSignatureStr;
    }
}
