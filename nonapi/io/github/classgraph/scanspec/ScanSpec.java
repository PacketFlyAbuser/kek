// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.scanspec;

import java.util.Collection;
import java.util.Collections;
import io.github.classgraph.ClassGraphException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.lang.reflect.Field;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.HashSet;
import java.util.Set;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ModulePathInfo;
import java.util.List;

public class ScanSpec
{
    public transient /* synthetic */ List<Object> overrideModuleLayers;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString classAcceptReject;
    public transient /* synthetic */ List<ClassLoader> addedClassLoaders;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString moduleAcceptReject;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString classPackagePathAcceptReject;
    public /* synthetic */ AcceptReject.AcceptRejectPrefix packagePrefixAcceptReject;
    public /* synthetic */ ModulePathInfo modulePathInfo;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString classPackageAcceptReject;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString packageAcceptReject;
    public /* synthetic */ boolean scanModules;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString pathAcceptReject;
    public /* synthetic */ boolean scanJars;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString classfilePathAcceptReject;
    public /* synthetic */ AcceptReject.AcceptRejectLeafname jarAcceptReject;
    public transient /* synthetic */ List<ClassGraph.ClasspathElementFilter> classpathElementFilters;
    public /* synthetic */ AcceptReject.AcceptRejectPrefix pathPrefixAcceptReject;
    public /* synthetic */ boolean scanDirs;
    public /* synthetic */ List<Object> overrideClasspath;
    public /* synthetic */ boolean scanNestedJars;
    public /* synthetic */ Set<String> allowedURLSchemes;
    public transient /* synthetic */ List<Object> addedModuleLayers;
    public /* synthetic */ AcceptReject.AcceptRejectWholeString classpathElementResourcePathAcceptReject;
    public /* synthetic */ AcceptReject.AcceptRejectLeafname libOrExtJarAcceptReject;
    public /* synthetic */ int maxBufferedJarRAMSize;
    public transient /* synthetic */ List<ClassLoader> overrideClassLoaders;
    public /* synthetic */ boolean extendScanningUpwardsToExternalClasses;
    
    public void enableURLScheme(final String s) {
        if (s == null || s.length() < 2) {
            throw new IllegalArgumentException("URL schemes must contain at least two characters");
        }
        if (this.allowedURLSchemes == null) {
            this.allowedURLSchemes = new HashSet<String>();
        }
        this.allowedURLSchemes.add(s.toLowerCase());
    }
    
    public void log(final LogNode logNode) {
        if (logNode != null) {
            final LogNode log = logNode.log("ScanSpec:");
            for (final Field field : ScanSpec.class.getDeclaredFields()) {
                try {
                    log.log(field.getName() + ": " + field.get(this));
                }
                catch (ReflectiveOperationException ex) {}
            }
        }
    }
    
    public void addClasspathOverride(final Object o) {
        if (this.overrideClasspath == null) {
            this.overrideClasspath = new ArrayList<Object>();
        }
        this.overrideClasspath.add((o instanceof String || o instanceof URL || o instanceof URI) ? o : o.toString());
    }
    
    public ScanSpecPathMatch dirAcceptMatchStatus(final String s) {
        if (this.pathAcceptReject.isRejected(s)) {
            return ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX;
        }
        if (this.pathPrefixAcceptReject.isRejected(s)) {
            return ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX;
        }
        if (this.pathAcceptReject.acceptIsEmpty() && this.classPackagePathAcceptReject.acceptIsEmpty()) {
            return (s.isEmpty() || s.equals("/")) ? ScanSpecPathMatch.AT_ACCEPTED_PATH : ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX;
        }
        if (this.pathAcceptReject.isSpecificallyAcceptedAndNotRejected(s)) {
            return ScanSpecPathMatch.AT_ACCEPTED_PATH;
        }
        if (this.classPackagePathAcceptReject.isSpecificallyAcceptedAndNotRejected(s)) {
            return ScanSpecPathMatch.AT_ACCEPTED_CLASS_PACKAGE;
        }
        if (this.pathPrefixAcceptReject.isSpecificallyAccepted(s)) {
            return ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX;
        }
        if (s.equals("/")) {
            return ScanSpecPathMatch.ANCESTOR_OF_ACCEPTED_PATH;
        }
        if (this.pathAcceptReject.acceptHasPrefix(s)) {
            return ScanSpecPathMatch.ANCESTOR_OF_ACCEPTED_PATH;
        }
        if (this.classfilePathAcceptReject.acceptHasPrefix(s)) {
            return ScanSpecPathMatch.ANCESTOR_OF_ACCEPTED_PATH;
        }
        return ScanSpecPathMatch.NOT_WITHIN_ACCEPTED_PATH;
    }
    
