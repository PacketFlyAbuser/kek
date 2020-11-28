// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.injection.InjectionPoint;
import org.apache.logging.log4j.Level;
import java.util.Collection;
import java.io.Reader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.launch.MixinInitialisationError;
import org.spongepowered.asm.util.VersionNumber;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.refmap.RemappingReferenceMapper;
import org.spongepowered.asm.mixin.refmap.ReferenceMapper;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.spongepowered.asm.mixin.refmap.IReferenceMapper;
import org.spongepowered.asm.service.IMixinService;
import java.util.Map;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import java.util.Set;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;

final class MixinConfig implements Comparable<MixinConfig>, IMixinConfig
{
    @SerializedName("verbose")
    private /* synthetic */ boolean verboseLogging;
    @SerializedName("mixinPriority")
    private /* synthetic */ int mixinPriority;
    private final transient /* synthetic */ List<IListener> listeners;
    @SerializedName("package")
    private /* synthetic */ String mixinPackage;
    private final transient /* synthetic */ Set<String> unhandledTargets;
    @SerializedName("client")
    private /* synthetic */ List<String> mixinClassesClient;
    private transient /* synthetic */ String name;
    private transient /* synthetic */ IMixinConfigPlugin plugin;
    private static /* synthetic */ int configOrder;
    private final /* synthetic */ Logger logger;
    @SerializedName("required")
    private /* synthetic */ boolean required;
    private final transient /* synthetic */ int order;
    private static final /* synthetic */ Set<String> globalMixinList;
    private transient /* synthetic */ boolean visited;
    @SerializedName("setSourceFile")
    private /* synthetic */ boolean setSourceFile;
    @SerializedName("target")
    private /* synthetic */ String selector;
    @SerializedName("overwrites")
    private /* synthetic */ OverwriteOptions overwriteOptions;
    private transient /* synthetic */ MixinEnvironment env;
    @SerializedName("compatibilityLevel")
    private /* synthetic */ String compatibility;
    @SerializedName("injectors")
    private /* synthetic */ InjectorOptions injectorOptions;
    private final transient /* synthetic */ Map<String, List<MixinInfo>> mixinMapping;
    private transient /* synthetic */ Config handle;
    @SerializedName("minVersion")
    private /* synthetic */ String version;
    @SerializedName("mixins")
    private /* synthetic */ List<String> mixinClasses;
    @SerializedName("priority")
    private /* synthetic */ int priority;
    @SerializedName("plugin")
    private /* synthetic */ String pluginClassName;
    private transient /* synthetic */ IMixinService service;
    @SerializedName("server")
    private /* synthetic */ List<String> mixinClassesServer;
    private transient /* synthetic */ boolean prepared;
    @SerializedName("refmap")
    private /* synthetic */ String refMapperConfig;
    private transient /* synthetic */ IReferenceMapper refMapper;
    private final transient /* synthetic */ List<MixinInfo> mixins;
    
    public int getDefaultRequiredInjections() {
        return this.injectorOptions.defaultRequireValue;
    }
    
    int getMixinCount() {
        return this.mixins.size();
    }
    
    private MixinEnvironment parseSelector(final String s, final MixinEnvironment mixinEnvironment) {
        if (s != null) {
            final String[] split = s.split("[&\\| ]");
            for (int length = split.length, i = 0; i < length; ++i) {
                final Matcher matcher = Pattern.compile("^@env(?:ironment)?\\(([A-Z]+)\\)$").matcher(split[i].trim());
                if (matcher.matches()) {
                    return MixinEnvironment.getEnvironment(MixinEnvironment.Phase.forName(matcher.group(1)));
                }
            }
            final MixinEnvironment.Phase forName = MixinEnvironment.Phase.forName(s);
            if (forName != null) {
                return MixinEnvironment.getEnvironment(forName);
            }
        }
        return mixinEnvironment;
    }
    
    private MixinConfig() {
        this.logger = LogManager.getLogger("mixin");
        this.mixinMapping = new HashMap<String, List<MixinInfo>>();
        this.unhandledTargets = new HashSet<String>();
        this.mixins = new ArrayList<MixinInfo>();
        this.priority = 1000;
        this.mixinPriority = 1000;
        this.setSourceFile = false;
        this.order = MixinConfig.configOrder++;
        this.listeners = new ArrayList<IListener>();
        this.injectorOptions = new InjectorOptions();
        this.overwriteOptions = new OverwriteOptions();
        this.prepared = false;
        this.visited = false;
    }
    
