// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import javax.lang.model.element.VariableElement;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.asm.mixin.Mixin;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.Pseudo;
import java.util.ArrayList;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import java.util.Iterator;
import java.util.Collection;
import org.spongepowered.tools.obfuscation.interfaces.IMixinValidator;
import javax.lang.model.element.ElementKind;
import org.spongepowered.tools.obfuscation.interfaces.ITypeHandleProvider;
import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.interfaces.IObfuscationManager;
import javax.lang.model.element.TypeElement;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import java.util.List;

class AnnotatedMixin
{
    private final /* synthetic */ List<ExecutableElement> methods;
    private final /* synthetic */ AnnotationHandle annotation;
    private final /* synthetic */ AnnotatedMixinElementHandlerShadow shadows;
    private final /* synthetic */ TypeHandle handle;
    private final /* synthetic */ boolean virtual;
    private final /* synthetic */ AnnotatedMixinElementHandlerOverwrite overwrites;
    private final /* synthetic */ Messager messager;
    private final /* synthetic */ TypeElement mixin;
    private /* synthetic */ boolean validated;
    private final /* synthetic */ AnnotatedMixinElementHandlerSoftImplements softImplements;
    private final /* synthetic */ TypeHandle primaryTarget;
    private final /* synthetic */ String classRef;
    private final /* synthetic */ IObfuscationManager obf;
    private final /* synthetic */ AnnotatedMixinElementHandlerAccessor accessors;
    private final /* synthetic */ AnnotatedMixinElementHandlerInjector injectors;
    private final /* synthetic */ IMappingConsumer mappings;
    private final /* synthetic */ List<TypeHandle> targets;
    private final /* synthetic */ boolean remap;
    private final /* synthetic */ ITypeHandleProvider typeProvider;
    
    public boolean isInterface() {
        return this.mixin.getKind() == ElementKind.INTERFACE;
    }
    
    public String getClassRef() {
        return this.classRef;
    }
    
    AnnotatedMixin runValidators(final IMixinValidator.ValidationPass validationPass, final Collection<IMixinValidator> collection) {
        final Iterator<IMixinValidator> iterator = collection.iterator();
        while (iterator.hasNext() && iterator.next().validate(validationPass, this.mixin, this.annotation, this.targets)) {}
        if (validationPass == IMixinValidator.ValidationPass.FINAL && !this.validated) {
            this.validated = true;
            this.runFinalValidation();
        }
        return this;
    }
    
    public boolean remap() {
        return this.remap;
    }
    
