// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.concurrency.WorkQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.JarUtils;
import java.util.ArrayList;
import java.util.Set;
import java.net.URI;
import nonapi.io.github.classgraph.utils.FileUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Queue;
import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;

abstract class ClasspathElement
{
    protected final /* synthetic */ AtomicBoolean scanned;
    protected final /* synthetic */ List<Resource> acceptedResources;
    protected /* synthetic */ ClassLoader classLoader;
    /* synthetic */ boolean skipClasspathElement;
    protected final /* synthetic */ Map<File, Long> fileToLastModified;
    protected /* synthetic */ List<Resource> acceptedClassfileResources;
    final /* synthetic */ Queue<Map.Entry<Integer, ClasspathElement>> childClasspathElementsIndexed;
    /* synthetic */ boolean containsSpecificallyAcceptedClasspathElementResourcePath;
    final /* synthetic */ ScanSpec scanSpec;
    
    protected LogNode log(final int i, final String s, final LogNode logNode) {
        return logNode.log(String.format("%07d", i), s);
    }
    
    protected void addAcceptedResource(final Resource resource, final ScanSpec.ScanSpecPathMatch scanSpecPathMatch, final boolean b, final LogNode logNode) {
        final String path = resource.getPath();
        final boolean classfile = FileUtils.isClassfile(path);
        boolean b2 = false;
        if (classfile) {
            if (this.scanSpec.enableClassInfo && !this.scanSpec.classfilePathAcceptReject.isRejected(path)) {
                this.acceptedClassfileResources.add(resource);
                b2 = true;
            }
        }
        else {
            b2 = true;
        }
        if (!b) {
            this.acceptedResources.add(resource);
        }
        if (logNode != null && b2) {
            final String s = classfile ? "classfile" : "resource";
            String str = null;
            switch (scanSpecPathMatch) {
                case HAS_ACCEPTED_PATH_PREFIX: {
                    str = "Found " + s + " within subpackage of accepted package: ";
                    break;
                }
                case AT_ACCEPTED_PATH: {
                    str = "Found " + s + " within accepted package: ";
                    break;
                }
                case AT_ACCEPTED_CLASS_PACKAGE: {
                    str = "Found specifically-accepted " + s + ": ";
                    break;
                }
                default: {
                    str = "Found accepted " + s + ": ";
                    break;
                }
            }
            resource.scanLog = logNode.log("0:" + path, str + path + (path.equals(resource.getPathRelativeToClasspathElement()) ? "" : (" ; full path: " + resource.getPathRelativeToClasspathElement())));
        }
    }
    
    ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    abstract Resource getResource(final String p0);
    
    abstract File getFile();
    
    abstract String getModuleName();
    
    abstract URI getURI();
    
    int getNumClassfileMatches() {
        return (this.acceptedClassfileResources == null) ? 0 : this.acceptedClassfileResources.size();
    }
    
    void maskClassfiles(final int i, final Set<String> set, final LogNode logNode) {
        final ArrayList<Resource> acceptedClassfileResources = new ArrayList<Resource>(this.acceptedClassfileResources.size());
        boolean b = false;
        for (final Resource obj : this.acceptedClassfileResources) {
            final String path = obj.getPath();
            if (!path.equals("module-info.class") && !path.equals("package-info.class") && !path.endsWith("/package-info.class") && !set.add(path)) {
                b = true;
                if (logNode == null) {
                    continue;
                }
                logNode.log(String.format("%06d-1", i), "Ignoring duplicate (masked) class " + JarUtils.classfilePathToClassName(path) + " found at " + obj);
            }
            else {
                acceptedClassfileResources.add(obj);
            }
        }
        if (b) {
            this.acceptedClassfileResources = acceptedClassfileResources;
        }
    }
    
    ClasspathElement(final ClassLoader classLoader, final ScanSpec scanSpec) {
        this.childClasspathElementsIndexed = new ConcurrentLinkedQueue<Map.Entry<Integer, ClasspathElement>>();
        this.acceptedResources = new ArrayList<Resource>();
        this.acceptedClassfileResources = new ArrayList<Resource>();
        this.fileToLastModified = new ConcurrentHashMap<File, Long>();
        this.scanned = new AtomicBoolean(false);
        this.classLoader = classLoader;
        this.scanSpec = scanSpec;
    }
    
    abstract void scanPaths(final LogNode p0);
    
    protected void checkResourcePathAcceptReject(final String s, final LogNode logNode) {
        if (!this.scanSpec.classpathElementResourcePathAcceptReject.acceptAndRejectAreEmpty()) {
            if (this.scanSpec.classpathElementResourcePathAcceptReject.isRejected(s)) {
                if (logNode != null) {
                    logNode.log("Reached rejected classpath element resource path, stopping scanning: " + s);
                }
                this.skipClasspathElement = true;
                return;
            }
            if (this.scanSpec.classpathElementResourcePathAcceptReject.isSpecificallyAccepted(s)) {
                if (logNode != null) {
                    logNode.log("Reached specifically accepted classpath element resource path: " + s);
                }
                this.containsSpecificallyAcceptedClasspathElementResourcePath = true;
            }
        }
    }
    
    protected void finishScanPaths(final LogNode logNode) {
        if (logNode != null) {
            if (this.acceptedResources.isEmpty() && this.acceptedClassfileResources.isEmpty()) {
                logNode.log(this.scanSpec.enableClassInfo ? "No accepted classfiles or resources found" : "Classfile scanning is disabled, and no accepted resources found");
            }
            else if (this.acceptedResources.isEmpty()) {
                logNode.log("No accepted resources found");
            }
            else if (this.acceptedClassfileResources.isEmpty()) {
                logNode.log(this.scanSpec.enableClassInfo ? "No accepted classfiles found" : "Classfile scanning is disabled");
            }
        }
        if (logNode != null) {
            logNode.addElapsedTime();
        }
    }
    
    abstract void open(final WorkQueue<Scanner.ClasspathEntryWorkUnit> p0, final LogNode p1) throws InterruptedException;
}
