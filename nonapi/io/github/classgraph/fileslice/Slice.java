// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice;

import java.util.concurrent.atomic.AtomicBoolean;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.io.IOException;
import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;
import java.io.Closeable;

public abstract class Slice implements Closeable
{
    public /* synthetic */ long sliceLength;
    public final /* synthetic */ long inflatedLengthHint;
    public final /* synthetic */ long sliceStartPos;
    private /* synthetic */ int hashCode;
    protected final /* synthetic */ NestedJarHandler nestedJarHandler;
    public final /* synthetic */ boolean isDeflatedZipEntry;
    protected final /* synthetic */ Slice parentSlice;
    
    public abstract RandomAccessReader randomAccessReader();
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Slice)) {
            return false;
        }
        final Slice slice = (Slice)o;
        return this.parentSlice == slice.parentSlice && this.sliceStartPos == slice.sliceStartPos && this.sliceLength == slice.sliceLength;
    }
    
    public ClassfileReader openClassfileReader() throws IOException {
        if (this.sliceLength > 2147483639L) {
            throw new IllegalArgumentException("Cannot open slices larger than 2GB for sequential buffered reading");
        }
        return new ClassfileReader(this);
    }
    
    public abstract Slice slice(final long p0, final long p1, final boolean p2, final long p3);
    
    public ByteBuffer read() throws IOException {
        return ByteBuffer.wrap(this.load());
    }
    
    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = (((this.parentSlice == null) ? 1 : this.parentSlice.hashCode()) ^ (int)this.sliceStartPos * 7 ^ (int)this.sliceLength * 15);
            if (this.hashCode == 0) {
                this.hashCode = 1;
            }
        }
        return this.hashCode;
    }
    
    protected Slice(final Slice parentSlice, final long n, final long sliceLength, final boolean isDeflatedZipEntry, final long inflatedLengthHint, final NestedJarHandler nestedJarHandler) {
        this.parentSlice = parentSlice;
        final long n2 = (parentSlice == null) ? 0L : parentSlice.sliceStartPos;
        this.sliceStartPos = n2 + n;
        this.sliceLength = sliceLength;
        this.isDeflatedZipEntry = isDeflatedZipEntry;
        this.inflatedLengthHint = inflatedLengthHint;
        this.nestedJarHandler = nestedJarHandler;
        if (this.sliceStartPos < 0L) {
            throw new IllegalArgumentException("Invalid startPos");
        }
        if (sliceLength < 0L) {
            throw new IllegalArgumentException("Invalid length");
        }
        if (parentSlice != null && (this.sliceStartPos < n2 || this.sliceStartPos + sliceLength > n2 + parentSlice.sliceLength)) {
            throw new IllegalArgumentException("Child slice is not completely contained within parent slice");
        }
    }
    
    @Override
    public void close() throws IOException {
    }
    
    public abstract byte[] load() throws IOException;
    
    public String loadAsString() throws IOException {
        return new String(this.load(), StandardCharsets.UTF_8);
    }
    
    protected Slice(final long n, final boolean b, final long n2, final NestedJarHandler nestedJarHandler) {
        this(null, 0L, n, b, n2, nestedJarHandler);
    }
    
    public InputStream open() throws IOException {
        final InputStream inputStream = new InputStream() {
            private /* synthetic */ long markOff;
            private final /* synthetic */ AtomicBoolean closed = new AtomicBoolean();
            private /* synthetic */ long currOff;
            /* synthetic */ RandomAccessReader randomAccessReader = Slice.this.randomAccessReader();
            private final /* synthetic */ byte[] byteBuf = new byte[1];
            
            @Override
            public int read(final byte[] array, final int n, final int a) throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                if (a == 0) {
                    return 0;
                }
                final int min = Math.min(a, this.available());
                if (min < 1) {
                    return -1;
                }
                final int read = this.randomAccessReader.read(this.currOff, array, n, min);
                if (read > 0) {
                    this.currOff += read;
                }
                return read;
            }
            
            @Override
            public int available() {
                return (int)Math.min(Math.max(Slice.this.sliceLength - this.currOff, 0L), 2147483639L);
            }
            
            @Override
            public int read() throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                return this.read(this.byteBuf, 0, 1);
            }
            
            @Override
            public synchronized void reset() {
                this.currOff = this.markOff;
            }
            
            @Override
            public boolean markSupported() {
                return true;
            }
            
            @Override
            public void close() {
                this.closed.getAndSet(true);
            }
            
            @Override
            public synchronized void mark(final int n) {
                this.markOff = this.currOff;
            }
            
            @Override
            public long skip(final long n) throws IOException {
                if (this.closed.get()) {
                    throw new IOException("Already closed");
                }
                final long min = Math.min(this.currOff + n, Slice.this.sliceLength);
                final long n2 = min - this.currOff;
                this.currOff = min;
                return n2;
            }
        };
        return this.isDeflatedZipEntry ? this.nestedJarHandler.openInflaterInputStream(inputStream) : inputStream;
    }
}
