// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class CollectionUtils
{
    public static <T> void sortIfNotEmpty(final List<T> list, final Comparator<? super T> c) {
        if (!list.isEmpty()) {
            Collections.sort(list, c);
        }
    }
    
    public static <T extends Comparable<? super T>> void sortIfNotEmpty(final List<T> list) {
        if (!list.isEmpty()) {
            Collections.sort(list);
        }
    }
    
    private CollectionUtils() {
    }
}
