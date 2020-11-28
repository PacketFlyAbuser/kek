// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import java.util.HashSet;
import java.lang.reflect.Array;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import java.util.Set;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class EquinoxClassLoaderHandler implements ClassLoaderHandler
{
    private static final /* synthetic */ String[] FIELD_NAMES;
    private static /* synthetic */ boolean alreadyReadSystemBundles;
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        classLoaderOrder.add(classLoader, logNode);
    }
    
    static {
        FIELD_NAMES = new String[] { "cp", "nestedDirName" };
    }
    
    private EquinoxClassLoaderHandler() {
    }
    
    private static void addBundleFile(final Object o, final Set<Object> set, final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        if (o != null && set.add(o)) {
            final Object fieldVal = ReflectionUtils.getFieldVal(o, "basefile", false);
            if (fieldVal != null) {
                boolean b = false;
                final String[] field_NAMES = EquinoxClassLoaderHandler.FIELD_NAMES;
                for (int length = field_NAMES.length, i = 0; i < length; ++i) {
                    final Object fieldVal2 = ReflectionUtils.getFieldVal(o, field_NAMES[i], false);
                    if (fieldVal2 != null) {
                        b = true;
                        Object o2 = fieldVal;
                        String str = "/";
                        if (o.getClass().getName().equals("org.eclipse.osgi.storage.bundlefile.NestedDirBundleFile")) {
                            final Object fieldVal3 = ReflectionUtils.getFieldVal(o, "baseBundleFile", false);
                            if (fieldVal3 != null && fieldVal3.getClass().getName().equals("org.eclipse.osgi.storage.bundlefile.ZipBundleFile")) {
                                o2 = fieldVal3;
                                str = "!/";
                            }
                        }
                        classpathOrder.addClasspathEntry(o2.toString() + str + fieldVal2.toString(), classLoader, scanSpec, logNode);
                        break;
                    }
                }
                if (!b) {
                    classpathOrder.addClasspathEntry(fieldVal.toString(), classLoader, scanSpec, logNode);
                }
            }
            addBundleFile(ReflectionUtils.getFieldVal(o, "wrapped", false), set, classLoader, classpathOrder, scanSpec, logNode);
            addBundleFile(ReflectionUtils.getFieldVal(o, "next", false), set, classLoader, classpathOrder, scanSpec, logNode);
        }
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "manager", false);
        addClasspathEntries(fieldVal, classLoader, classpathOrder, scanSpec, logNode);
        final Object fieldVal2 = ReflectionUtils.getFieldVal(fieldVal, "fragments", false);
        if (fieldVal2 != null) {
            for (int i = 0; i < Array.getLength(fieldVal2); ++i) {
                addClasspathEntries(Array.get(fieldVal2, i), classLoader, classpathOrder, scanSpec, logNode);
            }
        }
        if (!EquinoxClassLoaderHandler.alreadyReadSystemBundles) {
            final Object invokeMethod = ReflectionUtils.invokeMethod(ReflectionUtils.invokeMethod(ReflectionUtils.invokeMethod(ReflectionUtils.invokeMethod(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(classLoader, "delegate", false), "container", false), "storage", false), "moduleContainer", false), "moduleDatabase", false), "modulesById", false), "get", Object.class, 0L, false), "getBundle", false), "getBundleContext", false), "getBundles", false);
            if (invokeMethod != null) {
                for (int j = 0; j < Array.getLength(invokeMethod); ++j) {
                    final String s = (String)ReflectionUtils.getFieldVal(ReflectionUtils.getFieldVal(Array.get(invokeMethod, j), "module", false), "location", false);
                    if (s != null) {
                        final int index = s.indexOf("file:");
                        if (index >= 0) {
                            classpathOrder.addClasspathEntry(s.substring(index), classLoader, scanSpec, logNode);
                        }
                    }
                }
            }
            EquinoxClassLoaderHandler.alreadyReadSystemBundles = true;
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.eclipse.osgi.internal.loader.EquinoxClassLoader".equals(clazz.getName());
    }
    
    private static void addClasspathEntries(final Object o, final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        final Object fieldVal = ReflectionUtils.getFieldVal(o, "entries", false);
        if (fieldVal != null) {
            for (int i = 0; i < Array.getLength(fieldVal); ++i) {
                addBundleFile(ReflectionUtils.getFieldVal(Array.get(fieldVal, i), "bundlefile", false), new HashSet<Object>(), classLoader, classpathOrder, scanSpec, logNode);
            }
        }
    }
}
