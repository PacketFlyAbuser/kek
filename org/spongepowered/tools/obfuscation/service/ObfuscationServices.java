// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.service;

import java.util.ServiceConfigurationError;
import org.spongepowered.tools.obfuscation.ObfuscationType;
import javax.tools.Diagnostic;
import org.spongepowered.tools.obfuscation.interfaces.IMixinAnnotationProcessor;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.ServiceLoader;

public final class ObfuscationServices
{
    private final /* synthetic */ ServiceLoader<IObfuscationService> serviceLoader;
    private static /* synthetic */ ObfuscationServices instance;
    private final /* synthetic */ Set<IObfuscationService> services;
    
    private ObfuscationServices() {
        this.services = new HashSet<IObfuscationService>();
        this.serviceLoader = ServiceLoader.load(IObfuscationService.class, this.getClass().getClassLoader());
    }
    
    public Set<String> getSupportedOptions() {
        final HashSet<Object> set = (HashSet<Object>)new HashSet<String>();
        final Iterator<IObfuscationService> iterator = this.serviceLoader.iterator();
        while (iterator.hasNext()) {
            final Set<String> supportedOptions = iterator.next().getSupportedOptions();
            if (supportedOptions != null) {
                set.addAll(supportedOptions);
            }
        }
        return (Set<String>)set;
    }
    
    public IObfuscationService getService(final Class<? extends IObfuscationService> clazz) {
        for (final IObfuscationService obfuscationService : this.serviceLoader) {
            if (clazz.getName().equals(obfuscationService.getClass().getName())) {
                return obfuscationService;
            }
        }
        return null;
    }
    
    public static ObfuscationServices getInstance() {
        if (ObfuscationServices.instance == null) {
            ObfuscationServices.instance = new ObfuscationServices();
        }
        return ObfuscationServices.instance;
    }
    
    public void initProviders(final IMixinAnnotationProcessor mixinAnnotationProcessor) {
        try {
            for (final IObfuscationService obfuscationService : this.serviceLoader) {
                if (!this.services.contains(obfuscationService)) {
                    this.services.add(obfuscationService);
                    final String simpleName = obfuscationService.getClass().getSimpleName();
                    final Collection<ObfuscationTypeDescriptor> obfuscationTypes = obfuscationService.getObfuscationTypes();
                    if (obfuscationTypes == null) {
                        continue;
                    }
                    for (final ObfuscationTypeDescriptor obfuscationTypeDescriptor : obfuscationTypes) {
                        try {
                            mixinAnnotationProcessor.printMessage(Diagnostic.Kind.NOTE, simpleName + " supports type: \"" + ObfuscationType.create(obfuscationTypeDescriptor, mixinAnnotationProcessor) + "\"");
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        catch (ServiceConfigurationError serviceConfigurationError) {
            mixinAnnotationProcessor.printMessage(Diagnostic.Kind.ERROR, serviceConfigurationError.getClass().getSimpleName() + ": " + serviceConfigurationError.getMessage());
            serviceConfigurationError.printStackTrace();
        }
    }
}
