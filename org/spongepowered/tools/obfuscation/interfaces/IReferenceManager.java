// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import org.spongepowered.tools.obfuscation.ObfuscationData;

public interface IReferenceManager
{
    void write();
    
    void addClassMapping(final String p0, final String p1, final ObfuscationData<String> p2);
    
    ReferenceMapper getMapper();
    
    void setAllowConflicts(final boolean p0);
    
    boolean getAllowConflicts();
    
    void addMethodMapping(final String p0, final String p1, final ObfuscationData<MappingMethod> p2);
    
    void addFieldMapping(final String p0, final String p1, final MemberInfo p2, final ObfuscationData<MappingField> p3);
    
    void addMethodMapping(final String p0, final String p1, final MemberInfo p2, final ObfuscationData<MappingMethod> p3);
}
