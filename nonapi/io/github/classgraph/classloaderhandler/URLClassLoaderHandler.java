// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import java.net.URL;
import java.net.URLClassLoader;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class URLClassLoaderHandler implements ClassLoaderHandler
{
    private URLClassLoaderHandler() {
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final URL[] urLs = ((URLClassLoader)classLoader).getURLs();
        if (urLs != null) {
            for (final URL url : urLs) {
                if (url != null) {
                    classpathOrder.addClasspathEntry(url, classLoader, scanSpec, logNode);
                }
            }
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "java.net.URLClassLoader".equals(clazz.getName());
    }
}
