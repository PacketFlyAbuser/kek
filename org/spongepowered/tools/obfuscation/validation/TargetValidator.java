// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.validation;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.DeclaredType;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.mixin.gen.Accessor;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import java.util.Iterator;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import javax.lang.model.element.TypeElement;
import org.spongepowered.tools.obfuscation.MixinValidator;

public class TargetValidator extends MixinValidator
{
    private void validateClassMixin(final TypeElement obj, final Collection<TypeHandle> collection) {
        final TypeMirror superclass = obj.getSuperclass();
        final Iterator<TypeHandle> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final TypeMirror type = iterator.next().getType();
            if (type != null && !this.validateSuperClass(type, superclass)) {
                this.error("Superclass " + superclass + " of " + obj + " was not found in the hierarchy of target class " + type, obj);
            }
        }
    }
    
    private boolean checkMixinsFor(final TypeMirror typeMirror, final TypeMirror typeMirror2) {
        final Iterator<TypeMirror> iterator = this.getMixinsTargeting(typeMirror).iterator();
        while (iterator.hasNext()) {
            if (TypeUtils.isAssignable(this.processingEnv, iterator.next(), typeMirror2)) {
                return true;
            }
        }
        return false;
    }
    
    public TargetValidator(final IMixinAnnotationProcessor mixinAnnotationProcessor) {
        super(mixinAnnotationProcessor, IMixinValidator.ValidationPass.LATE);
    }
    
    private void validateInterfaceMixin(final TypeElement obj, final Collection<TypeHandle> collection) {
        boolean b = false;
        for (final Element element : obj.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                final boolean exists = AnnotationHandle.of(element, Accessor.class).exists();
                final boolean exists2 = AnnotationHandle.of(element, Invoker.class).exists();
                b |= (!exists && !exists2);
            }
        }
        if (!b) {
            return;
        }
        for (final TypeHandle obj2 : collection) {
            final TypeElement element2 = obj2.getElement();
            if (element2 != null && element2.getKind() != ElementKind.INTERFACE) {
                this.error("Targetted type '" + obj2 + " of " + obj + " is not an interface", obj);
            }
        }
    }
    
    private boolean validateSuperClassRecursive(final TypeMirror typeMirror, final TypeMirror typeMirror2) {
        if (!(typeMirror instanceof DeclaredType)) {
            return false;
        }
        if (TypeUtils.isAssignable(this.processingEnv, typeMirror, typeMirror2)) {
            return true;
        }
        final TypeMirror superclass = ((TypeElement)((DeclaredType)typeMirror).asElement()).getSuperclass();
        return superclass.getKind() != TypeKind.NONE && (this.checkMixinsFor(superclass, typeMirror2) || this.validateSuperClassRecursive(superclass, typeMirror2));
    }
    
    private boolean validateSuperClass(final TypeMirror typeMirror, final TypeMirror typeMirror2) {
        return TypeUtils.isAssignable(this.processingEnv, typeMirror, typeMirror2) || this.validateSuperClassRecursive(typeMirror, typeMirror2);
    }
    
    public boolean validate(final TypeElement typeElement, final AnnotationHandle annotationHandle, final Collection<TypeHandle> collection) {
        if ("true".equalsIgnoreCase(this.options.getOption("disableTargetValidator"))) {
            return true;
        }
        if (typeElement.getKind() == ElementKind.INTERFACE) {
            this.validateInterfaceMixin(typeElement, collection);
        }
        else {
            this.validateClassMixin(typeElement, collection);
        }
        return true;
    }
}
