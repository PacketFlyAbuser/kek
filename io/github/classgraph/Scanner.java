// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.util.HashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import nonapi.io.github.classgraph.classpath.ModuleFinder;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import nonapi.io.github.classgraph.concurrency.AutoCloseableExecutorService;
import java.io.FileNotFoundException;
import java.io.File;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.nio.file.Path;
import java.nio.file.FileSystemNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.net.URLDecoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.JarUtils;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.concurrency.SingletonMap;
import nonapi.io.github.classgraph.concurrency.InterruptionChecker;
import nonapi.io.github.classgraph.classpath.ClasspathFinder;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.List;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import java.util.Map;
import java.util.Comparator;
import java.util.concurrent.Callable;

class Scanner implements Callable<ScanResult>
{
    private static final /* synthetic */ Comparator<Map.Entry<Integer, ClasspathElement>> INDEXED_CLASSPATH_ELEMENT_COMPARATOR;
    private final /* synthetic */ ClassGraph.FailureHandler failureHandler;
    private final /* synthetic */ NestedJarHandler nestedJarHandler;
    private final /* synthetic */ LogNode topLevelLog;
    private final /* synthetic */ List<ClasspathElementModule> moduleOrder;
    private final /* synthetic */ ScanSpec scanSpec;
    private final /* synthetic */ ClasspathFinder classpathFinder;
    private final /* synthetic */ InterruptionChecker interruptionChecker;
    private final /* synthetic */ SingletonMap<ClasspathOrder.ClasspathElementAndClassLoader, ClasspathElement, IOException> classpathEntryToClasspathElementSingletonMap;
    public /* synthetic */ boolean performScan;
    private final /* synthetic */ ClassLoader[] classLoaderOrderRespectingParentDelegation;
    private final /* synthetic */ ExecutorService executorService;
    private final /* synthetic */ ClassGraph.ScanResultProcessor scanResultProcessor;
    private final /* synthetic */ int numParallelTasks;
    
    private void preprocessClasspathElementsByType(final List<ClasspathElement> list, final LogNode logNode) {
        final ArrayList<AbstractMap.SimpleEntry<Object, ClasspathElementFileDir>> list2 = new ArrayList<AbstractMap.SimpleEntry<Object, ClasspathElementFileDir>>();
        final ArrayList<AbstractMap.SimpleEntry<String, ClasspathElementZip>> list3 = new ArrayList<AbstractMap.SimpleEntry<String, ClasspathElementZip>>();
        for (final ClasspathElement classpathElement : list) {
            if (classpathElement instanceof ClasspathElementFileDir) {
                list2.add(new AbstractMap.SimpleEntry<Object, ClasspathElementFileDir>(((ClasspathElementFileDir)classpathElement).getFile().getPath(), classpathElement));
            }
            else {
                if (!(classpathElement instanceof ClasspathElementZip)) {
                    continue;
                }
                final ClasspathElementZip classpathElementZip = (ClasspathElementZip)classpathElement;
                list3.add(new AbstractMap.SimpleEntry<String, ClasspathElementZip>(classpathElementZip.getZipFilePath(), classpathElement));
                if (classpathElementZip.logicalZipFile == null) {
                    continue;
                }
                if (classpathElementZip.logicalZipFile.addExportsManifestEntryValue != null) {
                    final String[] smartPathSplit = JarUtils.smartPathSplit(classpathElementZip.logicalZipFile.addExportsManifestEntryValue, ' ', this.scanSpec);
                    for (int length = smartPathSplit.length, i = 0; i < length; ++i) {
                        this.scanSpec.modulePathInfo.addExports.add(smartPathSplit[i] + "=ALL-UNNAMED");
                    }
                }
                if (classpathElementZip.logicalZipFile.addOpensManifestEntryValue != null) {
                    final String[] smartPathSplit2 = JarUtils.smartPathSplit(classpathElementZip.logicalZipFile.addOpensManifestEntryValue, ' ', this.scanSpec);
                    for (int length2 = smartPathSplit2.length, j = 0; j < length2; ++j) {
                        this.scanSpec.modulePathInfo.addOpens.add(smartPathSplit2[j] + "=ALL-UNNAMED");
                    }
                }
                if (classpathElementZip.logicalZipFile.automaticModuleNameManifestEntryValue == null) {
                    continue;
                }
                classpathElementZip.moduleNameFromManifestFile = classpathElementZip.logicalZipFile.automaticModuleNameManifestEntryValue;
            }
        }
        this.findNestedClasspathElements((List<AbstractMap.SimpleEntry<String, ClasspathElement>>)list2, logNode);
        this.findNestedClasspathElements((List<AbstractMap.SimpleEntry<String, ClasspathElement>>)list3, logNode);
    }
    
