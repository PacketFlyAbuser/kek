// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.fastzipfilereader.ZipFileSlice;
import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.concurrency.SingletonMap;
import java.util.Map;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.PosixFilePermission;
import java.util.concurrent.atomic.AtomicBoolean;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import java.util.HashSet;
import nonapi.io.github.classgraph.fastzipfilereader.FastZipEntry;
import nonapi.io.github.classgraph.utils.VersionFinder;
import nonapi.io.github.classgraph.utils.LogNode;
import java.net.URISyntaxException;
import nonapi.io.github.classgraph.utils.URLPathEncoder;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import nonapi.io.github.classgraph.fastzipfilereader.LogicalZipFile;

class ClasspathElementZip extends ClasspathElement
{
    private /* synthetic */ String zipFilePath;
    /* synthetic */ LogicalZipFile logicalZipFile;
    private /* synthetic */ String packageRootPrefix;
    /* synthetic */ String moduleNameFromManifestFile;
    private /* synthetic */ String derivedAutomaticModuleName;
    private final /* synthetic */ NestedJarHandler nestedJarHandler;
    private final /* synthetic */ String rawPath;
    private final /* synthetic */ ConcurrentHashMap<String, Resource> relativePathToResource;
    
    @Override
    Resource getResource(final String key) {
        return this.relativePathToResource.get(key);
    }
    
    @Override
    URI getURI() {
        try {
            return new URI(URLPathEncoder.normalizeURLPath(this.getZipFilePath()));
        }
        catch (URISyntaxException obj) {
            throw new IllegalArgumentException("Could not form URI: " + obj);
        }
    }
    
    @Override
    void scanPaths(final LogNode logNode) {
        if (this.logicalZipFile == null) {
            this.skipClasspathElement = true;
        }
        if (this.skipClasspathElement) {
            return;
        }
        if (this.scanned.getAndSet(true)) {
            throw new IllegalArgumentException("Already scanned classpath element " + this.getZipFilePath());
        }
        final LogNode logNode2 = (logNode == null) ? null : this.log(this.classpathElementIdx, "Scanning jarfile classpath element " + this.getZipFilePath(), logNode);
        boolean b = false;
        if (VersionFinder.JAVA_MAJOR_VERSION >= 9) {
            String s = this.moduleNameFromModuleDescriptor;
            if (s == null || s.isEmpty()) {
                s = this.moduleNameFromManifestFile;
            }
            if (s != null && s.isEmpty()) {
                s = null;
            }
            if (s != null) {
                b = true;
            }
        }
        Set<String> set = null;
        Object anObject = null;
        ScanSpec.ScanSpecPathMatch scanSpecPathMatch = null;
        for (final FastZipEntry fastZipEntry : this.logicalZipFile.entries) {
            String s2 = fastZipEntry.entryNameUnversioned;
            if (s2.startsWith("META-INF/versions/")) {
                if (logNode2 == null) {
                    continue;
                }
                if (VersionFinder.JAVA_MAJOR_VERSION < 9) {
                    logNode2.log("Skipping versioned entry in jar, because JRE version " + VersionFinder.JAVA_MAJOR_VERSION + " does not support this: " + s2);
                }
                else {
                    logNode2.log("Found unexpected versioned entry in jar (the jar's manifest file may be missing the \"Multi-Release\" key) -- skipping: " + s2);
                }
            }
            else {
                if (b && s2.indexOf(47) < 0 && s2.endsWith(".class") && !s2.equals("module-info.class")) {
                    continue;
                }
                if (this.nestedClasspathRootPrefixes != null) {
                    boolean b2 = false;
                    for (final String s3 : this.nestedClasspathRootPrefixes) {
                        if (s2.startsWith(s3)) {
                            if (logNode2 != null) {
                                if (set == null) {
                                    set = new HashSet<String>();
                                }
                                if (set.add(s3)) {
                                    logNode2.log("Reached nested classpath root, stopping recursion to avoid duplicate scanning: " + s3);
                                }
                            }
                            b2 = true;
                            break;
                        }
                    }
                    if (b2) {
                        continue;
                    }
                }
                if (!this.packageRootPrefix.isEmpty() && !s2.startsWith(this.packageRootPrefix)) {
                    continue;
                }
                if (!this.packageRootPrefix.isEmpty()) {
                    s2 = s2.substring(this.packageRootPrefix.length());
                }
                else {
                    for (int i = 0; i < ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES.length; ++i) {
                        if (s2.startsWith(ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES[i])) {
                            s2 = s2.substring(ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES[i].length());
                        }
                    }
                }
                this.checkResourcePathAcceptReject(s2, logNode);
                if (this.skipClasspathElement) {
                    return;
                }
                final int lastIndex = s2.lastIndexOf(47);
                final String s4 = (lastIndex < 0) ? "/" : s2.substring(0, lastIndex + 1);
                final ScanSpec.ScanSpecPathMatch scanSpecPathMatch2 = s4.equals(anObject) ? scanSpecPathMatch : this.scanSpec.dirAcceptMatchStatus(s4);
                anObject = s4;
                if ((scanSpecPathMatch = scanSpecPathMatch2) == ScanSpec.ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX) {
                    if (logNode2 == null) {
                        continue;
                    }
                    logNode2.log("Skipping rejected path: " + s2);
                }
                else {
                    final Resource resource = this.newResource(fastZipEntry, s2);
                    if (this.relativePathToResource.putIfAbsent(s2, resource) != null) {
                        continue;
                    }
                    if (scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX || scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_PATH || (scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_CLASS_PACKAGE && this.scanSpec.classfileIsSpecificallyAccepted(s2))) {
                        this.addAcceptedResource(resource, scanSpecPathMatch2, false, logNode2);
                    }
                    else {
                        if (!this.scanSpec.enableClassInfo || !s2.equals("module-info.class")) {
                            continue;
                        }
                        this.addAcceptedResource(resource, scanSpecPathMatch2, true, logNode2);
                    }
                }
            }
        }
        final File file = this.getFile();
        if (file != null) {
            this.fileToLastModified.put(file, file.lastModified());
        }
        this.finishScanPaths(logNode2);
    }
    
