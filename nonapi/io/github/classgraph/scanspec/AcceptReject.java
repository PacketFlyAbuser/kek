// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.scanspec;

import nonapi.io.github.classgraph.utils.FileUtils;
import java.util.HashSet;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.List;

public abstract class AcceptReject
{
    protected /* synthetic */ List<String> acceptPrefixes;
    protected transient /* synthetic */ List<Pattern> acceptPatterns;
    protected /* synthetic */ char separatorChar;
    protected /* synthetic */ List<String> rejectPrefixes;
    protected /* synthetic */ Set<String> rejectGlobs;
    protected /* synthetic */ Set<String> acceptGlobs;
    protected transient /* synthetic */ List<Pattern> rejectPatterns;
    protected /* synthetic */ Set<String> reject;
    protected /* synthetic */ Set<String> accept;
    protected /* synthetic */ Set<String> acceptPrefixesSet;
    
    void sortPrefixes() {
        if (this.acceptPrefixesSet != null) {
            this.acceptPrefixes = new ArrayList<String>(this.acceptPrefixesSet);
        }
        if (this.acceptPrefixes != null) {
            CollectionUtils.sortIfNotEmpty(this.acceptPrefixes);
        }
        if (this.rejectPrefixes != null) {
            CollectionUtils.sortIfNotEmpty(this.rejectPrefixes);
        }
    }
    
    public AcceptReject() {
    }
    
    public abstract void addToAccept(final String p0);
    
    public abstract boolean acceptHasPrefix(final String p0);
    
    public boolean acceptAndRejectAreEmpty() {
        return this.acceptIsEmpty() && this.rejectIsEmpty();
    }
    
    public static String normalizePackageOrClassName(final String s) {
        return normalizePath(s.replace('.', '/')).replace('/', '.');
    }
    
    private static void quoteList(final Collection<String> collection, final StringBuilder sb) {
        sb.append('[');
        int n = 1;
        for (final String s : collection) {
            if (n != 0) {
                n = 0;
            }
            else {
                sb.append(", ");
            }
            sb.append('\"');
            for (int i = 0; i < s.length(); ++i) {
                final char char1 = s.charAt(i);
                if (char1 == '\"') {
                    sb.append("\\\"");
                }
                else {
                    sb.append(char1);
                }
            }
            sb.append('\"');
        }
        sb.append(']');
    }
    
    public static String pathToPackageName(final String s) {
        return s.replace('/', '.');
    }
    
    public abstract boolean isRejected(final String p0);
    
    public boolean isSpecificallyAcceptedAndNotRejected(final String s) {
        return !this.acceptIsEmpty() && this.isAcceptedAndNotRejected(s);
    }
    
    public boolean isSpecificallyAccepted(final String s) {
        return !this.acceptIsEmpty() && this.isAccepted(s);
    }
    
