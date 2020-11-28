// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.transformer.throwables.MixinReloadException;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.lib.tree.InnerClassNode;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.lib.ClassVisitor;
import org.spongepowered.asm.lib.ClassReader;
import java.util.HashSet;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.Surrogate;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.MethodVisitor;
import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.Pseudo;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.throwables.MixinTargetAlreadyLoadedException;
import java.util.Collection;
import org.spongepowered.asm.lib.Type;
import com.google.common.base.Function;
import java.util.ArrayList;
import org.spongepowered.asm.service.MixinService;
import java.util.Set;
import com.google.common.collect.Lists;
import com.google.common.base.Functions;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.apache.logging.log4j.Level;
import java.io.IOException;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import java.util.Collections;
import org.spongepowered.asm.service.IMixinService;
import java.util.List;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.util.perf.Profiler;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

class MixinInfo implements Comparable<MixinInfo>, IMixinInfo
{
    private final /* synthetic */ boolean virtual;
    private final transient /* synthetic */ MixinEnvironment.Phase phase;
    private final transient /* synthetic */ Logger logger;
    private final transient /* synthetic */ MixinConfig parent;
    private final transient /* synthetic */ Profiler profiler;
    private final transient /* synthetic */ IMixinConfigPlugin plugin;
    private final /* synthetic */ List<ClassInfo> targetClasses;
    private transient /* synthetic */ State state;
    private transient /* synthetic */ State pendingState;
    private final transient /* synthetic */ IMixinService service;
    static /* synthetic */ int mixinOrder;
    private static final /* synthetic */ IMixinService classLoaderUtil;
    private final transient /* synthetic */ boolean strict;
    private final transient /* synthetic */ ClassInfo info;
    private final transient /* synthetic */ int order;
    private final /* synthetic */ int priority;
    private final /* synthetic */ String name;
    private final /* synthetic */ String className;
    private final transient /* synthetic */ SubType type;
    private final /* synthetic */ List<String> targetClassNames;
    
    public boolean isLoadable() {
        return this.type.isLoadable();
    }
    
    List<ClassInfo> getTargets() {
        return Collections.unmodifiableList((List<? extends ClassInfo>)this.targetClasses);
    }
    
    ClassInfo getClassInfo() {
        return this.info;
    }
    
    private byte[] loadMixinClass(final String s, final boolean b) throws ClassNotFoundException {
        byte[] classBytes;
        try {
            classBytes = this.service.getBytecodeProvider().getClassBytes(s, b);
        }
        catch (ClassNotFoundException ex2) {
            throw new ClassNotFoundException(String.format("The specified mixin '%s' was not found", s));
        }
        catch (IOException ex) {
            this.logger.warn("Failed to load mixin %s, the specified mixin will not be applied", new Object[] { s });
            throw new InvalidMixinException(this, "An error was encountered whilst loading the mixin class", ex);
        }
        return classBytes;
    }
    
    public Level getLoggingLevel() {
        return this.parent.getLoggingLevel();
    }
    
    @Override
    public String getClassRef() {
        return this.getClassInfo().getName();
    }
    
    @Override
    public MixinEnvironment.Phase getPhase() {
        return this.phase;
    }
    
    private ClassInfo getTarget(final String s, final boolean b) throws InvalidMixinException {
        final ClassInfo forName = ClassInfo.forName(s);
        if (forName == null) {
            if (this.isVirtual()) {
                this.logger.debug("Skipping virtual target {} for {}", new Object[] { s, this });
            }
            else {
                this.handleTargetError(String.format("@Mixin target %s was not found %s", s, this));
            }
            return null;
        }
        this.type.validateTarget(s, forName);
        if (b && forName.isPublic() && !this.isVirtual()) {
            this.handleTargetError(String.format("@Mixin target %s is public in %s and should be specified in value", s, this));
        }
        return forName;
    }
    
