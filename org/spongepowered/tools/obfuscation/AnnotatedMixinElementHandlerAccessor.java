// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.spongepowered.tools.obfuscation.mirror.TypeUtils;
import org.spongepowered.tools.obfuscation.mirror.AnnotationHandle;
import javax.lang.model.element.ExecutableElement;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.mirror.MethodHandle;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import com.google.common.base.Strings;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.tools.obfuscation.mirror.FieldHandle;
import java.util.Iterator;
import org.spongepowered.asm.mixin.gen.AccessorInfo;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.refmap.IMixinContext;

public class AnnotatedMixinElementHandlerAccessor extends AnnotatedMixinElementHandler implements IMixinContext
{
    @Override
    public IMixinInfo getMixin() {
        throw new UnsupportedOperationException("MixinInfo not available at compile time");
    }
    
    public void registerAccessor(final AnnotatedElementAccessor annotatedElementAccessor) {
        if (annotatedElementAccessor.getAccessorType() == null) {
            annotatedElementAccessor.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unsupported accessor type");
            return;
        }
        final String accessorTargetName = this.getAccessorTargetName(annotatedElementAccessor);
        if (accessorTargetName == null) {
            annotatedElementAccessor.printMessage(this.ap, Diagnostic.Kind.WARNING, "Cannot inflect accessor target name");
            return;
        }
        annotatedElementAccessor.setTargetName(accessorTargetName);
        for (final TypeHandle typeHandle : this.mixin.getTargets()) {
            if (annotatedElementAccessor.getAccessorType() == AccessorInfo.AccessorType.METHOD_PROXY) {
                this.registerInvokerForTarget((AnnotatedElementInvoker)annotatedElementAccessor, typeHandle);
            }
            else {
                this.registerAccessorForTarget(annotatedElementAccessor, typeHandle);
            }
        }
    }
    
