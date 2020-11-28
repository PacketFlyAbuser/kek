// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.net.URI;
import java.net.MalformedURLException;
import java.net.URL;
import nonapi.io.github.classgraph.json.JSONSerializer;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import nonapi.io.github.classgraph.utils.JarUtils;
import java.util.regex.Matcher;
import nonapi.io.github.classgraph.concurrency.AutoCloseableExecutorService;
import nonapi.io.github.classgraph.json.JSONDeserializer;
import java.util.regex.Pattern;
import java.util.Collection;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.io.File;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.concurrent.atomic.AtomicInteger;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.Map;
import java.io.Closeable;

public final class ScanResult implements Closeable, AutoCloseable
{
    /* synthetic */ Map<String, ClassInfo> classNameToClassInfo;
    private /* synthetic */ boolean isObtainedFromDeserialization;
    private /* synthetic */ ClassGraphClassLoader classGraphClassLoader;
    private static /* synthetic */ Set<WeakReference<ScanResult>> nonClosedWeakReferences;
    private /* synthetic */ List<String> rawClasspathEltOrderStrs;
    private /* synthetic */ Map<String, PackageInfo> packageNameToPackageInfo;
    private /* synthetic */ ClassLoader[] classLoaderOrderRespectingParentDelegation;
    private final /* synthetic */ AtomicBoolean closed;
    private /* synthetic */ Map<String, ResourceList> pathToAcceptedResourcesCached;
    private /* synthetic */ List<ClasspathElement> classpathOrder;
    private /* synthetic */ NestedJarHandler nestedJarHandler;
    private /* synthetic */ ResourceList allAcceptedResourcesCached;
    private final /* synthetic */ WeakReference<ScanResult> weakReference;
    private final /* synthetic */ AtomicInteger getResourcesWithPathCallCount;
    private final /* synthetic */ LogNode topLevelLog;
    /* synthetic */ ScanSpec scanSpec;
    private /* synthetic */ Map<File, Long> fileToLastModified;
    private static final /* synthetic */ AtomicBoolean initialized;
    private /* synthetic */ Map<String, ModuleInfo> moduleNameToModuleInfo;
    
    public PackageInfo getPackageInfo(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return this.packageNameToPackageInfo.get(s);
    }
    
