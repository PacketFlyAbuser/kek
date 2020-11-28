// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fileslice.reader;

import java.io.IOException;

public interface SequentialReader
{
    short readShort() throws IOException;
    
    int readUnsignedByte() throws IOException;
    
    int readInt() throws IOException;
    
    int readUnsignedShort() throws IOException;
    
    long readUnsignedInt() throws IOException;
    
    String readString(final int p0) throws IOException;
    
    long readLong() throws IOException;
    
    void skip(final int p0) throws IOException;
    
    String readString(final int p0, final boolean p1, final boolean p2) throws IOException;
    
    byte readByte() throws IOException;
}
