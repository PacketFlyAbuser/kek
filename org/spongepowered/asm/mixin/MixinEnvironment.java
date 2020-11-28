// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin;

import org.spongepowered.asm.util.JavaVersion;
import com.google.common.collect.ImmutableList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import com.google.common.collect.Sets;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.util.PrettyPrinter;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.service.ITransformer;
import java.util.Collections;
import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
import org.spongepowered.asm.mixin.throwables.MixinException;
import org.spongepowered.asm.service.MixinService;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.spongepowered.asm.service.IMixinService;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.util.perf.Profiler;
import java.util.Map;
import java.util.Set;
import org.spongepowered.asm.obfuscation.RemapperChain;
import org.spongepowered.asm.service.ILegacyClassTransformer;
import java.util.List;
import org.spongepowered.asm.util.ITokenProvider;

public final class MixinEnvironment implements ITokenProvider
{
    private /* synthetic */ Side side;
    private static /* synthetic */ Phase currentPhase;
    private final /* synthetic */ String configsKey;
    private static /* synthetic */ boolean showHeader;
    private /* synthetic */ List<ILegacyClassTransformer> transformers;
    private final /* synthetic */ List<TokenProviderWrapper> tokenProviders;
    private static /* synthetic */ MixinEnvironment currentEnvironment;
    private final /* synthetic */ RemapperChain remappers;
    private final /* synthetic */ Set<String> tokenProviderClasses;
    private static final /* synthetic */ Set<String> excludeTransformers;
    private static /* synthetic */ CompatibilityLevel compatibility;
    private final /* synthetic */ Map<String, Integer> internalTokens;
    private final /* synthetic */ boolean[] options;
    private /* synthetic */ String obfuscationContext;
    private static final /* synthetic */ Profiler profiler;
    private static final /* synthetic */ Logger logger;
    private final /* synthetic */ Phase phase;
    private final /* synthetic */ IMixinService service;
    
    @Override
    public Integer getToken(String upperCase) {
        upperCase = upperCase.toUpperCase();
        final Iterator<TokenProviderWrapper> iterator = this.tokenProviders.iterator();
        while (iterator.hasNext()) {
            final Integer token = iterator.next().getToken(upperCase);
            if (token != null) {
                return token;
            }
        }
        return this.internalTokens.get(upperCase);
    }
    
    public String getRefmapObfuscationContext() {
        final String stringValue = Option.OBFUSCATION_TYPE.getStringValue();
        if (stringValue != null) {
            return stringValue;
        }
        return this.obfuscationContext;
    }
    
    public static MixinEnvironment getDefaultEnvironment() {
        return getEnvironment(Phase.DEFAULT);
    }
    
    MixinEnvironment(final Phase phase) {
        this.tokenProviderClasses = new HashSet<String>();
        this.tokenProviders = new ArrayList<TokenProviderWrapper>();
        this.internalTokens = new HashMap<String, Integer>();
        this.remappers = new RemapperChain();
        this.obfuscationContext = null;
        this.service = MixinService.getService();
        this.phase = phase;
        this.configsKey = "mixin.configs." + this.phase.name.toLowerCase();
        final String version = this.getVersion();
        if (version == null || !"0.7.4".equals(version)) {
            throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
        }
        this.service.checkEnv(this);
        this.options = new boolean[Option.values().length];
        for (final Option option : Option.values()) {
            this.options[option.ordinal()] = option.getBooleanValue();
        }
        if (MixinEnvironment.showHeader) {
            MixinEnvironment.showHeader = false;
            this.printHeader(version);
        }
    }
    
    public void setObfuscationContext(final String obfuscationContext) {
        this.obfuscationContext = obfuscationContext;
    }
    
    public MixinEnvironment registerTokenProvider(final IEnvironmentTokenProvider environmentTokenProvider) {
        if (environmentTokenProvider != null && !this.tokenProviderClasses.contains(environmentTokenProvider.getClass().getName())) {
            final String name = environmentTokenProvider.getClass().getName();
            final TokenProviderWrapper tokenProviderWrapper = new TokenProviderWrapper(environmentTokenProvider, this);
            MixinEnvironment.logger.info("Adding new token provider {} to {}", new Object[] { name, this });
            this.tokenProviders.add(tokenProviderWrapper);
            this.tokenProviderClasses.add(name);
            Collections.sort(this.tokenProviders);
        }
        return this;
    }
    
