// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.invoke.arg;

public abstract class Args
{
    protected final /* synthetic */ Object[] values;
    
    public abstract void setAll(final Object... p0);
    
    public int size() {
        return this.values.length;
    }
    
    protected Args(final Object[] values) {
        this.values = values;
    }
    
    public abstract <T> void set(final int p0, final T p1);
    
    public <T> T get(final int n) {
        return (T)this.values[n];
    }
}
