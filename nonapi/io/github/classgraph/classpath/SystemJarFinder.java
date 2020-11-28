// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.VersionFinder;
import java.util.LinkedHashSet;
import java.io.IOException;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.io.File;
import java.util.Set;

public final class SystemJarFinder
{
    private static final /* synthetic */ String RT_JAR;
    private static final /* synthetic */ Set<String> JRE_LIB_OR_EXT_JARS;
    private static final /* synthetic */ Set<String> RT_JARS;
    
    public static Set<String> getJreLibOrExtJars() {
        return SystemJarFinder.JRE_LIB_OR_EXT_JARS;
    }
    
    public static String getJreRtJarPath() {
        return SystemJarFinder.RT_JAR;
    }
    
    private static boolean addJREPath(final File file) {
        if (file != null && !file.getPath().isEmpty() && FileUtils.canReadAndIsDir(file)) {
            final File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (final File file2 : listFiles) {
                    final String path = file2.getPath();
                    if (path.endsWith(".jar")) {
                        final String resolve = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, path);
                        if (resolve.endsWith("/rt.jar")) {
                            SystemJarFinder.RT_JARS.add(resolve);
                        }
                        else {
                            SystemJarFinder.JRE_LIB_OR_EXT_JARS.add(resolve);
                        }
                        try {
                            if (!file2.getCanonicalFile().getPath().equals(path)) {
                                SystemJarFinder.JRE_LIB_OR_EXT_JARS.add(FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, path));
                            }
                        }
                        catch (IOException ex) {}
                        catch (SecurityException ex2) {}
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    private SystemJarFinder() {
    }
    
    static {
        RT_JARS = new LinkedHashSet<String>();
        JRE_LIB_OR_EXT_JARS = new LinkedHashSet<String>();
        String pathname = VersionFinder.getProperty("java.home");
        if (pathname == null || pathname.isEmpty()) {
            pathname = System.getenv("JAVA_HOME");
        }
        if (pathname != null && !pathname.isEmpty()) {
            final File file = new File(pathname);
            addJREPath(file);
            if (file.getName().equals("jre")) {
                final File parentFile = file.getParentFile();
                addJREPath(parentFile);
                addJREPath(new File(parentFile, "lib"));
                addJREPath(new File(parentFile, "lib/ext"));
            }
            else {
                addJREPath(new File(file, "jre"));
            }
            addJREPath(new File(file, "lib"));
            addJREPath(new File(file, "lib/ext"));
            addJREPath(new File(file, "jre/lib"));
            addJREPath(new File(file, "jre/lib/ext"));
            addJREPath(new File(file, "packages"));
            addJREPath(new File(file, "packages/lib"));
            addJREPath(new File(file, "packages/lib/ext"));
        }
        final String property = VersionFinder.getProperty("java.ext.dirs");
        if (property != null && !property.isEmpty()) {
            for (final String pathname2 : JarUtils.smartPathSplit(property, null)) {
                if (!pathname2.isEmpty()) {
                    addJREPath(new File(pathname2));
                }
            }
        }
        switch (VersionFinder.OS) {
            case Linux:
            case Unix:
            case BSD:
            case Unknown: {
                addJREPath(new File("/usr/java/packages"));
                addJREPath(new File("/usr/java/packages/lib"));
                addJREPath(new File("/usr/java/packages/lib/ext"));
                break;
            }
            case MacOSX: {
                addJREPath(new File("/System/Library/Java"));
                addJREPath(new File("/System/Library/Java/Libraries"));
                addJREPath(new File("/System/Library/Java/Extensions"));
                break;
            }
            case Windows: {
                final String s = (File.separatorChar == '\\') ? System.getenv("SystemRoot") : null;
                if (s != null) {
                    addJREPath(new File(s, "Sun\\Java"));
                    addJREPath(new File(s, "Sun\\Java\\lib"));
                    addJREPath(new File(s, "Sun\\Java\\lib\\ext"));
                    addJREPath(new File(s, "Oracle\\Java"));
                    addJREPath(new File(s, "Oracle\\Java\\lib"));
                    addJREPath(new File(s, "Oracle\\Java\\lib\\ext"));
                    break;
                }
                break;
            }
            case Solaris: {
                addJREPath(new File("/usr/jdk/packages"));
                addJREPath(new File("/usr/jdk/packages/lib"));
                addJREPath(new File("/usr/jdk/packages/lib/ext"));
                break;
            }
        }
        RT_JAR = (SystemJarFinder.RT_JARS.isEmpty() ? null : FastPathResolver.resolve(SystemJarFinder.RT_JARS.iterator().next()));
    }
}
