// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

public class Pair<T, S>
{
    /* synthetic */ T key;
    /* synthetic */ S value;
    
    public void setValue(final S value) {
        this.value = value;
    }
    
    public Pair(final T key, final S value) {
        this.key = key;
        this.value = value;
    }
    
    public T getKey() {
        return this.key;
    }
    
    public void setKey(final T key) {
        this.key = key;
    }
    
    public S getValue() {
        return this.value;
    }
}
