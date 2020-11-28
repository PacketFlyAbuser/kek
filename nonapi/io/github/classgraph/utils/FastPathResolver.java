// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.io.File;
import java.util.regex.Pattern;

public final class FastPathResolver
{
    private static final /* synthetic */ Pattern schemeTwoSlashMatcher;
    private static final /* synthetic */ Pattern schemeOneSlashMatcher;
    private static final /* synthetic */ boolean WINDOWS;
    private static final /* synthetic */ Pattern percentMatcher;
    
    private static int hexCharToInt(final char c) {
        return (c >= '0' && c <= '9') ? (c - '0') : ((c >= 'a' && c <= 'f') ? (c - 'a' + 10) : (c - 'A' + 10));
    }
    
    private static void translateSeparator(final String s, final int n, final int n2, final boolean b, final StringBuilder sb) {
        for (int i = n; i < n2; ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '\\' || char1 == '/') {
                if (i < n2 - 1 || !b) {
                    if (((sb.length() == 0) ? '\0' : sb.charAt(sb.length() - 1)) != '/') {
                        sb.append('/');
                    }
                }
            }
            else {
                sb.append(char1);
            }
        }
    }
    
    private FastPathResolver() {
    }
    
    public static String resolve(final String s) {
        return resolve(null, s);
    }
    
    static {
        percentMatcher = Pattern.compile("([%][0-9a-fA-F][0-9a-fA-F])+");
        schemeTwoSlashMatcher = Pattern.compile("^[a-zA-Z+\\-.]+://");
        schemeOneSlashMatcher = Pattern.compile("^[a-zA-Z+\\-.]+:/");
        WINDOWS = (File.separatorChar == '\\');
    }
    
    public static String normalizePath(final String input, final boolean b) {
        final boolean b2 = input.indexOf(37) >= 0;
        if (!b2 && input.indexOf(92) < 0 && !input.endsWith("/")) {
            return input;
        }
        final int length = input.length();
        final StringBuilder sb = new StringBuilder();
        if (b2 && b) {
            int n = 0;
            final Matcher matcher = FastPathResolver.percentMatcher.matcher(input);
            while (matcher.find()) {
                final int start = matcher.start();
                final int end = matcher.end();
                translateSeparator(input, n, start, false, sb);
                unescapePercentEncoding(input, start, end, sb);
                n = end;
            }
            translateSeparator(input, n, length, true, sb);
            return sb.toString();
        }
        translateSeparator(input, 0, length, true, sb);
        return sb.toString();
    }
    
    public static String resolve(final String str, final String s) {
        if (s == null || s.isEmpty()) {
            return (str == null) ? "" : str;
        }
        String string = "";
        boolean b = false;
        boolean b2 = false;
        int beginIndex = 0;
        if (s.regionMatches(true, 0, "jar:", 0, 4)) {
            beginIndex = 4;
            b2 = true;
        }
        if (s.regionMatches(true, beginIndex, "http://", 0, 7)) {
            beginIndex += 7;
            string = "http://";
            b = true;
        }
        else if (s.regionMatches(true, beginIndex, "https://", 0, 8)) {
            beginIndex += 8;
            string = "https://";
            b = true;
        }
        else if (s.regionMatches(true, beginIndex, "jrt:", 0, 5)) {
            beginIndex += 4;
            string = "jrt:";
            b = true;
        }
        else if (s.regionMatches(true, beginIndex, "file:", 0, 5)) {
            beginIndex += 5;
            b2 = true;
        }
        else {
            final String s2 = (beginIndex == 0) ? s : s.substring(beginIndex);
            final Matcher matcher = FastPathResolver.schemeTwoSlashMatcher.matcher(s2);
            if (matcher.find()) {
                final String group = matcher.group();
                beginIndex += group.length();
                string = group;
                b = true;
            }
            else {
                final Matcher matcher2 = FastPathResolver.schemeOneSlashMatcher.matcher(s2);
                if (matcher2.find()) {
                    final String group2 = matcher2.group();
                    beginIndex += group2.length();
                    string = group2;
                    b = true;
                }
            }
        }
        if (b2) {
            if (FastPathResolver.WINDOWS) {
                if (s.startsWith("\\\\\\\\", beginIndex) || s.startsWith("////", beginIndex)) {
                    beginIndex += 4;
                    string += "//";
                    b = true;
                }
                else if (s.startsWith("\\\\", beginIndex)) {
                    beginIndex += 2;
                }
            }
            if (s.startsWith("///", beginIndex)) {
                beginIndex += 2;
            }
        }
        if (FastPathResolver.WINDOWS) {
            if (s.startsWith("//", beginIndex) || s.startsWith("\\\\", beginIndex)) {
                beginIndex += 2;
                string = "//";
                b = true;
            }
            else if (s.length() - beginIndex > 2 && Character.isLetter(s.charAt(beginIndex)) && s.charAt(beginIndex + 1) == ':') {
                b = true;
            }
            else if (s.length() - beginIndex > 3 && (s.charAt(beginIndex) == '/' || s.charAt(beginIndex) == '\\') && Character.isLetter(s.charAt(beginIndex + 1)) && s.charAt(beginIndex + 2) == ':') {
                b = true;
                ++beginIndex;
            }
        }
        if (s.length() - beginIndex > 1 && (s.charAt(beginIndex) == '/' || s.charAt(beginIndex) == '\\')) {
            b = true;
        }
        String str2 = normalizePath((beginIndex == 0) ? s : s.substring(beginIndex), b2);
        if (!str2.equals("/")) {
            if (str2.endsWith("/")) {
                str2 = str2.substring(0, str2.length() - 1);
            }
            if (str2.endsWith("!")) {
                str2 = str2.substring(0, str2.length() - 1);
            }
            if (str2.endsWith("/")) {
                str2 = str2.substring(0, str2.length() - 1);
            }
            if (str2.isEmpty()) {
                str2 = "/";
            }
        }
        String str3;
        if (b || str == null || str.isEmpty()) {
            str3 = FileUtils.sanitizeEntryPath(str2, false, true);
        }
        else {
            str3 = FileUtils.sanitizeEntryPath(str + (str.endsWith("/") ? "" : "/") + str2, false, true);
        }
        return string.isEmpty() ? str3 : (string + str3);
    }
    
    private static void unescapePercentEncoding(final String s, final int n, final int n2, final StringBuilder sb) {
        if (n2 - n == 3 && s.charAt(n + 1) == '2' && s.charAt(n + 2) == '0') {
            sb.append(' ');
        }
        else {
            final byte[] bytes = new byte[(n2 - n) / 3];
            for (int i = n, n3 = 0; i < n2; i += 3, ++n3) {
                bytes[n3] = (byte)(hexCharToInt(s.charAt(i + 1)) << 4 | hexCharToInt(s.charAt(i + 2)));
            }
            sb.append(new String(bytes, StandardCharsets.UTF_8).replace("/", "%2F").replace("\\", "%5C"));
        }
    }
}
