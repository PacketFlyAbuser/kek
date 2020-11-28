// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch.platform;

import java.util.HashSet;
import java.net.URI;
import java.util.Collections;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.Launch;
import java.lang.reflect.Field;
import java.util.Iterator;
import org.spongepowered.asm.launch.GlobalProperties;
import java.util.List;
import java.util.Set;
import net.minecraft.launchwrapper.ITweaker;

public class MixinPlatformAgentFML extends MixinPlatformAgentAbstract
{
    private final /* synthetic */ ITweaker coreModWrapper;
    private /* synthetic */ Class<?> clCoreModManager;
    private static final /* synthetic */ Set<String> loadedCoreMods;
    private /* synthetic */ boolean initInjectionState;
    private final /* synthetic */ String fileName;
    
    private static boolean isTweakerQueued(final String suffix) {
        final Iterator<String> iterator = GlobalProperties.get("TweakClasses").iterator();
        while (iterator.hasNext()) {
            if (iterator.next().endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isAlreadyInjected(final String s) {
        if (MixinPlatformAgentFML.loadedCoreMods.contains(s)) {
            return true;
        }
        try {
            final List<ITweaker> list = GlobalProperties.get("Tweaks");
            if (list == null) {
                return false;
            }
            for (final ITweaker obj : list) {
                final Class<? extends ITweaker> class1 = obj.getClass();
                if ("FMLPluginWrapper".equals(class1.getSimpleName())) {
                    final Field field = class1.getField("coreModInstance");
                    field.setAccessible(true);
                    if (s.equals(field.get(obj).getClass().getName())) {
                        return true;
                    }
                    continue;
                }
            }
        }
        catch (Exception ex) {}
        return false;
    }
    
    @Override
    public String getPhaseProvider() {
        return MixinPlatformAgentFML.class.getName() + "$PhaseProvider";
    }
    
    @Override
    public String getLaunchTarget() {
        return null;
    }
    
    @Override
    public void inject() {
        if (this.coreModWrapper != null && this.checkForCoInitialisation()) {
            MixinPlatformAgentAbstract.logger.debug("FML agent is co-initiralising coremod instance {} for {}", new Object[] { this.coreModWrapper, this.uri });
            this.coreModWrapper.injectIntoClassLoader(Launch.classLoader);
        }
    }
    
    private static Class<?> getCoreModManagerClass() throws ClassNotFoundException {
        try {
            return Class.forName(GlobalProperties.getString("mixin.launch.fml.coremodmanagerclass", "net.minecraftforge.fml.relauncher.CoreModManager"));
        }
        catch (ClassNotFoundException ex) {
            return Class.forName("cpw.mods.fml.relauncher.CoreModManager");
        }
    }
    
    private ITweaker injectCorePlugin() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        final String value = this.attributes.get("FMLCorePlugin");
        if (value == null) {
            return null;
        }
        if (this.isAlreadyInjected(value)) {
            MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Skipping because it was already injected.", new Object[] { this.fileName, value });
            return null;
        }
        MixinPlatformAgentAbstract.logger.debug("{} has core plugin {}. Injecting it into FML for co-initialisation:", new Object[] { this.fileName, value });
        final Method declaredMethod = this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.loadcoremodmethod", "loadCoreMod"), LaunchClassLoader.class, String.class, File.class);
        declaredMethod.setAccessible(true);
        final ITweaker tweaker = (ITweaker)declaredMethod.invoke(null, Launch.classLoader, value, this.container);
        if (tweaker == null) {
            MixinPlatformAgentAbstract.logger.debug("Core plugin {} could not be loaded.", new Object[] { value });
            return null;
        }
        this.initInjectionState = isTweakerQueued("FMLInjectionAndSortingTweaker");
        MixinPlatformAgentFML.loadedCoreMods.add(value);
        return tweaker;
    }
    
    private void loadAsMod() {
        try {
            getIgnoredMods(this.clCoreModManager).remove(this.fileName);
        }
        catch (Exception ex) {
            MixinPlatformAgentAbstract.logger.catching((Throwable)ex);
        }
        if (this.attributes.get("FMLCorePluginContainsFMLMod") != null) {
            if (this.isIgnoredReparseable()) {
                MixinPlatformAgentAbstract.logger.debug("Ignoring request to add {} to reparseable coremod collection - it is a deobfuscated dependency", new Object[] { this.fileName });
                return;
            }
            this.addReparseableJar();
        }
    }
    
    private void addReparseableJar() {
        try {
            final List list = (List)this.clCoreModManager.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.reparseablecoremodsmethod", "getReparseableCoremods"), (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            if (!list.contains(this.fileName)) {
                MixinPlatformAgentAbstract.logger.debug("Adding {} to reparseable coremod collection", new Object[] { this.fileName });
                list.add(this.fileName);
            }
        }
        catch (Exception ex) {
            MixinPlatformAgentAbstract.logger.catching((Throwable)ex);
        }
    }
    
    private void injectRemapper() {
        try {
            MixinPlatformAgentAbstract.logger.debug("Creating FML remapper adapter: {}", new Object[] { "org.spongepowered.asm.bridge.RemapperAdapterFML" });
            MixinEnvironment.getDefaultEnvironment().getRemappers().add((IRemapper)Class.forName("org.spongepowered.asm.bridge.RemapperAdapterFML", true, (ClassLoader)Launch.classLoader).getDeclaredMethod("create", (Class<?>[])new Class[0]).invoke(null, new Object[0]));
        }
        catch (Exception ex) {
            MixinPlatformAgentAbstract.logger.debug("Failed instancing FML remapper adapter, things will probably go horribly for notch-obf'd mods!");
        }
    }
    
    private boolean isIgnoredReparseable() {
        return this.container.toString().contains("deobfedDeps");
    }
    
    private static List<String> getIgnoredMods(final Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = clazz.getDeclaredMethod(GlobalProperties.getString("mixin.launch.fml.ignoredmodsmethod", "getIgnoredMods"), (Class[])new Class[0]);
        }
        catch (NoSuchMethodException ex2) {
            try {
                method = clazz.getDeclaredMethod("getLoadedCoremods", (Class[])new Class[0]);
            }
            catch (NoSuchMethodException ex) {
                MixinPlatformAgentAbstract.logger.catching(Level.DEBUG, (Throwable)ex);
                return Collections.emptyList();
            }
        }
        return (List<String>)method.invoke(null, new Object[0]);
    }
    
