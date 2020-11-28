// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.mixin.transformer.Config;
import java.util.Collections;
import org.apache.logging.log4j.Logger;
import java.util.Set;

public final class Mixins
{
    private static final /* synthetic */ Set<String> errorHandlers;
    private static final /* synthetic */ Logger logger;
    
    public static Set<String> getErrorHandlerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)Mixins.errorHandlers);
    }
    
    public static void addConfiguration(final String s) {
        createConfiguration(s, MixinEnvironment.getDefaultEnvironment());
    }
    
    @Deprecated
    static void addConfiguration(final String s, final MixinEnvironment mixinEnvironment) {
        createConfiguration(s, mixinEnvironment);
    }
    
    public static void registerErrorHandlerClass(final String s) {
        if (s != null) {
            Mixins.errorHandlers.add(s);
        }
    }
    
    public static Set<Config> getConfigs() {
        Set<Config> set = GlobalProperties.get("mixin.configs.queue");
        if (set == null) {
            set = new LinkedHashSet<Config>();
            GlobalProperties.put("mixin.configs.queue", set);
        }
        return set;
    }
    
    public static int getUnvisitedCount() {
        int n = 0;
        final Iterator<Config> iterator = getConfigs().iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isVisited()) {
                ++n;
            }
        }
        return n;
    }
    
    private static void registerConfiguration(final Config config) {
        if (config == null) {
            return;
        }
        final MixinEnvironment environment = config.getEnvironment();
        if (environment != null) {
            environment.registerConfig(config.getName());
        }
        getConfigs().add(config);
    }
    
    private static void createConfiguration(final String str, final MixinEnvironment mixinEnvironment) {
        Config create = null;
        try {
            create = Config.create(str, mixinEnvironment);
        }
        catch (Exception ex) {
            Mixins.logger.error("Error encountered reading mixin config " + str + ": " + ex.getClass().getName() + " " + ex.getMessage(), (Throwable)ex);
        }
        registerConfiguration(create);
    }
    
    private Mixins() {
    }
    
    static {
        CONFIGS_KEY = "mixin.configs.queue";
        logger = LogManager.getLogger("mixin");
        errorHandlers = new LinkedHashSet<String>();
    }
    
    public static void addConfigurations(final String... array) {
        final MixinEnvironment defaultEnvironment = MixinEnvironment.getDefaultEnvironment();
        for (int length = array.length, i = 0; i < length; ++i) {
            createConfiguration(array[i], defaultEnvironment);
        }
    }
}
