// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;

class JPMSClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
    }
    
    private JPMSClassLoaderHandler() {
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "jdk.internal.loader.ClassLoaders$AppClassLoader".equals(clazz.getName()) || "jdk.internal.loader.BuiltinClassLoader".equals(clazz.getName());
    }
}
