// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection;

import java.util.LinkedHashSet;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import com.google.common.base.Joiner;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.points.BeforeFinalReturn;
import org.spongepowered.asm.mixin.injection.modify.AfterStoreLocal;
import org.spongepowered.asm.mixin.injection.modify.BeforeLoadLocal;
import org.spongepowered.asm.mixin.injection.points.AfterInvoke;
import org.spongepowered.asm.mixin.injection.points.MethodHead;
import org.spongepowered.asm.mixin.injection.points.JumpInsnPoint;
import org.spongepowered.asm.mixin.injection.points.BeforeStringInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import org.spongepowered.asm.mixin.injection.points.BeforeInvoke;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import java.lang.reflect.Constructor;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.apache.logging.log4j.LogManager;
import java.util.Map;

public abstract class InjectionPoint
{
    private final /* synthetic */ Selector selector;
    private static /* synthetic */ Map<String, Class<? extends InjectionPoint>> types;
    private final /* synthetic */ String slice;
    private final /* synthetic */ String id;
    
    public static void register(final Class<? extends InjectionPoint> clazz) {
        final AtCode atCode = clazz.getAnnotation(AtCode.class);
        if (atCode == null) {
            throw new IllegalArgumentException("Injection point class " + clazz + " is not annotated with @AtCode");
        }
        final Class<? extends InjectionPoint> clazz2 = InjectionPoint.types.get(atCode.value());
        if (clazz2 != null && !clazz2.equals(clazz)) {
            LogManager.getLogger("mixin").debug("Overriding InjectionPoint {} with {} (previously {})", new Object[] { atCode.value(), clazz.getName(), clazz2.getName() });
        }
        InjectionPoint.types.put(atCode.value(), clazz);
    }
    
    private static Class<? extends InjectionPoint> findClass(final IMixinContext mixinContext, final InjectionPointData injectionPointData) {
        final String type = injectionPointData.getType();
        Class<?> forName = InjectionPoint.types.get(type);
        if (forName == null) {
            if (type.matches("^([A-Za-z_][A-Za-z0-9_]*\\.)+[A-Za-z_][A-Za-z0-9_]*$")) {
                try {
                    forName = Class.forName(type);
                    InjectionPoint.types.put(type, (Class<? extends InjectionPoint>)forName);
                    return (Class<? extends InjectionPoint>)forName;
                }
                catch (Exception ex) {
                    throw new InvalidInjectionException(mixinContext, injectionPointData + " could not be loaded or is not a valid InjectionPoint", ex);
                }
            }
            throw new InvalidInjectionException(mixinContext, injectionPointData + " is not a valid injection point specifier");
        }
        return (Class<? extends InjectionPoint>)forName;
    }
    
    public abstract boolean find(final String p0, final InsnList p1, final Collection<AbstractInsnNode> p2);
    
    public String getSlice() {
        return this.slice;
    }
    
    private static InjectionPoint create(final IMixinContext mixinContext, final InjectionPointData injectionPointData, final Class<? extends InjectionPoint> clazz) {
        Constructor<? extends InjectionPoint> declaredConstructor;
        try {
            declaredConstructor = clazz.getDeclaredConstructor(InjectionPointData.class);
            declaredConstructor.setAccessible(true);
        }
        catch (NoSuchMethodException ex) {
            throw new InvalidInjectionException(mixinContext, clazz.getName() + " must contain a constructor which accepts an InjectionPointData", ex);
        }
        InjectionPoint injectionPoint;
        try {
            injectionPoint = (InjectionPoint)declaredConstructor.newInstance(injectionPointData);
        }
        catch (Exception ex2) {
            throw new InvalidInjectionException(mixinContext, "Error whilst instancing injection point " + clazz.getName() + " for " + injectionPointData.getAt(), ex2);
        }
        return injectionPoint;
    }
    
    protected InjectionPoint(final InjectionPointData injectionPointData) {
        this(injectionPointData.getSlice(), injectionPointData.getSelector(), injectionPointData.getId());
    }
    
    private static void validateByValue(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final InjectionPoint injectionPoint, final int i) {
        final ShiftByViolationBehaviour shiftByViolationBehaviour = mixinContext.getMixin().getConfig().getEnvironment().getOption(MixinEnvironment.Option.SHIFT_BY_VIOLATION_BEHAVIOUR, ShiftByViolationBehaviour.WARN);
        if (shiftByViolationBehaviour == ShiftByViolationBehaviour.IGNORE) {
            return;
        }
        int maxShiftByValue = 0;
        if (mixinContext instanceof MixinTargetContext) {
            maxShiftByValue = ((MixinTargetContext)mixinContext).getMaxShiftByValue();
        }
        if (i <= maxShiftByValue) {
            return;
        }
        final String format = String.format("@%s(%s) Shift.BY=%d on %s::%s exceeds the maximum allowed value %d.", Bytecode.getSimpleName(annotationNode), injectionPoint, i, mixinContext, methodNode.name, maxShiftByValue);
        if (shiftByViolationBehaviour == ShiftByViolationBehaviour.WARN) {
            LogManager.getLogger("mixin").warn("{} Increase the value of maxShiftBy to suppress this warning.", new Object[] { format });
            return;
        }
        throw new InvalidInjectionException(mixinContext, format);
    }
    
