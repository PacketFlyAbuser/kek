// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke;

import java.lang.annotation.Annotation;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.mixin.Final;
import java.util.HashMap;
import com.google.common.primitives.Ints;
import com.google.common.collect.ObjectArrays;
import java.util.Iterator;
import org.spongepowered.asm.mixin.injection.InjectionPoint;
import java.util.Set;
import java.util.List;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import com.google.common.base.Joiner;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.points.BeforeNew;
import java.util.Map;

public class RedirectInjector extends InvokeInjector
{
    protected /* synthetic */ Meta meta;
    private /* synthetic */ Map<BeforeNew, ConstructorRedirectData> ctorRedirectors;
    
    private static Type[] getArrayArgs(final Type type, final int n, final Type... array) {
        final int n2 = type.getDimensions() + n;
        final Type[] array2 = new Type[n2 + array.length];
        for (int i = 0; i < array2.length; ++i) {
            array2[i] = ((i == 0) ? type : ((i < n2) ? Type.INT_TYPE : array[n2 - i]));
        }
        return array2;
    }
    
    protected boolean checkDescriptor(final String s, final Target target, final String str) {
        if (this.methodNode.desc.equals(s)) {
            return false;
        }
        final int index = s.indexOf(41);
        if (this.methodNode.desc.equals(String.format("%s%s%s", s.substring(0, index), Joiner.on("").join((Object[])target.arguments), s.substring(index)))) {
            return true;
        }
        throw new InvalidInjectionException(this.info, this.annotationType + " method " + str + " " + this + " has an invalid signature. Expected " + s + " but found " + this.methodNode.desc);
    }
    
    private AbstractInsnNode injectAtPutField(final InsnList list, final Target target, final FieldInsnNode fieldInsnNode, final boolean b, final Type type, final Type type2) {
        final boolean checkDescriptor = this.checkDescriptor(b ? Bytecode.generateDescriptor(null, type2) : Bytecode.generateDescriptor(null, type, type2), target, "setter");
        if (!this.isStatic) {
            if (b) {
                list.add(new VarInsnNode(25, 0));
                list.add(new InsnNode(95));
            }
            else {
                final int allocateLocals = target.allocateLocals(type2.getSize());
                list.add(new VarInsnNode(type2.getOpcode(54), allocateLocals));
                list.add(new VarInsnNode(25, 0));
                list.add(new InsnNode(95));
                list.add(new VarInsnNode(type2.getOpcode(21), allocateLocals));
            }
        }
        if (checkDescriptor) {
            this.pushArgs(target.arguments, list, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        target.addToStack((!this.isStatic && !b) ? 1 : 0);
        return this.invokeHandler(list);
    }
    
    @Override
    protected void checkTarget(final Target target) {
    }
    
    private void injectAtArrayField(final Target target, final FieldInsnNode fieldInsnNode, final int n, final Type type, final Type type2, final int n2, int opcode) {
        final Type elementType = type2.getElementType();
        if (n != 178 && n != 180) {
            throw new InvalidInjectionException(this.info, "Unspported opcode " + Bytecode.getOpcodeName(n) + " for array access " + this.info);
        }
        if (this.returnType.getSort() != 0) {
            if (opcode != 190) {
                opcode = elementType.getOpcode(46);
            }
            this.injectAtGetArray(target, fieldInsnNode, BeforeFieldAccess.findArrayNode(target.insns, fieldInsnNode, opcode, n2), type, type2);
        }
        else {
            this.injectAtSetArray(target, fieldInsnNode, BeforeFieldAccess.findArrayNode(target.insns, fieldInsnNode, elementType.getOpcode(79), n2), type, type2);
        }
    }
    
    protected void injectAtConstructor(final Target obj, final InjectionNodes.InjectionNode injectionNode) {
        final ConstructorRedirectData constructorRedirectData = injectionNode.getDecoration("ctor");
        final boolean booleanValue = injectionNode.getDecoration("wildcard");
        final TypeInsnNode typeInsnNode = (TypeInsnNode)injectionNode.getCurrentTarget();
        final AbstractInsnNode value = obj.get(obj.indexOf(typeInsnNode) + 1);
        final MethodInsnNode initNode = obj.findInitNodeFor(typeInsnNode);
        if (initNode != null) {
            final boolean b = value.getOpcode() == 89;
            final String replace = initNode.desc.replace(")V", ")L" + typeInsnNode.desc + ";");
            boolean checkDescriptor;
            try {
                checkDescriptor = this.checkDescriptor(replace, obj, "constructor");
            }
            catch (InvalidInjectionException ex) {
                if (!booleanValue) {
                    throw ex;
                }
                return;
            }
            if (b) {
                obj.removeNode(value);
            }
            if (this.isStatic) {
                obj.removeNode(typeInsnNode);
            }
            else {
                obj.replaceNode(typeInsnNode, new VarInsnNode(25, 0));
            }
            final InsnList list = new InsnList();
            if (checkDescriptor) {
                this.pushArgs(obj.arguments, list, obj.getArgIndices(), 0, obj.arguments.length);
                obj.addToStack(Bytecode.getArgsSize(obj.arguments));
            }
            this.invokeHandler(list);
            if (b) {
                final LabelNode labelNode = new LabelNode();
                list.add(new InsnNode(89));
                list.add(new JumpInsnNode(199, labelNode));
                this.throwException(list, "java/lang/NullPointerException", this.annotationType + " constructor handler " + this + " returned null for " + typeInsnNode.desc.replace('/', '.'));
                list.add(labelNode);
                obj.addToStack(1);
            }
            else {
                list.add(new InsnNode(87));
            }
            obj.replaceNode(initNode, list);
            final ConstructorRedirectData constructorRedirectData2 = constructorRedirectData;
            ++constructorRedirectData2.injected;
            return;
        }
        if (!booleanValue) {
            throw new InvalidInjectionException(this.info, this.annotationType + " ctor invocation was not found in " + obj);
        }
    }
    
    public void injectArrayRedirect(final Target obj, final FieldInsnNode fieldInsnNode, final AbstractInsnNode abstractInsnNode, final boolean b, final String str) {
        if (abstractInsnNode == null) {
            throw new InvalidInjectionException(this.info, "Array element " + this.annotationType + " on " + this + " could not locate a matching " + str + " instruction in " + obj + ". " + "");
        }
        if (!this.isStatic) {
            obj.insns.insertBefore(fieldInsnNode, new VarInsnNode(25, 0));
            obj.addToStack(1);
        }
        final InsnList list = new InsnList();
        if (b) {
            this.pushArgs(obj.arguments, list, obj.getArgIndices(), 0, obj.arguments.length);
            obj.addToStack(Bytecode.getArgsSize(obj.arguments));
        }
        obj.replaceNode(abstractInsnNode, this.invokeHandler(list), list);
    }
    
    private static String getGetArrayHandlerDescriptor(final AbstractInsnNode abstractInsnNode, final Type type, final Type type2) {
        if (abstractInsnNode != null && abstractInsnNode.getOpcode() == 190) {
            return Bytecode.generateDescriptor(Type.INT_TYPE, (Object[])getArrayArgs(type2, 0, new Type[0]));
        }
        return Bytecode.generateDescriptor(type, (Object[])getArrayArgs(type2, 1, new Type[0]));
    }
    
    protected boolean preInject(final InjectionNodes.InjectionNode injectionNode) {
        final Meta meta = injectionNode.getDecoration("redirector");
        if (meta.getOwner() != this) {
            Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, meta.name, meta.priority });
            return false;
        }
        return true;
    }
    
