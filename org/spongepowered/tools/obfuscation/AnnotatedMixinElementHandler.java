// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.util.List;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.tools.obfuscation.mirror.Visibility;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.asm.util.ConstraintParser;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;

abstract class AnnotatedMixinElementHandler
{
    protected final /* synthetic */ IObfuscationManager obf;
    protected final /* synthetic */ AnnotatedMixin mixin;
    private /* synthetic */ IMappingConsumer mappings;
    protected final /* synthetic */ IMixinAnnotationProcessor ap;
    protected final /* synthetic */ String classRef;
    
    protected final void addFieldMapping(final ObfuscationType obfuscationType, final ShadowElementName shadowElementName, final String s, final String s2) {
        this.addFieldMapping(obfuscationType, shadowElementName.name(), shadowElementName.obfuscated(), s, s2);
    }
    
    protected final void addMethodMappings(final String s, final String s2, final ObfuscationData<MappingMethod> obfuscationData) {
        for (final ObfuscationType obfuscationType : obfuscationData) {
            final MappingMethod mappingMethod = obfuscationData.get(obfuscationType);
            this.addMethodMapping(obfuscationType, s, mappingMethod.getSimpleName(), s2, mappingMethod.getDesc());
        }
    }
    
    protected static <T extends IMapping<T>> ObfuscationData<T> stripOwnerData(final ObfuscationData<T> obfuscationData) {
        final ObfuscationData<Object> obfuscationData2 = (ObfuscationData<Object>)new ObfuscationData<T>();
        for (final ObfuscationType obfuscationType : obfuscationData) {
            obfuscationData2.put(obfuscationType, obfuscationData.get(obfuscationType).move(null));
        }
        return (ObfuscationData<T>)obfuscationData2;
    }
    
    AnnotatedMixinElementHandler(final IMixinAnnotationProcessor ap, final AnnotatedMixin mixin) {
        this.ap = ap;
        this.mixin = mixin;
        this.classRef = mixin.getClassRef();
        this.obf = ap.getObfuscationManager();
    }
    