    public static InjectionPoint and(final InjectionPoint... array) {
        return new Intersection(array);
    }
    
    private static InjectionPoint shift(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final InjectionPoint injectionPoint, final At.Shift shift, final int n) {
        if (injectionPoint != null) {
            if (shift == At.Shift.BEFORE) {
                return before(injectionPoint);
            }
            if (shift == At.Shift.AFTER) {
                return after(injectionPoint);
            }
            if (shift == At.Shift.BY) {
                validateByValue(mixinContext, methodNode, annotationNode, injectionPoint, n);
                return shift(injectionPoint, n);
            }
        }
        return injectionPoint;
    }
    
    public static InjectionPoint parse(final IInjectionPointContext injectionPointContext, final AnnotationNode annotationNode) {
        return parse(injectionPointContext.getContext(), injectionPointContext.getMethod(), injectionPointContext.getAnnotation(), annotationNode);
    }
    
    public String getId() {
        return this.id;
    }
    
    protected static AbstractInsnNode nextNode(final InsnList list, final AbstractInsnNode abstractInsnNode) {
        final int n = list.indexOf(abstractInsnNode) + 1;
        if (n > 0 && n < list.size()) {
            return list.get(n);
        }
        return abstractInsnNode;
    }
    
    public Selector getSelector() {
        return this.selector;
    }
    
    protected String getAtCode() {
        final AtCode atCode = this.getClass().getAnnotation(AtCode.class);
        return (atCode == null) ? this.getClass().getName() : atCode.value();
    }
    
    @Override
    public String toString() {
        return String.format("@At(\"%s\")", this.getAtCode());
    }
    
    protected InjectionPoint() {
        this("", Selector.DEFAULT, null);
    }
    
    public static List<InjectionPoint> parse(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final List<AnnotationNode> list) {
        final ImmutableList.Builder builder = ImmutableList.builder();
        final Iterator<AnnotationNode> iterator = list.iterator();
        while (iterator.hasNext()) {
            final InjectionPoint parse = parse(mixinContext, methodNode, annotationNode, iterator.next());
            if (parse != null) {
                builder.add((Object)parse);
            }
        }
        return (List<InjectionPoint>)builder.build();
    }
    
    public static InjectionPoint before(final InjectionPoint injectionPoint) {
        return new Shift(injectionPoint, -1);
    }
    
    public static InjectionPoint after(final InjectionPoint injectionPoint) {
        return new Shift(injectionPoint, 1);
    }
    
    public static InjectionPoint shift(final InjectionPoint injectionPoint, final int n) {
        return new Shift(injectionPoint, n);
    }
    
    public InjectionPoint(final String slice, final Selector selector, final String id) {
        this.slice = slice;
        this.selector = selector;
        this.id = id;
    }
    
    public static InjectionPoint parse(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final String s, final At.Shift shift, final int n, final List<String> list, final String s2, final String s3, final int n2, final int n3, final String s4) {
        final InjectionPointData injectionPointData = new InjectionPointData(mixinContext, methodNode, annotationNode, s, list, s2, s3, n2, n3, s4);
        return shift(mixinContext, methodNode, annotationNode, create(mixinContext, injectionPointData, findClass(mixinContext, injectionPointData)), shift, n);
    }
    
    public static InjectionPoint parse(final IInjectionPointContext injectionPointContext, final At at) {
        return parse(injectionPointContext.getContext(), injectionPointContext.getMethod(), injectionPointContext.getAnnotation(), at.value(), at.shift(), at.by(), Arrays.asList(at.args()), at.target(), at.slice(), at.ordinal(), at.opcode(), at.id());
    }
    
    public static InjectionPoint parse(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final At at) {
        return parse(mixinContext, methodNode, annotationNode, at.value(), at.shift(), at.by(), Arrays.asList(at.args()), at.target(), at.slice(), at.ordinal(), at.opcode(), at.id());
    }
    
    static {
        DEFAULT_ALLOWED_SHIFT_BY = 0;
        MAX_ALLOWED_SHIFT_BY = 0;
        InjectionPoint.types = new HashMap<String, Class<? extends InjectionPoint>>();
        register(BeforeFieldAccess.class);
        register(BeforeInvoke.class);
        register(BeforeNew.class);
        register(BeforeReturn.class);
        register(BeforeStringInvoke.class);
        register(JumpInsnPoint.class);
        register(MethodHead.class);
        register(AfterInvoke.class);
        register(BeforeLoadLocal.class);
        register(AfterStoreLocal.class);
        register(BeforeFinalReturn.class);
        register(BeforeConstant.class);
    }
    