    void addListener(final IListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public Set<String> getTargets() {
        return Collections.unmodifiableSet((Set<? extends String>)this.mixinMapping.keySet());
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public Set<String> getUnhandledTargets() {
        return Collections.unmodifiableSet((Set<? extends String>)this.unhandledTargets);
    }
    
    private boolean onLoad(final IMixinService service, final String name, final MixinEnvironment mixinEnvironment) {
        this.service = service;
        this.name = name;
        this.env = this.parseSelector(this.selector, mixinEnvironment);
        this.required &= !this.env.getOption(MixinEnvironment.Option.IGNORE_REQUIRED);
        this.initCompatibilityLevel();
        this.initInjectionPoints();
        return this.checkVersion();
    }
    
    @Override
    public int compareTo(final MixinConfig mixinConfig) {
        if (mixinConfig == null) {
            return 0;
        }
        if (mixinConfig.priority == this.priority) {
            return this.order - mixinConfig.order;
        }
        return this.priority - mixinConfig.priority;
    }
    
    void postApply(final String s, final ClassNode classNode) {
        this.unhandledTargets.remove(s);
    }
    
    void postInitialise() {
        if (this.plugin != null) {
            this.prepareMixins(this.plugin.getMixins(), true);
        }
        final Iterator<MixinInfo> iterator = this.mixins.iterator();
        while (iterator.hasNext()) {
            final MixinInfo mixinInfo = iterator.next();
            try {
                mixinInfo.validate();
                final Iterator<IListener> iterator2 = this.listeners.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().onInit(mixinInfo);
                }
            }
            catch (InvalidMixinException ex) {
                this.logger.error(ex.getMixin() + ": " + ex.getMessage(), (Throwable)ex);
                this.removeMixin(mixinInfo);
                iterator.remove();
            }
            catch (Exception ex2) {
                this.logger.error(ex2.getMessage(), (Throwable)ex2);
                this.removeMixin(mixinInfo);
                iterator.remove();
            }
        }
    }
    
    public boolean select(final MixinEnvironment mixinEnvironment) {
        this.visited = true;
        return this.env == mixinEnvironment;
    }
    
    public int getDefaultMixinPriority() {
        return this.mixinPriority;
    }
    
    public boolean requireOverwriteAnnotations() {
        return this.overwriteOptions.requireOverwriteAnnotations;
    }
    
    void onSelect() {
        if (this.pluginClassName != null) {
            try {
                this.plugin = (IMixinConfigPlugin)this.service.getClassProvider().findClass(this.pluginClassName, true).newInstance();
                if (this.plugin != null) {
                    this.plugin.onLoad(this.mixinPackage);
                }
            }
            catch (Throwable t) {
                t.printStackTrace();
                this.plugin = null;
            }
        }
        if (!this.mixinPackage.endsWith(".")) {
            this.mixinPackage += ".";
        }
        boolean b = false;
        if (this.refMapperConfig == null) {
            if (this.plugin != null) {
                this.refMapperConfig = this.plugin.getRefMapperConfig();
            }
            if (this.refMapperConfig == null) {
                b = true;
                this.refMapperConfig = "mixin.refmap.json";
            }
        }
        this.refMapper = ReferenceMapper.read(this.refMapperConfig);
        this.verboseLogging |= this.env.getOption(MixinEnvironment.Option.DEBUG_VERBOSE);
        if (!b && this.refMapper.isDefault() && !this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            this.logger.warn("Reference map '{}' for {} could not be read. If this is a development environment you can ignore this message", new Object[] { this.refMapperConfig, this });
        }
        if (this.env.getOption(MixinEnvironment.Option.REFMAP_REMAP)) {
            this.refMapper = RemappingReferenceMapper.of(this.env, this.refMapper);
        }
    }
    
    public List<MixinInfo> getMixinsFor(final String s) {
        return this.mixinsFor(s);
    }
    
    @Override
    public String getMixinPackage() {
        return this.mixinPackage;
    }
    
    private void prepareMixins(final List<String> list, final boolean b) {
        if (list == null) {
            return;
        }
        for (final String str : list) {
            final String string = this.mixinPackage + str;
            if (str != null) {
                if (MixinConfig.globalMixinList.contains(string)) {
                    continue;
                }
                MixinInfo obj = null;
                try {
                    obj = new MixinInfo(this.service, this, str, true, this.plugin, b);
                    if (obj.getTargetClasses().size() <= 0) {
                        continue;
                    }
                    MixinConfig.globalMixinList.add(string);
                    final Iterator<String> iterator2 = obj.getTargetClasses().iterator();
                    while (iterator2.hasNext()) {
                        final String replace = iterator2.next().replace('/', '.');
                        this.mixinsFor(replace).add(obj);
                        this.unhandledTargets.add(replace);
                    }
                    final Iterator<IListener> iterator3 = this.listeners.iterator();
                    while (iterator3.hasNext()) {
                        iterator3.next().onPrepare(obj);
                    }
                    this.mixins.add(obj);
                }
                catch (InvalidMixinException ex) {
                    if (this.required) {
                        throw ex;
                    }
                    this.logger.error(ex.getMessage(), (Throwable)ex);
                }
                catch (Exception ex2) {
                    if (this.required) {
                        throw new InvalidMixinException(obj, "Error initialising mixin " + obj + " - " + ex2.getClass() + ": " + ex2.getMessage(), ex2);
                    }
                    this.logger.error(ex2.getMessage(), (Throwable)ex2);
                }
            }
        }
    }
    
    public IReferenceMapper getReferenceMapper() {
        if (this.env.getOption(MixinEnvironment.Option.DISABLE_REFMAP)) {
            return ReferenceMapper.DEFAULT_MAPPER;
        }
        this.refMapper.setContext(this.env.getRefmapObfuscationContext());
        return this.refMapper;
    }
    
    public int getMaxShiftByValue() {
        return Math.min(Math.max(this.injectorOptions.maxShiftBy, 0), 0);
    }
    
    private List<MixinInfo> mixinsFor(final String s) {
        List<MixinInfo> list = this.mixinMapping.get(s);
        if (list == null) {
            list = new ArrayList<MixinInfo>();
            this.mixinMapping.put(s, list);
        }
        return list;
    }
    
    public List<String> reloadMixin(final String anObject, final byte[] array) {
        for (final MixinInfo mixinInfo : this.mixins) {
            if (mixinInfo.getClassName().equals(anObject)) {
                mixinInfo.reloadMixin(array);
                return mixinInfo.getTargetClasses();
            }
        }
        return Collections.emptyList();
    }
    
    @Override
    public IMixinConfigPlugin getPlugin() {
        return this.plugin;
    }
    
    private boolean checkVersion() throws MixinInitialisationError {
        if (this.version == null) {
            this.logger.error("Mixin config {} does not specify \"minVersion\" property", new Object[] { this.name });
        }
        final VersionNumber parse = VersionNumber.parse(this.version);
        final VersionNumber parse2 = VersionNumber.parse(this.env.getVersion());
        if (parse.compareTo(parse2) <= 0) {
            return true;
        }
        this.logger.warn("Mixin config {} requires mixin subsystem version {} but {} was found. The mixin config will not be applied.", new Object[] { this.name, parse, parse2 });
        if (this.required) {
            throw new MixinInitialisationError("Required mixin config " + this.name + " requires mixin subsystem version " + parse);
        }
        return false;
    }
    
    static Config create(final String s, final MixinEnvironment mixinEnvironment) {
        try {
            final IMixinService service = MixinService.getService();
            final MixinConfig mixinConfig = (MixinConfig)new Gson().fromJson((Reader)new InputStreamReader(service.getResourceAsStream(s)), (Class)MixinConfig.class);
            if (mixinConfig.onLoad(service, s, mixinEnvironment)) {
                return mixinConfig.getHandle();
            }
            return null;
        }
        catch (Exception cause) {
            cause.printStackTrace();
            throw new IllegalArgumentException(String.format("The specified resource '%s' was invalid or could not be read", s), cause);
        }
    }
    
    private void initCompatibilityLevel() {
        if (this.compatibility == null) {
            return;
        }
        final MixinEnvironment.CompatibilityLevel value = MixinEnvironment.CompatibilityLevel.valueOf(this.compatibility.trim().toUpperCase());
        final MixinEnvironment.CompatibilityLevel compatibilityLevel = MixinEnvironment.getCompatibilityLevel();
        if (value == compatibilityLevel) {
            return;
        }
        if (compatibilityLevel.isAtLeast(value) && !compatibilityLevel.canSupport(value)) {
            throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + value + " which is too old");
        }
        if (!compatibilityLevel.canElevateTo(value)) {
            throw new MixinInitialisationError("Mixin config " + this.name + " requires compatibility level " + value + " which is prohibited by " + compatibilityLevel);
        }
        MixinEnvironment.setCompatibilityLevel(value);
    }
    
