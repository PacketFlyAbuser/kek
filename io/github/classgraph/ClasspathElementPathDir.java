// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.classpath.ClasspathOrder;
import nonapi.io.github.classgraph.classloaderhandler.ClassLoaderHandlerRegistry;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import java.io.InputStream;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.ByteBuffer;
import nonapi.io.github.classgraph.fileslice.Slice;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.util.concurrent.atomic.AtomicBoolean;
import nonapi.io.github.classgraph.fileslice.PathSlice;
import java.util.Objects;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.io.IOError;
import java.net.URI;
import java.io.File;
import java.util.Iterator;
import java.nio.file.DirectoryStream;
import nonapi.io.github.classgraph.utils.VersionFinder;
import java.util.List;
import java.util.Collections;
import java.nio.file.Files;
import java.util.ArrayList;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import java.io.IOException;
import java.nio.file.LinkOption;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.HashSet;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.Set;
import java.nio.file.Path;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;

class ClasspathElementPathDir extends ClasspathElement
{
    private final /* synthetic */ NestedJarHandler nestedJarHandler;
    private final /* synthetic */ Path packageRootPath;
    private final /* synthetic */ Path classpathEltPath;
    private final /* synthetic */ Set<Path> scannedCanonicalPaths;
    
    ClasspathElementPathDir(final Path classpathEltPath, final String other, final ClassLoader classLoader, final NestedJarHandler nestedJarHandler, final ScanSpec scanSpec) {
        super(classLoader, scanSpec);
        this.scannedCanonicalPaths = new HashSet<Path>();
        this.classpathEltPath = classpathEltPath;
        this.packageRootPath = classpathEltPath.resolve(other);
        this.nestedJarHandler = nestedJarHandler;
    }
    
