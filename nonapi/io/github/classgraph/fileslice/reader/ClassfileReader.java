// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice.reader;

import java.nio.ReadOnlyBufferException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import nonapi.io.github.classgraph.fileslice.ArraySlice;
import nonapi.io.github.classgraph.fileslice.Slice;
import java.util.Arrays;
import nonapi.io.github.classgraph.utils.StringUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;

public class ClassfileReader implements Closeable, SequentialReader, RandomAccessReader
{
    private /* synthetic */ int classfileLengthHint;
    private /* synthetic */ int currIdx;
    private /* synthetic */ InputStream inflaterInputStream;
    private /* synthetic */ byte[] arr;
    private /* synthetic */ RandomAccessReader randomAccessReader;
    private /* synthetic */ int arrUsed;
    
    static {
        INITIAL_BUF_SIZE = 16384;
        BUF_CHUNK_SIZE = 8184;
    }
    
    @Override
    public int readUnsignedByte() throws IOException {
        final int unsignedByte = this.readUnsignedByte(this.currIdx);
        ++this.currIdx;
        return unsignedByte;
    }
    
    public int currPos() {
        return this.currIdx;
    }
    
    @Override
    public byte readByte() throws IOException {
        final byte byte1 = this.readByte(this.currIdx);
        ++this.currIdx;
        return byte1;
    }
    
