// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import java.util.Iterator;
import java.util.SortedSet;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;

class PlexusClassWorldsClassRealmClassLoaderHandler implements ClassLoaderHandler
{
    private PlexusClassWorldsClassRealmClassLoaderHandler() {
    }
    
    public static void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "foreignImports", false);
        if (fieldVal != null) {
            final Iterator<Object> iterator = ((SortedSet<Object>)fieldVal).iterator();
            while (iterator.hasNext()) {
                classLoaderOrder.delegateTo((ClassLoader)ReflectionUtils.invokeMethod(iterator.next(), "getClassLoader", false), true, logNode);
            }
        }
        final boolean parentFirstStrategy = isParentFirstStrategy(classLoader);
        if (!parentFirstStrategy) {
            classLoaderOrder.add(classLoader, logNode);
        }
        classLoaderOrder.delegateTo((ClassLoader)ReflectionUtils.invokeMethod(classLoader, "getParentClassLoader", false), true, logNode);
        classLoaderOrder.delegateTo(classLoader.getParent(), true, logNode);
        if (parentFirstStrategy) {
            classLoaderOrder.add(classLoader, logNode);
        }
    }
    
    public static boolean canHandle(final Class<?> clazz, final LogNode logNode) {
        return "org.codehaus.plexus.classworlds.realm.ClassRealm".equals(clazz.getName());
    }
    
    public static void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
        URLClassLoaderHandler.findClasspathOrder(classLoader, classpathOrder, scanSpec, logNode);
    }
    
    private static boolean isParentFirstStrategy(final ClassLoader classLoader) {
        final Object fieldVal = ReflectionUtils.getFieldVal(classLoader, "strategy", false);
        if (fieldVal != null) {
            final String name = fieldVal.getClass().getName();
            if (name.equals("org.codehaus.plexus.classworlds.strategy.SelfFirstStrategy") || name.equals("org.codehaus.plexus.classworlds.strategy.OsgiBundleStrategy")) {
                return false;
            }
        }
        return true;
    }
}
