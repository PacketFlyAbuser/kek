// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.net.URI;
import java.util.concurrent.Callable;
import java.io.File;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.Iterator;
import nonapi.io.github.classgraph.classpath.SystemJarFinder;
import java.util.List;
import nonapi.io.github.classgraph.utils.VersionFinder;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import nonapi.io.github.classgraph.concurrency.InterruptionChecker;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import nonapi.io.github.classgraph.concurrency.AutoCloseableExecutorService;
import nonapi.io.github.classgraph.scanspec.AcceptReject;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.scanspec.ScanSpec;

public class ClassGraph
{
    /* synthetic */ ScanSpec scanSpec;
    static final /* synthetic */ int DEFAULT_NUM_WORKER_THREADS;
    private /* synthetic */ LogNode topLevelLog;
    
    public ClassGraph rejectJars(final String... array) {
        for (final String s : array) {
            final String leafName = JarUtils.leafName(s);
            if (!leafName.equals(s)) {
                throw new IllegalArgumentException("Can only reject jars by leafname: " + s);
            }
            this.scanSpec.jarAcceptReject.addToReject(leafName);
        }
        return this;
    }
    
    @Deprecated
    public ClassGraph whitelistPaths(final String... array) {
        return this.acceptPaths(array);
    }
    
    public ClassGraph overrideClasspath(final String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Can't override classpath with an empty path");
        }
        final String[] smartPathSplit = JarUtils.smartPathSplit(s, this.scanSpec);
        for (int length = smartPathSplit.length, i = 0; i < length; ++i) {
            this.scanSpec.addClasspathOverride(smartPathSplit[i]);
        }
        return this;
    }
    
    @Deprecated
    public ClassGraph whitelistClasspathElementsContainingResourcePath(final String... array) {
        return this.acceptClasspathElementsContainingResourcePath(array);
    }
    
    @Deprecated
    public ClassGraph blacklistClasspathElementsContainingResourcePath(final String... array) {
        return this.rejectClasspathElementsContainingResourcePath(array);
    }
    
    public ClassGraph enableSystemJarsAndModules() {
        this.enableClassInfo();
        this.scanSpec.enableSystemJarsAndModules = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph blacklistPaths(final String... array) {
        return this.rejectPaths(array);
    }
    
    public ClassGraph ignoreClassVisibility() {
        this.enableClassInfo();
        this.scanSpec.ignoreClassVisibility = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph whitelistClasses(final String... array) {
        return this.acceptClasses(array);
    }
    
    @Deprecated
    public ClassGraph blacklistPackages(final String... array) {
        return this.rejectPackages(array);
    }
    
    public ClassGraph enableURLScheme(final String s) {
        this.scanSpec.enableURLScheme(s);
        return this;
    }
    
    @Deprecated
    public ClassGraph blacklistClasses(final String... array) {
        return this.rejectClasses(array);
    }
    
    public ClassGraph enableRemoteJarScanning() {
        this.scanSpec.enableURLScheme("http");
        this.scanSpec.enableURLScheme("https");
        return this;
    }
    
    public ClassGraph ignoreFieldVisibility() {
        this.enableClassInfo();
        this.enableFieldInfo();
        this.scanSpec.ignoreFieldVisibility = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph blacklistJars(final String... array) {
        return this.rejectJars(array);
    }
    
    public ClassGraph acceptPaths(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final String normalizePath = AcceptReject.normalizePath(array[i]);
            final String pathToPackageName = AcceptReject.pathToPackageName(normalizePath);
            this.scanSpec.packageAcceptReject.addToAccept(pathToPackageName);
            this.scanSpec.pathAcceptReject.addToAccept(normalizePath + "/");
            if (normalizePath.isEmpty()) {
                this.scanSpec.pathAcceptReject.addToAccept("");
            }
            if (!normalizePath.contains("*")) {
                if (normalizePath.isEmpty()) {
                    this.scanSpec.packagePrefixAcceptReject.addToAccept("");
                    this.scanSpec.pathPrefixAcceptReject.addToAccept("");
                }
                else {
                    this.scanSpec.packagePrefixAcceptReject.addToAccept(pathToPackageName + ".");
                    this.scanSpec.pathPrefixAcceptReject.addToAccept(normalizePath + "/");
                }
            }
        }
        return this;
    }
    
    public ClassGraph addClassLoader(final ClassLoader classLoader) {
        this.scanSpec.addClassLoader(classLoader);
        return this;
    }
    
    public ClassGraph acceptJars(final String... array) {
        for (final String s : array) {
            final String leafName = JarUtils.leafName(s);
            if (!leafName.equals(s)) {
                throw new IllegalArgumentException("Can only accept jars by leafname: " + s);
            }
            this.scanSpec.jarAcceptReject.addToAccept(leafName);
        }
        return this;
    }
    
    public ClassGraph acceptClasspathElementsContainingResourcePath(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scanSpec.classpathElementResourcePathAcceptReject.addToAccept(AcceptReject.normalizePath(array[i]));
        }
        return this;
    }
    
    public ClassGraph rejectClasspathElementsContainingResourcePath(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scanSpec.classpathElementResourcePathAcceptReject.addToReject(AcceptReject.normalizePath(array[i]));
        }
        return this;
    }
    
    public ClassGraph addModuleLayer(final Object o) {
        this.scanSpec.addModuleLayer(o);
        return this;
    }
    
    public ClassGraph enableRealtimeLogging() {
        this.verbose();
        LogNode.logInRealtime(true);
        return this;
    }
    
    ScanResult getClasspathScanResult(final AutoCloseableExecutorService autoCloseableExecutorService) {
        try {
            final ScanResult scanResult = this.scanAsync(false, autoCloseableExecutorService, ClassGraph.DEFAULT_NUM_WORKER_THREADS).get();
            if (scanResult == null) {
                throw new NullPointerException();
            }
            return scanResult;
        }
        catch (InterruptedException | CancellationException ex2) {
            final Object o;
            throw ClassGraphException.newClassGraphException("Scan interrupted", (Throwable)o);
        }
        catch (ExecutionException ex) {
            throw ClassGraphException.newClassGraphException("Uncaught exception during scan", InterruptionChecker.getCause(ex));
        }
    }
    
    public Future<ScanResult> scanAsync(final ExecutorService executorService, final int n) {
        return this.scanAsync(true, executorService, n);
    }
    
    public ClassGraph disableDirScanning() {
        this.scanSpec.scanDirs = false;
        return this;
    }
    
    @Deprecated
    public ClassGraph whitelistPackagesNonRecursive(final String... array) {
        return this.acceptPackagesNonRecursive(array);
    }
    
    @Deprecated
    public ClassGraph whitelistLibOrExtJars(final String... array) {
        return this.acceptLibOrExtJars(array);
    }
    
    public ClassGraph rejectPaths(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final String normalizePath = AcceptReject.normalizePath(array[i]);
            if (normalizePath.isEmpty()) {
                throw new IllegalArgumentException("Rejecting the root package (\"\") will cause nothing to be scanned");
            }
            final String pathToPackageName = AcceptReject.pathToPackageName(normalizePath);
            this.scanSpec.packageAcceptReject.addToReject(pathToPackageName);
            this.scanSpec.pathAcceptReject.addToReject(normalizePath + "/");
            if (!normalizePath.contains("*")) {
                this.scanSpec.packagePrefixAcceptReject.addToReject(pathToPackageName + ".");
                this.scanSpec.pathPrefixAcceptReject.addToReject(normalizePath + "/");
            }
        }
        return this;
    }
    
    public ClassGraph acceptClasses(final String... array) {
        this.enableClassInfo();
        for (final String str : array) {
            if (str.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + str);
            }
            final String normalizePackageOrClassName = AcceptReject.normalizePackageOrClassName(str);
            this.scanSpec.classAcceptReject.addToAccept(normalizePackageOrClassName);
            this.scanSpec.classfilePathAcceptReject.addToAccept(AcceptReject.classNameToClassfilePath(normalizePackageOrClassName));
            final String parentPackageName = PackageInfo.getParentPackageName(normalizePackageOrClassName);
            this.scanSpec.classPackageAcceptReject.addToAccept(parentPackageName);
            this.scanSpec.classPackagePathAcceptReject.addToAccept(AcceptReject.packageNameToPath(parentPackageName) + "/");
        }
        return this;
    }
    
    public static String getVersion() {
        return VersionFinder.getVersion();
    }
    
    public ClassGraph ignoreParentModuleLayers() {
        this.scanSpec.ignoreParentModuleLayers = true;
        return this;
    }
    
    public List<ModuleRef> getModules() {
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(ClassGraph.DEFAULT_NUM_WORKER_THREADS);
             final ScanResult classpathScanResult = this.getClasspathScanResult(autoCloseableExecutorService)) {
            return classpathScanResult.getModules();
        }
    }
    
    public ClassGraph acceptPackagesNonRecursive(final String... array) {
        this.enableClassInfo();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String normalizePackageOrClassName = AcceptReject.normalizePackageOrClassName(array[i]);
            if (normalizePackageOrClassName.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + normalizePackageOrClassName);
            }
            this.scanSpec.packageAcceptReject.addToAccept(normalizePackageOrClassName);
            this.scanSpec.pathAcceptReject.addToAccept(AcceptReject.packageNameToPath(normalizePackageOrClassName) + "/");
            if (normalizePackageOrClassName.isEmpty()) {
                this.scanSpec.pathAcceptReject.addToAccept("");
            }
        }
        return this;
    }
    
    public ScanResult scan(final ExecutorService executorService, final int n) {
        try {
            final ScanResult scanResult = this.scanAsync(executorService, n).get();
            if (scanResult == null) {
                throw new NullPointerException();
            }
            return scanResult;
        }
        catch (InterruptedException | CancellationException ex2) {
            final Object o;
            throw ClassGraphException.newClassGraphException("Scan interrupted", (Throwable)o);
        }
        catch (ExecutionException ex) {
            throw ClassGraphException.newClassGraphException("Uncaught exception during scan", InterruptionChecker.getCause(ex));
        }
    }
    
    public ClassGraph enableStaticFinalFieldConstantInitializerValues() {
        this.enableClassInfo();
        this.enableFieldInfo();
        this.scanSpec.enableStaticFinalFieldConstantInitializerValues = true;
        return this;
    }
    
    public ClassGraph enableMemoryMapping() {
        this.scanSpec.enableMemoryMapping = true;
        return this;
    }
    
    public ClassGraph enableExternalClasses() {
        this.enableClassInfo();
        this.scanSpec.enableExternalClasses = true;
        return this;
    }
    
    private void acceptOrRejectLibOrExtJars(final boolean b, final String... array) {
        if (array.length == 0) {
            final Iterator<String> iterator = SystemJarFinder.getJreLibOrExtJars().iterator();
            while (iterator.hasNext()) {
                this.acceptOrRejectLibOrExtJars(b, JarUtils.leafName(iterator.next()));
            }
        }
        else {
            for (final String s : array) {
                if (!JarUtils.leafName(s).equals(s)) {
                    throw new IllegalArgumentException("Can only " + (b ? "accept" : "reject") + " jars by leafname: " + s);
                }
                if (s.contains("*")) {
                    final Pattern globToPattern = AcceptReject.globToPattern(s);
                    boolean b2 = false;
                    final Iterator<String> iterator2 = SystemJarFinder.getJreLibOrExtJars().iterator();
                    while (iterator2.hasNext()) {
                        final String leafName = JarUtils.leafName(iterator2.next());
                        if (globToPattern.matcher(leafName).matches()) {
                            if (!leafName.contains("*")) {
                                this.acceptOrRejectLibOrExtJars(b, leafName);
                            }
                            b2 = true;
                        }
                    }
                    if (!b2 && this.topLevelLog != null) {
                        this.topLevelLog.log("Could not find lib or ext jar matching wildcard: " + s);
                    }
                }
                else {
                    boolean b3 = false;
                    for (final String str : SystemJarFinder.getJreLibOrExtJars()) {
                        if (s.equals(JarUtils.leafName(str))) {
                            if (b) {
                                this.scanSpec.libOrExtJarAcceptReject.addToAccept(s);
                            }
                            else {
                                this.scanSpec.libOrExtJarAcceptReject.addToReject(s);
                            }
                            if (this.topLevelLog != null) {
                                this.topLevelLog.log((b ? "Accepting" : "Rejecting") + " lib or ext jar: " + str);
                            }
                            b3 = true;
                            break;
                        }
                    }
                    if (!b3 && this.topLevelLog != null) {
                        this.topLevelLog.log("Could not find lib or ext jar: " + s);
                    }
                }
            }
        }
    }
    
    @Deprecated
    public ClassGraph whitelistPathsNonRecursive(final String... array) {
        return this.acceptPathsNonRecursive(array);
    }
    
    public ClassGraph enableClassInfo() {
        this.scanSpec.enableClassInfo = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph blacklistModules(final String... array) {
        return this.rejectModules(array);
    }
    
    public ClassGraph overrideClasspath(final Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException("Can't override classpath with an empty path");
        }
        final Iterator<?> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            this.scanSpec.addClasspathOverride(iterator.next());
        }
        return this;
    }
    
    public ClassGraph disableJarScanning() {
        this.scanSpec.scanJars = false;
        return this;
    }
    
    public ClassGraph rejectPackages(final String... array) {
        this.enableClassInfo();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String normalizePackageOrClassName = AcceptReject.normalizePackageOrClassName(array[i]);
            if (normalizePackageOrClassName.isEmpty()) {
                throw new IllegalArgumentException("Rejecting the root package (\"\") will cause nothing to be scanned");
            }
            this.scanSpec.packageAcceptReject.addToReject(normalizePackageOrClassName);
            final String packageNameToPath = AcceptReject.packageNameToPath(normalizePackageOrClassName);
            this.scanSpec.pathAcceptReject.addToReject(packageNameToPath + "/");
            if (!normalizePackageOrClassName.contains("*")) {
                this.scanSpec.packagePrefixAcceptReject.addToReject(normalizePackageOrClassName + ".");
                this.scanSpec.pathPrefixAcceptReject.addToReject(packageNameToPath + "/");
            }
        }
        return this;
    }
    
    public ClassGraph enableAnnotationInfo() {
        this.enableClassInfo();
        this.scanSpec.enableAnnotationInfo = true;
        return this;
    }
    
    public List<URL> getClasspathURLs() {
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(ClassGraph.DEFAULT_NUM_WORKER_THREADS);
             final ScanResult classpathScanResult = this.getClasspathScanResult(autoCloseableExecutorService)) {
            return classpathScanResult.getClasspathURLs();
        }
    }
    
    public List<File> getClasspathFiles() {
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(ClassGraph.DEFAULT_NUM_WORKER_THREADS);
             final ScanResult classpathScanResult = this.getClasspathScanResult(autoCloseableExecutorService)) {
            return classpathScanResult.getClasspathFiles();
        }
    }
    
    public ClassGraph ignoreParentClassLoaders() {
        this.scanSpec.ignoreParentClassLoaders = true;
        return this;
    }
    
    public ClassGraph enableFieldInfo() {
        this.enableClassInfo();
        this.scanSpec.enableFieldInfo = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph blacklistLibOrExtJars(final String... array) {
        return this.rejectLibOrExtJars(array);
    }
    
    public ClassGraph enableInterClassDependencies() {
        this.enableClassInfo();
        this.enableFieldInfo();
        this.enableMethodInfo();
        this.enableAnnotationInfo();
        this.ignoreClassVisibility();
        this.ignoreFieldVisibility();
        this.ignoreMethodVisibility();
        this.scanSpec.enableInterClassDependencies = true;
        return this;
    }
    
    @Deprecated
    public ClassGraph whitelistPackages(final String... array) {
        return this.acceptPackages(array);
    }
    
    public ClassGraph disableRuntimeInvisibleAnnotations() {
        this.enableClassInfo();
        this.scanSpec.disableRuntimeInvisibleAnnotations = true;
        return this;
    }
    
    public ClassGraph overrideModuleLayers(final Object... array) {
        this.scanSpec.overrideModuleLayers(array);
        return this;
    }
    
    public ClassGraph acceptPackages(final String... array) {
        this.enableClassInfo();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String normalizePackageOrClassName = AcceptReject.normalizePackageOrClassName(array[i]);
            if (normalizePackageOrClassName.startsWith("!") || normalizePackageOrClassName.startsWith("-")) {
                throw new IllegalArgumentException("This style of accepting/rejecting is no longer supported: " + normalizePackageOrClassName);
            }
            this.scanSpec.packageAcceptReject.addToAccept(normalizePackageOrClassName);
            final String packageNameToPath = AcceptReject.packageNameToPath(normalizePackageOrClassName);
            this.scanSpec.pathAcceptReject.addToAccept(packageNameToPath + "/");
            if (normalizePackageOrClassName.isEmpty()) {
                this.scanSpec.pathAcceptReject.addToAccept("");
            }
            if (!normalizePackageOrClassName.contains("*")) {
                if (normalizePackageOrClassName.isEmpty()) {
                    this.scanSpec.packagePrefixAcceptReject.addToAccept("");
                    this.scanSpec.pathPrefixAcceptReject.addToAccept("");
                }
                else {
                    this.scanSpec.packagePrefixAcceptReject.addToAccept(normalizePackageOrClassName + ".");
                    this.scanSpec.pathPrefixAcceptReject.addToAccept(packageNameToPath + "/");
                }
            }
        }
        return this;
    }
    
    private Future<ScanResult> scanAsync(final boolean b, final ExecutorService executorService, final int n) {
        try {
            return executorService.submit((Callable<ScanResult>)new Scanner(b, this.scanSpec, executorService, n, null, null, this.topLevelLog));
        }
        catch (InterruptedException ex) {
            return executorService.submit((Callable<ScanResult>)new Callable<ScanResult>() {
                @Override
                public ScanResult call() throws Exception {
                    throw ex;
                }
            });
        }
    }
    
    public ClassGraph rejectLibOrExtJars(final String... array) {
        this.acceptOrRejectLibOrExtJars(false, array);
        return this;
    }
    
    public ClassGraph overrideClassLoaders(final ClassLoader... array) {
        this.scanSpec.overrideClassLoaders(array);
        return this;
    }
    
    public ClassGraph verbose() {
        if (this.topLevelLog == null) {
            this.topLevelLog = new LogNode();
        }
        return this;
    }
    
    public ScanResult scan(final int n) {
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(n)) {
            return this.scan(autoCloseableExecutorService, n);
        }
    }
    
    public ClassGraph overrideClasspath(final Object... array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Can't override classpath with an empty path");
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scanSpec.addClasspathOverride(array[i]);
        }
        return this;
    }
    
    public ClassGraph rejectModules(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scanSpec.moduleAcceptReject.addToReject(AcceptReject.normalizePackageOrClassName(array[i]));
        }
        return this;
    }
    
    public ClassGraph ignoreMethodVisibility() {
        this.enableClassInfo();
        this.enableMethodInfo();
        this.scanSpec.ignoreMethodVisibility = true;
        return this;
    }
    
    public void scanAsync(final ExecutorService executorService, final int n, final ScanResultProcessor scanResultProcessor, final FailureHandler failureHandler) {
        if (scanResultProcessor == null) {
            throw new IllegalArgumentException("scanResultProcessor cannot be null");
        }
        if (failureHandler == null) {
            throw new IllegalArgumentException("failureHandler cannot be null");
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new Scanner(true, ClassGraph.this.scanSpec, executorService, n, scanResultProcessor, failureHandler, ClassGraph.this.topLevelLog).call();
                }
                catch (InterruptedException | CancellationException | ExecutionException ex) {
                    final Throwable t;
                    failureHandler.onFailure(t);
                }
            }
        });
    }
    
    public ClassGraph enableAllInfo() {
        this.enableClassInfo();
        this.enableFieldInfo();
        this.enableMethodInfo();
        this.enableAnnotationInfo();
        this.enableStaticFinalFieldConstantInitializerValues();
        this.ignoreClassVisibility();
        this.ignoreFieldVisibility();
        this.ignoreMethodVisibility();
        return this;
    }
    
    public ClassGraph acceptModules(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.scanSpec.moduleAcceptReject.addToAccept(AcceptReject.normalizePackageOrClassName(array[i]));
        }
        return this;
    }
    
    public String getClasspath() {
        return JarUtils.pathElementsToPathStr(this.getClasspathFiles());
    }
    
    public ClassGraph rejectClasses(final String... array) {
        this.enableClassInfo();
        for (final String str : array) {
            if (str.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + str);
            }
            final String normalizePackageOrClassName = AcceptReject.normalizePackageOrClassName(str);
            this.scanSpec.classAcceptReject.addToReject(normalizePackageOrClassName);
            this.scanSpec.classfilePathAcceptReject.addToReject(AcceptReject.classNameToClassfilePath(normalizePackageOrClassName));
        }
        return this;
    }
    
    public ClassGraph acceptLibOrExtJars(final String... array) {
        this.acceptOrRejectLibOrExtJars(true, array);
        return this;
    }
    
    public ClassGraph setMaxBufferedJarRAMSize(final int maxBufferedJarRAMSize) {
        this.scanSpec.maxBufferedJarRAMSize = maxBufferedJarRAMSize;
        return this;
    }
    
    static {
        DEFAULT_NUM_WORKER_THREADS = Math.max(2, (int)Math.ceil(Math.min(4.0, Runtime.getRuntime().availableProcessors() * 0.75) + Runtime.getRuntime().availableProcessors() * 1.25));
    }
    
    @Deprecated
    public ClassGraph whitelistModules(final String... array) {
        return this.acceptModules(array);
    }
    
    @Deprecated
    public ClassGraph whitelistJars(final String... array) {
        return this.acceptJars(array);
    }
    
    public ScanResult scan() {
        return this.scan(ClassGraph.DEFAULT_NUM_WORKER_THREADS);
    }
    
    public ClassGraph verbose(final boolean b) {
        if (b) {
            this.verbose();
        }
        return this;
    }
    
    public ClassGraph() {
        this.scanSpec = new ScanSpec();
        ScanResult.init();
    }
    
    public ClassGraph disableNestedJarScanning() {
        this.scanSpec.scanNestedJars = false;
        return this;
    }
    
    public ClassGraph removeTemporaryFilesAfterScan() {
        this.scanSpec.removeTemporaryFilesAfterScan = true;
        return this;
    }
    
    public List<URI> getClasspathURIs() {
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(ClassGraph.DEFAULT_NUM_WORKER_THREADS);
             final ScanResult classpathScanResult = this.getClasspathScanResult(autoCloseableExecutorService)) {
            return classpathScanResult.getClasspathURIs();
        }
    }
    
    public ClassGraph enableMethodInfo() {
        this.enableClassInfo();
        this.scanSpec.enableMethodInfo = true;
        return this;
    }
    
    public ClassGraph disableModuleScanning() {
        this.scanSpec.scanModules = false;
        return this;
    }
    
    public ClassGraph initializeLoadedClasses() {
        this.scanSpec.initializeLoadedClasses = true;
        return this;
    }
    
    public ClassGraph filterClasspathElements(final ClasspathElementFilter classpathElementFilter) {
        this.scanSpec.filterClasspathElements(classpathElementFilter);
        return this;
    }
    
    public ClassGraph acceptPathsNonRecursive(final String... array) {
        for (final String str : array) {
            if (str.contains("*")) {
                throw new IllegalArgumentException("Cannot use a glob wildcard here: " + str);
            }
            final String normalizePath = AcceptReject.normalizePath(str);
            this.scanSpec.packageAcceptReject.addToAccept(AcceptReject.pathToPackageName(normalizePath));
            this.scanSpec.pathAcceptReject.addToAccept(normalizePath + "/");
            if (normalizePath.isEmpty()) {
                this.scanSpec.pathAcceptReject.addToAccept("");
            }
        }
        return this;
    }
    
    public ModulePathInfo getModulePathInfo() {
        return this.scanSpec.modulePathInfo;
    }
    
    @FunctionalInterface
    public interface ScanResultProcessor
    {
        void processScanResult(final ScanResult p0);
    }
    
    @FunctionalInterface
    public interface FailureHandler
    {
        void onFailure(final Throwable p0);
    }
    
    @FunctionalInterface
    public interface ClasspathElementFilter
    {
        boolean includeClasspathElement(final String p0);
    }
}