    private void registerAccessorForTarget(final AnnotatedElementAccessor obj, final TypeHandle obj2) {
        FieldHandle field = obj2.findField(obj.getTargetName(), obj.getTargetTypeName(), false);
        if (field == null) {
            if (!obj2.isImaginary()) {
                obj.printMessage(this.ap, Diagnostic.Kind.ERROR, "Could not locate @Accessor target " + obj + " in target " + obj2);
                return;
            }
            field = new FieldHandle(obj2.getName(), obj.getTargetName(), obj.getDesc());
        }
        if (!obj.shouldRemap()) {
            return;
        }
        final ObfuscationData<MappingField> obfField = this.obf.getDataProvider().getObfField(field.asMapping(false).move(obj2.getName()));
        if (obfField.isEmpty()) {
            obj.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + (this.mixin.isMultiTarget() ? (" in target " + obj2) : "") + " for @Accessor target " + obj);
            return;
        }
        final ObfuscationData<MappingField> stripOwnerData = AnnotatedMixinElementHandler.stripOwnerData(obfField);
        try {
            this.obf.getReferenceManager().addFieldMapping(this.mixin.getClassRef(), obj.getTargetName(), obj.getContext(), stripOwnerData);
        }
        catch (ReferenceManager.ReferenceConflictException ex) {
            obj.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Accessor target " + obj + ": " + ex.getNew() + " for target " + obj2 + " conflicts with existing mapping " + ex.getOld());
        }
    }
    
    @Override
    public int getPriority() {
        throw new UnsupportedOperationException("Priority not available at compile time");
    }
    
    @Override
    public String getClassRef() {
        return this.mixin.getClassRef();
    }
    
    private String getAccessorTargetName(final AnnotatedElementAccessor annotatedElementAccessor) {
        final String annotationValue = annotatedElementAccessor.getAnnotationValue();
        if (Strings.isNullOrEmpty(annotationValue)) {
            return this.inflectAccessorTarget(annotatedElementAccessor);
        }
        return annotationValue;
    }
    
    @Override
    public Extensions getExtensions() {
        throw new UnsupportedOperationException("Mixin Extensions not available at compile time");
    }
    
    private void registerInvokerForTarget(final AnnotatedElementInvoker obj, final TypeHandle obj2) {
        MethodHandle method = obj2.findMethod(obj.getTargetName(), obj.getTargetTypeName(), false);
        if (method == null) {
            if (!obj2.isImaginary()) {
                obj.printMessage(this.ap, Diagnostic.Kind.ERROR, "Could not locate @Invoker target " + obj + " in target " + obj2);
                return;
            }
            method = new MethodHandle(obj2, obj.getTargetName(), obj.getDesc());
        }
        if (!obj.shouldRemap()) {
            return;
        }
        final ObfuscationData<MappingMethod> obfMethod = this.obf.getDataProvider().getObfMethod(method.asMapping(false).move(obj2.getName()));
        if (obfMethod.isEmpty()) {
            obj.printMessage(this.ap, Diagnostic.Kind.WARNING, "Unable to locate obfuscation mapping" + (this.mixin.isMultiTarget() ? (" in target " + obj2) : "") + " for @Accessor target " + obj);
            return;
        }
        final ObfuscationData<MappingMethod> stripOwnerData = AnnotatedMixinElementHandler.stripOwnerData(obfMethod);
        try {
            this.obf.getReferenceManager().addMethodMapping(this.mixin.getClassRef(), obj.getTargetName(), obj.getContext(), stripOwnerData);
        }
        catch (ReferenceManager.ReferenceConflictException ex) {
            obj.printMessage(this.ap, Diagnostic.Kind.ERROR, "Mapping conflict for @Invoker target " + obj + ": " + ex.getNew() + " for target " + obj2 + " conflicts with existing mapping " + ex.getOld());
        }
    }
    
    @Override
    public boolean getOption(final MixinEnvironment.Option option) {
        throw new UnsupportedOperationException("Options not available at compile time");
    }
    
    @Override
    public ReferenceMapper getReferenceMapper() {
        return null;
    }
    
    private String inflectAccessorTarget(final AnnotatedElementAccessor annotatedElementAccessor) {
        return AccessorInfo.inflectTarget(annotatedElementAccessor.getSimpleName(), annotatedElementAccessor.getAccessorType(), "", this, false);
    }
    
    @Override
    public String getTargetClassRef() {
        throw new UnsupportedOperationException("Target class not available at compile time");
    }
    
    public AnnotatedMixinElementHandlerAccessor(final IMixinAnnotationProcessor mixinAnnotationProcessor, final AnnotatedMixin annotatedMixin) {
        super(mixinAnnotationProcessor, annotatedMixin);
    }
    
    @Override
    public Target getTargetMethod(final MethodNode methodNode) {
        throw new UnsupportedOperationException("Target not available at compile time");
    }
    
    static class AnnotatedElementInvoker extends AnnotatedElementAccessor
    {
        public AnnotatedElementInvoker(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean b) {
            super(executableElement, annotationHandle, b);
        }
        
        @Override
        public AccessorInfo.AccessorType getAccessorType() {
            return AccessorInfo.AccessorType.METHOD_PROXY;
        }
        
        @Override
        public String getTargetTypeName() {
            return TypeUtils.getJavaSignature(((AnnotatedElement<Element>)this).getElement());
        }
        
        @Override
        public String getAccessorDesc() {
            return TypeUtils.getDescriptor(this.getElement());
        }
    }
    
    static class AnnotatedElementAccessor extends AnnotatedElement<ExecutableElement>
    {
        private final /* synthetic */ boolean shouldRemap;
        private final /* synthetic */ TypeMirror returnType;
        private /* synthetic */ String targetName;
        
        public boolean shouldRemap() {
            return this.shouldRemap;
        }
        
        public String getAnnotationValue() {
            return this.getAnnotation().getValue();
        }
        
        public AccessorInfo.AccessorType getAccessorType() {
            return (this.returnType.getKind() == TypeKind.VOID) ? AccessorInfo.AccessorType.FIELD_SETTER : AccessorInfo.AccessorType.FIELD_GETTER;
        }
        
        public String getTargetName() {
            return this.targetName;
        }
        
        public String getTargetTypeName() {
            return TypeUtils.getTypeName(this.getTargetType());
        }
        
        @Override
        public String toString() {
            return (this.targetName != null) ? this.targetName : "<invalid>";
        }
        
        public MemberInfo getContext() {
            return new MemberInfo(this.getTargetName(), null, this.getAccessorDesc());
        }
        
        public AnnotatedElementAccessor(final ExecutableElement executableElement, final AnnotationHandle annotationHandle, final boolean shouldRemap) {
            super(executableElement, annotationHandle);
            this.shouldRemap = shouldRemap;
            this.returnType = this.getElement().getReturnType();
        }
        
        public String getAccessorDesc() {
            return TypeUtils.getInternalName(this.getTargetType());
        }
        
        public TypeMirror getTargetType() {
            switch (this.getAccessorType()) {
                case FIELD_GETTER: {
                    return this.returnType;
                }
                case FIELD_SETTER: {
                    return ((VariableElement)this.getElement().getParameters().get(0)).asType();
                }
                default: {
                    return null;
                }
            }
        }
        
        public void setTargetName(final String targetName) {
            this.targetName = targetName;
        }
    }
}
