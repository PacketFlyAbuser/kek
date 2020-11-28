// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class ParentLastDelegationOrderTestClassLoaderHandler implements ClassLoaderHandler
{
    private ParentLastDelegationOrderTestClassLoaderHandler() {
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.add(classLoader, logNode);
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        classpathOrder.addClasspathEntry(ReflectionUtils.invokeMethod(classLoader, "getClasspath", true), classLoader, scanSpec, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "io.github.classgraph.issues.issue267.FakeRestartClassLoader".equals(clazz.getName());
    }
}
