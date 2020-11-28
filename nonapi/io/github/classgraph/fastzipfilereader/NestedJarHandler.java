// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fastzipfilereader;

import nonapi.io.github.classgraph.recycler.Resettable;
import java.nio.file.Files;
import java.util.zip.DataFormatException;
import java.util.zip.ZipException;
import java.util.zip.Inflater;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.util.AbstractMap;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import nonapi.io.github.classgraph.fileslice.FileSlice;
import nonapi.io.github.classgraph.fileslice.ArraySlice;
import java.util.Arrays;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.net.HttpURLConnection;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import nonapi.io.github.classgraph.utils.LogNode;
import java.io.File;
import nonapi.io.github.classgraph.scanspec.ScanSpec;
import java.util.concurrent.atomic.AtomicBoolean;
import nonapi.io.github.classgraph.fileslice.Slice;
import java.util.Set;
import io.github.classgraph.ModuleReaderProxy;
import nonapi.io.github.classgraph.recycler.Recycler;
import io.github.classgraph.ModuleRef;
import java.io.IOException;
import java.util.Map;
import nonapi.io.github.classgraph.concurrency.SingletonMap;
import nonapi.io.github.classgraph.concurrency.InterruptionChecker;

public class NestedJarHandler
{
    public /* synthetic */ InterruptionChecker interruptionChecker;
    public /* synthetic */ SingletonMap<String, Map.Entry<LogicalZipFile, String>, IOException> nestedPathToLogicalZipFileAndPackageRootMap;
    public /* synthetic */ SingletonMap<ModuleRef, Recycler<ModuleReaderProxy, IOException>, IOException> moduleRefToModuleReaderProxyRecyclerMap;
    private /* synthetic */ Recycler<RecyclableInflater, RuntimeException> inflaterRecycler;
    private /* synthetic */ Set<Slice> openSlices;
    private final /* synthetic */ AtomicBoolean closed;
    public final /* synthetic */ ScanSpec scanSpec;
    private /* synthetic */ Set<File> tempFiles;
    private /* synthetic */ SingletonMap<FastZipEntry, ZipFileSlice, IOException> fastZipEntryToZipFileSliceMap;
    private /* synthetic */ SingletonMap<File, PhysicalZipFile, IOException> canonicalFileToPhysicalZipFileMap;
    private /* synthetic */ SingletonMap<ZipFileSlice, LogicalZipFile, IOException> zipFileSliceToLogicalZipFileMap;
    
    public void close(final LogNode logNode) {
        if (!this.closed.getAndSet(true)) {
            boolean b = false;
            if (this.moduleRefToModuleReaderProxyRecyclerMap != null) {
                int i = 0;
                while (i == 0) {
                    try {
                        final Iterator<Recycler<ModuleReaderProxy, IOException>> iterator = this.moduleRefToModuleReaderProxyRecyclerMap.values().iterator();
                        while (iterator.hasNext()) {
                            iterator.next().forceClose();
                        }
                        i = 1;
                    }
                    catch (InterruptedException ex) {
                        b = true;
                    }
                }
                this.moduleRefToModuleReaderProxyRecyclerMap.clear();
                this.moduleRefToModuleReaderProxyRecyclerMap = null;
            }
            if (this.zipFileSliceToLogicalZipFileMap != null) {
                this.zipFileSliceToLogicalZipFileMap.clear();
                this.zipFileSliceToLogicalZipFileMap = null;
            }
            if (this.nestedPathToLogicalZipFileAndPackageRootMap != null) {
                this.nestedPathToLogicalZipFileAndPackageRootMap.clear();
                this.nestedPathToLogicalZipFileAndPackageRootMap = null;
            }
            if (this.canonicalFileToPhysicalZipFileMap != null) {
                this.canonicalFileToPhysicalZipFileMap.clear();
                this.canonicalFileToPhysicalZipFileMap = null;
            }
            if (this.fastZipEntryToZipFileSliceMap != null) {
                this.fastZipEntryToZipFileSliceMap.clear();
                this.fastZipEntryToZipFileSliceMap = null;
            }
            if (this.openSlices != null) {
                while (!this.openSlices.isEmpty()) {
                    for (final Slice slice : new ArrayList<Slice>(this.openSlices)) {
                        try {
                            slice.close();
                        }
                        catch (IOException ex2) {}
                        this.markSliceAsClosed(slice);
                    }
                }
                this.openSlices.clear();
                this.openSlices = null;
            }
            if (this.inflaterRecycler != null) {
                this.inflaterRecycler.forceClose();
                this.inflaterRecycler = null;
            }
            if (this.tempFiles != null) {
                final LogNode logNode2 = (this.tempFiles.isEmpty() || logNode == null) ? null : logNode.log("Removing temporary files");
                while (!this.tempFiles.isEmpty()) {
                    for (final File obj : new ArrayList<File>(this.tempFiles)) {
                        try {
                            this.removeTempFile(obj);
                        }
                        catch (IOException | SecurityException ex3) {
                            if (logNode2 == null) {
                                continue;
                            }
                            logNode2.log("Removing temporary file failed: " + obj);
                        }
                    }
                }
                this.tempFiles = null;
            }
            if (b) {
                this.interruptionChecker.interrupt();
            }
        }
    }
    