    Scanner(final boolean performScan, final ScanSpec scanSpec, final ExecutorService executorService, final int n, final ClassGraph.ScanResultProcessor scanResultProcessor, final ClassGraph.FailureHandler failureHandler, final LogNode topLevelLog) throws InterruptedException {
        this.classpathEntryToClasspathElementSingletonMap = new SingletonMap<ClasspathOrder.ClasspathElementAndClassLoader, ClasspathElement, IOException>() {
            @Override
            public ClasspathElement newInstance(final ClasspathOrder.ClasspathElementAndClassLoader classpathElementAndClassLoader, final LogNode logNode) throws InterruptedException, IOException {
                Object classpathElementRoot = classpathElementAndClassLoader.classpathElementRoot;
                String other;
                for (other = classpathElementAndClassLoader.dirOrPathPackageRoot; other.startsWith("/"); other = other.substring(1)) {}
                if (classpathElementRoot instanceof String) {
                    final String str = (String)classpathElementRoot;
                    if (JarUtils.URL_SCHEME_PATTERN.matcher(str).matches()) {
                        try {
                            classpathElementRoot = new URL(str);
                        }
                        catch (MalformedURLException ex) {
                            throw new IOException("Malformed URL: " + str);
                        }
                    }
                }
                String s2;
                if (classpathElementRoot instanceof URL) {
                    URL obj = (URL)classpathElementRoot;
                    String s = obj.getProtocol();
                    if ("jar".equals(s)) {
                        try {
                            obj = new URL(URLDecoder.decode(obj.toString(), "UTF-8").substring(4));
                            s = obj.getProtocol();
                        }
                        catch (MalformedURLException cause) {
                            throw new IOException("Could not strip 'jar:' prefix from " + classpathElementRoot, cause);
                        }
                    }
                    if ("file".equals(s)) {
                        s2 = URLDecoder.decode(obj.getPath(), "UTF-8");
                    }
                    else {
                        if ("http".equals(s) || "https".equals(s)) {
                            return new ClasspathElementZip(obj, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
                        }
                        try {
                            final Path value = Paths.get(obj.toURI());
                            if (Files.isDirectory(value, new LinkOption[0])) {
                                return new ClasspathElementPathDir(value, other, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
                            }
                        }
                        catch (URISyntaxException | IllegalArgumentException | SecurityException ex2) {
                            final Throwable t;
                            throw new IOException("Cannot handle URL " + obj + " : " + t.getMessage());
                        }
                        catch (FileSystemNotFoundException ex3) {}
                        return new ClasspathElementZip(obj, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
                    }
                }
                else if (classpathElementRoot instanceof Path) {
                    final Path obj2 = (Path)classpathElementRoot;
                    final Path resolve = obj2.resolve(other);
                    if (FileUtils.canReadAndIsFile(resolve)) {
                        return new ClasspathElementZip(obj2.toUri(), classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
                    }
                    if (FileUtils.canReadAndIsDir(resolve)) {
                        return new ClasspathElementPathDir((Path)classpathElementRoot, other, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
                    }
                    throw new IOException("Path is not a directory or file: " + obj2);
                }
                else {
                    s2 = classpathElementRoot.toString();
                }
                final String resolve2 = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, s2);
                final int index = resolve2.indexOf(33);
                final File canonicalFile = new File((index < 0) ? resolve2 : resolve2.substring(0, index)).getCanonicalFile();
                if (!canonicalFile.exists()) {
                    throw new FileNotFoundException();
                }
                if (!FileUtils.canRead(canonicalFile)) {
                    throw new IOException("Cannot read file or directory");
                }
                boolean b = s2.regionMatches(true, 0, "jar:", 0, 4) || index > 0;
                if (canonicalFile.isFile()) {
                    b = true;
                }
                else {
                    if (!canonicalFile.isDirectory()) {
                        throw new IOException("Not a normal file or directory");
                    }
                    if (b) {
                        throw new IOException("Expected jar, found directory");
                    }
                }
                final String resolve3 = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, canonicalFile.getPath());
                final String str2 = (index < 0) ? resolve3 : (resolve3 + resolve2.substring(index));
                if (!str2.equals(resolve2)) {
                    try {
                        return (ClasspathElement)((SingletonMap<ClasspathOrder.ClasspathElementAndClassLoader, Object, Exception>)this).get(new ClasspathOrder.ClasspathElementAndClassLoader(str2, other, classpathElementAndClassLoader.classLoader), logNode);
                    }
                    catch (NullSingletonException obj3) {
                        throw new IOException("Cannot get classpath element for canonical path " + str2 + " : " + obj3);
                    }
                }
                return b ? new ClasspathElementZip(str2, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec) : new ClasspathElementFileDir(canonicalFile, other, classpathElementAndClassLoader.classLoader, Scanner.this.nestedJarHandler, Scanner.this.scanSpec);
            }
        };
        this.scanSpec = scanSpec;
        this.performScan = performScan;
        scanSpec.sortPrefixes();
        scanSpec.log(topLevelLog);
        if (topLevelLog != null) {
            if (scanSpec.pathAcceptReject != null && scanSpec.packagePrefixAcceptReject.isSpecificallyAccepted("")) {
                topLevelLog.log("Note: There is no need to accept the root package (\"\") -- not accepting anything will have the same effect of causing all packages to be scanned");
            }
            topLevelLog.log("Number of worker threads: " + n);
        }
        this.executorService = executorService;
        this.interruptionChecker = ((executorService instanceof AutoCloseableExecutorService) ? ((AutoCloseableExecutorService)executorService).interruptionChecker : new InterruptionChecker());
        this.nestedJarHandler = new NestedJarHandler(scanSpec, this.interruptionChecker);
        this.numParallelTasks = n;
        this.scanResultProcessor = scanResultProcessor;
        this.failureHandler = failureHandler;
        this.topLevelLog = topLevelLog;
        final LogNode logNode = (topLevelLog == null) ? null : topLevelLog.log("Finding classpath");
        this.classpathFinder = new ClasspathFinder(scanSpec, logNode);
        this.classLoaderOrderRespectingParentDelegation = this.classpathFinder.getClassLoaderOrderRespectingParentDelegation();
        try {
            this.moduleOrder = new ArrayList<ClasspathElementModule>();
            final ModuleFinder moduleFinder = this.classpathFinder.getModuleFinder();
            if (moduleFinder != null) {
                final List<ModuleRef> systemModuleRefs = moduleFinder.getSystemModuleRefs();
                final ClassLoader classLoader = (this.classLoaderOrderRespectingParentDelegation != null && this.classLoaderOrderRespectingParentDelegation.length != 0) ? this.classLoaderOrderRespectingParentDelegation[0] : null;
                if (systemModuleRefs != null) {
                    for (final ModuleRef moduleRef : systemModuleRefs) {
                        final String name = moduleRef.getName();
                        if ((scanSpec.enableSystemJarsAndModules && scanSpec.moduleAcceptReject.acceptAndRejectAreEmpty()) || scanSpec.moduleAcceptReject.isSpecificallyAcceptedAndNotRejected(name)) {
                            final ClasspathElementModule classpathElementModule = new ClasspathElementModule(moduleRef, classLoader, this.nestedJarHandler.moduleRefToModuleReaderProxyRecyclerMap, scanSpec);
                            this.moduleOrder.add(classpathElementModule);
                            classpathElementModule.open(null, logNode);
                        }
                        else {
                            if (logNode == null) {
                                continue;
                            }
                            logNode.log("Skipping non-accepted or rejected system module: " + name);
                        }
                    }
                }
                final List<ModuleRef> nonSystemModuleRefs = moduleFinder.getNonSystemModuleRefs();
                if (nonSystemModuleRefs != null) {
                    for (final ModuleRef moduleRef2 : nonSystemModuleRefs) {
                        String name2 = moduleRef2.getName();
                        if (name2 == null) {
                            name2 = "";
                        }
                        if (scanSpec.moduleAcceptReject.isAcceptedAndNotRejected(name2)) {
                            final ClasspathElementModule classpathElementModule2 = new ClasspathElementModule(moduleRef2, classLoader, this.nestedJarHandler.moduleRefToModuleReaderProxyRecyclerMap, scanSpec);
                            this.moduleOrder.add(classpathElementModule2);
                            classpathElementModule2.open(null, logNode);
                        }
                        else {
                            if (logNode == null) {
                                continue;
                            }
                            logNode.log("Skipping non-accepted or rejected module: " + name2);
                        }
                    }
                }
            }
        }
        catch (InterruptedException ex) {
            this.nestedJarHandler.close(null);
            throw ex;
        }
    }
    
    private static List<ClasspathElement> orderClasspathElements(final Collection<Map.Entry<Integer, ClasspathElement>> c) {
        final ArrayList<Object> list = new ArrayList<Object>(c);
        CollectionUtils.sortIfNotEmpty(list, (Comparator<? super Object>)Scanner.INDEXED_CLASSPATH_ELEMENT_COMPARATOR);
        final ArrayList list2 = new ArrayList<ClasspathElement>(list.size());
        final Iterator<Map.Entry<K, Object>> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add((ClasspathElement)iterator.next().getValue());
        }
        return (List<ClasspathElement>)list2;
    }
    
    private List<ClasspathElement> findClasspathOrder(final Set<ClasspathElement> set, final Queue<Map.Entry<Integer, ClasspathElement>> queue) {
        final List<ClasspathElement> orderClasspathElements = orderClasspathElements(queue);
        for (final ClasspathElement classpathElement : set) {
            classpathElement.childClasspathElementsOrdered = orderClasspathElements(classpathElement.childClasspathElementsIndexed);
        }
        final HashSet<ClasspathElement> set2 = new HashSet<ClasspathElement>();
        final ArrayList<ClasspathElement> list = new ArrayList<ClasspathElement>();
        final Iterator<ClasspathElement> iterator2 = orderClasspathElements.iterator();
        while (iterator2.hasNext()) {
            findClasspathOrderRec(iterator2.next(), set2, list);
        }
        return list;
    }
    
    private <W> void processWorkUnits(final Collection<W> collection, final LogNode logNode, final WorkQueue.WorkUnitProcessor<W> workUnitProcessor) throws InterruptedException, ExecutionException {
        WorkQueue.runWorkQueue(collection, this.executorService, this.interruptionChecker, this.numParallelTasks, logNode, workUnitProcessor);
        if (logNode != null) {
            logNode.addElapsedTime();
        }
        this.interruptionChecker.check();
    }
    
    private static void findClasspathOrderRec(final ClasspathElement classpathElement, final Set<ClasspathElement> set, final List<ClasspathElement> list) {
        if (set.add(classpathElement)) {
            if (!classpathElement.skipClasspathElement) {
                list.add(classpathElement);
            }
            final Iterator<ClasspathElement> iterator = classpathElement.childClasspathElementsOrdered.iterator();
            while (iterator.hasNext()) {
                findClasspathOrderRec(iterator.next(), set, list);
            }
        }
    }
    
    private ScanResult openClasspathElementsThenScan() throws ExecutionException, InterruptedException {
        final ArrayList<ClasspathEntryWorkUnit> list = new ArrayList<ClasspathEntryWorkUnit>();
        final Iterator<ClasspathOrder.ClasspathElementAndClassLoader> iterator = this.classpathFinder.getClasspathOrder().getOrder().iterator();
        while (iterator.hasNext()) {
            list.add(new ClasspathEntryWorkUnit(iterator.next(), null, list.size()));
        }
        final Set<ClasspathElement> setFromMap = Collections.newSetFromMap(new ConcurrentHashMap<ClasspathElement, Boolean>());
        final ConcurrentLinkedQueue<Map.Entry<Integer, ClasspathElement>> concurrentLinkedQueue = new ConcurrentLinkedQueue<Map.Entry<Integer, ClasspathElement>>();
        this.processWorkUnits(list, (this.topLevelLog == null) ? null : this.topLevelLog.log("Opening classpath elements"), this.newClasspathEntryWorkUnitProcessor(setFromMap, concurrentLinkedQueue));
        final List<ClasspathElement> classpathOrder = this.findClasspathOrder(setFromMap, concurrentLinkedQueue);
        this.preprocessClasspathElementsByType(classpathOrder, (this.topLevelLog == null) ? null : this.topLevelLog.log("Finding nested classpath elements"));
        final LogNode logNode = (this.topLevelLog == null) ? null : this.topLevelLog.log("Final classpath element order:");
        final int n = this.moduleOrder.size() + classpathOrder.size();
        final ArrayList list2 = new ArrayList<Object>(n);
        final ArrayList list3 = new ArrayList<String>(n);
        int n2 = 0;
        for (final ClasspathElementModule classpathElementModule : this.moduleOrder) {
            classpathElementModule.classpathElementIdx = n2++;
            list2.add(classpathElementModule);
            list3.add(classpathElementModule.toString());
            if (logNode != null) {
                logNode.log(classpathElementModule.getModuleRef().toString());
            }
        }
        for (final ClasspathElement classpathElement : classpathOrder) {
            classpathElement.classpathElementIdx = n2++;
            list2.add(classpathElement);
            list3.add(classpathElement.toString());
            if (logNode != null) {
                logNode.log(classpathElement.toString());
            }
        }
        this.processWorkUnits((Collection<Object>)list2, (this.topLevelLog == null) ? null : this.topLevelLog.log("Scanning classpath elements"), (WorkQueue.WorkUnitProcessor<Object>)new WorkQueue.WorkUnitProcessor<ClasspathElement>() {
            @Override
            public void processWorkUnit(final ClasspathElement classpathElement, final WorkQueue<ClasspathElement> workQueue, final LogNode logNode) throws InterruptedException {
                classpathElement.scanPaths(logNode);
            }
        });
        ArrayList<Object> list4 = (ArrayList<Object>)list2;
        if (!this.scanSpec.classpathElementResourcePathAcceptReject.acceptIsEmpty()) {
            list4 = new ArrayList<Object>(list2.size());
            for (final ClasspathElement classpathElement2 : list2) {
                if (classpathElement2.containsSpecificallyAcceptedClasspathElementResourcePath) {
                    list4.add(classpathElement2);
                }
            }
        }
        if (this.performScan) {
            return this.performScan((List<ClasspathElement>)list4, (List<String>)list3, this.classLoaderOrderRespectingParentDelegation);
        }
        if (this.topLevelLog != null) {
            this.topLevelLog.log("Only returning classpath elements (not performing a scan)");
        }
        return new ScanResult(this.scanSpec, (List<ClasspathElement>)list4, (List<String>)list3, this.classLoaderOrderRespectingParentDelegation, null, null, null, null, this.nestedJarHandler, this.topLevelLog);
    }
    
    private void findNestedClasspathElements(final List<AbstractMap.SimpleEntry<String, ClasspathElement>> list, final LogNode logNode) {
        CollectionUtils.sortIfNotEmpty((List<Object>)list, (Comparator<? super Object>)new Comparator<AbstractMap.SimpleEntry<String, ClasspathElement>>() {
            @Override
            public int compare(final AbstractMap.SimpleEntry<String, ClasspathElement> simpleEntry, final AbstractMap.SimpleEntry<String, ClasspathElement> simpleEntry2) {
                return simpleEntry.getKey().compareTo((String)simpleEntry2.getKey());
            }
        });
        for (int i = 0; i < list.size(); ++i) {
            final AbstractMap.SimpleEntry<String, V> simpleEntry = list.get(i);
            final String s = simpleEntry.getKey();
            final int length = s.length();
            for (int j = i + 1; j < list.size(); ++j) {
                final String str = list.get(j).getKey();
                final int length2 = str.length();
                boolean b = false;
                if (str.startsWith(s) && length2 > length) {
                    final char char1 = str.charAt(length);
                    if (char1 == '/' || char1 == '!') {
                        final String substring = str.substring(length + 1);
                        if (substring.indexOf(33) < 0) {
                            b = true;
                            final ClasspathElement classpathElement = (ClasspathElement)simpleEntry.getValue();
                            if (classpathElement.nestedClasspathRootPrefixes == null) {
                                classpathElement.nestedClasspathRootPrefixes = new ArrayList<String>();
                            }
                            classpathElement.nestedClasspathRootPrefixes.add(substring + "/");
                            if (logNode != null) {
                                logNode.log(s + " is a prefix of the nested element " + str);
                            }
                        }
                    }
                }
                if (!b) {
                    break;
                }
            }
        }
    }
    
    private void maskClassfiles(final List<ClasspathElement> list, final LogNode logNode) {
        final HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).maskClassfiles(i, set, logNode);
        }
        if (logNode != null) {
            logNode.addElapsedTime();
        }
    }
    
