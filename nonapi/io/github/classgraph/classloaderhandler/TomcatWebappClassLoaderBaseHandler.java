// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import java.util.Iterator;
import java.io.File;
import java.util.List;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.ReflectionUtils;

class TomcatWebappClassLoaderBaseHandler implements ClassLoaderHandler
{
    private TomcatWebappClassLoaderBaseHandler() {
    }
    
    private static boolean isParentFirst(final ClassLoader classLoader) {
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "delegate", false);
        return fieldVal == null || (boolean)fieldVal;
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        final boolean parentFirst = isParentFirst(classLoader);
        if (parentFirst) {
            classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        }
        classLoaderOrder.add(classLoader, logNode);
        if (!parentFirst) {
            classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        }
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object invokeMethod = ReflectionUtils.invokeMethod(classLoader, "getResources", false);
        classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(invokeMethod, "getBaseUrls", false), classLoader, scanSpec, logNode);
        final List list = (List)ReflectionUtils.getFieldVal(invokeMethod, "allResources", false);
        if (list != null) {
            final Iterator<List<Object>> iterator = list.iterator();
            while (iterator.hasNext()) {
                for (final Object next : iterator.next()) {
                    if (next != null) {
                        final File file = (File)ReflectionUtils.invokeMethod(next, "getFileBase", false);
                        String string = (file == null) ? null : file.getPath();
                        if (string == null) {
                            string = (String)ReflectionUtils.invokeMethod(next, "getBase", false);
                        }
                        if (string == null) {
                            string = (String)ReflectionUtils.invokeMethod(next, "getBaseUrlString", false);
                        }
                        if (string == null) {
                            continue;
                        }
                        final String str = (String)ReflectionUtils.getFieldVal(next, "archivePath", false);
                        if (str != null && !str.isEmpty()) {
                            string = string + "!" + (str.startsWith("/") ? str : ("/" + str));
                        }
                        final String name = next.getClass().getName();
                        final boolean b = name.equals("java.org.apache.catalina.webresources.JarResourceSet") || name.equals("java.org.apache.catalina.webresources.JarWarResourceSet");
                        final String str2 = (String)ReflectionUtils.invokeMethod(next, "getInternalPath", false);
                        if (str2 != null && !str2.isEmpty() && !str2.equals("/")) {
                            classpathOrder.addClasspathEntryObject(string + (b ? "!" : "") + (str2.startsWith("/") ? str2 : ("/" + str2)), classLoader, scanSpec, logNode);
                        }
                        else {
                            classpathOrder.addClasspathEntryObject(string, classLoader, scanSpec, logNode);
                        }
                    }
                }
            }
        }
        classpathOrder.addClasspathEntryObject(ReflectionUtils.invokeMethod(classLoader, "getURLs", false), classLoader, scanSpec, logNode);
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.apache.catalina.loader.WebappClassLoaderBase".equals(clazz.getName());
    }
}
