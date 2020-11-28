// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.agent;

import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.ClassWriter;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import java.util.Map;

class MixinAgentClassLoader extends ClassLoader
{
    private /* synthetic */ Map<Class<?>, byte[]> mixins;
    private static final /* synthetic */ Logger logger;
    private /* synthetic */ Map<String, byte[]> targets;
    
    byte[] getFakeMixinBytecode(final Class<?> clazz) {
        return this.mixins.get(clazz);
    }
    
    MixinAgentClassLoader() {
        this.mixins = new HashMap<Class<?>, byte[]>();
        this.targets = new HashMap<String, byte[]>();
    }
    
    static {
        logger = LogManager.getLogger("mixin.agent");
    }
    
    void addTargetClass(final String s, final byte[] array) {
        this.targets.put(s, array);
    }
    
    void addMixinClass(final String name) {
        MixinAgentClassLoader.logger.debug("Mixin class {} added to class loader", new Object[] { name });
        try {
            final byte[] materialise = this.materialise(name);
            final Class<?> defineClass = this.defineClass(name, materialise, 0, materialise.length);
            defineClass.newInstance();
            this.mixins.put(defineClass, materialise);
        }
        catch (Throwable t) {
            MixinAgentClassLoader.logger.catching(t);
        }
    }
    
    private byte[] materialise(final String s) {
        final ClassWriter classWriter = new ClassWriter(3);
        classWriter.visit(MixinEnvironment.getCompatibilityLevel().classVersion(), 1, s.replace('.', '/'), null, Type.getInternalName(Object.class), null);
        final MethodVisitor visitMethod = classWriter.visitMethod(1, "<init>", "()V", null, null);
        visitMethod.visitCode();
        visitMethod.visitVarInsn(25, 0);
        visitMethod.visitMethodInsn(183, Type.getInternalName(Object.class), "<init>", "()V", false);
        visitMethod.visitInsn(177);
        visitMethod.visitMaxs(1, 1);
        visitMethod.visitEnd();
        classWriter.visitEnd();
        return classWriter.toByteArray();
    }
    
    byte[] getOriginalTargetBytecode(final String s) {
        return this.targets.get(s);
    }
}
