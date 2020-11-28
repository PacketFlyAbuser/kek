// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;

class WeblogicClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        classpathOrder.addClasspathPathStr((String)ReflectionUtils.invokeMethod(classLoader, "getFinderClassPath", false), classLoader, scanSpec, logNode);
        classpathOrder.addClasspathPathStr((String)ReflectionUtils.invokeMethod(classLoader, "getClassPath", false), classLoader, scanSpec, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "weblogic.utils.classloaders.ChangeAwareClassLoader".equals(clazz.getName()) || "weblogic.utils.classloaders.GenericClassLoader".equals(clazz.getName()) || "weblogic.utils.classloaders.FilteringClassLoader".equals(clazz.getName()) || "weblogic.servlet.jsp.JspClassLoader".equals(clazz.getName()) || "weblogic.servlet.jsp.TagFileClassLoader".equals(clazz.getName());
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    private WeblogicClassLoaderHandler() {
    }
}
