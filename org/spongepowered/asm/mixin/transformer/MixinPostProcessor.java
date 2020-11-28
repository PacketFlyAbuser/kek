// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.FieldVisitor;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.lib.ClassReader;
import java.util.HashMap;
import java.util.HashSet;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.transformers.TreeTransformer;

class MixinPostProcessor extends TreeTransformer implements MixinConfig.IListener
{
    private final /* synthetic */ Set<String> loadable;
    private final /* synthetic */ Set<String> syntheticInnerClasses;
    private final /* synthetic */ Map<String, MixinInfo> accessorMixins;
    
    private static void createProxy(final MethodNode methodNode, final ClassInfo classInfo, final ClassInfo.Method method) {
        methodNode.instructions.clear();
        final Type[] argumentTypes = Type.getArgumentTypes(methodNode.desc);
        final Type returnType = Type.getReturnType(methodNode.desc);
        Bytecode.loadArgs(argumentTypes, methodNode.instructions, 0);
        methodNode.instructions.add(new MethodInsnNode(184, classInfo.getName(), method.getName(), methodNode.desc, false));
        methodNode.instructions.add(new InsnNode(returnType.getOpcode(172)));
        methodNode.maxStack = Bytecode.getFirstNonArgLocalIndex(argumentTypes, false);
        methodNode.maxLocals = 0;
    }
    
    @Override
    public void onPrepare(final MixinInfo mixinInfo) {
        final String className = mixinInfo.getClassName();
        if (mixinInfo.isLoadable()) {
            this.registerLoadable(className);
        }
        if (mixinInfo.isAccessor()) {
            this.registerAccessor(mixinInfo);
        }
    }
    
    void registerLoadable(final String s) {
        this.loadable.add(s);
    }
    
    @Override
    public byte[] transformClassBytes(final String s, final String s2, final byte[] array) {
        if (this.syntheticInnerClasses.contains(s2)) {
            return this.processSyntheticInner(array);
        }
        if (this.accessorMixins.containsKey(s2)) {
            return this.processAccessor(array, this.accessorMixins.get(s2));
        }
        return array;
    }
    
    private byte[] processAccessor(final byte[] array, final MixinInfo mixinInfo) {
        if (!MixinEnvironment.getCompatibilityLevel().isAtLeast(MixinEnvironment.CompatibilityLevel.JAVA_8)) {
            return array;
        }
        boolean b = false;
        final MixinInfo.MixinClassNode classNode = mixinInfo.getClassNode(0);
        final ClassInfo classInfo = mixinInfo.getTargets().get(0);
        for (final MixinInfo.MixinMethodNode mixinMethodNode : classNode.mixinMethods) {
            if (!Bytecode.hasFlag(mixinMethodNode, 8)) {
                continue;
            }
            final AnnotationNode visibleAnnotation = mixinMethodNode.getVisibleAnnotation(Accessor.class);
            final AnnotationNode visibleAnnotation2 = mixinMethodNode.getVisibleAnnotation(Invoker.class);
            if (visibleAnnotation == null && visibleAnnotation2 == null) {
                continue;
            }
            createProxy(mixinMethodNode, classInfo, getAccessorMethod(mixinInfo, mixinMethodNode, classInfo));
            b = true;
        }
        if (b) {
            return this.writeClass(classNode);
        }
        return array;
    }
    
    void registerAccessor(final MixinInfo mixinInfo) {
        this.registerLoadable(mixinInfo.getClassName());
        this.accessorMixins.put(mixinInfo.getClassName(), mixinInfo);
    }
    
    boolean canTransform(final String s) {
        return this.syntheticInnerClasses.contains(s) || this.loadable.contains(s);
    }
    
    @Override
    public String getName() {
        return this.getClass().getName();
    }
    
    MixinPostProcessor() {
        this.syntheticInnerClasses = new HashSet<String>();
        this.accessorMixins = new HashMap<String, MixinInfo>();
        this.loadable = new HashSet<String>();
    }
    
    private byte[] processSyntheticInner(final byte[] array) {
        final ClassReader classReader = new ClassReader(array);
        final MixinClassWriter mixinClassWriter = new MixinClassWriter(classReader, 0);
        classReader.accept(new ClassVisitor(327680, mixinClassWriter) {
            @Override
            public FieldVisitor visitField(int n, final String s, final String s2, final String s3, final Object o) {
                if ((n & 0x6) == 0x0) {
                    n |= 0x1;
                }
                return super.visitField(n, s, s2, s3, o);
            }
            
            @Override
            public MethodVisitor visitMethod(int n, final String s, final String s2, final String s3, final String[] array) {
                if ((n & 0x6) == 0x0) {
                    n |= 0x1;
                }
                return super.visitMethod(n, s, s2, s3, array);
            }
            
            @Override
            public void visit(final int n, final int n2, final String s, final String s2, final String s3, final String[] array) {
                super.visit(n, n2 | 0x1, s, s2, s3, array);
            }
        }, 8);
        return mixinClassWriter.toByteArray();
    }
    
    @Override
    public void onInit(final MixinInfo mixinInfo) {
        final Iterator<String> iterator = mixinInfo.getSyntheticInnerClasses().iterator();
        while (iterator.hasNext()) {
            this.registerSyntheticInner(iterator.next().replace('/', '.'));
        }
    }
    
    void registerSyntheticInner(final String s) {
        this.syntheticInnerClasses.add(s);
    }
    
    @Override
    public boolean isDelegationExcluded() {
        return true;
    }
    
    private static ClassInfo.Method getAccessorMethod(final MixinInfo obj, final MethodNode methodNode, final ClassInfo obj2) throws MixinTransformerError {
        final ClassInfo.Method method = obj.getClassInfo().findMethod(methodNode, 10);
        if (!method.isRenamed()) {
            throw new MixinTransformerError("Unexpected state: " + obj + " loaded before " + obj2 + " was conformed");
        }
        return method;
    }
}