    ClasspathElementZip(final Object o, final ClassLoader classLoader, final NestedJarHandler nestedJarHandler, final ScanSpec scanSpec) {
        super(classLoader, scanSpec);
        this.packageRootPrefix = "";
        this.relativePathToResource = new ConcurrentHashMap<String, Resource>();
        this.rawPath = o.toString();
        this.zipFilePath = this.rawPath;
        this.nestedJarHandler = nestedJarHandler;
    }
    
    @Override
    public String toString() {
        return this.getZipFilePath();
    }
    
    @Override
    File getFile() {
        if (this.logicalZipFile != null) {
            return this.logicalZipFile.getPhysicalFile();
        }
        final int index = this.rawPath.indexOf(33);
        return new File(FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, (index < 0) ? this.rawPath : this.rawPath.substring(0, index)));
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ClasspathElementZip && this.getZipFilePath().equals(((ClasspathElementZip)o).getZipFilePath()));
    }
    
    String getZipFilePath() {
        return this.packageRootPrefix.isEmpty() ? this.zipFilePath : (this.zipFilePath + "!/" + this.packageRootPrefix.substring(0, this.packageRootPrefix.length() - 1));
    }
    
    private Resource newResource(final FastZipEntry fastZipEntry, final String s) {
        return new Resource(this, fastZipEntry.uncompressedSize) {
            protected /* synthetic */ AtomicBoolean isOpen = new AtomicBoolean();
            
            @Override
            public Set<PosixFilePermission> getPosixFilePermissions() {
                final int fileAttributes = fastZipEntry.fileAttributes;
                Set<PosixFilePermission> set;
                if (fileAttributes == 0) {
                    set = null;
                }
                else {
                    set = new HashSet<PosixFilePermission>();
                    if ((fileAttributes & 0x100) > 0) {
                        set.add(PosixFilePermission.OWNER_READ);
                    }
                    if ((fileAttributes & 0x80) > 0) {
                        set.add(PosixFilePermission.OWNER_WRITE);
                    }
                    if ((fileAttributes & 0x40) > 0) {
                        set.add(PosixFilePermission.OWNER_EXECUTE);
                    }
                    if ((fileAttributes & 0x20) > 0) {
                        set.add(PosixFilePermission.GROUP_READ);
                    }
                    if ((fileAttributes & 0x10) > 0) {
                        set.add(PosixFilePermission.GROUP_WRITE);
                    }
                    if ((fileAttributes & 0x8) > 0) {
                        set.add(PosixFilePermission.GROUP_EXECUTE);
                    }
                    if ((fileAttributes & 0x4) > 0) {
                        set.add(PosixFilePermission.OTHERS_READ);
                    }
                    if ((fileAttributes & 0x2) > 0) {
                        set.add(PosixFilePermission.OTHERS_WRITE);
                    }
                    if ((fileAttributes & 0x1) > 0) {
                        set.add(PosixFilePermission.OTHERS_EXECUTE);
                    }
                }
                return set;
            }
            
            @Override
            public InputStream open() throws IOException {
                if (ClasspathElementZip.this.skipClasspathElement) {
                    throw new IOException("Jarfile could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                try {
                    this.inputStream = fastZipEntry.getSlice().open();
                    this.length = fastZipEntry.uncompressedSize;
                    return this.inputStream;
                }
                catch (IOException ex) {
                    this.close();
                    throw ex;
                }
            }
            
            @Override
            public void close() {
                super.close();
                if (this.isOpen.getAndSet(false) && this.byteBuffer != null) {
                    this.byteBuffer = null;
                }
            }
            
            @Override
            public ByteBuffer read() throws IOException {
                if (ClasspathElementZip.this.skipClasspathElement) {
                    throw new IOException("Jarfile could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                try {
                    this.byteBuffer = fastZipEntry.getSlice().read();
                    this.length = this.byteBuffer.remaining();
                    return this.byteBuffer;
                }
                catch (IOException ex) {
                    this.close();
                    throw ex;
                }
            }
            
            @Override
            public String getPathRelativeToClasspathElement() {
                return fastZipEntry.entryName;
            }
            
            @Override
            public long getLastModified() {
                return fastZipEntry.getLastModifiedTimeMillis();
            }
            
            @Override
            public byte[] load() throws IOException {
                if (ClasspathElementZip.this.skipClasspathElement) {
                    throw new IOException("Jarfile could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                Throwable t = null;
                try {
                    final byte[] load = fastZipEntry.getSlice().load();
                    this.length = load.length;
                    return load;
                }
                catch (Throwable t2) {
                    t = t2;
                    throw t2;
                }
                finally {
                    if (this != null) {
                        if (t != null) {
                            try {
                                this.close();
                            }
                            catch (Throwable exception) {
                                t.addSuppressed(exception);
                            }
                        }
                        else {
                            this.close();
                        }
                    }
                }
            }
            
            @Override
            public String getPath() {
                return s;
            }
            
            @Override
            ClassfileReader openClassfile() throws IOException {
                return new ClassfileReader(this.open());
            }
        };
    }
    
    @Override
    public int hashCode() {
        return this.getZipFilePath().hashCode();
    }
    
    @Override
    void open(final WorkQueue<Scanner.ClasspathEntryWorkUnit> workQueue, final LogNode logNode) throws InterruptedException {
        if (!this.scanSpec.scanJars) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping classpath element, since jar scanning is disabled: " + this.rawPath, logNode);
            }
            this.skipClasspathElement = true;
            return;
        }
        final LogNode logNode2 = (logNode == null) ? null : this.log(this.classpathElementIdx, "Opening jar: " + this.rawPath, logNode);
        final int index = this.rawPath.indexOf(33);
        if (!this.scanSpec.jarAcceptReject.isAcceptedAndNotRejected(FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, (index < 0) ? this.rawPath : this.rawPath.substring(0, index)))) {
            if (logNode2 != null) {
                logNode2.log("Skipping jarfile that is rejected or not accepted: " + this.rawPath);
            }
            this.skipClasspathElement = true;
            return;
        }
        try {
            Map.Entry<LogicalZipFile, String> entry;
            try {
                entry = this.nestedJarHandler.nestedPathToLogicalZipFileAndPackageRootMap.get(this.rawPath, logNode2);
            }
            catch (SingletonMap.NullSingletonException obj) {
                throw new IOException("Could not get logical zipfile " + this.rawPath + " : " + obj);
            }
            this.logicalZipFile = entry.getKey();
            if (this.logicalZipFile == null) {
                throw new IOException("Logical zipfile was null");
            }
            this.zipFilePath = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, this.logicalZipFile.getPath());
            final String str = entry.getValue();
            if (!str.isEmpty()) {
                this.packageRootPrefix = str + "/";
            }
        }
        catch (IOException | IllegalArgumentException ex2) {
            final IllegalArgumentException ex;
            final IllegalArgumentException obj2 = ex;
            if (logNode2 != null) {
                logNode2.log("Could not open jarfile " + this.rawPath + " : " + obj2);
            }
            this.skipClasspathElement = true;
            return;
        }
        if (!this.scanSpec.enableSystemJarsAndModules && this.logicalZipFile.isJREJar) {
            if (logNode2 != null) {
                logNode2.log("Ignoring JRE jar: " + this.rawPath);
            }
            this.skipClasspathElement = true;
            return;
        }
        if (!this.logicalZipFile.isAcceptedAndNotRejected(this.scanSpec.jarAcceptReject)) {
            if (logNode2 != null) {
                logNode2.log("Skipping jarfile that is rejected or not accepted: " + this.rawPath);
            }
            this.skipClasspathElement = true;
            return;
        }
        int n = 0;
        if (this.scanSpec.scanNestedJars) {
            for (final FastZipEntry fastZipEntry : this.logicalZipFile.entries) {
                final String[] automatic_LIB_DIR_PREFIXES = ClassLoaderHandlerRegistry.AUTOMATIC_LIB_DIR_PREFIXES;
                for (int length = automatic_LIB_DIR_PREFIXES.length, i = 0; i < length; ++i) {
                    if (fastZipEntry.entryNameUnversioned.startsWith(automatic_LIB_DIR_PREFIXES[i]) && fastZipEntry.entryNameUnversioned.endsWith(".jar")) {
                        final String path = fastZipEntry.getPath();
                        if (logNode2 != null) {
                            logNode2.log("Found nested lib jar: " + path);
                        }
                        workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(path, this.classLoader), this, n++));
                        break;
                    }
                }
            }
        }
        final HashSet<String> set = new HashSet<String>();
        set.add(this.rawPath);
        if (this.logicalZipFile.classPathManifestEntryValue != null) {
            final String parentDirPath = FileUtils.getParentDirPath(this.logicalZipFile.getPathWithinParentZipFileSlice());
            for (final String s : this.logicalZipFile.classPathManifestEntryValue.split(" ")) {
                if (!s.isEmpty()) {
                    final String resolve = FastPathResolver.resolve(parentDirPath, s);
                    final ZipFileSlice parentZipFileSlice = this.logicalZipFile.getParentZipFileSlice();
                    final String s2 = (parentZipFileSlice == null) ? resolve : (parentZipFileSlice.getPath() + (resolve.startsWith("/") ? "!" : "!/") + resolve);
                    if (set.add(s2)) {
                        workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(s2, this.classLoader), this, n++));
                    }
                }
            }
        }
        if (this.logicalZipFile.bundleClassPathManifestEntryValue != null) {
            final String string = this.zipFilePath + "!/";
            for (String substring : this.logicalZipFile.bundleClassPathManifestEntryValue.split(",")) {
                while (substring.startsWith("/")) {
                    substring = substring.substring(1);
                }
                if (!substring.isEmpty() && !substring.equals(".")) {
                    final String string2 = string + FileUtils.sanitizeEntryPath(substring, true, true);
                    if (set.add(string2)) {
                        workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(string2, this.classLoader), this, n++));
                    }
                }
            }
        }
    }
    
    public String getModuleName() {
        String s = this.moduleNameFromModuleDescriptor;
        if (s == null || s.isEmpty()) {
            s = this.moduleNameFromManifestFile;
        }
        if (s == null || s.isEmpty()) {
            if (this.derivedAutomaticModuleName == null) {
                this.derivedAutomaticModuleName = JarUtils.derivedAutomaticModuleName(this.zipFilePath);
            }
            s = this.derivedAutomaticModuleName;
        }
        return (s == null || s.isEmpty()) ? null : s;
    }
}