    public void sortPrefixes() {
        for (final Field obj : ScanSpec.class.getDeclaredFields()) {
            if (AcceptReject.class.isAssignableFrom(obj.getType())) {
                try {
                    ((AcceptReject)obj.get(this)).sortPrefixes();
                }
                catch (ReflectiveOperationException ex) {
                    throw ClassGraphException.newClassGraphException("Field is not accessible: " + obj, ex);
                }
            }
        }
    }
    
    public ScanSpec() {
        this.packageAcceptReject = new AcceptReject.AcceptRejectWholeString('.');
        this.packagePrefixAcceptReject = new AcceptReject.AcceptRejectPrefix('.');
        this.pathAcceptReject = new AcceptReject.AcceptRejectWholeString('/');
        this.pathPrefixAcceptReject = new AcceptReject.AcceptRejectPrefix('/');
        this.classAcceptReject = new AcceptReject.AcceptRejectWholeString('.');
        this.classfilePathAcceptReject = new AcceptReject.AcceptRejectWholeString('/');
        this.classPackageAcceptReject = new AcceptReject.AcceptRejectWholeString('.');
        this.classPackagePathAcceptReject = new AcceptReject.AcceptRejectWholeString('/');
        this.moduleAcceptReject = new AcceptReject.AcceptRejectWholeString('.');
        this.jarAcceptReject = new AcceptReject.AcceptRejectLeafname('/');
        this.classpathElementResourcePathAcceptReject = new AcceptReject.AcceptRejectWholeString('/');
        this.libOrExtJarAcceptReject = new AcceptReject.AcceptRejectLeafname('/');
        this.scanJars = true;
        this.scanNestedJars = true;
        this.scanDirs = true;
        this.scanModules = true;
        this.extendScanningUpwardsToExternalClasses = true;
        this.modulePathInfo = new ModulePathInfo();
        this.maxBufferedJarRAMSize = 67108864;
    }
    
    private static boolean isModuleLayer(final Object o) {
        if (o == null) {
            throw new IllegalArgumentException("ModuleLayer references must not be null");
        }
        for (Class<?> clazz = o.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            if (clazz.getName().equals("java.lang.ModuleLayer")) {
                return true;
            }
        }
        return false;
    }
    
    public void addClassLoader(final ClassLoader classLoader) {
        if (this.addedClassLoaders == null) {
            this.addedClassLoaders = new ArrayList<ClassLoader>();
        }
        if (classLoader != null) {
            this.addedClassLoaders.add(classLoader);
        }
    }
    
    public void overrideClassLoaders(final ClassLoader... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("At least one override ClassLoader must be provided");
        }
        this.addedClassLoaders = null;
        this.overrideClassLoaders = new ArrayList<ClassLoader>();
        for (final ClassLoader classLoader : array) {
            if (classLoader != null) {
                this.overrideClassLoaders.add(classLoader);
            }
        }
    }
    
    public void overrideModuleLayers(final Object... elements) {
        if (elements == null) {
            throw new IllegalArgumentException("overrideModuleLayers cannot be null");
        }
        if (elements.length == 0) {
            throw new IllegalArgumentException("At least one override ModuleLayer must be provided");
        }
        for (int length = elements.length, i = 0; i < length; ++i) {
            if (!isModuleLayer(elements[i])) {
                throw new IllegalArgumentException("moduleLayer must be of type java.lang.ModuleLayer");
            }
        }
        this.addedModuleLayers = null;
        this.overrideModuleLayers = new ArrayList<Object>();
        Collections.addAll(this.overrideModuleLayers, elements);
    }
    
    public void addModuleLayer(final Object o) {
        if (!isModuleLayer(o)) {
            throw new IllegalArgumentException("moduleLayer must be of type java.lang.ModuleLayer");
        }
        if (this.addedModuleLayers == null) {
            this.addedModuleLayers = new ArrayList<Object>();
        }
        this.addedModuleLayers.add(o);
    }
    
    public void filterClasspathElements(final ClassGraph.ClasspathElementFilter classpathElementFilter) {
        if (this.classpathElementFilters == null) {
            this.classpathElementFilters = new ArrayList<ClassGraph.ClasspathElementFilter>(2);
        }
        this.classpathElementFilters.add(classpathElementFilter);
    }
    
    public boolean classfileIsSpecificallyAccepted(final String s) {
        return this.classfilePathAcceptReject.isSpecificallyAcceptedAndNotRejected(s);
    }
    
    public boolean classOrPackageIsRejected(final String s) {
        return this.classAcceptReject.isRejected(s) || this.packagePrefixAcceptReject.isRejected(s);
    }
    
    public enum ScanSpecPathMatch
    {
        NOT_WITHIN_ACCEPTED_PATH, 
        HAS_ACCEPTED_PATH_PREFIX, 
        ANCESTOR_OF_ACCEPTED_PATH, 
        AT_ACCEPTED_CLASS_PACKAGE, 
        AT_ACCEPTED_PATH, 
        HAS_REJECTED_PATH_PREFIX;
    }
}
