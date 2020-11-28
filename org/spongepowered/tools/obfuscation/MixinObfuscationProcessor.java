// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.util.Iterator;
import javax.tools.Diagnostic;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Mixin;
import javax.annotation.processing.RoundEnvironment;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.annotation.processing.AbstractProcessor;

public abstract class MixinObfuscationProcessor extends AbstractProcessor
{
    protected /* synthetic */ AnnotatedMixins mixins;
    
    @Override
    public SourceVersion getSupportedSourceVersion() {
        try {
            return SourceVersion.valueOf("RELEASE_8");
        }
        catch (IllegalArgumentException ex) {
            return super.getSupportedSourceVersion();
        }
    }
    
    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.mixins = AnnotatedMixins.getMixinsForEnvironment(processingEnv);
    }
    
    @Override
    public Set<String> getSupportedOptions() {
        return SupportedOptions.getAllOptions();
    }
    
    protected void postProcess(final RoundEnvironment roundEnvironment) {
        this.mixins.onPassCompleted(roundEnvironment);
    }
    
    protected void processMixins(final RoundEnvironment roundEnvironment) {
        this.mixins.onPassStarted();
        for (final Element element : roundEnvironment.getElementsAnnotatedWith(Mixin.class)) {
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE) {
                this.mixins.registerMixin((TypeElement)element);
            }
            else {
                this.mixins.printMessage(Diagnostic.Kind.ERROR, "Found an @Mixin annotation on an element which is not a class or interface", element);
            }
        }
    }
}
