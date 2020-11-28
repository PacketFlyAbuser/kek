// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

public final class StringUtils
{
    private StringUtils() {
    }
    
    public static String readString(final byte[] array, final int n, final int n2, final boolean b, final boolean b2) throws IllegalArgumentException {
        if (n < 0L || n2 < 0 || n + n2 > array.length) {
            throw new IllegalArgumentException("offset or numBytes out of range");
        }
        final char[] array2 = new char[n2];
        int i = 0;
        int count = 0;
        while (i < n2) {
            final int n3 = array[n + i] & 0xFF;
            if (n3 > 127) {
                break;
            }
            array2[count++] = (char)((b && n3 == 47) ? 46 : n3);
            ++i;
        }
        while (i < n2) {
            final int n4 = array[n + i] & 0xFF;
            switch (n4 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7: {
                    ++i;
                    array2[count++] = (char)((b && n4 == 47) ? 46 : n4);
                    continue;
                }
                case 12:
                case 13: {
                    i += 2;
                    if (i > n2) {
                        throw new IllegalArgumentException("Bad modified UTF8");
                    }
                    final byte b3 = array[n + i - 1];
                    if ((b3 & 0xC0) != 0x80) {
                        throw new IllegalArgumentException("Bad modified UTF8");
                    }
                    final int n5 = (n4 & 0x1F) << 6 | (b3 & 0x3F);
                    array2[count++] = (char)((b && n5 == 47) ? 46 : n5);
                    continue;
                }
                case 14: {
                    i += 3;
                    if (i > n2) {
                        throw new IllegalArgumentException("Bad modified UTF8");
                    }
                    final byte b4 = array[n + i - 2];
                    final byte b5 = array[n + i - 1];
                    if ((b4 & 0xC0) != 0x80 || (b5 & 0xC0) != 0x80) {
                        throw new IllegalArgumentException("Bad modified UTF8");
                    }
                    final int n6 = (n4 & 0xF) << 12 | (b4 & 0x3F) << 6 | (b5 & 0x3F);
                    array2[count++] = (char)((b && n6 == 47) ? 46 : n6);
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("Bad modified UTF8");
                }
            }
        }
        if (count == n2 && !b2) {
            return new String(array2);
        }
        if (!b2) {
            return new String(array2, 0, count);
        }
        if (count < 2 || array2[0] != 'L' || array2[count - 1] != ';') {
            throw new IllegalArgumentException("Expected string to start with 'L' and end with ';', got \"" + new String(array2) + "\"");
        }
        return new String(array2, 1, count - 2);
    }
}
