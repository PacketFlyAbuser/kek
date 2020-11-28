// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import java.nio.file.Path;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.util.Collection;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;

class QuarkusClassLoaderHandler implements ClassLoaderHandler
{
    private static void findClasspathOrderForQuarkusClassloader(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        for (final Object next : (Collection)ReflectionUtils.getFieldVal(classLoader, "elements", false)) {
            final String name = next.getClass().getName();
            if ("io.quarkus.bootstrap.classloading.JarClassPathElement".equals(name)) {
                classpathOrder.addClasspathEntry(ReflectionUtils.getFieldVal(next, "file", false), classLoader, scanSpec, logNode);
            }
            else {
                if (!"io.quarkus.bootstrap.classloading.DirectoryClassPathElement".equals(name)) {
                    continue;
                }
                classpathOrder.addClasspathEntry(ReflectionUtils.getFieldVal(next, "root", false), classLoader, scanSpec, logNode);
            }
        }
    }
    
    private static void findClasspathOrderForRuntimeClassloader(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Collection collection = (Collection)ReflectionUtils.getFieldVal(classLoader, "applicationClassDirectories", false);
        if (collection != null) {
            final Iterator<Path> iterator = collection.iterator();
            while (iterator.hasNext()) {
                classpathOrder.addClasspathEntryObject(iterator.next().toUri(), classLoader, scanSpec, logNode);
            }
        }
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    static {
        QUARKUS_CLASSLOADER = "io.quarkus.bootstrap.classloading.QuarkusClassLoader";
        RUNTIME_CLASSLOADER = "io.quarkus.runner.RuntimeClassLoader";
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final String name = classLoader.getClass().getName();
        if ("io.quarkus.runner.RuntimeClassLoader".equals(name)) {
            findClasspathOrderForRuntimeClassloader(classLoader, classpathOrder, scanSpec, logNode);
        }
        else if ("io.quarkus.bootstrap.classloading.QuarkusClassLoader".equals(name)) {
            findClasspathOrderForQuarkusClassloader(classLoader, classpathOrder, scanSpec, logNode);
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "io.quarkus.runner.RuntimeClassLoader".equals(clazz.getName()) || "io.quarkus.bootstrap.classloading.QuarkusClassLoader".equals(clazz.getName());
    }
    
    private QuarkusClassLoaderHandler() {
    }
}
