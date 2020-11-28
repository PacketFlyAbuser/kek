// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.HashSet;
import java.util.Collections;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.LdcInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import java.util.Set;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.TreeMap;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.List;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.Type;

public abstract class Injector
{
    protected final /* synthetic */ Type[] methodArgs;
    protected final /* synthetic */ Type returnType;
    protected static final /* synthetic */ Logger logger;
    protected /* synthetic */ InjectionInfo info;
    protected final /* synthetic */ boolean isStatic;
    protected final /* synthetic */ MethodNode methodNode;
    protected final /* synthetic */ ClassNode classNode;
    
    public static boolean canCoerce(final Type type, final Type type2) {
        if (type.getSort() == 10 && type2.getSort() == 10) {
            return canCoerce(ClassInfo.forType(type), ClassInfo.forType(type2));
        }
        return canCoerce(type.getDescriptor(), type2.getDescriptor());
    }
    
    public final List<InjectionNodes.InjectionNode> find(final InjectorTarget injectorTarget, final List<InjectionPoint> list) {
        this.sanityCheck(injectorTarget.getTarget(), list);
        final ArrayList<InjectionNodes.InjectionNode> list2 = new ArrayList<InjectionNodes.InjectionNode>();
        for (final TargetNode targetNode : this.findTargetNodes(injectorTarget, list)) {
            this.addTargetNode(injectorTarget.getTarget(), list2, targetNode.insn, targetNode.nominators);
        }
        return list2;
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    private Collection<TargetNode> findTargetNodes(final InjectorTarget injectorTarget, final List<InjectionPoint> list) {
        final MethodNode method = injectorTarget.getMethod();
        final TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
        final ArrayList<AbstractInsnNode> list2 = new ArrayList<AbstractInsnNode>(32);
        for (final InjectionPoint injectionPoint : list) {
            list2.clear();
            if (this.findTargetNodes(method, injectionPoint, injectorTarget.getSlice(injectionPoint), list2)) {
                for (final AbstractInsnNode abstractInsnNode : list2) {
                    final Integer value = method.instructions.indexOf(abstractInsnNode);
                    TargetNode targetNode = treeMap.get(value);
                    if (targetNode == null) {
                        targetNode = new TargetNode(abstractInsnNode);
                        treeMap.put(value, targetNode);
                    }
                    targetNode.nominators.add(injectionPoint);
                }
            }
        }
        return treeMap.values();
    }
    
    protected abstract void inject(final Target p0, final InjectionNodes.InjectionNode p1);
    
    public static boolean canCoerce(final String s, final String s2) {
        return s.length() <= 1 && s2.length() <= 1 && canCoerce(s.charAt(0), s2.charAt(0));
    }
    
    protected void addTargetNode(final Target target, final List<InjectionNodes.InjectionNode> list, final AbstractInsnNode abstractInsnNode, final Set<InjectionPoint> set) {
        list.add(target.addInjectionNode(abstractInsnNode));
    }
    
    protected void sanityCheck(final Target target, final List<InjectionPoint> list) {
        if (target.classNode != this.classNode) {
            throw new InvalidInjectionException(this.info, "Target class does not match injector class in " + this);
        }
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList list) {
        return this.invokeHandler(list, this.methodNode);
    }
    
    public static boolean canCoerce(final char ch, final char c) {
        return c == 'I' && "IBSCZ".indexOf(ch) > -1;
    }
    
    private static boolean canCoerce(final ClassInfo classInfo, final ClassInfo classInfo2) {
        return classInfo != null && classInfo2 != null && (classInfo2 == classInfo || classInfo2.hasSuperClass(classInfo));
    }
    
    protected boolean findTargetNodes(final MethodNode methodNode, final InjectionPoint injectionPoint, final InsnList list, final Collection<AbstractInsnNode> collection) {
        return injectionPoint.find(methodNode.desc, list, collection);
    }
    
    public final void inject(final Target target, final List<InjectionNodes.InjectionNode> list) {
        for (final InjectionNodes.InjectionNode injectionNode : list) {
            if (injectionNode.isRemoved()) {
                if (!this.info.getContext().getOption(MixinEnvironment.Option.DEBUG_VERBOSE)) {
                    continue;
                }
                Injector.logger.warn("Target node for {} was removed by a previous injector in {}", new Object[] { this.info, target });
            }
            else {
                this.inject(target, injectionNode);
            }
        }
        final Iterator<InjectionNodes.InjectionNode> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            this.postInject(target, iterator2.next());
        }
    }
    
    protected void postInject(final Target target, final InjectionNodes.InjectionNode injectionNode) {
    }
    
    public Injector(final InjectionInfo info) {
        this(info.getClassNode(), info.getMethod());
        this.info = info;
    }
    
    protected void throwException(final InsnList list, final String s, final String s2) {
        list.add(new TypeInsnNode(187, s));
        list.add(new InsnNode(89));
        list.add(new LdcInsnNode(s2));
        list.add(new MethodInsnNode(183, s, "<init>", "(Ljava/lang/String;)V", false));
        list.add(new InsnNode(191));
    }
    
    private Injector(final ClassNode classNode, final MethodNode methodNode) {
        this.classNode = classNode;
        this.methodNode = methodNode;
        this.methodArgs = Type.getArgumentTypes(this.methodNode.desc);
        this.returnType = Type.getReturnType(this.methodNode.desc);
        this.isStatic = Bytecode.methodIsStatic(this.methodNode);
    }
    
    @Override
    public String toString() {
        return String.format("%s::%s", this.classNode.name, this.methodNode.name);
    }
    
    protected AbstractInsnNode invokeHandler(final InsnList list, final MethodNode methodNode) {
        final boolean b = (methodNode.access & 0x2) != 0x0;
        final MethodInsnNode methodInsnNode = new MethodInsnNode(this.isStatic ? 184 : (b ? 183 : 182), this.classNode.name, methodNode.name, methodNode.desc, false);
        list.add(methodInsnNode);
        this.info.addCallbackInvocation(methodNode);
        return methodInsnNode;
    }
    
    public static final class TargetNode
    {
        final /* synthetic */ Set<InjectionPoint> nominators;
        final /* synthetic */ AbstractInsnNode insn;
        
        @Override
        public int hashCode() {
            return this.insn.hashCode();
        }
        
        public Set<InjectionPoint> getNominators() {
            return Collections.unmodifiableSet((Set<? extends InjectionPoint>)this.nominators);
        }
        
        TargetNode(final AbstractInsnNode insn) {
            this.nominators = new HashSet<InjectionPoint>();
            this.insn = insn;
        }
        
        public AbstractInsnNode getNode() {
            return this.insn;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o != null && o.getClass() == TargetNode.class && ((TargetNode)o).insn == this.insn;
        }
    }
}