    public void registerInvoker(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
        this.methods.remove(executableElement);
        this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementInvoker(executableElement, annotationHandle, b));
    }
    
    public void registerInjectionPoint(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final AnnotationHandle annotationHandle2, final InjectorRemap injectorRemap, final String s) {
        this.injectors.registerInjectionPoint(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjectionPoint(executableElement, annotationHandle, annotationHandle2, injectorRemap), s);
    }
    
    public void registerOverwrite(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
        this.methods.remove(executableElement);
        this.overwrites.registerOverwrite(new AnnotatedMixinElementHandlerOverwrite.AnnotatedElementOverwrite(executableElement, annotationHandle, b));
    }
    
    private void runFinalValidation() {
        final Iterator<ExecutableElement> iterator = this.methods.iterator();
        while (iterator.hasNext()) {
            this.overwrites.registerMerge(iterator.next());
        }
    }
    
    private void addSoftTarget(final TypeHandle typeHandle, final String s) {
        final ObfuscationData<String> obfClass = this.obf.getDataProvider().getObfClass(typeHandle);
        if (!obfClass.isEmpty()) {
            this.obf.getReferenceManager().addClassMapping(this.classRef, s, obfClass);
        }
        this.addTarget(typeHandle);
    }
    
    public void registerInjector(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final InjectorRemap injectorRemap) {
        this.methods.remove(executableElement);
        this.injectors.registerInjector(new AnnotatedMixinElementHandlerInjector.AnnotatedElementInjector(executableElement, annotationHandle, injectorRemap));
        final Iterator<AnnotationHandle> iterator = annotationHandle.getAnnotationList("at").iterator();
        while (iterator.hasNext()) {
            this.registerInjectionPoint(executableElement, annotationHandle, iterator.next(), injectorRemap, "@At(%s)");
        }
        for (final AnnotationHandle annotationHandle2 : annotationHandle.getAnnotationList("slice")) {
            final String s = annotationHandle2.getValue("id", "");
            final AnnotationHandle annotation = annotationHandle2.getAnnotation("from");
            if (annotation != null) {
                this.registerInjectionPoint(executableElement, annotationHandle, annotation, injectorRemap, "@Slice[" + s + "](from=@At(%s))");
            }
            final AnnotationHandle annotation2 = annotationHandle2.getAnnotation("to");
            if (annotation2 != null) {
                this.registerInjectionPoint(executableElement, annotationHandle, annotation2, injectorRemap, "@Slice[" + s + "](to=@At(%s))");
            }
        }
    }
    
    @Deprecated
    public TypeHandle getPrimaryTarget() {
        return this.primaryTarget;
    }
    
    public TypeElement getMixin() {
        return this.mixin;
    }
    
    private void printMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final AnnotatedMixin annotatedMixin) {
        this.messager.printMessage(kind, charSequence, this.mixin, this.annotation.asMirror());
    }
    
    public void registerAccessor(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
        this.methods.remove(executableElement);
        this.accessors.registerAccessor(new AnnotatedMixinElementHandlerAccessor.AnnotatedElementAccessor(executableElement, annotationHandle, b));
    }
    
    public IMappingConsumer getMappings() {
        return this.mappings;
    }
    
    public void registerSoftImplements(final AnnotationHandle annotationHandle) {
        this.softImplements.process(annotationHandle);
    }
    
    private TypeHandle initTargets() {
        TypeHandle typeHandle = null;
        try {
            final Iterator<TypeMirror> iterator = this.annotation.getList().iterator();
            while (iterator.hasNext()) {
                final TypeHandle typeHandle2 = new TypeHandle((DeclaredType)iterator.next());
                if (this.targets.contains(typeHandle2)) {
                    continue;
                }
                this.addTarget(typeHandle2);
                if (typeHandle != null) {
                    continue;
                }
                typeHandle = typeHandle2;
            }
        }
        catch (Exception ex) {
            this.printMessage(Diagnostic.Kind.WARNING, "Error processing public targets: " + ex.getClass().getName() + ": " + ex.getMessage(), this);
        }
        try {
            for (final String s : this.annotation.getList("targets")) {
                TypeHandle typeHandle3 = this.typeProvider.getTypeHandle(s);
                if (this.targets.contains(typeHandle3)) {
                    continue;
                }
                if (this.virtual) {
                    typeHandle3 = this.typeProvider.getSimulatedHandle(s, this.mixin.asType());
                }
                else {
                    if (typeHandle3 == null) {
                        this.printMessage(Diagnostic.Kind.ERROR, "Mixin target " + s + " could not be found", this);
                        return null;
                    }
                    if (typeHandle3.isPublic()) {
                        this.printMessage(Diagnostic.Kind.WARNING, "Mixin target " + s + " is public and must be specified in value", this);
                        return null;
                    }
                }
                this.addSoftTarget(typeHandle3, s);
                if (typeHandle != null) {
                    continue;
                }
                typeHandle = typeHandle3;
            }
        }
        catch (Exception ex2) {
            this.printMessage(Diagnostic.Kind.WARNING, "Error processing private targets: " + ex2.getClass().getName() + ": " + ex2.getMessage(), this);
        }
        if (typeHandle == null) {
            this.printMessage(Diagnostic.Kind.ERROR, "Mixin has no targets", this);
        }
        return typeHandle;
    }
    
    private void addTarget(final TypeHandle typeHandle) {
        this.targets.add(typeHandle);
    }
    
    public AnnotatedMixin(final IMixinAnnotationProcessor messager, final TypeElement mixin) {
        this.targets = new ArrayList<TypeHandle>();
        this.validated = false;
        this.typeProvider = messager.getTypeProvider();
        this.obf = messager.getObfuscationManager();
        this.mappings = this.obf.createMappingConsumer();
        this.messager = messager;
        this.mixin = mixin;
        this.handle = new TypeHandle(mixin);
        this.methods = new ArrayList<ExecutableElement>((Collection<? extends ExecutableElement>)this.handle.getEnclosedElements(ElementKind.METHOD));
        this.virtual = this.handle.getAnnotation(Pseudo.class).exists();
        this.annotation = this.handle.getAnnotation(Mixin.class);
        this.classRef = TypeUtils.getInternalName(mixin);
        this.primaryTarget = this.initTargets();
        this.remap = (this.annotation.getBoolean("remap", true) && this.targets.size() > 0);
        this.overwrites = new AnnotatedMixinElementHandlerOverwrite(messager, this);
        this.shadows = new AnnotatedMixinElementHandlerShadow(messager, this);
        this.injectors = new AnnotatedMixinElementHandlerInjector(messager, this);
        this.accessors = new AnnotatedMixinElementHandlerAccessor(messager, this);
        this.softImplements = new AnnotatedMixinElementHandlerSoftImplements(messager, this);
    }
    
    @Override
    public String toString() {
        return this.mixin.getSimpleName().toString();
    }
    
    public List<TypeHandle> getTargets() {
        return this.targets;
    }
    
    public void registerShadow(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
        this.methods.remove(executableElement);
        this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowMethod(executableElement, annotationHandle, b));
    }
    
    public AnnotationHandle getAnnotation() {
        return this.annotation;
    }
    
    public boolean isMultiTarget() {
        return this.targets.size() > 1;
    }
    
    public void registerShadow(final VariableElement variableElement, final AnnotationHandle annotationHandle, final boolean b) {
        this.shadows.registerShadow(this.shadows.new AnnotatedElementShadowField(variableElement, annotationHandle, b));
    }
    
    public TypeHandle getHandle() {
        return this.handle;
    }
}
