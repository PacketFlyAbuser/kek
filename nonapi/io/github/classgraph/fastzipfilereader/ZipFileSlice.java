// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fastzipfilereader;

import nonapi.io.github.classgraph.scanspec.AcceptReject;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import nonapi.io.github.classgraph.fileslice.Slice;

public class ZipFileSlice
{
    private final /* synthetic */ ZipFileSlice parentZipFileSlice;
    public /* synthetic */ Slice slice;
    protected final /* synthetic */ PhysicalZipFile physicalZipFile;
    private final /* synthetic */ String pathWithinParentZipFileSlice;
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ZipFileSlice)) {
            return false;
        }
        final ZipFileSlice zipFileSlice = (ZipFileSlice)o;
        return Objects.equals(this.physicalZipFile, zipFileSlice.physicalZipFile) && Objects.equals(this.slice, zipFileSlice.slice) && Objects.equals(this.pathWithinParentZipFileSlice, zipFileSlice.pathWithinParentZipFileSlice);
    }
    
    public String getPath() {
        final StringBuilder sb = new StringBuilder();
        this.appendPath(sb);
        return sb.toString();
    }
    
    ZipFileSlice(final FastZipEntry fastZipEntry) throws IOException, InterruptedException {
        this.parentZipFileSlice = fastZipEntry.parentLogicalZipFile;
        this.physicalZipFile = fastZipEntry.parentLogicalZipFile.physicalZipFile;
        this.slice = fastZipEntry.getSlice();
        this.pathWithinParentZipFileSlice = fastZipEntry.entryName;
    }
    
    ZipFileSlice(final ZipFileSlice zipFileSlice) {
        this.parentZipFileSlice = zipFileSlice.parentZipFileSlice;
        this.physicalZipFile = zipFileSlice.physicalZipFile;
        this.slice = zipFileSlice.slice;
        this.pathWithinParentZipFileSlice = zipFileSlice.pathWithinParentZipFileSlice;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.physicalZipFile, this.slice, this.pathWithinParentZipFileSlice);
    }
    
    public File getPhysicalFile() {
        final Path path = this.physicalZipFile.getPath();
        if (path != null) {
            try {
                return path.toFile();
            }
            catch (UnsupportedOperationException ex) {
                return null;
            }
        }
        return this.physicalZipFile.getFile();
    }
    
    public String getPathWithinParentZipFileSlice() {
        return this.pathWithinParentZipFileSlice;
    }
    
    private void appendPath(final StringBuilder sb) {
        if (this.parentZipFileSlice != null) {
            this.parentZipFileSlice.appendPath(sb);
            if (sb.length() > 0) {
                sb.append("!/");
            }
        }
        sb.append(this.pathWithinParentZipFileSlice);
    }
    
    public boolean isAcceptedAndNotRejected(final AcceptReject.AcceptRejectLeafname acceptRejectLeafname) {
        return acceptRejectLeafname.isAcceptedAndNotRejected(this.pathWithinParentZipFileSlice) && (this.parentZipFileSlice == null || this.parentZipFileSlice.isAcceptedAndNotRejected(acceptRejectLeafname));
    }
    
    ZipFileSlice(final PhysicalZipFile physicalZipFile) {
        this.parentZipFileSlice = null;
        this.physicalZipFile = physicalZipFile;
        this.slice = physicalZipFile.slice;
        this.pathWithinParentZipFileSlice = physicalZipFile.getPathStr();
    }
    
    public ZipFileSlice getParentZipFileSlice() {
        return this.parentZipFileSlice;
    }
    
    ZipFileSlice(final PhysicalZipFile physicalZipFile, final FastZipEntry fastZipEntry) {
        this.parentZipFileSlice = fastZipEntry.parentLogicalZipFile;
        this.physicalZipFile = physicalZipFile;
        this.slice = physicalZipFile.slice;
        this.pathWithinParentZipFileSlice = fastZipEntry.entryName;
    }
    
    @Override
    public String toString() {
        final String path = this.getPath();
        String str = (this.physicalZipFile.getPath() == null) ? null : this.physicalZipFile.getPath().toString();
        if (str == null) {
            str = ((this.physicalZipFile.getFile() == null) ? null : this.physicalZipFile.getFile().toString());
        }
        return "[" + ((str != null && !str.equals(path)) ? (path + " -> " + str) : path) + " ; byte range: " + this.slice.sliceStartPos + ".." + (this.slice.sliceStartPos + this.slice.sliceLength) + " / " + this.physicalZipFile.length() + "]";
    }
}
