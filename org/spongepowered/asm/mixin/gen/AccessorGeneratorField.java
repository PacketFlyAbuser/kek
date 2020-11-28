// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.gen;

import org.spongepowered.asm.lib.tree.FieldNode;
import org.spongepowered.asm.lib.Type;

public abstract class AccessorGeneratorField extends AccessorGenerator
{
    protected final /* synthetic */ Type targetType;
    protected final /* synthetic */ boolean isInstanceField;
    protected final /* synthetic */ FieldNode targetField;
    
    public AccessorGeneratorField(final AccessorInfo accessorInfo) {
        super(accessorInfo);
        this.targetField = accessorInfo.getTargetField();
        this.targetType = accessorInfo.getTargetFieldType();
        this.isInstanceField = ((this.targetField.access & 0x8) == 0x0);
    }
}
