// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mcp;

import org.spongepowered.tools.obfuscation.ObfuscationType;
import org.spongepowered.tools.obfuscation.mapping.mcp.MappingProviderSrg;
import org.spongepowered.tools.obfuscation.mapping.IMappingProvider;
import org.spongepowered.tools.obfuscation.mapping.mcp.MappingWriterSrg;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;

public class ObfuscationEnvironmentMCP extends ObfuscationEnvironment
{
    @Override
    protected IMappingWriter getMappingWriter(final Messager messager, final Filer filer) {
        return new MappingWriterSrg(messager, filer);
    }
    
    @Override
    protected IMappingProvider getMappingProvider(final Messager messager, final Filer filer) {
        return new MappingProviderSrg(messager, filer);
    }
    
    protected ObfuscationEnvironmentMCP(final ObfuscationType obfuscationType) {
        super(obfuscationType);
    }
}
