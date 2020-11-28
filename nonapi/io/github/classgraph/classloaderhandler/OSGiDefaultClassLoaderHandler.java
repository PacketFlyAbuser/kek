// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import java.io.File;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class OSGiDefaultClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object[] array = (Object[])ReflectionUtils.getFieldVal(ReflectionUtils.invokeMethod(classLoader, "getClasspathManager", false), "entries", false);
        if (array != null) {
            final Object[] array2 = array;
            for (int length = array2.length, i = 0; i < length; ++i) {
                final File file = (File)ReflectionUtils.invokeMethod(ReflectionUtils.invokeMethod(array2[i], "getBundleFile", false), "getBaseFile", false);
                if (file != null) {
                    classpathOrder.addClasspathEntry(file.getPath(), classLoader, scanSpec, logNode);
                }
            }
        }
    }
    
    private OSGiDefaultClassLoaderHandler() {
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader".equals(clazz.getName());
    }
}
