// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.nio.file.attribute.PosixFilePermission;
import java.nio.ByteBuffer;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.URI;
import nonapi.io.github.classgraph.concurrency.WorkQueue;
import java.io.File;
import java.util.Iterator;
import nonapi.io.github.classgraph.recycler.RecycleOnClose;
import java.util.List;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import nonapi.io.github.classgraph.utils.VersionFinder;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.HashSet;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.concurrency.SingletonMap;
import java.util.Set;
import java.io.IOException;
import nonapi.io.github.classgraph.recycler.Recycler;

class ClasspathElementModule extends ClasspathElement
{
    private /* synthetic */ Recycler<ModuleReaderProxy, IOException> moduleReaderProxyRecycler;
    final /* synthetic */ ModuleRef moduleRef;
    private final /* synthetic */ Set<String> allResourcePaths;
    /* synthetic */ SingletonMap<ModuleRef, Recycler<ModuleReaderProxy, IOException>, IOException> moduleRefToModuleReaderProxyRecyclerMap;
    
    ModuleRef getModuleRef() {
        return this.moduleRef;
    }
    
    ClasspathElementModule(final ModuleRef moduleRef, final ClassLoader classLoader, final SingletonMap<ModuleRef, Recycler<ModuleReaderProxy, IOException>, IOException> moduleRefToModuleReaderProxyRecyclerMap, final ScanSpec scanSpec) {
        super(classLoader, scanSpec);
        this.allResourcePaths = new HashSet<String>();
        this.moduleRefToModuleReaderProxyRecyclerMap = moduleRefToModuleReaderProxyRecyclerMap;
        this.moduleRef = moduleRef;
    }
    
    public String getModuleName() {
        String s = this.moduleRef.getName();
        if (s == null || s.isEmpty()) {
            s = this.moduleNameFromModuleDescriptor;
        }
        return (s == null || s.isEmpty()) ? null : s;
    }
    
    @Override
    Resource getResource(final String s) {
        return this.allResourcePaths.contains(s) ? this.newResource(s) : null;
    }
    
    @Override
    void scanPaths(final LogNode logNode) {
        if (this.skipClasspathElement) {
            return;
        }
        if (this.scanned.getAndSet(true)) {
            throw new IllegalArgumentException("Already scanned classpath element " + this.toString());
        }
        final LogNode logNode2 = (logNode == null) ? null : this.log(this.classpathElementIdx, "Scanning module " + this.moduleRef.getName(), logNode);
        final boolean b = VersionFinder.JAVA_MAJOR_VERSION >= 9 && this.getModuleName() != null;
        try (final RecycleOnClose<ModuleReaderProxy, IOException> acquireRecycleOnClose = this.moduleReaderProxyRecycler.acquireRecycleOnClose()) {
            List<String> list;
            try {
                list = acquireRecycleOnClose.get().list();
            }
            catch (SecurityException ex) {
                if (logNode2 != null) {
                    logNode2.log("Could not get resource list for module " + this.moduleRef.getName(), ex);
                }
                return;
            }
            CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
            Object anObject = null;
            ScanSpec.ScanSpecPathMatch scanSpecPathMatch = null;
            for (final String s : list) {
                if (s.endsWith("/")) {
                    continue;
                }
                if (s.startsWith("META-INF/versions/")) {
                    if (logNode2 == null) {
                        continue;
                    }
                    logNode2.log("Found unexpected nested versioned entry in module -- skipping: " + s);
                }
                else {
                    if (b && s.indexOf(47) < 0 && s.endsWith(".class") && !s.equals("module-info.class")) {
                        continue;
                    }
                    this.checkResourcePathAcceptReject(s, logNode);
                    if (this.skipClasspathElement) {
                        return;
                    }
                    final int lastIndex = s.lastIndexOf(47);
                    final String s2 = (lastIndex < 0) ? "/" : s.substring(0, lastIndex + 1);
                    final boolean b2 = !s2.equals(anObject);
                    final ScanSpec.ScanSpecPathMatch scanSpecPathMatch2 = (anObject == null || b2) ? this.scanSpec.dirAcceptMatchStatus(s2) : scanSpecPathMatch;
                    anObject = s2;
                    if ((scanSpecPathMatch = scanSpecPathMatch2) == ScanSpec.ScanSpecPathMatch.HAS_REJECTED_PATH_PREFIX) {
                        if (logNode2 == null) {
                            continue;
                        }
                        logNode2.log("Skipping rejected path: " + s);
                    }
                    else {
                        if (!this.allResourcePaths.add(s)) {
                            continue;
                        }
                        if (scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.HAS_ACCEPTED_PATH_PREFIX || scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_PATH || (scanSpecPathMatch2 == ScanSpec.ScanSpecPathMatch.AT_ACCEPTED_CLASS_PACKAGE && this.scanSpec.classfileIsSpecificallyAccepted(s))) {
                            this.addAcceptedResource(this.newResource(s), scanSpecPathMatch2, false, logNode2);
                        }
                        else {
                            if (!this.scanSpec.enableClassInfo || !s.equals("module-info.class")) {
                                continue;
                            }
                            this.addAcceptedResource(this.newResource(s), scanSpecPathMatch2, true, logNode2);
                        }
                    }
                }
            }
            final File locationFile = this.moduleRef.getLocationFile();
            if (locationFile != null && locationFile.exists()) {
                this.fileToLastModified.put(locationFile, locationFile.lastModified());
            }
        }
        catch (IOException ex2) {
            if (logNode2 != null) {
                logNode2.log("Exception opening module " + this.moduleRef.getName(), ex2);
            }
            this.skipClasspathElement = true;
        }
        this.finishScanPaths(logNode2);
    }
    
