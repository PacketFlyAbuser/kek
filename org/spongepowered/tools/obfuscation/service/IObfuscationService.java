// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.service;

import java.util.Set;
import java.util.Collection;

public interface IObfuscationService
{
    Collection<ObfuscationTypeDescriptor> getObfuscationTypes();
    
    Set<String> getSupportedOptions();
}
