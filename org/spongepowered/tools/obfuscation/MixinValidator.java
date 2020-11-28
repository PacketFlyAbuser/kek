// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.lang.model.element.Element;
import java.util.Collection;
import javax.lang.model.type.TypeMirror;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.interfaces.IOptionProvider;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;

public abstract class MixinValidator implements IMixinValidator
{
    protected final /* synthetic */ IOptionProvider options;
    protected final /* synthetic */ Messager messager;
    protected final /* synthetic */ ValidationPass pass;
    protected final /* synthetic */ ProcessingEnvironment processingEnv;
    
    protected final Collection<TypeMirror> getMixinsTargeting(final TypeMirror typeMirror) {
        return AnnotatedMixins.getMixinsForEnvironment(this.processingEnv).getMixinsTargeting(typeMirror);
    }
    
    protected final void error(final String s, final Element element) {
        this.messager.printMessage(Diagnostic.Kind.ERROR, s, element);
    }
    
    protected final void note(final String s, final Element element) {
        this.messager.printMessage(Diagnostic.Kind.NOTE, s, element);
    }
    
    protected final void warning(final String s, final Element element) {
        this.messager.printMessage(Diagnostic.Kind.WARNING, s, element);
    }
    
    @Override
    public final boolean validate(final ValidationPass validationPass, final TypeElement typeElement, final AnnotationHandle annotationHandle, final Collection<TypeHandle> collection) {
        return validationPass != this.pass || this.validate(typeElement, annotationHandle, collection);
    }
    
    public MixinValidator(final IMixinAnnotationProcessor mixinAnnotationProcessor, final ValidationPass pass) {
        this.processingEnv = mixinAnnotationProcessor.getProcessingEnvironment();
        this.messager = mixinAnnotationProcessor;
        this.options = mixinAnnotationProcessor;
        this.pass = pass;
    }
    
    protected abstract boolean validate(final TypeElement p0, final AnnotationHandle p1, final Collection<TypeHandle> p2);
}
