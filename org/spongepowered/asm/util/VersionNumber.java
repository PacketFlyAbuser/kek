// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.Serializable;

public final class VersionNumber implements Comparable<VersionNumber>, Serializable
{
    private final /* synthetic */ String suffix;
    private static final /* synthetic */ Pattern PATTERN;
    private final /* synthetic */ long value;
    public static final /* synthetic */ VersionNumber NONE;
    
    private VersionNumber(final short n, final short n2, final short n3, final short n4, final String s) {
        this.value = pack(n, n2, n3, n4);
        this.suffix = ((s != null) ? s : "");
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof VersionNumber && ((VersionNumber)o).value == this.value;
    }
    
    private VersionNumber(final short[] array) {
        this(array, null);
    }
    
    @Override
    public int compareTo(final VersionNumber versionNumber) {
        if (versionNumber == null) {
            return 1;
        }
        final long n = this.value - versionNumber.value;
        return (n > 0L) ? 1 : ((n < 0L) ? -1 : 0);
    }
    
    static {
        NONE = new VersionNumber();
        PATTERN = Pattern.compile("^(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5})(?:\\.(\\d{1,5}))?)?)?(-[a-zA-Z0-9_\\-]+)?$");
    }
    
    public static VersionNumber parse(final String s) {
        return parse(s, VersionNumber.NONE);
    }
    
    public static VersionNumber parse(final String s, final String s2) {
        return parse(s, parse(s2));
    }
    
    private VersionNumber(final short[] array, final String s) {
        this.value = pack(array);
        this.suffix = ((s != null) ? s : "");
    }
    
    @Override
    public int hashCode() {
        return (int)(this.value >> 32) ^ (int)(this.value & 0xFFFFFFFFL);
    }
    
    private static long pack(final short... array) {
        return (long)array[0] << 48 | (long)array[1] << 32 | (long)(array[2] << 16) | (long)array[3];
    }
    
    private static short[] unpack(final long n) {
        return new short[] { (short)(n >> 48), (short)(n >> 32 & 0x7FFFL), (short)(n >> 16 & 0x7FFFL), (short)(n & 0x7FFFL) };
    }
    
    private static VersionNumber parse(final String input, final VersionNumber versionNumber) {
        if (input == null) {
            return versionNumber;
        }
        final Matcher matcher = VersionNumber.PATTERN.matcher(input);
        if (!matcher.matches()) {
            return versionNumber;
        }
        final short[] array = new short[4];
        for (int i = 0; i < 4; ++i) {
            final String group = matcher.group(i + 1);
            if (group != null) {
                final int int1 = Integer.parseInt(group);
                if (int1 > 32767) {
                    throw new IllegalArgumentException("Version parts cannot exceed 32767, found " + int1);
                }
                array[i] = (short)int1;
            }
        }
        return new VersionNumber(array, matcher.group(5));
    }
    
    private VersionNumber() {
        this.value = 0L;
        this.suffix = "";
    }
    
    @Override
    public String toString() {
        final short[] unpack = unpack(this.value);
        return String.format("%d.%d%3$s%4$s%5$s", unpack[0], unpack[1], ((this.value & 0x7FFFFFFFL) > 0L) ? String.format(".%d", unpack[2]) : "", ((this.value & 0x7FFFL) > 0L) ? String.format(".%d", unpack[3]) : "", this.suffix);
    }
    
    private VersionNumber(final short n, final short n2, final short n3, final short n4) {
        this(n, n2, n3, n4, null);
    }
}
