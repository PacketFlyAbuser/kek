// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice.reader;

import nonapi.io.github.classgraph.utils.StringUtils;
import java.nio.ReadOnlyBufferException;
import java.nio.BufferUnderflowException;
import java.nio.ByteOrder;
import java.io.IOException;
import java.nio.ByteBuffer;

public class RandomAccessByteBufferReader implements RandomAccessReader
{
    private final /* synthetic */ int sliceLength;
    private final /* synthetic */ int sliceStartPos;
    private final /* synthetic */ ByteBuffer byteBuffer;
    
    @Override
    public int readUnsignedShort(final long n) throws IOException {
        return this.byteBuffer.getShort((int)(this.sliceStartPos + n)) & 0xFF;
    }
    
    @Override
    public int readUnsignedByte(final long n) throws IOException {
        return this.byteBuffer.get((int)(this.sliceStartPos + n)) & 0xFF;
    }
    
    @Override
    public int readInt(final long n) throws IOException {
        return this.byteBuffer.getInt((int)(this.sliceStartPos + n));
    }
    
    @Override
    public String readString(final long n, final int n2) throws IOException {
        return this.readString(n, n2, false, false);
    }
    
    @Override
    public long readUnsignedInt(final long n) throws IOException {
        return (long)this.readInt(n) & 0xFFFFFFFFL;
    }
    
    public RandomAccessByteBufferReader(final ByteBuffer byteBuffer, final long n, final long n2) {
        this.byteBuffer = byteBuffer.duplicate();
        this.byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.sliceStartPos = (int)n;
        this.sliceLength = (int)n2;
        this.byteBuffer.position(this.sliceStartPos);
        this.byteBuffer.limit(this.sliceStartPos + this.sliceLength);
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
                this.byteBuffer.position((int)(this.sliceStartPos + n));
                byteBuffer.position(newPosition);
                byteBuffer.limit(newPosition + max);
                byteBuffer.put(this.byteBuffer);
                this.byteBuffer.limit(this.sliceStartPos + this.sliceLength);
                this.byteBuffer.position(this.sliceStartPos);
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
    public int read(final long n, final byte[] dst, final int offset, final int a) throws IOException {
        if (a == 0) {
            return 0;
        }
        if (n < 0L || a < 0 || a > this.sliceLength - n) {
            throw new IOException("Read index out of bounds");
        }
        try {
            final int max = Math.max(Math.min(a, dst.length - offset), 0);
            if (max == 0) {
                return -1;
            }
            this.byteBuffer.position(this.sliceStartPos + (int)n);
            this.byteBuffer.get(dst, offset, max);
            this.byteBuffer.position(this.sliceStartPos);
            return max;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
    
    @Override
    public long readLong(final long n) throws IOException {
        return this.byteBuffer.getLong((int)(this.sliceStartPos + n));
    }
    
    @Override
    public short readShort(final long n) throws IOException {
        return (short)this.readUnsignedShort(n);
    }
    
    @Override
    public String readString(final long n, final int n2, final boolean b, final boolean b2) throws IOException {
        final int n3 = (int)(this.sliceStartPos + n);
        final byte[] array = new byte[n2];
        if (this.read(n, array, 0, n2) < n2) {
            throw new IOException("Premature EOF while reading string");
        }
        return StringUtils.readString(array, n3, n2, b, b2);
    }
    
    @Override
    public byte readByte(final long n) throws IOException {
        return this.byteBuffer.get((int)(this.sliceStartPos + n));
    }
}
