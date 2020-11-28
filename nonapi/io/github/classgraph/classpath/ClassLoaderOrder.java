// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;
import java.util.AbstractMap;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.Set;
import java.util.List;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import java.util.Map;

public class ClassLoaderOrder
{
    private final /* synthetic */ Map<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry> classLoaderToClassLoaderHandlerRegistryEntry;
    private final /* synthetic */ List<Map.Entry<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry>> classLoaderOrder;
    private final /* synthetic */ Set<ClassLoader> added;
    private final /* synthetic */ Set<ClassLoader> allParentClassLoaders;
    private final /* synthetic */ Set<ClassLoader> delegatedTo;
    
    public void add(final ClassLoader key, final LogNode logNode) {
        if (key == null) {
            return;
        }
        if (this.added.add(key)) {
            final ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry registryEntry = this.getRegistryEntry(key, logNode);
            if (registryEntry != null) {
                this.classLoaderOrder.add(new AbstractMap.SimpleEntry<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry>(key, registryEntry));
            }
        }
    }
    
    public void delegateTo(final ClassLoader classLoader, final boolean b, final LogNode logNode) {
        if (classLoader == null) {
            return;
        }
        if (b) {
            this.allParentClassLoaders.add(classLoader);
        }
        if (this.delegatedTo.add(classLoader)) {
            this.getRegistryEntry(classLoader, logNode).findClassLoaderOrder(classLoader, this, logNode);
        }
    }
    
    private ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry getRegistryEntry(final ClassLoader classLoader, final LogNode logNode) {
        ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry fallback_HANDLER = this.classLoaderToClassLoaderHandlerRegistryEntry.get(classLoader);
        if (fallback_HANDLER == null) {
            for (Serializable s = classLoader.getClass(); s != Object.class && s != null; s = ((Class<Object>)s).getSuperclass()) {
                for (final ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry classLoaderHandlerRegistryEntry : ClassLoaderHandlerRegistry.CLASS_LOADER_HANDLERS) {
                    if (classLoaderHandlerRegistryEntry.canHandle((Class<?>)s, logNode)) {
                        fallback_HANDLER = classLoaderHandlerRegistryEntry;
                        break;
                    }
                }
                if (fallback_HANDLER != null) {
                    break;
                }
            }
            if (fallback_HANDLER == null) {
                fallback_HANDLER = ClassLoaderHandlerRegistry.FALLBACK_HANDLER;
            }
            this.classLoaderToClassLoaderHandlerRegistryEntry.put(classLoader, fallback_HANDLER);
        }
        return fallback_HANDLER;
    }
    
    public Set<ClassLoader> getAllParentClassLoaders() {
        return this.allParentClassLoaders;
    }
    
    public List<Map.Entry<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry>> getClassLoaderOrder() {
        return this.classLoaderOrder;
    }
    
    public ClassLoaderOrder() {
        this.classLoaderOrder = new ArrayList<Map.Entry<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry>>();
        this.added = new HashSet<ClassLoader>();
        this.delegatedTo = new HashSet<ClassLoader>();
        this.allParentClassLoaders = new HashSet<ClassLoader>();
        this.classLoaderToClassLoaderHandlerRegistryEntry = new HashMap<ClassLoader, ClassLoaderHandlerRegistry.ClassLoaderHandlerRegistryEntry>();
    }
}
