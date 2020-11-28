// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import java.util.Iterator;
import org.spongepowered.asm.obfuscation.mapping.IMapping;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import java.util.List;
import org.spongepowered.tools.obfuscation.interfaces.IReferenceManager;

public class ReferenceManager implements IReferenceManager
{
    private final /* synthetic */ List<ObfuscationEnvironment> environments;
    private /* synthetic */ boolean allowConflicts;
    private final /* synthetic */ ReferenceMapper refMapper;
    private final /* synthetic */ String outRefMapFileName;
    private final /* synthetic */ IMixinAnnotationProcessor ap;
    
    @Override
    public void setAllowConflicts(final boolean allowConflicts) {
        this.allowConflicts = allowConflicts;
    }
    
    @Override
    public void addMethodMapping(final String s, final String s2, final ObfuscationData<MappingMethod> obfuscationData) {
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MappingMethod mappingMethod = obfuscationData.get(obfuscationEnvironment.getType());
            if (mappingMethod != null) {
                this.addMapping(obfuscationEnvironment.getType(), s, s2, new MemberInfo(mappingMethod).toString());
            }
        }
    }
    
    @Override
    public void addFieldMapping(final String s, final String s2, final MemberInfo memberInfo, final ObfuscationData<MappingField> obfuscationData) {
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MappingField mappingField = obfuscationData.get(obfuscationEnvironment.getType());
            if (mappingField != null) {
                this.addMapping(obfuscationEnvironment.getType(), s, s2, MemberInfo.fromMapping(mappingField.transform(obfuscationEnvironment.remapDescriptor(memberInfo.desc))).toString());
            }
        }
    }
    
    @Override
    public void write() {
        if (this.outRefMapFileName == null) {
            return;
        }
        Appendable writer = null;
        try {
            writer = this.newWriter(this.outRefMapFileName, "refmap");
            this.refMapper.write(writer);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (writer != null) {
                try {
                    ((PrintWriter)writer).close();
                }
                catch (Exception ex2) {}
            }
        }
    }
    
    public ReferenceManager(final IMixinAnnotationProcessor ap, final List<ObfuscationEnvironment> environments) {
        this.refMapper = new ReferenceMapper();
        this.ap = ap;
        this.environments = environments;
        this.outRefMapFileName = this.ap.getOption("outRefMapFile");
    }
    
    private PrintWriter newWriter(final String pathname, final String s) throws IOException {
        if (pathname.matches("^.*[\\\\/:].*$")) {
            final File file = new File(pathname);
            file.getParentFile().mkdirs();
            this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + s + " to " + file.getAbsolutePath());
            return new PrintWriter(file);
        }
        final FileObject resource = this.ap.getProcessingEnvironment().getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", pathname, new Element[0]);
        this.ap.printMessage(Diagnostic.Kind.NOTE, "Writing " + s + " to " + new File(resource.toUri()).getAbsolutePath());
        return new PrintWriter(resource.openWriter());
    }
    
    @Override
    public void addClassMapping(final String s, final String s2, final ObfuscationData<String> obfuscationData) {
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final String s3 = obfuscationData.get(obfuscationEnvironment.getType());
            if (s3 != null) {
                this.addMapping(obfuscationEnvironment.getType(), s, s2, s3);
            }
        }
    }
    
    @Override
    public boolean getAllowConflicts() {
        return this.allowConflicts;
    }
    
    @Override
    public ReferenceMapper getMapper() {
        return this.refMapper;
    }
    
    @Override
    public void addMethodMapping(final String s, final String s2, final MemberInfo memberInfo, final ObfuscationData<MappingMethod> obfuscationData) {
        for (final ObfuscationEnvironment obfuscationEnvironment : this.environments) {
            final MappingMethod mappingMethod = obfuscationData.get(obfuscationEnvironment.getType());
            if (mappingMethod != null) {
                this.addMapping(obfuscationEnvironment.getType(), s, s2, memberInfo.remapUsing(mappingMethod, true).toString());
            }
        }
    }
    
    protected void addMapping(final ObfuscationType obfuscationType, final String s, final String s2, final String anObject) {
        final String addMapping = this.refMapper.addMapping(obfuscationType.getKey(), s, s2, anObject);
        if (obfuscationType.isDefault()) {
            this.refMapper.addMapping(null, s, s2, anObject);
        }
        if (!this.allowConflicts && addMapping != null && !addMapping.equals(anObject)) {
            throw new ReferenceConflictException(addMapping, anObject);
        }
    }
    
    public static class ReferenceConflictException extends RuntimeException
    {
        private final /* synthetic */ String newReference;
        private final /* synthetic */ String oldReference;
        
        public ReferenceConflictException(final String oldReference, final String newReference) {
            this.oldReference = oldReference;
            this.newReference = newReference;
        }
        
        public String getOld() {
            return this.oldReference;
        }
        
        public String getNew() {
            return this.newReference;
        }
    }
}
