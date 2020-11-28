// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.util.Iterator;

public final class Join
{
    public static String join(final String s, final Iterable<?> iterable) {
        final StringBuilder sb = new StringBuilder();
        join(sb, "", s, "", iterable);
        return sb.toString();
    }
    
    private Join() {
    }
    
    public static String join(final String str, final Object... array) {
        final StringBuilder sb = new StringBuilder();
        int n = 1;
        for (final Object o : array) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(str);
            }
            sb.append(o.toString());
        }
        return sb.toString();
    }
    
    public static void join(final StringBuilder sb, final String str, final String str2, final String str3, final Iterable<?> iterable) {
        if (!str.isEmpty()) {
            sb.append(str);
        }
        int n = 1;
        for (final Object next : iterable) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(str2);
            }
            sb.append((next == null) ? "null" : next.toString());
        }
        if (!str3.isEmpty()) {
            sb.append(str3);
        }
    }
}
