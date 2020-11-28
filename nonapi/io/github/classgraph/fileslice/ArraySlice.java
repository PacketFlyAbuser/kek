// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice;

import nonapi.io.github.classgraph.fileslice.reader.RandomAccessArrayReader;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import nonapi.io.github.classgraph.fastzipfilereader.NestedJarHandler;

public class ArraySlice extends Slice
{
    public /* synthetic */ byte[] arr;
    
    @Override
    public byte[] load() throws IOException {
        if (this.isDeflatedZipEntry) {
            try (final InputStream open = this.open()) {
                return NestedJarHandler.readAllBytesAsArray(open, this.inflatedLengthHint);
            }
        }
        if (this.sliceStartPos == 0L && this.sliceLength == this.arr.length) {
            return this.arr;
        }
        return Arrays.copyOfRange(this.arr, (int)this.sliceStartPos, (int)(this.sliceStartPos + this.sliceLength));
    }
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    public ArraySlice(final byte[] arr, final boolean b, final long n, final NestedJarHandler nestedJarHandler) {
        super(arr.length, b, n, nestedJarHandler);
        this.arr = arr;
    }
    
    private ArraySlice(final ArraySlice arraySlice, final long n, final long n2, final boolean b, final long n3, final NestedJarHandler nestedJarHandler) {
        super(arraySlice, n, n2, b, n3, nestedJarHandler);
        this.arr = arraySlice.arr;
    }
    
    @Override
    public RandomAccessReader randomAccessReader() {
        return new RandomAccessArrayReader(this.arr, (int)this.sliceStartPos, (int)this.sliceLength);
    }
    
    @Override
    public Slice slice(final long n, final long n2, final boolean b, final long n3) {
        if (this.isDeflatedZipEntry) {
            throw new IllegalArgumentException("Cannot slice a deflated zip entry");
        }
        return new ArraySlice(this, n, n2, b, n3, this.nestedJarHandler);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