    public static InjectionPoint parse(final IMixinContext mixinContext, final MethodNode methodNode, final AnnotationNode annotationNode, final AnnotationNode annotationNode2) {
        final String s = Annotations.getValue(annotationNode2, "value");
        Object of = Annotations.getValue(annotationNode2, "args");
        final String s2 = Annotations.getValue(annotationNode2, "target", "");
        final String s3 = Annotations.getValue(annotationNode2, "slice", "");
        final At.Shift shift = Annotations.getValue(annotationNode2, "shift", At.Shift.class, At.Shift.NONE);
        final int intValue = Annotations.getValue(annotationNode2, "by", 0);
        final int intValue2 = Annotations.getValue(annotationNode2, "ordinal", -1);
        final int intValue3 = Annotations.getValue(annotationNode2, "opcode", 0);
        final String s4 = Annotations.getValue(annotationNode2, "id");
        if (of == null) {
            of = ImmutableList.of();
        }
        return parse(mixinContext, methodNode, annotationNode, s, shift, intValue, (List<String>)of, s2, s3, intValue2, intValue3, s4);
    }
    
    public static InjectionPoint or(final InjectionPoint... array) {
        return new Union(array);
    }
    
    public static List<InjectionPoint> parse(final IInjectionPointContext injectionPointContext, final List<AnnotationNode> list) {
        return parse(injectionPointContext.getContext(), injectionPointContext.getMethod(), injectionPointContext.getAnnotation(), list);
    }
    
    abstract static class CompositeInjectionPoint extends InjectionPoint
    {
        protected final /* synthetic */ InjectionPoint[] components;
        
        @Override
        public String toString() {
            return "CompositeInjectionPoint(" + this.getClass().getSimpleName() + ")[" + Joiner.on(',').join((Object[])this.components) + "]";
        }
        
        protected CompositeInjectionPoint(final InjectionPoint... components) {
            if (components == null || components.length < 2) {
                throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
            }
            this.components = components;
        }
    }
    
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AtCode {
        String value();
    }
    
    static final class Shift extends InjectionPoint
    {
        private final /* synthetic */ InjectionPoint input;
        private final /* synthetic */ int shift;
        
        @Override
        public String toString() {
            return "InjectionPoint(" + this.getClass().getSimpleName() + ")[" + this.input + "]";
        }
        
        public Shift(final int shift) {
            if (InjectionPoint.this == null) {
                throw new IllegalArgumentException("Must supply an input injection point for SHIFT");
            }
            this.shift = shift;
        }
        
        @Override
        public boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> c) {
            final List<AbstractInsnNode> list2 = (c instanceof List) ? c : new ArrayList<AbstractInsnNode>(c);
            this.input.find(s, list, c);
            for (int i = 0; i < list2.size(); ++i) {
                list2.set(i, list.get(list.indexOf(list2.get(i)) + this.shift));
            }
            if (c != list2) {
                c.clear();
                c.addAll((Collection<?>)list2);
            }
            return c.size() > 0;
        }
    }
    
    static final class Intersection extends CompositeInjectionPoint
    {
        public Intersection(final InjectionPoint... array) {
            super(array);
        }
        
        @Override
        public boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> collection) {
            boolean b = false;
            final ArrayList[] array = (ArrayList[])Array.newInstance(ArrayList.class, this.components.length);
            for (int i = 0; i < this.components.length; ++i) {
                array[i] = new ArrayList();
                this.components[i].find(s, list, array[i]);
            }
            final ArrayList list2 = array[0];
            for (int j = 0; j < list2.size(); ++j) {
                final AbstractInsnNode o = list2.get(j);
                final boolean b2 = true;
                for (int n = 1; n < array.length && array[n].contains(o); ++n) {}
                if (b2) {
                    collection.add(o);
                    b = true;
                }
            }
            return b;
        }
    }
    
    enum ShiftByViolationBehaviour
    {
        ERROR, 
        WARN, 
        IGNORE;
    }
    
    public enum Selector
    {
        ONE, 
        LAST, 
        FIRST;
        
        public static final /* synthetic */ Selector DEFAULT;
        
        static {
            DEFAULT = Selector.FIRST;
        }
    }
    
    static final class Union extends CompositeInjectionPoint
    {
        public Union(final InjectionPoint... array) {
            super(array);
        }
        
        @Override
        public boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> collection) {
            final LinkedHashSet<AbstractInsnNode> set = new LinkedHashSet<AbstractInsnNode>();
            for (int i = 0; i < this.components.length; ++i) {
                this.components[i].find(s, list, set);
            }
            collection.addAll(set);
            return set.size() > 0;
        }
    }
}
