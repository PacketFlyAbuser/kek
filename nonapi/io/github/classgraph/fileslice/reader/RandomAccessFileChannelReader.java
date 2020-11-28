// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice.reader;

import nonapi.io.github.classgraph.utils.StringUtils;
import java.nio.BufferUnderflowException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessFileChannelReader implements RandomAccessReader
{
    private final /* synthetic */ long sliceLength;
    private final /* synthetic */ long sliceStartPos;
    private final /* synthetic */ FileChannel fileChannel;
    private /* synthetic */ byte[] utf8Bytes;
    private /* synthetic */ ByteBuffer reusableByteBuffer;
    private final /* synthetic */ ByteBuffer scratchByteBuf;
    private final /* synthetic */ byte[] scratchArr;
    
    @Override
    public int read(final long n, final ByteBuffer byteBuffer, final int newPosition, final int n2) throws IOException {
        if (n2 == 0) {
            return 0;
        }
        try {
            if (n < 0L || n2 < 0 || n2 > this.sliceLength - n) {
                throw new IOException("Read index out of bounds");
            }
            final long n3 = this.sliceStartPos + n;
            byteBuffer.position(newPosition);
            byteBuffer.limit(newPosition + n2);
            final int read = this.fileChannel.read(byteBuffer, n3);
            return (read == 0) ? -1 : read;
        }
        catch (BufferUnderflowException | IndexOutOfBoundsException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
    
    @Override
    public long readLong(final long n) throws IOException {
        if (this.read(n, this.scratchByteBuf, 0, 8) < 8) {
            throw new IOException("Premature EOF");
        }
        return ((long)this.scratchArr[7] & 0xFFL) << 56 | ((long)this.scratchArr[6] & 0xFFL) << 48 | ((long)this.scratchArr[5] & 0xFFL) << 40 | ((long)this.scratchArr[4] & 0xFFL) << 32 | ((long)this.scratchArr[3] & 0xFFL) << 24 | ((long)this.scratchArr[2] & 0xFFL) << 16 | ((long)this.scratchArr[1] & 0xFFL) << 8 | ((long)this.scratchArr[0] & 0xFFL);
    }
    
    @Override
    public int readUnsignedByte(final long n) throws IOException {
        if (this.read(n, this.scratchByteBuf, 0, 1) < 1) {
            throw new IOException("Premature EOF");
        }
        return this.scratchArr[0] & 0xFF;
    }
    
    @Override
    public byte readByte(final long n) throws IOException {
        if (this.read(n, this.scratchByteBuf, 0, 1) < 1) {
            throw new IOException("Premature EOF");
        }
        return this.scratchArr[0];
    }
    
    @Override
    public int readUnsignedShort(final long n) throws IOException {
        if (this.read(n, this.scratchByteBuf, 0, 2) < 2) {
            throw new IOException("Premature EOF");
        }
        return (this.scratchArr[1] & 0xFF) << 8 | (this.scratchArr[0] & 0xFF);
    }
    
    @Override
    public short readShort(final long n) throws IOException {
        return (short)this.readUnsignedShort(n);
    }
    
    public RandomAccessFileChannelReader(final FileChannel fileChannel, final long sliceStartPos, final long sliceLength) {
        this.scratchArr = new byte[8];
        this.scratchByteBuf = ByteBuffer.wrap(this.scratchArr);
        this.fileChannel = fileChannel;
        this.sliceStartPos = sliceStartPos;
        this.sliceLength = sliceLength;
    }
    
    @Override
    public String readString(final long n, final int n2, final boolean b, final boolean b2) throws IOException {
        if (this.utf8Bytes == null || this.utf8Bytes.length < n2) {
            this.utf8Bytes = new byte[n2];
        }
        if (this.read(n, this.utf8Bytes, 0, n2) < n2) {
            throw new IOException("Premature EOF");
        }
        return StringUtils.readString(this.utf8Bytes, 0, n2, b, b2);
    }
    
    @Override
    public int readInt(final long n) throws IOException {
        if (this.read(n, this.scratchByteBuf, 0, 4) < 4) {
            throw new IOException("Premature EOF");
        }
        return (this.scratchArr[3] & 0xFF) << 24 | (this.scratchArr[2] & 0xFF) << 16 | (this.scratchArr[1] & 0xFF) << 8 | (this.scratchArr[0] & 0xFF);
    }
    
    @Override
    public String readString(final long n, final int n2) throws IOException {
        return this.readString(n, n2, false, false);
    }
    
    @Override
    public int read(final long n, final byte[] array, final int n2, final int n3) throws IOException {
        if (n3 == 0) {
            return 0;
        }
        try {
            if (n < 0L || n3 < 0 || n3 > this.sliceLength - n) {
                throw new IOException("Read index out of bounds");
            }
            if (this.reusableByteBuffer == null || this.reusableByteBuffer.array() != array) {
                this.reusableByteBuffer = ByteBuffer.wrap(array);
            }
            return this.read(n, this.reusableByteBuffer, n2, n3);
        }
        catch (BufferUnderflowException | IndexOutOfBoundsException ex) {
            throw new IOException("Read index out of bounds");
        }
    }
    
    @Override
    public long readUnsignedInt(final long n) throws IOException {
        return (long)this.readInt(n) & 0xFFFFFFFFL;
    }
}
