// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.struct;

import java.util.Iterator;
import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.TypeInsnNode;
import org.spongepowered.asm.lib.tree.LocalVariableNode;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import java.util.ArrayList;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.lib.tree.LabelNode;
import java.util.List;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;

public class Target implements Comparable<Target>, Iterable<AbstractInsnNode>
{
    private final /* synthetic */ int maxStack;
    public final /* synthetic */ Type[] arguments;
    private final /* synthetic */ InjectionNodes injectionNodes;
    private /* synthetic */ String callbackInfoClass;
    public final /* synthetic */ boolean isStatic;
    public final /* synthetic */ Type returnType;
    public final /* synthetic */ ClassNode classNode;
    private /* synthetic */ List<Integer> argMapVars;
    private /* synthetic */ String callbackDescriptor;
    private /* synthetic */ LabelNode end;
    public final /* synthetic */ MethodNode method;
    public final /* synthetic */ boolean isCtor;
    private /* synthetic */ int[] argIndices;
    private final /* synthetic */ int maxLocals;
    private /* synthetic */ LabelNode start;
    public final /* synthetic */ InsnList insns;
    
    public int getMaxStack() {
        return this.maxStack;
    }
    
    public InjectionNodes.InjectionNode getInjectionNode(final AbstractInsnNode abstractInsnNode) {
        return this.injectionNodes.get(abstractInsnNode);
    }
    
    public void addLocalVariable(final int n, final String s, final String s2) {
        if (this.start == null) {
            this.start = new LabelNode(new Label());
            this.end = new LabelNode(new Label());
            this.insns.insert(this.start);
            this.insns.add(this.end);
        }
        this.addLocalVariable(n, s, s2, this.start, this.end);
    }
    
    @Override
    public int compareTo(final Target target) {
        if (target == null) {
            return Integer.MAX_VALUE;
        }
        return this.toString().compareTo(target.toString());
    }
    
    public Target(final ClassNode classNode, final MethodNode method) {
        this.injectionNodes = new InjectionNodes();
        this.classNode = classNode;
        this.method = method;
        this.insns = method.instructions;
        this.isStatic = Bytecode.methodIsStatic(method);
        this.isCtor = method.name.equals("<init>");
        this.arguments = Type.getArgumentTypes(method.desc);
        this.returnType = Type.getReturnType(method.desc);
        this.maxStack = method.maxStack;
        this.maxLocals = method.maxLocals;
    }
    
    public void wrapNode(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2, final InsnList list, final InsnList list2) {
        this.insns.insertBefore(abstractInsnNode, list);
        this.insns.insert(abstractInsnNode, list2);
        this.injectionNodes.replace(abstractInsnNode, abstractInsnNode2);
    }
    
    public AbstractInsnNode get(final int n) {
        return this.insns.get(n);
    }
    
    public void addToLocals(final int n) {
        this.setMaxLocals(this.maxLocals + n);
    }
    
    public InjectionNodes.InjectionNode addInjectionNode(final AbstractInsnNode abstractInsnNode) {
        return this.injectionNodes.add(abstractInsnNode);
    }
    
    public int getCurrentMaxLocals() {
        return this.method.maxLocals;
    }
    
    public String getCallbackDescriptor(final boolean b, final Type[] array, final Type[] array2, final int n, int n2) {
        if (this.callbackDescriptor == null) {
            this.callbackDescriptor = String.format("(%sL%s;)V", this.method.desc.substring(1, this.method.desc.indexOf(41)), this.getCallbackInfoClass());
        }
        if (!b || array == null) {
            return this.callbackDescriptor;
        }
        final StringBuilder sb = new StringBuilder(this.callbackDescriptor.substring(0, this.callbackDescriptor.indexOf(41)));
        for (int n3 = n; n3 < array.length && n2 > 0; ++n3) {
            if (array[n3] != null) {
                sb.append(array[n3].getDescriptor());
                --n2;
            }
        }
        return sb.append(")V").toString();
    }
    
    public int getCurrentMaxStack() {
        return this.method.maxStack;
    }
    
    public int indexOf(final AbstractInsnNode abstractInsnNode) {
        return this.insns.indexOf(abstractInsnNode);
    }
    
    public int getMaxLocals() {
        return this.maxLocals;
    }
    
    public String getCallbackInfoClass() {
        if (this.callbackInfoClass == null) {
            this.callbackInfoClass = CallbackInfo.getCallInfoClassName(this.returnType);
        }
        return this.callbackInfoClass;
    }
    
    @Override
    public String toString() {
        return String.format("%s::%s%s", this.classNode.name, this.method.name, this.method.desc);
    }
    
    public int allocateLocal() {
        return this.allocateLocals(1);
    }
    
    public int[] generateArgMap(final Type[] array, final int n) {
        if (this.argMapVars == null) {
            this.argMapVars = new ArrayList<Integer>();
        }
        final int[] array2 = new int[array.length];
        int i = n;
        int n2 = 0;
        while (i < array.length) {
            final int size = array[i].getSize();
            array2[i] = this.allocateArgMapLocal(n2, size);
            n2 += size;
            ++i;
        }
        return array2;
    }
    
