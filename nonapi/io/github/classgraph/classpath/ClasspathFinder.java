// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.util.Set;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import nonapi.io.github.classgraph.utils.JarUtils;
import java.util.Map;
import java.util.ArrayList;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;

public class ClasspathFinder
{
    private final /* synthetic */ ClasspathOrder classpathOrder;
    private final /* synthetic */ ModuleFinder moduleFinder;
    private /* synthetic */ ClassLoader[] classLoaderOrderRespectingParentDelegation;
    
    public ClassLoader[] getClassLoaderOrderRespectingParentDelegation() {
        return this.classLoaderOrderRespectingParentDelegation;
    }
    
    public ClasspathFinder(final ScanSpec scanSpec, final LogNode logNode) {
        final LogNode logNode2 = (logNode == null) ? null : logNode.log("Finding classpath and modules");
        boolean scanModules;
        if (scanSpec.overrideClasspath != null) {
            scanModules = false;
        }
        else if (scanSpec.overrideClassLoaders != null) {
            scanModules = false;
            final Iterator<ClassLoader> iterator = scanSpec.overrideClassLoaders.iterator();
            while (iterator.hasNext()) {
                final String name = iterator.next().getClass().getName();
                if (name.equals("jdk.internal.loader.ClassLoaders$AppClassLoader")) {
                    scanModules = true;
                }
                else {
                    if (!name.equals("jdk.internal.loader.ClassLoaders$PlatformClassLoader")) {
                        continue;
                    }
                    scanModules = true;
                    if (scanSpec.enableSystemJarsAndModules) {
                        continue;
                    }
                    if (logNode2 != null) {
                        logNode2.log("overrideClassLoaders() was called with an instance of jdk.internal.loader.ClassLoaders$PlatformClassLoader, which is a system classloader, so enableSystemJarsAndModules() was called automatically");
                    }
                    scanSpec.enableSystemJarsAndModules = true;
                }
            }
        }
        else {
            scanModules = scanSpec.scanModules;
        }
        this.moduleFinder = (scanModules ? new ModuleFinder(CallStackReader.getClassContext(logNode2), scanSpec, logNode2) : null);
        this.classpathOrder = new ClasspathOrder(scanSpec);
        final ClassLoaderFinder classLoaderFinder = (scanSpec.overrideClasspath == null && scanSpec.overrideClassLoaders == null) ? new ClassLoaderFinder(scanSpec, logNode2) : null;
        final ClassLoader[] classLoaderOrderRespectingParentDelegation = (classLoaderFinder == null) ? new ClassLoader[0] : classLoaderFinder.getContextClassLoaders();
        final ClassLoader classLoader = (classLoaderOrderRespectingParentDelegation.length > 0) ? classLoaderOrderRespectingParentDelegation[0] : null;
        if (scanSpec.overrideClasspath != null) {
            if (scanSpec.overrideClassLoaders != null && logNode2 != null) {
                logNode2.log("It is not possible to override both the classpath and the ClassLoaders -- ignoring the ClassLoader override");
            }
            final LogNode logNode3 = (logNode2 == null) ? null : logNode2.log("Overriding classpath with: " + scanSpec.overrideClasspath);
            this.classpathOrder.addClasspathEntries(scanSpec.overrideClasspath, classLoader, scanSpec, logNode3);
            if (logNode3 != null) {
                logNode3.log("WARNING: when the classpath is overridden, there is no guarantee that the classes found by classpath scanning will be the same as the classes loaded by the context classloader");
            }
            this.classLoaderOrderRespectingParentDelegation = classLoaderOrderRespectingParentDelegation;
        }
        else if (scanSpec.overrideClassLoaders == null) {
            final String jreRtJarPath = SystemJarFinder.getJreRtJarPath();
            final LogNode logNode4 = (logNode2 == null) ? null : logNode2.log("System jars:");
            if (jreRtJarPath != null) {
                if (scanSpec.enableSystemJarsAndModules) {
                    this.classpathOrder.addSystemClasspathEntry(jreRtJarPath, classLoader);
                    if (logNode4 != null) {
                        logNode4.log("Found rt.jar: " + jreRtJarPath);
                    }
                }
                else if (logNode4 != null) {
                    logNode4.log((scanSpec.enableSystemJarsAndModules ? "" : "Scanning disabled for rt.jar: ") + jreRtJarPath);
                }
            }
            final boolean b = !scanSpec.libOrExtJarAcceptReject.acceptAndRejectAreEmpty();
            for (final String s : SystemJarFinder.getJreLibOrExtJars()) {
                if (b || scanSpec.libOrExtJarAcceptReject.isSpecificallyAcceptedAndNotRejected(s)) {
                    this.classpathOrder.addSystemClasspathEntry(s, classLoader);
                    if (logNode4 == null) {
                        continue;
                    }
                    logNode4.log("Found lib or ext jar: " + s);
                }
                else {
                    if (logNode4 == null) {
                        continue;
                    }
                    logNode4.log("Scanning disabled for lib or ext jar: " + s);
                }
            }
        }
        if (scanSpec.overrideClasspath == null) {
            if (logNode2 != null) {
                final LogNode log = logNode2.log("ClassLoaderHandlers:");
                final Iterator<ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry> iterator3 = ClassLoaderHandlerRegistry.CLASS_LOADER_HANDLERS.iterator();
                while (iterator3.hasNext()) {
                    log.log(iterator3.next().classLoaderHandlerClass.getName());
                }
            }
            final LogNode logNode5 = (logNode2 == null) ? null : logNode2.log("Finding unique classloaders in delegation order");
            final ClassLoaderOrder classLoaderOrder = new ClassLoaderOrder();
            final ClassLoader[] array = (scanSpec.overrideClassLoaders != null) ? scanSpec.overrideClassLoaders.toArray(new ClassLoader[0]) : classLoaderOrderRespectingParentDelegation;
            if (array != null) {
                final ClassLoader[] array2 = array;
                for (int length = array2.length, i = 0; i < length; ++i) {
                    classLoaderOrder.delegateTo(array2[i], false, logNode5);
                }
            }
            final Set<ClassLoader> allParentClassLoaders = classLoaderOrder.getAllParentClassLoaders();
            final LogNode logNode6 = (logNode2 == null) ? null : logNode2.log("Obtaining URLs from classloaders in delegation order");
            final ArrayList<ClassLoader> list = new ArrayList<ClassLoader>();
            for (final Map.Entry<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry> entry : classLoaderOrder.getClassLoaderOrder()) {
                final ClassLoader classLoader2 = entry.getKey();
                final ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry classLoaderHandlerRegistryEntry = entry.getValue();
                if (!scanSpec.ignoreParentClassLoaders || !allParentClassLoaders.contains(classLoader2)) {
                    classLoaderHandlerRegistryEntry.findClasspathOrder(classLoader2, this.classpathOrder, scanSpec, (logNode6 == null) ? null : logNode6.log("Classloader " + classLoader2 + " is handled by " + classLoaderHandlerRegistryEntry.classLoaderHandlerClass.getName()));
                    list.add(classLoader2);
                }
                else {
                    if (logNode6 == null) {
                        continue;
                    }
                    logNode6.log("Ignoring parent classloader " + classLoader2 + ", normally handled by " + classLoaderHandlerRegistryEntry.classLoaderHandlerClass.getName());
                }
            }
            this.classLoaderOrderRespectingParentDelegation = list.toArray(new ClassLoader[0]);
        }
        if ((!scanSpec.ignoreParentClassLoaders && scanSpec.overrideClassLoaders == null && scanSpec.overrideClasspath == null) || (this.moduleFinder != null && this.moduleFinder.forceScanJavaClassPath())) {
            final String[] smartPathSplit = JarUtils.smartPathSplit(System.getProperty("java.class.path"), scanSpec);
            if (smartPathSplit.length > 0) {
                final LogNode logNode7 = (logNode2 == null) ? null : logNode2.log("Getting classpath entries from java.class.path");
                final String[] array3 = smartPathSplit;
                for (int length2 = array3.length, j = 0; j < length2; ++j) {
                    this.classpathOrder.addClasspathEntry(FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, array3[j]), classLoader, scanSpec, logNode7);
                }
            }
        }
    }
    
    public ModuleFinder getModuleFinder() {
        return this.moduleFinder;
    }
    
    public ClasspathOrder getClasspathOrder() {
        return this.classpathOrder;
    }
}