    @Deprecated
    public Set<String> getErrorHandlerClasses() {
        return Mixins.getErrorHandlerClasses();
    }
    
    private void buildTransformerDelegationList() {
        MixinEnvironment.logger.debug("Rebuilding transformer delegation list:");
        this.transformers = new ArrayList<ILegacyClassTransformer>();
        for (final ITransformer transformer : this.service.getTransformers()) {
            if (!(transformer instanceof ILegacyClassTransformer)) {
                continue;
            }
            final ILegacyClassTransformer legacyClassTransformer = (ILegacyClassTransformer)transformer;
            final String name = legacyClassTransformer.getName();
            boolean b = true;
            final Iterator<String> iterator2 = MixinEnvironment.excludeTransformers.iterator();
            while (iterator2.hasNext()) {
                if (name.contains(iterator2.next())) {
                    b = false;
                    break;
                }
            }
            if (b && !legacyClassTransformer.isDelegationExcluded()) {
                MixinEnvironment.logger.debug("  Adding:    {}", new Object[] { name });
                this.transformers.add(legacyClassTransformer);
            }
            else {
                MixinEnvironment.logger.debug("  Excluding: {}", new Object[] { name });
            }
        }
        MixinEnvironment.logger.debug("Transformer delegation list created with {} entries", new Object[] { this.transformers.size() });
    }
    
    @Deprecated
    public List<String> getMixinConfigs() {
        List<String> list = GlobalProperties.get(this.configsKey);
        if (list == null) {
            list = new ArrayList<String>();
            GlobalProperties.put(this.configsKey, list);
        }
        return list;
    }
    