    private static String leafname(final String s) {
        return s.substring(s.lastIndexOf(47) + 1);
    }
    
    public File makeTempFile(final String s, final boolean b) throws IOException {
        final File tempFile = File.createTempFile("ClassGraph--", "---" + this.sanitizeFilename(b ? leafname(s) : s));
        tempFile.deleteOnExit();
        this.tempFiles.add(tempFile);
        return tempFile;
    }
    
    private PhysicalZipFile downloadJarFromURL(final String s, final LogNode logNode) throws InterruptedException, IOException {
        URL url;
        try {
            url = new URL(s);
        }
        catch (MalformedURLException ex) {
            try {
                url = new URI(s).toURL();
            }
            catch (URISyntaxException ex2) {
                throw new IOException("Could not parse URL: " + s);
            }
        }
        final String protocol = url.getProtocol();
        if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
            try {
                final Path value = Paths.get(url.toURI());
                final FileSystem fileSystem = value.getFileSystem();
                if (logNode != null) {
                    logNode.log("URL " + s + " is backed by filesystem " + fileSystem.getClass().getName());
                }
                return new PhysicalZipFile(value, this, logNode);
            }
            catch (URISyntaxException ex3) {
                throw new IOException("Could not convert URL to URI: " + url);
            }
            catch (FileSystemNotFoundException ex4) {}
        }
        final URLConnection openConnection = url.openConnection();
        HttpURLConnection httpURLConnection = null;
        try {
            long contentLengthLong = -1L;
            if (openConnection instanceof HttpURLConnection) {
                httpURLConnection = (HttpURLConnection)url.openConnection();
                contentLengthLong = httpURLConnection.getContentLengthLong();
                if (contentLengthLong < -1L) {
                    contentLengthLong = -1L;
                }
            }
            else if (openConnection.getURL().getProtocol().equalsIgnoreCase("file")) {
                try {
                    return new PhysicalZipFile(new File(openConnection.getURL().toURI()), this, logNode);
                }
                catch (URISyntaxException ex5) {}
            }
            final LogNode logNode2 = (logNode == null) ? null : logNode.log("Downloading jar from URL " + s);
            try (final InputStream inputStream = openConnection.getInputStream()) {
                final PhysicalZipFile physicalZipFile = new PhysicalZipFile(inputStream, contentLengthLong, s, this, logNode2);
                if (logNode2 != null) {
                    logNode2.addElapsedTime();
                    logNode2.log("***** Note that it is time-consuming to scan jars at non-\"file:\" URLs, the URL must be opened (possibly after an http(s) fetch) for every scan, and the same URL must also be separately opened by the ClassLoader *****");
                }
                return physicalZipFile;
            }
            catch (MalformedURLException ex6) {
                throw new IOException("Malformed URL: " + s);
            }
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
    
    private String sanitizeFilename(final String s) {
        return s.replace('/', '_').replace('\\', '_').replace(':', '_').replace('?', '_').replace('&', '_').replace('=', '_').replace(' ', '_');
    }
    
    public Slice readAllBytesWithSpilloverToDisk(final InputStream inputStream, final String s, final long n, final LogNode logNode) throws IOException {
        Throwable t = null;
        try {
            if (n <= this.scanSpec.maxBufferedJarRAMSize) {
                byte[] copy;
                int length;
                int n2;
                int read;
                for (copy = new byte[(n == -1L) ? this.scanSpec.maxBufferedJarRAMSize : ((n == 0L) ? 16384 : Math.min((int)n, this.scanSpec.maxBufferedJarRAMSize))], length = copy.length, n2 = 0; (read = inputStream.read(copy, n2, length - n2)) > 0; n2 += read) {}
                if (read == 0) {
                    final byte[] b = { 0 };
                    if (inputStream.read(b, 0, 1) == 1) {
                        return this.spillToDisk(inputStream, s, copy, b, logNode);
                    }
                }
                if (n2 < copy.length) {
                    copy = Arrays.copyOf(copy, n2);
                }
                return new ArraySlice(copy, false, 0L, this);
            }
            return this.spillToDisk(inputStream, s, null, null, logNode);
        }
        catch (Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            if (inputStream != null) {
                if (t != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                else {
                    inputStream.close();
                }
            }
        }
    }
    
    public void markSliceAsOpen(final Slice slice) throws IOException {
        this.openSlices.add(slice);
    }
    
    private FileSlice spillToDisk(final InputStream inputStream, final String str, final byte[] b, final byte[] b2, final LogNode logNode) throws IOException {
        File tempFile;
        try {
            tempFile = this.makeTempFile(str, true);
        }
        catch (IOException ex) {
            throw new IOException("Could not create temporary file: " + ex.getMessage());
        }
        if (logNode != null) {
            logNode.log("Could not fit InputStream content into max RAM buffer size, saving to temporary file: " + str + " -> " + tempFile);
        }
        try (final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
            if (b != null) {
                bufferedOutputStream.write(b);
                bufferedOutputStream.write(b2);
            }
            final byte[] array = new byte[8192];
            int read;
            while ((read = inputStream.read(array, 0, array.length)) > 0) {
                bufferedOutputStream.write(array, 0, read);
            }
        }
        return new FileSlice(tempFile, this, logNode);
    }
    
    public NestedJarHandler(final ScanSpec scanSpec, final InterruptionChecker interruptionChecker) {
        this.canonicalFileToPhysicalZipFileMap = new SingletonMap<File, PhysicalZipFile, IOException>() {
            @Override
            public PhysicalZipFile newInstance(final File file, final LogNode logNode) throws IOException {
                return new PhysicalZipFile(file, NestedJarHandler.this, logNode);
            }
        };
        this.fastZipEntryToZipFileSliceMap = new SingletonMap<FastZipEntry, ZipFileSlice, IOException>() {
            @Override
            public ZipFileSlice newInstance(final FastZipEntry obj, final LogNode logNode) throws IOException, InterruptedException {
                ZipFileSlice zipFileSlice;
                if (!obj.isDeflated) {
                    zipFileSlice = new ZipFileSlice(obj);
                }
                else {
                    if (logNode != null) {
                        logNode.log("Inflating nested zip entry: " + obj + " ; uncompressed size: " + obj.uncompressedSize);
                    }
                    zipFileSlice = new ZipFileSlice(new PhysicalZipFile(obj.getSlice().open(), (obj.uncompressedSize >= 0L && obj.uncompressedSize <= 2147483639L) ? ((long)(int)obj.uncompressedSize) : -1L, obj.entryName, NestedJarHandler.this, logNode), obj);
                }
                return zipFileSlice;
            }
        };
        this.zipFileSliceToLogicalZipFileMap = new SingletonMap<ZipFileSlice, LogicalZipFile, IOException>() {
            @Override
            public LogicalZipFile newInstance(final ZipFileSlice zipFileSlice, final LogNode logNode) throws InterruptedException, IOException {
                return new LogicalZipFile(zipFileSlice, NestedJarHandler.this, logNode);
            }
        };
        this.nestedPathToLogicalZipFileAndPackageRootMap = new SingletonMap<String, Map.Entry<LogicalZipFile, String>, IOException>() {
            @Override
            public Map.Entry<LogicalZipFile, String> newInstance(final String s, final LogNode logNode) throws IOException, InterruptedException {
                final String resolve = FastPathResolver.resolve(s);
                final int lastIndex = resolve.lastIndexOf(33);
                if (lastIndex < 0) {
                    PhysicalZipFile access$000;
                    if (JarUtils.URL_SCHEME_PATTERN.matcher(resolve).matches()) {
                        final String substring = resolve.substring(0, resolve.indexOf(58));
                        if (NestedJarHandler.this.scanSpec.allowedURLSchemes == null || !NestedJarHandler.this.scanSpec.allowedURLSchemes.contains(substring)) {
                            throw new IOException("Scanning of URL scheme \"" + substring + "\" has not been enabled -- cannot scan classpath element: " + resolve);
                        }
                        access$000 = NestedJarHandler.this.downloadJarFromURL(resolve, logNode);
                    }
                    else {
                        try {
                            access$000 = NestedJarHandler.this.canonicalFileToPhysicalZipFileMap.get(new File(resolve).getCanonicalFile(), logNode);
                        }
                        catch (NullSingletonException obj) {
                            throw new IOException("Could not get PhysicalZipFile for path " + resolve + " : " + obj);
                        }
                        catch (SecurityException obj2) {
                            throw new IOException("Path component " + resolve + " could not be canonicalized: " + obj2);
                        }
                    }
                    final ZipFileSlice obj3 = new ZipFileSlice(access$000);
                    LogicalZipFile key;
                    try {
                        key = NestedJarHandler.this.zipFileSliceToLogicalZipFileMap.get(obj3, logNode);
                    }
                    catch (NullSingletonException obj4) {
                        throw new IOException("Could not get toplevel slice " + obj3 + " : " + obj4);
                    }
                    return new AbstractMap.SimpleEntry<LogicalZipFile, String>(key, "");
                }
                final String substring2 = resolve.substring(0, lastIndex);
                String str = FileUtils.sanitizeEntryPath(resolve.substring(lastIndex + 1), true, true);
                Map.Entry<LogicalZipFile, String> entry;
                try {
                    entry = NestedJarHandler.this.nestedPathToLogicalZipFileAndPackageRootMap.get(substring2, logNode);
                }
                catch (NullSingletonException obj5) {
                    throw new IOException("Could not get parent logical zipfile " + substring2 + " : " + obj5);
                }
                final LogicalZipFile obj6 = entry.getKey();
                int n = 0;
                while (str.endsWith("/")) {
                    n = 1;
                    str = str.substring(0, str.length() - 1);
                }
                FastZipEntry obj7 = null;
                if (n == 0) {
                    for (final FastZipEntry fastZipEntry : obj6.entries) {
                        if (fastZipEntry.entryName.equals(str)) {
                            obj7 = fastZipEntry;
                            break;
                        }
                    }
                }
                if (obj7 == null) {
                    final String string = str + "/";
                    final Iterator<FastZipEntry> iterator2 = obj6.entries.iterator();
                    while (iterator2.hasNext()) {
                        if (iterator2.next().entryName.startsWith(string)) {
                            n = 1;
                            break;
                        }
                    }
                }
                if (n != 0) {
                    if (!str.isEmpty()) {
                        if (logNode != null) {
                            logNode.log("Path " + str + " in jarfile " + obj6 + " is a directory, not a file -- using as package root");
                        }
                        obj6.classpathRoots.add(str);
                    }
                    return new AbstractMap.SimpleEntry<LogicalZipFile, String>(obj6, str);
                }
                if (obj7 == null) {
                    throw new IOException("Path " + str + " does not exist in jarfile " + obj6);
                }
                if (!NestedJarHandler.this.scanSpec.scanNestedJars) {
                    throw new IOException("Nested jar scanning is disabled -- skipping nested jar " + resolve);
                }
                ZipFileSlice zipFileSlice;
                try {
                    zipFileSlice = NestedJarHandler.this.fastZipEntryToZipFileSliceMap.get(obj7, logNode);
                }
                catch (NullSingletonException obj8) {
                    throw new IOException("Could not get child zip entry slice " + obj7 + " : " + obj8);
                }
                final LogNode logNode2 = (logNode == null) ? null : logNode.log("Getting zipfile slice " + zipFileSlice + " for nested jar " + obj7.entryName);
                LogicalZipFile key2;
                try {
                    key2 = NestedJarHandler.this.zipFileSliceToLogicalZipFileMap.get(zipFileSlice, logNode2);
                }
                catch (NullSingletonException obj9) {
                    throw new IOException("Could not get child logical zipfile " + zipFileSlice + " : " + obj9);
                }
                return new AbstractMap.SimpleEntry<LogicalZipFile, String>(key2, "");
            }
        };
        this.moduleRefToModuleReaderProxyRecyclerMap = new SingletonMap<ModuleRef, Recycler<ModuleReaderProxy, IOException>, IOException>() {
            @Override
            public Recycler<ModuleReaderProxy, IOException> newInstance(final ModuleRef moduleRef, final LogNode logNode) {
                return new Recycler<ModuleReaderProxy, IOException>() {
                    @Override
                    public ModuleReaderProxy newInstance() throws IOException {
                        return moduleRef.open();
                    }
                };
            }
        };
        this.inflaterRecycler = new Recycler<RecyclableInflater, RuntimeException>() {
            @Override
            public RecyclableInflater newInstance() throws RuntimeException {
                return new RecyclableInflater();
            }
        };
        this.openSlices = Collections.newSetFromMap(new ConcurrentHashMap<Slice, Boolean>());
        this.tempFiles = Collections.newSetFromMap(new ConcurrentHashMap<File, Boolean>());
        this.closed = new AtomicBoolean(false);
        this.scanSpec = scanSpec;
        this.interruptionChecker = interruptionChecker;
    }
    
    public void markSliceAsClosed(final Slice slice) {
        this.openSlices.remove(slice);
    }
    
    public static byte[] readAllBytesAsArray(final InputStream inputStream, final long n) throws IOException {
        if (n > 2147483639L) {
            throw new IOException("InputStream is too large to read");
        }
        Throwable t = null;
        try {
            byte[] copy = new byte[(n < 1L) ? 16384 : Math.min((int)n, 16777216)];
            int n2 = 0;
            while (true) {
                final int read;
                if ((read = inputStream.read(copy, n2, copy.length - n2)) > 0) {
                    n2 += read;
                }
                else {
                    if (read < 0) {
                        break;
                    }
                    final int read2 = inputStream.read();
                    if (read2 == -1) {
                        break;
                    }
                    if (copy.length == 2147483639) {
                        throw new IOException("InputStream too large to read into array");
                    }
                    copy = Arrays.copyOf(copy, (int)Math.min(copy.length * 2L, 2147483639L));
                    copy[n2++] = (byte)read2;
                }
            }
            return (n2 == copy.length) ? copy : Arrays.copyOf(copy, n2);
        }
        catch (Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            if (inputStream != null) {
                if (t != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                else {
                    inputStream.close();
                }
            }
        }
    }
    
    public InputStream openInflaterInputStream(final InputStream inputStream) throws IOException {
        return new InputStream() {
            private final /* synthetic */ Inflater inflater = this.recyclableInflater.getInflater();
            private final /* synthetic */ RecyclableInflater recyclableInflater = NestedJarHandler.this.inflaterRecycler.acquire();
            private final /* synthetic */ AtomicBoolean closed = new AtomicBoolean();
            private final /* synthetic */ byte[] buf = new byte[8192];
            
            @Override
            public long skip(final long n) throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                if (n < 0L) {
                    throw new IllegalArgumentException("numToSkip cannot be negative");
                }
                if (n == 0L) {
                    return 0L;
                }
                if (this.inflater.finished()) {
                    return -1L;
                }
                long n2 = 0L;
                while (true) {
                    final int read = this.read(this.buf, 0, (int)Math.min(n - n2, this.buf.length));
                    if (read <= 0) {
                        break;
                    }
                    n2 -= read;
                }
                return n2;
            }
            
            @Override
            public void close() {
                if (!this.closed.getAndSet(true)) {
                    try {
                        inputStream.close();
                    }
                    catch (IOException ex) {}
                    finally {
                        NestedJarHandler.this.inflaterRecycler.recycle(this.recyclableInflater);
                    }
                }
            }
            
            @Override
            public synchronized void mark(final int n) {
                throw new IllegalArgumentException("Not supported");
            }
            
            @Override
            public int read(final byte[] output, final int n, final int n2) throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                if (n2 < 0) {
                    throw new IllegalArgumentException("len cannot be negative");
                }
                if (n2 == 0) {
                    return 0;
                }
                try {
                    int n3 = 0;
                    while (!this.inflater.finished() && n3 < n2) {
                        final int inflate = this.inflater.inflate(output, n + n3, n2 - n3);
                        if (inflate == 0) {
                            if (this.inflater.needsDictionary()) {
                                throw new IOException("Inflater needs preset dictionary");
                            }
                            if (!this.inflater.needsInput()) {
                                continue;
                            }
                            final int read = inputStream.read(this.buf, 0, this.buf.length);
                            if (read == -1) {
                                this.buf[0] = 0;
                                this.inflater.setInput(this.buf, 0, 1);
                            }
                            else {
                                this.inflater.setInput(this.buf, 0, read);
                            }
                        }
                        else {
                            n3 += inflate;
                        }
                    }
                    if (n3 == 0) {
                        return -1;
                    }
                    return n3;
                }
                catch (DataFormatException ex) {
                    throw new ZipException((ex.getMessage() != null) ? ex.getMessage() : "Invalid deflated zip entry data");
                }
            }
            
            static {
                INFLATE_BUF_SIZE = 8192;
            }
            
            @Override
            public int available() throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                return this.inflater.finished() ? 0 : 1;
            }
            
            @Override
            public synchronized void reset() throws IOException {
                throw new IllegalArgumentException("Not supported");
            }
            
            @Override
            public int read() throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                if (this.inflater.finished()) {
                    return -1;
                }
                if (this.read(this.buf, 0, 1) < 0) {
                    return -1;
                }
                return this.buf[0] & 0xFF;
            }
            
            @Override
            public boolean markSupported() {
                return false;
            }
        };
    }
    
    void removeTempFile(final File obj) throws SecurityException, IOException {
        if (this.tempFiles.contains(obj)) {
            try {
                Files.delete(obj.toPath());
            }
            finally {
                this.tempFiles.remove(obj);
            }
            return;
        }
        throw new IOException("Not a temp file: " + obj);
    }
    
    static {
        TEMP_FILENAME_LEAF_SEPARATOR = "---";
        DEFAULT_BUFFER_SIZE = 16384;
        MAX_INITIAL_BUFFER_SIZE = 16777216;
    }
    
    private static class RecyclableInflater implements Resettable, AutoCloseable
    {
        private final /* synthetic */ Inflater inflater;
        
        private RecyclableInflater() {
            this.inflater = new Inflater(true);
        }
        
        public Inflater getInflater() {
            return this.inflater;
        }
        
        @Override
        public void reset() {
            this.inflater.reset();
        }
        
        @Override
        public void close() {
            this.inflater.end();
        }
    }
}
