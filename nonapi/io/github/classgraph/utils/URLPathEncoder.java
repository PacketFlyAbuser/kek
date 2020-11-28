// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.nio.charset.StandardCharsets;

public final class URLPathEncoder
{
    private static /* synthetic */ boolean[] safe;
    private static final /* synthetic */ char[] HEXADECIMAL;
    private static final /* synthetic */ String[] SCHEME_PREFIXES;
    
    private URLPathEncoder() {
    }
    
    static {
        URLPathEncoder.safe = new boolean[256];
        for (int i = 97; i <= 122; ++i) {
            URLPathEncoder.safe[i] = true;
        }
        for (int j = 65; j <= 90; ++j) {
            URLPathEncoder.safe[j] = true;
        }
        for (int k = 48; k <= 57; ++k) {
            URLPathEncoder.safe[k] = true;
        }
        final boolean[] safe = URLPathEncoder.safe;
        final int n = 36;
        final boolean[] safe2 = URLPathEncoder.safe;
        final int n2 = 45;
        final boolean[] safe3 = URLPathEncoder.safe;
        final int n3 = 95;
        final boolean[] safe4 = URLPathEncoder.safe;
        final int n4 = 46;
        final boolean[] safe5 = URLPathEncoder.safe;
        final int n5 = 43;
        final boolean b = true;
        safe5[n5] = b;
        safe3[n3] = (safe4[n4] = b);
        safe[n] = (safe2[n2] = b);
        final boolean[] safe6 = URLPathEncoder.safe;
        final int n6 = 33;
        final boolean[] safe7 = URLPathEncoder.safe;
        final int n7 = 42;
        final boolean[] safe8 = URLPathEncoder.safe;
        final int n8 = 39;
        final boolean[] safe9 = URLPathEncoder.safe;
        final int n9 = 40;
        final boolean[] safe10 = URLPathEncoder.safe;
        final int n10 = 41;
        final boolean[] safe11 = URLPathEncoder.safe;
        final int n11 = 44;
        final boolean b2 = true;
        safe10[n10] = (safe11[n11] = b2);
        safe8[n8] = (safe9[n9] = b2);
        safe6[n6] = (safe7[n7] = b2);
        URLPathEncoder.safe[47] = true;
        HEXADECIMAL = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        SCHEME_PREFIXES = new String[] { "jrt:", "file:", "jar:file:", "jar:", "http:", "https:" };
    }
    
    public static String encodePath(final String s) {
        int length = 0;
        for (final String prefix : URLPathEncoder.SCHEME_PREFIXES) {
            if (s.startsWith(prefix)) {
                length = prefix.length();
                break;
            }
        }
        if (VersionFinder.OS == VersionFinder.OperatingSystem.Windows) {
            int n = length;
            if (n < s.length() && s.charAt(n) == '/') {
                ++n;
            }
            if (n < s.length() - 1 && Character.isLetter(s.charAt(n)) && s.charAt(n + 1) == ':') {
                length = n + 2;
            }
        }
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        final StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int j = 0; j < bytes.length; ++j) {
            final int n2 = bytes[j] & 0xFF;
            if (URLPathEncoder.safe[n2] || (n2 == 58 && j < length)) {
                sb.append((char)n2);
            }
            else {
                sb.append('%');
                sb.append(URLPathEncoder.HEXADECIMAL[(n2 & 0xF0) >> 4]);
                sb.append(URLPathEncoder.HEXADECIMAL[n2 & 0xF]);
            }
        }
        return sb.toString();
    }
    
    public static String normalizeURLPath(final String s) {
        String str = s;
        if (!str.startsWith("jrt:") && !str.startsWith("http://") && !str.startsWith("https://")) {
            if (str.startsWith("jar:")) {
                str = str.substring(4);
            }
            if (str.startsWith("file:")) {
                str = str.substring(4);
            }
            String str2 = "";
            if (VersionFinder.OS == VersionFinder.OperatingSystem.Windows) {
                if (str.length() >= 2 && Character.isLetter(str.charAt(0)) && str.charAt(1) == ':') {
                    str2 = str.substring(0, 2);
                    str = str.substring(2);
                }
                else if (str.length() >= 3 && str.charAt(0) == '/' && Character.isLetter(str.charAt(1)) && str.charAt(2) == ':') {
                    str2 = str.substring(1, 3);
                    str = str.substring(3);
                }
            }
            final String replace = str.replace("/!", "!").replace("!/", "!").replace("!", "!/");
            if (str2.isEmpty()) {
                str = (replace.startsWith("/") ? ("file:" + replace) : ("file:/" + replace));
            }
            else {
                str = "file:/" + str2 + (replace.startsWith("/") ? replace : ("/" + replace));
            }
            if (str.contains("!") && !str.startsWith("jar:")) {
                str = "jar:" + str;
            }
        }
        return encodePath(str);
    }
}
