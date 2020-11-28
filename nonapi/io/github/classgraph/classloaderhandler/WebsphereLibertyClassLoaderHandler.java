// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashSet;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.util.Collection;

class WebsphereLibertyClassLoaderHandler implements ClassLoaderHandler
{
    private static Collection<Object> callGetUrls(final Object o, final String s) {
        if (o != null) {
            try {
                final Collection collection = (Collection)ReflectionUtils.invokeMethod(o, s, false);
                if (collection != null && !collection.isEmpty()) {
                    final HashSet<Collection<Collection>> set = (HashSet<Collection<Collection>>)new HashSet<Object>();
                    for (final Collection<Collection> next : collection) {
                        if (next instanceof Collection) {
                            for (final Collection<Collection> next2 : next) {
                                if (next2 != null) {
                                    set.add(next2);
                                }
                            }
                        }
                        else {
                            if (next == null) {
                                continue;
                            }
                            set.add(next);
                        }
                    }
                    return (Collection<Object>)set;
                }
            }
            catch (UnsupportedOperationException ex) {}
        }
        return Collections.emptyList();
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "appLoader", false);
        Object o;
        if (fieldVal != null) {
            o = ReflectionUtils.getFieldVal(fieldVal, "smartClassPath", false);
        }
        else {
            o = ReflectionUtils.getFieldVal(classLoader, "smartClassPath", false);
        }
        if (o != null) {
            final Collection<Object> callGetUrls = callGetUrls(o, "getClassPath");
            if (!callGetUrls.isEmpty()) {
                final Iterator<Object> iterator = callGetUrls.iterator();
                while (iterator.hasNext()) {
                    classpathOrder.addClasspathEntry(iterator.next(), classLoader, scanSpec, logNode);
                }
            }
            else {
                final List list = (List)ReflectionUtils.getFieldVal(o, "classPath", false);
                if (list != null && !list.isEmpty()) {
                    final Iterator<Object> iterator2 = list.iterator();
                    while (iterator2.hasNext()) {
                        final Iterator<Object> iterator3 = getPaths(iterator2.next()).iterator();
                        while (iterator3.hasNext()) {
                            classpathOrder.addClasspathEntry(iterator3.next(), classLoader, scanSpec, logNode);
                        }
                    }
                }
            }
        }
    }
    
    private static Collection<Object> getPaths(final Object o) {
        if (o == null) {
            return Collections.emptyList();
        }
        final Collection<Object> callGetUrls = callGetUrls(o, "getContainerURLs");
        if (callGetUrls != null && !callGetUrls.isEmpty()) {
            return callGetUrls;
        }
        final Object fieldVal = ReflectionUtils.getFieldVal(o, "container", false);
        if (fieldVal == null) {
            return Collections.emptyList();
        }
        final Collection<Object> callGetUrls2 = callGetUrls(fieldVal, "getURLs");
        if (callGetUrls2 != null && !callGetUrls2.isEmpty()) {
            return callGetUrls2;
        }
        final Object fieldVal2 = ReflectionUtils.getFieldVal(fieldVal, "delegate", false);
        if (fieldVal2 == null) {
            return Collections.emptyList();
        }
        final String s = (String)ReflectionUtils.getFieldVal(fieldVal2, "path", false);
        if (s != null && s.length() > 0) {
            return Arrays.asList(s);
        }
        final Object fieldVal3 = ReflectionUtils.getFieldVal(fieldVal2, "base", false);
        if (fieldVal3 == null) {
            return Collections.emptyList();
        }
        final Object fieldVal4 = ReflectionUtils.getFieldVal(fieldVal3, "archiveFile", false);
        if (fieldVal4 != null) {
            return Arrays.asList(((File)fieldVal4).getAbsolutePath());
        }
        return Collections.emptyList();
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    static {
        PKG_PREFIX = "com.ibm.ws.classloading.internal.";
        IBM_APP_CLASS_LOADER = "com.ibm.ws.classloading.internal.AppClassLoader";
        IBM_THREAD_CONTEXT_CLASS_LOADER = "com.ibm.ws.classloading.internal.ThreadContextClassLoader";
    }
    
    private WebsphereLibertyClassLoaderHandler() {
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "com.ibm.ws.classloading.internal.AppClassLoader".equals(clazz.getName()) || "com.ibm.ws.classloading.internal.ThreadContextClassLoader".equals(clazz.getName());
    }
}
