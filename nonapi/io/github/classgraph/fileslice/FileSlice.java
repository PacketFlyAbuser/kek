// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice;

import nonapi.io.github.classgraph.fileslice.reader.RandomAccessByteBufferReader;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessFileChannelReader;
import nonapi.io.github.classgraph.utils.LogNode;
import nonapi.io.github.classgraph.utils.FileUtils;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import java.io.InputStream;
import java.io.IOException;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.io.File;
import java.nio.channels.FileChannel;

public class FileSlice extends Slice
{
    private /* synthetic */ FileChannel fileChannel;
    public final /* synthetic */ File file;
    private final /* synthetic */ long fileLength;
    private /* synthetic */ ByteBuffer backingByteBuffer;
    public /* synthetic */ RandomAccessFile raf;
    private final /* synthetic */ AtomicBoolean isClosed;
    private final /* synthetic */ boolean isTopLevelFileSlice;
    
    private FileSlice(final FileSlice fileSlice, final long n, final long n2, final boolean b, final long n3, final NestedJarHandler nestedJarHandler) {
        super(fileSlice, n, n2, b, n3, nestedJarHandler);
        this.isClosed = new AtomicBoolean();
        this.file = fileSlice.file;
        this.raf = fileSlice.raf;
        this.fileChannel = fileSlice.fileChannel;
        this.fileLength = fileSlice.fileLength;
        this.isTopLevelFileSlice = false;
        if (fileSlice.backingByteBuffer != null) {
            this.backingByteBuffer = fileSlice.backingByteBuffer.duplicate();
            this.backingByteBuffer.position((int)this.sliceStartPos);
            this.backingByteBuffer.limit((int)(this.sliceStartPos + this.sliceLength));
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
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
    public void close() {
        if (!this.isClosed.getAndSet(true)) {
            if (this.isTopLevelFileSlice && this.backingByteBuffer != null) {
                FileUtils.closeDirectByteBuffer(this.backingByteBuffer, null);
            }
            this.backingByteBuffer = null;
            this.fileChannel = null;
            try {
                this.raf.close();
            }
            catch (IOException ex) {}
            this.raf = null;
            this.nestedJarHandler.markSliceAsClosed(this);
        }
    }
    
    public FileSlice(final File obj, final boolean b, final long n, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException {
        super(obj.length(), b, n, nestedJarHandler);
        this.isClosed = new AtomicBoolean();
        FileUtils.checkCanReadAndIsFile(obj);
        this.file = obj;
        this.raf = new RandomAccessFile(obj, "r");
        this.fileChannel = this.raf.getChannel();
        this.fileLength = obj.length();
        this.isTopLevelFileSlice = true;
        Label_0180: {
            if (nestedJarHandler.scanSpec.enableMemoryMapping) {
                try {
                    this.backingByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, this.fileLength);
                }
                catch (IOException | OutOfMemoryError ex) {
                    System.gc();
                    System.runFinalization();
                    try {
                        this.backingByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, this.fileLength);
                    }
                    catch (IOException | OutOfMemoryError ex2) {
                        final OutOfMemoryError outOfMemoryError;
                        final OutOfMemoryError obj2 = outOfMemoryError;
                        if (logNode == null) {
                            break Label_0180;
                        }
                        logNode.log("File " + obj + " cannot be memory mapped: " + obj2 + " (using RandomAccessFile API instead)");
                    }
                }
            }
        }
        nestedJarHandler.markSliceAsOpen(this);
    }
    
    @Override
    public Slice slice(final long n, final long n2, final boolean b, final long n3) {
        if (this.isDeflatedZipEntry) {
            throw new IllegalArgumentException("Cannot slice a deflated zip entry");
        }
        return new FileSlice(this, n, n2, b, n3, this.nestedJarHandler);
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
            if (this.backingByteBuffer != null) {
                return this.backingByteBuffer.duplicate();
            }
            if (this.sliceLength > 2147483639L) {
                throw new IOException("File is larger than 2GB");
            }
            return ByteBuffer.wrap(this.load());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    public FileSlice(final File file, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException {
        this(file, false, 0L, nestedJarHandler, logNode);
    }
    
    @Override
    public RandomAccessReader randomAccessReader() {
        if (this.backingByteBuffer == null) {
            return new RandomAccessFileChannelReader(this.fileChannel, this.sliceStartPos, this.sliceLength);
        }
        return new RandomAccessByteBufferReader(this.backingByteBuffer, this.sliceStartPos, this.sliceLength);
    }
}