    @Deprecated
    public static void setCompatibilityLevel(final CompatibilityLevel compatibilityLevel) throws IllegalArgumentException {
        if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(Thread.currentThread().getStackTrace()[2].getClassName())) {
            MixinEnvironment.logger.warn("MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!");
        }
        if (compatibilityLevel != MixinEnvironment.compatibility && compatibilityLevel.isAtLeast(MixinEnvironment.compatibility)) {
            if (!compatibilityLevel.isSupported()) {
                throw new IllegalArgumentException("The requested compatibility level " + compatibilityLevel + " could not be set. Level is not supported");
            }
            MixinEnvironment.compatibility = compatibilityLevel;
            MixinEnvironment.logger.info("Compatibility level set to {}", new Object[] { compatibilityLevel });
        }
    }
    
    public static Profiler getProfiler() {
        return MixinEnvironment.profiler;
    }
    
    public String getOptionValue(final Option option) {
        return option.getStringValue();
    }
    
    @Deprecated
    public MixinEnvironment registerErrorHandlerClass(final String s) {
        Mixins.registerErrorHandlerClass(s);
        return this;
    }
    
    private void printHeader(final Object o) {
        final String codeSource = this.getCodeSource();
        final String name = this.service.getName();
        final Side side = this.getSide();
        MixinEnvironment.logger.info("SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", new Object[] { o, codeSource, name, side });
        final boolean option = this.getOption(Option.DEBUG_VERBOSE);
        if (option || this.getOption(Option.DEBUG_EXPORT) || this.getOption(Option.DEBUG_PROFILER)) {
            final PrettyPrinter prettyPrinter = new PrettyPrinter(32);
            prettyPrinter.add("SpongePowered MIXIN%s", option ? " (Verbose debugging enabled)" : "").centre().hr();
            prettyPrinter.kv("Code source", (Object)codeSource);
            prettyPrinter.kv("Internal Version", o);
            prettyPrinter.kv("Java 8 Supported", CompatibilityLevel.JAVA_8.isSupported()).hr();
            prettyPrinter.kv("Service Name", (Object)name);
            prettyPrinter.kv("Service Class", (Object)this.service.getClass().getName()).hr();
            for (final Option option2 : Option.values()) {
                final StringBuilder sb = new StringBuilder();
                for (int j = 0; j < option2.depth; ++j) {
                    sb.append("- ");
                }
                prettyPrinter.kv(option2.property, "%s<%s>", sb, option2);
            }
            prettyPrinter.hr().kv("Detected Side", side);
            prettyPrinter.print(System.err);
        }
    }
    
    public Object getActiveTransformer() {
        return GlobalProperties.get("mixin.transformer");
    }
    
    public static MixinEnvironment getCurrentEnvironment() {
        if (MixinEnvironment.currentEnvironment == null) {
            MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
        }
        return MixinEnvironment.currentEnvironment;
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s]", this.getClass().getSimpleName(), this.phase);
    }
    
    public void audit() {
        final Object activeTransformer = this.getActiveTransformer();
        if (activeTransformer instanceof MixinTransformer) {
            ((MixinTransformer)activeTransformer).audit(this);
        }
    }
    
    public List<ILegacyClassTransformer> getTransformers() {
        if (this.transformers == null) {
            this.buildTransformerDelegationList();
        }
        return Collections.unmodifiableList((List<? extends ILegacyClassTransformer>)this.transformers);
    }
    
    private String getCodeSource() {
        try {
            return this.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        }
        catch (Throwable t) {
            return "Unknown";
        }
    }
    
    public void addTransformerExclusion(final String s) {
        MixinEnvironment.excludeTransformers.add(s);
        this.transformers = null;
    }
    
    public String getObfuscationContext() {
        return this.obfuscationContext;
    }
    
    public MixinEnvironment registerTokenProviderClass(final String str) {
        if (!this.tokenProviderClasses.contains(str)) {
            try {
                this.registerTokenProvider((IEnvironmentTokenProvider)this.service.getClassProvider().findClass(str, true).newInstance());
            }
            catch (Throwable t) {
                MixinEnvironment.logger.error("Error instantiating " + str, t);
            }
        }
        return this;
    }
    
    public void setActiveTransformer(final ITransformer transformer) {
        if (transformer != null) {
            GlobalProperties.put("mixin.transformer", transformer);
        }
    }
    
    public static CompatibilityLevel getCompatibilityLevel() {
        return MixinEnvironment.compatibility;
    }
    
    public void setOption(final Option option, final boolean b) {
        this.options[option.ordinal()] = b;
    }
    
    void registerConfig(final String s) {
        final List<String> mixinConfigs = this.getMixinConfigs();
        if (!mixinConfigs.contains(s)) {
            mixinConfigs.add(s);
        }
    }
    
    static void gotoPhase(final Phase currentPhase) {
        if (currentPhase == null || currentPhase.ordinal < 0) {
            throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
        }
        if (currentPhase.ordinal > getCurrentPhase().ordinal) {
            MixinService.getService().beginPhase();
        }
        if (currentPhase == Phase.DEFAULT) {
            ((org.apache.logging.log4j.core.Logger)LogManager.getLogger("FML")).removeAppender((Appender)MixinLogger.appender);
        }
        MixinEnvironment.currentPhase = currentPhase;
        MixinEnvironment.currentEnvironment = getEnvironment(getCurrentPhase());
    }
    
    public <E extends Enum<E>> E getOption(final Option option, final E e) {
        return option.getEnumValue(e);
    }
    
    public static void init(final Phase currentPhase) {
        if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            MixinEnvironment.currentPhase = currentPhase;
            getProfiler().setActive(getEnvironment(currentPhase).getOption(Option.DEBUG_PROFILER));
            final MixinLogger mixinLogger = new MixinLogger();
        }
    }
    
    private static Phase getCurrentPhase() {
        if (MixinEnvironment.currentPhase == Phase.NOT_INITIALISED) {
            init(Phase.PREINIT);
        }
        return MixinEnvironment.currentPhase;
    }
    
    public MixinEnvironment setSide(final Side side) {
        if (side != null && this.getSide() == Side.UNKNOWN && side != Side.UNKNOWN) {
            this.side = side;
        }
        return this;
    }
    
    static {
        excludeTransformers = Sets.newHashSet((Object[])new String[] { "net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer" });
        MixinEnvironment.currentPhase = Phase.NOT_INITIALISED;
        MixinEnvironment.compatibility = Option.DEFAULT_COMPATIBILITY_LEVEL.getEnumValue(CompatibilityLevel.JAVA_6);
        MixinEnvironment.showHeader = true;
        logger = LogManager.getLogger("mixin");
        profiler = new Profiler();
    }
    
    public Side getSide() {
        if (this.side == null) {
            for (final Side side : Side.values()) {
                if (side.detect()) {
                    this.side = side;
                    break;
                }
            }
        }
        return (this.side != null) ? this.side : Side.UNKNOWN;
    }
    
    public RemapperChain getRemappers() {
        return this.remappers;
    }
    
    public Phase getPhase() {
        return this.phase;
    }
    
    public String getVersion() {
        return GlobalProperties.get("mixin.initialised");
    }
    
    public boolean getOption(final Option option) {
        return this.options[option.ordinal()];
    }
    
    @Deprecated
    public MixinEnvironment addConfiguration(final String s) {
        MixinEnvironment.logger.warn("MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!");
        Mixins.addConfiguration(s, this);
        return this;
    }
    
    public static MixinEnvironment getEnvironment(final Phase phase) {
        if (phase == null) {
            return Phase.DEFAULT.getEnvironment();
        }
        return phase.getEnvironment();
    }
    
    static class MixinLogger
    {
        static /* synthetic */ MixinAppender appender;
        
        public MixinLogger() {
            final org.apache.logging.log4j.core.Logger logger = (org.apache.logging.log4j.core.Logger)LogManager.getLogger("FML");
            MixinLogger.appender.start();
            logger.addAppender((Appender)MixinLogger.appender);
        }
        
        static {
            MixinLogger.appender = new MixinAppender("MixinLogger", null, null);
        }
        
        static class MixinAppender extends AbstractAppender
        {
            protected MixinAppender(final String s, final Filter filter, final Layout<? extends Serializable> layout) {
                super(s, filter, (Layout)layout);
            }
            
            public void append(final LogEvent logEvent) {
                if (logEvent.getLevel() == Level.DEBUG && "Validating minecraft".equals(logEvent.getMessage().getFormat())) {
                    MixinEnvironment.gotoPhase(Phase.INIT);
                }
            }
        }
    }
    
    public static final class Phase
    {
        final /* synthetic */ String name;
        static final /* synthetic */ Phase NOT_INITIALISED;
        public static final /* synthetic */ Phase INIT;
        public static final /* synthetic */ Phase PREINIT;
        static final /* synthetic */ List<Phase> phases;
        public static final /* synthetic */ Phase DEFAULT;
        final /* synthetic */ int ordinal;
        private /* synthetic */ MixinEnvironment environment;
        
        static {
            NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
            PREINIT = new Phase(0, "PREINIT");
            INIT = new Phase(1, "INIT");
            DEFAULT = new Phase(2, "DEFAULT");
            phases = (List)ImmutableList.of((Object)Phase.PREINIT, (Object)Phase.INIT, (Object)Phase.DEFAULT);
        }
        
        private Phase(final int ordinal, final String name) {
            this.ordinal = ordinal;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        MixinEnvironment getEnvironment() {
            if (this.ordinal < 0) {
                throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
            }
            if (this.environment == null) {
                this.environment = new MixinEnvironment(this);
            }
            return this.environment;
        }
        
        public static Phase forName(final String anObject) {
            for (final Phase phase : Phase.phases) {
                if (phase.name.equals(anObject)) {
                    return phase;
                }
            }
            return null;
        }
    }
    
    public enum Option
    {
        CHECK_IMPLEMENTS_STRICT(Option.CHECK_IMPLEMENTS, Inherit.ALLOW_OVERRIDE, "strict"), 
        DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"), 
        DISABLE_REFMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "disableRefMap"), 
        DEBUG_STRICT(Option.DEBUG_ALL, Inherit.INDEPENDENT, "strict"), 
        REFMAP_REMAP_SOURCE_ENV(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingEnv", "searge"), 
        INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default"), 
        DEFAULT_COMPATIBILITY_LEVEL(Option.ENVIRONMENT, Inherit.INDEPENDENT, "compatLevel"), 
        DEBUG_UNIQUE(Option.DEBUG_STRICT, "unique"), 
        CHECK_ALL("checks");
        
        final /* synthetic */ Option parent;
        
        DEBUG_TARGETS(Option.DEBUG_STRICT, "targets");
        
        final /* synthetic */ Inherit inheritance;
        final /* synthetic */ String property;
        
        REFMAP_REMAP_RESOURCE(Option.ENVIRONMENT, Inherit.INDEPENDENT, "refMapRemappingFile", ""), 
        DEBUG_PROFILER(Option.DEBUG_ALL, Inherit.ALLOW_OVERRIDE, "profiler"), 
        DEBUG_EXPORT_DECOMPILE_THREADED(Option.DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, "async"), 
        DEBUG_EXPORT_FILTER(Option.DEBUG_EXPORT, "filter", false), 
        IGNORE_REQUIRED(Option.ENVIRONMENT, Inherit.INDEPENDENT, "ignoreRequired"), 
        DEBUG_VERBOSE(Option.DEBUG_ALL, "verbose"), 
        DEBUG_EXPORT_DECOMPILE(Option.DEBUG_EXPORT, Inherit.ALLOW_OVERRIDE, "decompile"), 
        DEBUG_EXPORT(Option.DEBUG_ALL, "export"), 
        ENVIRONMENT(Inherit.ALWAYS_FALSE, "env"), 
        IGNORE_CONSTRAINTS("ignoreConstraints"), 
        SHIFT_BY_VIOLATION_BEHAVIOUR(Option.ENVIRONMENT, Inherit.INDEPENDENT, "shiftByViolation", "warn"), 
        OBFUSCATION_TYPE(Option.ENVIRONMENT, Inherit.ALWAYS_FALSE, "obf"), 
        DEBUG_ALL("debug");
        
        final /* synthetic */ int depth;
        final /* synthetic */ String defaultValue;
        
        DEBUG_VERIFY(Option.DEBUG_ALL, "verify"), 
        HOT_SWAP("hotSwap"), 
        REFMAP_REMAP(Option.ENVIRONMENT, Inherit.INDEPENDENT, "remapRefMap");
        
        final /* synthetic */ boolean isFlag;
        
        CHECK_IMPLEMENTS(Option.CHECK_ALL, "interfaces"), 
        DEBUG_INJECTORS(Option.DEBUG_ALL, "countInjections");
        
        final String getStringValue() {
            return (this.parent == null || this.parent.getBooleanValue()) ? System.getProperty(this.property, this.defaultValue) : this.defaultValue;
        }
        
        private Option(final String s2, final String s3) {
            this(null, Inherit.INDEPENDENT, s2, false, s3);
        }
        
        final boolean getBooleanValue() {
            if (this.inheritance == Inherit.ALWAYS_FALSE) {
                return false;
            }
            final boolean localBooleanValue = this.getLocalBooleanValue(false);
            if (this.inheritance == Inherit.INDEPENDENT) {
                return localBooleanValue;
            }
            final boolean b = localBooleanValue || this.getInheritedBooleanValue();
            return (this.inheritance == Inherit.INHERIT) ? b : this.getLocalBooleanValue(b);
        }
        
        private Option(final Option option, final String s2) {
            this(option, Inherit.INHERIT, s2, true);
        }
        
        private Option(final Option option, final Inherit inherit, final String s2, final boolean b) {
            this(option, inherit, s2, b, null);
        }
        
        private Option(final Option option, final String s2, final boolean b) {
            this(option, Inherit.INHERIT, s2, b, null);
        }
        
        String getProperty() {
            return this.property;
        }
        
        private Option(final String s2) {
            this(null, s2, true);
        }
        
        private boolean getLocalBooleanValue(final boolean b) {
            return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(b)));
        }
        
        Option getParent() {
            return this.parent;
        }
        
        private Option(final String s2, final boolean b) {
            this(null, s2, b);
        }
        
        private Option(final Option option, final String s2, final String s3) {
            this(option, Inherit.INHERIT, s2, false, s3);
        }
        
        @Override
        public String toString() {
            return this.isFlag ? String.valueOf(this.getBooleanValue()) : this.getStringValue();
        }
        
         <E extends Enum<E>> E getEnumValue(final E e) {
            final String property = System.getProperty(this.property, e.name());
            try {
                return Enum.valueOf(e.getClass(), property.toUpperCase());
            }
            catch (IllegalArgumentException ex) {
                return e;
            }
        }
        
        private Option(final Inherit inherit, final String s2) {
            this(null, inherit, s2, true);
        }
        
        private boolean getInheritedBooleanValue() {
            return this.parent != null && this.parent.getBooleanValue();
        }
        
        private Option(final Option option, final Inherit inherit, final String s2, final String s3) {
            this(option, inherit, s2, false, s3);
        }
        
        private Option(Option parent, final Inherit inheritance, final String str, final boolean isFlag, final String defaultValue) {
            this.parent = parent;
            this.inheritance = inheritance;
            this.property = ((parent != null) ? parent.property : "mixin") + "." + str;
            this.defaultValue = defaultValue;
            this.isFlag = isFlag;
            int depth;
            for (depth = 0; parent != null; parent = parent.parent, ++depth) {}
            this.depth = depth;
        }
        
        private Option(final Option option, final Inherit inherit, final String s2) {
            this(option, inherit, s2, true);
        }
        
        static {
            PREFIX = "mixin";
        }
        
        private enum Inherit
        {
            ALWAYS_FALSE, 
            INHERIT, 
            ALLOW_OVERRIDE, 
            INDEPENDENT;
        }
    }
    
    public enum Side
    {
        CLIENT {
            @Override
            protected boolean detect() {
                return "CLIENT".equals(MixinService.getService().getSideName());
            }
        }, 
        UNKNOWN {
            @Override
            protected boolean detect() {
                return false;
            }
        }, 
        SERVER {
            @Override
            protected boolean detect() {
                final String sideName = MixinService.getService().getSideName();
                return "SERVER".equals(sideName) || "DEDICATEDSERVER".equals(sideName);
            }
        };
        
        protected abstract boolean detect();
    }
    
    public enum CompatibilityLevel
    {
        JAVA_8(8, 52, true) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 1.8;
            }
        };
        
        private final /* synthetic */ int classVersion;
        private final /* synthetic */ int ver;
        
        JAVA_7(7, 51, false) {
            @Override
            boolean isSupported() {
                return JavaVersion.current() >= 1.7;
            }
        }, 
        JAVA_9(9, 53, true) {
            @Override
            boolean isSupported() {
                return false;
            }
        };
        
        private final /* synthetic */ boolean supportsMethodsInInterfaces;
        
        JAVA_6(6, 50, false);
        
        private /* synthetic */ CompatibilityLevel maxCompatibleLevel;
        
        private CompatibilityLevel(final int ver, final int classVersion, final boolean supportsMethodsInInterfaces) {
            this.ver = ver;
            this.classVersion = classVersion;
            this.supportsMethodsInInterfaces = supportsMethodsInInterfaces;
        }
        
        public boolean canSupport(final CompatibilityLevel compatibilityLevel) {
            return compatibilityLevel == null || compatibilityLevel.canElevateTo(this);
        }
        
        private void setMaxCompatibleLevel(final CompatibilityLevel maxCompatibleLevel) {
            this.maxCompatibleLevel = maxCompatibleLevel;
        }
        
        static {
            CLASS_V1_9 = 53;
        }
        
        public boolean supportsMethodsInInterfaces() {
            return this.supportsMethodsInInterfaces;
        }
        
        public boolean isAtLeast(final CompatibilityLevel compatibilityLevel) {
            return compatibilityLevel == null || this.ver >= compatibilityLevel.ver;
        }
        
        public int classVersion() {
            return this.classVersion;
        }
        
        boolean isSupported() {
            return true;
        }
        
        public boolean canElevateTo(final CompatibilityLevel compatibilityLevel) {
            return compatibilityLevel == null || this.maxCompatibleLevel == null || compatibilityLevel.ver <= this.maxCompatibleLevel.ver;
        }
    }
    
    static class TokenProviderWrapper implements Comparable<TokenProviderWrapper>
    {
        private final /* synthetic */ int priority;
        private final /* synthetic */ IEnvironmentTokenProvider provider;
        private final /* synthetic */ int order;
        private final /* synthetic */ MixinEnvironment environment;
        private static /* synthetic */ int nextOrder;
        
        Integer getToken(final String s) {
            return this.provider.getToken(s, this.environment);
        }
        
        public IEnvironmentTokenProvider getProvider() {
            return this.provider;
        }
        
        public TokenProviderWrapper(final IEnvironmentTokenProvider provider) {
            this.provider = provider;
            this.order = TokenProviderWrapper.nextOrder++;
            this.priority = provider.getPriority();
        }
        
        @Override
        public int compareTo(final TokenProviderWrapper tokenProviderWrapper) {
            if (tokenProviderWrapper == null) {
                return 0;
            }
            if (tokenProviderWrapper.priority == this.priority) {
                return tokenProviderWrapper.order - this.order;
            }
            return tokenProviderWrapper.priority - this.priority;
        }
        
        static {
            TokenProviderWrapper.nextOrder = 0;
        }
    }
}
