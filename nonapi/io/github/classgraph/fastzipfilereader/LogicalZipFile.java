// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.fastzipfilereader;

import java.util.Iterator;
import nonapi.io.github.classgraph.fileslice.reader.RandomAccessReader;
import java.util.HashMap;
import nonapi.io.github.classgraph.utils.Join;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.HashSet;
import nonapi.io.github.classgraph.utils.VersionFinder;
import java.io.EOFException;
import nonapi.io.github.classgraph.utils.FileUtils;
import java.util.ArrayList;
import nonapi.io.github.classgraph.fileslice.ArraySlice;
import java.util.AbstractMap;
import java.io.UnsupportedEncodingException;
import io.github.classgraph.ClassGraphException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import nonapi.io.github.classgraph.utils.LogNode;
import java.util.List;
import java.util.Set;

public class LogicalZipFile extends ZipFileSlice
{
    public /* synthetic */ String classPathManifestEntryValue;
    public /* synthetic */ String addOpensManifestEntryValue;
    public /* synthetic */ String addExportsManifestEntryValue;
    private /* synthetic */ boolean isMultiReleaseJar;
    private static final /* synthetic */ byte[] ADD_OPENS_KEY;
    /* synthetic */ Set<String> classpathRoots;
    public /* synthetic */ String automaticModuleNameManifestEntryValue;
    private static final /* synthetic */ byte[] SPRING_BOOT_CLASSES_KEY;
    private static final /* synthetic */ byte[] SPRING_BOOT_LIB_KEY;
    public /* synthetic */ boolean isJREJar;
    private static /* synthetic */ byte[] toLowerCase;
    private static final /* synthetic */ byte[] CLASS_PATH_KEY;
    private static final /* synthetic */ byte[] MULTI_RELEASE_KEY;
    private static final /* synthetic */ byte[] IMPLEMENTATION_TITLE_KEY;
    private static final /* synthetic */ byte[] SPECIFICATION_TITLE_KEY;
    private static final /* synthetic */ byte[] AUTOMATIC_MODULE_NAME_KEY;
    private static final /* synthetic */ byte[] BUNDLE_CLASSPATH_KEY;
    public /* synthetic */ List<FastZipEntry> entries;
    private static final /* synthetic */ byte[] ADD_EXPORTS_KEY;
    public /* synthetic */ String bundleClassPathManifestEntryValue;
    
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }
    
    LogicalZipFile(final ZipFileSlice zipFileSlice, final NestedJarHandler nestedJarHandler, final LogNode logNode) throws InterruptedException, IOException {
        super(zipFileSlice);
        this.classpathRoots = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        this.readCentralDirectory(nestedJarHandler, logNode);
    }
    
    private static Map.Entry<String, Integer> getManifestValue(final byte[] bytes, final int n) {
        int i;
        int length;
        for (i = n, length = bytes.length; i < length && bytes[i] == 32; ++i) {}
        final int offset = i;
        int n2 = 0;
        while (i < length && n2 == 0) {
            final byte b = bytes[i];
            if (b == 13 && i < length - 1 && bytes[i + 1] == 10) {
                if (i < length - 2 && bytes[i + 2] == 32) {
                    n2 = 1;
                    break;
                }
                break;
            }
            else if (b == 13 || b == 10) {
                if (i < length - 1 && bytes[i + 1] == 32) {
                    n2 = 1;
                    break;
                }
                break;
            }
            else {
                ++i;
            }
        }
        String string;
        if (n2 == 0) {
            string = new String(bytes, offset, i - offset, StandardCharsets.UTF_8);
        }
        else {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (i = offset; i < length; ++i) {
                final byte b2 = bytes[i];
                boolean b3;
                if (b2 == 13 && i < length - 1 && bytes[i + 1] == 10) {
                    i += 2;
                    b3 = true;
                }
                else if (b2 == 13 || b2 == 10) {
                    ++i;
                    b3 = true;
                }
                else {
                    byteArrayOutputStream.write(b2);
                    b3 = false;
                }
                if (b3 && i < length && bytes[i] != 32) {
                    break;
                }
            }
            try {
                string = byteArrayOutputStream.toString("UTF-8");
            }
            catch (UnsupportedEncodingException ex) {
                throw ClassGraphException.newClassGraphException("UTF-8 encoding unsupported", ex);
            }
        }
        return new AbstractMap.SimpleEntry<String, Integer>(string.endsWith(" ") ? string.trim() : string, i);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return this.getPath();
    }
    
    private static boolean keyMatchesAtPosition(final byte[] array, final byte[] array2, final int n) {
        if (n + array2.length + 1 > array.length || array[n + array2.length] != 58) {
            return false;
        }
        for (int i = 0; i < array2.length; ++i) {
            if (LogicalZipFile.toLowerCase[array[i + n]] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    private void readCentralDirectory(final NestedJarHandler nestedJarHandler, final LogNode logNode) throws IOException, InterruptedException {
        if (this.slice.sliceLength < 22L) {
            throw new IOException("Zipfile too short to have a central directory");
        }
        final RandomAccessReader randomAccessReader = this.slice.randomAccessReader();
        long lng = -1L;
        for (long n = this.slice.sliceLength - 22L; n >= this.slice.sliceLength - 22L - 32L && n >= 0L; --n) {
            if (randomAccessReader.readInt(n) == 101010256) {
                lng = n;
                break;
            }
        }
        if (lng < 0L && this.slice.sliceLength > 54L) {
            final int n2 = (int)Math.min(this.slice.sliceLength, 65536L);
            final byte[] array = new byte[n2];
            final long n3 = this.slice.sliceLength - n2;
            if (randomAccessReader.read(n3, array, 0, n2) < n2) {
                throw new IOException("Zipfile is truncated");
            }
            try (final ArraySlice arraySlice = new ArraySlice(array, false, 0L, nestedJarHandler)) {
                final RandomAccessReader randomAccessReader2 = arraySlice.randomAccessReader();
                for (long n4 = array.length - 22L; n4 >= 0L; --n4) {
                    if (randomAccessReader2.readInt(n4) == 101010256) {
                        lng = n4 + n3;
                        break;
                    }
                }
            }
        }
        if (lng < 0L) {
            throw new IOException("Jarfile central directory signature not found: " + this.getPath());
        }
        long n5 = randomAccessReader.readUnsignedShort(lng + 8L);
        if (randomAccessReader.readUnsignedShort(lng + 4L) > 0 || randomAccessReader.readUnsignedShort(lng + 6L) > 0 || n5 != randomAccessReader.readUnsignedShort(lng + 10L)) {
            throw new IOException("Multi-disk jarfiles not supported: " + this.getPath());
        }
        long unsignedInt = randomAccessReader.readUnsignedInt(lng + 12L);
        if (unsignedInt > lng) {
            throw new IOException("Central directory size out of range: " + unsignedInt + " vs. " + lng + ": " + this.getPath());
        }
        long unsignedInt2 = randomAccessReader.readUnsignedInt(lng + 16L);
        long n6 = lng - unsignedInt;
        final long n7 = lng - 20L;
        if (n7 >= 0L && randomAccessReader.readInt(n7) == 117853008) {
            if (randomAccessReader.readInt(n7 + 4L) > 0 || randomAccessReader.readInt(n7 + 16L) > 1) {
                throw new IOException("Multi-disk jarfiles not supported: " + this.getPath());
            }
            final long long1 = randomAccessReader.readLong(n7 + 8L);
            if (randomAccessReader.readInt(long1) != 101075792) {
                throw new IOException("Zip64 central directory at location " + long1 + " does not have Zip64 central directory header: " + this.getPath());
            }
            final long long2 = randomAccessReader.readLong(long1 + 24L);
            if (randomAccessReader.readInt(long1 + 16L) > 0 || randomAccessReader.readInt(long1 + 20L) > 0 || long2 != randomAccessReader.readLong(long1 + 32L)) {
                throw new IOException("Multi-disk jarfiles not supported: " + this.getPath());
            }
            if (n5 == 65535L) {
                n5 = long2;
            }
            else if (n5 != long2) {
                n5 = -1L;
            }
            final long long3 = randomAccessReader.readLong(long1 + 40L);
            if (unsignedInt == 4294967295L) {
                unsignedInt = long3;
            }
            else if (unsignedInt != long3) {
                throw new IOException("Mismatch in central directory size: " + unsignedInt + " vs. " + long3 + ": " + this.getPath());
            }
            n6 = long1 - unsignedInt;
            final long long4 = randomAccessReader.readLong(long1 + 48L);
            if (unsignedInt2 == 4294967295L) {
                unsignedInt2 = long4;
            }
            else if (unsignedInt2 != long4) {
                throw new IOException("Mismatch in central directory offset: " + unsignedInt2 + " vs. " + long4 + ": " + this.getPath());
            }
        }
        final long lng2 = n6 - unsignedInt2;
        if (lng2 < 0L) {
            throw new IOException("Local file header offset out of range: " + lng2 + ": " + this.getPath());
        }
        RandomAccessReader randomAccessReader3;
        if (unsignedInt > 2147483639L) {
            randomAccessReader3 = this.slice.slice(n6, unsignedInt, false, 0L).randomAccessReader();
        }
        else {
            final byte[] array2 = new byte[(int)unsignedInt];
            if (randomAccessReader.read(n6, array2, 0, (int)unsignedInt) < unsignedInt) {
                throw new IOException("Zipfile is truncated");
            }
            randomAccessReader3 = new ArraySlice(array2, false, 0L, nestedJarHandler).randomAccessReader();
        }
        if (n5 == -1L) {
            n5 = 0L;
            for (long n8 = 0L; n8 + 46L <= unsignedInt; n8 += 46 + randomAccessReader3.readUnsignedShort(n8 + 28L) + randomAccessReader3.readUnsignedShort(n8 + 30L) + randomAccessReader3.readUnsignedShort(n8 + 32L), ++n5) {
                final int int1 = randomAccessReader3.readInt(n8);
                if (int1 != 33639248) {
                    throw new IOException("Invalid central directory signature: 0x" + Integer.toString(int1, 16) + ": " + this.getPath());
                }
            }
        }
        if (n5 > 2147483639L) {
            throw new IOException("Too many zipfile entries: " + n5);
        }
        if (n5 > unsignedInt / 46L) {
            throw new IOException("Too many zipfile entries: " + n5 + " (expected a max of " + unsignedInt / 46L + " based on central directory size)");
        }
        this.entries = new ArrayList<FastZipEntry>((int)n5);
        FastZipEntry fastZipEntry = null;
        try {
            long lng3 = 0L;
            while (lng3 + 46L <= unsignedInt) {
                final int int2 = randomAccessReader3.readInt(lng3);
                if (int2 != 33639248) {
                    throw new IOException("Invalid central directory signature: 0x" + Integer.toString(int2, 16) + ": " + this.getPath());
                }
                final int unsignedShort = randomAccessReader3.readUnsignedShort(lng3 + 28L);
                final int unsignedShort2 = randomAccessReader3.readUnsignedShort(lng3 + 30L);
                final int n9 = 46 + unsignedShort + unsignedShort2 + randomAccessReader3.readUnsignedShort(lng3 + 32L);
                final long n10 = lng3 + 46L;
                final long n11 = n10 + unsignedShort;
                if (n11 > unsignedInt) {
                    if (logNode != null) {
                        logNode.log("Filename extends past end of entry -- skipping entry at offset " + lng3);
                        break;
                    }
                    break;
                }
                else {
                    final String string = randomAccessReader3.readString(n10, unsignedShort);
                    String s = FileUtils.sanitizeEntryPath(string, true, false);
                    if (!s.isEmpty()) {
                        if (!string.endsWith("/")) {
                            if ((randomAccessReader3.readUnsignedShort(lng3 + 8L) & 0x1) != 0x0) {
                                if (logNode != null) {
                                    logNode.log("Skipping encrypted zip entry: " + s);
                                }
                            }
                            else {
                                final int unsignedShort3 = randomAccessReader3.readUnsignedShort(lng3 + 10L);
                                if (unsignedShort3 != 0 && unsignedShort3 != 8) {
                                    if (logNode != null) {
                                        logNode.log("Skipping zip entry with invalid compression method " + unsignedShort3 + ": " + s);
                                    }
                                }
                                else {
                                    final boolean b = unsignedShort3 == 8;
                                    long unsignedInt3 = randomAccessReader3.readUnsignedInt(lng3 + 20L);
                                    long unsignedInt4 = randomAccessReader3.readUnsignedInt(lng3 + 24L);
                                    final int unsignedShort4 = randomAccessReader3.readUnsignedShort(lng3 + 40L);
                                    long lng4 = randomAccessReader3.readInt(lng3 + 42L);
                                    long n12 = 0L;
                                    if (unsignedShort2 > 0) {
                                        int n13 = 0;
                                        while (n13 + 4 < unsignedShort2) {
                                            final long n14 = n11 + n13;
                                            final int unsignedShort5 = randomAccessReader3.readUnsignedShort(n14);
                                            final int unsignedShort6 = randomAccessReader3.readUnsignedShort(n14 + 2L);
                                            if (n13 + 4 + unsignedShort6 > unsignedShort2) {
                                                if (logNode != null) {
                                                    logNode.log("Skipping zip entry with invalid extra field size: " + s);
                                                    break;
                                                }
                                                break;
                                            }
                                            else if (unsignedShort5 == 1 && unsignedShort6 >= 20) {
                                                final long long5 = randomAccessReader3.readLong(n14 + 4L + 0L);
                                                if (unsignedInt4 == 4294967295L) {
                                                    unsignedInt4 = long5;
                                                }
                                                else if (unsignedInt4 != long5) {
                                                    throw new IOException("Mismatch in uncompressed size: " + unsignedInt4 + " vs. " + long5 + ": " + s);
                                                }
                                                final long long6 = randomAccessReader3.readLong(n14 + 4L + 8L);
                                                if (unsignedInt3 == 4294967295L) {
                                                    unsignedInt3 = long6;
                                                }
                                                else if (unsignedInt3 != long6) {
                                                    throw new IOException("Mismatch in compressed size: " + unsignedInt3 + " vs. " + long6 + ": " + s);
                                                }
                                                if (unsignedShort6 >= 28) {
                                                    final long long7 = randomAccessReader3.readLong(n14 + 4L + 16L);
                                                    if (lng4 == 4294967295L) {
                                                        lng4 = long7;
                                                    }
                                                    else if (lng4 != long7) {
                                                        throw new IOException("Mismatch in entry pos: " + lng4 + " vs. " + long7 + ": " + s);
                                                    }
                                                    break;
                                                }
                                                break;
                                            }
                                            else {
                                                if (unsignedShort5 == 21589 && unsignedShort6 >= 5) {
                                                    if ((randomAccessReader3.readByte(n14 + 4L + 0L) & 0x1) == 0x1 && unsignedShort6 >= 13) {
                                                        n12 = randomAccessReader3.readLong(n14 + 4L + 1L) * 1000L;
                                                    }
                                                }
                                                else if (unsignedShort5 == 22613 && unsignedShort6 >= 20) {
                                                    n12 = randomAccessReader3.readLong(n14 + 4L + 8L) * 1000L;
                                                }
                                                else if (unsignedShort5 != 30805) {
                                                    if (unsignedShort5 == 28789) {
                                                        final byte byte1 = randomAccessReader3.readByte(n14 + 4L + 0L);
                                                        if (byte1 != 1) {
                                                            throw new IOException("Unknown Unicode entry name format " + byte1 + " in extra field: " + s);
                                                        }
                                                        if (unsignedShort6 > 9) {
                                                            try {
                                                                s = randomAccessReader3.readString(n14 + 9L, unsignedShort6 - 9);
                                                            }
                                                            catch (IllegalArgumentException ex) {
                                                                throw new IOException("Malformed extended Unicode entry name for entry: " + s);
                                                            }
                                                        }
                                                    }
                                                }
                                                n13 += 4 + unsignedShort6;
                                            }
                                        }
                                    }
                                    int unsignedShort7 = 0;
                                    int unsignedShort8 = 0;
                                    if (n12 == 0L) {
                                        unsignedShort7 = randomAccessReader3.readUnsignedShort(lng3 + 12L);
                                        unsignedShort8 = randomAccessReader3.readUnsignedShort(lng3 + 14L);
                                    }
                                    if (unsignedInt3 >= 0L) {
                                        if (lng4 >= 0L) {
                                            final long n15 = lng2 + lng4;
                                            if (n15 < 0L) {
                                                if (logNode != null) {
                                                    logNode.log("Skipping zip entry with invalid loc header position: " + s);
                                                }
                                            }
                                            else if (n15 + 4L >= this.slice.sliceLength) {
                                                if (logNode != null) {
                                                    logNode.log("Unexpected EOF when trying to read LOC header: " + s);
                                                }
                                            }
                                            else {
                                                final FastZipEntry fastZipEntry2 = new FastZipEntry(this, n15, s, b, unsignedInt3, unsignedInt4, n12, unsignedShort7, unsignedShort8, unsignedShort4);
                                                this.entries.add(fastZipEntry2);
                                                if (fastZipEntry2.entryName.equals("META-INF/MANIFEST.MF")) {
                                                    fastZipEntry = fastZipEntry2;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    lng3 += n9;
                }
            }
        }
        catch (EOFException | IndexOutOfBoundsException ex2) {
            if (logNode != null) {
                logNode.log("Reached premature EOF" + (this.entries.isEmpty() ? "" : (" after reading zip entry " + this.entries.get(this.entries.size() - 1))));
            }
        }
        if (fastZipEntry != null) {
            this.parseManifest(fastZipEntry, logNode);
        }
        if (this.isMultiReleaseJar) {
            if (VersionFinder.JAVA_MAJOR_VERSION < 9) {
                if (logNode != null) {
                    logNode.log("This is a multi-release jar, but JRE version " + VersionFinder.JAVA_MAJOR_VERSION + " does not support multi-release jars");
                }
            }
            else {
                if (logNode != null) {
                    final HashSet<Integer> c = new HashSet<Integer>();
                    for (final FastZipEntry fastZipEntry3 : this.entries) {
                        if (fastZipEntry3.version > 8) {
                            c.add(fastZipEntry3.version);
                        }
                    }
                    final ArrayList list = new ArrayList<Object>(c);
                    CollectionUtils.sortIfNotEmpty((List<Comparable>)list);
                    logNode.log("This is a multi-release jar, with versions: " + Join.join(", ", list));
                }
                CollectionUtils.sortIfNotEmpty(this.entries);
                final ArrayList<FastZipEntry> entries = new ArrayList<FastZipEntry>(this.entries.size());
                final HashMap<String, String> hashMap = (HashMap<String, String>)new HashMap<Object, String>();
                for (final FastZipEntry fastZipEntry4 : this.entries) {
                    if (!hashMap.containsKey(fastZipEntry4.entryNameUnversioned)) {
                        hashMap.put(fastZipEntry4.entryNameUnversioned, fastZipEntry4.entryName);
                        entries.add(fastZipEntry4);
                    }
                    else {
                        if (logNode == null) {
                            continue;
                        }
                        logNode.log(hashMap.get(fastZipEntry4.entryNameUnversioned) + " masks " + fastZipEntry4.entryName);
                    }
                }
                this.entries = entries;
            }
        }
    }
    
    static {
        MULTI_RELEASE_PATH_PREFIX = "META-INF/versions/";
        MANIFEST_PATH = "META-INF/MANIFEST.MF";
        META_INF_PATH_PREFIX = "META-INF/";
        IMPLEMENTATION_TITLE_KEY = manifestKeyToBytes("Implementation-Title");
        SPECIFICATION_TITLE_KEY = manifestKeyToBytes("Specification-Title");
        CLASS_PATH_KEY = manifestKeyToBytes("Class-Path");
        BUNDLE_CLASSPATH_KEY = manifestKeyToBytes("Bundle-ClassPath");
        SPRING_BOOT_CLASSES_KEY = manifestKeyToBytes("Spring-Boot-Classes");
        SPRING_BOOT_LIB_KEY = manifestKeyToBytes("Spring-Boot-Lib");
        MULTI_RELEASE_KEY = manifestKeyToBytes("Multi-Release");
        ADD_EXPORTS_KEY = manifestKeyToBytes("Add-Exports");
        ADD_OPENS_KEY = manifestKeyToBytes("Add-Opens");
        AUTOMATIC_MODULE_NAME_KEY = manifestKeyToBytes("Automatic-Module-Name");
        LogicalZipFile.toLowerCase = new byte[256];
        for (int i = 32; i < 127; ++i) {
            LogicalZipFile.toLowerCase[i] = (byte)Character.toLowerCase((char)i);
        }
    }
    
    private static byte[] manifestKeyToBytes(final String s) {
        final byte[] array = new byte[s.length()];
        for (int i = 0; i < s.length(); ++i) {
            array[i] = (byte)Character.toLowerCase(s.charAt(i));
        }
        return array;
    }
    
    private void parseManifest(final FastZipEntry fastZipEntry, final LogNode logNode) throws IOException, InterruptedException {
        final byte[] load = fastZipEntry.getSlice().load();
        int i = 0;
        while (i < load.length) {
            boolean b = false;
            if (load[i] == 10 || load[i] == 13) {
                b = true;
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.IMPLEMENTATION_TITLE_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue = getManifestValue(load, i + LogicalZipFile.IMPLEMENTATION_TITLE_KEY.length + 1);
                if (manifestValue.getKey().equalsIgnoreCase("Java Runtime Environment")) {
                    this.isJREJar = true;
                }
                i = manifestValue.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.SPECIFICATION_TITLE_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue2 = getManifestValue(load, i + LogicalZipFile.SPECIFICATION_TITLE_KEY.length + 1);
                if (manifestValue2.getKey().equalsIgnoreCase("Java Platform API Specification")) {
                    this.isJREJar = true;
                }
                i = manifestValue2.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.CLASS_PATH_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue3 = getManifestValue(load, i + LogicalZipFile.CLASS_PATH_KEY.length + 1);
                this.classPathManifestEntryValue = manifestValue3.getKey();
                if (logNode != null) {
                    logNode.log("Found Class-Path entry in manifest file: " + this.classPathManifestEntryValue);
                }
                i = manifestValue3.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.BUNDLE_CLASSPATH_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue4 = getManifestValue(load, i + LogicalZipFile.BUNDLE_CLASSPATH_KEY.length + 1);
                this.bundleClassPathManifestEntryValue = manifestValue4.getKey();
                if (logNode != null) {
                    logNode.log("Found Bundle-ClassPath entry in manifest file: " + this.bundleClassPathManifestEntryValue);
                }
                i = manifestValue4.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.SPRING_BOOT_CLASSES_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue5 = getManifestValue(load, i + LogicalZipFile.SPRING_BOOT_CLASSES_KEY.length + 1);
                final String str = manifestValue5.getKey();
                if (!str.equals("BOOT-INF/classes") && !str.equals("BOOT-INF/classes/") && !str.equals("WEB-INF/classes") && !str.equals("WEB-INF/classes/")) {
                    throw new IOException("Spring boot classes are at \"" + str + "\" rather than the standard location \"BOOT-INF/classes/\" or \"WEB-INF/classes/\" -- please report this at https://github.com/classgraph/classgraph/issues");
                }
                i = manifestValue5.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.SPRING_BOOT_LIB_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue6 = getManifestValue(load, i + LogicalZipFile.SPRING_BOOT_LIB_KEY.length + 1);
                final String str2 = manifestValue6.getKey();
                if (!str2.equals("BOOT-INF/lib") && !str2.equals("BOOT-INF/lib/") && !str2.equals("WEB-INF/lib") && !str2.equals("WEB-INF/lib/")) {
                    throw new IOException("Spring boot lib jars are at \"" + str2 + "\" rather than the standard location \"BOOT-INF/lib/\" or \"WEB-INF/lib/\" -- please report this at https://github.com/classgraph/classgraph/issues");
                }
                i = manifestValue6.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.MULTI_RELEASE_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue7 = getManifestValue(load, i + LogicalZipFile.MULTI_RELEASE_KEY.length + 1);
                if (manifestValue7.getKey().equalsIgnoreCase("true")) {
                    this.isMultiReleaseJar = true;
                }
                i = manifestValue7.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.ADD_EXPORTS_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue8 = getManifestValue(load, i + LogicalZipFile.ADD_EXPORTS_KEY.length + 1);
                this.addExportsManifestEntryValue = manifestValue8.getKey();
                if (logNode != null) {
                    logNode.log("Found Add-Exports entry in manifest file: " + this.addExportsManifestEntryValue);
                }
                i = manifestValue8.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.ADD_OPENS_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue9 = getManifestValue(load, i + LogicalZipFile.ADD_OPENS_KEY.length + 1);
                this.addExportsManifestEntryValue = manifestValue9.getKey();
                if (logNode != null) {
                    logNode.log("Found Add-Opens entry in manifest file: " + this.addOpensManifestEntryValue);
                }
                i = manifestValue9.getValue();
            }
            else if (keyMatchesAtPosition(load, LogicalZipFile.AUTOMATIC_MODULE_NAME_KEY, i)) {
                final Map.Entry<String, Integer> manifestValue10 = getManifestValue(load, i + LogicalZipFile.AUTOMATIC_MODULE_NAME_KEY.length + 1);
                this.automaticModuleNameManifestEntryValue = manifestValue10.getKey();
                if (logNode != null) {
                    logNode.log("Found Automatic-Module-Name entry in manifest file: " + this.automaticModuleNameManifestEntryValue);
                }
                i = manifestValue10.getValue();
            }
            else {
                b = true;
            }
            if (b) {
                while (i < load.length - 2) {
                    if (load[i] == 13 && load[i + 1] == 10 && load[i + 2] != 32) {
                        i += 2;
                        break;
                    }
                    if ((load[i] == 13 || load[i] == 10) && load[i + 1] != 32) {
                        ++i;
                        break;
                    }
                    ++i;
                }
                if (i >= load.length - 2) {
                    break;
                }
                continue;
            }
        }
    }
}