    public String getDefaultInjectorGroup() {
        final String defaultGroup = this.injectorOptions.defaultGroup;
        return (defaultGroup != null && !defaultGroup.isEmpty()) ? defaultGroup : "default";
    }
    
    boolean isVisited() {
        return this.visited;
    }
    
    int getDeclaredMixinCount() {
        return getCollectionSize(this.mixinClasses, this.mixinClassesClient, this.mixinClassesServer);
    }
    
    static {
        MixinConfig.configOrder = 0;
        globalMixinList = new HashSet<String>();
    }
    
    public boolean hasMixinsFor(final String s) {
        return this.mixinMapping.containsKey(s);
    }
    
    @Override
    public boolean isRequired() {
        return this.required;
    }
    
    public Level getLoggingLevel() {
        return this.verboseLogging ? Level.INFO : Level.DEBUG;
    }
    
    private void initInjectionPoints() {
        if (this.injectorOptions.injectionPoints == null) {
            return;
        }
        for (final String s : this.injectorOptions.injectionPoints) {
            try {
                final Class<?> class1 = this.service.getClassProvider().findClass(s, true);
                if (InjectionPoint.class.isAssignableFrom(class1)) {
                    InjectionPoint.register((Class<? extends InjectionPoint>)class1);
                }
                else {
                    this.logger.error("Unable to register injection point {} for {}, class must extend InjectionPoint", new Object[] { class1, this });
                }
            }
            catch (Throwable t) {
                this.logger.catching(t);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public MixinEnvironment getEnvironment() {
        return this.env;
    }
    
    private static int getCollectionSize(final Collection<?>... array) {
        int n = 0;
        for (final Collection<?> collection : array) {
            if (collection != null) {
                n += collection.size();
            }
        }
        return n;
    }
    
    String remapClassName(final String s, final String s2) {
        return this.getReferenceMapper().remap(s, s2);
    }
    
    public boolean packageMatch(final String s) {
        return s.startsWith(this.mixinPackage);
    }
    
    public List<String> getClasses() {
        return Collections.unmodifiableList((List<? extends String>)this.mixinClasses);
    }
    
    void prepare() {
        if (this.prepared) {
            return;
        }
        this.prepared = true;
        this.prepareMixins(this.mixinClasses, false);
        switch (this.env.getSide()) {
            case CLIENT: {
                this.prepareMixins(this.mixinClassesClient, false);
                break;
            }
            case SERVER: {
                this.prepareMixins(this.mixinClassesServer, false);
                break;
            }
            default: {
                this.logger.warn("Mixin environment was unable to detect the current side, sided mixins will not be applied");
                break;
            }
        }
    }
    
    private void removeMixin(final MixinInfo mixinInfo) {
        final Iterator<List<MixinInfo>> iterator = this.mixinMapping.values().iterator();
        while (iterator.hasNext()) {
            final Iterator<MixinInfo> iterator2 = iterator.next().iterator();
            while (iterator2.hasNext()) {
                if (mixinInfo == iterator2.next()) {
                    iterator2.remove();
                }
            }
        }
    }
    
    public boolean shouldSetSourceFile() {
        return this.setSourceFile;
    }
    
    public Config getHandle() {
        if (this.handle == null) {
            this.handle = new Config(this);
        }
        return this.handle;
    }
    
    public boolean conformOverwriteVisibility() {
        return this.overwriteOptions.conformAccessModifiers;
    }
    
    interface IListener
    {
        void onPrepare(final MixinInfo p0);
        
        void onInit(final MixinInfo p0);
    }
    
    static class OverwriteOptions
    {
        @SerializedName("conformVisibility")
        /* synthetic */ boolean conformAccessModifiers;
        @SerializedName("requireAnnotations")
        /* synthetic */ boolean requireOverwriteAnnotations;
    }
    
    static class InjectorOptions
    {
        @SerializedName("maxShiftBy")
        /* synthetic */ int maxShiftBy;
        @SerializedName("defaultGroup")
        /* synthetic */ String defaultGroup;
        @SerializedName("injectionPoints")
        /* synthetic */ List<String> injectionPoints;
        @SerializedName("defaultRequire")
        /* synthetic */ int defaultRequireValue;
        
        InjectorOptions() {
            this.defaultRequireValue = 0;
            this.defaultGroup = "default";
            this.maxShiftBy = 0;
        }
    }
}
