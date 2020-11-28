// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.tools.obfuscation.mirror.mapping.ResolvableMappingMethod;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import com.google.common.collect.ImmutableList;
import java.util.Collections;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import java.util.Iterator;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class TypeHandle
{
    private /* synthetic */ TypeReference reference;
    private final /* synthetic */ TypeElement element;
    private final /* synthetic */ String name;
    private final /* synthetic */ PackageElement pkg;
    
    public boolean isImaginary() {
        return this.getTargetElement() == null;
    }
    
    public MethodHandle findMethod(final String s, final String s2, final boolean b) {
        return findMethod(this, s, s2, TypeUtils.stripGenerics(s2), b);
    }
    
    protected static boolean compareElement(final Element element, final String s, final String s2, final boolean b) {
        try {
            final String string = element.getSimpleName().toString();
            final String javaSignature = TypeUtils.getJavaSignature(element);
            final String stripGenerics = TypeUtils.stripGenerics(javaSignature);
            return (b ? s.equals(string) : s.equalsIgnoreCase(string)) && (s2.length() == 0 || s2.equals(javaSignature) || s2.equals(stripGenerics));
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
    
    public boolean isPublic() {
        return this.getTargetElement() != null && this.getTargetElement().getModifiers().contains(Modifier.PUBLIC);
    }
    
    public <T extends Element> List<T> getEnclosedElements(final ElementKind... array) {
        return getEnclosedElements(this.getTargetElement(), array);
    }
    
    public boolean isSimulated() {
        return false;
    }
    
    protected static MethodHandle findMethod(final TypeHandle typeHandle, final String s, final String s2, final String s3, final boolean b) {
        for (final ExecutableElement executableElement : getEnclosedElements(typeHandle.getTargetElement(), ElementKind.CONSTRUCTOR, ElementKind.METHOD)) {
            if (compareElement(executableElement, s, s2, b) || compareElement(executableElement, s, s3, b)) {
                return new MethodHandle(typeHandle, executableElement);
            }
        }
        return null;
    }
    
    public String findDescriptor(final MemberInfo memberInfo) {
        String s = memberInfo.desc;
        if (s == null) {
            for (final ExecutableElement executableElement : this.getEnclosedElements(ElementKind.METHOD)) {
                if (executableElement.getSimpleName().toString().equals(memberInfo.name)) {
                    s = TypeUtils.getDescriptor(executableElement);
                    break;
                }
            }
        }
        return s;
    }
    
    public AnnotationHandle getAnnotation(final Class<? extends Annotation> clazz) {
        return AnnotationHandle.of(this.getTargetElement(), clazz);
    }
    
    public TypeHandle getSuperclass() {
        final TypeElement targetElement = this.getTargetElement();
        if (targetElement == null) {
            return null;
        }
        final TypeMirror superclass = targetElement.getSuperclass();
        if (superclass == null || superclass.getKind() == TypeKind.NONE) {
            return null;
        }
        return new TypeHandle((DeclaredType)superclass);
    }
    
    public TypeHandle(final DeclaredType declaredType) {
        this((TypeElement)declaredType.asElement());
    }
    
    public final FieldHandle findField(final VariableElement variableElement, final boolean b) {
        return this.findField(variableElement.getSimpleName().toString(), TypeUtils.getTypeName(variableElement.asType()), b);
    }
    
    public final MethodHandle findMethod(final String s, final String s2) {
        return this.findMethod(s, s2, true);
    }
    
    @Override
    public final String toString() {
        return this.name.replace('/', '.');
    }
    
    public final List<? extends Element> getEnclosedElements() {
        return getEnclosedElements(this.getTargetElement());
    }
    
    public final FieldHandle findField(final String s, final String s2) {
        return this.findField(s, s2, true);
    }
    
    public TypeHandle(final PackageElement pkg, final String s) {
        this.name = s.replace('.', '/');
        this.pkg = pkg;
        this.element = null;
    }
    
    public List<TypeHandle> getInterfaces() {
        if (this.getTargetElement() == null) {
            return Collections.emptyList();
        }
        final ImmutableList.Builder builder = ImmutableList.builder();
        final Iterator<? extends TypeMirror> iterator = this.getTargetElement().getInterfaces().iterator();
        while (iterator.hasNext()) {
            builder.add((Object)new TypeHandle((DeclaredType)iterator.next()));
        }
        return (List<TypeHandle>)builder.build();
    }
    
    protected TypeElement getTargetElement() {
        return this.element;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final PackageElement getPackage() {
        return this.pkg;
    }
    
    public final TypeReference getReference() {
        if (this.reference == null) {
            this.reference = new TypeReference(this);
        }
        return this.reference;
    }
    
    public final MethodHandle findMethod(final ExecutableElement executableElement) {
        return this.findMethod(executableElement, true);
    }
    
    public FieldHandle findField(final String s, final String s2, final boolean b) {
        final String stripGenerics = TypeUtils.stripGenerics(s2);
        for (final VariableElement variableElement : this.getEnclosedElements(ElementKind.FIELD)) {
            if (compareElement(variableElement, s, s2, b)) {
                return new FieldHandle(this.getTargetElement(), variableElement);
            }
            if (compareElement(variableElement, s, stripGenerics, b)) {
                return new FieldHandle(this.getTargetElement(), variableElement, true);
            }
        }
        return null;
    }
    
    public final TypeElement getElement() {
        return this.element;
    }
    
    public final FieldHandle findField(final VariableElement variableElement) {
        return this.findField(variableElement, true);
    }
    
    public TypeMirror getType() {
        return (this.getTargetElement() != null) ? this.getTargetElement().asType() : null;
    }
    
    public MappingMethod getMappingMethod(final String s, final String s2) {
        return new ResolvableMappingMethod(this, s, s2);
    }
    
    protected static <T extends Element> List<T> getEnclosedElements(final TypeElement typeElement, final ElementKind... array) {
        if (array == null || array.length < 1) {
            return (List<T>)getEnclosedElements(typeElement);
        }
        if (typeElement == null) {
            return Collections.emptyList();
        }
        final ImmutableList.Builder builder = ImmutableList.builder();
        for (final Element element : typeElement.getEnclosedElements()) {
            for (int length = array.length, i = 0; i < length; ++i) {
                if (element.getKind() == array[i]) {
                    builder.add((Object)element);
                    break;
                }
            }
        }
        return (List<T>)builder.build();
    }
    
    protected static List<? extends Element> getEnclosedElements(final TypeElement typeElement) {
        return (typeElement != null) ? typeElement.getEnclosedElements() : Collections.emptyList();
    }
    
    public final MethodHandle findMethod(final ExecutableElement executableElement, final boolean b) {
        return this.findMethod(executableElement.getSimpleName().toString(), TypeUtils.getJavaSignature(executableElement), b);
    }
    
    public TypeHandle(final TypeElement element) {
        this.pkg = TypeUtils.getPackage(element);
        this.name = TypeUtils.getInternalName(element);
        this.element = element;
    }
}
