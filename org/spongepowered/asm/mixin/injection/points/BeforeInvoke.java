// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.points;

import java.util.ListIterator;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
import org.spongepowered.asm.lib.tree.MethodInsnNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.injection.struct.MemberInfo;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

@AtCode("INVOKE")
public class BeforeInvoke extends InjectionPoint
{
    protected final /* synthetic */ MemberInfo target;
    private /* synthetic */ boolean log;
    protected final /* synthetic */ MemberInfo permissiveTarget;
    private final /* synthetic */ Logger logger;
    protected final /* synthetic */ String className;
    protected final /* synthetic */ int ordinal;
    
    protected void log(final String s, final Object... array) {
        if (this.log) {
            this.logger.info(s, array);
        }
    }
    
    @Override
    public boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> collection) {
        this.log("{} is searching for an injection point in method with descriptor {}", this.className, s);
        return this.find(s, list, collection, this.target) || this.find(s, list, collection, this.permissiveTarget);
    }
    
    protected void inspectInsn(final String s, final InsnList list, final AbstractInsnNode abstractInsnNode) {
    }
    
    protected boolean addInsn(final InsnList list, final Collection<AbstractInsnNode> collection, final AbstractInsnNode abstractInsnNode) {
        collection.add(abstractInsnNode);
        return true;
    }
    
    private String getClassName() {
        final AtCode atCode = this.getClass().getAnnotation(AtCode.class);
        return String.format("@At(%s)", (atCode != null) ? atCode.value() : this.getClass().getSimpleName().toUpperCase());
    }
    
    protected boolean matchesInsn(final AbstractInsnNode abstractInsnNode) {
        return abstractInsnNode instanceof MethodInsnNode;
    }
    
    public BeforeInvoke(final InjectionPointData injectionPointData) {
        super(injectionPointData);
        this.log = false;
        this.logger = LogManager.getLogger("mixin");
        this.target = injectionPointData.getTarget();
        this.ordinal = injectionPointData.getOrdinal();
        this.log = injectionPointData.get("log", false);
        this.className = this.getClassName();
        this.permissiveTarget = (injectionPointData.getContext().getOption(MixinEnvironment.Option.REFMAP_REMAP) ? this.target.transform(null) : null);
    }
    
    public BeforeInvoke setLogging(final boolean log) {
        this.log = log;
        return this;
    }
    
    protected boolean matchesInsn(final MemberInfo memberInfo, final int i) {
        this.log("{} > > comparing target ordinal {} with current ordinal {}", this.className, this.ordinal, i);
        return this.ordinal == -1 || this.ordinal == i;
    }
    
    protected boolean find(final String s, final InsnList list, final Collection<AbstractInsnNode> collection, final MemberInfo memberInfo) {
        if (memberInfo == null) {
            return false;
        }
        int i = 0;
        boolean b = false;
        for (final AbstractInsnNode abstractInsnNode : list) {
            if (this.matchesInsn(abstractInsnNode)) {
                final MemberInfo memberInfo2 = new MemberInfo(abstractInsnNode);
                this.log("{} is considering insn {}", this.className, memberInfo2);
                if (memberInfo.matches(memberInfo2.owner, memberInfo2.name, memberInfo2.desc)) {
                    this.log("{} > found a matching insn, checking preconditions...", this.className);
                    if (this.matchesInsn(memberInfo2, i)) {
                        this.log("{} > > > found a matching insn at ordinal {}", this.className, i);
                        b |= this.addInsn(list, collection, abstractInsnNode);
                        if (this.ordinal == i) {
                            break;
                        }
                    }
                    ++i;
                }
            }
            this.inspectInsn(s, list, abstractInsnNode);
        }
        return b;
    }
}