    static {
        INDEXED_CLASSPATH_ELEMENT_COMPARATOR = new Comparator<Map.Entry<Integer, ClasspathElement>>() {
            @Override
            public int compare(final Map.Entry<Integer, ClasspathElement> entry, final Map.Entry<Integer, ClasspathElement> entry2) {
                return entry.getKey() - entry2.getKey();
            }
        };
    }
    
    @Override
    public ScanResult call() throws CancellationException, InterruptedException, ExecutionException {
        ScanResult openClasspathElementsThenScan = null;
        final long currentTimeMillis = System.currentTimeMillis();
        boolean removeTemporaryFilesAfterScan = this.scanSpec.removeTemporaryFilesAfterScan;
        try {
            openClasspathElementsThenScan = this.openClasspathElementsThenScan();
            if (this.topLevelLog != null) {
                this.topLevelLog.log("~", String.format("Total time: %.3f sec", (System.currentTimeMillis() - currentTimeMillis) * 0.001));
                this.topLevelLog.flush();
            }
            if (this.scanResultProcessor != null) {
                try {
                    this.scanResultProcessor.processScanResult(openClasspathElementsThenScan);
                }
                finally {
                    openClasspathElementsThenScan.close();
                }
            }
        }
        catch (Throwable exception) {
            if (this.topLevelLog != null) {
                this.topLevelLog.log("~", (exception instanceof InterruptedException || exception instanceof CancellationException) ? "Scan interrupted or canceled" : ((exception instanceof ExecutionException || exception instanceof RuntimeException) ? "Uncaught exception during scan" : exception.getMessage()), InterruptionChecker.getCause(exception));
                this.topLevelLog.flush();
            }
            removeTemporaryFilesAfterScan = true;
            this.interruptionChecker.interrupt();
            if (this.failureHandler == null) {
                throw exception;
            }
            try {
                this.failureHandler.onFailure(exception);
            }
            catch (Exception cause) {
                if (this.topLevelLog != null) {
                    this.topLevelLog.log("~", "The failure handler threw an exception:", cause);
                    this.topLevelLog.flush();
                }
                final ExecutionException ex = new ExecutionException("Exception while calling failure handler", cause);
                ex.addSuppressed(exception);
                throw ex;
            }
        }
        finally {
            if (removeTemporaryFilesAfterScan) {
                this.nestedJarHandler.close(this.topLevelLog);
            }
        }
        return openClasspathElementsThenScan;
    }
    
