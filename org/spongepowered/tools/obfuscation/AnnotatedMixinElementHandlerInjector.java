// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.lang.annotation.Annotation;
import javax.lang.model.element.Element;
import org.spongepowered.asm.mixin.injection.Coerce;
import javax.lang.model.element.VariableElement;
import java.util.HashMap;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import org.spongepowered.tools.obfuscation.struct.InjectorRemap;
import java.util.Map;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import javax.lang.model.element.AnnotationMirror;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import java.util.Iterator;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.mixin.injection.struct.InvalidMemberDescriptorException;
import javax.annotation.processing.Messager;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import javax.tools.Diagnostic;

class AnnotatedMixinElementHandlerInjector extends AnnotatedMixinElementHandler
{
    public void registerInjector(final AnnotatedElementInjector annotatedElementInjector) {
        if (this.mixin.isInterface()) {
            this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", ((AnnotatedElement<Element>)annotatedElementInjector).getElement());
        }
        for (final String s : annotatedElementInjector.getAnnotation().getList("method")) {
            final MemberInfo parse = MemberInfo.parse(s);
            if (parse.name == null) {
                continue;
            }
            try {
                parse.validate();
            }
            catch (InvalidMemberDescriptorException ex) {
                annotatedElementInjector.printMessage(this.ap, Diagnostic.Kind.ERROR, ex.getMessage());
            }
            if (parse.desc != null) {
                this.validateReferencedTarget(annotatedElementInjector.getElement(), annotatedElementInjector.getAnnotation(), parse, annotatedElementInjector.toString());
            }
            if (!annotatedElementInjector.shouldRemap()) {
                continue;
            }
            final Iterator<TypeHandle> iterator2 = this.mixin.getTargets().iterator();
            while (iterator2.hasNext() && this.registerInjector(annotatedElementInjector, s, parse, iterator2.next())) {}
        }
    }
    
    public void registerInjectionPoint(final AnnotatedElementInjectionPoint annotatedElementInjectionPoint, final String format) {
        if (this.mixin.isInterface()) {
            this.ap.printMessage(Diagnostic.Kind.ERROR, "Injector in interface is unsupported", ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement());
        }
        if (!annotatedElementInjectionPoint.shouldRemap()) {
            return;
        }
        final String type = InjectionPointData.parseType(annotatedElementInjectionPoint.getAt().getValue("value"));
        final String s = annotatedElementInjectionPoint.getAt().getValue("target");
        if ("NEW".equals(type)) {
            this.remapNewTarget(String.format(format, type + ".<target>"), s, annotatedElementInjectionPoint);
            this.remapNewTarget(String.format(format, type + ".args[class]"), annotatedElementInjectionPoint.getAtArg("class"), annotatedElementInjectionPoint);
        }
        else {
            this.remapReference(String.format(format, type + ".<target>"), s, annotatedElementInjectionPoint);
        }
    }
    
