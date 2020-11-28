// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch;

import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.platform.MixinPlatformManager;
import org.apache.logging.log4j.Logger;

public abstract class MixinBootstrap
{
    private static final /* synthetic */ Logger logger;
    private static /* synthetic */ MixinPlatformManager platform;
    private static /* synthetic */ boolean initState;
    private static /* synthetic */ boolean initialised;
    
    static boolean start() {
        if (!isSubsystemRegistered()) {
            registerSubsystem("0.7.4");
            if (!MixinBootstrap.initialised) {
                MixinBootstrap.initialised = true;
                final String property = System.getProperty("sun.java.command");
                if (property != null && property.contains("GradleStart")) {
                    System.setProperty("mixin.env.remapRefMap", "true");
                }
                final MixinEnvironment.Phase initialPhase = MixinService.getService().getInitialPhase();
                if (initialPhase == MixinEnvironment.Phase.DEFAULT) {
                    MixinBootstrap.logger.error("Initialising mixin subsystem after game pre-init phase! Some mixins may be skipped.");
                    MixinEnvironment.init(initialPhase);
                    getPlatform().prepare(null);
                    MixinBootstrap.initState = false;
                }
                else {
                    MixinEnvironment.init(initialPhase);
                }
                MixinService.getService().beginPhase();
            }
            getPlatform();
            return true;
        }
        if (!checkSubsystemVersion()) {
            throw new MixinInitialisationError("Mixin subsystem version " + getActiveSubsystemVersion() + " was already initialised. Cannot bootstrap version " + "0.7.4");
        }
        return false;
    }
    
    private static Object getActiveSubsystemVersion() {
        final String value = GlobalProperties.get("mixin.initialised");
        return (value != null) ? value : "";
    }
    
    static {
        VERSION = "0.7.4";
        logger = LogManager.getLogger("mixin");
        MixinBootstrap.initialised = false;
        MixinBootstrap.initState = true;
        MixinService.boot();
        MixinService.getService().prepare();
    }
    
    private static boolean isSubsystemRegistered() {
        return GlobalProperties.get("mixin.initialised") != null;
    }
    
    public static MixinPlatformManager getPlatform() {
        if (MixinBootstrap.platform == null) {
            final MixinPlatformManager value = GlobalProperties.get("mixin.platform");
            if (value instanceof MixinPlatformManager) {
                MixinBootstrap.platform = value;
            }
            else {
                GlobalProperties.put("mixin.platform", MixinBootstrap.platform = new MixinPlatformManager());
                MixinBootstrap.platform.init();
            }
        }
        return MixinBootstrap.platform;
    }
    
    static void doInit(final List<String> list) {
        if (MixinBootstrap.initialised) {
            getPlatform().getPhaseProviderClasses();
            if (MixinBootstrap.initState) {
                getPlatform().prepare(list);
                MixinService.getService().init();
            }
            return;
        }
        if (isSubsystemRegistered()) {
            MixinBootstrap.logger.warn("Multiple Mixin containers present, init suppressed for 0.7.4");
            return;
        }
        throw new IllegalStateException("MixinBootstrap.doInit() called before MixinBootstrap.start()");
    }
    
    private MixinBootstrap() {
    }
    
    private static boolean checkSubsystemVersion() {
        return "0.7.4".equals(getActiveSubsystemVersion());
    }
    
    public static void init() {
        if (!start()) {
            return;
        }
        doInit(null);
    }
    
    @Deprecated
    public static void addProxy() {
        MixinService.getService().beginPhase();
    }
    
    private static void registerSubsystem(final String s) {
        GlobalProperties.put("mixin.initialised", s);
    }
    
    static void inject() {
        getPlatform().inject();
    }
}
