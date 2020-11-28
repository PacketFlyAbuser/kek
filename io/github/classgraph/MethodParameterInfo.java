// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collection;
import java.util.Collections;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Arrays;

public class MethodParameterInfo
{
    private final /* synthetic */ int modifiers;
    private /* synthetic */ ScanResult scanResult;
    private final /* synthetic */ MethodInfo methodInfo;
    private final /* synthetic */ TypeSignature typeSignature;
    private final /* synthetic */ TypeSignature typeDescriptor;
    final /* synthetic */ AnnotationInfo[] annotationInfo;
    private final /* synthetic */ String name;
    
    public String getModifiersStr() {
        final StringBuilder sb = new StringBuilder();
        modifiersToString(this.modifiers, sb);
        return sb.toString();
    }
    
    public boolean isSynthetic() {
        return (this.modifiers & 0x1000) != 0x0;
    }
    
    public int getModifiers() {
        return this.modifiers;
    }
    
    MethodParameterInfo(final MethodInfo methodInfo, final AnnotationInfo[] annotationInfo, final int modifiers, final TypeSignature typeDescriptor, final TypeSignature typeSignature, final String name) {
        this.methodInfo = methodInfo;
        this.name = name;
        this.modifiers = modifiers;
        this.typeDescriptor = typeDescriptor;
        this.typeSignature = typeSignature;
        this.annotationInfo = annotationInfo;
    }
    
    public MethodInfo getMethodInfo() {
        return this.methodInfo;
    }
    
    public AnnotationInfo getAnnotationInfo(final String s) {
        return this.getAnnotationInfo().get(s);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.methodInfo, Arrays.hashCode(this.annotationInfo), this.typeDescriptor, this.typeSignature, this.name) + this.modifiers;
    }
    
    protected void setScanResult(final ScanResult scanResult) {
        this.scanResult = scanResult;
        if (this.annotationInfo != null) {
            final AnnotationInfo[] annotationInfo = this.annotationInfo;
            for (int length = annotationInfo.length, i = 0; i < length; ++i) {
                annotationInfo[i].setScanResult(scanResult);
            }
        }
        if (this.typeDescriptor != null) {
            this.typeDescriptor.setScanResult(scanResult);
        }
        if (this.typeSignature != null) {
            this.typeSignature.setScanResult(scanResult);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.annotationInfo != null) {
            final AnnotationInfo[] annotationInfo = this.annotationInfo;
            for (int length = annotationInfo.length, i = 0; i < length; ++i) {
                annotationInfo[i].toString(sb);
                sb.append(' ');
            }
        }
        modifiersToString(this.modifiers, sb);
        sb.append(this.getTypeSignatureOrTypeDescriptor().toString());
        sb.append(' ');
        sb.append((this.name == null) ? "_unnamed_param" : this.name);
        return sb.toString();
    }
    
    public TypeSignature getTypeSignature() {
        return this.typeSignature;
    }
    
    static void modifiersToString(final int n, final StringBuilder sb) {
        if ((n & 0x10) != 0x0) {
            sb.append("final ");
        }
        if ((n & 0x1000) != 0x0) {
            sb.append("synthetic ");
        }
        if ((n & 0x8000) != 0x0) {
            sb.append("mandated ");
        }
    }
    
    public boolean isFinal() {
        return Modifier.isFinal(this.modifiers);
    }
    
    public AnnotationInfoList getAnnotationInfo() {
        if (!this.scanResult.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableAnnotationInfo() before #scan()");
        }
        if (this.annotationInfo == null || this.annotationInfo.length == 0) {
            return AnnotationInfoList.EMPTY_LIST;
        }
        final AnnotationInfoList c = new AnnotationInfoList(this.annotationInfo.length);
        Collections.addAll(c, this.annotationInfo);
        return AnnotationInfoList.getIndirectAnnotations(c, null);
    }
    
    public String getName() {
        return this.name;
    }
    
    public TypeSignature getTypeSignatureOrTypeDescriptor() {
        return (this.typeSignature != null) ? this.typeSignature : this.typeDescriptor;
    }
    
    public boolean isMandated() {
        return (this.modifiers & 0x8000) != 0x0;
    }
    
    public AnnotationInfoList getAnnotationInfoRepeatable(final String s) {
        return this.getAnnotationInfo().getRepeatable(s);
    }
    
    public boolean hasAnnotation(final String s) {
        return this.getAnnotationInfo().containsName(s);
    }
    
    public TypeSignature getTypeDescriptor() {
        return this.typeDescriptor;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MethodParameterInfo)) {
            return false;
        }
        final MethodParameterInfo methodParameterInfo = (MethodParameterInfo)o;
        return Objects.equals(this.methodInfo, methodParameterInfo.methodInfo) && Objects.deepEquals(this.annotationInfo, methodParameterInfo.annotationInfo) && this.modifiers == methodParameterInfo.modifiers && Objects.equals(this.typeDescriptor, methodParameterInfo.typeDescriptor) && Objects.equals(this.typeSignature, methodParameterInfo.typeSignature) && Objects.equals(this.name, methodParameterInfo.name);
    }
}
