// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;

class WebsphereTraditionalClassLoaderHandler implements ClassLoaderHandler
{
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "com.ibm.ws.classloader.CompoundClassLoader".equals(clazz.getName()) || "com.ibm.ws.classloader.ProtectionClassLoader".equals(clazz.getName()) || "com.ibm.ws.bootstrap.ExtClassLoader".equals(clazz.getName());
    }
    
    private WebsphereTraditionalClassLoaderHandler() {
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        classpathOrder.addClasspathPathStr((String)ReflectionUtils.invokeMethod(classLoader, "getClassPath", false), classLoader, scanSpec, logNode);
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
}
