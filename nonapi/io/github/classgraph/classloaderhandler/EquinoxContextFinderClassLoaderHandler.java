// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class EquinoxContextFinderClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo((ClassLoader)ReflectionUtils.getFieldVal(classLoader, "parentContextClassLoader", false), true, logNode);
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.eclipse.osgi.internal.framework.ContextFinder".equals(clazz.getName());
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
    }
    
    private EquinoxContextFinderClassLoaderHandler() {
    }
}
