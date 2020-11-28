// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class MappableInfoList<T extends HasName> extends InfoList<T>
{
    public Map<String, T> asMap() {
        final HashMap<String, HasName> hashMap = (HashMap<String, HasName>)new HashMap<String, T>();
        for (final HasName hasName : this) {
            if (hasName != null) {
                hashMap.put(hasName.getName(), hasName);
            }
        }
        return (Map<String, T>)hashMap;
    }
    
    public T get(final String anObject) {
        for (final HasName hasName : this) {
            if (hasName != null && hasName.getName().equals(anObject)) {
                return (T)hasName;
            }
        }
        return null;
    }
    
    public boolean containsName(final String anObject) {
        for (final HasName hasName : this) {
            if (hasName != null && hasName.getName().equals(anObject)) {
                return true;
            }
        }
        return false;
    }
    
    MappableInfoList(final Collection<T> collection) {
        super(collection);
    }
    
    MappableInfoList() {
    }
    
    MappableInfoList(final int n) {
        super(n);
    }
}
