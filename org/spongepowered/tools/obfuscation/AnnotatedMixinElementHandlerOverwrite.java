// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import javax.lang.model.element.Element;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;

class AnnotatedMixinElementHandlerOverwrite extends AnnotatedMixinElementHandler
{
    AnnotatedMixinElementHandlerOverwrite(final IMixinAnnotationProcessor mixinAnnotationProcessor, final AnnotatedMixin annotatedMixin) {
        super(mixinAnnotationProcessor, annotatedMixin);
    }
    
    private boolean registerOverwriteForTarget(final AnnotatedElementOverwrite annotatedElementOverwrite, final TypeHandle obj) {
        final ObfuscationData<MappingMethod> obfMethod = this.obf.getDataProvider().getObfMethod(obj.getMappingMethod(annotatedElementOverwrite.getSimpleName(), annotatedElementOverwrite.getDesc()));
        if (obfMethod.isEmpty()) {
            Diagnostic.Kind kind = Diagnostic.Kind.ERROR;
            try {
                if (annotatedElementOverwrite.getElement().getClass().getMethod("isStatic", (Class<?>[])new Class[0]).invoke(((AnnotatedElement<Object>)annotatedElementOverwrite).getElement(), new Object[0])) {
                    kind = Diagnostic.Kind.WARNING;
                }
            }
            catch (Exception ex2) {}
            this.ap.printMessage(kind, "No obfuscation mapping for @Overwrite method", ((AnnotatedElement<Element>)annotatedElementOverwrite).getElement());
            return false;
        }
        try {
            this.addMethodMappings(annotatedElementOverwrite.getSimpleName(), annotatedElementOverwrite.getDesc(), obfMethod);
        }
        catch (Mappings.MappingConflictException ex) {
            annotatedElementOverwrite.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Overwrite method: " + ex.getNew().getSimpleName() + " for target " + obj + " conflicts with existing mapping " + ex.getOld().getSimpleName());
            return false;
        }
        return true;
    }
    
    public void registerMerge(final ExecutableElement executableElement) {
        this.validateTargetMethod(executableElement, null, new AliasedElementName(executableElement, AnnotationHandle.MISSING), "overwrite", true, true);
    }
    
    public void registerOverwrite(final AnnotatedElementOverwrite annotatedElementOverwrite) {
        this.validateTargetMethod(annotatedElementOverwrite.getElement(), annotatedElementOverwrite.getAnnotation(), new AliasedElementName(((AnnotatedElement<Element>)annotatedElementOverwrite).getElement(), annotatedElementOverwrite.getAnnotation()), "@Overwrite", true, false);
        this.checkConstraints(annotatedElementOverwrite.getElement(), annotatedElementOverwrite.getAnnotation());
        if (annotatedElementOverwrite.shouldRemap()) {
            final Iterator<TypeHandle> iterator = this.mixin.getTargets().iterator();
            while (iterator.hasNext()) {
                if (!this.registerOverwriteForTarget(annotatedElementOverwrite, iterator.next())) {
                    return;
                }
            }
        }
        if (!"true".equalsIgnoreCase(this.ap.getOption("disableOverwriteChecker"))) {
            final Diagnostic.Kind kind = "error".equalsIgnoreCase(this.ap.getOption("overwriteErrorLevel")) ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
            final String javadoc = this.ap.getJavadocProvider().getJavadoc(((AnnotatedElement<Element>)annotatedElementOverwrite).getElement());
            if (javadoc == null) {
                this.ap.printMessage(kind, "@Overwrite is missing javadoc comment", ((AnnotatedElement<Element>)annotatedElementOverwrite).getElement());
                return;
            }
            if (!javadoc.toLowerCase().contains("@author")) {
                this.ap.printMessage(kind, "@Overwrite is missing an @author tag", ((AnnotatedElement<Element>)annotatedElementOverwrite).getElement());
            }
            if (!javadoc.toLowerCase().contains("@reason")) {
                this.ap.printMessage(kind, "@Overwrite is missing an @reason tag", ((AnnotatedElement<Element>)annotatedElementOverwrite).getElement());
            }
        }
    }
    
    static class AnnotatedElementOverwrite extends AnnotatedElement<ExecutableElement>
    {
        private final /* synthetic */ boolean shouldRemap;
        
        public boolean shouldRemap() {
            return this.shouldRemap;
        }
        
        public AnnotatedElementOverwrite(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean shouldRemap) {
            super(executableElement, annotationHandle);
            this.shouldRemap = shouldRemap;
        }
    }
}
