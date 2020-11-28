// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.classpath;

import java.util.Iterator;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Collections;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.LinkedHashSet;
import io.github.classgraph.ModuleRef;
import java.util.List;

public class ModuleFinder
{
    private /* synthetic */ boolean forceScanJavaClassPath;
    private /* synthetic */ List<ModuleRef> nonSystemModuleRefs;
    private /* synthetic */ List<ModuleRef> systemModuleRefs;
    
    public boolean forceScanJavaClassPath() {
        return this.forceScanJavaClassPath;
    }
    
    private static List<ModuleRef> findModuleRefs(final LinkedHashSet<Object> set, final ScanSpec scanSpec, final LogNode logNode) {
        if (set.isEmpty()) {
            return Collections.emptyList();
        }
        final ArrayDeque<Object> c = new ArrayDeque<Object>();
        final HashSet<Object> set2 = new HashSet<Object>();
        for (final Object next : set) {
            if (next != null) {
                findLayerOrder(next, new HashSet<Object>(), set2, c);
            }
        }
        if (scanSpec.addedModuleLayers != null) {
            for (final Object next2 : scanSpec.addedModuleLayers) {
                if (next2 != null) {
                    findLayerOrder(next2, new HashSet<Object>(), set2, c);
                }
            }
        }
        ArrayList<Object> list;
        if (scanSpec.ignoreParentModuleLayers) {
            list = new ArrayList<Object>();
            for (final Object next3 : c) {
                if (!set2.contains(next3)) {
                    list.add(next3);
                }
            }
        }
        else {
            list = new ArrayList<Object>(c);
        }
        final HashSet<Object> set3 = new HashSet<Object>();
        final LinkedHashSet<ModuleRef> c2 = new LinkedHashSet<ModuleRef>();
        for (final Object next4 : list) {
            final Object invokeMethod = ReflectionUtils.invokeMethod(next4, "configuration", true);
            if (invokeMethod != null) {
                final Set set4 = (Set)ReflectionUtils.invokeMethod(invokeMethod, "modules", true);
                if (set4 == null) {
                    continue;
                }
                final ArrayList<ModuleRef> c3 = new ArrayList<ModuleRef>();
                final Iterator<Object> iterator5 = set4.iterator();
                while (iterator5.hasNext()) {
                    final Object invokeMethod2 = ReflectionUtils.invokeMethod(iterator5.next(), "reference", true);
                    if (invokeMethod2 != null && set3.add(invokeMethod2)) {
                        try {
                            c3.add(new ModuleRef(invokeMethod2, next4));
                        }
                        catch (IllegalArgumentException ex) {
                            if (logNode == null) {
                                continue;
                            }
                            logNode.log("Exception while creating ModuleRef for module " + invokeMethod2, ex);
                        }
                    }
                }
                CollectionUtils.sortIfNotEmpty((List<Comparable>)c3);
                c2.addAll((Collection<?>)c3);
            }
        }
        return new ArrayList<ModuleRef>(c2);
    }
    
    private List<ModuleRef> findModuleRefsFromCallstack(final Class<?>[] array, final ScanSpec scanSpec, final LogNode logNode) {
        final LinkedHashSet<Object> set = new LinkedHashSet<Object>();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final Object invokeMethod = ReflectionUtils.invokeMethod(array[i], "getModule", false);
                if (invokeMethod != null) {
                    final Object invokeMethod2 = ReflectionUtils.invokeMethod(invokeMethod, "getLayer", true);
                    if (invokeMethod2 != null) {
                        set.add(invokeMethod2);
                    }
                    else {
                        this.forceScanJavaClassPath = true;
                    }
                }
            }
        }
        Class<?> forName = null;
        try {
            forName = Class.forName("java.lang.ModuleLayer");
        }
        catch (ClassNotFoundException ex) {}
        catch (LinkageError linkageError) {}
        if (forName != null) {
            final Object invokeStaticMethod = ReflectionUtils.invokeStaticMethod(forName, "boot", false);
            if (invokeStaticMethod != null) {
                set.add(invokeStaticMethod);
            }
            else {
                this.forceScanJavaClassPath = true;
            }
        }
        return findModuleRefs(set, scanSpec, logNode);
    }
    
    private static void findLayerOrder(final Object o, final Set<Object> set, final Set<Object> set2, final Deque<Object> deque) {
        if (set.add(o)) {
            final List list = (List)ReflectionUtils.invokeMethod(o, "parents", true);
            if (list != null) {
                set2.addAll(list);
                final Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    findLayerOrder(iterator.next(), set, set2, deque);
                }
            }
            deque.push(o);
        }
    }
    
    public List<ModuleRef> getNonSystemModuleRefs() {
        return this.nonSystemModuleRefs;
    }
    
    public ModuleFinder(final Class<?>[] array, final ScanSpec scanSpec, final LogNode logNode) {
        if (scanSpec.scanModules) {
            List<ModuleRef> list = null;
            if (scanSpec.overrideModuleLayers == null) {
                if (array != null && array.length > 0) {
                    list = this.findModuleRefsFromCallstack(array, scanSpec, logNode);
                }
            }
            else {
                if (logNode != null) {
                    final LogNode log = logNode.log("Overriding module layers");
                    final Iterator<Object> iterator = scanSpec.overrideModuleLayers.iterator();
                    while (iterator.hasNext()) {
                        log.log(iterator.next().toString());
                    }
                }
                list = findModuleRefs(new LinkedHashSet<Object>(scanSpec.overrideModuleLayers), scanSpec, logNode);
            }
            if (list != null) {
                this.systemModuleRefs = new ArrayList<ModuleRef>();
                this.nonSystemModuleRefs = new ArrayList<ModuleRef>();
                for (final ModuleRef moduleRef : list) {
                    if (moduleRef != null) {
                        if (moduleRef.isSystemModule()) {
                            this.systemModuleRefs.add(moduleRef);
                        }
                        else {
                            this.nonSystemModuleRefs.add(moduleRef);
                        }
                    }
                }
            }
            if (logNode != null) {
                final LogNode log2 = logNode.log("Found system modules:");
                if (this.systemModuleRefs != null && !this.systemModuleRefs.isEmpty()) {
                    final Iterator<ModuleRef> iterator3 = this.systemModuleRefs.iterator();
                    while (iterator3.hasNext()) {
                        log2.log(iterator3.next().toString());
                    }
                }
                else {
                    log2.log("[None]");
                }
                final LogNode log3 = logNode.log("Found non-system modules:");
                if (this.nonSystemModuleRefs != null && !this.nonSystemModuleRefs.isEmpty()) {
                    final Iterator<ModuleRef> iterator4 = this.nonSystemModuleRefs.iterator();
                    while (iterator4.hasNext()) {
                        log3.log(iterator4.next().toString());
                    }
                }
                else {
                    log3.log("[None]");
                }
            }
        }
        else if (logNode != null) {
            logNode.log("Module scanning is disabled, because classloaders or classpath was overridden");
        }
    }
    
    public List<ModuleRef> getSystemModuleRefs() {
        return this.systemModuleRefs;
    }
}
