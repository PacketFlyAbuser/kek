// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.modify;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.VarInsnNode;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.lib.Type;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("LOAD")
public class BeforeLoadLocal extends ModifyVariableInjector.ContextualInjectionPoint
{
    private final /* synthetic */ Type returnType;
    private final /* synthetic */ int ordinal;
    private /* synthetic */ boolean opcodeAfter;
    private final /* synthetic */ LocalVariableDiscriminator discriminator;
    private final /* synthetic */ int opcode;
    
    protected BeforeLoadLocal(final InjectionPointData injectionPointData, final int n, final boolean opcodeAfter) {
        super(injectionPointData.getContext());
        this.returnType = injectionPointData.getMethodReturnType();
        this.discriminator = injectionPointData.getLocalVariableDiscriminator();
        this.opcode = injectionPointData.getOpcode(this.returnType.getOpcode(n));
        this.ordinal = injectionPointData.getOrdinal();
        this.opcodeAfter = opcodeAfter;
    }
    
    protected BeforeLoadLocal(final InjectionPointData injectionPointData) {
        this(injectionPointData, 21, false);
    }
    
    @Override
    boolean find(final Target target, final Collection<AbstractInsnNode> collection) {
        final SearchState searchState = new SearchState(this.ordinal, this.discriminator.printLVT());
        for (final AbstractInsnNode abstractInsnNode : target.method.instructions) {
            if (searchState.isPendingCheck()) {
                searchState.check(collection, abstractInsnNode, this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), target, abstractInsnNode));
            }
            else {
                if (!(abstractInsnNode instanceof VarInsnNode) || abstractInsnNode.getOpcode() != this.opcode || (this.ordinal != -1 && searchState.success())) {
                    continue;
                }
                searchState.register((VarInsnNode)abstractInsnNode);
                if (this.opcodeAfter) {
                    searchState.setPendingCheck();
                }
                else {
                    searchState.check(collection, abstractInsnNode, this.discriminator.findLocal(this.returnType, this.discriminator.isArgsOnly(), target, abstractInsnNode));
                }
            }
        }
        return searchState.success();
    }
    
    static class SearchState
    {
        private final /* synthetic */ int targetOrdinal;
        private /* synthetic */ boolean pendingCheck;
        private /* synthetic */ VarInsnNode varNode;
        private final /* synthetic */ boolean print;
        private /* synthetic */ int ordinal;
        private /* synthetic */ boolean found;
        
        SearchState(final int targetOrdinal, final boolean print) {
            this.ordinal = 0;
            this.pendingCheck = false;
            this.found = false;
            this.targetOrdinal = targetOrdinal;
            this.print = print;
        }
        
        boolean success() {
            return this.found;
        }
        
        void setPendingCheck() {
            this.pendingCheck = true;
        }
        
        boolean isPendingCheck() {
            return this.pendingCheck;
        }
        
        void check(final Collection<AbstractInsnNode> collection, final AbstractInsnNode abstractInsnNode, final int n) {
            this.pendingCheck = false;
            if (n != this.varNode.var && (n > -2 || !this.print)) {
                return;
            }
            if (this.targetOrdinal == -1 || this.targetOrdinal == this.ordinal) {
                collection.add(abstractInsnNode);
                this.found = true;
            }
            ++this.ordinal;
            this.varNode = null;
        }
        
        void register(final VarInsnNode varNode) {
            this.varNode = varNode;
        }
    }
}