    public AcceptReject(final char separatorChar) {
        this.separatorChar = separatorChar;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        if (this.accept != null) {
            sb.append("accept: ");
            quoteList(this.accept, sb);
        }
        if (this.acceptPrefixes != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("acceptPrefixes: ");
            quoteList(this.acceptPrefixes, sb);
        }
        if (this.acceptGlobs != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("acceptGlobs: ");
            quoteList(this.acceptGlobs, sb);
        }
        if (this.reject != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("reject: ");
            quoteList(this.reject, sb);
        }
        if (this.rejectPrefixes != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("rejectPrefixes: ");
            quoteList(this.rejectPrefixes, sb);
        }
        if (this.rejectGlobs != null) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append("rejectGlobs: ");
            quoteList(this.rejectGlobs, sb);
        }
        return sb.toString();
    }
    
    public abstract boolean isAccepted(final String p0);
    
    public static Pattern globToPattern(final String s) {
        return Pattern.compile("^" + s.replace(".", "\\.").replace("*", ".*") + "$");
    }
    
    public static String normalizePath(final String s) {
        String s2;
        for (s2 = FastPathResolver.resolve(s); s2.startsWith("/"); s2 = s2.substring(1)) {}
        return s2;
    }
    
    public boolean acceptIsEmpty() {
        return this.accept == null && this.acceptPrefixes == null && this.acceptGlobs == null;
    }
    
    public static String packageNameToPath(final String s) {
        return s.replace('.', '/');
    }
    
    public abstract void addToReject(final String p0);
    
    private static boolean matchesPatternList(final String input, final List<Pattern> list) {
        if (list != null) {
            final Iterator<Pattern> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().matcher(input).matches()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean rejectIsEmpty() {
        return this.reject == null && this.rejectPrefixes == null && this.rejectGlobs == null;
    }
    
    public static String classNameToClassfilePath(final String s) {
        return JarUtils.classNameToClassfilePath(s);
    }
    
    public abstract boolean isAcceptedAndNotRejected(final String p0);
    
    public static class AcceptRejectLeafname extends AcceptRejectWholeString
    {
        @Override
        public boolean isAcceptedAndNotRejected(final String s) {
            return super.isAcceptedAndNotRejected(JarUtils.leafName(s));
        }
        
        @Override
        public boolean acceptHasPrefix(final String s) {
            throw new IllegalArgumentException("Can only find prefixes of whole strings");
        }
        
        public AcceptRejectLeafname(final char c) {
            super(c);
        }
        
        @Override
        public boolean isAccepted(final String s) {
            return super.isAccepted(JarUtils.leafName(s));
        }
        
        public AcceptRejectLeafname() {
        }
        
        @Override
        public boolean isRejected(final String s) {
            return super.isRejected(JarUtils.leafName(s));
        }
        
        @Override
        public void addToAccept(final String s) {
            super.addToAccept(JarUtils.leafName(s));
        }
        
        @Override
        public void addToReject(final String s) {
            super.addToReject(JarUtils.leafName(s));
        }
    }
    
    public static class AcceptRejectWholeString extends AcceptReject
    {
        @Override
        public boolean isAcceptedAndNotRejected(final String s) {
            return this.isAccepted(s) && !this.isRejected(s);
        }
        
        public AcceptRejectWholeString() {
        }
        
        @Override
        public boolean acceptHasPrefix(final String s) {
            return this.acceptPrefixesSet != null && this.acceptPrefixesSet.contains(s);
        }
        
        @Override
        public boolean isRejected(final String s) {
            return (this.reject != null && this.reject.contains(s)) || matchesPatternList(s, this.rejectPatterns);
        }
        
        public AcceptRejectWholeString(final char c) {
            super(c);
        }
        
        @Override
        public boolean isAccepted(final String s) {
            return (this.accept == null && this.acceptPatterns == null) || (this.accept != null && this.accept.contains(s)) || matchesPatternList(s, this.acceptPatterns);
        }
        
        @Override
        public void addToReject(final String s) {
            if (s.contains("*")) {
                if (this.rejectGlobs == null) {
                    this.rejectGlobs = new HashSet<String>();
                    this.rejectPatterns = new ArrayList<Pattern>();
                }
                this.rejectGlobs.add(s);
                this.rejectPatterns.add(AcceptReject.globToPattern(s));
            }
            else {
                if (this.reject == null) {
                    this.reject = new HashSet<String>();
                }
                this.reject.add(s);
            }
        }
        
        @Override
        public void addToAccept(final String s) {
            if (s.contains("*")) {
                if (this.acceptGlobs == null) {
                    this.acceptGlobs = new HashSet<String>();
                    this.acceptPatterns = new ArrayList<Pattern>();
                }
                this.acceptGlobs.add(s);
                this.acceptPatterns.add(AcceptReject.globToPattern(s));
            }
            else {
                if (this.accept == null) {
                    this.accept = new HashSet<String>();
                }
                this.accept.add(s);
            }
            if (this.acceptPrefixesSet == null) {
                this.acceptPrefixesSet = new HashSet<String>();
                this.acceptPrefixesSet.add("");
                this.acceptPrefixesSet.add("/");
            }
            final String string = Character.toString(this.separatorChar);
            String str = s;
            if (str.contains("*")) {
                final String substring = str.substring(0, str.indexOf(42));
                if (substring.lastIndexOf(this.separatorChar) < 0) {
                    str = "";
                }
                else {
                    str = substring.substring(0, substring.lastIndexOf(this.separatorChar));
                }
            }
            while (str.endsWith(string)) {
                str = str.substring(0, str.length() - 1);
            }
            while (!str.isEmpty()) {
                this.acceptPrefixesSet.add(str + this.separatorChar);
                str = FileUtils.getParentDirPath(str, this.separatorChar);
            }
        }
    }
    
    public static class AcceptRejectPrefix extends AcceptReject
    {
        @Override
        public void addToReject(final String str) {
            if (str.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + str);
            }
            if (this.rejectPrefixes == null) {
                this.rejectPrefixes = new ArrayList<String>();
            }
            this.rejectPrefixes.add(str);
        }
        
        public AcceptRejectPrefix(final char c) {
            super(c);
        }
        
        @Override
        public boolean acceptHasPrefix(final String s) {
            throw new IllegalArgumentException("Can only find prefixes of whole strings");
        }
        
        @Override
        public boolean isAcceptedAndNotRejected(final String s) {
            int n = (this.acceptPrefixes == null) ? 1 : 0;
            if (n == 0) {
                final Iterator<String> iterator = this.acceptPrefixes.iterator();
                while (iterator.hasNext()) {
                    if (s.startsWith(iterator.next())) {
                        n = 1;
                        break;
                    }
                }
            }
            if (n == 0) {
                return false;
            }
            if (this.rejectPrefixes != null) {
                final Iterator<String> iterator2 = this.rejectPrefixes.iterator();
                while (iterator2.hasNext()) {
                    if (s.startsWith(iterator2.next())) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        public boolean isRejected(final String s) {
            if (this.rejectPrefixes != null) {
                final Iterator<String> iterator = this.rejectPrefixes.iterator();
                while (iterator.hasNext()) {
                    if (s.startsWith(iterator.next())) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public AcceptRejectPrefix() {
        }
        
        @Override
        public void addToAccept(final String str) {
            if (str.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + str);
            }
            if (this.acceptPrefixesSet == null) {
                this.acceptPrefixesSet = new HashSet<String>();
            }
            this.acceptPrefixesSet.add(str);
        }
        
        @Override
        public boolean isAccepted(final String s) {
            boolean b = this.acceptPrefixes == null;
            if (!b) {
                final Iterator<String> iterator = this.acceptPrefixes.iterator();
                while (iterator.hasNext()) {
                    if (s.startsWith(iterator.next())) {
                        b = true;
                        break;
                    }
                }
            }
            return b;
        }
    }
}
