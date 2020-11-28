// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.tools.obfuscation.mapping.IMappingConsumer;
import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;
import java.util.List;

public interface IObfuscationManager
{
    IObfuscationDataProvider getDataProvider();
    
    List<ObfuscationEnvironment> getEnvironments();
    
    IMappingConsumer createMappingConsumer();
    
    void writeReferences();
    
    IReferenceManager getReferenceManager();
    
    void init();
    
    void writeMappings();
}