    private void injectAtGetArray(final Target target, final FieldInsnNode fieldInsnNode, final AbstractInsnNode abstractInsnNode, final Type type, final Type type2) {
        this.injectArrayRedirect(target, fieldInsnNode, abstractInsnNode, this.checkDescriptor(getGetArrayHandlerDescriptor(abstractInsnNode, this.returnType, type2), target, "array getter"), "array getter");
    }
    
    private void injectAtSetArray(final Target target, final FieldInsnNode fieldInsnNode, final AbstractInsnNode abstractInsnNode, final Type type, final Type type2) {
        this.injectArrayRedirect(target, fieldInsnNode, abstractInsnNode, this.checkDescriptor(Bytecode.generateDescriptor(null, (Object[])getArrayArgs(type2, 1, type2.getElementType())), target, "array setter"), "array setter");
    }
    
    public void injectAtScalarField(final Target target, final FieldInsnNode fieldInsnNode, final int n, final Type type, final Type type2) {
        final InsnList list = new InsnList();
        AbstractInsnNode abstractInsnNode;
        if (n == 178 || n == 180) {
            abstractInsnNode = this.injectAtGetField(list, target, fieldInsnNode, n == 178, type, type2);
        }
        else {
            if (n != 179 && n != 181) {
                throw new InvalidInjectionException(this.info, "Unspported opcode " + Bytecode.getOpcodeName(n) + " for " + this.info);
            }
            abstractInsnNode = this.injectAtPutField(list, target, fieldInsnNode, n == 179, type, type2);
        }
        target.replaceNode(fieldInsnNode, abstractInsnNode, list);
    }
    
    public RedirectInjector(final InjectionInfo injectionInfo) {
        this(injectionInfo, "@Redirect");
    }
    
