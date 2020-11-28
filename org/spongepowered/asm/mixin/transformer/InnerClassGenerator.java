// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import java.io.IOException;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.lib.commons.Remapper;
import org.spongepowered.asm.lib.AnnotationVisitor;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.lib.commons.ClassRemapper;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.transformers.MixinClassWriter;
import org.spongepowered.asm.lib.ClassReader;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import org.spongepowered.asm.mixin.transformer.ext.IClassGenerator;

final class InnerClassGenerator implements IClassGenerator
{
    private final /* synthetic */ Map<String, String> innerClassNames;
    private static final /* synthetic */ Logger logger;
    private final /* synthetic */ Map<String, InnerClassInfo> innerClasses;
    
    private byte[] generate(final InnerClassInfo innerClassInfo) {
        try {
            InnerClassGenerator.logger.debug("Generating mapped inner class {} (originally {})", new Object[] { innerClassInfo.getName(), innerClassInfo.getOriginalName() });
            final ClassReader classReader = new ClassReader(innerClassInfo.getClassBytes());
            final MixinClassWriter mixinClassWriter = new MixinClassWriter(classReader, 0);
            classReader.accept(new InnerClassAdapter(mixinClassWriter, innerClassInfo), 8);
            return mixinClassWriter.toByteArray();
        }
        catch (InvalidMixinException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            InnerClassGenerator.logger.catching((Throwable)ex2);
            return null;
        }
    }
    
    @Override
    public byte[] generate(final String s) {
        final InnerClassInfo innerClassInfo = this.innerClasses.get(s.replace('.', '/'));
        if (innerClassInfo != null) {
            return this.generate(innerClassInfo);
        }
        return null;
    }
    
    InnerClassGenerator() {
        this.innerClassNames = new HashMap<String, String>();
        this.innerClasses = new HashMap<String, InnerClassInfo>();
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    public String registerInnerClass(final MixinInfo mixinInfo, final String s, final MixinTargetContext mixinTargetContext) {
        final String format = String.format("%s%s", s, mixinTargetContext);
        String uniqueReference = this.innerClassNames.get(format);
        if (uniqueReference == null) {
            uniqueReference = getUniqueReference(s, mixinTargetContext);
            this.innerClassNames.put(format, uniqueReference);
            this.innerClasses.put(uniqueReference, new InnerClassInfo(uniqueReference, s, mixinInfo, mixinTargetContext));
            InnerClassGenerator.logger.debug("Inner class {} in {} on {} gets unique name {}", new Object[] { s, mixinInfo.getClassRef(), mixinTargetContext.getTargetClassRef(), uniqueReference });
        }
        return uniqueReference;
    }
    
    private static String getUniqueReference(final String s, final MixinTargetContext mixinTargetContext) {
        String substring = s.substring(s.lastIndexOf(36) + 1);
        if (substring.matches("^[0-9]+$")) {
            substring = "Anonymous";
        }
        return String.format("%s$%s$%s", mixinTargetContext.getTargetClassRef(), substring, UUID.randomUUID().toString().replace("-", ""));
    }
    
    static class InnerClassAdapter extends ClassRemapper
    {
        private final /* synthetic */ InnerClassInfo info;
        
        @Override
        public void visitInnerClass(final String str, final String s, final String s2, final int n) {
            if (str.startsWith(this.info.getOriginalName() + "$")) {
                throw new InvalidMixinException(this.info.getOwner(), "Found unsupported nested inner class " + str + " in " + this.info.getOriginalName());
            }
            super.visitInnerClass(str, s, s2, n);
        }
        
        @Override
        public void visitSource(final String s, final String s2) {
            super.visitSource(s, s2);
            final AnnotationVisitor visitAnnotation = this.cv.visitAnnotation("Lorg/spongepowered/asm/mixin/transformer/meta/MixinInner;", false);
            visitAnnotation.visit("mixin", this.info.getOwner().toString());
            visitAnnotation.visit("name", this.info.getOriginalName().substring(this.info.getOriginalName().lastIndexOf(47) + 1));
            visitAnnotation.visitEnd();
        }
        
        public InnerClassAdapter(final ClassVisitor classVisitor, final InnerClassInfo info) {
            super(327680, classVisitor, info);
            this.info = info;
        }
    }
    
    static class InnerClassInfo extends Remapper
    {
        private final /* synthetic */ MixinTargetContext target;
        private final /* synthetic */ String originalName;
        private final /* synthetic */ String ownerName;
        private final /* synthetic */ String targetName;
        private final /* synthetic */ String name;
        private final /* synthetic */ MixinInfo owner;
        
        String getName() {
            return this.name;
        }
        
        MixinInfo getOwner() {
            return this.owner;
        }
        
        String getOwnerName() {
            return this.ownerName;
        }
        
        MixinTargetContext getTarget() {
            return this.target;
        }
        
        String getTargetName() {
            return this.targetName;
        }
        
        public String toString() {
            return this.name;
        }
        
        String getOriginalName() {
            return this.originalName;
        }
        
        InnerClassInfo(final String name, final String originalName, final MixinInfo owner, final MixinTargetContext target) {
            this.name = name;
            this.originalName = originalName;
            this.owner = owner;
            this.ownerName = owner.getClassRef();
            this.target = target;
            this.targetName = target.getTargetClassRef();
        }
        
        public String map(final String s) {
            if (this.originalName.equals(s)) {
                return this.name;
            }
            if (this.ownerName.equals(s)) {
                return this.targetName;
            }
            return s;
        }
        
        byte[] getClassBytes() throws ClassNotFoundException, IOException {
            return MixinService.getService().getBytecodeProvider().getClassBytes(this.originalName, true);
        }
        
        public String mapMethodName(final String anotherString, final String s, final String s2) {
            if (this.ownerName.equalsIgnoreCase(anotherString)) {
                final ClassInfo.Method method = this.owner.getClassInfo().findMethod(s, s2, 10);
                if (method != null) {
                    return method.getName();
                }
            }
            return super.mapMethodName(anotherString, s, s2);
        }
    }
}
