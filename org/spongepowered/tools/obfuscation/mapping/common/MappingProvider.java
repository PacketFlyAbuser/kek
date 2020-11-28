// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mapping.common;

import com.google.common.collect.HashBiMap;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import com.google.common.collect.BiMap;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;

public abstract class MappingProvider implements IMappingProvider
{
    protected final /* synthetic */ Messager messager;
    protected final /* synthetic */ Filer filer;
    protected final /* synthetic */ BiMap<MappingMethod, MappingMethod> methodMap;
    protected final /* synthetic */ BiMap<String, String> packageMap;
    protected final /* synthetic */ BiMap<MappingField, MappingField> fieldMap;
    protected final /* synthetic */ BiMap<String, String> classMap;
    
    public MappingProvider(final Messager messager, final Filer filer) {
        this.packageMap = (BiMap<String, String>)HashBiMap.create();
        this.classMap = (BiMap<String, String>)HashBiMap.create();
        this.fieldMap = (BiMap<MappingField, MappingField>)HashBiMap.create();
        this.methodMap = (BiMap<MappingMethod, MappingMethod>)HashBiMap.create();
        this.messager = messager;
        this.filer = filer;
    }
    
    @Override
    public String getPackageMapping(final String s) {
        return (String)this.packageMap.get((Object)s);
    }
    
    @Override
    public MappingMethod getMethodMapping(final MappingMethod mappingMethod) {
        return (MappingMethod)this.methodMap.get((Object)mappingMethod);
    }
    
    @Override
    public String getClassMapping(final String s) {
        return (String)this.classMap.get((Object)s);
    }
    
    @Override
    public boolean isEmpty() {
        return this.packageMap.isEmpty() && this.classMap.isEmpty() && this.fieldMap.isEmpty() && this.methodMap.isEmpty();
    }
    
    @Override
    public void clear() {
        this.packageMap.clear();
        this.classMap.clear();
        this.fieldMap.clear();
        this.methodMap.clear();
    }
    
    @Override
    public MappingField getFieldMapping(final MappingField mappingField) {
        return (MappingField)this.fieldMap.get((Object)mappingField);
    }
}