    @Override
    protected void postInject(final Target obj, final InjectionNodes.InjectionNode injectionNode) {
        super.postInject(obj, injectionNode);
        if (injectionNode.getOriginalTarget() instanceof TypeInsnNode && injectionNode.getOriginalTarget().getOpcode() == 187) {
            final ConstructorRedirectData constructorRedirectData = injectionNode.getDecoration("ctor");
            if (injectionNode.getDecoration("wildcard") && constructorRedirectData.injected == 0) {
                throw new InvalidInjectionException(this.info, this.annotationType + " ctor invocation was not found in " + obj);
            }
        }
    }
    
    static {
        KEY_NOMINATORS = "nominators";
        KEY_OPCODE = "opcode";
        KEY_WILD = "wildcard";
        KEY_FUZZ = "fuzz";
    }
    
    @Override
    protected void inject(final Target obj, final InjectionNodes.InjectionNode injectionNode) {
        if (!this.preInject(injectionNode)) {
            return;
        }
        if (injectionNode.isReplaced()) {
            throw new UnsupportedOperationException("Redirector target failure for " + this.info);
        }
        if (injectionNode.getCurrentTarget() instanceof MethodInsnNode) {
            this.checkTargetForNode(obj, injectionNode);
            this.injectAtInvoke(obj, injectionNode);
            return;
        }
        if (injectionNode.getCurrentTarget() instanceof FieldInsnNode) {
            this.checkTargetForNode(obj, injectionNode);
            this.injectAtFieldAccess(obj, injectionNode);
            return;
        }
        if (!(injectionNode.getCurrentTarget() instanceof TypeInsnNode) || injectionNode.getCurrentTarget().getOpcode() != 187) {
            throw new InvalidInjectionException(this.info, this.annotationType + " annotation on is targetting an invalid insn in " + obj + " in " + this);
        }
        if (!this.isStatic && obj.isStatic) {
            throw new InvalidInjectionException(this.info, "non-static callback method " + this + " has a static target which is not supported");
        }
        this.injectAtConstructor(obj, injectionNode);
    }
    
    private void injectAtFieldAccess(final Target target, final InjectionNodes.InjectionNode injectionNode) {
        final FieldInsnNode fieldInsnNode = (FieldInsnNode)injectionNode.getCurrentTarget();
        final int opcode = fieldInsnNode.getOpcode();
        final Type type = Type.getType("L" + fieldInsnNode.owner + ";");
        final Type type2 = Type.getType(fieldInsnNode.desc);
        final int n = (type2.getSort() == 9) ? type2.getDimensions() : 0;
        final int n2 = (this.returnType.getSort() == 9) ? this.returnType.getDimensions() : 0;
        if (n2 > n) {
            throw new InvalidInjectionException(this.info, "Dimensionality of handler method is greater than target array on " + this);
        }
        if (n2 == 0 && n > 0) {
            this.injectAtArrayField(target, fieldInsnNode, opcode, type, type2, injectionNode.getDecoration("fuzz"), injectionNode.getDecoration("opcode"));
        }
        else {
            this.injectAtScalarField(target, fieldInsnNode, opcode, type, type2);
        }
    }
    
    private ConstructorRedirectData getCtorRedirect(final BeforeNew beforeNew) {
        ConstructorRedirectData constructorRedirectData = this.ctorRedirectors.get(beforeNew);
        if (constructorRedirectData == null) {
            constructorRedirectData = new ConstructorRedirectData();
            this.ctorRedirectors.put(beforeNew, constructorRedirectData);
        }
        return constructorRedirectData;
    }
    
    @Override
    protected void addTargetNode(final Target target, final List<InjectionNodes.InjectionNode> list, final AbstractInsnNode abstractInsnNode, final Set<InjectionPoint> set) {
        final InjectionNodes.InjectionNode injectionNode = target.getInjectionNode(abstractInsnNode);
        ConstructorRedirectData ctorRedirect = null;
        int fuzzFactor = 8;
        int arrayOpcode = 0;
        if (injectionNode != null) {
            final Meta meta = injectionNode.getDecoration("redirector");
            if (meta != null && meta.getOwner() != this) {
                if (meta.priority >= this.meta.priority) {
                    Injector.logger.warn("{} conflict. Skipping {} with priority {}, already redirected by {} with priority {}", new Object[] { this.annotationType, this.info, this.meta.priority, meta.name, meta.priority });
                    return;
                }
                if (meta.isFinal) {
                    throw new InvalidInjectionException(this.info, this.annotationType + " conflict: " + this + " failed because target was already remapped by " + meta.name);
                }
            }
        }
        for (final InjectionPoint injectionPoint : set) {
            if (injectionPoint instanceof BeforeNew && !((BeforeNew)injectionPoint).hasDescriptor()) {
                ctorRedirect = this.getCtorRedirect((BeforeNew)injectionPoint);
            }
            else {
                if (!(injectionPoint instanceof BeforeFieldAccess)) {
                    continue;
                }
                final BeforeFieldAccess beforeFieldAccess = (BeforeFieldAccess)injectionPoint;
                fuzzFactor = beforeFieldAccess.getFuzzFactor();
                arrayOpcode = beforeFieldAccess.getArrayOpcode();
            }
        }
        final InjectionNodes.InjectionNode addInjectionNode = target.addInjectionNode(abstractInsnNode);
        addInjectionNode.decorate("redirector", this.meta);
        addInjectionNode.decorate("nominators", set);
        if (abstractInsnNode instanceof TypeInsnNode && abstractInsnNode.getOpcode() == 187) {
            addInjectionNode.decorate("wildcard", ctorRedirect != null);
            addInjectionNode.decorate("ctor", ctorRedirect);
        }
        else {
            addInjectionNode.decorate("fuzz", fuzzFactor);
            addInjectionNode.decorate("opcode", arrayOpcode);
        }
        list.add(addInjectionNode);
    }
    
