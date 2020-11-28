// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.gen;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.spongepowered.asm.util.Bytecode;
import java.util.regex.Matcher;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import java.util.List;
import com.google.common.base.Strings;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.gen.throwables.InvalidAccessorException;
import java.lang.annotation.Annotation;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.mixin.refmap.IMixinContext;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.FieldNode;
import java.util.regex.Pattern;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.struct.SpecialMethodInfo;

public class AccessorInfo extends SpecialMethodInfo
{
    protected final /* synthetic */ Type returnType;
    protected static final /* synthetic */ Pattern PATTERN_ACCESSOR;
    protected /* synthetic */ FieldNode targetField;
    protected final /* synthetic */ Type[] argTypes;
    protected final /* synthetic */ AccessorType type;
    private final /* synthetic */ Type targetFieldType;
    protected /* synthetic */ MethodNode targetMethod;
    protected final /* synthetic */ MemberInfo target;
    
    protected String inflectTarget() {
        return inflectTarget(this.method.name, this.type, this.toString(), this.mixin, this.mixin.getEnvironment().getOption(MixinEnvironment.Option.DEBUG_VERBOSE));
    }
    
    public AccessorInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode) {
        this(mixinTargetContext, methodNode, Accessor.class);
    }
    
    public final MethodNode getTargetMethod() {
        return this.targetMethod;
    }
    
    public final Type getTargetFieldType() {
        return this.targetFieldType;
    }
    
    public static AccessorInfo of(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final Class<? extends Annotation> clazz) {
        if (clazz == Accessor.class) {
            return new AccessorInfo(mixinTargetContext, methodNode);
        }
        if (clazz == Invoker.class) {
            return new InvokerInfo(mixinTargetContext, methodNode);
        }
        throw new InvalidAccessorException(mixinTargetContext, "Could not parse accessor for unknown type " + clazz.getName());
    }
    
    public final Type[] getArgTypes() {
        return this.argTypes;
    }
    
    public void locate() {
        this.targetField = this.findTargetField();
    }
    
    protected Type initTargetFieldType() {
        switch (this.type) {
            case FIELD_GETTER: {
                if (this.argTypes.length > 0) {
                    throw new InvalidAccessorException(this.mixin, this + " must take exactly 0 arguments, found " + this.argTypes.length);
                }
                return this.returnType;
            }
            case FIELD_SETTER: {
                if (this.argTypes.length != 1) {
                    throw new InvalidAccessorException(this.mixin, this + " must take exactly 1 argument, found " + this.argTypes.length);
                }
                return this.argTypes[0];
            }
            default: {
                throw new InvalidAccessorException(this.mixin, "Computed unsupported accessor type " + this.type + " for " + this);
            }
        }
    }
    
    protected AccessorType initType() {
        if (this.returnType.equals(Type.VOID_TYPE)) {
            return AccessorType.FIELD_SETTER;
        }
        return AccessorType.FIELD_GETTER;
    }
    
    private FieldNode findTargetField() {
        return this.findTarget(this.classNode.fields);
    }
    
    protected String getTargetName() {
        final String s = Annotations.getValue(this.annotation);
        if (!Strings.isNullOrEmpty(s)) {
            return MemberInfo.parse(s, this.mixin).name;
        }
        final String inflectTarget = this.inflectTarget();
        if (inflectTarget == null) {
            throw new InvalidAccessorException(this.mixin, "Failed to inflect target name for " + this + ", supported prefixes: [get, set, is]");
        }
        return inflectTarget;
    }
    
    public final FieldNode getTargetField() {
        return this.targetField;
    }
    
    private static boolean isUpperCase(final String anObject) {
        return anObject.toUpperCase().equals(anObject);
    }
    
    protected <TNode> TNode findTarget(final List<TNode> list) {
        TNode tNode = null;
        final ArrayList<Object> list2 = (ArrayList<Object>)new ArrayList<TNode>();
        for (final TNode next : list) {
            final String nodeDesc = getNodeDesc(next);
            if (nodeDesc != null) {
                if (!nodeDesc.equals(this.target.desc)) {
                    continue;
                }
                final String nodeName = getNodeName(next);
                if (nodeName == null) {
                    continue;
                }
                if (nodeName.equals(this.target.name)) {
                    tNode = next;
                }
                if (!nodeName.equalsIgnoreCase(this.target.name)) {
                    continue;
                }
                list2.add(next);
            }
        }
        if (tNode != null) {
            if (list2.size() > 1) {
                LogManager.getLogger("mixin").debug("{} found an exact match for {} but other candidates were found!", new Object[] { this, this.target });
            }
            return tNode;
        }
        if (list2.size() == 1) {
            return (TNode)list2.get(0);
        }
        throw new InvalidAccessorException(this, ((list2.size() == 0) ? "No" : "Multiple") + " candidates were found matching " + this.target + " in " + this.classNode.name + " for " + this);
    }
    
    public static String inflectTarget(final String input, final AccessorType accessorType, final String s, final IMixinContext mixinContext, final boolean b) {
        final Matcher matcher = AccessorInfo.PATTERN_ACCESSOR.matcher(input);
        if (matcher.matches()) {
            final String group = matcher.group(1);
            final String group2 = matcher.group(3);
            final String group3 = matcher.group(4);
            final String format = String.format("%s%s", toLowerCase(group2, !isUpperCase(group3)), group3);
            if (!accessorType.isExpectedPrefix(group) && b) {
                LogManager.getLogger("mixin").warn("Unexpected prefix for {}, found [{}] expecting {}", new Object[] { s, group, accessorType.getExpectedPrefixes() });
            }
            return MemberInfo.parse(format, mixinContext).name;
        }
        return null;
    }
    
    protected MemberInfo initTarget() {
        final MemberInfo memberInfo = new MemberInfo(this.getTargetName(), null, this.targetFieldType.getDescriptor());
        this.annotation.visit("target", memberInfo.toString());
        return memberInfo;
    }
    
    public final MemberInfo getTarget() {
        return this.target;
    }
    
    private static <TNode> String getNodeName(final TNode tNode) {
        return (tNode instanceof MethodNode) ? ((MethodNode)tNode).name : ((tNode instanceof FieldNode) ? ((FieldNode)tNode).name : null);
    }
    
    protected AccessorInfo(final MixinTargetContext mixinTargetContext, final MethodNode methodNode, final Class<? extends Annotation> clazz) {
        super(mixinTargetContext, methodNode, Annotations.getVisible(methodNode, clazz));
        this.argTypes = Type.getArgumentTypes(methodNode.desc);
        this.returnType = Type.getReturnType(methodNode.desc);
        this.type = this.initType();
        this.targetFieldType = this.initTargetFieldType();
        this.target = this.initTarget();
    }
    
    private static <TNode> String getNodeDesc(final TNode tNode) {
        return (tNode instanceof MethodNode) ? ((MethodNode)tNode).desc : ((tNode instanceof FieldNode) ? ((FieldNode)tNode).desc : null);
    }
    
    public MethodNode generate() {
        final MethodNode generate = this.type.getGenerator(this).generate();
        Bytecode.mergeAnnotations(this.method, generate);
        return generate;
    }
    
    static {
        PATTERN_ACCESSOR = Pattern.compile("^(get|set|is|invoke|call)(([A-Z])(.*?))(_\\$md.*)?$");
    }
    
    public final Type getReturnType() {
        return this.returnType;
    }
    
    private static String toLowerCase(final String s, final boolean b) {
        return b ? s.toLowerCase() : s;
    }
    
    @Override
    public String toString() {
        return String.format("%s->@%s[%s]::%s%s", this.mixin.toString(), Bytecode.getSimpleName(this.annotation), this.type.toString(), this.method.name, this.method.desc);
    }
    
    public enum AccessorType
    {
        private final /* synthetic */ Set<String> expectedPrefixes;
        
        METHOD_PROXY((Set)ImmutableSet.of((Object)"call", (Object)"invoke")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo accessorInfo) {
                return new AccessorGeneratorMethodProxy(accessorInfo);
            }
        }, 
        FIELD_GETTER((Set)ImmutableSet.of((Object)"get", (Object)"is")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo accessorInfo) {
                return new AccessorGeneratorFieldGetter(accessorInfo);
            }
        }, 
        FIELD_SETTER((Set)ImmutableSet.of((Object)"set")) {
            @Override
            AccessorGenerator getGenerator(final AccessorInfo accessorInfo) {
                return new AccessorGeneratorFieldSetter(accessorInfo);
            }
        };
        
        private AccessorType(final Set<String> expectedPrefixes) {
            this.expectedPrefixes = expectedPrefixes;
        }
        
        public String getExpectedPrefixes() {
            return this.expectedPrefixes.toString();
        }
        
        abstract AccessorGenerator getGenerator(final AccessorInfo p0);
        
        public boolean isExpectedPrefix(final String s) {
            return this.expectedPrefixes.contains(s);
        }
    }
}
