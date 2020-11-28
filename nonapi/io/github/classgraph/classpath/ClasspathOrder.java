// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.util.Objects;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import java.util.ArrayList;
import java.util.HashSet;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.nio.file.InvalidPathException;
import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.lang.reflect.Array;
import java.io.File;
import java.nio.file.Path;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import io.github.classgraph.ClassGraph;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Set;
import java.util.List;

public class ClasspathOrder
{
    private final /* synthetic */ List<ClasspathElementAndClassLoader> order;
    private final /* synthetic */ Set<String> classpathEntryUniqueResolvedPaths;
    private final /* synthetic */ ScanSpec scanSpec;
    private static final /* synthetic */ List<String> AUTOMATIC_PACKAGE_ROOT_SUFFIXES;
    
    public boolean addClasspathPathStr(final String s, final ClassLoader classLoader, final ScanSpec scanSpec, final LogNode logNode) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        final String[] smartPathSplit = JarUtils.smartPathSplit(s, scanSpec);
        if (smartPathSplit.length == 0) {
            return false;
        }
        final String[] array = smartPathSplit;
        for (int length = array.length, i = 0; i < length; ++i) {
            this.addClasspathEntry(array[i], classLoader, scanSpec, logNode);
        }
        return true;
    }
    
    private boolean filter(final String s) {
        if (this.scanSpec.classpathElementFilters != null) {
            final Iterator<ClassGraph.ClasspathElementFilter> iterator = this.scanSpec.classpathElementFilters.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().includeClasspathElement(s)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public List<ClasspathElementAndClassLoader> getOrder() {
        return this.order;
    }
    
    public boolean addClasspathEntryObject(final Object o, final ClassLoader classLoader, final ScanSpec scanSpec, final LogNode logNode) {
        boolean b = false;
        if (o != null) {
            if (o instanceof URL || o instanceof URI || o instanceof Path || o instanceof File) {
                b |= this.addClasspathEntry(o, classLoader, scanSpec, logNode);
            }
            else if (o instanceof Iterable) {
                final Iterator<Object> iterator = (Iterator<Object>)((Iterable)o).iterator();
                while (iterator.hasNext()) {
                    b |= this.addClasspathEntryObject(iterator.next(), classLoader, scanSpec, logNode);
                }
            }
            else if (o.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(o); ++i) {
                    b |= this.addClasspathEntryObject(Array.get(o, i), classLoader, scanSpec, logNode);
                }
            }
            else {
                b |= this.addClasspathPathStr(o.toString(), classLoader, scanSpec, logNode);
            }
        }
        return b;
    }
    
    private boolean addClasspathEntry(final Object o, final String s, final ClassLoader classLoader, final ScanSpec scanSpec) {
        String substring = s;
        boolean b = false;
        for (final String suffix : ClasspathOrder.AUTOMATIC_PACKAGE_ROOT_SUFFIXES) {
            if (s.endsWith(suffix)) {
                substring = s.substring(0, s.length() - suffix.length());
                b = true;
                break;
            }
        }
        if (o instanceof URL || o instanceof URI || o instanceof Path || o instanceof File) {
            Object o2 = o;
            if (b) {
                try {
                    o2 = ((o instanceof URL) ? new URL(substring) : ((o instanceof URI) ? new URI(substring) : ((o instanceof Path) ? Paths.get(substring, new String[0]) : substring)));
                }
                catch (MalformedURLException | URISyntaxException | InvalidPathException ex) {
                    return false;
                }
            }
            if (this.classpathEntryUniqueResolvedPaths.add(substring)) {
                this.order.add(new ClasspathElementAndClassLoader(o2, classLoader));
                return true;
            }
        }
        else {
            final String resolve = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, substring);
            if (scanSpec.overrideClasspath == null && (SystemJarFinder.getJreLibOrExtJars().contains(resolve) || resolve.equals(SystemJarFinder.getJreRtJarPath()))) {
                return false;
            }
            if (this.classpathEntryUniqueResolvedPaths.add(resolve)) {
                this.order.add(new ClasspathElementAndClassLoader(resolve, classLoader));
                return true;
            }
        }
        return false;
    }
    
    boolean addSystemClasspathEntry(final String s, final ClassLoader classLoader) {
        if (this.classpathEntryUniqueResolvedPaths.add(s)) {
            this.order.add(new ClasspathElementAndClassLoader(s, classLoader));
            return true;
        }
        return false;
    }
    
    public Set<String> getClasspathEntryUniqueResolvedPaths() {
        return this.classpathEntryUniqueResolvedPaths;
    }
    
    ClasspathOrder(final ScanSpec scanSpec) {
        this.classpathEntryUniqueResolvedPaths = new HashSet<String>();
        this.order = new ArrayList<ClasspathElementAndClassLoader>();
        this.scanSpec = scanSpec;
    }
    
    static {
        AUTOMATIC_PACKAGE_ROOT_SUFFIXES = new ArrayList<String>();
        for (final String s : ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES) {
            ClasspathOrder.AUTOMATIC_PACKAGE_ROOT_SUFFIXES.add("!/" + s.substring(0, s.length() - 1));
        }
    }
    
    public boolean addClasspathEntries(final List<Object> list, final ClassLoader classLoader, final ScanSpec scanSpec, final LogNode logNode) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addClasspathEntry(iterator.next(), classLoader, scanSpec, logNode);
        }
        return true;
    }
    
    public boolean addClasspathEntry(final Object o, final ClassLoader classLoader, final ScanSpec scanSpec, final LogNode logNode) {
        if (o == null) {
            return false;
        }
        final String resolve = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, o.toString());
        if (resolve.isEmpty()) {
            return false;
        }
        if (o instanceof URL || o instanceof URI || o instanceof File) {
            if (!this.filter(resolve)) {
                if (logNode != null) {
                    logNode.log("Classpath element did not match filter criterion, skipping: " + resolve);
                }
                return false;
            }
            Object o2;
            try {
                o2 = ((o instanceof File) ? resolve : ((o instanceof URI) ? ((URI)o).toURL() : o));
            }
            catch (MalformedURLException ex) {
                if (logNode != null) {
                    logNode.log("Cannot convert from URI to URL: " + resolve);
                }
                return false;
            }
            if (this.addClasspathEntry(o2, resolve, classLoader, scanSpec)) {
                if (logNode != null) {
                    logNode.log("Found classpath element: " + resolve);
                }
                return true;
            }
            if (logNode != null) {
                logNode.log("Ignoring duplicate classpath element: " + resolve);
            }
            return false;
        }
        else if (resolve.endsWith("*")) {
            if (resolve.length() != 1 && (resolve.length() <= 2 || resolve.charAt(resolve.length() - 1) != '*' || (resolve.charAt(resolve.length() - 2) != File.separatorChar && (File.separatorChar == '/' || resolve.charAt(resolve.length() - 2) != '/')))) {
                if (logNode != null) {
                    logNode.log("Wildcard classpath elements can only end with a leaf of \"*\", can't have a partial name and then a wildcard: " + resolve);
                }
                return false;
            }
            final String anObject = (resolve.length() == 1) ? "" : resolve.substring(0, resolve.length() - 2);
            final String resolve2 = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, anObject);
            if (!this.filter(anObject) || (!resolve2.equals(anObject) && !this.filter(resolve2))) {
                if (logNode != null) {
                    logNode.log("Classpath element did not match filter criterion, skipping: " + resolve);
                }
                return false;
            }
            final File file = new File(resolve2);
            if (!file.exists()) {
                if (logNode != null) {
                    logNode.log("Directory does not exist for wildcard classpath element: " + resolve);
                }
                return false;
            }
            if (!FileUtils.canRead(file)) {
                if (logNode != null) {
                    logNode.log("Cannot read directory for wildcard classpath element: " + resolve);
                }
                return false;
            }
            if (!file.isDirectory()) {
                if (logNode != null) {
                    logNode.log("Wildcard is appended to something other than a directory: " + resolve);
                }
                return false;
            }
            final LogNode logNode2 = (logNode == null) ? null : logNode.log("Adding classpath elements from wildcarded directory: " + resolve);
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (final File file2 : listFiles) {
                    final String name = file2.getName();
                    if (!name.equals(".") && !name.equals("..")) {
                        final String path = file2.getPath();
                        final String resolve3 = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, path);
                        if (this.addClasspathEntry(resolve3, resolve3, classLoader, scanSpec)) {
                            if (logNode2 != null) {
                                logNode2.log("Found classpath element: " + path + (path.equals(resolve3) ? "" : (" -> " + resolve3)));
                            }
                        }
                        else if (logNode2 != null) {
                            logNode2.log("Ignoring duplicate classpath element: " + path + (path.equals(resolve3) ? "" : (" -> " + resolve3)));
                        }
                    }
                }
                return true;
            }
            return false;
        }
        else {
            final String resolve4 = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, resolve);
            if (!this.filter(resolve) || (!resolve4.equals(resolve) && !this.filter(resolve4))) {
                if (logNode != null) {
                    logNode.log("Classpath element did not match filter criterion, skipping: " + resolve + (resolve.equals(resolve4) ? "" : (" -> " + resolve4)));
                }
                return false;
            }
            if (this.addClasspathEntry(resolve4, resolve4, classLoader, scanSpec)) {
                if (logNode != null) {
                    logNode.log("Found classpath element: " + resolve + (resolve.equals(resolve4) ? "" : (" -> " + resolve4)));
                }
                return true;
            }
            if (logNode != null) {
                logNode.log("Ignoring duplicate classpath element: " + resolve + (resolve.equals(resolve4) ? "" : (" -> " + resolve4)));
            }
            return false;
        }
    }
    
    public static class ClasspathElementAndClassLoader
    {
        public final /* synthetic */ ClassLoader classLoader;
        public final /* synthetic */ String dirOrPathPackageRoot;
        public final /* synthetic */ Object classpathElementRoot;
        
        @Override
        public String toString() {
            return this.classpathElementRoot + " [" + this.classLoader + "]";
        }
        
        public ClasspathElementAndClassLoader(final Object classpathElementRoot, final String dirOrPathPackageRoot, final ClassLoader classLoader) {
            this.classpathElementRoot = classpathElementRoot;
            this.dirOrPathPackageRoot = dirOrPathPackageRoot;
            this.classLoader = classLoader;
        }
        
        public ClasspathElementAndClassLoader(final Object classpathElementRoot, final ClassLoader classLoader) {
            this.classpathElementRoot = classpathElementRoot;
            this.dirOrPathPackageRoot = "";
            this.classLoader = classLoader;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ClasspathElementAndClassLoader)) {
                return false;
            }
            final ClasspathElementAndClassLoader classpathElementAndClassLoader = (ClasspathElementAndClassLoader)o;
            return Objects.equals(this.dirOrPathPackageRoot, classpathElementAndClassLoader.dirOrPathPackageRoot) && Objects.equals(this.classpathElementRoot, classpathElementAndClassLoader.classpathElementRoot) && Objects.equals(this.classLoader, classpathElementAndClassLoader.classLoader);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.classpathElementRoot, this.dirOrPathPackageRoot, this.classLoader);
        }
    }
}
