// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;

public class ClassLoaderFinder
{
    private final /* synthetic */ ClassLoader[] contextClassLoaders;
    
    public ClassLoader[] getContextClassLoaders() {
        return this.contextClassLoaders;
    }
    
    ClassLoaderFinder(final ScanSpec scanSpec, final LogNode logNode) {
        LinkedHashSet<Object> set;
        LogNode logNode2;
        if (scanSpec.overrideClassLoaders == null) {
            set = new LinkedHashSet<Object>();
            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                set.add(contextClassLoader);
            }
            final ClassLoader classLoader = this.getClass().getClassLoader();
            if (classLoader != null) {
                set.add(classLoader);
            }
            final ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            if (systemClassLoader != null) {
                set.add(systemClassLoader);
            }
            try {
                final Class<?>[] classContext = CallStackReader.getClassContext(logNode);
                for (int i = classContext.length - 1; i >= 0; --i) {
                    final ClassLoader classLoader2 = classContext[i].getClassLoader();
                    if (classLoader2 != null) {
                        set.add(classLoader2);
                    }
                }
            }
            catch (IllegalArgumentException ex) {
                if (logNode != null) {
                    logNode.log("Could not get call stack", ex);
                }
            }
            if (scanSpec.addedClassLoaders != null) {
                set.addAll(scanSpec.addedClassLoaders);
            }
            logNode2 = ((logNode == null) ? null : logNode.log("Found ClassLoaders:"));
        }
        else {
            set = new LinkedHashSet<Object>(scanSpec.overrideClassLoaders);
            logNode2 = ((logNode == null) ? null : logNode.log("Override ClassLoaders:"));
        }
        if (logNode2 != null) {
            final Iterator<ClassLoader> iterator = set.iterator();
            while (iterator.hasNext()) {
                logNode2.log(iterator.next().getClass().getName());
            }
        }
        this.contextClassLoaders = set.toArray(new ClassLoader[0]);
    }
}
