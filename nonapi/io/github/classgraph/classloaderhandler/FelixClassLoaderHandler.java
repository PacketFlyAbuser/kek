// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import java.util.HashSet;
import java.util.Iterator;
import java.io.File;
import java.util.List;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Set;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;

class FelixClassLoaderHandler implements ClassLoaderHandler
{
    private static void addBundle(final Object o, final ClassLoader classLoader, final ClasspathOrder classpathOrder, final Set<Object> set, final ScanSpec scanSpec, final LogNode logNode) {
        set.add(o);
        final Object invokeMethod = ReflectionUtils.invokeMethod(o, "getRevision", false);
        final Object invokeMethod2 = ReflectionUtils.invokeMethod(invokeMethod, "getContent", false);
        final File file = (invokeMethod2 != null) ? getContentLocation(invokeMethod2) : null;
        if (file != null) {
            classpathOrder.addClasspathEntry(file, classLoader, scanSpec, logNode);
            final List list = (List)ReflectionUtils.invokeMethod(invokeMethod, "getContentPath", false);
            if (list != null) {
                for (final Object next : list) {
                    if (next != invokeMethod2) {
                        final File file2 = (next != null) ? getContentLocation(next) : null;
                        if (file2 == null) {
                            continue;
                        }
                        classpathOrder.addClasspathEntry(file2, classLoader, scanSpec, logNode);
                    }
                }
            }
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.apache.felix.framework.BundleWiringImpl$BundleClassLoaderJava5".equals(clazz.getName()) || "org.apache.felix.framework.BundleWiringImpl$BundleClassLoader".equals(clazz.getName());
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final HashSet<Object> set = new HashSet<Object>();
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "m_wiring", false);
        addBundle(fieldVal, classLoader, classpathOrder, set, scanSpec, logNode);
        final List list = (List)ReflectionUtils.invokeMethod(fieldVal, "getRequiredWires", String.class, null, false);
        if (list != null) {
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                final Object invokeMethod = ReflectionUtils.invokeMethod(iterator.next(), "getProviderWiring", false);
                if (!set.contains(invokeMethod)) {
                    addBundle(invokeMethod, classLoader, classpathOrder, set, scanSpec, logNode);
                }
            }
        }
    }
    
    private static File getContentLocation(final Object o) {
        return (File)ReflectionUtils.invokeMethod(o, "getFile", false);
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    private FelixClassLoaderHandler() {
    }
}
