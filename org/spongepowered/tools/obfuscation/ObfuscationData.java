// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObfuscationData<T> implements Iterable<ObfuscationType>
{
    private final /* synthetic */ T defaultValue;
    private final /* synthetic */ Map<ObfuscationType, T> data;
    
    public ObfuscationData() {
        this(null);
    }
    
    @Override
    public String toString() {
        return String.format("ObfuscationData[%s,DEFAULT=%s]", this.listValues(), this.defaultValue);
    }
    
    public void put(final ObfuscationType obfuscationType, final T t) {
        this.data.put(obfuscationType, t);
    }
    
    private String listValues() {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        for (final ObfuscationType obfuscationType : this.data.keySet()) {
            if (n != 0) {
                sb.append(',');
            }
            sb.append(obfuscationType.getKey()).append('=').append(this.data.get(obfuscationType));
            n = 1;
        }
        return sb.toString();
    }
    
    @Override
    public Iterator<ObfuscationType> iterator() {
        return this.data.keySet().iterator();
    }
    
    public T get(final ObfuscationType obfuscationType) {
        final T value = this.data.get(obfuscationType);
        return (value != null) ? value : this.defaultValue;
    }
    
    public String values() {
        return "[" + this.listValues() + "]";
    }
    
    public boolean isEmpty() {
        return this.data.isEmpty();
    }
    
    public ObfuscationData(final T defaultValue) {
        this.data = new HashMap<ObfuscationType, T>();
        this.defaultValue = defaultValue;
    }
    
    @Deprecated
    public void add(final ObfuscationType obfuscationType, final T t) {
        this.put(obfuscationType, t);
    }
}