    private WorkQueue.WorkUnitProcessor<ClasspathEntryWorkUnit> newClasspathEntryWorkUnitProcessor(final Set<ClasspathElement> set, final Queue<Map.Entry<Integer, ClasspathElement>> queue) {
        return new WorkQueue.WorkUnitProcessor<ClasspathEntryWorkUnit>() {
            @Override
            public void processWorkUnit(final ClasspathEntryWorkUnit classpathEntryWorkUnit, final WorkQueue<ClasspathEntryWorkUnit> workQueue, final LogNode logNode) throws InterruptedException {
                try {
                    ClasspathElement classpathElement;
                    try {
                        classpathElement = Scanner.this.classpathEntryToClasspathElementSingletonMap.get(classpathEntryWorkUnit.rawClasspathEntry, logNode);
                    }
                    catch (SingletonMap.NullSingletonException obj) {
                        throw new IOException("Cannot get classpath element for classpath entry " + classpathEntryWorkUnit.rawClasspathEntry + " : " + obj);
                    }
                    if (set.add(classpathElement)) {
                        classpathElement.open(workQueue, (logNode == null) ? null : logNode.log("Opening classpath element " + classpathElement));
                        final AbstractMap.SimpleEntry simpleEntry = new AbstractMap.SimpleEntry<Integer, ClasspathElement>(classpathEntryWorkUnit.orderWithinParentClasspathElement, classpathElement);
                        if (classpathEntryWorkUnit.parentClasspathElement != null) {
                            classpathEntryWorkUnit.parentClasspathElement.childClasspathElementsIndexed.add((AbstractMap.SimpleEntry<Integer, ClasspathElement>)simpleEntry);
                        }
                        else {
                            queue.add(simpleEntry);
                        }
                    }
                }
                catch (IOException | SecurityException ex2) {
                    final SecurityException ex;
                    final SecurityException obj2 = ex;
                    if (logNode != null) {
                        logNode.log("Skipping invalid classpath element " + classpathEntryWorkUnit.rawClasspathEntry.classpathElementRoot + (classpathEntryWorkUnit.rawClasspathEntry.dirOrPathPackageRoot.isEmpty() ? "" : ("/" + classpathEntryWorkUnit.rawClasspathEntry.dirOrPathPackageRoot)) + " : " + obj2);
                    }
                }
            }
        };
    }
    
