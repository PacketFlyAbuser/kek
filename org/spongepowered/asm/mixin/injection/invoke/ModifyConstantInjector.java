// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke;

import org.spongepowered.asm.mixin.injection.struct.InjectionNodes;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.JumpInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;

public class ModifyConstantInjector extends RedirectInjector
{
    private void injectExpandedConstantModifier(final Target obj, final JumpInsnNode jumpInsnNode) {
        final int opcode = jumpInsnNode.getOpcode();
        if (opcode < 155 || opcode > 158) {
            throw new InvalidInjectionException(this.info, this.annotationType + " annotation selected an invalid opcode " + Bytecode.getOpcodeName(opcode) + " in " + obj + " in " + this);
        }
        final InsnList list = new InsnList();
        list.add(new InsnNode(3));
        final AbstractInsnNode invokeConstantHandler = this.invokeConstantHandler(Type.getType("I"), obj, list, list);
        list.add(new JumpInsnNode(opcode + 6, jumpInsnNode.label));
        obj.replaceNode(jumpInsnNode, invokeConstantHandler, list);
        obj.addToStack(1);
    }
    
    private AbstractInsnNode invokeConstantHandler(final Type type, final Target target, final InsnList list, final InsnList list2) {
        final boolean checkDescriptor = this.checkDescriptor(Bytecode.generateDescriptor(type, type), target, "getter");
        if (!this.isStatic) {
            list.insert(new VarInsnNode(25, 0));
            target.addToStack(1);
        }
        if (checkDescriptor) {
            this.pushArgs(target.arguments, list2, target.getArgIndices(), 0, target.arguments.length);
            target.addToStack(Bytecode.getArgsSize(target.arguments));
        }
        return this.invokeHandler(list2);
    }
    
    private void injectConstantModifier(final Target target, final AbstractInsnNode abstractInsnNode) {
        final Type constantType = Bytecode.getConstantType(abstractInsnNode);
        final InsnList list = new InsnList();
        final InsnList list2 = new InsnList();
        target.wrapNode(abstractInsnNode, this.invokeConstantHandler(constantType, target, list, list2), list, list2);
    }
    
    public ModifyConstantInjector(final InjectionInfo injectionInfo) {
        super(injectionInfo, "@ModifyConstant");
    }
    
    @Override
    protected void inject(final Target obj, final InjectionNodes.InjectionNode injectionNode) {
        if (!this.preInject(injectionNode)) {
            return;
        }
        if (injectionNode.isReplaced()) {
            throw new UnsupportedOperationException("Target failure for " + this.info);
        }
        final AbstractInsnNode currentTarget = injectionNode.getCurrentTarget();
        if (currentTarget instanceof JumpInsnNode) {
            this.checkTargetModifiers(obj, false);
            this.injectExpandedConstantModifier(obj, (JumpInsnNode)currentTarget);
            return;
        }
        if (Bytecode.isConstant(currentTarget)) {
            this.checkTargetModifiers(obj, false);
            this.injectConstantModifier(obj, currentTarget);
            return;
        }
        throw new InvalidInjectionException(this.info, this.annotationType + " annotation is targetting an invalid insn in " + obj + " in " + this);
    }
    
    static {
        OPCODE_OFFSET = 6;
    }
}