    private boolean registerInjector(final AnnotatedElementInjector annotatedElementInjector, final String s, final MemberInfo memberInfo, final TypeHandle obj) {
        final String descriptor = obj.findDescriptor(memberInfo);
        if (descriptor == null) {
            final Diagnostic.Kind kind = this.mixin.isMultiTarget() ? Diagnostic.Kind.ERROR : Diagnostic.Kind.WARNING;
            if (obj.isSimulated()) {
                annotatedElementInjector.printMessage(this.ap, Diagnostic.Kind.NOTE, annotatedElementInjector + " target '" + s + "' in @Pseudo mixin will not be obfuscated");
            }
            else if (obj.isImaginary()) {
                annotatedElementInjector.printMessage(this.ap, kind, annotatedElementInjector + " target requires method signature because enclosing type information for " + obj + " is unavailable");
            }
            else if (!memberInfo.isInitialiser()) {
                annotatedElementInjector.printMessage(this.ap, kind, "Unable to determine signature for " + annotatedElementInjector + " target method");
            }
            return true;
        }
        final String string = annotatedElementInjector + " target " + memberInfo.name;
        final MappingMethod mappingMethod = obj.getMappingMethod(memberInfo.name, descriptor);
        Iterable<ObfuscationType> iterable = this.obf.getDataProvider().getObfMethod(mappingMethod);
        if (((ObfuscationData)iterable).isEmpty()) {
            if (obj.isSimulated()) {
                iterable = this.obf.getDataProvider().getRemappedMethod(mappingMethod);
            }
            else {
                if (memberInfo.isClassInitialiser()) {
                    return true;
                }
                annotatedElementInjector.addMessage(memberInfo.isConstructor() ? Diagnostic.Kind.WARNING : Diagnostic.Kind.ERROR, "No obfuscation mapping for " + string, ((AnnotatedElement<Element>)annotatedElementInjector).getElement(), annotatedElementInjector.getAnnotation());
                return false;
            }
        }
        final IReferenceManager referenceManager = this.obf.getReferenceManager();
        try {
            if ((memberInfo.owner == null && this.mixin.isMultiTarget()) || obj.isSimulated()) {
                iterable = AnnotatedMixinElementHandler.stripOwnerData((ObfuscationData<IMapping>)iterable);
            }
            referenceManager.addMethodMapping(this.classRef, s, (ObfuscationData<MappingMethod>)iterable);
        }
        catch (ReferenceManager.ReferenceConflictException ex) {
            final String s2 = this.mixin.isMultiTarget() ? "Multi-target" : "Target";
            if (annotatedElementInjector.hasCoerceArgument() && memberInfo.owner == null && memberInfo.desc == null && MemberInfo.parse(ex.getOld()).name.equals(MemberInfo.parse(ex.getNew()).name)) {
                final ObfuscationData<MappingMethod> stripDescriptors = AnnotatedMixinElementHandler.stripDescriptors((ObfuscationData<MappingMethod>)iterable);
                referenceManager.setAllowConflicts(true);
                referenceManager.addMethodMapping(this.classRef, s, stripDescriptors);
                referenceManager.setAllowConflicts(false);
                annotatedElementInjector.printMessage(this.ap, Diagnostic.Kind.WARNING, "Coerced " + s2 + " reference has conflicting descriptors for " + string + ": Storing bare references " + stripDescriptors.values() + " in refMap");
                return true;
            }
            annotatedElementInjector.printMessage(this.ap, Diagnostic.Kind.ERROR, s2 + " reference conflict for " + string + ": " + s + " -> " + ex.getNew() + " previously defined as " + ex.getOld());
        }
        return true;
    }
    
    AnnotatedMixinElementHandlerInjector(final IMixinAnnotationProcessor mixinAnnotationProcessor, final AnnotatedMixin annotatedMixin) {
        super(mixinAnnotationProcessor, annotatedMixin);
    }
    
