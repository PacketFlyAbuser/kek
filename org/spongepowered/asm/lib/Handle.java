// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib;

public final class Handle
{
    final /* synthetic */ boolean itf;
    final /* synthetic */ int tag;
    final /* synthetic */ String owner;
    final /* synthetic */ String desc;
    final /* synthetic */ String name;
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Handle)) {
            return false;
        }
        final Handle handle = (Handle)o;
        return this.tag == handle.tag && this.itf == handle.itf && this.owner.equals(handle.owner) && this.name.equals(handle.name) && this.desc.equals(handle.desc);
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public boolean isInterface() {
        return this.itf;
    }
    
    @Deprecated
    public Handle(final int n, final String s, final String s2, final String s3) {
        this(n, s, s2, s3, n == 9);
    }
    
    public int getTag() {
        return this.tag;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.owner + '.' + this.name + this.desc + " (" + this.tag + (this.itf ? " itf" : "") + ')';
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    @Override
    public int hashCode() {
        return this.tag + (this.itf ? 64 : 0) + this.owner.hashCode() * this.name.hashCode() * this.desc.hashCode();
    }
    
    public Handle(final int tag, final String owner, final String name, final String desc, final boolean itf) {
        this.tag = tag;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.itf = itf;
    }
}
