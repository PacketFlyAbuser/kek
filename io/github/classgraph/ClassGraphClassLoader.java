// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Collection;
import java.net.URLClassLoader;
import java.util.LinkedHashSet;
import java.io.InputStream;
import java.security.ProtectionDomain;
import nonapi.io.github.classgraph.utils.JarUtils;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

class ClassGraphClassLoader extends ClassLoader
{
    private final /* synthetic */ boolean initializeLoadedClasses;
    private final /* synthetic */ Set<ClassLoader> environmentClassLoaderDelegationOrder;
    private final /* synthetic */ ScanResult scanResult;
    private final /* synthetic */ Set<ClassLoader> overriddenOrAddedClassLoaderDelegationOrder;
    
    @Override
    public Enumeration<URL> getResources(final String s) throws IOException {
        if (!this.environmentClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator = this.environmentClassLoaderDelegationOrder.iterator();
            while (iterator.hasNext()) {
                final Enumeration<URL> resources = iterator.next().getResources(s);
                if (resources != null && resources.hasMoreElements()) {
                    return resources;
                }
            }
        }
        if (!this.overriddenOrAddedClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator2 = this.overriddenOrAddedClassLoaderDelegationOrder.iterator();
            while (iterator2.hasNext()) {
                final Enumeration<URL> resources2 = iterator2.next().getResources(s);
                if (resources2 != null && resources2.hasMoreElements()) {
                    return resources2;
                }
            }
        }
        final ResourceList resourcesWithPath = this.scanResult.getResourcesWithPath(s);
        if (resourcesWithPath == null || resourcesWithPath.isEmpty()) {
            return Collections.emptyEnumeration();
        }
        return new Enumeration<URL>() {
            /* synthetic */ int idx;
            
            @Override
            public boolean hasMoreElements() {
                return this.idx < resourcesWithPath.size();
            }
            
            @Override
            public URL nextElement() {
                return resourcesWithPath.get(this.idx++).getURL();
            }
        };
    }
    
    @Override
    protected Class<?> findClass(final String s) throws ClassNotFoundException, SecurityException, LinkageError {
        final Class<?> loadedClass = this.findLoadedClass(s);
        if (loadedClass != null) {
            return loadedClass;
        }
        if (!this.environmentClassLoaderDelegationOrder.isEmpty()) {
            for (final ClassLoader loader : this.environmentClassLoaderDelegationOrder) {
                try {
                    return Class.forName(s, this.initializeLoadedClasses, loader);
                }
                catch (ClassNotFoundException | LinkageError ex) {
                    continue;
                }
                break;
            }
        }
        ClassLoader classLoader = null;
        final ClassInfo classInfo = (this.scanResult.classNameToClassInfo == null) ? null : this.scanResult.classNameToClassInfo.get(s);
        if (classInfo != null) {
            classLoader = classInfo.classLoader;
            if (classLoader != null && !this.environmentClassLoaderDelegationOrder.contains(classLoader)) {
                try {
                    return Class.forName(s, this.initializeLoadedClasses, classLoader);
                }
                catch (ClassNotFoundException ex2) {}
                catch (LinkageError linkageError) {}
            }
            if (classInfo.classpathElement instanceof ClasspathElementModule && !classInfo.isPublic()) {
                throw new ClassNotFoundException("Classfile for class " + s + " was found in a module, but the context and system classloaders could not load the class, probably because the class is not public.");
            }
        }
        if (!this.overriddenOrAddedClassLoaderDelegationOrder.isEmpty()) {
            for (final ClassLoader loader2 : this.overriddenOrAddedClassLoaderDelegationOrder) {
                if (loader2 != classLoader) {
                    try {
                        return Class.forName(s, this.initializeLoadedClasses, loader2);
                    }
                    catch (ClassNotFoundException ex3) {}
                    catch (LinkageError linkageError2) {}
                }
            }
        }
        final ResourceList resourcesWithPath = this.scanResult.getResourcesWithPath(JarUtils.classNameToClassfilePath(s));
        if (resourcesWithPath != null) {
            final Iterator iterator3 = resourcesWithPath.iterator();
            if (iterator3.hasNext()) {
                final Resource resource = iterator3.next();
                try {
                    try {
                        return this.defineClass(s, resource.read(), null);
                    }
                    finally {
                        resource.close();
                    }
                }
                catch (IOException obj) {
                    throw new ClassNotFoundException("Could not load classfile for class " + s + " : " + obj);
                }
                finally {
                    resource.close();
                }
            }
        }
        throw new ClassNotFoundException("Could not load classfile for class " + s);
    }
    