    @Override
    public int read(final long n, final byte[] array, final int n2, final int a) throws IOException {
        if (a == 0) {
            return 0;
        }
        final int n3 = (int)n;
        if (n3 + a > this.arrUsed) {
            this.readTo(n3 + a);
        }
        final int max = Math.max(Math.min(a, array.length - n2), 0);
        if (max == 0) {
            return -1;
        }
        try {
            System.arraycopy(this.arr, n3, array, n2, max);
            return max;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
    
    @Override
    public long readLong() throws IOException {
        final long long1 = this.readLong(this.currIdx);
        this.currIdx += 8;
        return long1;
    }
    
    @Override
    public void close() {
        try {
            if (this.inflaterInputStream != null) {
                this.inflaterInputStream.close();
            }
        }
        catch (Exception ex) {}
    }
    
    public ClassfileReader(final InputStream inflaterInputStream) throws IOException {
        this.classfileLengthHint = -1;
        this.inflaterInputStream = inflaterInputStream;
        this.arr = new byte[16384];
    }
    
    @Override
    public short readShort(final long n) throws IOException {
        return (short)this.readUnsignedShort(n);
    }
    
    @Override
    public int readInt() throws IOException {
        final int int1 = this.readInt(this.currIdx);
        this.currIdx += 4;
        return int1;
    }
    
    @Override
    public String readString(final int n, final boolean b, final boolean b2) throws IOException {
        final String string = StringUtils.readString(this.arr, this.currIdx, n, b, b2);
        this.currIdx += n;
        return string;
    }
    
    @Override
    public int readUnsignedShort() throws IOException {
        final int unsignedShort = this.readUnsignedShort(this.currIdx);
        this.currIdx += 2;
        return unsignedShort;
    }
    
    @Override
    public long readLong(final long n) throws IOException {
        final int n2 = (int)n;
        if (n2 + 8 > this.arrUsed) {
            this.readTo(n2 + 8);
        }
        return ((long)this.arr[n2] & 0xFFL) << 56 | ((long)this.arr[n2 + 1] & 0xFFL) << 48 | ((long)this.arr[n2 + 2] & 0xFFL) << 40 | ((long)this.arr[n2 + 3] & 0xFFL) << 32 | ((long)this.arr[n2 + 4] & 0xFFL) << 24 | ((long)this.arr[n2 + 5] & 0xFFL) << 16 | ((long)this.arr[n2 + 6] & 0xFFL) << 8 | ((long)this.arr[n2 + 7] & 0xFFL);
    }
    
    @Override
    public int readUnsignedShort(final long n) throws IOException {
        final int n2 = (int)n;
        if (n2 + 2 > this.arrUsed) {
            this.readTo(n2 + 2);
        }
        return (this.arr[n2] & 0xFF) << 8 | (this.arr[n2 + 1] & 0xFF);
    }
    
    @Override
    public String readString(final int n) throws IOException {
        return this.readString(n, false, false);
    }
    
    @Override
    public int readInt(final long n) throws IOException {
        final int n2 = (int)n;
        if (n2 + 4 > this.arrUsed) {
            this.readTo(n2 + 4);
        }
        return (this.arr[n2] & 0xFF) << 24 | (this.arr[n2 + 1] & 0xFF) << 16 | (this.arr[n2 + 2] & 0xFF) << 8 | (this.arr[n2 + 3] & 0xFF);
    }
    
    public byte[] buf() {
        return this.arr;
    }
    
    @Override
    public long readUnsignedInt(final long n) throws IOException {
        return (long)this.readInt(n) & 0xFFFFFFFFL;
    }
    
    private void readTo(final int n) throws IOException {
        final int n2 = (this.classfileLengthHint == -1) ? 2147483639 : this.classfileLengthHint;
        if (this.inflaterInputStream == null && this.randomAccessReader == null) {
            throw new IOException("Tried to read past end of fixed array buffer");
        }
        if (n > 2147483639 || n < 0 || this.arrUsed == n2) {
            throw new IOException("Hit 2GB limit while trying to grow buffer array");
        }
        int n3;
        long min;
        for (n3 = (int)Math.min(Math.max(n, (long)(this.arrUsed + 8184)), n2), min = this.arr.length; min < n3; min = Math.min(n3, min * 2L)) {}
        if (min > 2147483639L) {
            throw new IOException("Hit 2GB limit while trying to grow buffer array");
        }
        this.arr = Arrays.copyOf(this.arr, (int)Math.min(min, n2));
        final int n4 = this.arr.length - this.arrUsed;
        if (this.inflaterInputStream != null) {
            final int read = this.inflaterInputStream.read(this.arr, this.arrUsed, n4);
            if (read > 0) {
                this.arrUsed += read;
            }
        }
        else {
            final int read2 = this.randomAccessReader.read(this.arrUsed, this.arr, this.arrUsed, Math.min(n4, n2 - this.arrUsed));
            if (read2 > 0) {
                this.arrUsed += read2;
            }
        }
        if (this.arrUsed < n) {
            throw new IOException("Buffer underflow");
        }
    }
    
    public ClassfileReader(final Slice slice) throws IOException {
        this.classfileLengthHint = -1;
        this.classfileLengthHint = (int)slice.sliceLength;
        if (slice.isDeflatedZipEntry) {
            this.inflaterInputStream = slice.open();
            this.arr = new byte[16384];
            this.classfileLengthHint = (int)Math.min(slice.inflatedLengthHint, 2147483639L);
        }
        else if (slice instanceof ArraySlice) {
            final ArraySlice arraySlice = (ArraySlice)slice;
            if (arraySlice.sliceStartPos == 0L && arraySlice.sliceLength == arraySlice.arr.length) {
                this.arr = arraySlice.arr;
            }
            else {
                this.arr = Arrays.copyOfRange(arraySlice.arr, (int)arraySlice.sliceStartPos, (int)(arraySlice.sliceStartPos + arraySlice.sliceLength));
            }
            this.arrUsed = this.arr.length;
            this.classfileLengthHint = this.arr.length;
        }
        else {
            this.randomAccessReader = slice.randomAccessReader();
            this.arr = new byte[16384];
            this.classfileLengthHint = (int)Math.min(slice.sliceLength, 2147483639L);
        }
    }
    
    @Override
    public String readString(final long n, final int n2) throws IOException {
        return this.readString(n, n2, false, false);
    }
    
    public void bufferTo(final int n) throws IOException {
        if (n > this.arrUsed) {
            this.readTo(n);
        }
    }
    
    @Override
    public byte readByte(final long n) throws IOException {
        final int n2 = (int)n;
        if (n2 + 1 > this.arrUsed) {
            this.readTo(n2 + 1);
        }
        return this.arr[n2];
    }
    
    @Override
    public long readUnsignedInt() throws IOException {
        final long unsignedInt = this.readUnsignedInt(this.currIdx);
        this.currIdx += 4;
        return unsignedInt;
    }
    
    @Override
    public int readUnsignedByte(final long n) throws IOException {
        final int n2 = (int)n;
        if (n2 + 1 > this.arrUsed) {
            this.readTo(n2 + 1);
        }
        return this.arr[n2] & 0xFF;
    }
    
    @Override
    public short readShort() throws IOException {
        final short short1 = this.readShort(this.currIdx);
        this.currIdx += 2;
        return short1;
    }
    
    @Override
    public String readString(final long n, final int n2, final boolean b, final boolean b2) throws IOException {
        final int n3 = (int)n;
        if (n3 + n2 > this.arrUsed) {
            this.readTo(n3 + n2);
        }
        return StringUtils.readString(this.arr, n3, n2, b, b2);
    }
    
    @Override
    public void skip(final int n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("Tried to skip a negative number of bytes");
        }
        final int currIdx = this.currIdx;
        if (currIdx + n > this.arrUsed) {
            this.readTo(currIdx + n);
        }
        this.currIdx += n;
    }
    
    @Override
    public int read(final long n, final ByteBuffer byteBuffer, final int newPosition, final int a) throws IOException {
        if (a == 0) {
            return 0;
        }
        final int offset = (int)n;
        if (offset + a > this.arrUsed) {
            this.readTo(offset + a);
        }
        final int max = Math.max(Math.min(a, byteBuffer.capacity() - newPosition), 0);
        if (max == 0) {
            return -1;
        }
        try {
            byteBuffer.position(newPosition);
            byteBuffer.limit(newPosition + max);
            byteBuffer.put(this.arr, offset, max);
            return max;
        }
        catch (BufferUnderflowException | IndexOutOfBoundsException | ReadOnlyBufferException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
}
