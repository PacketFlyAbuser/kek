// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch;

import java.util.ServiceLoader;
import org.spongepowered.asm.service.IGlobalPropertyService;

public final class GlobalProperties
{
    private static /* synthetic */ IGlobalPropertyService service;
    
    public static <T> T get(final String s) {
        return getService().getProperty(s);
    }
    
    public static void put(final String s, final Object o) {
        getService().setProperty(s, o);
    }
    
    public static <T> T get(final String s, final T t) {
        return getService().getProperty(s, t);
    }
    
    private GlobalProperties() {
    }
    
    private static IGlobalPropertyService getService() {
        if (GlobalProperties.service == null) {
            GlobalProperties.service = ServiceLoader.load(IGlobalPropertyService.class, GlobalProperties.class.getClassLoader()).iterator().next();
        }
        return GlobalProperties.service;
    }
    
    public static String getString(final String s, final String s2) {
        return getService().getPropertyString(s, s2);
    }
    
    public static final class Keys
    {
        static {
            AGENTS = "mixin.agents";
            FML_LOAD_CORE_MOD = "mixin.launch.fml.loadcoremodmethod";
            INIT = "mixin.initialised";
            FML_GET_IGNORED_MODS = "mixin.launch.fml.ignoredmodsmethod";
            FML_CORE_MOD_MANAGER = "mixin.launch.fml.coremodmanagerclass";
            FML_GET_REPARSEABLE_COREMODS = "mixin.launch.fml.reparseablecoremodsmethod";
            TRANSFORMER = "mixin.transformer";
            PLATFORM_MANAGER = "mixin.platform";
            CONFIGS = "mixin.configs";
        }
        
        private Keys() {
        }
    }
}
