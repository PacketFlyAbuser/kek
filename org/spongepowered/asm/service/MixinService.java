// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service;

import java.util.ServiceConfigurationError;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import java.util.HashSet;
import org.apache.logging.log4j.Logger;
import java.util.ServiceLoader;
import java.util.Set;

public final class MixinService
{
    private static /* synthetic */ MixinService instance;
    private final /* synthetic */ Set<String> bootedServices;
    private /* synthetic */ ServiceLoader<IMixinService> serviceLoader;
    private /* synthetic */ ServiceLoader<IMixinServiceBootstrap> bootstrapServiceLoader;
    private static final /* synthetic */ Logger logger;
    private /* synthetic */ IMixinService service;
    
    public static IMixinService getService() {
        return getInstance().getServiceInstance();
    }
    
    private synchronized IMixinService getServiceInstance() {
        if (this.service == null) {
            this.service = this.initService();
            if (this.service == null) {
                throw new ServiceNotAvailableError("No mixin host service is available");
            }
        }
        return this.service;
    }
    
    private MixinService() {
        this.bootedServices = new HashSet<String>();
        this.service = null;
        this.runBootServices();
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    private void runBootServices() {
        this.bootstrapServiceLoader = ServiceLoader.load(IMixinServiceBootstrap.class, this.getClass().getClassLoader());
        for (final IMixinServiceBootstrap mixinServiceBootstrap : this.bootstrapServiceLoader) {
            try {
                mixinServiceBootstrap.bootstrap();
                this.bootedServices.add(mixinServiceBootstrap.getServiceClassName());
            }
            catch (Throwable t) {
                MixinService.logger.catching(t);
            }
        }
    }
    
    public static void boot() {
        getInstance();
    }
    
    private static MixinService getInstance() {
        if (MixinService.instance == null) {
            MixinService.instance = new MixinService();
        }
        return MixinService.instance;
    }
    
    private IMixinService initService() {
        this.serviceLoader = ServiceLoader.load(IMixinService.class, this.getClass().getClassLoader());
        final Iterator<IMixinService> iterator = this.serviceLoader.iterator();
        while (iterator.hasNext()) {
            try {
                final IMixinService mixinService = iterator.next();
                if (this.bootedServices.contains(mixinService.getClass().getName())) {
                    MixinService.logger.debug("MixinService [{}] was successfully booted in {}", new Object[] { mixinService.getName(), this.getClass().getClassLoader() });
                }
                if (mixinService.isValid()) {
                    return mixinService;
                }
                continue;
            }
            catch (ServiceConfigurationError serviceConfigurationError) {
                serviceConfigurationError.printStackTrace();
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return null;
    }
}