    private ScanResult performScan(final List<ClasspathElement> list, final List<String> list2, final ClassLoader[] array) throws ExecutionException, InterruptedException {
        if (this.scanSpec.enableClassInfo) {
            this.maskClassfiles(list, (this.topLevelLog == null) ? null : this.topLevelLog.log("Masking classfiles"));
        }
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        final Iterator<ClasspathElement> iterator = list.iterator();
        while (iterator.hasNext()) {
            hashMap.putAll(iterator.next().fileToLastModified);
        }
        final ConcurrentHashMap<String, ClassInfo> concurrentHashMap = new ConcurrentHashMap<String, ClassInfo>();
        final HashMap<String, PackageInfo> hashMap2 = new HashMap<String, PackageInfo>();
        final HashMap<String, ModuleInfo> hashMap3 = new HashMap<String, ModuleInfo>();
        if (this.scanSpec.enableClassInfo) {
            final ArrayList<Object> list3 = new ArrayList<Object>();
            final HashSet<String> s = new HashSet<String>();
            for (final ClasspathElement classpathElement : list) {
                for (final Resource resource : classpathElement.acceptedClassfileResources) {
                    final String classfilePathToClassName = JarUtils.classfilePathToClassName(resource.getPath());
                    if (!s.add(classfilePathToClassName) && !classfilePathToClassName.equals("module-info") && !classfilePathToClassName.equals("package-info") && !classfilePathToClassName.endsWith(".package-info")) {
                        throw new IllegalArgumentException("Class " + classfilePathToClassName + " should not have been scheduled more than once for scanning due to classpath masking -- please report this bug at: https://github.com/classgraph/classgraph/issues");
                    }
                    list3.add(new ClassfileScanWorkUnit(classpathElement, resource, false));
                }
            }
            final ConcurrentLinkedQueue<Classfile> concurrentLinkedQueue = new ConcurrentLinkedQueue<Classfile>();
            this.processWorkUnits(list3, (this.topLevelLog == null) ? null : this.topLevelLog.log("Scanning classfiles"), (WorkQueue.WorkUnitProcessor<Object>)new ClassfileScannerWorkUnitProcessor(this.scanSpec, list, Collections.unmodifiableSet((Set<? extends String>)s), concurrentLinkedQueue));
            final LogNode logNode = (this.topLevelLog == null) ? null : this.topLevelLog.log("Linking related classfiles");
            while (!concurrentLinkedQueue.isEmpty()) {
                ((Classfile)concurrentLinkedQueue.remove()).link(concurrentHashMap, hashMap2, hashMap3);
            }
            if (logNode != null) {
                logNode.addElapsedTime();
            }
        }
        else if (this.topLevelLog != null) {
            this.topLevelLog.log("Classfile scanning is disabled");
        }
        return new ScanResult(this.scanSpec, list, list2, array, concurrentHashMap, hashMap2, hashMap3, (Map<File, Long>)hashMap, this.nestedJarHandler, this.topLevelLog);
    }
    
