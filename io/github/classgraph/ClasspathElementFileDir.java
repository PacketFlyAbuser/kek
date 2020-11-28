// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import nonapi.io.github.classgraph.utils.VersionFinder;
import java.util.Arrays;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.io.InputStream;
import nonapi.io.github.classgraph.fileslice.Slice;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.LogNode;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import nonapi.io.github.classgraph.fileslice.FileSlice;
import java.net.URI;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.util.HashSet;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Objects;
import java.io.File;
import java.util.Set;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;

class ClasspathElementFileDir extends ClasspathElement
{
    private final /* synthetic */ NestedJarHandler nestedJarHandler;
    private final /* synthetic */ Set<String> scannedCanonicalPaths;
    private final /* synthetic */ File classpathEltDir;
    private final /* synthetic */ File packageRootDir;
    
    @Override
    public int hashCode() {
        return Objects.hash(this.classpathEltDir, this.packageRootDir);
    }
    
    public String getModuleName() {
        return (this.moduleNameFromModuleDescriptor == null || this.moduleNameFromModuleDescriptor.isEmpty()) ? null : this.moduleNameFromModuleDescriptor;
    }
    
    ClasspathElementFileDir(final File file, final String child, final ClassLoader classLoader, final NestedJarHandler nestedJarHandler, final ScanSpec scanSpec) {
        super(classLoader, scanSpec);
        this.scannedCanonicalPaths = new HashSet<String>();
        this.classpathEltDir = file;
        this.packageRootDir = new File(file, child);
        this.nestedJarHandler = nestedJarHandler;
    }
    