    @Override
    public InputStream getResourceAsStream(final String name) {
        if (!this.environmentClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator = this.environmentClassLoaderDelegationOrder.iterator();
            while (iterator.hasNext()) {
                final InputStream resourceAsStream = iterator.next().getResourceAsStream(name);
                if (resourceAsStream != null) {
                    return resourceAsStream;
                }
            }
        }
        if (!this.overriddenOrAddedClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator2 = this.overriddenOrAddedClassLoaderDelegationOrder.iterator();
            while (iterator2.hasNext()) {
                final InputStream resourceAsStream2 = iterator2.next().getResourceAsStream(name);
                if (resourceAsStream2 != null) {
                    return resourceAsStream2;
                }
            }
        }
        final ResourceList resourcesWithPath = this.scanResult.getResourcesWithPath(name);
        if (resourcesWithPath == null || resourcesWithPath.isEmpty()) {
            return super.getResourceAsStream(name);
        }
        try {
            return resourcesWithPath.get(0).open();
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    ClassGraphClassLoader(final ScanResult scanResult) {
        super(null);
        registerAsParallelCapable();
        this.scanResult = scanResult;
        final ScanSpec scanSpec = scanResult.scanSpec;
        this.initializeLoadedClasses = scanSpec.initializeLoadedClasses;
        final boolean b = scanSpec.overrideClasspath != null && !scanSpec.overrideClasspath.isEmpty();
        final boolean b2 = scanSpec.overrideClassLoaders != null && !scanSpec.overrideClassLoaders.isEmpty();
        final boolean b3 = scanSpec.addedClassLoaders != null && !scanSpec.addedClassLoaders.isEmpty();
        this.environmentClassLoaderDelegationOrder = new LinkedHashSet<ClassLoader>();
        if (!b && !b2) {
            this.environmentClassLoaderDelegationOrder.add(null);
            final ClassLoader[] classLoaderOrderRespectingParentDelegation = scanResult.getClassLoaderOrderRespectingParentDelegation();
            if (classLoaderOrderRespectingParentDelegation != null) {
                final ClassLoader[] array = classLoaderOrderRespectingParentDelegation;
                for (int length = array.length, i = 0; i < length; ++i) {
                    this.environmentClassLoaderDelegationOrder.add(array[i]);
                }
            }
        }
        final URLClassLoader urlClassLoader = new URLClassLoader(scanResult.getClasspathURLs().toArray(new URL[0]));
        if (b) {
            this.environmentClassLoaderDelegationOrder.add(urlClassLoader);
        }
        this.overriddenOrAddedClassLoaderDelegationOrder = new LinkedHashSet<ClassLoader>();
        if (b2) {
            this.overriddenOrAddedClassLoaderDelegationOrder.addAll(scanSpec.overrideClassLoaders);
        }
        if (b3) {
            this.overriddenOrAddedClassLoaderDelegationOrder.addAll(scanSpec.addedClassLoaders);
        }
        if (!b) {
            this.overriddenOrAddedClassLoaderDelegationOrder.add(urlClassLoader);
        }
        this.overriddenOrAddedClassLoaderDelegationOrder.removeAll(this.environmentClassLoaderDelegationOrder);
    }
    
    @Override
    public URL getResource(final String name) {
        if (!this.environmentClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator = this.environmentClassLoaderDelegationOrder.iterator();
            while (iterator.hasNext()) {
                final URL resource = iterator.next().getResource(name);
                if (resource != null) {
                    return resource;
                }
            }
        }
        if (!this.overriddenOrAddedClassLoaderDelegationOrder.isEmpty()) {
            final Iterator<ClassLoader> iterator2 = this.overriddenOrAddedClassLoaderDelegationOrder.iterator();
            while (iterator2.hasNext()) {
                final URL resource2 = iterator2.next().getResource(name);
                if (resource2 != null) {
                    return resource2;
                }
            }
        }
        final ResourceList resourcesWithPath = this.scanResult.getResourcesWithPath(name);
        if (resourcesWithPath == null || resourcesWithPath.isEmpty()) {
            return super.getResource(name);
        }
        return resourcesWithPath.get(0).getURL();
    }
}