    protected final void remapReference(final String s, final String str, final AnnotatedElementInjectionPoint annotatedElementInjectionPoint) {
        if (str == null) {
            return;
        }
        final AnnotationMirror mirror = ((this.ap.getCompilerEnvironment() == IMixinAnnotationProcessor.CompilerEnvironment.JDT) ? annotatedElementInjectionPoint.getAt() : annotatedElementInjectionPoint.getAnnotation()).asMirror();
        final MemberInfo parse = MemberInfo.parse(str);
        if (!parse.isFullyQualified()) {
            this.ap.printMessage(Diagnostic.Kind.ERROR, s + " is not fully qualified, missing " + ((parse.owner == null) ? ((parse.desc == null) ? "owner and signature" : "owner") : "signature"), ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), mirror);
            return;
        }
        try {
            parse.validate();
        }
        catch (InvalidMemberDescriptorException ex) {
            this.ap.printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), mirror);
        }
        try {
            if (parse.isField()) {
                final ObfuscationData<MappingField> obfFieldRecursive = this.obf.getDataProvider().getObfFieldRecursive(parse);
                if (obfFieldRecursive.isEmpty()) {
                    this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find field mapping for " + s + " '" + str + "'", ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), mirror);
                    return;
                }
                this.obf.getReferenceManager().addFieldMapping(this.classRef, str, parse, obfFieldRecursive);
            }
            else {
                final ObfuscationData<MappingMethod> obfMethodRecursive = this.obf.getDataProvider().getObfMethodRecursive(parse);
                if (obfMethodRecursive.isEmpty() && (parse.owner == null || !parse.owner.startsWith("java/lang/"))) {
                    this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find method mapping for " + s + " '" + str + "'", ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), mirror);
                    return;
                }
                this.obf.getReferenceManager().addMethodMapping(this.classRef, str, parse, obfMethodRecursive);
            }
        }
        catch (ReferenceManager.ReferenceConflictException ex2) {
            this.ap.printMessage(Diagnostic.Kind.ERROR, "Unexpected reference conflict for " + s + ": " + str + " -> " + ex2.getNew() + " previously defined as " + ex2.getOld(), ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), mirror);
            return;
        }
        annotatedElementInjectionPoint.notifyRemapped();
    }
    
    protected final void remapNewTarget(final String str, final String s, final AnnotatedElementInjectionPoint annotatedElementInjectionPoint) {
        if (s == null) {
            return;
        }
        final MemberInfo parse = MemberInfo.parse(s);
        final String ctorType = parse.toCtorType();
        if (ctorType != null) {
            final String ctorDesc = parse.toCtorDesc();
            final ObfuscationData<MappingMethod> remappedMethod = this.obf.getDataProvider().getRemappedMethod(new MappingMethod(ctorType, ".", (ctorDesc != null) ? ctorDesc : "()V"));
            if (remappedMethod.isEmpty()) {
                this.ap.printMessage(Diagnostic.Kind.WARNING, "Cannot find class mapping for " + str + " '" + ctorType + "'", ((AnnotatedElement<Element>)annotatedElementInjectionPoint).getElement(), annotatedElementInjectionPoint.getAnnotation().asMirror());
                return;
            }
            final ObfuscationData<String> obfuscationData = new ObfuscationData<String>();
            for (final ObfuscationType obfuscationType : remappedMethod) {
                final MappingMethod mappingMethod = remappedMethod.get(obfuscationType);
                if (ctorDesc == null) {
                    obfuscationData.put(obfuscationType, mappingMethod.getOwner());
                }
                else {
                    obfuscationData.put(obfuscationType, mappingMethod.getDesc().replace(")V", ")L" + mappingMethod.getOwner() + ";"));
                }
            }
            this.obf.getReferenceManager().addClassMapping(this.classRef, s, obfuscationData);
        }
        annotatedElementInjectionPoint.notifyRemapped();
    }
    
    static class AnnotatedElementInjectionPoint extends AnnotatedElement<ExecutableElement>
    {
        private /* synthetic */ Map<String, String> args;
        private final /* synthetic */ InjectorRemap state;
        private final /* synthetic */ AnnotationHandle at;
        
        public boolean shouldRemap() {
            return this.at.getBoolean("remap", this.state.shouldRemap());
        }
        
        public AnnotatedElementInjectionPoint(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final AnnotationHandle at, final InjectorRemap state) {
            super(executableElement, annotationHandle);
            this.at = at;
            this.state = state;
        }
        
        public AnnotationHandle getAt() {
            return this.at;
        }
        
        public void notifyRemapped() {
            this.state.notifyRemapped();
        }
        
        public String getAtArg(final String s) {
            if (this.args == null) {
                this.args = new HashMap<String, String>();
                for (final String s2 : this.at.getList("args")) {
                    if (s2 == null) {
                        continue;
                    }
                    final int index = s2.indexOf(61);
                    if (index > -1) {
                        this.args.put(s2.substring(0, index), s2.substring(index + 1));
                    }
                    else {
                        this.args.put(s2, "");
                    }
                }
            }
            return this.args.get(s);
        }
    }
    
    static class AnnotatedElementInjector extends AnnotatedElement<ExecutableElement>
    {
        private final /* synthetic */ InjectorRemap state;
        
        public boolean hasCoerceArgument() {
            if (!this.annotation.toString().equals("@Inject")) {
                return false;
            }
            final Iterator<? extends VariableElement> iterator = ((ExecutableElement)this.element).getParameters().iterator();
            return iterator.hasNext() && AnnotationHandle.of((Element)iterator.next(), Coerce.class).exists();
        }
        
        public AnnotatedElementInjector(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final InjectorRemap state) {
            super(executableElement, annotationHandle);
            this.state = state;
        }
        
        public void addMessage(final Diagnostic.Kind kind, final CharSequence charSequence, final Element element, final AnnotationHandle annotationHandle) {
            this.state.addMessage(kind, charSequence, element, annotationHandle);
        }
        
        @Override
        public String toString() {
            return this.getAnnotation().toString();
        }
        
        public boolean shouldRemap() {
            return this.state.shouldRemap();
        }
    }
}
