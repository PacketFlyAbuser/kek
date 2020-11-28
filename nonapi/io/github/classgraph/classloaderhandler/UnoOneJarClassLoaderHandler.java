// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;

class UnoOneJarClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        classpathOrder.addClasspathEntry(ReflectionUtils.invokeMethod(classLoader, "getOneJarPath", false), classLoader, scanSpec, logNode);
        classpathOrder.addClasspathEntry(System.getProperty("uno-jar.jar.path"), classLoader, scanSpec, logNode);
        classpathOrder.addClasspathEntry(System.getProperty("one-jar.jar.path"), classLoader, scanSpec, logNode);
        final String property = System.getProperty("one-jar.class.path");
        if (property != null) {
            classpathOrder.addClasspathEntryObject(property.split("\\|"), classLoader, scanSpec, logNode);
        }
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    private UnoOneJarClassLoaderHandler() {
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "com.needhamsoftware.unojar.JarClassLoader".equals(clazz.getName()) || "com.simontuffs.onejar.JarClassLoader".equals(clazz.getName());
    }
}