    static class ClassfileScanWorkUnit
    {
        private final /* synthetic */ ClasspathElement classpathElement;
        private final /* synthetic */ boolean isExternalClass;
        private final /* synthetic */ Resource classfileResource;
        
        ClassfileScanWorkUnit(final ClasspathElement classpathElement, final Resource classfileResource, final boolean isExternalClass) {
            this.classpathElement = classpathElement;
            this.classfileResource = classfileResource;
            this.isExternalClass = isExternalClass;
        }
    }
    
    private static class ClassfileScannerWorkUnitProcessor implements WorkQueue.WorkUnitProcessor<ClassfileScanWorkUnit>
    {
        private final /* synthetic */ Set<String> classNamesScheduledForExtendedScanning;
        private final /* synthetic */ Queue<Classfile> scannedClassfiles;
        private final /* synthetic */ Set<String> acceptedClassNamesFound;
        private final /* synthetic */ List<ClasspathElement> classpathOrder;
        private final /* synthetic */ ConcurrentHashMap<String, String> stringInternMap;
        private final /* synthetic */ ScanSpec scanSpec;
        
        public ClassfileScannerWorkUnitProcessor(final ScanSpec scanSpec, final List<ClasspathElement> classpathOrder, final Set<String> acceptedClassNamesFound, final Queue<Classfile> scannedClassfiles) {
            this.classNamesScheduledForExtendedScanning = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
            this.stringInternMap = new ConcurrentHashMap<String, String>();
            this.scanSpec = scanSpec;
            this.classpathOrder = classpathOrder;
            this.acceptedClassNamesFound = acceptedClassNamesFound;
            this.scannedClassfiles = scannedClassfiles;
        }
        
