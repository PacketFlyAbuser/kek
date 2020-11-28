// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.ListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InfoList<T extends HasName> extends PotentiallyUnmodifiableList<T>
{
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    public List<String> getNames() {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayList<String> list = new ArrayList<String>(this.size());
        for (final HasName hasName : this) {
            if (hasName != null) {
                list.add(hasName.getName());
            }
        }
        return list;
    }
    
    InfoList() {
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    InfoList(final Collection<T> collection) {
        super(collection);
    }
    
    InfoList(final int n) {
        super(n);
    }
    
    public List<String> getAsStrings() {
        if (this.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayList<String> list = new ArrayList<String>(this.size());
        for (final HasName hasName : this) {
            list.add((hasName == null) ? "null" : hasName.toString());
        }
        return list;
    }
}
