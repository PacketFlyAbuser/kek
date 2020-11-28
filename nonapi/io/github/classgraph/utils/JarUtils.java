// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.regex.Matcher;
import io.github.classgraph.ClassGraphException;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.io.File;
import java.util.regex.Pattern;

public final class JarUtils
{
    private static final /* synthetic */ Pattern DASH_VERSION;
    private static final /* synthetic */ String[] UNIX_NON_PATH_SEPARATORS;
    private static final /* synthetic */ Pattern LEADING_DOTS;
    private static final /* synthetic */ Pattern NON_ALPHANUM;
    private static final /* synthetic */ Pattern REPEATING_DOTS;
    private static final /* synthetic */ Pattern TRAILING_DOTS;
    private static final /* synthetic */ int[] UNIX_NON_PATH_SEPARATOR_COLON_POSITIONS;
    
    public static String leafName(final String s) {
        final int index = s.indexOf(33);
        final int b = (index >= 0) ? index : s.length();
        final int a = 1 + ((File.separatorChar == '/') ? s.lastIndexOf(47, b) : Math.max(s.lastIndexOf(47, b), s.lastIndexOf(File.separatorChar, b)));
        int index2 = s.indexOf("---");
        if (index2 >= 0) {
            index2 += "---".length();
        }
        return s.substring(Math.min(Math.max(a, index2), b), b);
    }
    
    public static String[] smartPathSplit(final String s, final ScanSpec scanSpec) {
        return smartPathSplit(s, File.pathSeparatorChar, scanSpec);
    }
    
    public static String pathElementsToPathStr(final Object... array) {
        final StringBuilder sb = new StringBuilder();
        for (int length = array.length, i = 0; i < length; ++i) {
            appendPathElt(array[i], sb);
        }
        return sb.toString();
    }
    
