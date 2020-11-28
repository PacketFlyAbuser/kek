// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.obfuscation.mapping.common;

import com.google.common.base.Strings;
import com.google.common.base.Objects;
import org.spongepowered.asm.obfuscation.mapping.IMapping;

public class MappingField implements IMapping<MappingField>
{
    private final /* synthetic */ String owner;
    private final /* synthetic */ String name;
    private final /* synthetic */ String desc;
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public MappingField remap(final String s) {
        return new MappingField(this.getOwner(), s, this.getDesc());
    }
    
    @Override
    public MappingField getSuper() {
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof MappingField && Objects.equal((Object)this.toString(), (Object)((MappingField)o).toString()));
    }
    
    @Override
    public final String getOwner() {
        return this.owner;
    }
    
    public MappingField(final String owner, final String name, final String desc) {
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }
    
    @Override
    public MappingField transform(final String s) {
        return new MappingField(this.getOwner(), this.getName(), s);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(new Object[] { this.toString() });
    }
    
    public MappingField(final String s, final String s2) {
        this(s, s2, null);
    }
    
    @Override
    public final String getSimpleName() {
        return this.name;
    }
    
    @Override
    public final String getDesc() {
        return this.desc;
    }
    
    @Override
    public MappingField move(final String s) {
        return new MappingField(s, this.getName(), this.getDesc());
    }
    
    @Override
    public String toString() {
        return String.format("L%s;%s:%s", this.getOwner(), this.getName(), Strings.nullToEmpty(this.getDesc()));
    }
    
    @Override
    public MappingField copy() {
        return new MappingField(this.getOwner(), this.getName(), this.getDesc());
    }
    
    @Override
    public Type getType() {
        return Type.FIELD;
    }
    
    @Override
    public String serialise() {
        return this.toString();
    }
}