    private ITweaker initFMLCoreMod() {
        try {
            try {
                this.clCoreModManager = getCoreModManagerClass();
            }
            catch (ClassNotFoundException ex) {
                MixinPlatformAgentAbstract.logger.info("FML platform manager could not load class {}. Proceeding without FML support.", new Object[] { ex.getMessage() });
                return null;
            }
            if ("true".equalsIgnoreCase(this.attributes.get("ForceLoadAsMod"))) {
                MixinPlatformAgentAbstract.logger.debug("ForceLoadAsMod was specified for {}, attempting force-load", new Object[] { this.fileName });
                this.loadAsMod();
            }
            return this.injectCorePlugin();
        }
        catch (Exception ex2) {
            MixinPlatformAgentAbstract.logger.catching((Throwable)ex2);
            return null;
        }
    }
    
    public MixinPlatformAgentFML(final MixinPlatformManager mixinPlatformManager, final URI uri) {
        super(mixinPlatformManager, uri);
        this.fileName = this.container.getName();
        this.coreModWrapper = this.initFMLCoreMod();
    }
    
    @Override
    public void initPrimaryContainer() {
        if (this.clCoreModManager != null) {
            this.injectRemapper();
        }
    }
    
    @Override
    public void prepare() {
        this.initInjectionState |= isTweakerQueued("FMLInjectionAndSortingTweaker");
    }
    
    protected final boolean checkForCoInitialisation() {
        final boolean tweakerQueued = isTweakerQueued("FMLInjectionAndSortingTweaker");
        final boolean tweakerQueued2 = isTweakerQueued("TerminalTweaker");
        if ((this.initInjectionState && tweakerQueued2) || tweakerQueued) {
            MixinPlatformAgentAbstract.logger.debug("FML agent is skipping co-init for {} because FML will inject it normally", new Object[] { this.coreModWrapper });
            return false;
        }
        return !isTweakerQueued("FMLDeobfTweaker");
    }
    
    static {
        FML_PLUGIN_WRAPPER_CLASS = "FMLPluginWrapper";
        LOAD_CORE_MOD_METHOD = "loadCoreMod";
        FML_CORE_MOD_INSTANCE_FIELD = "coreModInstance";
        MFATT_COREMODCONTAINSMOD = "FMLCorePluginContainsFMLMod";
        CORE_MOD_MANAGER_CLASS = "net.minecraftforge.fml.relauncher.CoreModManager";
        FML_TWEAKER_INJECTION = "FMLInjectionAndSortingTweaker";
        FML_TWEAKER_TERMINAL = "TerminalTweaker";
        MFATT_FMLCOREPLUGIN = "FMLCorePlugin";
        GET_IGNORED_MODS_METHOD_LEGACY = "getLoadedCoremods";
        FML_TWEAKER_DEOBF = "FMLDeobfTweaker";
        GET_REPARSEABLE_COREMODS_METHOD = "getReparseableCoremods";
        GET_IGNORED_MODS_METHOD = "getIgnoredMods";
        FML_CMDLINE_COREMODS = "fml.coreMods.load";
        CORE_MOD_MANAGER_CLASS_LEGACY = "cpw.mods.fml.relauncher.CoreModManager";
        FML_REMAPPER_ADAPTER_CLASS = "org.spongepowered.asm.bridge.RemapperAdapterFML";
        MFATT_FORCELOADASMOD = "ForceLoadAsMod";
        loadedCoreMods = new HashSet<String>();
        for (final String s : System.getProperty("fml.coreMods.load", "").split(",")) {
            if (!s.isEmpty()) {
                MixinPlatformAgentAbstract.logger.debug("FML platform agent will ignore coremod {} specified on the command line", new Object[] { s });
                MixinPlatformAgentFML.loadedCoreMods.add(s);
            }
        }
    }
}
