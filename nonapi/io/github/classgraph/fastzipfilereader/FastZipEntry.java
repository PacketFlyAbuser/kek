// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fastzipfilereader;

import nonapi.io.github.classgraph.utils.VersionFinder;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import nonapi.io.github.classgraph.fileslice.Slice;

public class FastZipEntry implements Comparable<FastZipEntry>
{
    public final /* synthetic */ long uncompressedSize;
    public final /* synthetic */ String entryName;
    private final /* synthetic */ long locHeaderPos;
    final /* synthetic */ LogicalZipFile parentLogicalZipFile;
    final /* synthetic */ int version;
    private final /* synthetic */ int lastModifiedDateMSDOS;
    public final /* synthetic */ long compressedSize;
    private /* synthetic */ Slice slice;
    final /* synthetic */ boolean isDeflated;
    public final /* synthetic */ int fileAttributes;
    private /* synthetic */ long lastModifiedTimeMillis;
    public final /* synthetic */ String entryNameUnversioned;
    private final /* synthetic */ int lastModifiedTimeMSDOS;
    
    public long getLastModifiedTimeMillis() {
        if (this.lastModifiedTimeMillis == 0L && (this.lastModifiedDateMSDOS != 0 || this.lastModifiedTimeMSDOS != 0)) {
            final int second = (this.lastModifiedTimeMSDOS & 0x1F) * 2;
            final int minute = this.lastModifiedTimeMSDOS >> 5 & 0x3F;
            final int hourOfDay = this.lastModifiedTimeMSDOS >> 11;
            final int date = this.lastModifiedDateMSDOS & 0x1F;
            final int month = (this.lastModifiedDateMSDOS >> 5 & 0x7) - 1;
            final int year = (this.lastModifiedDateMSDOS >> 9) + 1980;
            final Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            instance.set(year, month, date, hourOfDay, minute, second);
            instance.set(14, 0);
            this.lastModifiedTimeMillis = instance.getTimeInMillis();
        }
        return this.lastModifiedTimeMillis;
    }
    
    @Override
    public int compareTo(final FastZipEntry fastZipEntry) {
        final int n = fastZipEntry.version - this.version;
        if (n != 0) {
            return n;
        }
        final int compareTo = this.entryNameUnversioned.compareTo(fastZipEntry.entryNameUnversioned);
        if (compareTo != 0) {
            return compareTo;
        }
        final int compareTo2 = this.entryName.compareTo(fastZipEntry.entryName);
        if (compareTo2 != 0) {
            return compareTo2;
        }
        final long n2 = this.locHeaderPos - fastZipEntry.locHeaderPos;
        return (n2 < 0L) ? -1 : (n2 > 0L);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FastZipEntry)) {
            return false;
        }
        final FastZipEntry fastZipEntry = (FastZipEntry)o;
        return this.parentLogicalZipFile.equals(fastZipEntry.parentLogicalZipFile) && this.compareTo(fastZipEntry) == 0;
    }
    
    public Slice getSlice() throws IOException {
        if (this.slice == null) {
            final RandomAccessReader randomAccessReader = this.parentLogicalZipFile.slice.randomAccessReader();
            if (randomAccessReader.readInt(this.locHeaderPos) != 67324752) {
                throw new IOException("Zip entry has bad LOC header: " + this.entryName);
            }
            final long n = this.locHeaderPos + 30L + randomAccessReader.readShort(this.locHeaderPos + 26L) + randomAccessReader.readShort(this.locHeaderPos + 28L);
            if (n > this.parentLogicalZipFile.slice.sliceLength) {
                throw new IOException("Unexpected EOF when trying to read zip entry data: " + this.entryName);
            }
            this.slice = this.parentLogicalZipFile.slice.slice(n, this.compressedSize, this.isDeflated, this.uncompressedSize);
        }
        return this.slice;
    }
    
    FastZipEntry(final LogicalZipFile parentLogicalZipFile, final long locHeaderPos, final String entryName, final boolean isDeflated, final long compressedSize, final long n, final long lastModifiedTimeMillis, final int lastModifiedTimeMSDOS, final int lastModifiedDateMSDOS, final int fileAttributes) {
        this.parentLogicalZipFile = parentLogicalZipFile;
        this.locHeaderPos = locHeaderPos;
        this.entryName = entryName;
        this.isDeflated = isDeflated;
        this.compressedSize = compressedSize;
        this.uncompressedSize = ((!isDeflated && n < 0L) ? compressedSize : n);
        this.lastModifiedTimeMillis = lastModifiedTimeMillis;
        this.lastModifiedTimeMSDOS = lastModifiedTimeMSDOS;
        this.lastModifiedDateMSDOS = lastModifiedDateMSDOS;
        this.fileAttributes = fileAttributes;
        int version = 8;
        String substring = entryName;
        if (entryName.startsWith("META-INF/versions/") && entryName.length() > "META-INF/versions/".length() + 1) {
            final int index = entryName.indexOf(47, "META-INF/versions/".length());
            if (index > 0) {
                final String substring2 = entryName.substring("META-INF/versions/".length(), index);
                int n2 = 0;
                if (substring2.length() < 6 && !substring2.isEmpty()) {
                    for (int i = 0; i < substring2.length(); ++i) {
                        final char char1 = substring2.charAt(i);
                        if (char1 < '0' || char1 > '9') {
                            n2 = 0;
                            break;
                        }
                        if (n2 == 0) {
                            n2 = char1 - '0';
                        }
                        else {
                            n2 = n2 * 10 + char1 - 48;
                        }
                    }
                }
                if (n2 != 0) {
                    version = n2;
                }
                if (version < 9 || version > VersionFinder.JAVA_MAJOR_VERSION) {
                    version = 8;
                }
                if (version > 8) {
                    substring = entryName.substring(index + 1);
                    if (substring.startsWith("META-INF/")) {
                        version = 8;
                        substring = entryName;
                    }
                }
            }
        }
        this.version = version;
        this.entryNameUnversioned = substring;
    }
    
    @Override
    public String toString() {
        return "jar:file:" + this.getPath();
    }
    
    @Override
    public int hashCode() {
        return this.parentLogicalZipFile.hashCode() ^ this.version ^ this.entryName.hashCode() ^ (int)this.locHeaderPos;
    }
    
    public String getPath() {
        return this.parentLogicalZipFile.getPath() + "!/" + this.entryName;
    }
}
