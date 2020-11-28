// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Objects;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingMethod implements IMapping<MappingMethod>
{
    private final /* synthetic */ String owner;
    private final /* synthetic */ String desc;
    private final /* synthetic */ String name;
    
    @Override
    public String getName() {
        if (this.name == null) {
            return null;
        }
        return ((this.owner != null) ? (this.owner + "/") : "") + this.name;
    }
    
    @Override
    public String serialise() {
        return this.toString();
    }
    
    private static String getBaseName(final String s) {
        if (s == null) {
            return null;
        }
        final int lastIndex = s.lastIndexOf(47);
        return (lastIndex > -1) ? s.substring(lastIndex + 1) : s;
    }
    
    @Override
    public MappingMethod copy() {
        return new MappingMethod(this.getOwner(), this.getSimpleName(), this.getDesc());
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof MappingMethod && Objects.equal((Object)this.name, (Object)((MappingMethod)o).name) && Objects.equal((Object)this.desc, (Object)((MappingMethod)o).desc));
    }
    
    public MappingMethod(final String s, final String s2) {
        this(getOwnerFromName(s), getBaseName(s), s2);
    }
    
    public MappingMethod(final String owner, final String name, final String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }
    
    public MappingMethod addPrefix(final String s) {
        final String simpleName = this.getSimpleName();
        if (simpleName == null || simpleName.startsWith(s)) {
            return this;
        }
        return new MappingMethod(this.getOwner(), s + simpleName, this.getDesc());
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.getName(), this.getDesc() });
    }
    
    @Override
    public String getSimpleName() {
        return this.name;
    }
    
    @Override
    public String getOwner() {
        return this.owner;
    }
    
    @Override
    public Type getType() {
        return Type.METHOD;
    }
    
    @Override
    public MappingMethod getSuper() {
        return null;
    }
    
    private static String getOwnerFromName(final String s) {
        if (s == null) {
            return null;
        }
        final int lastIndex = s.lastIndexOf(47);
        return (lastIndex > -1) ? s.substring(0, lastIndex) : null;
    }
    
    @Override
    public MappingMethod remap(final String s) {
        return new MappingMethod(this.getOwner(), s, this.getDesc());
    }
    
    @Override
    public String toString() {
        final String desc = this.getDesc();
        return String.format("%s%s%s", this.getName(), (desc != null) ? " " : "", (desc != null) ? desc : "");
    }
    
    @Override
    public MappingMethod transform(final String s) {
        return new MappingMethod(this.getOwner(), this.getSimpleName(), s);
    }
    
    @Override
    public MappingMethod move(final String s) {
        return new MappingMethod(s, this.getSimpleName(), this.getDesc());
    }
    
    public boolean isConstructor() {
        return "<init>".equals(this.name);
    }
    
    @Override
    public String getDesc() {
        return this.desc;
    }
}