    protected int readPriority(final ClassNode classNode) {
        if (classNode == null) {
            return this.parent.getDefaultMixinPriority();
        }
        final AnnotationNode invisible = Annotations.getInvisible(classNode, Mixin.class);
        if (invisible == null) {
            throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final Integer n = Annotations.getValue(invisible, "priority");
        return (n == null) ? this.parent.getDefaultMixinPriority() : n;
    }
    
    MixinInfo(final IMixinService service, final MixinConfig parent, final String s, final boolean b, final IMixinConfigPlugin plugin, final boolean b2) {
        this.logger = LogManager.getLogger("mixin");
        this.profiler = MixinEnvironment.getProfiler();
        this.order = MixinInfo.mixinOrder++;
        this.service = service;
        this.parent = parent;
        this.name = s;
        this.className = parent.getMixinPackage() + s;
        this.plugin = plugin;
        this.phase = parent.getEnvironment().getPhase();
        this.strict = parent.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_TARGETS);
        try {
            this.pendingState = new State(this.loadMixinClass(this.className, b));
            this.info = this.pendingState.getClassInfo();
            this.type = SubType.getTypeFor(this);
        }
        catch (InvalidMixinException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new InvalidMixinException(this, ex2);
        }
        if (!this.type.isLoadable()) {
            MixinInfo.classLoaderUtil.registerInvalidClass(this.className);
        }
        try {
            this.priority = this.readPriority(this.pendingState.getClassNode());
            this.virtual = this.readPseudo(this.pendingState.getClassNode());
            this.targetClasses = this.readTargetClasses(this.pendingState.getClassNode(), b2);
            this.targetClassNames = Collections.unmodifiableList((List<? extends String>)Lists.transform((List)this.targetClasses, Functions.toStringFunction()));
        }
        catch (InvalidMixinException ex3) {
            throw ex3;
        }
        catch (Exception ex4) {
            throw new InvalidMixinException(this, ex4);
        }
    }
    
    Set<String> getInterfaces() {
        return this.getState().getInterfaces();
    }
    
    @Override
    public boolean isDetachedSuper() {
        return this.getState().isDetachedSuper();
    }
    
    @Override
    public int compareTo(final MixinInfo mixinInfo) {
        if (mixinInfo == null) {
            return 0;
        }
        if (mixinInfo.priority == this.priority) {
            return this.order - mixinInfo.order;
        }
        return this.priority - mixinInfo.priority;
    }
    
    private void handleTargetError(final String s) {
        if (this.strict) {
            this.logger.error(s);
            throw new InvalidMixinException(this, s);
        }
        this.logger.warn(s);
    }
    
    public boolean isVirtual() {
        return this.virtual;
    }
    
    public boolean isAccessor() {
        return this.type instanceof SubType.Accessor;
    }
    
    MixinTargetContext createContextFor(final TargetClassContext targetClassContext) {
        final MixinClassNode classNode = this.getClassNode(8);
        final Profiler.Section begin = this.profiler.begin("pre");
        final MixinTargetContext context = this.type.createPreProcessor(classNode).prepare().createContextFor(targetClassContext);
        begin.end();
        return context;
    }
    
    static {
        classLoaderUtil = MixinService.getService();
        MixinInfo.mixinOrder = 0;
    }
    