    @Override
    void open(final WorkQueue<Scanner.ClasspathEntryWorkUnit> workQueue, final LogNode logNode) throws InterruptedException {
        if (!this.scanSpec.scanModules) {
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping module, since module scanning is disabled: " + this.getModuleName(), logNode);
            }
            this.skipClasspathElement = true;
            return;
        }
        try {
            this.moduleReaderProxyRecycler = this.moduleRefToModuleReaderProxyRecyclerMap.get(this.moduleRef, logNode);
        }
        catch (IOException | SingletonMap.NullSingletonException ex2) {
            final SingletonMap.NullSingletonException ex;
            final SingletonMap.NullSingletonException obj = ex;
            if (logNode != null) {
                this.log(this.classpathElementIdx, "Skipping invalid module " + this.getModuleName() + " : " + obj, logNode);
            }
            this.skipClasspathElement = true;
        }
    }
    
    private String getModuleNameOrEmpty() {
        final String moduleName = this.getModuleName();
        return (moduleName == null) ? "" : moduleName;
    }
    
    @Override
    File getFile() {
        try {
            final URI location = this.moduleRef.getLocation();
            if (location != null && !location.getScheme().equals("jrt")) {
                final File file = new File(location);
                if (file.exists()) {
                    return file;
                }
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    private Resource newResource(final String s) {
        return new Resource(this, -1L) {
            protected /* synthetic */ AtomicBoolean isOpen = new AtomicBoolean();
            private /* synthetic */ ModuleReaderProxy moduleReaderProxy;
            
            @Override
            public InputStream open() throws IOException {
                if (ClasspathElementModule.this.skipClasspathElement) {
                    throw new IOException("Module could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                try {
                    this.moduleReaderProxy = ClasspathElementModule.this.moduleReaderProxyRecycler.acquire();
                    this.inputStream = this.moduleReaderProxy.open(s);
                    this.length = -1L;
                    return this.inputStream;
                }
                catch (SecurityException cause) {
                    this.close();
                    throw new IOException("Could not open " + this, cause);
                }
            }
            
            @Override
            public String getPath() {
                return s;
            }
            
            @Override
            public byte[] load() throws IOException {
                this.read();
                Throwable t = null;
                try {
                    byte[] array;
                    if (this.byteBuffer.hasArray() && this.byteBuffer.position() == 0 && this.byteBuffer.limit() == this.byteBuffer.capacity()) {
                        array = this.byteBuffer.array();
                    }
                    else {
                        array = new byte[this.byteBuffer.remaining()];
                        this.byteBuffer.get(array);
                    }
                    this.length = array.length;
                    return array;
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
            ClassfileReader openClassfile() throws IOException {
                return new ClassfileReader(this.open());
            }
            
            @Override
            public String getPathRelativeToClasspathElement() {
                return s;
            }
            
            @Override
            public ByteBuffer read() throws IOException {
                if (ClasspathElementModule.this.skipClasspathElement) {
                    throw new IOException("Module could not be opened");
                }
                if (this.isOpen.getAndSet(true)) {
                    throw new IOException("Resource is already open -- cannot open it again without first calling close()");
                }
                try {
                    this.moduleReaderProxy = ClasspathElementModule.this.moduleReaderProxyRecycler.acquire();
                    this.byteBuffer = this.moduleReaderProxy.read(s);
                    this.length = this.byteBuffer.remaining();
                    return this.byteBuffer;
                }
                catch (SecurityException | OutOfMemoryError ex) {
                    final OutOfMemoryError outOfMemoryError;
                    final OutOfMemoryError cause = outOfMemoryError;
                    this.close();
                    throw new IOException("Could not open " + this, cause);
                }
            }
            
            @Override
            public void close() {
                super.close();
                if (this.isOpen.getAndSet(false) && this.moduleReaderProxy != null) {
                    if (this.byteBuffer != null) {
                        this.moduleReaderProxy.release(this.byteBuffer);
                    }
                    ClasspathElementModule.this.moduleReaderProxyRecycler.recycle(this.moduleReaderProxy);
                    this.moduleReaderProxy = null;
                }
            }
            
            @Override
            public long getLastModified() {
                return 0L;
            }
            
            @Override
            public Set<PosixFilePermission> getPosixFilePermissions() {
                return null;
            }
        };
    }
    
    @Override
    public int hashCode() {
        return this.getModuleNameOrEmpty().hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof ClasspathElementModule && this.getModuleNameOrEmpty().equals(((ClasspathElementModule)o).getModuleNameOrEmpty()));
    }
    
    @Override
    URI getURI() {
        final URI location = this.moduleRef.getLocation();
        if (location == null) {
            throw new IllegalArgumentException("Module " + this.getModuleName() + " has a null location");
        }
        return location;
    }
    
    @Override
    public String toString() {
        return this.moduleRef.toString();
    }
}