        @Override
        public void processWorkUnit(final ClassfileScanWorkUnit classfileScanWorkUnit, final WorkQueue<ClassfileScanWorkUnit> workQueue, final LogNode logNode) throws InterruptedException {
            final LogNode logNode2 = (classfileScanWorkUnit.classfileResource.scanLog == null) ? null : classfileScanWorkUnit.classfileResource.scanLog.log(classfileScanWorkUnit.classfileResource.getPath(), "Parsing classfile");
            try {
                this.scannedClassfiles.add(new Classfile(classfileScanWorkUnit.classpathElement, this.classpathOrder, this.acceptedClassNamesFound, this.classNamesScheduledForExtendedScanning, classfileScanWorkUnit.classfileResource.getPath(), classfileScanWorkUnit.classfileResource, classfileScanWorkUnit.isExternalClass, this.stringInternMap, workQueue, this.scanSpec, logNode2));
            }
            catch (Classfile.SkipClassException ex) {
                if (logNode2 != null) {
                    logNode2.log(classfileScanWorkUnit.classfileResource.getPath(), "Skipping classfile: " + ex.getMessage());
                }
            }
            catch (Classfile.ClassfileFormatException ex2) {
                if (logNode2 != null) {
                    logNode2.log(classfileScanWorkUnit.classfileResource.getPath(), "Invalid classfile: " + ex2.getMessage());
                }
            }
            catch (IOException obj) {
                if (logNode2 != null) {
                    logNode2.log(classfileScanWorkUnit.classfileResource.getPath(), "Could not read classfile: " + obj);
                }
            }
            finally {
                if (logNode2 != null) {
                    logNode2.addElapsedTime();
                }
            }
        }
    }
    
    static class ClasspathEntryWorkUnit
    {
        private final /* synthetic */ ClasspathElement parentClasspathElement;
        private final /* synthetic */ ClasspathOrder.ClasspathElementAndClassLoader rawClasspathEntry;
        private final /* synthetic */ int orderWithinParentClasspathElement;
        
        public ClasspathEntryWorkUnit(final ClasspathOrder.ClasspathElementAndClassLoader rawClasspathEntry, final ClasspathElement parentClasspathElement, final int orderWithinParentClasspathElement) {
            this.rawClasspathEntry = rawClasspathEntry;
            this.parentClasspathElement = parentClasspathElement;
            this.orderWithinParentClasspathElement = orderWithinParentClasspathElement;
        }
    }
}