    public ClassInfoList getAllAnnotations() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() and #enableAnnotationInfo() before #scan()");
        }
        return ClassInfo.getAllAnnotationClasses(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public Map<ClassInfo, ClassInfoList> getClassDependencyMap() {
        final HashMap<ClassInfo, ClassInfoList> hashMap = new HashMap<ClassInfo, ClassInfoList>();
        for (final ClassInfo classInfo : this.getAllClasses()) {
            hashMap.put(classInfo, classInfo.getClassDependencies());
        }
        return hashMap;
    }
    
    ClassLoader[] getClassLoaderOrderRespectingParentDelegation() {
        return this.classLoaderOrderRespectingParentDelegation;
    }
    
    public ModuleInfo getModuleInfo(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return this.moduleNameToModuleInfo.get(s);
    }
    
    public ClassInfoList getSubclasses(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        if (s.equals("java.lang.Object")) {
            return this.getAllStandardClasses();
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getSubclasses();
    }
    
    public List<ModuleRef> getModules() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ArrayList<ModuleRef> list = new ArrayList<ModuleRef>();
        for (final ClasspathElement classpathElement : this.classpathOrder) {
            if (classpathElement instanceof ClasspathElementModule) {
                list.add(((ClasspathElementModule)classpathElement).getModuleRef());
            }
        }
        return list;
    }
    
    public ClassInfoList getAllClasses() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return ClassInfo.getAllClasses(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public Map<String, ResourceList> getAllResourcesAsMap() {
        if (this.pathToAcceptedResourcesCached == null) {
            final HashMap<String, ResourceList> pathToAcceptedResourcesCached = (HashMap<String, ResourceList>)new HashMap<Object, ResourceList>();
            for (final Resource resource : this.getAllResources()) {
                ResourceList list = pathToAcceptedResourcesCached.get(resource.getPath());
                if (list == null) {
                    pathToAcceptedResourcesCached.put(resource.getPath(), list = new ResourceList());
                }
                list.add(resource);
            }
            this.pathToAcceptedResourcesCached = pathToAcceptedResourcesCached;
        }
        return this.pathToAcceptedResourcesCached;
    }
    
    public ClassInfoList getAllInterfacesAndAnnotations() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() and #enableAnnotationInfo() before #scan()");
        }
        return ClassInfo.getAllInterfacesOrAnnotationClasses(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public ClassInfo getClassInfo(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return this.classNameToClassInfo.get(s);
    }
    
    public ResourceList getResourcesWithExtension(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ResourceList allResources = this.getAllResources();
        if (allResources.isEmpty()) {
            return ResourceList.EMPTY_LIST;
        }
        String substring;
        for (substring = s; substring.startsWith("."); substring = substring.substring(1)) {}
        final ResourceList list = new ResourceList();
        for (final Resource resource : allResources) {
            final String path = resource.getPath();
            final int lastIndex = path.lastIndexOf(47);
            final int lastIndex2 = path.lastIndexOf(46);
            if (lastIndex2 > lastIndex && path.substring(lastIndex2 + 1).equalsIgnoreCase(substring)) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public ModuleInfoList getModuleInfo() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return new ModuleInfoList(this.moduleNameToModuleInfo.values());
    }
    
    public ResourceList getResourcesWithLeafName(final String anObject) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ResourceList allResources = this.getAllResources();
        if (allResources.isEmpty()) {
            return ResourceList.EMPTY_LIST;
        }
        final ResourceList list = new ResourceList();
        for (final Resource resource : allResources) {
            final String path = resource.getPath();
            if (path.substring(path.lastIndexOf(47) + 1).equals(anObject)) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public ClassInfoList getAllInterfaces() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return ClassInfo.getAllImplementedInterfaceClasses(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public ResourceList getResourcesWithPathIgnoringAccept(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final String sanitizeEntryPath = FileUtils.sanitizeEntryPath(s, true, true);
        final ResourceList list = new ResourceList();
        final Iterator<ClasspathElement> iterator = this.classpathOrder.iterator();
        while (iterator.hasNext()) {
            final Resource resource = iterator.next().getResource(sanitizeEntryPath);
            if (resource != null) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public static void closeAll() {
        final Iterator<WeakReference<ScanResult>> iterator = new ArrayList<WeakReference<ScanResult>>(ScanResult.nonClosedWeakReferences).iterator();
        while (iterator.hasNext()) {
            final ScanResult scanResult = iterator.next().get();
            if (scanResult != null) {
                scanResult.close();
            }
        }
    }
    
    public ClassInfoList getInterfaces(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getInterfaces();
    }
    
    public ClassInfoList getClassesImplementing(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getClassesImplementing();
    }
    
    public ClassInfoList getAllStandardClasses() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return ClassInfo.getAllStandardClasses(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public String toJSON() {
        return this.toJSON(0);
    }
    
    public static ScanResult fromJSON(final String input) {
        final Matcher matcher = Pattern.compile("\\{[\\n\\r ]*\"format\"[ ]?:[ ]?\"([^\"]+)\"").matcher(input);
        if (!matcher.find()) {
            throw new IllegalArgumentException("JSON is not in correct format");
        }
        if (!"10".equals(matcher.group(1))) {
            throw new IllegalArgumentException("JSON was serialized in a different format from the format used by the current version of ClassGraph -- please serialize and deserialize your ScanResult using the same version of ClassGraph");
        }
        final SerializationFormat serializationFormat = JSONDeserializer.deserializeObject(SerializationFormat.class, input);
        if (!serializationFormat.format.equals("10")) {
            throw new IllegalArgumentException("JSON was serialized by newer version of ClassGraph");
        }
        final ClassGraph classGraph = new ClassGraph();
        classGraph.scanSpec = serializationFormat.scanSpec;
        ScanResult classpathScanResult;
        try (final AutoCloseableExecutorService autoCloseableExecutorService = new AutoCloseableExecutorService(ClassGraph.DEFAULT_NUM_WORKER_THREADS)) {
            classpathScanResult = classGraph.getClasspathScanResult(autoCloseableExecutorService);
        }
        classpathScanResult.rawClasspathEltOrderStrs = serializationFormat.classpath;
        classpathScanResult.scanSpec = serializationFormat.scanSpec;
        classpathScanResult.classNameToClassInfo = new HashMap<String, ClassInfo>();
        if (serializationFormat.classInfo != null) {
            for (final ClassInfo classInfo : serializationFormat.classInfo) {
                classpathScanResult.classNameToClassInfo.put(classInfo.getName(), classInfo);
                classInfo.setScanResult(classpathScanResult);
            }
        }
        classpathScanResult.moduleNameToModuleInfo = new HashMap<String, ModuleInfo>();
        if (serializationFormat.moduleInfo != null) {
            for (final ModuleInfo moduleInfo : serializationFormat.moduleInfo) {
                classpathScanResult.moduleNameToModuleInfo.put(moduleInfo.getName(), moduleInfo);
            }
        }
        classpathScanResult.packageNameToPackageInfo = new HashMap<String, PackageInfo>();
        if (serializationFormat.packageInfo != null) {
            for (final PackageInfo packageInfo : serializationFormat.packageInfo) {
                classpathScanResult.packageNameToPackageInfo.put(packageInfo.getName(), packageInfo);
            }
        }
        classpathScanResult.indexResourcesAndClassInfo();
        classpathScanResult.isObtainedFromDeserialization = true;
        return classpathScanResult;
    }
    
    public String getClasspath() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        return JarUtils.pathElementsToPathStr(this.getClasspathFiles());
    }
    
    public String toJSON(final int n) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        final ArrayList<ClassInfo> list = new ArrayList<ClassInfo>(this.classNameToClassInfo.values());
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
        final ArrayList<PackageInfo> list2 = (ArrayList<PackageInfo>)new ArrayList<Comparable>(this.packageNameToPackageInfo.values());
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list2);
        final ArrayList<ModuleInfo> list3 = (ArrayList<ModuleInfo>)new ArrayList<Comparable>(this.moduleNameToModuleInfo.values());
        CollectionUtils.sortIfNotEmpty((List<Comparable>)list3);
        return JSONSerializer.serializeObject(new SerializationFormat("10", this.scanSpec, list, list2, list3, this.rawClasspathEltOrderStrs), n, false);
    }
    
    public Class<?> loadClass(final String s, final boolean b) throws IllegalArgumentException {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (s == null || s.isEmpty()) {
            throw new NullPointerException("className cannot be null or empty");
        }
        try {
            return Class.forName(s, this.scanSpec.initializeLoadedClasses, this.classGraphClassLoader);
        }
        catch (ClassNotFoundException | LinkageError ex) {
            final LinkageError linkageError;
            final LinkageError obj = linkageError;
            if (b) {
                return null;
            }
            throw new IllegalArgumentException("Could not load class " + s + " : " + obj);
        }
    }
    
    public List<URL> getClasspathURLs() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ArrayList<URL> list = new ArrayList<URL>();
        for (final ClasspathElement classpathElement : this.classpathOrder) {
            try {
                final URI uri = classpathElement.getURI();
                if (uri == null) {
                    continue;
                }
                list.add(uri.toURL());
            }
            catch (IllegalArgumentException ex) {}
            catch (MalformedURLException ex2) {}
        }
        return list;
    }
    
    public ClassInfoList getClassesWithFieldAnnotation(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableFieldInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo(), #enableFieldInfo(), and #enableAnnotationInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getClassesWithFieldAnnotation();
    }
    
    public ResourceList getResourcesMatchingPattern(final Pattern pattern) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ResourceList allResources = this.getAllResources();
        if (allResources.isEmpty()) {
            return ResourceList.EMPTY_LIST;
        }
        final ResourceList list = new ResourceList();
        for (final Resource resource : allResources) {
            if (pattern.matcher(resource.getPath()).matches()) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public List<URI> getClasspathURIs() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ArrayList<URI> list = new ArrayList<URI>();
        for (final ClasspathElement classpathElement : this.classpathOrder) {
            try {
                final URI uri = classpathElement.getURI();
                if (uri == null) {
                    continue;
                }
                list.add(uri);
            }
            catch (IllegalArgumentException ex) {}
        }
        return list;
    }
    
    public ClassInfoList getClassesWithAnnotation(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() and #enableAnnotationInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getClassesWithAnnotation();
    }
    
    public PackageInfoList getPackageInfo() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return new PackageInfoList(this.packageNameToPackageInfo.values());
    }
    
    public ClassInfoList getAllRecords() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return ClassInfo.getAllRecords(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    ScanResult(final ScanSpec scanSpec, final List<ClasspathElement> classpathOrder, final List<String> rawClasspathEltOrderStrs, final ClassLoader[] classLoaderOrderRespectingParentDelegation, final Map<String, ClassInfo> classNameToClassInfo, final Map<String, PackageInfo> packageNameToPackageInfo, final Map<String, ModuleInfo> moduleNameToModuleInfo, final Map<File, Long> fileToLastModified, final NestedJarHandler nestedJarHandler, final LogNode topLevelLog) {
        this.getResourcesWithPathCallCount = new AtomicInteger();
        this.closed = new AtomicBoolean(false);
        this.scanSpec = scanSpec;
        this.rawClasspathEltOrderStrs = rawClasspathEltOrderStrs;
        this.classpathOrder = classpathOrder;
        this.classLoaderOrderRespectingParentDelegation = classLoaderOrderRespectingParentDelegation;
        this.fileToLastModified = fileToLastModified;
        this.classNameToClassInfo = classNameToClassInfo;
        this.packageNameToPackageInfo = packageNameToPackageInfo;
        this.moduleNameToModuleInfo = moduleNameToModuleInfo;
        this.nestedJarHandler = nestedJarHandler;
        this.topLevelLog = topLevelLog;
        if (classNameToClassInfo != null) {
            this.indexResourcesAndClassInfo();
        }
        if (classNameToClassInfo != null) {
            final HashSet<String> set = new HashSet<String>();
            for (final ClassInfo classInfo : classNameToClassInfo.values()) {
                if (classInfo.isAnnotation() && classInfo.annotationInfo != null) {
                    final AnnotationInfo annotationInfo = classInfo.annotationInfo.get("java.lang.annotation.Repeatable");
                    if (annotationInfo == null) {
                        continue;
                    }
                    final AnnotationParameterValueList parameterValues = annotationInfo.getParameterValues();
                    if (parameterValues.isEmpty()) {
                        continue;
                    }
                    final Object value = parameterValues.getValue("value");
                    if (!(value instanceof AnnotationClassRef)) {
                        continue;
                    }
                    final String name = ((AnnotationClassRef)value).getName();
                    if (name == null) {
                        continue;
                    }
                    set.add(name);
                }
            }
            if (!set.isEmpty()) {
                final Iterator<ClassInfo> iterator2 = classNameToClassInfo.values().iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().handleRepeatableAnnotations(set);
                }
            }
        }
        this.classGraphClassLoader = new ClassGraphClassLoader(this);
        this.weakReference = new WeakReference<ScanResult>(this);
        ScanResult.nonClosedWeakReferences.add(this.weakReference);
    }
    
    public Map<ClassInfo, ClassInfoList> getReverseClassDependencyMap() {
        final HashMap<ClassInfo, Set<ClassInfo>> hashMap = (HashMap<ClassInfo, Set<ClassInfo>>)new HashMap<Object, Object>();
        for (final ClassInfo classInfo : this.getAllClasses()) {
            for (final ClassInfo classInfo2 : classInfo.getClassDependencies()) {
                Set<ClassInfo> set = hashMap.get(classInfo2);
                if (set == null) {
                    hashMap.put(classInfo2, set = new HashSet<ClassInfo>());
                }
                set.add(classInfo);
            }
        }
        final HashMap<Object, ClassInfoList> hashMap2 = (HashMap<Object, ClassInfoList>)new HashMap<ClassInfo, ClassInfoList>();
        for (final Map.Entry<ClassInfo, HashSet<ClassInfo>> entry : hashMap.entrySet()) {
            hashMap2.put(entry.getKey(), new ClassInfoList(entry.getValue(), true));
        }
        return (Map<ClassInfo, ClassInfoList>)hashMap2;
    }
    
    @Deprecated
    public ResourceList getResourcesWithPathIgnoringWhitelist(final String s) {
        return this.getResourcesWithPathIgnoringAccept(s);
    }
    
    static void init() {
        if (!ScanResult.initialized.getAndSet(true)) {
            FileUtils.closeDirectByteBuffer(ByteBuffer.allocateDirect(32), null);
        }
    }
    
    public ClassInfoList getAnnotationsOnClass(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() and #enableAnnotationInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getAnnotations();
    }
    
    public ResourceList getResourcesWithPath(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final String sanitizeEntryPath = FileUtils.sanitizeEntryPath(s, true, true);
        if (this.getResourcesWithPathCallCount.incrementAndGet() > 3) {
            return this.getAllResourcesAsMap().get(sanitizeEntryPath);
        }
        ResourceList list = null;
        final Iterator<ClasspathElement> iterator = this.classpathOrder.iterator();
        while (iterator.hasNext()) {
            for (final Resource resource : iterator.next().acceptedResources) {
                if (resource.getPath().equals(sanitizeEntryPath)) {
                    if (list == null) {
                        list = new ResourceList();
                    }
                    list.add(resource);
                }
            }
        }
        return (list == null) ? ResourceList.EMPTY_LIST : list;
    }
    
    public ModulePathInfo getModulePathInfo() {
        return this.scanSpec.modulePathInfo;
    }
    
    public ClassInfoList getAllEnums() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return ClassInfo.getAllEnums(this.classNameToClassInfo.values(), this.scanSpec);
    }
    
    public boolean isObtainedFromDeserialization() {
        return this.isObtainedFromDeserialization;
    }
    
    public long classpathContentsLastModifiedTime() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        long n = 0L;
        if (this.fileToLastModified != null) {
            final long currentTimeMillis = System.currentTimeMillis();
            for (final long longValue : this.fileToLastModified.values()) {
                if (longValue > n && longValue < currentTimeMillis) {
                    n = longValue;
                }
            }
        }
        return n;
    }
    
    @Override
    public void close() {
        if (!this.closed.getAndSet(true)) {
            ScanResult.nonClosedWeakReferences.remove(this.weakReference);
            if (this.classpathOrder != null) {
                this.classpathOrder.clear();
                this.classpathOrder = null;
            }
            if (this.allAcceptedResourcesCached != null) {
                final Iterator iterator = this.allAcceptedResourcesCached.iterator();
                while (iterator.hasNext()) {
                    iterator.next().close();
                }
                this.allAcceptedResourcesCached.clear();
                this.allAcceptedResourcesCached = null;
            }
            if (this.pathToAcceptedResourcesCached != null) {
                this.pathToAcceptedResourcesCached.clear();
                this.pathToAcceptedResourcesCached = null;
            }
            this.classGraphClassLoader = null;
            if (this.classNameToClassInfo != null) {}
            if (this.packageNameToPackageInfo != null) {
                this.packageNameToPackageInfo.clear();
                this.packageNameToPackageInfo = null;
            }
            if (this.moduleNameToModuleInfo != null) {
                this.moduleNameToModuleInfo.clear();
                this.moduleNameToModuleInfo = null;
            }
            if (this.fileToLastModified != null) {
                this.fileToLastModified.clear();
                this.fileToLastModified = null;
            }
            if (this.nestedJarHandler != null) {
                this.nestedJarHandler.close(this.topLevelLog);
                this.nestedJarHandler = null;
            }
            this.classGraphClassLoader = null;
            this.classLoaderOrderRespectingParentDelegation = null;
            if (this.topLevelLog != null) {
                this.topLevelLog.flush();
            }
        }
    }
    
    public ClassInfoList getClassesWithMethodAnnotation(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableMethodInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo(), #enableMethodInfo(), and #enableAnnotationInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getClassesWithMethodAnnotation();
    }
    
    public <T> Class<T> loadClass(final String s, final Class<T> clazz, final boolean b) throws IllegalArgumentException {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (s == null || s.isEmpty()) {
            throw new NullPointerException("className cannot be null or empty");
        }
        if (clazz == null) {
            throw new NullPointerException("superclassOrInterfaceType parameter cannot be null");
        }
        Class<?> forName;
        try {
            forName = Class.forName(s, this.scanSpec.initializeLoadedClasses, this.classGraphClassLoader);
        }
        catch (ClassNotFoundException | LinkageError ex) {
            final LinkageError linkageError;
            final LinkageError obj = linkageError;
            if (b) {
                return null;
            }
            throw new IllegalArgumentException("Could not load class " + s + " : " + obj);
        }
        if (forName == null || clazz.isAssignableFrom(forName)) {
            return (Class<T>)forName;
        }
        if (b) {
            return null;
        }
        throw new IllegalArgumentException("Loaded class " + forName.getName() + " cannot be cast to " + clazz.getName());
    }
    
    public List<File> getClasspathFiles() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        final ArrayList<File> list = new ArrayList<File>();
        final Iterator<ClasspathElement> iterator = this.classpathOrder.iterator();
        while (iterator.hasNext()) {
            final File file = iterator.next().getFile();
            if (file != null) {
                list.add(file);
            }
        }
        return list;
    }
    
    public boolean classpathContentsModifiedSinceScan() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (this.fileToLastModified == null) {
            return true;
        }
        for (final Map.Entry<File, Long> entry : this.fileToLastModified.entrySet()) {
            if (entry.getKey().lastModified() != entry.getValue()) {
                return true;
            }
        }
        return false;
    }
    
    private void indexResourcesAndClassInfo() {
        final Iterator<ClassInfo> iterator = this.classNameToClassInfo.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().setScanResult(this);
        }
        if (this.scanSpec.enableInterClassDependencies) {
            for (final ClassInfo classInfo : new ArrayList<ClassInfo>(this.classNameToClassInfo.values())) {
                final HashSet<ClassInfo> set = new HashSet<ClassInfo>();
                for (final ClassInfo classInfo2 : classInfo.findReferencedClassInfo()) {
                    if (classInfo2 != null && !classInfo.equals(classInfo2) && !classInfo2.getName().equals("java.lang.Object") && (!classInfo2.isExternalClass() || this.scanSpec.enableExternalClasses)) {
                        classInfo2.setScanResult(this);
                        set.add(classInfo2);
                    }
                }
                classInfo.setReferencedClasses(new ClassInfoList(set, true));
            }
        }
    }
    
    public ClassInfoList getSuperclasses(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getSuperclasses();
    }
    
    public ClassInfoList getClassesWithMethodParameterAnnotation(final String s) {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo || !this.scanSpec.enableMethodInfo || !this.scanSpec.enableAnnotationInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo(), #enableMethodInfo(), and #enableAnnotationInfo() before #scan()");
        }
        final ClassInfo classInfo = this.classNameToClassInfo.get(s);
        return (classInfo == null) ? ClassInfoList.EMPTY_LIST : classInfo.getClassesWithMethodParameterAnnotation();
    }
    
    public Map<String, ClassInfo> getAllClassesAsMap() {
        if (this.closed.get()) {
            throw new IllegalArgumentException("Cannot use a ScanResult after it has been closed");
        }
        if (!this.scanSpec.enableClassInfo) {
            throw new IllegalArgumentException("Please call ClassGraph#enableClassInfo() before #scan()");
        }
        return this.classNameToClassInfo;
    }
    
    public ResourceList getAllResources() {
        if (this.allAcceptedResourcesCached == null) {
            final ResourceList allAcceptedResourcesCached = new ResourceList();
            final Iterator<ClasspathElement> iterator = this.classpathOrder.iterator();
            while (iterator.hasNext()) {
                allAcceptedResourcesCached.addAll(iterator.next().acceptedResources);
            }
            this.allAcceptedResourcesCached = allAcceptedResourcesCached;
        }
        return this.allAcceptedResourcesCached;
    }
    
    static {
        CURRENT_SERIALIZATION_FORMAT = "10";
        ScanResult.nonClosedWeakReferences = Collections.newSetFromMap(new ConcurrentHashMap<WeakReference<ScanResult>, Boolean>());
        initialized = new AtomicBoolean(false);
    }
    
    private static class SerializationFormat
    {
        public /* synthetic */ ScanSpec scanSpec;
        public /* synthetic */ String format;
        public /* synthetic */ List<String> classpath;
        public /* synthetic */ List<ModuleInfo> moduleInfo;
        public /* synthetic */ List<ClassInfo> classInfo;
        public /* synthetic */ List<PackageInfo> packageInfo;
        
        public SerializationFormat(final String format, final ScanSpec scanSpec, final List<ClassInfo> classInfo, final List<PackageInfo> packageInfo, final List<ModuleInfo> moduleInfo, final List<String> classpath) {
            this.format = format;
            this.scanSpec = scanSpec;
            this.classpath = classpath;
            this.classInfo = classInfo;
            this.packageInfo = packageInfo;
            this.moduleInfo = moduleInfo;
        }
        
        public SerializationFormat() {
        }
    }
}