    protected final void checkConstraints(final ExecutableElement executableElement, final AnnotationHandle annotationHandle) {
        try {
            final ConstraintParser.Constraint parse = ConstraintParser.parse(annotationHandle.getValue("constraints"));
            try {
                parse.check(this.ap.getTokenProvider());
            }
            catch (ConstraintViolationException ex) {
                this.ap.printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), executableElement, annotationHandle.asMirror());
            }
        }
        catch (InvalidConstraintException ex2) {
            this.ap.printMessage(Diagnostic.Kind.WARNING, ex2.getMessage(), executableElement, annotationHandle.asMirror());
        }
    }
    
    protected final void addMethodMapping(final ObfuscationType obfuscationType, final String s, final String s2, final String s3, final String s4) {
        this.getMappings().addMethodMapping(obfuscationType, new MappingMethod(this.classRef, s, s3), new MappingMethod(this.classRef, s2, s4));
    }
    
    protected final void addFieldMapping(final ObfuscationType obfuscationType, final String s, final String s2, final String s3, final String s4) {
        this.getMappings().addFieldMapping(obfuscationType, new MappingField(this.classRef, s, s3), new MappingField(this.classRef, s2, s4));
    }
    
    private void printMessage(final Diagnostic.Kind kind, final String s, final Element element, final AnnotationHandle annotationHandle) {
        if (annotationHandle == null) {
            this.ap.printMessage(kind, s, element);
        }
        else {
            this.ap.printMessage(kind, s, element, annotationHandle.asMirror());
        }
    }
    
    private IMappingConsumer getMappings() {
        if (this.mappings == null) {
            final IMappingConsumer mappings = this.mixin.getMappings();
            if (mappings instanceof Mappings) {
                this.mappings = ((Mappings)mappings).asUnique();
            }
            else {
                this.mappings = mappings;
            }
        }
        return this.mappings;
    }
    
    private void validateMethodVisibility(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final String s, final TypeHandle obj, final MethodHandle methodHandle) {
        final Visibility visibility = methodHandle.getVisibility();
        if (visibility == null) {
            return;
        }
        final Visibility visibility2 = TypeUtils.getVisibility(executableElement);
        final String string = "visibility of " + visibility + " method in " + obj;
        if (visibility.ordinal() > visibility2.ordinal()) {
            this.printMessage(Diagnostic.Kind.WARNING, visibility2 + " " + s + " method cannot reduce " + string, executableElement, annotationHandle);
        }
        else if (visibility == Visibility.PRIVATE && visibility2.ordinal() > visibility.ordinal()) {
            this.printMessage(Diagnostic.Kind.WARNING, visibility2 + " " + s + " method will upgrade " + string, executableElement, annotationHandle);
        }
    }
    
    protected final void validateTargetMethod(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final AliasedElementName aliasedElementName, final String str, final boolean b, final boolean b2) {
        final String javaSignature = TypeUtils.getJavaSignature(executableElement);
        for (final TypeHandle obj : this.mixin.getTargets()) {
            if (obj.isImaginary()) {
                continue;
            }
            MethodHandle methodHandle = obj.findMethod(executableElement);
            if (methodHandle == null && aliasedElementName.hasPrefix()) {
                methodHandle = obj.findMethod(aliasedElementName.baseName(), javaSignature);
            }
            if (methodHandle == null && aliasedElementName.hasAliases()) {
                final Iterator<String> iterator2 = aliasedElementName.getAliases().iterator();
                while (iterator2.hasNext()) {
                    if ((methodHandle = obj.findMethod(iterator2.next(), javaSignature)) != null) {
                        break;
                    }
                }
            }
            if (methodHandle != null) {
                if (!b) {
                    continue;
                }
                this.validateMethodVisibility(executableElement, annotationHandle, str, obj, methodHandle);
            }
            else {
                if (b2) {
                    continue;
                }
                this.printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + str + " method in " + obj, executableElement, annotationHandle);
            }
        }
    }
    
    protected final void validateReferencedTarget(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final MemberInfo memberInfo, final String str) {
        final String descriptor = memberInfo.toDescriptor();
        for (final TypeHandle obj : this.mixin.getTargets()) {
            if (obj.isImaginary()) {
                continue;
            }
            if (obj.findMethod(memberInfo.name, descriptor) != null) {
                continue;
            }
            this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target method for " + str + " in " + obj, executableElement, annotationHandle.asMirror());
        }
    }
    
    protected final void validateTarget(final Element element, final AnnotationHandle annotationHandle, final AliasedElementName aliasedElementName, final String s) {
        if (element instanceof ExecutableElement) {
            this.validateTargetMethod((ExecutableElement)element, annotationHandle, aliasedElementName, s, false, false);
        }
        else if (element instanceof VariableElement) {
            this.validateTargetField((VariableElement)element, annotationHandle, aliasedElementName, s);
        }
    }
    
    protected final void validateTargetField(final VariableElement variableElement, final AnnotationHandle annotationHandle, final AliasedElementName aliasedElementName, final String str) {
        final String string = variableElement.asType().toString();
        for (final TypeHandle obj : this.mixin.getTargets()) {
            if (obj.isImaginary()) {
                continue;
            }
            FieldHandle fieldHandle = obj.findField(variableElement);
            if (fieldHandle != null) {
                continue;
            }
            final Iterator<String> iterator2 = aliasedElementName.getAliases().iterator();
            while (iterator2.hasNext() && (fieldHandle = obj.findField(iterator2.next(), string)) == null) {}
            if (fieldHandle != null) {
                continue;
            }
            this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find target for " + str + " field in " + obj, variableElement, annotationHandle.asMirror());
        }
    }
    
    protected static <T extends IMapping<T>> ObfuscationData<T> stripDescriptors(final ObfuscationData<T> obfuscationData) {
        final ObfuscationData<Object> obfuscationData2 = (ObfuscationData<Object>)new ObfuscationData<T>();
        for (final ObfuscationType obfuscationType : obfuscationData) {
            obfuscationData2.put(obfuscationType, obfuscationData.get(obfuscationType).transform(null));
        }
        return (ObfuscationData<T>)obfuscationData2;
    }
    
    protected final void addFieldMappings(final String s, final String s2, final ObfuscationData<MappingField> obfuscationData) {
        for (final ObfuscationType obfuscationType : obfuscationData) {
            final MappingField mappingField = obfuscationData.get(obfuscationType);
            this.addFieldMapping(obfuscationType, s, mappingField.getSimpleName(), s2, mappingField.getDesc());
        }
    }
    
    protected final void addMethodMapping(final ObfuscationType obfuscationType, final ShadowElementName shadowElementName, final String s, final String s2) {
        this.addMethodMapping(obfuscationType, shadowElementName.name(), shadowElementName.obfuscated(), s, s2);
    }
    
    abstract static class AnnotatedElement<E extends Element>
    {
        protected final /* synthetic */ E element;
        private final /* synthetic */ String desc;
        protected final /* synthetic */ AnnotationHandle annotation;
        
        public AnnotationHandle getAnnotation() {
            return this.annotation;
        }
        
        public final void printMessage(final Messager messager, final Diagnostic.Kind kind, final CharSequence charSequence) {
            messager.printMessage(kind, charSequence, this.element, this.annotation.asMirror());
        }
        
        public E getElement() {
            return this.element;
        }
        
        public String getDesc() {
            return this.desc;
        }
        
        public AnnotatedElement(final E element, final AnnotationHandle annotation) {
            this.element = element;
            this.annotation = annotation;
            this.desc = TypeUtils.getDescriptor(element);
        }
        
        public String getSimpleName() {
            return this.getElement().getSimpleName().toString();
        }
    }
    
    static class AliasedElementName
    {
        private final /* synthetic */ List<String> aliases;
        private /* synthetic */ boolean caseSensitive;
        protected final /* synthetic */ String originalName;
        
        public AliasedElementName(final Element element, final AnnotationHandle annotationHandle) {
            this.originalName = element.getSimpleName().toString();
            this.aliases = annotationHandle.getList("aliases");
        }
        
        public AliasedElementName setCaseSensitive(final boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
            return this;
        }
        
        public boolean isCaseSensitive() {
            return this.caseSensitive;
        }
        
        public List<String> getAliases() {
            return this.aliases;
        }
        
        public String baseName() {
            return this.originalName;
        }
        
        public boolean hasPrefix() {
            return false;
        }
        
        public boolean hasAliases() {
            return this.aliases.size() > 0;
        }
        
        public String elementName() {
            return this.originalName;
        }
    }
    
    static class ShadowElementName extends AliasedElementName
    {
        private final /* synthetic */ boolean hasPrefix;
        private final /* synthetic */ String prefix;
        private /* synthetic */ String obfuscated;
        private final /* synthetic */ String baseName;
        
        @Override
        public String toString() {
            return this.baseName;
        }
        
        public String prefix(final String str) {
            return this.hasPrefix ? (this.prefix + str) : str;
        }
        
        @Override
        public String baseName() {
            return this.baseName;
        }
        
        ShadowElementName(final Element element, final AnnotationHandle annotationHandle) {
            super(element, annotationHandle);
            this.prefix = annotationHandle.getValue("prefix", "shadow$");
            boolean hasPrefix = false;
            String s = this.originalName;
            if (s.startsWith(this.prefix)) {
                hasPrefix = true;
                s = s.substring(this.prefix.length());
            }
            this.hasPrefix = hasPrefix;
            final String s2 = s;
            this.baseName = s2;
            this.obfuscated = s2;
        }
        
        @Override
        public boolean hasPrefix() {
            return this.hasPrefix;
        }
        
        public ShadowElementName setObfuscatedName(final String obfuscated) {
            this.obfuscated = obfuscated;
            return this;
        }
        
        public String name() {
            return this.prefix(this.baseName);
        }
        
        public ShadowElementName setObfuscatedName(final IMapping<?> mapping) {
            this.obfuscated = mapping.getName();
            return this;
        }
        
        public String prefix() {
            return this.hasPrefix ? this.prefix : "";
        }
        
        public String obfuscated() {
            return this.prefix(this.obfuscated);
        }
    }
}