    static {
        URL_SCHEME_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9+-.]+[:].*");
        DASH_VERSION = Pattern.compile("-(\\d+(\\.|$))");
        NON_ALPHANUM = Pattern.compile("[^A-Za-z0-9]");
        REPEATING_DOTS = Pattern.compile("(\\.)(\\1)+");
        LEADING_DOTS = Pattern.compile("^\\.");
        TRAILING_DOTS = Pattern.compile("\\.$");
        UNIX_NON_PATH_SEPARATORS = new String[] { "jar:", "file:", "http://", "https://", "\\:" };
        UNIX_NON_PATH_SEPARATOR_COLON_POSITIONS = new int[JarUtils.UNIX_NON_PATH_SEPARATORS.length];
        for (int i = 0; i < JarUtils.UNIX_NON_PATH_SEPARATORS.length; ++i) {
            JarUtils.UNIX_NON_PATH_SEPARATOR_COLON_POSITIONS[i] = JarUtils.UNIX_NON_PATH_SEPARATORS[i].indexOf(58);
            if (JarUtils.UNIX_NON_PATH_SEPARATOR_COLON_POSITIONS[i] < 0) {
                throw ClassGraphException.newClassGraphException("Could not find ':' in \"" + JarUtils.UNIX_NON_PATH_SEPARATORS[i] + "\"");
            }
        }
    }
    
    public static String classNameToClassfilePath(final String s) {
        return s.replace('.', '/') + ".class";
    }
    
    public static String classfilePathToClassName(final String str) {
        if (!str.endsWith(".class")) {
            throw new IllegalArgumentException("Classfile path does not end with \".class\": " + str);
        }
        return str.substring(0, str.length() - 6).replace('/', '.');
    }
    
    private static void appendPathElt(final Object o, final StringBuilder sb) {
        if (sb.length() > 0) {
            sb.append(File.pathSeparatorChar);
        }
        sb.append((File.separatorChar == '\\') ? o.toString() : o.toString().replaceAll(File.pathSeparator, "\\" + File.pathSeparator));
    }
    
    public static String derivedAutomaticModuleName(final String s) {
        int length = s.length();
        final int lastIndex = s.lastIndexOf(33);
        if (lastIndex > 0 && s.lastIndexOf(46) <= Math.max(lastIndex, s.lastIndexOf(47))) {
            length = lastIndex;
        }
        final int beginIndex = Math.max((length == 0) ? -1 : s.lastIndexOf("!", length - 1), s.lastIndexOf(47, length - 1)) + 1;
        final int lastIndex2 = s.lastIndexOf(46, length - 1);
        if (lastIndex2 > beginIndex) {
            length = lastIndex2;
        }
        String s2 = s.substring(beginIndex, length);
        final Matcher matcher = JarUtils.DASH_VERSION.matcher(s2);
        if (matcher.find()) {
            s2 = s2.substring(0, matcher.start());
        }
        String s3 = JarUtils.REPEATING_DOTS.matcher(JarUtils.NON_ALPHANUM.matcher(s2).replaceAll(".")).replaceAll(".");
        if (s3.length() > 0 && s3.charAt(0) == '.') {
            s3 = JarUtils.LEADING_DOTS.matcher(s3).replaceAll("");
        }
        final int length2 = s3.length();
        if (length2 > 0 && s3.charAt(length2 - 1) == '.') {
            s3 = JarUtils.TRAILING_DOTS.matcher(s3).replaceAll("");
        }
        return s3;
    }
    
    public static String[] smartPathSplit(final String s, final char c, final ScanSpec scanSpec) {
        if (s == null || s.isEmpty()) {
            return new String[0];
        }
        if (c != ':') {
            final ArrayList<String> list = new ArrayList<String>();
            final String[] split = s.split(String.valueOf(c));
            for (int length = split.length, i = 0; i < length; ++i) {
                final String trim = split[i].trim();
                if (!trim.isEmpty()) {
                    list.add(trim);
                }
            }
            return list.toArray(new String[0]);
        }
        final HashSet<Integer> c2 = new HashSet<Integer>();
        int index = -1;
        while (true) {
            int n = 0;
            for (int j = 0; j < JarUtils.UNIX_NON_PATH_SEPARATORS.length; ++j) {
                final int toffset = index - JarUtils.UNIX_NON_PATH_SEPARATOR_COLON_POSITIONS[j];
                if (s.regionMatches(true, toffset, JarUtils.UNIX_NON_PATH_SEPARATORS[j], 0, JarUtils.UNIX_NON_PATH_SEPARATORS[j].length()) && (toffset == 0 || s.charAt(toffset - 1) == ':')) {
                    n = 1;
                    break;
                }
            }
            if (n == 0 && scanSpec != null && scanSpec.allowedURLSchemes != null && !scanSpec.allowedURLSchemes.isEmpty()) {
                for (final String other : scanSpec.allowedURLSchemes) {
                    if (!other.equals("http") && !other.equals("https") && !other.equals("jar") && !other.equals("file")) {
                        final int length2 = other.length();
                        final int toffset2 = index - length2;
                        if (s.regionMatches(true, toffset2, other, 0, length2) && (toffset2 == 0 || s.charAt(toffset2 - 1) == ':')) {
                            n = 1;
                            break;
                        }
                        continue;
                    }
                }
            }
            if (n == 0) {
                c2.add(index);
            }
            index = s.indexOf(58, index + 1);
            if (index < 0) {
                break;
            }
        }
        c2.add(s.length());
        final ArrayList list2 = new ArrayList<Integer>(c2);
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list2);
        final ArrayList<String> list3 = new ArrayList<String>();
        for (int k = 1; k < list2.size(); ++k) {
            final String replaceAll = s.substring(list2.get(k - 1) + 1, list2.get(k)).trim().replaceAll("\\\\:", ":");
            if (!replaceAll.isEmpty()) {
                list3.add(replaceAll);
            }
        }
        return list3.toArray(new String[0]);
    }
    
    private JarUtils() {
    }
    
    public static String pathElementsToPathStr(final Iterable<?> iterable) {
        final StringBuilder sb = new StringBuilder();
        final Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            appendPathElt(iterator.next(), sb);
        }
        return sb.toString();
    }
}