    @Override
    protected void injectAtInvoke(final Target target, final InjectionNodes.InjectionNode injectionNode) {
        final MethodInsnNode methodInsnNode = (MethodInsnNode)injectionNode.getCurrentTarget();
        final boolean b = methodInsnNode.getOpcode() == 184;
        final Type type = Type.getType("L" + methodInsnNode.owner + ";");
        final Type returnType = Type.getReturnType(methodInsnNode.desc);
        final Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
        final Type[] array = (Type[])(b ? argumentTypes : ObjectArrays.concat((Object)type, (Object[])argumentTypes));
        boolean b2 = false;
        final String descriptor = Bytecode.getDescriptor(array, returnType);
        if (!descriptor.equals(this.methodNode.desc)) {
            if (!Bytecode.getDescriptor((Type[])ObjectArrays.concat((Object[])array, (Object[])target.arguments, (Class)Type.class), returnType).equals(this.methodNode.desc)) {
                throw new InvalidInjectionException(this.info, this.annotationType + " handler method " + this + " has an invalid signature, expected " + descriptor + " found " + this.methodNode.desc);
            }
            b2 = true;
        }
        final InsnList list = new InsnList();
        int n = Bytecode.getArgsSize(array) + 1;
        int n2 = 1;
        int[] array2 = this.storeArgs(target, array, list, 0);
        if (b2) {
            final int argsSize = Bytecode.getArgsSize(target.arguments);
            n += argsSize;
            n2 += argsSize;
            array2 = Ints.concat(new int[][] { array2, target.getArgIndices() });
        }
        target.replaceNode(methodInsnNode, this.invokeHandlerWithArgs(this.methodArgs, list, array2), list);
        target.addToLocals(n);
        target.addToStack(n2);
    }
    
    private AbstractInsnNode injectAtGetField(final InsnList list, final Target target, final FieldInsnNode fieldInsnNode, final boolean b, final Type type, final Type type2) {
        final boolean checkDescriptor = this.checkDescriptor(b ? Bytecode.generateDescriptor(type2, new Object[0]) : Bytecode.generateDescriptor(type2, type), target, "getter");
        if (!this.isStatic) {
            list.add(new VarInsnNode(25, 0));
            if (!b) {
                list.add(new InsnNode(95));
            }
        }
        if (checkDescriptor) {
            this.pushArgs(target.arguments, list, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        target.addToStack(this.isStatic ? 0 : 1);
        return this.invokeHandler(list);
    }
    
    protected RedirectInjector(final InjectionInfo injectionInfo, final String s) {
        super(injectionInfo, s);
        this.ctorRedirectors = new HashMap<BeforeNew, ConstructorRedirectData>();
        this.meta = new Meta(injectionInfo.getContext().getPriority(), Annotations.getVisible(this.methodNode, Final.class) != null, this.info.toString(), this.methodNode.desc);
    }
    
    class ConstructorRedirectData
    {
        public /* synthetic */ int injected;
        
        ConstructorRedirectData() {
            this.injected = 0;
        }
        
        static {
            KEY = "ctor";
        }
    }
    
    class Meta
    {
        final /* synthetic */ String desc;
        final /* synthetic */ boolean isFinal;
        final /* synthetic */ int priority;
        final /* synthetic */ String name;
        
        RedirectInjector getOwner() {
            return RedirectInjector.this;
        }
        
        static {
            KEY = "redirector";
        }
        
        public Meta(final int priority, final boolean isFinal, final String name, final String desc) {
            this.priority = priority;
            this.isFinal = isFinal;
            this.name = name;
            this.desc = desc;
        }
    }
}
