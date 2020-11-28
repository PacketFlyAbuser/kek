// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classloaderhandler;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.classpath.ClassLoaderOrder;
import nonapi.io.github.classgraph.utils.LogNode;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class ClassLoaderHandlerRegistry
{
    static {
        CLASS_LOADER_HANDLERS = Collections.unmodifiableList((List<? extends ClassLoaderHandlerRegistryEntry>)Arrays.asList(new ClassLoaderHandlerRegistryEntry((Class)AntClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)EquinoxClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)EquinoxContextFinderClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)FelixClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)JBossClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)WeblogicClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)WebsphereLibertyClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)WebsphereTraditionalClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)OSGiDefaultClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)SpringBootRestartClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)TomcatWebappClassLoaderBaseHandler.class), new ClassLoaderHandlerRegistryEntry((Class)PlexusClassWorldsClassRealmClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)QuarkusClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)UnoOneJarClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)ParentLastDelegationOrderTestClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)JPMSClassLoaderHandler.class), new ClassLoaderHandlerRegistryEntry((Class)URLClassLoaderHandler.class)));
        FALLBACK_HANDLER = new ClassLoaderHandlerRegistryEntry((Class)FallbackClassLoaderHandler.class);
        AUTOMATIC_LIB_DIR_PREFIXES = new String[] { "BOOT-INF/lib/", "WEB-INF/lib/", "WEB-INF/lib-provided/", "META-INF/lib/", "lib/", "lib/ext/", "main/" };
        AUTOMATIC_PACKAGE_ROOT_PREFIXES = new String[] { "classes/", "test-classes/", "BOOT-INF/classes/", "WEB-INF/classes/" };
    }
    
    private ClassLoaderHandlerRegistry() {
    }
    
    public static class ClassLoaderHandlerRegistryEntry
    {
        private final /* synthetic */ Method canHandleMethod;
        private final /* synthetic */ Method findClassLoaderOrderMethod;
        private final /* synthetic */ Method findClasspathOrderMethod;
        public final /* synthetic */ Class<? extends ClassLoaderHandler> classLoaderHandlerClass;
        
        private ClassLoaderHandlerRegistryEntry(final Class<? extends ClassLoaderHandler> classLoaderHandlerClass) {
            this.classLoaderHandlerClass = classLoaderHandlerClass;
            try {
                this.canHandleMethod = classLoaderHandlerClass.getDeclaredMethod("canHandle", Class.class, LogNode.class);
            }
            catch (Exception cause) {
                throw new RuntimeException("Could not find canHandle method for " + classLoaderHandlerClass.getName(), cause);
            }
            try {
                this.findClassLoaderOrderMethod = classLoaderHandlerClass.getDeclaredMethod("findClassLoaderOrder", ClassLoader.class, ClassLoaderOrder.class, LogNode.class);
            }
            catch (Exception cause2) {
                throw new RuntimeException("Could not find findClassLoaderOrder method for " + classLoaderHandlerClass.getName(), cause2);
            }
            try {
                this.findClasspathOrderMethod = classLoaderHandlerClass.getDeclaredMethod("findClasspathOrder", ClassLoader.class, ClasspathOrder.class, ScanSpec.class, LogNode.class);
            }
            catch (Exception cause3) {
                throw new RuntimeException("Could not find findClasspathOrder method for " + classLoaderHandlerClass.getName(), cause3);
            }
        }
        
        public boolean canHandle(final Class<?> clazz, final LogNode logNode) {
            try {
                return (boolean)this.canHandleMethod.invoke(null, clazz, logNode);
            }
            catch (Throwable cause) {
                throw new RuntimeException("Exception while calling canHandle for " + this.classLoaderHandlerClass.getName(), cause);
            }
        }
        
        public void findClassLoaderOrder(final ClassLoader classLoader, final ClassLoaderOrder classLoaderOrder, final LogNode logNode) {
            try {
                this.findClassLoaderOrderMethod.invoke(null, classLoader, classLoaderOrder, logNode);
            }
            catch (Throwable cause) {
                throw new RuntimeException("Exception while calling findClassLoaderOrder for " + this.classLoaderHandlerClass.getName(), cause);
            }
        }
        
        public void findClasspathOrder(final ClassLoader classLoader, final ClasspathOrder classpathOrder, final ScanSpec scanSpec, final LogNode logNode) {
            try {
                this.findClasspathOrderMethod.invoke(null, classLoader, classpathOrder, scanSpec, logNode);
            }
            catch (Throwable cause) {
                throw new RuntimeException("Exception while calling findClassLoaderOrder for " + this.classLoaderHandlerClass.getName(), cause);
            }
        }
    }
}