    private void scanPathRecursively(final Path path, final LogNode logNode) {
        if (this.skipClasspathElement) {
            return;
        }
        try {
            final Path realPath = path.toRealPath(new LinkOption[0]);
            if (!this.scannedCanonicalPaths.add(realPath)) {
                if (logNode != null) {
                    logNode.log("Reached symlink cycle, stopping recursion: " + path);
                }
                return;
            }
        }
        catch (IOException | SecurityException ex4) {
            final Object o2;
            final Object o = o2;
            if (logNode != null) {
                logNode.log("Could not canonicalize path: " + path, (Throwable)o);
            }
            return;
        }
        String s;
        for (s = FastPathResolver.resolve(this.packageRootPath.relativize(path).toString()); s.startsWith("/"); s = s.substring(1)) {}
        if (!s.endsWith("/")) {
            s += "/";
        }
        final boolean equals = s.equals("/");
        if (this.nestedClasspathRootPrefixes != null && this.nestedClasspathRootPrefixes.contains(s)) {
            if (logNode != null) {
                logNode.log("Reached nested classpath root, stopping recursion to avoid duplicate scanning: " + s);
            }
            return;
        }
        if (s.startsWith("META-INF/versions/")) {
            if (logNode != null) {
                logNode.log("Found unexpected nested versioned entry in directory classpath element -- skipping: " + s);
            }
            return;
        }
        this.checkResourcePathAcceptReject(s, logNode);
        if (this.skipClasspathElement) {
            return;
        }
        final ScanSpec.ScanSpecPathMatch dirAcceptMatchStatus = this.scanSpec.dirAcceptMatchStatus(s);
        if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX) {
            if (logNode != null) {
                logNode.log("Reached rejected directory, stopping recursive scan: " + s);
            }
            return;
        }
        if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.NOT_WITHIN_ACCEPTED_PATH) {
            return;
        }
        Path realPath;
        final LogNode logNode2 = (logNode == null) ? null : logNode.log("1:" + realPath, "Scanning Path: " + FastPathResolver.resolve(path.toString()) + (path.equals(realPath) ? "" : (" ; canonical path: " + FastPathResolver.resolve(realPath.toString()))));
        final ArrayList<Comparable> list = new ArrayList<Comparable>();
        try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
            final Iterator<Path> iterator = directoryStream.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        catch (IOException | SecurityException ex5) {
            final SecurityException ex2;
            final SecurityException ex = ex2;
            if (logNode != null) {
                logNode.log("Could not read directory " + path + " : " + ex.getMessage());
            }
            this.skipClasspathElement = true;
            return;
        }
        Collections.sort(list);
        final boolean b = VersionFinder.JAVA_MAJOR_VERSION >= 9 && this.getModuleName() != null;
        if (dirAcceptMatchStatus != ScanSpec.ScanSpecPathMatch.ANCESTOR_OF_ACCEPTED_PATH) {
            for (final Path path2 : list) {
                if (Files.isRegularFile(path2, new LinkOption[0])) {
                    final Path relativize = this.classpathEltPath.relativize(path2);
                    final String string = relativize.toString();
                    if (b && equals && string.endsWith(".class") && !string.equals("module-info.class")) {
                        continue;
                    }
                    this.checkResourcePathAcceptReject(string, logNode2);
                    if (this.skipClasspathElement) {
                        return;
                    }
                    if (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX || dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_PATH || (dirAcceptMatchStatus == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_CLASS_PACKAGE && this.scanSpec.classfileIsSpecificallyAccepted(string))) {
                        this.addAcceptedResource(this.newResource(path2, this.nestedJarHandler), dirAcceptMatchStatus, false, logNode2);
                        try {
                            this.fileToLastModified.put(path2.toFile(), path2.toFile().lastModified());
                        }
                        catch (UnsupportedOperationException ex6) {}
                    }
                    else {
                        if (logNode2 == null) {
                            continue;
                        }
                        logNode2.log("Skipping non-accepted file: " + relativize);
                    }
                }
            }
        }
        else if (this.scanSpec.enableClassInfo && s.equals("/")) {
            for (final Path path3 : list) {
                if (path3.getFileName().toString().equals("module-info.class") && Files.isRegularFile(path3, new LinkOption[0])) {
                    this.addAcceptedResource(this.newResource(path3, this.nestedJarHandler), dirAcceptMatchStatus, true, logNode2);
                    try {
                        this.fileToLastModified.put(path3.toFile(), path3.toFile().lastModified());
                    }
                    catch (UnsupportedOperationException ex7) {}
                    break;
                }
            }
        }
        for (final Path path4 : list) {
            try {
                if (!Files.isDirectory(path4, new LinkOption[0])) {
                    continue;
                }
                this.scanPathRecursively(path4, logNode2);
                if (this.skipClasspathElement) {
                    if (logNode2 != null) {
                        logNode2.addElapsedTime();
                    }
                    return;
                }
                continue;
            }
            catch (SecurityException ex3) {
                if (logNode2 == null) {
                    continue;
                }
                logNode2.log("Could not read sub-directory " + path4 + " : " + ex3.getMessage());
            }
        }
        if (logNode2 != null) {
            logNode2.addElapsedTime();
        }
        try {
            final File file = path.toFile();
            this.fileToLastModified.put(file, file.lastModified());
        }
        catch (UnsupportedOperationException ex8) {}
    }
    
    public File getFile() {
        try {
            return this.classpathEltPath.toFile();
        }
        catch (UnsupportedOperationException ex) {
            return null;
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
        final LogNode logNode2 = (logNode == null) ? null : this.log(this.classpathElementIdx, "Scanning Path classpath element " + this.getURI(), logNode);
        this.scanPathRecursively(this.packageRootPath, logNode2);
        this.finishScanPaths(logNode2);
    }
    
    @Override
    URI getURI() {
        return this.packageRootPath.toUri();
    }
    
    @Override
    public String toString() {
        try {
            return this.packageRootPath.toUri().toString();
        }
        catch (IOError | SecurityException ioError) {
            return this.packageRootPath.toString();
        }
    }
    
    @Override
    Resource getResource(final String other) {
        final Path resolve = this.packageRootPath.resolve(other);
        return FileUtils.canReadAndIsFile(resolve) ? this.newResource(resolve, this.nestedJarHandler) : null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClasspathElementPathDir)) {
            return false;
        }
        final ClasspathElementPathDir classpathElementPathDir = (ClasspathElementPathDir)o;
        return Objects.equals(this.classpathEltPath, classpathElementPathDir.classpathEltPath) && Objects.equals(this.packageRootPath, classpathElementPathDir.packageRootPath);
    }
    
    private Resource newResource(final Path path, final NestedJarHandler nestedJarHandler) {
        long size;
        try {
            size = Files.size(path);
        }
        catch (IOException | SecurityException ex) {
            size = -1L;
        }
        return new Resource(this, size) {
            private /* synthetic */ PathSlice pathSlice;
            protected /* synthetic */ AtomicBoolean isOpen = new AtomicBoolean();
            
            @Override
            ClassfileReader openClassfile() throws IOException {
                if (ClasspathElementPathDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.pathSlice = new PathSlice(path, nestedJarHandler);
                this.length = this.pathSlice.sliceLength;
                return new ClassfileReader(this.pathSlice);
            }
            
            @Override
            public String getPath() {
                String s;
                for (s = FastPathResolver.resolve(ClasspathElementPathDir.this.packageRootPath.relativize(path).toString()); s.startsWith("/"); s = s.substring(1)) {}
                return s;
            }
            
            @Override
            public long getLastModified() {
                try {
                    return path.toFile().lastModified();
                }
                catch (UnsupportedOperationException ex) {
                    return 0L;
                }
            }
            
            @Override
            public String getPathRelativeToClasspathElement() {
                String s;
                for (s = FastPathResolver.resolve(ClasspathElementPathDir.this.classpathEltPath.relativize(path).toString()); s.startsWith("/"); s = s.substring(1)) {}
                return s;
            }
            
            @Override
            public ByteBuffer read() throws IOException {
                if (ClasspathElementPathDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.pathSlice = new PathSlice(path, nestedJarHandler);
                this.length = this.pathSlice.sliceLength;
                this.byteBuffer = this.pathSlice.read();
                return this.byteBuffer;
            }
            
            @Override
            public Set<PosixFilePermission> getPosixFilePermissions() {
                Set<PosixFilePermission> permissions = null;
                try {
                    permissions = Files.readAttributes(path, PosixFileAttributes.class, new LinkOption[0]).permissions();
                }
                catch (UnsupportedOperationException ex) {}
                catch (IOException ex2) {}
                catch (SecurityException ex3) {}
                return permissions;
            }
            
            @Override
            public byte[] load() throws IOException {
                this.read();
                Throwable t = null;
                try {
                    this.pathSlice = new PathSlice(path, nestedJarHandler);
                    final byte[] load = this.pathSlice.load();
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
                if (ClasspathElementPathDir.this.skipClasspathElement) {
                    throw new IOException("Parent directory could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                this.pathSlice = new PathSlice(path, nestedJarHandler);
                this.inputStream = this.pathSlice.open();
                this.length = this.pathSlice.sliceLength;
                return this.inputStream;
            }
            
            @Override
            public void close() {
                super.close();
                if (this.isOpen.getAndSet(false)) {
                    if (this.byteBuffer != null) {
                        this.byteBuffer = null;
                    }
                    if (this.pathSlice != null) {
                        this.pathSlice.close();
                        nestedJarHandler.markSliceAsClosed(this.pathSlice);
                        this.pathSlice = null;
                    }
                }
            }
        };
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.classpathEltPath, this.packageRootPath);
    }
    
    @Override
    void open(final WorkQueue<Scanner.ClasspathEntryWorkUnit> workQueue, final LogNode logNode) {
        if (!this.scanSpec.scanDirs) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping classpath element, since dir scanning is disabled: " + this.classpathEltPath, logNode);
            }
            this.skipClasspathElement = true;
            return;
        }
        try {
            int n = 0;
            final String[] automatic_LIB_DIR_PREFIXES = ClassLoaderHandlerRegistry.AUTOMATIC_LIB_DIR_PREFIXES;
            for (int length = automatic_LIB_DIR_PREFIXES.length, i = 0; i < length; ++i) {
                final Path resolve = this.classpathEltPath.resolve(automatic_LIB_DIR_PREFIXES[i]);
                if (FileUtils.canReadAndIsDir(resolve)) {
                    try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resolve)) {
                        for (final Path path : directoryStream) {
                            if (Files.isRegularFile(path, new LinkOption[0]) && path.getFileName().endsWith(".jar")) {
                                if (logNode != null) {
                                    this.log(this.classpathElementIdx, "Found lib jar: " + path, logNode);
                                }
                                workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(path, this.classLoader), this, n++));
                            }
                        }
                    }
                    catch (IOException ex) {}
                }
            }
            if (this.packageRootPath.equals(this.classpathEltPath)) {
                for (final String s : ClassLoaderHandlerRegistry.AUTOMATIC_PACKAGE_ROOT_PREFIXES) {
                    if (FileUtils.canReadAndIsDir(this.classpathEltPath.resolve(s))) {
                        if (logNode != null) {
                            this.log(this.classpathElementIdx, "Found package root: " + s, logNode);
                        }
                        workQueue.addWorkUnit(new Scanner.ClasspathEntryWorkUnit(new ClasspathOrder.ClasspathElementAndClassLoader(this.classpathEltPath, s, this.classLoader), this, n++));
                    }
                }
            }
        }
        catch (SecurityException ex2) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping classpath element, since dir cannot be accessed: " + this.classpathEltPath, logNode);
            }
            this.skipClasspathElement = true;
        }
    }
    
    public String getModuleName() {
        return (this.moduleNameFromModuleDescriptor == null || this.moduleNameFromModuleDescriptor.isEmpty()) ? null : this.moduleNameFromModuleDescriptor;
    }
}
