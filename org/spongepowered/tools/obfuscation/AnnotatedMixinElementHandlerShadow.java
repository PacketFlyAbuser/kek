// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationDataProvider;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import javax.lang.model.element.VariableElement;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;

class AnnotatedMixinElementHandlerShadow extends AnnotatedMixinElementHandler
{
    private void registerShadowForTarget(final AnnotatedElementShadow<?, ?> obj, final TypeHandle typeHandle) {
        final ObfuscationData<?> obfuscationData = obj.getObfuscationData(this.obf.getDataProvider(), typeHandle);
        if (obfuscationData.isEmpty()) {
            final String s = this.mixin.isMultiTarget() ? (" in target " + typeHandle) : "";
            if (typeHandle.isSimulated()) {
                obj.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + s + " for @Shadow " + obj);
            }
            else {
                obj.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + s + " for @Shadow " + obj);
            }
            return;
        }
        for (final ObfuscationType obfuscationType : obfuscationData) {
            try {
                obj.addMapping(obfuscationType, obfuscationData.get(obfuscationType));
            }
            catch (Mappings.MappingConflictException ex) {
                obj.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Shadow " + obj + ": " + ex.getNew().getSimpleName() + " for target " + typeHandle + " conflicts with existing mapping " + ex.getOld().getSimpleName());
            }
        }
    }
    
    public void registerShadow(final AnnotatedElementShadow<?, ?> annotatedElementShadow) {
        this.validateTarget(annotatedElementShadow.getElement(), annotatedElementShadow.getAnnotation(), annotatedElementShadow.getName(), "@Shadow");
        if (!annotatedElementShadow.shouldRemap()) {
            return;
        }
        final Iterator<TypeHandle> iterator = this.mixin.getTargets().iterator();
        while (iterator.hasNext()) {
            this.registerShadowForTarget(annotatedElementShadow, iterator.next());
        }
    }
    
    AnnotatedMixinElementHandlerShadow(final IMixinAnnotationProcessor mixinAnnotationProcessor, final AnnotatedMixin annotatedMixin) {
        super(mixinAnnotationProcessor, annotatedMixin);
    }
    
    class AnnotatedElementShadowField extends AnnotatedElementShadow<VariableElement, MappingField>
    {
        @Override
        public void addMapping(final ObfuscationType obfuscationType, final IMapping<?> obfuscatedName) {
            AnnotatedMixinElementHandlerShadow.this.addFieldMapping(obfuscationType, this.setObfuscatedName(obfuscatedName), this.getDesc(), obfuscatedName.getDesc());
        }
        
        @Override
        public MappingField getMapping(final TypeHandle typeHandle, final String s, final String s2) {
            return new MappingField(typeHandle.getName(), s, s2);
        }
        
        public AnnotatedElementShadowField(final VariableElement variableElement, final AnnotationHandle annotationHandle, final boolean b) {
            super(variableElement, annotationHandle, b, IMapping.Type.FIELD);
        }
    }
    
    abstract static class AnnotatedElementShadow<E extends Element, M extends IMapping<M>> extends AnnotatedElement<E>
    {
        private final /* synthetic */ boolean shouldRemap;
        private final /* synthetic */ IMapping.Type type;
        private final /* synthetic */ ShadowElementName name;
        
        @Override
        public String toString() {
            return this.getElementType().name().toLowerCase();
        }
        
        public boolean shouldRemap() {
            return this.shouldRemap;
        }
        
        protected AnnotatedElementShadow(final E e, final AnnotationHandle annotationHandle, final boolean shouldRemap, final IMapping.Type type) {
            super(e, annotationHandle);
            this.shouldRemap = shouldRemap;
            this.name = new ShadowElementName(e, annotationHandle);
            this.type = type;
        }
        
        public abstract M getMapping(final TypeHandle p0, final String p1, final String p2);
        
        public ObfuscationData<M> getObfuscationData(final IObfuscationDataProvider obfuscationDataProvider, final TypeHandle typeHandle) {
            return obfuscationDataProvider.getObfEntry((IMapping<M>)this.getMapping(typeHandle, this.getName().toString(), this.getDesc()));
        }
        
        public ShadowElementName setObfuscatedName(final String obfuscatedName) {
            return this.getName().setObfuscatedName(obfuscatedName);
        }
        
        public abstract void addMapping(final ObfuscationType p0, final IMapping<?> p1);
        
        public IMapping.Type getElementType() {
            return this.type;
        }
        
        public ShadowElementName setObfuscatedName(final IMapping<?> mapping) {
            return this.setObfuscatedName(mapping.getSimpleName());
        }
        
        public ShadowElementName getName() {
            return this.name;
        }
    }
    
    class AnnotatedElementShadowMethod extends AnnotatedElementShadow<ExecutableElement, MappingMethod>
    {
        @Override
        public void addMapping(final ObfuscationType obfuscationType, final IMapping<?> obfuscatedName) {
            AnnotatedMixinElementHandlerShadow.this.addMethodMapping(obfuscationType, this.setObfuscatedName(obfuscatedName), this.getDesc(), obfuscatedName.getDesc());
        }
        
        public AnnotatedElementShadowMethod(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
            super(executableElement, annotationHandle, b, IMapping.Type.METHOD);
        }
        
        @Override
        public MappingMethod getMapping(final TypeHandle typeHandle, final String s, final String s2) {
            return typeHandle.getMappingMethod(s, s2);
        }
    }
}
