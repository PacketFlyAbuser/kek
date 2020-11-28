// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.struct;

import org.spongepowered.asm.mixin.transformer.throwables.MixinTransformerError;
import org.spongepowered.asm.lib.Handle;
import org.spongepowered.asm.lib.tree.FieldInsnNode;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.util.Bytecode;

public abstract class MemberRef
{
    private static final /* synthetic */ int[] H_OPCODES;
    
    public abstract int getOpcode();
    
    public abstract void setOpcode(final int p0);
    
    public abstract void setOwner(final String p0);
    
    public abstract String getDesc();
    
    public abstract boolean isField();
    
    static {
        H_OPCODES = new int[] { 0, 180, 178, 181, 179, 182, 184, 183, 183, 185 };
    }
    
    public abstract String getOwner();
    
    public abstract void setName(final String p0);
    
    @Override
    public String toString() {
        return String.format("%s for %s.%s%s%s", Bytecode.getOpcodeName(this.getOpcode()), this.getOwner(), this.getName(), this.isField() ? ":" : "", this.getDesc());
    }
    
    static int opcodeFromTag(final int n) {
        return (n >= 0 && n < MemberRef.H_OPCODES.length) ? MemberRef.H_OPCODES[n] : 0;
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public abstract void setDesc(final String p0);
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MemberRef)) {
            return false;
        }
        final MemberRef memberRef = (MemberRef)o;
        return this.getOpcode() == memberRef.getOpcode() && this.getOwner().equals(memberRef.getOwner()) && this.getName().equals(memberRef.getName()) && this.getDesc().equals(memberRef.getDesc());
    }
    
    public abstract String getName();
    
    static int tagFromOpcode(final int n) {
        for (int i = 1; i < MemberRef.H_OPCODES.length; ++i) {
            if (MemberRef.H_OPCODES[i] == n) {
                return i;
            }
        }
        return 0;
    }
    
    public static final class Method extends MemberRef
    {
        public final /* synthetic */ MethodInsnNode insn;
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        @Override
        public void setOpcode(final int n) {
            if ((n & 0xBF) == 0x0) {
                throw new IllegalArgumentException("Invalid opcode for method instruction: 0x" + Integer.toHexString(n));
            }
            this.insn.setOpcode(n);
        }
        
        @Override
        public int getOpcode() {
            return this.insn.getOpcode();
        }
        
        @Override
        public void setName(final String name) {
            this.insn.name = name;
        }
        
        @Override
        public boolean isField() {
            return false;
        }
        
        @Override
        public void setOwner(final String owner) {
            this.insn.owner = owner;
        }
        
        public Method(final MethodInsnNode insn) {
            this.insn = insn;
        }
        
        static {
            OPCODES = 191;
        }
        
        @Override
        public void setDesc(final String desc) {
            this.insn.desc = desc;
        }
    }
    
    public static final class Field extends MemberRef
    {
        public final /* synthetic */ FieldInsnNode insn;
        
        @Override
        public boolean isField() {
            return true;
        }
        
        @Override
        public String getName() {
            return this.insn.name;
        }
        
        public Field(final FieldInsnNode insn) {
            this.insn = insn;
        }
        
        @Override
        public String getOwner() {
            return this.insn.owner;
        }
        
        @Override
        public void setDesc(final String desc) {
            this.insn.desc = desc;
        }
        
        @Override
        public int getOpcode() {
            return this.insn.getOpcode();
        }
        
        static {
            OPCODES = 183;
        }
        
        @Override
        public void setName(final String name) {
            this.insn.name = name;
        }
        
        @Override
        public void setOpcode(final int n) {
            if ((n & 0xB7) == 0x0) {
                throw new IllegalArgumentException("Invalid opcode for field instruction: 0x" + Integer.toHexString(n));
            }
            this.insn.setOpcode(n);
        }
        
        @Override
        public String getDesc() {
            return this.insn.desc;
        }
        
        @Override
        public void setOwner(final String owner) {
            this.insn.owner = owner;
        }
    }
    
    public static final class Handle extends MemberRef
    {
        private /* synthetic */ org.spongepowered.asm.lib.Handle handle;
        
        @Override
        public String getDesc() {
            return this.handle.getDesc();
        }
        
        @Override
        public boolean isField() {
            switch (this.handle.getTag()) {
                case 5:
                case 6:
                case 7:
                case 8:
                case 9: {
                    return false;
                }
                case 1:
                case 2:
                case 3:
                case 4: {
                    return true;
                }
                default: {
                    throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
                }
            }
        }
        
        @Override
        public void setName(final String s) {
            this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), s, this.handle.getDesc(), this.handle.getTag() == 9);
        }
        
        public org.spongepowered.asm.lib.Handle getMethodHandle() {
            return this.handle;
        }
        
        @Override
        public String getOwner() {
            return this.handle.getOwner();
        }
        
        @Override
        public void setOpcode(final int n) {
            final int tagFromOpcode = MemberRef.tagFromOpcode(n);
            if (tagFromOpcode == 0) {
                throw new MixinTransformerError("Invalid opcode " + Bytecode.getOpcodeName(n) + " for method handle " + this.handle + ".");
            }
            this.handle = new org.spongepowered.asm.lib.Handle(tagFromOpcode, this.handle.getOwner(), this.handle.getName(), this.handle.getDesc(), tagFromOpcode == 9);
        }
        
        @Override
        public String getName() {
            return this.handle.getName();
        }
        
        @Override
        public void setOwner(final String s) {
            this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), s, this.handle.getName(), this.handle.getDesc(), this.handle.getTag() == 9);
        }
        
        public Handle(final org.spongepowered.asm.lib.Handle handle) {
            this.handle = handle;
        }
        
        @Override
        public int getOpcode() {
            final int opcodeFromTag = MemberRef.opcodeFromTag(this.handle.getTag());
            if (opcodeFromTag == 0) {
                throw new MixinTransformerError("Invalid tag " + this.handle.getTag() + " for method handle " + this.handle + ".");
            }
            return opcodeFromTag;
        }
        
        @Override
        public void setDesc(final String s) {
            this.handle = new org.spongepowered.asm.lib.Handle(this.handle.getTag(), this.handle.getOwner(), this.handle.getName(), s, this.handle.getTag() == 9);
        }
    }
}