    public void preApply(final String s, final ClassNode classNode) {
        if (this.plugin != null) {
            final Profiler.Section begin = this.profiler.begin("plugin");
            this.plugin.preApply(s, classNode, this.className, this);
            begin.end();
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s:%s", this.parent.getName(), this.name);
    }
    
    protected List<ClassInfo> readTargetClasses(final MixinClassNode mixinClassNode, final boolean b) {
        if (mixinClassNode == null) {
            return Collections.emptyList();
        }
        final AnnotationNode invisible = Annotations.getInvisible(mixinClassNode, Mixin.class);
        if (invisible == null) {
            throw new InvalidMixinException(this, String.format("The mixin '%s' is missing an @Mixin annotation", this.className));
        }
        final ArrayList<ClassInfo> list = new ArrayList<ClassInfo>();
        final List list2 = Annotations.getValue(invisible, "value");
        final List list3 = Annotations.getValue(invisible, "targets");
        if (list2 != null) {
            this.readTargets(list, Lists.transform((List)list2, (Function)new Function<Type, String>() {
                public String apply(final Type type) {
                    return type.getClassName();
                }
            }), b, false);
        }
        if (list3 != null) {
            this.readTargets(list, Lists.transform((List)list3, (Function)new Function<String, String>() {
                public String apply(final String s) {
                    return MixinInfo.this.getParent().remapClassName(MixinInfo.this.getClassRef(), s);
                }
            }), b, true);
        }
        return list;
    }
    
    private boolean shouldApplyMixin(final boolean b, final String s) {
        final Profiler.Section begin = this.profiler.begin("plugin");
        final boolean b2 = this.plugin == null || b || this.plugin.shouldApplyMixin(s, this.className);
        begin.end();
        return b2;
    }
    
    @Override
    public String getClassName() {
        return this.className;
    }
    
    @Override
    public List<String> getTargetClasses() {
        return this.targetClassNames;
    }
    
    void validate() {
        if (this.pendingState == null) {
            throw new IllegalStateException("No pending validation state for " + this);
        }
        try {
            this.pendingState.validate(this.type, this.targetClasses);
            this.state = this.pendingState;
        }
        finally {
            this.pendingState = null;
        }
    }
    
    Set<String> getSyntheticInnerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)this.getState().getSyntheticInnerClasses());
    }
    
    @Override
    public MixinClassNode getClassNode(final int n) {
        return this.getState().createClassNode(n);
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public byte[] getClassBytes() {
        return this.getState().getClassBytes();
    }
    
    public boolean isUnique() {
        return this.getState().isUnique();
    }
    
    private void readTargets(final Collection<ClassInfo> collection, final Collection<String> collection2, final boolean b, final boolean b2) {
        final Iterator<String> iterator = collection2.iterator();
        while (iterator.hasNext()) {
            final String replace = iterator.next().replace('/', '.');
            if (MixinInfo.classLoaderUtil.isClassLoaded(replace) && !this.isReloading()) {
                final String format = String.format("Critical problem: %s target %s was already transformed.", this, replace);
                if (this.parent.isRequired()) {
                    throw new MixinTargetAlreadyLoadedException(this, format, replace);
                }
                this.logger.error(format);
            }
            if (this.shouldApplyMixin(b, replace)) {
                final ClassInfo target = this.getTarget(replace, b2);
                if (target == null || collection.contains(target)) {
                    continue;
                }
                collection.add(target);
                target.addMixin(this);
            }
        }
    }
    
    protected boolean readPseudo(final ClassNode classNode) {
        return Annotations.getInvisible(classNode, Pseudo.class) != null;
    }
    
    private State getState() {
        return (this.state != null) ? this.state : this.pendingState;
    }
    
    private boolean isReloading() {
        return this.pendingState instanceof Reloaded;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    Set<String> getInnerClasses() {
        return Collections.unmodifiableSet((Set<? extends String>)this.getState().getInnerClasses());
    }
    
    List<InterfaceInfo> getSoftImplements() {
        return Collections.unmodifiableList(this.getState().getSoftImplements());
    }
    
    void reloadMixin(final byte[] array) {
        if (this.pendingState != null) {
            throw new IllegalStateException("Cannot reload mixin while it is initialising");
        }
        this.pendingState = new Reloaded(this.state, array);
        this.validate();
    }
    
    @Override
    public IMixinConfig getConfig() {
        return this.parent;
    }
    
    public void postApply(final String s, final ClassNode classNode) {
        if (this.plugin != null) {
            final Profiler.Section begin = this.profiler.begin("plugin");
            this.plugin.postApply(s, classNode, this.className, this);
            begin.end();
        }
        this.parent.postApply(s, classNode);
    }
    
    MixinConfig getParent() {
        return this.parent;
    }
    
    abstract static class SubType
    {
        protected final /* synthetic */ String annotationType;
        protected /* synthetic */ boolean detached;
        protected final /* synthetic */ MixinInfo mixin;
        protected final /* synthetic */ boolean targetMustBeInterface;
        
        abstract MixinPreProcessorStandard createPreProcessor(final MixinClassNode p0);
        
        abstract void validate(final State p0, final List<ClassInfo> p1);
        
        boolean isLoadable() {
            return false;
        }
        
        static SubType getTypeFor(final MixinInfo mixinInfo) {
            if (!mixinInfo.getClassInfo().isInterface()) {
                return new Standard(mixinInfo);
            }
            boolean b = false;
            final Iterator<ClassInfo.Method> iterator = mixinInfo.getClassInfo().getMethods().iterator();
            while (iterator.hasNext()) {
                b |= !iterator.next().isAccessor();
            }
            if (b) {
                return new Interface(mixinInfo);
            }
            return new Accessor(mixinInfo);
        }
        
        boolean isDetachedSuper() {
            return this.detached;
        }
        
        void validateTarget(final String str, final ClassInfo classInfo) {
            final boolean interface1 = classInfo.isInterface();
            if (interface1 != this.targetMustBeInterface) {
                throw new InvalidMixinException(this.mixin, this.annotationType + " target type mismatch: " + str + " is " + (interface1 ? "" : "not ") + "an interface in " + this);
            }
        }
        
        Collection<String> getInterfaces() {
            return (Collection<String>)Collections.emptyList();
        }
        
        SubType(final String annotationType, final boolean targetMustBeInterface) {
            this.annotationType = annotationType;
            this.targetMustBeInterface = targetMustBeInterface;
        }
        
        static class Interface extends SubType
        {
            @Override
            void validate(final State state, final List<ClassInfo> list) {
                if (!MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    throw new InvalidMixinException(this.mixin, "Interface mixin not supported in current enviromnment");
                }
                final MixinClassNode classNode = state.getClassNode();
                if (!"java/lang/Object".equals(classNode.superName)) {
                    throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                }
            }
            
            Interface(final MixinInfo mixinInfo) {
                super(mixinInfo, "@Mixin", true);
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode mixinClassNode) {
                return new MixinPreProcessorInterface(this.mixin, mixinClassNode);
            }
        }
        
        static class Standard extends SubType
        {
            @Override
            void validate(final State state, final List<ClassInfo> list) {
                final MixinClassNode classNode = state.getClassNode();
                for (final ClassInfo obj : list) {
                    if (classNode.superName.equals(obj.getSuperName())) {
                        continue;
                    }
                    if (!obj.hasSuperClass(classNode.superName, ClassInfo.Traversal.SUPER)) {
                        final ClassInfo forName = ClassInfo.forName(classNode.superName);
                        if (forName.isMixin()) {
                            for (final ClassInfo classInfo : forName.getTargets()) {
                                if (list.contains(classInfo)) {
                                    throw new InvalidMixinException(this.mixin, "Illegal hierarchy detected. Derived mixin " + this + " targets the same class " + classInfo.getClassName() + " as its superclass " + forName.getClassName());
                                }
                            }
                        }
                        throw new InvalidMixinException(this.mixin, "Super class '" + classNode.superName.replace('/', '.') + "' of " + this.mixin.getName() + " was not found in the hierarchy of target class '" + obj + "'");
                    }
                    this.detached = true;
                }
            }
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode mixinClassNode) {
                return new MixinPreProcessorStandard(this.mixin, mixinClassNode);
            }
            
            Standard(final MixinInfo mixinInfo) {
                super(mixinInfo, "@Mixin", false);
            }
        }
        
        static class Accessor extends SubType
        {
            private final /* synthetic */ Collection<String> interfaces;
            
            @Override
            MixinPreProcessorStandard createPreProcessor(final MixinClassNode mixinClassNode) {
                return new MixinPreProcessorAccessor(this.mixin, mixinClassNode);
            }
            
            @Override
            void validateTarget(final String s, final ClassInfo classInfo) {
                if (classInfo.isInterface() && !MixinEnvironment.getCompatibilityLevel().supportsMethodsInInterfaces()) {
                    throw new InvalidMixinException(this.mixin, "Accessor mixin targetting an interface is not supported in current enviromnment");
                }
            }
            
            @Override
            void validate(final State state, final List<ClassInfo> list) {
                final MixinClassNode classNode = state.getClassNode();
                if (!"java/lang/Object".equals(classNode.superName)) {
                    throw new InvalidMixinException(this.mixin, "Super class of " + this + " is invalid, found " + classNode.superName.replace('/', '.'));
                }
            }
            
            Accessor(final MixinInfo mixinInfo) {
                super(mixinInfo, "@Mixin", false);
                this.interfaces = new ArrayList<String>();
                this.interfaces.add(mixinInfo.getClassRef());
            }
            
            @Override
            boolean isLoadable() {
                return true;
            }
            
            @Override
            Collection<String> getInterfaces() {
                return this.interfaces;
            }
        }
    }
    
    class MixinClassNode extends ClassNode
    {
        public final /* synthetic */ List<MixinMethodNode> mixinMethods;
        
        public MixinClassNode(final MixinInfo mixinInfo, final MixinInfo mixinInfo2) {
            this(mixinInfo, 327680);
        }
        
        @Override
        public MethodVisitor visitMethod(final int n, final String s, final String s2, final String s3, final String[] array) {
            final MixinMethodNode mixinMethodNode = new MixinMethodNode(n, s, s2, s3, array);
            this.methods.add(mixinMethodNode);
            return mixinMethodNode;
        }
        
        public MixinClassNode(final int n) {
            super(n);
            this.mixinMethods = (List<MixinMethodNode>)this.methods;
        }
        
        public MixinInfo getMixin() {
            return MixinInfo.this;
        }
    }
    
    class MixinMethodNode extends MethodNode
    {
        private final /* synthetic */ String originalName;
        
        public boolean isSurrogate() {
            return this.getVisibleAnnotation(Surrogate.class) != null;
        }
        
        @Override
        public String toString() {
            return String.format("%s%s", this.originalName, this.desc);
        }
        
        public AnnotationNode getVisibleAnnotation(final Class<? extends Annotation> clazz) {
            return Annotations.getVisible(this, clazz);
        }
        
        public AnnotationNode getInjectorAnnotation() {
            return InjectionInfo.getInjectorAnnotation(MixinInfo.this, this);
        }
        
        public String getOriginalName() {
            return this.originalName;
        }
        
        public boolean isInjector() {
            return this.getInjectorAnnotation() != null || this.isSurrogate();
        }
        
        public MixinMethodNode(final int n, final String originalName, final String s, final String s2, final String[] array) {
            super(327680, n, originalName, s, s2, array);
            this.originalName = originalName;
        }
        
        public boolean isSynthetic() {
            return Bytecode.hasFlag(this, 4096);
        }
        
        public IMixinInfo getOwner() {
            return MixinInfo.this;
        }
    }
    
    class State
    {
        private /* synthetic */ boolean detachedSuper;
        protected final /* synthetic */ Set<String> innerClasses;
        protected final /* synthetic */ List<InterfaceInfo> softImplements;
        private /* synthetic */ boolean unique;
        private final /* synthetic */ ClassInfo classInfo;
        protected final /* synthetic */ Set<String> interfaces;
        private /* synthetic */ byte[] mixinBytes;
        protected final /* synthetic */ Set<String> syntheticInnerClasses;
        protected /* synthetic */ MixinClassNode classNode;
        
        State(final byte[] mixinBytes, final ClassInfo classInfo) {
            this.interfaces = new HashSet<String>();
            this.softImplements = new ArrayList<InterfaceInfo>();
            this.syntheticInnerClasses = new HashSet<String>();
            this.innerClasses = new HashSet<String>();
            this.mixinBytes = mixinBytes;
            this.connect();
            this.classInfo = ((classInfo != null) ? classInfo : ClassInfo.fromClassNode(this.getClassNode()));
        }
        
        MixinClassNode createClassNode(final int n) {
            final MixinClassNode mixinClassNode = new MixinClassNode(MixinInfo.this);
            new ClassReader(this.mixinBytes).accept(mixinClassNode, n);
            return mixinClassNode;
        }
        
        private void validateClassVersion() {
            if (this.classNode.version > MixinEnvironment.getCompatibilityLevel().classVersion()) {
                String format = ".";
                for (final MixinEnvironment.CompatibilityLevel compatibilityLevel : MixinEnvironment.CompatibilityLevel.values()) {
                    if (compatibilityLevel.classVersion() >= this.classNode.version) {
                        format = String.format(". Mixin requires compatibility level %s or above.", compatibilityLevel.name());
                    }
                }
                throw new InvalidMixinException(MixinInfo.this, "Unsupported mixin class version " + this.classNode.version + format);
            }
        }
        
        private void connect() {
            this.classNode = this.createClassNode(0);
        }
        
        private void validateInner() {
            if (!this.classInfo.isProbablyStatic()) {
                throw new InvalidMixinException(MixinInfo.this, "Inner class mixin must be declared static");
            }
        }
        
        private void validateRemappable(final Class<Shadow> clazz, final String str, final AnnotationNode annotationNode) {
            if (annotationNode != null && Annotations.getValue(annotationNode, "remap", Boolean.TRUE)) {
                throw new InvalidMixinException(MixinInfo.this, "Found a remappable @" + clazz.getSimpleName() + " annotation on " + str + " in " + this);
            }
        }
        
        void readInnerClasses() {
            for (final InnerClassNode innerClassNode : this.classNode.innerClasses) {
                final ClassInfo forName = ClassInfo.forName(innerClassNode.name);
                if ((innerClassNode.outerName != null && innerClassNode.outerName.equals(this.classInfo.getName())) || innerClassNode.name.startsWith(this.classNode.name + "$")) {
                    if (forName.isProbablyStatic() && forName.isSynthetic()) {
                        this.syntheticInnerClasses.add(innerClassNode.name);
                    }
                    else {
                        this.innerClasses.add(innerClassNode.name);
                    }
                }
            }
        }
        
        byte[] getClassBytes() {
            return this.mixinBytes;
        }
        
        void readImplementations(final SubType subType) {
            this.interfaces.addAll(this.classNode.interfaces);
            this.interfaces.addAll(subType.getInterfaces());
            final AnnotationNode invisible = Annotations.getInvisible(this.classNode, Implements.class);
            if (invisible == null) {
                return;
            }
            final List<AnnotationNode> list = Annotations.getValue(invisible);
            if (list == null) {
                return;
            }
            final Iterator<AnnotationNode> iterator = list.iterator();
            while (iterator.hasNext()) {
                final InterfaceInfo fromAnnotation = InterfaceInfo.fromAnnotation(MixinInfo.this, iterator.next());
                this.softImplements.add(fromAnnotation);
                this.interfaces.add(fromAnnotation.getInternalName());
                if (!(this instanceof Reloaded)) {
                    this.classInfo.addInterface(fromAnnotation.getInternalName());
                }
            }
        }
        
        protected void validateChanges(final SubType subType, final List<ClassInfo> list) {
            subType.createPreProcessor(this.classNode).prepare();
        }
        
        private void validateRemappables(final List<ClassInfo> list) {
            if (list.size() > 1) {
                for (final FieldNode fieldNode : this.classNode.fields) {
                    this.validateRemappable(Shadow.class, fieldNode.name, Annotations.getVisible(fieldNode, Shadow.class));
                }
                for (final MethodNode methodNode : this.classNode.methods) {
                    this.validateRemappable(Shadow.class, methodNode.name, Annotations.getVisible(methodNode, Shadow.class));
                    if (Annotations.getVisible(methodNode, Overwrite.class) != null && ((methodNode.access & 0x8) == 0x0 || (methodNode.access & 0x1) == 0x0)) {
                        throw new InvalidMixinException(MixinInfo.this, "Found @Overwrite annotation on " + methodNode.name + " in " + MixinInfo.this);
                    }
                }
            }
        }
        
        List<? extends InterfaceInfo> getSoftImplements() {
            return this.softImplements;
        }
        
        State(final MixinInfo mixinInfo, final byte[] array) {
            this(mixinInfo, array, null);
        }
        
        MixinClassNode getClassNode() {
            return this.classNode;
        }
        
        Set<String> getInnerClasses() {
            return this.innerClasses;
        }
        
        Set<String> getSyntheticInnerClasses() {
            return this.syntheticInnerClasses;
        }
        
        boolean isUnique() {
            return this.unique;
        }
        
        Set<String> getInterfaces() {
            return this.interfaces;
        }
        
        void validate(final SubType subType, final List<ClassInfo> list) {
            final MixinPreProcessorStandard prepare = subType.createPreProcessor(this.getClassNode()).prepare();
            final Iterator<ClassInfo> iterator = list.iterator();
            while (iterator.hasNext()) {
                prepare.conform(iterator.next());
            }
            subType.validate(this, list);
            this.detachedSuper = subType.isDetachedSuper();
            this.unique = (Annotations.getVisible(this.getClassNode(), Unique.class) != null);
            this.validateInner();
            this.validateClassVersion();
            this.validateRemappables(list);
            this.readImplementations(subType);
            this.readInnerClasses();
            this.validateChanges(subType, list);
            this.complete();
        }
        
        boolean isDetachedSuper() {
            return this.detachedSuper;
        }
        
        private void complete() {
            this.classNode = null;
        }
        
        ClassInfo getClassInfo() {
            return this.classInfo;
        }
    }
    
    class Reloaded extends State
    {
        private final /* synthetic */ State previous;
        
        @Override
        protected void validateChanges(final SubType subType, final List<ClassInfo> c) {
            if (!this.syntheticInnerClasses.equals(this.previous.syntheticInnerClasses)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change inner classes");
            }
            if (!this.interfaces.equals(this.previous.interfaces)) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change interfaces");
            }
            if (!new HashSet(this.softImplements).equals(new HashSet(this.previous.softImplements))) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change soft interfaces");
            }
            if (!new HashSet(MixinInfo.this.readTargetClasses(this.classNode, true)).equals(new HashSet(c))) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change target classes");
            }
            if (MixinInfo.this.readPriority(this.classNode) != MixinInfo.this.getPriority()) {
                throw new MixinReloadException(MixinInfo.this, "Cannot change mixin priority");
            }
        }
        
        Reloaded(final State previous, final byte[] array) {
            super(array, previous.getClassInfo());
            this.previous = previous;
        }
    }
}
