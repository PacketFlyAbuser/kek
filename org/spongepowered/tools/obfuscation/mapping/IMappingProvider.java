// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mapping;

import java.io.IOException;
import java.io.File;
import org.spongepowered.asm.obfuscation.mapping.common.MappingField;
import org.spongepowered.asm.obfuscation.mapping.common.MappingMethod;

public interface IMappingProvider
{
    MappingMethod getMethodMapping(final MappingMethod p0);
    
    boolean isEmpty();
    
    MappingField getFieldMapping(final MappingField p0);
    
    void read(final File p0) throws IOException;
    
    String getPackageMapping(final String p0);
    
    String getClassMapping(final String p0);
    
    void clear();
}
