// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import java.nio.file.Path;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class JBossClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object invokeMethod = ReflectionUtils.invokeMethod(classLoader, "getModule", false);
        final Object invokeMethod2 = ReflectionUtils.invokeMethod(invokeMethod, "getCallerModuleLoader", false);
        final HashSet<Object> set = new HashSet<Object>();
        final Iterator<Map.Entry<K, Object>> iterator = ((Map)ReflectionUtils.getFieldVal(invokeMethod2, "moduleMap", false)).entrySet().iterator();
        while (iterator.hasNext()) {
            handleRealModule(ReflectionUtils.invokeMethod(iterator.next().getValue(), "getModule", false), set, classLoader, classpathOrder, scanSpec, logNode);
        }
        final Iterator<Map.Entry<K, List>> iterator2 = ((Map)ReflectionUtils.invokeMethod(invokeMethod, "getPaths", false)).entrySet().iterator();
        while (iterator2.hasNext()) {
            final Iterator<Object> iterator3 = iterator2.next().getValue().iterator();
            while (iterator3.hasNext()) {
                handleRealModule(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(iterator3.next(), "this$0", false), "module", false), set, classLoader, classpathOrder, scanSpec, logNode);
            }
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.jboss.modules.ModuleClassLoader".equals(clazz.getName());
    }
    
    private static void handleRealModule(final Object o, final Set<Object> set, final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        if (!set.add(o)) {
            return;
        }
        ClassLoader classLoader2 = (ClassLoader)ReflectionUtils.invokeMethod(o, "getClassLoader", false);
        if (classLoader2 == null) {
            classLoader2 = classLoader;
        }
        final Object invokeMethod = ReflectionUtils.invokeMethod(classLoader2, "getResourceLoaders", false);
        if (invokeMethod != null) {
            for (int i = 0; i < Array.getLength(invokeMethod); ++i) {
                handleResourceLoader(Array.get(invokeMethod, i), classLoader2, classpathOrder, scanSpec, logNode);
            }
        }
    }
    
    private JBossClassLoaderHandler() {
    }
    
    private static void handleResourceLoader(final Object obj, final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        if (obj == null) {
            return;
        }
        final Object fieldVal = ReflectionUtils.getFieldVal(obj, "root", false);
        final File file = (File)ReflectionUtils.invokeMethod(fieldVal, "getPhysicalFile", false);
        String s;
        if (file != null) {
            final String child = (String)ReflectionUtils.invokeMethod(fieldVal, "getName", false);
            if (child != null) {
                final File file2 = new File(file.getParentFile(), child);
                if (FileUtils.canRead(file2)) {
                    s = file2.getAbsolutePath();
                }
                else {
                    s = file.getAbsolutePath();
                }
            }
            else {
                s = file.getAbsolutePath();
            }
        }
        else {
            s = (String)ReflectionUtils.invokeMethod(fieldVal, "getPathName", false);
            if (s == null) {
                final File file3 = (fieldVal instanceof Path) ? ((Path)fieldVal).toFile() : ((fieldVal instanceof File) ? ((File)fieldVal) : null);
                if (file3 != null) {
                    s = file3.getAbsolutePath();
                }
            }
        }
        if (s == null) {
            final File file4 = (File)ReflectionUtils.getFieldVal(obj, "fileOfJar", false);
            if (file4 != null) {
                s = file4.getAbsolutePath();
            }
        }
        if (s != null) {
            classpathOrder.addClasspathEntry(s, classLoader, scanSpec, logNode);
        }
        else if (logNode != null) {
            logNode.log("Could not determine classpath for ResourceLoader: " + obj);
        }
    }
}
