// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch.platform;

import java.lang.reflect.Constructor;
import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import java.util.Iterator;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.ArrayList;
import java.net.URI;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class MixinContainer
{
    private static final /* synthetic */ List<String> agentClasses;
    private final /* synthetic */ Logger logger;
    private final /* synthetic */ URI uri;
    private final /* synthetic */ List<IMixinPlatformAgent> agents;
    
    static {
        GlobalProperties.put("mixin.agents", agentClasses = new ArrayList<String>());
        final Iterator<String> iterator = MixinService.getService().getPlatformAgents().iterator();
        while (iterator.hasNext()) {
            MixinContainer.agentClasses.add(iterator.next());
        }
        MixinContainer.agentClasses.add("org.spongepowered.asm.launch.platform.MixinPlatformAgentDefault");
    }
    
    public void initPrimaryContainer() {
        for (final IMixinPlatformAgent mixinPlatformAgent : this.agents) {
            this.logger.debug("Processing launch tasks for {}", new Object[] { mixinPlatformAgent });
            mixinPlatformAgent.initPrimaryContainer();
        }
    }
    
    public void prepare() {
        for (final IMixinPlatformAgent mixinPlatformAgent : this.agents) {
            this.logger.debug("Processing prepare() for {}", new Object[] { mixinPlatformAgent });
            mixinPlatformAgent.prepare();
        }
    }
    
    public Collection<String> getPhaseProviders() {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<IMixinPlatformAgent> iterator = this.agents.iterator();
        while (iterator.hasNext()) {
            final String phaseProvider = iterator.next().getPhaseProvider();
            if (phaseProvider != null) {
                list.add(phaseProvider);
            }
        }
        return list;
    }
    
    public void inject() {
        for (final IMixinPlatformAgent mixinPlatformAgent : this.agents) {
            this.logger.debug("Processing inject() for {}", new Object[] { mixinPlatformAgent });
            mixinPlatformAgent.inject();
        }
    }
    
    public MixinContainer(final MixinPlatformManager mixinPlatformManager, final URI uri) {
        this.logger = LogManager.getLogger("mixin");
        this.agents = new ArrayList<IMixinPlatformAgent>();
        this.uri = uri;
        for (final String className : MixinContainer.agentClasses) {
            try {
                final Class<?> forName = Class.forName(className);
                final Constructor<?> declaredConstructor = forName.getDeclaredConstructor(MixinPlatformManager.class, URI.class);
                this.logger.debug("Instancing new {} for {}", new Object[] { forName.getSimpleName(), this.uri });
                this.agents.add((IMixinPlatformAgent)declaredConstructor.newInstance(mixinPlatformManager, uri));
            }
            catch (Exception ex) {
                this.logger.catching((Throwable)ex);
            }
        }
    }
    
    public String getLaunchTarget() {
        final Iterator<IMixinPlatformAgent> iterator = this.agents.iterator();
        while (iterator.hasNext()) {
            final String launchTarget = iterator.next().getLaunchTarget();
            if (launchTarget != null) {
                return launchTarget;
            }
        }
        return null;
    }
    
    public URI getURI() {
        return this.uri;
    }
}