    public MethodInsnNode findSuperInitNode() {
        if (!this.isCtor) {
            return null;
        }
        return Bytecode.findSuperInit(this.method, ClassInfo.forName(this.classNode.name).getSuperName());
    }
    
    public void replaceNode(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2) {
        this.insns.insertBefore(abstractInsnNode, abstractInsnNode2);
        this.insns.remove(abstractInsnNode);
        this.injectionNodes.replace(abstractInsnNode, abstractInsnNode2);
    }
    
    public void insertBefore(final InjectionNodes.InjectionNode injectionNode, final InsnList list) {
        this.insns.insertBefore(injectionNode.getCurrentTarget(), list);
    }
    
    private void addLocalVariable(final int n, final String s, final String s2, final LabelNode labelNode, final LabelNode labelNode2) {
        if (this.method.localVariables == null) {
            this.method.localVariables = new ArrayList<LocalVariableNode>();
        }
        this.method.localVariables.add(new LocalVariableNode(s, s2, null, labelNode, labelNode2, n));
    }
    
    private int[] calcArgIndices(int n) {
        final int[] array = new int[this.arguments.length];
        for (int i = 0; i < this.arguments.length; ++i) {
            array[i] = n;
            n += this.arguments[i].getSize();
        }
        return array;
    }
    
    public void addToStack(final int n) {
        this.setMaxStack(this.maxStack + n);
    }
    
    public String getSimpleCallbackDescriptor() {
        return String.format("(L%s;)V", this.getCallbackInfoClass());
    }
    
    public MethodInsnNode findInitNodeFor(final TypeInsnNode typeInsnNode) {
        final ListIterator<AbstractInsnNode> iterator = this.insns.iterator(this.indexOf(typeInsnNode));
        while (iterator.hasNext()) {
            final AbstractInsnNode abstractInsnNode = iterator.next();
            if (abstractInsnNode instanceof MethodInsnNode && abstractInsnNode.getOpcode() == 183) {
                final MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                if ("<init>".equals(methodInsnNode.name) && methodInsnNode.owner.equals(typeInsnNode.desc)) {
                    return methodInsnNode;
                }
                continue;
            }
        }
        return null;
    }
    
    private int allocateArgMapLocal(final int n, final int n2) {
        if (n >= this.argMapVars.size()) {
            final int allocateLocals = this.allocateLocals(n2);
            for (int i = 0; i < n2; ++i) {
                this.argMapVars.add(allocateLocals + i);
            }
            return allocateLocals;
        }
        final int intValue = this.argMapVars.get(n);
        if (n2 <= 1 || n + n2 <= this.argMapVars.size()) {
            return intValue;
        }
        final int allocateLocals2 = this.allocateLocals(1);
        if (allocateLocals2 == intValue + 1) {
            this.argMapVars.add(allocateLocals2);
            return intValue;
        }
        this.argMapVars.set(n, allocateLocals2);
        this.argMapVars.add(this.allocateLocals(1));
        return allocateLocals2;
    }
    
    public void insertBefore(final AbstractInsnNode abstractInsnNode, final InsnList list) {
        this.insns.insertBefore(abstractInsnNode, list);
    }
    
    public void setMaxStack(final int maxStack) {
        if (maxStack > this.method.maxStack) {
            this.method.maxStack = maxStack;
        }
    }
    
    public int indexOf(final InjectionNodes.InjectionNode injectionNode) {
        return this.insns.indexOf(injectionNode.getCurrentTarget());
    }
    
    public void replaceNode(final AbstractInsnNode abstractInsnNode, final AbstractInsnNode abstractInsnNode2, final InsnList list) {
        this.insns.insertBefore(abstractInsnNode, list);
        this.insns.remove(abstractInsnNode);
        this.injectionNodes.replace(abstractInsnNode, abstractInsnNode2);
    }
    
    public int allocateLocals(final int n) {
        final int maxLocals = this.method.maxLocals;
        final MethodNode method = this.method;
        method.maxLocals += n;
        return maxLocals;
    }
    
    public void setMaxLocals(final int maxLocals) {
        if (maxLocals > this.method.maxLocals) {
            this.method.maxLocals = maxLocals;
        }
    }
    
    public void replaceNode(final AbstractInsnNode abstractInsnNode, final InsnList list) {
        this.insns.insertBefore(abstractInsnNode, list);
        this.removeNode(abstractInsnNode);
    }
    
    public void removeNode(final AbstractInsnNode abstractInsnNode) {
        this.insns.remove(abstractInsnNode);
        this.injectionNodes.remove(abstractInsnNode);
    }
    
    public String getCallbackDescriptor(final Type[] array, final Type[] array2) {
        return this.getCallbackDescriptor(false, array, array2, 0, 32767);
    }
    
    public int[] getArgIndices() {
        if (this.argIndices == null) {
            this.argIndices = this.calcArgIndices(this.isStatic ? 0 : 1);
        }
        return this.argIndices;
    }
    
    @Override
    public Iterator<AbstractInsnNode> iterator() {
        return this.insns.iterator();
    }
}
