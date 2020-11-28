// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import java.util.HashSet;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.util.throwables.InvalidConstraintException;
import org.spongepowered.asm.util.throwables.ConstraintViolationException;
import org.spongepowered.asm.util.ITokenProvider;
import org.spongepowered.asm.util.ConstraintParser;
import org.spongepowered.asm.lib.signature.SignatureVisitor;
import org.spongepowered.asm.lib.signature.SignatureReader;
import org.spongepowered.asm.mixin.Intrinsic;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.ArrayDeque;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.SortedSet;
import org.spongepowered.asm.mixin.transformer.meta.MixinRenamed;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Overwrite;
import java.util.Map;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.transformer.throwables.InvalidMixinException;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.util.Bytecode;
import java.util.Iterator;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Deque;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.lang.annotation.Annotation;
import java.util.List;
import org.spongepowered.asm.util.perf.Profiler;

class MixinApplicatorStandard
{
    protected final /* synthetic */ String targetName;
    protected static final /* synthetic */ int[] INITIALISER_OPCODE_BLACKLIST;
    protected final /* synthetic */ Profiler profiler;
    protected static final /* synthetic */ List<Class<? extends Annotation>> CONSTRAINED_ANNOTATIONS;
    protected final /* synthetic */ TargetClassContext context;
    protected final /* synthetic */ ClassNode targetClass;
    protected final /* synthetic */ Logger logger;
    
    protected void applyInitialisers(final MixinTargetContext mixinTargetContext) {
        final MethodNode constructor = this.getConstructor(mixinTargetContext);
        if (constructor == null) {
            return;
        }
        final Deque<AbstractInsnNode> initialiser = this.getInitialiser(mixinTargetContext, constructor);
        if (initialiser == null || initialiser.size() == 0) {
            return;
        }
        for (final MethodNode methodNode : this.targetClass.methods) {
            if ("<init>".equals(methodNode.name)) {
                methodNode.maxStack = Math.max(methodNode.maxStack, constructor.maxStack);
                this.injectInitialiser(mixinTargetContext, methodNode, initialiser);
            }
        }
    }
    
    protected final void injectInitialiser(final MixinTargetContext obj, final MethodNode methodNode, final Deque<AbstractInsnNode> deque) {
        final Map<LabelNode, LabelNode> cloneLabels = Bytecode.cloneLabels(methodNode.instructions);
        AbstractInsnNode initialiserInjectionPoint = this.findInitialiserInjectionPoint(obj, methodNode, deque);
        if (initialiserInjectionPoint == null) {
            this.logger.warn("Failed to locate initialiser injection point in <init>{}, initialiser was not mixed in.", new Object[] { methodNode.desc });
            return;
        }
        for (final AbstractInsnNode abstractInsnNode : deque) {
            if (abstractInsnNode instanceof LabelNode) {
                continue;
            }
            if (abstractInsnNode instanceof JumpInsnNode) {
                throw new InvalidMixinException(obj, "Unsupported JUMP opcode in initialiser in " + obj);
            }
            final AbstractInsnNode clone = abstractInsnNode.clone(cloneLabels);
            methodNode.instructions.insert(initialiserInjectionPoint, clone);
            initialiserInjectionPoint = clone;
        }
    }
    
    protected void checkMethodVisibility(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        if (Bytecode.hasFlag(methodNode, 8) && !Bytecode.hasFlag(methodNode, 2) && !Bytecode.hasFlag(methodNode, 4096) && Annotations.getVisible(methodNode, Overwrite.class) == null) {
            throw new InvalidMixinException(mixinTargetContext, String.format("Mixin %s contains non-private static method %s", mixinTargetContext, methodNode));
        }
    }
    
