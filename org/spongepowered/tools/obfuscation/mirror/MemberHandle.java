// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mirror;

import org.spongepowered.asm.obfuscation.mapping.IMapping;

public abstract class MemberHandle<T extends IMapping<T>>
{
    private final /* synthetic */ String desc;
    private final /* synthetic */ String name;
    private final /* synthetic */ String owner;
    
    public final String getName() {
        return this.name;
    }
    
    public abstract T asMapping(final boolean p0);
    
    public abstract Visibility getVisibility();
    
    public final String getOwner() {
        return this.owner;
    }
    
    public final String getDesc() {
        return this.desc;
    }
    
    protected MemberHandle(final String owner, final String name, final String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }
}
