// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class FallbackClassLoaderHandler implements ClassLoaderHandler
{
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return true;
    }
    
    private FallbackClassLoaderHandler() {
    }
    
    public static void findClasspathOrder(final ClassLoader obj, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final boolean b = false | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getClassPath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getClasspath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "classpath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "classPath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "cp", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "classpath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "classPath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "cp", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getPath", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getPaths", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "path", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "paths", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "paths", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "paths", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getDir", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getDirs", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "dir", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "dirs", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "dir", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "dirs", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getFile", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getFiles", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "file", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "files", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "file", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "files", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getJar", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getJars", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "jar", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "jars", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "jar", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "jars", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getURL", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getURLs", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getUrl", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "getUrls", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "url", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(obj, "urls", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "url", false), obj, scanSpec, logNode) | classpathOrder.addClasspathEntryObject(ReflectionUtils.getFieldVal(obj, "urls", false), obj, scanSpec, logNode);
        if (logNode != null) {
            logNode.log("FallbackClassLoaderHandler " + (b ? "found" : "did not find") + " classpath entries in unknown ClassLoader " + obj);
        }
    }
}
