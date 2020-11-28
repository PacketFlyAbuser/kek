// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice.reader;

import java.nio.ReadOnlyBufferException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.io.IOException;
import nonapi.io.github.classgraph.utils.StringUtils;

public class RandomAccessArrayReader implements RandomAccessReader
{
    private final /* synthetic */ int sliceStartPos;
    private final /* synthetic */ int sliceLength;
    private final /* synthetic */ byte[] arr;
    
    @Override
    public String readString(final long n, final int n2, final boolean b, final boolean b2) throws IOException {
        return StringUtils.readString(this.arr, this.sliceStartPos + (int)n, n2, b, b2);
    }
    
    @Override
    public int readUnsignedByte(final long n) throws IOException {
        return this.arr[this.sliceStartPos + (int)n] & 0xFF;
    }
    
    @Override
    public int readUnsignedShort(final long n) throws IOException {
        final int n2 = this.sliceStartPos + (int)n;
        return (this.arr[n2 + 1] & 0xFF) << 8 | (this.arr[n2] & 0xFF);
    }
    
    @Override
    public long readUnsignedInt(final long n) throws IOException {
        return (long)this.readInt(n) & 0xFFFFFFFFL;
    }
    
    public RandomAccessArrayReader(final byte[] arr, final int sliceStartPos, final int sliceLength) {
        this.arr = arr;
        this.sliceStartPos = sliceStartPos;
        this.sliceLength = sliceLength;
    }
    
    @Override
    public String readString(final long n, final int n2) throws IOException {
        return this.readString(n, n2, false, false);
    }
    
    @Override
    public byte readByte(final long n) throws IOException {
        return this.arr[this.sliceStartPos + (int)n];
    }
    
    @Override
    public long readLong(final long n) throws IOException {
        final int n2 = this.sliceStartPos + (int)n;
        return ((long)this.arr[n2 + 7] & 0xFFL) << 56 | ((long)this.arr[n2 + 6] & 0xFFL) << 48 | ((long)this.arr[n2 + 5] & 0xFFL) << 40 | ((long)this.arr[n2 + 4] & 0xFFL) << 32 | ((long)this.arr[n2 + 3] & 0xFFL) << 24 | ((long)this.arr[n2 + 2] & 0xFFL) << 16 | ((long)this.arr[n2 + 1] & 0xFFL) << 8 | ((long)this.arr[n2] & 0xFFL);
    }
    
    @Override
    public int readInt(final long n) throws IOException {
        final int n2 = this.sliceStartPos + (int)n;
        return (this.arr[n2 + 3] & 0xFF) << 24 | (this.arr[n2 + 2] & 0xFF) << 16 | (this.arr[n2 + 1] & 0xFF) << 8 | (this.arr[n2] & 0xFF);
    }
    
    @Override
    public int read(final long n, final byte[] array, final int n2, final int a) throws IOException {
        if (a == 0) {
            return 0;
        }
        if (n < 0L || a < 0 || a > this.sliceLength - n) {
            throw new IOException("Read index out of bounds");
        }
        try {
            final int max = Math.max(Math.min(a, array.length - n2), 0);
            if (max == 0) {
                return -1;
            }
            System.arraycopy(this.arr, (int)(this.sliceStartPos + n), array, n2, max);
            return max;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
    
    @Override
    public int read(final long n, final ByteBuffer byteBuffer, final int newPosition, final int a) throws IOException {
        if (a == 0) {
            return 0;
        }
        if (n < 0L || a < 0 || a > this.sliceLength - n) {
            throw new IOException("Read index out of bounds");
        }
        try {
            final int max = Math.max(Math.min(a, byteBuffer.capacity() - newPosition), 0);
            if (max == 0) {
                return -1;
            }
            try {
                final int offset = (int)(this.sliceStartPos + n);
                byteBuffer.position(newPosition);
                byteBuffer.limit(newPosition + max);
                byteBuffer.put(this.arr, offset, max);
                return max;
            }
            catch (IndexOutOfBoundsException ex) {
                throw new IOException("Read index out of bounds");
            }
        }
        catch (BufferUnderflowException ex2) {}
        catch (IndexOutOfBoundsException ex3) {}
        catch (ReadOnlyBufferException ex4) {}
    }
    
    @Override
    public short readShort(final long n) throws IOException {
        return (short)this.readUnsignedShort(n);
    }
}