    protected final void applyMixin(final MixinTargetContext mixinTargetContext, final ApplicatorPass obj) {
        switch (obj) {
            case MAIN: {
                this.applySignature(mixinTargetContext);
                this.applyInterfaces(mixinTargetContext);
                this.applyAttributes(mixinTargetContext);
                this.applyAnnotations(mixinTargetContext);
                this.applyFields(mixinTargetContext);
                this.applyMethods(mixinTargetContext);
                this.applyInitialisers(mixinTargetContext);
                break;
            }
            case PREINJECT: {
                this.prepareInjections(mixinTargetContext);
                break;
            }
            case INJECT: {
                this.applyAccessors(mixinTargetContext);
                this.applyInjections(mixinTargetContext);
                break;
            }
            default: {
                throw new IllegalStateException("Invalid pass specified " + obj);
            }
        }
    }
    
    protected void applyShadowMethod(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        final MethodNode targetMethod = this.findTargetMethod(methodNode);
        if (targetMethod != null) {
            Bytecode.mergeAnnotations(methodNode, targetMethod);
        }
    }
    
    protected void displaceIntrinsic(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final MethodNode methodNode2) {
        final String string = "proxy+" + methodNode2.name;
        for (final AbstractInsnNode abstractInsnNode : methodNode.instructions) {
            if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() != 184) {
                final MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                if (!methodInsnNode.owner.equals(this.targetClass.name) || !methodInsnNode.name.equals(methodNode2.name) || !methodInsnNode.desc.equals(methodNode2.desc)) {
                    continue;
                }
                methodInsnNode.name = string;
            }
        }
        methodNode2.name = string;
    }
    
    protected void applyInterfaces(final MixinTargetContext mixinTargetContext) {
        for (final String s : mixinTargetContext.getInterfaces()) {
            if (!this.targetClass.interfaces.contains(s)) {
                this.targetClass.interfaces.add(s);
                mixinTargetContext.getTargetClassInfo().addInterface(s);
            }
        }
    }
    
    protected boolean mergeIntrinsic(final MixinTargetContext obj, final MethodNode methodNode, final boolean b, final MethodNode methodNode2, final AnnotationNode annotationNode) {
        if (b) {
            throw new InvalidMixinException(obj, "@Intrinsic is not compatible with @Overwrite, remove one of these annotations on " + methodNode.name + " in " + obj);
        }
        final String string = methodNode.name + methodNode.desc;
        if (Bytecode.hasFlag(methodNode, 8)) {
            throw new InvalidMixinException(obj, "@Intrinsic method cannot be static, found " + string + " in " + obj);
        }
        if (!Bytecode.hasFlag(methodNode, 4096)) {
            final AnnotationNode visible = Annotations.getVisible(methodNode, MixinRenamed.class);
            if (visible == null || !Annotations.getValue(visible, "isInterfaceMember", Boolean.FALSE)) {
                throw new InvalidMixinException(obj, "@Intrinsic method must be prefixed interface method, no rename encountered on " + string + " in " + obj);
            }
        }
        if (!Annotations.getValue(annotationNode, "displace", Boolean.FALSE)) {
            this.logger.log(obj.getLoggingLevel(), "Skipping Intrinsic mixin method {} for {}", new Object[] { string, obj.getTargetClassRef() });
            return true;
        }
        this.displaceIntrinsic(obj, methodNode, methodNode2);
        return false;
    }
    
    protected void applyAnnotations(final MixinTargetContext mixinTargetContext) {
        Bytecode.mergeAnnotations(mixinTargetContext.getClassNode(), this.targetClass);
    }
    
    void apply(final SortedSet<MixinInfo> set) {
        final ArrayList<MixinTargetContext> list = new ArrayList<MixinTargetContext>();
        for (final MixinInfo mixinInfo : set) {
            this.logger.log(mixinInfo.getLoggingLevel(), "Mixing {} from {} into {}", new Object[] { mixinInfo.getName(), mixinInfo.getParent(), this.targetName });
            list.add(mixinInfo.createContextFor(this.context));
        }
        IMixinContext mixinContext = null;
        try {
            final Iterator<Object> iterator2 = list.iterator();
            while (iterator2.hasNext()) {
                ((MixinTargetContext)(mixinContext = iterator2.next())).preApply(this.targetName, this.targetClass);
            }
            for (final ApplicatorPass applicatorPass : ApplicatorPass.values()) {
                final Profiler.Section begin = this.profiler.begin("pass", applicatorPass.name().toLowerCase());
                final Iterator<Object> iterator3 = list.iterator();
                while (iterator3.hasNext()) {
                    this.applyMixin((MixinTargetContext)(mixinContext = iterator3.next()), applicatorPass);
                }
                begin.end();
            }
            final Iterator<Object> iterator4 = list.iterator();
            while (iterator4.hasNext()) {
                ((MixinTargetContext)(mixinContext = iterator4.next())).postApply(this.targetName, this.targetClass);
            }
        }
        catch (InvalidMixinException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new InvalidMixinException(mixinContext, "Unexpecteded " + ex2.getClass().getSimpleName() + " whilst applying the mixin class: " + ex2.getMessage(), ex2);
        }
        this.applySourceMap(this.context);
        this.context.processDebugTasks();
    }
    
    protected void applyAccessors(final MixinTargetContext mixinTargetContext) {
        for (final MethodNode methodNode : mixinTargetContext.generateAccessors()) {
            if (!methodNode.name.startsWith("<")) {
                this.mergeMethod(mixinTargetContext, methodNode);
            }
        }
    }
    
    MixinApplicatorStandard(final TargetClassContext context) {
        this.logger = LogManager.getLogger("mixin");
        this.profiler = MixinEnvironment.getProfiler();
        this.context = context;
        this.targetName = context.getClassName();
        this.targetClass = context.getClassNode();
    }
    
    protected final void appendInsns(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        if (Type.getReturnType(methodNode.desc) != Type.VOID_TYPE) {
            throw new IllegalArgumentException("Attempted to merge insns from a method which does not return void");
        }
        final MethodNode targetMethod = this.findTargetMethod(methodNode);
        if (targetMethod != null) {
            final AbstractInsnNode insn = Bytecode.findInsn(targetMethod, 177);
            if (insn != null) {
                for (final AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                    if (!(abstractInsnNode instanceof LineNumberNode) && abstractInsnNode.getOpcode() != 177) {
                        targetMethod.instructions.insertBefore(insn, abstractInsnNode);
                    }
                }
                targetMethod.maxLocals = Math.max(targetMethod.maxLocals, methodNode.maxLocals);
                targetMethod.maxStack = Math.max(targetMethod.maxStack, methodNode.maxStack);
            }
            return;
        }
        this.targetClass.methods.add(methodNode);
    }
    
    protected void applySourceMap(final TargetClassContext targetClassContext) {
        this.targetClass.sourceDebug = targetClassContext.getSourceMap().toString();
    }
    
    protected final Deque<AbstractInsnNode> getInitialiser(final MixinTargetContext obj, final MethodNode methodNode) {
        final Range constructorRange = this.getConstructorRange(methodNode);
        if (!constructorRange.isValid()) {
            return null;
        }
        final ArrayDeque<AbstractInsnNode> arrayDeque = new ArrayDeque<AbstractInsnNode>();
        boolean excludes = false;
        int n = -1;
        LabelNode labelNode = null;
        final ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator(constructorRange.marker);
        while (iterator.hasNext()) {
            final AbstractInsnNode abstractInsnNode = iterator.next();
            if (abstractInsnNode instanceof LineNumberNode) {
                final int line = ((LineNumberNode)abstractInsnNode).line;
                final AbstractInsnNode value = methodNode.instructions.get(methodNode.instructions.indexOf(abstractInsnNode) + 1);
                if (line == constructorRange.end && value.getOpcode() != 177) {
                    excludes = true;
                    n = 177;
                }
                else {
                    excludes = constructorRange.excludes(line);
                    n = -1;
                }
            }
            else {
                if (!excludes) {
                    continue;
                }
                if (labelNode != null) {
                    arrayDeque.add(labelNode);
                    labelNode = null;
                }
                if (abstractInsnNode instanceof LabelNode) {
                    labelNode = (LabelNode)abstractInsnNode;
                }
                else {
                    final int opcode = abstractInsnNode.getOpcode();
                    if (opcode == n) {
                        n = -1;
                    }
                    else {
                        final int[] initialiser_OPCODE_BLACKLIST = MixinApplicatorStandard.INITIALISER_OPCODE_BLACKLIST;
                        for (int length = initialiser_OPCODE_BLACKLIST.length, i = 0; i < length; ++i) {
                            if (opcode == initialiser_OPCODE_BLACKLIST[i]) {
                                throw new InvalidMixinException(obj, "Cannot handle " + Bytecode.getOpcodeName(opcode) + " opcode (0x" + Integer.toHexString(opcode).toUpperCase() + ") in class initialiser");
                            }
                        }
                        arrayDeque.add(abstractInsnNode);
                    }
                }
            }
        }
        final LabelNode labelNode2 = arrayDeque.peekLast();
        if (labelNode2 != null && labelNode2.getOpcode() != 181) {
            throw new InvalidMixinException(obj, "Could not parse initialiser, expected 0xB5, found 0x" + Integer.toHexString(labelNode2.getOpcode()) + " in " + obj);
        }
        return arrayDeque;
    }
    
    protected void applyAttributes(final MixinTargetContext mixinTargetContext) {
        if (mixinTargetContext.shouldSetSourceFile()) {
            this.targetClass.sourceFile = mixinTargetContext.getSourceFile();
        }
        this.targetClass.version = Math.max(this.targetClass.version, mixinTargetContext.getMinRequiredClassVersion());
    }
    
    protected void mergeShadowFields(final MixinTargetContext mixinTargetContext) {
        for (final Map.Entry<FieldNode, ClassInfo.Field> entry : mixinTargetContext.getShadowFields()) {
            final FieldNode fieldNode = entry.getKey();
            final FieldNode targetField = this.findTargetField(fieldNode);
            if (targetField != null) {
                Bytecode.mergeAnnotations(fieldNode, targetField);
                if (!entry.getValue().isDecoratedMutable() || Bytecode.hasFlag(targetField, 2)) {
                    continue;
                }
                final FieldNode fieldNode2 = targetField;
                fieldNode2.access &= 0xFFFFFFEF;
            }
        }
    }
    
    private Range getConstructorRange(final MethodNode methodNode) {
        boolean b = false;
        AbstractInsnNode abstractInsnNode = null;
        int line = 0;
        int n = 0;
        int n2 = 0;
        int index = -1;
        for (final AbstractInsnNode abstractInsnNode2 : methodNode.instructions) {
            if (abstractInsnNode2 instanceof LineNumberNode) {
                line = ((LineNumberNode)abstractInsnNode2).line;
                b = true;
            }
            else if (abstractInsnNode2 instanceof MethodInsnNode) {
                if (abstractInsnNode2.getOpcode() != 183 || !"<init>".equals(((MethodInsnNode)abstractInsnNode2).name) || index != -1) {
                    continue;
                }
                index = methodNode.instructions.indexOf(abstractInsnNode2);
                n = line;
            }
            else if (abstractInsnNode2.getOpcode() == 181) {
                b = false;
            }
            else {
                if (abstractInsnNode2.getOpcode() != 177) {
                    continue;
                }
                if (b) {
                    n2 = line;
                }
                else {
                    n2 = n;
                    abstractInsnNode = abstractInsnNode2;
                }
            }
        }
        if (abstractInsnNode != null) {
            final LabelNode labelNode = new LabelNode(new Label());
            methodNode.instructions.insertBefore(abstractInsnNode, labelNode);
            methodNode.instructions.insertBefore(abstractInsnNode, new LineNumberNode(n, labelNode));
        }
        return new Range(n, n2, index);
    }
    
    protected void applyNormalMethod(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        mixinTargetContext.transformMethod(methodNode);
        if (!methodNode.name.startsWith("<")) {
            this.checkMethodVisibility(mixinTargetContext, methodNode);
            this.checkMethodConstraints(mixinTargetContext, methodNode);
            this.mergeMethod(mixinTargetContext, methodNode);
        }
        else if ("<clinit>".equals(methodNode.name)) {
            this.appendInsns(mixinTargetContext, methodNode);
        }
    }
    
    protected void applySignature(final MixinTargetContext mixinTargetContext) {
        this.context.mergeSignature(mixinTargetContext.getSignature());
    }
    
    protected final MethodNode findTargetMethod(final MethodNode methodNode) {
        for (final MethodNode methodNode2 : this.targetClass.methods) {
            if (methodNode2.name.equals(methodNode.name) && methodNode2.desc.equals(methodNode.desc)) {
                return methodNode2;
            }
        }
        return null;
    }
    
    protected MethodNode getConstructor(final MixinTargetContext mixinTargetContext) {
        MethodNode methodNode = null;
        for (final MethodNode methodNode2 : mixinTargetContext.getMethods()) {
            if ("<init>".equals(methodNode2.name) && Bytecode.methodHasLineNumbers(methodNode2)) {
                if (methodNode == null) {
                    methodNode = methodNode2;
                }
                else {
                    this.logger.warn(String.format("Mixin %s has multiple constructors, %s was selected\n", mixinTargetContext, methodNode.desc));
                }
            }
        }
        return methodNode;
    }
    
    protected boolean isAlreadyMerged(final MixinTargetContext obj, final MethodNode methodNode, final boolean b, final MethodNode methodNode2) {
        final AnnotationNode visible = Annotations.getVisible(methodNode2, MixinMerged.class);
        if (visible == null) {
            if (Annotations.getVisible(methodNode2, Final.class) != null) {
                this.logger.warn("Overwrite prohibited for @Final method {} in {}. Skipping method.", new Object[] { methodNode.name, obj });
                return true;
            }
            return false;
        }
        else {
            if (!this.context.getSessionId().equals(Annotations.getValue(visible, "sessionId"))) {
                throw new ClassFormatError("Invalid @MixinMerged annotation found in" + obj + " at " + methodNode.name + " in " + this.targetClass.name);
            }
            if (Bytecode.hasFlag(methodNode2, 4160) && Bytecode.hasFlag(methodNode, 4160)) {
                if (obj.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    this.logger.warn("Synthetic bridge method clash for {} in {}", new Object[] { methodNode.name, obj });
                }
                return true;
            }
            final String s = Annotations.getValue(visible, "mixin");
            if (Annotations.getValue(visible, "priority") >= obj.getPriority() && !s.equals(obj.getClassName())) {
                this.logger.warn("Method overwrite conflict for {} in {}, previously written by {}. Skipping method.", new Object[] { methodNode.name, obj, s });
                return true;
            }
            if (Annotations.getVisible(methodNode2, Final.class) != null) {
                this.logger.warn("Method overwrite conflict for @Final method {} in {} declared by {}. Skipping method.", new Object[] { methodNode.name, obj, s });
                return true;
            }
            return false;
        }
    }
    
    static {
        CONSTRAINED_ANNOTATIONS = (List)ImmutableList.of((Object)Overwrite.class, (Object)Inject.class, (Object)ModifyArg.class, (Object)ModifyArgs.class, (Object)Redirect.class, (Object)ModifyVariable.class, (Object)ModifyConstant.class);
        INITIALISER_OPCODE_BLACKLIST = new int[] { 177, 21, 22, 23, 24, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 79, 80, 81, 82, 83, 84, 85, 86 };
    }
    
    protected void mergeMethod(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        final boolean b = Annotations.getVisible(methodNode, Overwrite.class) != null;
        final MethodNode targetMethod = this.findTargetMethod(methodNode);
        if (targetMethod != null) {
            if (this.isAlreadyMerged(mixinTargetContext, methodNode, b, targetMethod)) {
                return;
            }
            final AnnotationNode invisible = Annotations.getInvisible(methodNode, Intrinsic.class);
            if (invisible != null) {
                if (this.mergeIntrinsic(mixinTargetContext, methodNode, b, targetMethod, invisible)) {
                    mixinTargetContext.getTarget().methodMerged(methodNode);
                    return;
                }
            }
            else {
                if (mixinTargetContext.requireOverwriteAnnotations() && !b) {
                    throw new InvalidMixinException(mixinTargetContext, String.format("%s%s in %s cannot overwrite method in %s because @Overwrite is required by the parent configuration", methodNode.name, methodNode.desc, mixinTargetContext, mixinTargetContext.getTarget().getClassName()));
                }
                this.targetClass.methods.remove(targetMethod);
            }
        }
        else if (b) {
            throw new InvalidMixinException(mixinTargetContext, String.format("Overwrite target \"%s\" was not located in target class %s", methodNode.name, mixinTargetContext.getTargetClassRef()));
        }
        this.targetClass.methods.add(methodNode);
        mixinTargetContext.methodMerged(methodNode);
        if (methodNode.signature != null) {
            final SignatureVisitor remapper = mixinTargetContext.getSignature().getRemapper();
            new SignatureReader(methodNode.signature).accept(remapper);
            methodNode.signature = remapper.toString();
        }
    }
    
    protected final FieldNode findTargetField(final FieldNode fieldNode) {
        for (final FieldNode fieldNode2 : this.targetClass.fields) {
            if (fieldNode2.name.equals(fieldNode.name)) {
                return fieldNode2;
            }
        }
        return null;
    }
    
    protected void applyMethods(final MixinTargetContext mixinTargetContext) {
        final Iterator<MethodNode> iterator = mixinTargetContext.getShadowMethods().iterator();
        while (iterator.hasNext()) {
            this.applyShadowMethod(mixinTargetContext, iterator.next());
        }
        final Iterator<MethodNode> iterator2 = mixinTargetContext.getMethods().iterator();
        while (iterator2.hasNext()) {
            this.applyNormalMethod(mixinTargetContext, iterator2.next());
        }
    }
    
    protected void applyInjections(final MixinTargetContext mixinTargetContext) {
        mixinTargetContext.applyInjections();
    }
    
    protected final void checkConstraints(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final AnnotationNode annotationNode) {
        try {
            final ConstraintParser.Constraint parse = ConstraintParser.parse(annotationNode);
            try {
                parse.check(mixinTargetContext.getEnvironment());
            }
            catch (ConstraintViolationException ex) {
                final String format = String.format("Constraint violation: %s on %s in %s", ex.getMessage(), methodNode, mixinTargetContext);
                this.logger.warn(format);
                if (!mixinTargetContext.getEnvironment().getOption(MixinEnvironment.Option.IGNORE_CONSTRAINTS)) {
                    throw new InvalidMixinException(mixinTargetContext, format, ex);
                }
            }
        }
        catch (InvalidConstraintException ex2) {
            throw new InvalidMixinException(mixinTargetContext, ex2.getMessage());
        }
    }
    
    protected void applyFields(final MixinTargetContext mixinTargetContext) {
        this.mergeShadowFields(mixinTargetContext);
        this.mergeNewFields(mixinTargetContext);
    }
    
    private static String fieldKey(final FieldInsnNode fieldInsnNode) {
        return String.format("%s:%s", fieldInsnNode.desc, fieldInsnNode.name);
    }
    
    private InitialiserInjectionMode getInitialiserInjectionMode(final MixinEnvironment mixinEnvironment) {
        final String optionValue = mixinEnvironment.getOptionValue(MixinEnvironment.Option.INITIALISER_INJECTION_MODE);
        if (optionValue == null) {
            return InitialiserInjectionMode.DEFAULT;
        }
        try {
            return InitialiserInjectionMode.valueOf(optionValue.toUpperCase());
        }
        catch (Exception ex) {
            this.logger.warn("Could not parse unexpected value \"{}\" for mixin.initialiserInjectionMode, reverting to DEFAULT", new Object[] { optionValue });
            return InitialiserInjectionMode.DEFAULT;
        }
    }
    
    protected void prepareInjections(final MixinTargetContext mixinTargetContext) {
        mixinTargetContext.prepareInjections();
    }
    
    protected void mergeNewFields(final MixinTargetContext mixinTargetContext) {
        for (final FieldNode fieldNode : mixinTargetContext.getFields()) {
            if (this.findTargetField(fieldNode) == null) {
                this.targetClass.fields.add(fieldNode);
            }
        }
    }
    
    protected void checkMethodConstraints(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        final Iterator<Class<? extends Annotation>> iterator = MixinApplicatorStandard.CONSTRAINED_ANNOTATIONS.iterator();
        while (iterator.hasNext()) {
            final AnnotationNode visible = Annotations.getVisible(methodNode, iterator.next());
            if (visible != null) {
                this.checkConstraints(mixinTargetContext, methodNode, visible);
            }
        }
    }
    
    protected AbstractInsnNode findInitialiserInjectionPoint(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final Deque<AbstractInsnNode> deque) {
        final HashSet<String> set = new HashSet<String>();
        for (final AbstractInsnNode abstractInsnNode : deque) {
            if (abstractInsnNode.getOpcode() == 181) {
                set.add(fieldKey((FieldInsnNode)abstractInsnNode));
            }
        }
        final InitialiserInjectionMode initialiserInjectionMode = this.getInitialiserInjectionMode(mixinTargetContext.getEnvironment());
        final String name = mixinTargetContext.getTargetClassInfo().getName();
        final String superName = mixinTargetContext.getTargetClassInfo().getSuperName();
        AbstractInsnNode abstractInsnNode2 = null;
        for (final AbstractInsnNode abstractInsnNode3 : methodNode.instructions) {
            if (abstractInsnNode3.getOpcode() == 183 && "<init>".equals(((MethodInsnNode)abstractInsnNode3).name)) {
                final String owner = ((MethodInsnNode)abstractInsnNode3).owner;
                if (!owner.equals(name) && !owner.equals(superName)) {
                    continue;
                }
                abstractInsnNode2 = abstractInsnNode3;
                if (initialiserInjectionMode == InitialiserInjectionMode.SAFE) {
                    break;
                }
                continue;
            }
            else {
                if (abstractInsnNode3.getOpcode() != 181 || initialiserInjectionMode != InitialiserInjectionMode.DEFAULT || !set.contains(fieldKey((FieldInsnNode)abstractInsnNode3))) {
                    continue;
                }
                abstractInsnNode2 = abstractInsnNode3;
            }
        }
        return abstractInsnNode2;
    }
    
    class Range
    {
        final /* synthetic */ int start;
        final /* synthetic */ int marker;
        final /* synthetic */ int end;
        
        @Override
        public String toString() {
            return String.format("Range[%d-%d,%d,valid=%s)", this.start, this.end, this.marker, this.isValid());
        }
        
        boolean isValid() {
            return this.start != 0 && this.end != 0 && this.end >= this.start;
        }
        
        boolean excludes(final int n) {
            return n < this.start || n > this.end;
        }
        
        Range(final int start, final int end, final int marker) {
            this.start = start;
            this.end = end;
            this.marker = marker;
        }
        
        boolean contains(final int n) {
            return n >= this.start && n <= this.end;
        }
    }
    
    enum ApplicatorPass
    {
        PREINJECT, 
        MAIN, 
        INJECT;
    }
    
    enum InitialiserInjectionMode
    {
        DEFAULT, 
        SAFE;
    }
}
