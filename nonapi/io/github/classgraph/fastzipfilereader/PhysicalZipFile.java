// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fastzipfilereader;

import nonapi.io.github.classgraph.fileslice.PathSlice;
import java.io.InputStream;
import java.util.Objects;
import nonapi.io.github.classgraph.fileslice.FileSlice;
import nonapi.io.github.classgraph.utils.FastPathResolver;
import nonapi.io.github.classgraph.utils.FileUtils;
import nonapi.io.github.classgraph.utils.LogNode;
import java.io.IOException;
import nonapi.io.github.classgraph.fileslice.ArraySlice;
import java.nio.file.Path;
import nonapi.io.github.classgraph.fileslice.Slice;
import java.io.File;

class PhysicalZipFile
{
    private final /* synthetic */ String pathStr;
    private /* synthetic */ File file;
    /* synthetic */ Slice slice;
    /* synthetic */ NestedJarHandler nestedJarHandler;
    private /* synthetic */ Path path;
    private /* synthetic */ int hashCode;
    
    @Override
    public String toString() {
        return this.pathStr;
    }
    
    PhysicalZipFile(final byte[] array, final File file, final String pathStr, final NestedJarHandler nestedJarHandler) throws IOException {
        this.nestedJarHandler = nestedJarHandler;
        this.file = file;
        this.pathStr = pathStr;
        this.slice = new ArraySlice(array, false, 0L, nestedJarHandler);
    }
    
    public long length() {
        return this.slice.sliceLength;
    }
    
    PhysicalZipFile(final File file, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException {
        this.nestedJarHandler = nestedJarHandler;
        FileUtils.checkCanReadAndIsFile(file);
        this.file = file;
        this.pathStr = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, file.getPath());
        this.slice = new FileSlice(file, nestedJarHandler, logNode);
    }
    
    public String getPathStr() {
        return this.pathStr;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof PhysicalZipFile && Objects.equals(this.file, ((PhysicalZipFile)o).file));
    }
    
    PhysicalZipFile(final InputStream inputStream, final long n, final String pathStr, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException {
        this.nestedJarHandler = nestedJarHandler;
        this.pathStr = pathStr;
        this.slice = nestedJarHandler.readAllBytesWithSpilloverToDisk(inputStream, pathStr, n, logNode);
        this.file = ((this.slice instanceof FileSlice) ? ((FileSlice)this.slice).file : null);
    }
    
    public File getFile() {
        return this.file;
    }
    
    public Path getPath() {
        return this.path;
    }
    
    PhysicalZipFile(final Path path, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException {
        this.nestedJarHandler = nestedJarHandler;
        FileUtils.checkCanReadAndIsFile(path);
        this.path = path;
        this.pathStr = FastPathResolver.resolve(FileUtils.CURR_DIR_PATH, path.toString());
        this.slice = new PathSlice(path, nestedJarHandler);
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = ((this.file == null) ? 0 : this.file.hashCode());
            if (this.hashCode == 0) {
                this.hashCode = 1;
            }
        }
        return this.hashCode;
    }
}
