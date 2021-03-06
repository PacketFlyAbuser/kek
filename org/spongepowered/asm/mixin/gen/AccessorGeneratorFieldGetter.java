// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.InsnNode;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;

public class AccessorGeneratorFieldGetter extends AccessorGeneratorField
{
    @Override
    public MethodNode generate() {
        final MethodNode method = this.createMethod(this.targetType.getSize(), this.targetType.getSize());
        if (this.isInstanceField) {
            method.instructions.add(new VarInsnNode(25, 0));
        }
        method.instructions.add(new FieldInsnNode(this.isInstanceField ? 180 : 178, this.info.getClassNode().name, this.targetField.name, this.targetField.desc));
        method.instructions.add(new InsnNode(this.targetType.getOpcode(172)));
        return method;
    }
    
    public AccessorGeneratorFieldGetter(final AccessorInfo accessorInfo) {
        super(accessorInfo);
    }
}