    @Override
    Resource getResource(final String child) {
        final File file = new File(this.packageRootDir, child);
        return FileUtils.canReadAndIsFile(file) ? this.newResource(child, file, this.nestedJarHandler) : null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClasspathElementFileDir)) {
            return false;
        }
        final ClasspathElementFileDir classpathElementFileDir = (ClasspathElementFileDir)o;
        return Objects.equals(this.classpathEltDir, classpathElementFileDir.classpathEltDir) && Objects.equals(this.packageRootDir, classpathElementFileDir.packageRootDir);
    }
    
    public File getFile() {
        return this.classpathEltDir;
    }
    
    @Override
    URI getURI() {
        return this.packageRootDir.toURI();
    }
    
    private Resource newResource(final String s, final File file, final NestedJarHandler nestedJarHandler) {
        return new Resource(this, file.length()) {
            private /* synthetic */ FileSlice fileSlice;
            protected /* synthetic */ AtomicBoolean isOpen = new AtomicBoolean();
            
            @Override
            public ByteBuffer read() throws IOException {
                if (ClasspathElementFileDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.fileSlice = new FileSlice(file, nestedJarHandler, null);
                this.length = this.fileSlice.sliceLength;
                this.byteBuffer = this.fileSlice.read();
                return this.byteBuffer;
            }
            
            @Override
            public long getLastModified() {
                return file.lastModified();
            }
            
            @Override
            public String getPathRelativeToClasspathElement() {
                String s;
                for (s = FastPathResolver.resolve(new File(ClasspathElementFileDir.this.packageRootDir, s).getPath().substring(ClasspathElementFileDir.this.classpathEltDir.getPath().length())); s.startsWith("/"); s = s.substring(1)) {}
                return s;
            }
            
            @Override
            public void close() {
                super.close();
                if (this.isOpen.getAndSet(false)) {
                    if (this.byteBuffer != null) {
                        this.byteBuffer = null;
                    }
                    if (this.fileSlice != null) {
                        this.fileSlice.close();
                        nestedJarHandler.markSliceAsClosed(this.fileSlice);
                        this.fileSlice = null;
                    }
                }
            }
            
            @Override
            public byte[] load() throws IOException {
                this.read();
                Throwable t = null;
                try {
                    this.fileSlice = new FileSlice(file, nestedJarHandler, null);
                    final byte[] load = this.fileSlice.load();
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
            public InputStream open() throws IOException {
                if (ClasspathElementFileDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.fileSlice = new FileSlice(file, nestedJarHandler, null);
                this.inputStream = this.fileSlice.open();
                this.length = this.fileSlice.sliceLength;
                return this.inputStream;
            }
            
            @Override
            public Set<PosixFilePermission> getPosixFilePermissions() {
                Set<PosixFilePermission> permissions = null;
                try {
                    permissions = Files.readAttributes(file.toPath(), PosixFileAttributes.class, new LinkOption[0]).permissions();
                }
                catch (UnsupportedOperationException ex) {}
                catch (IOException ex2) {}
                catch (SecurityException ex3) {}
                return permissions;
            }
            
            @Override
            public String getPath() {
                String s;
                for (s = FastPathResolver.resolve(s); s.startsWith("/"); s = s.substring(1)) {}
                return s;
            }
            
            @Override
            ClassfileReader openClassfile() throws IOException {
                if (ClasspathElementFileDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.fileSlice = new FileSlice(file, nestedJarHandler, null);
                this.length = this.fileSlice.sliceLength;
                return new ClassfileReader(this.fileSlice);
            }
        };
    }
    
    private void scanDirRecursively(final File file, final LogNode logNode) {
        if (this.skipClasspathElement) {
            return;
        }
        try {
            final String canonicalPath = file.getCanonicalPath();
            if (!this.scannedCanonicalPaths.add(canonicalPath)) {
                if (logNode != null) {
                    logNode.log("Reached symlink cycle, stopping recursion: " + file);
                }
                return;
            }
        }
        catch (IOException | SecurityException ex) {
            final Object o2;
            final Object o = o2;
            if (logNode != null) {
                logNode.log("Could not canonicalize path: " + file, (Throwable)o);
            }
            return;
        }
        final String path = file.getPath();
        final int beginIndex = this.packageRootDir.getPath().length() + 1;
        final String str = (beginIndex > path.length()) ? "/" : (path.substring(beginIndex).replace(File.separatorChar, '/') + "/");
        final boolean equals = "/".equals(str);
        if (this.nestedClasspathRootPrefixes != null && this.nestedClasspathRootPrefixes.contains(str)) {
            if (logNode != null) {
                logNode.log("Reached nested classpath root, stopping recursion to avoid duplicate scanning: " + str);
            }
            return;
        }
        if (str.startsWith("META-INF/versions/")) {
            if (logNode != null) {
                logNode.log("Found unexpected nested versioned entry in directory classpath element -- skipping: " + str);
            }
            return;
        }
        this.checkResourcePathAcceptReject(str, logNode);
        if (this.skipClasspathElement) {
            return;
        }
        final ScanSpec.ScanSpecPathMatch dirAcceptMatchStatus = this.scanSpec.dirAcceptMatchStatus(str);
        if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX) {
            if (logNode != null) {
                logNode.log("Reached rejected directory, stopping recursive scan: " + str);
            }
            return;
        }
        if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.NOT_WITHIN_ACCEPTED_PATH) {
            return;
        }
        String canonicalPath;
        final LogNode logNode2 = (logNode == null) ? null : logNode.log("1:" + canonicalPath, "Scanning directory: " + file + (file.getPath().equals(canonicalPath) ? "" : (" ; canonical path: " + canonicalPath)));
        final File[] listFiles = file.listFiles();
        if (listFiles == null) {
            if (logNode != null) {
                logNode.log("Invalid directory " + file);
            }
            return;
        }
        Arrays.sort(listFiles);
        final boolean b = VersionFinder.JAVA_MAJOR_VERSION >= 9 && this.getModuleName() != null;
        if (dirAcceptMatchStatus != ScanSpec.ScanSpecPathMatch.ANCESTOR_OF_ACCEPTED_PATH) {
            for (final File file2 : listFiles) {
                if (file2.isFile()) {
                    final String str2 = (str.isEmpty() || equals) ? file2.getName() : (str + file2.getName());
                    if (!b || !equals || !str2.endsWith(".class") || str2.equals("module-info.class")) {
                        this.checkResourcePathAcceptReject(str2, logNode2);
                        if (this.skipClasspathElement) {
                            return;
                        }
                        if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX || dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_PATH || (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_CLASS_PACKAGE && this.scanSpec.classfileIsSpecificallyAccepted(str2))) {
                            this.addAcceptedResource(this.newResource(str2, file2, this.nestedJarHandler), dirAcceptMatchStatus, false, logNode2);
                            this.fileToLastModified.put(file2, file2.lastModified());
                        }
                        else if (logNode2 != null) {
                            logNode2.log("Skipping non-accepted file: " + str2);
                        }
                    }
                }
            }
        }
        else if (this.scanSpec.enableClassInfo && str.equals("/")) {
            for (final File file3 : listFiles) {
                if (file3.getName().equals("module-info.class") && file3.isFile()) {
                    this.addAcceptedResource(this.newResource("module-info.class", file3, this.nestedJarHandler), dirAcceptMatchStatus, true, logNode2);
                    this.fileToLastModified.put(file3, file3.lastModified());
                    break;
                }
            }
        }
        for (final File file4 : listFiles) {
            if (file4.isDirectory()) {
                this.scanDirRecursively(file4, logNode2);
                if (this.skipClasspathElement) {
                    if (logNode2 != null) {
                        logNode2.addElapsedTime();
                    }
                    return;
                }
            }
        }
        if (logNode2 != null) {
            logNode2.addElapsedTime();
        }
        this.fileToLastModified.put(file, file.lastModified());
    }
    
    @Override
    void open(final WorkQueue<Scanner.ClasspathEntryWorkUnit> workQueue, final LogNode logNode) {
        if (!this.scanSpec.scanDirs) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping classpath element, since dir scanning is disabled: " + this.classpathEltDir, logNode);
            }
            this.skipClasspathElement = true;
            return;
        }
        try {
            int n = 0;
            final String[] automatic_LIB_DIR_PREFIXES = ClassLoaderHandlerRegistry.AUTOMATIC_LIB_DIR_PREFIXES;
            for (int length = automatic_LIB_DIR_PREFIXES.length, i = 0; i < length; ++i) {
                final File file = new File(this.classpathEltDir, automatic_LIB_DIR_PREFIXES[i]);
                if (FileUtils.canReadAndIsDir(file)) {
                    final File[] listFiles = file.listFiles();
                    if (listFiles != null) {
                        Arrays.sort(listFiles);
                        for (final File obj : listFiles) {
                            if (obj.isFile() && obj.getName().endsWith(".jar")) {
                                if (logNode != null) {
                                    this.log(this.classpathElementIdx, "Found lib jar: " + obj, logNode);
                                }
                                workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(obj.getPath(), this.classLoader), this, n++));
                            }
                        }
                    }
                }
            }
            if (this.packageRootDir.equals(this.classpathEltDir)) {
                for (final String child : ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES) {
                    final File obj2 = new File(this.classpathEltDir, child);
                    if (FileUtils.canReadAndIsDir(obj2)) {
                        if (logNode != null) {
                            this.log(this.classpathElementIdx, "Found package root: " + obj2, logNode);
                        }
                        workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(this.classpathEltDir, child, this.classLoader), this, n++));
                    }
                }
            }
        }
        catch (SecurityException ex) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping classpath element, since dir cannot be accessed: " + this.classpathEltDir, logNode);
            }
            this.skipClasspathElement = true;
        }
    }
    
    @Override
    void scanPaths(final LogNode logNode) {
        if (this.skipClasspathElement) {
            return;
        }
        if (this.scanned.getAndSet(true)) {
            throw new IllegalArgumentException("Already scanned classpath element " + this.toString());
        }
        final LogNode logNode2 = (logNode == null) ? null : this.log(this.classpathElementIdx, "Scanning directory classpath element " + this.packageRootDir, logNode);
        this.scanDirRecursively(this.packageRootDir, logNode2);
        this.finishScanPaths(logNode2);
    }
    
    @Override
    public String toString() {
        return this.packageRootDir.toString();
    }
}
