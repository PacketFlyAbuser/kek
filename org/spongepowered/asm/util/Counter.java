// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

public final class Counter
{
    public /* synthetic */ int value;
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o.getClass() == Counter.class && ((Counter)o).value == this.value;
    }
}
