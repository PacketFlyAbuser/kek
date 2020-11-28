// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice;

import java.nio.ByteBuffer;
import java.nio.file.StandardOpenOption;
import java.nio.file.OpenOption;
import nonapi.io.github.classgraph.utils.FileUtils;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessFileChannelReader;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import java.io.InputStream;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.file.Path;
import java.nio.channels.FileChannel;
import java.io.Closeable;

public class PathSlice extends Slice implements Closeable
{
    private /* synthetic */ FileChannel fileChannel;
    private final /* synthetic */ boolean isTopLevelFileSlice;
    public final /* synthetic */ Path path;
    private final /* synthetic */ AtomicBoolean isClosed;
    private final /* synthetic */ long fileLength;
    
    @Override
    public byte[] load() throws IOException {
        if (this.isDeflatedZipEntry) {
            if (this.inflatedLengthHint > 2147483639L) {
                throw new IOException("Uncompressed size is larger than 2GB");
            }
            try (final InputStream open = this.open()) {
                return NestedJarHandler.readAllBytesAsArray(open, this.inflatedLengthHint);
            }
        }
        if (this.sliceLength > 2147483639L) {
            throw new IOException("File is larger than 2GB");
        }
        final RandomAccessReader randomAccessReader = this.randomAccessReader();
        final byte[] array = new byte[(int)this.sliceLength];
        if (randomAccessReader.read(0L, array, 0, array.length) < array.length) {
            throw new IOException("File is truncated");
        }
        return array;
    }
    
    @Override
    public RandomAccessReader randomAccessReader() {
        return new RandomAccessFileChannelReader(this.fileChannel, this.sliceStartPos, this.sliceLength);
    }
    
    @Override
    public void close() {
        if (!this.isClosed.getAndSet(true)) {
            if (this.isTopLevelFileSlice && this.fileChannel != null) {
                try {
                    this.fileChannel.close();
                }
                catch (IOException ex) {}
                this.fileChannel = null;
            }
            this.fileChannel = null;
            this.nestedJarHandler.markSliceAsClosed(this);
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    public PathSlice(final Path path, final boolean b, final long n, final NestedJarHandler nestedJarHandler) throws IOException {
        super(0L, b, n, nestedJarHandler);
        this.isClosed = new AtomicBoolean();
        FileUtils.checkCanReadAndIsFile(path);
        this.path = path;
        this.fileChannel = FileChannel.open(path, StandardOpenOption.READ);
        this.fileLength = this.fileChannel.size();
        this.isTopLevelFileSlice = true;
        this.sliceLength = this.fileLength;
        nestedJarHandler.markSliceAsOpen(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    public PathSlice(final Path path, final NestedJarHandler nestedJarHandler) throws IOException {
        this(path, false, 0L, nestedJarHandler);
    }
    
    @Override
    public Slice slice(final long n, final long n2, final boolean b, final long n3) {
        if (this.isDeflatedZipEntry) {
            throw new IllegalArgumentException("Cannot slice a deflated zip entry");
        }
        return new PathSlice(this, n, n2, b, n3, this.nestedJarHandler);
    }
    
    private PathSlice(final PathSlice pathSlice, final long n, final long n2, final boolean b, final long n3, final NestedJarHandler nestedJarHandler) {
        super(pathSlice, n, n2, b, n3, nestedJarHandler);
        this.isClosed = new AtomicBoolean();
        this.path = pathSlice.path;
        this.fileChannel = pathSlice.fileChannel;
        this.fileLength = pathSlice.fileLength;
        this.isTopLevelFileSlice = false;
    }
    
    @Override
    public ByteBuffer read() throws IOException {
        if (this.isDeflatedZipEntry) {
            if (this.inflatedLengthHint > 2147483639L) {
                throw new IOException("Uncompressed size is larger than 2GB");
            }
            return ByteBuffer.wrap(this.load());
        }
        else {
            if (this.sliceLength > 2147483639L) {
                throw new IOException("File is larger than 2GB");
            }
            return ByteBuffer.wrap(this.load());
        }
    }
}
