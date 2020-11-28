// 
// Decompiled by Procyon v0.5.36
// 

package nonapi.io.github.classgraph.utils;

import java.lang.reflect.Field;
import io.github.classgraph.ClassGraphException;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;

public final class FileUtils
{
    private static /* synthetic */ Object theUnsafe;
    private static /* synthetic */ Method cleanMethod;
    private static /* synthetic */ Method attachmentMethod;
    
    public static String sanitizeEntryPath(final String str, final boolean b, final boolean b2) {
        if (str.isEmpty()) {
            return "";
        }
        boolean b3 = false;
        final int length = str.length();
        final char[] dst = new char[length];
        str.getChars(0, length, dst, 0);
        int n = -1;
        int n2 = '\0';
        for (int i = 0; i < length + 1; ++i) {
            final char c = (i == length) ? '\0' : dst[i];
            if (c == '/' || c == '!' || c == '\0') {
                final int n3 = i - (n + 1);
                if ((n3 == 0 && n2 == c) || (n3 == 1 && dst[i - 1] == '.') || (n3 == 2 && dst[i - 2] == '.' && dst[i - 1] == '.')) {
                    b3 = true;
                }
                n = i;
            }
            n2 = c;
        }
        final boolean b4 = length > 0 && dst[0] == '/';
        final StringBuilder sb = new StringBuilder(length + 16);
        if (b3) {
            final ArrayList<ArrayList<CharSequence>> list = new ArrayList<ArrayList<CharSequence>>();
            ArrayList<CharSequence> list2 = new ArrayList<CharSequence>();
            list.add(list2);
            int n4 = -1;
            for (int j = 0; j < length + 1; ++j) {
                final char c2 = (j == length) ? '\0' : dst[j];
                if (c2 == '/' || c2 == '!' || c2 == '\0') {
                    final int beginIndex = n4 + 1;
                    final int n5 = j - beginIndex;
                    if (n5 != 0) {
                        if (n5 != 1 || dst[beginIndex] != '.') {
                            if (n5 == 2 && dst[beginIndex] == '.' && dst[beginIndex + 1] == '.') {
                                if (!list2.isEmpty()) {
                                    list2.remove(list2.size() - 1);
                                }
                            }
                            else {
                                list2.add(str.subSequence(beginIndex, beginIndex + n5));
                            }
                        }
                    }
                    if (c2 == '!' && !list2.isEmpty()) {
                        list2 = new ArrayList<CharSequence>();
                        list.add(list2);
                    }
                    n4 = j;
                }
            }
            for (final List<CharSequence> list3 : list) {
                if (!list3.isEmpty()) {
                    if (sb.length() > 0) {
                        sb.append('!');
                    }
                    for (final CharSequence s : list3) {
                        sb.append('/');
                        sb.append(s);
                    }
                }
            }
            if (sb.length() == 0 && b4) {
                sb.append('/');
            }
        }
        else {
            sb.append(str);
        }
        int n6 = 0;
        if (b || !b4) {
            while (n6 < sb.length() && sb.charAt(n6) == '/') {
                ++n6;
            }
        }
        if (b2) {
            while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '/') {
                sb.setLength(sb.length() - 1);
            }
        }
        return sb.substring(n6);
    }
    
    public static void checkCanReadAndIsFile(final File obj) throws IOException {
        try {
            if (!obj.canRead()) {
                throw new FileNotFoundException("File does not exist or cannot be read: " + obj);
            }
        }
        catch (SecurityException obj2) {
            throw new FileNotFoundException("File " + obj + " cannot be accessed: " + obj2);
        }
        if (!obj.isFile()) {
            throw new IOException("Not a regular file: " + obj);
        }
    }
    
    private static boolean closeDirectByteBufferPrivileged(final ByteBuffer obj, final LogNode logNode) {
        try {
            if (FileUtils.cleanMethod == null) {
                if (logNode != null) {
                    logNode.log("Could not unmap ByteBuffer, cleanMethod == null");
                }
                return false;
            }
            if (VersionFinder.JAVA_MAJOR_VERSION < 9) {
                if (FileUtils.attachmentMethod == null) {
                    if (logNode != null) {
                        logNode.log("Could not unmap ByteBuffer, attachmentMethod == null");
                    }
                    return false;
                }
                if (FileUtils.attachmentMethod.invoke(obj, new Object[0]) != null) {
                    return false;
                }
                final Method method = obj.getClass().getMethod("cleaner", (Class<?>[])new Class[0]);
                if (method == null) {
                    if (logNode != null) {
                        logNode.log("Could not unmap ByteBuffer, cleaner == null");
                    }
                    return false;
                }
                try {
                    method.setAccessible(true);
                }
                catch (Exception ex2) {
                    if (logNode != null) {
                        logNode.log("Could not unmap ByteBuffer, cleaner.setAccessible(true) failed");
                    }
                    return false;
                }
                if (method.invoke(obj, new Object[0]) == null) {
                    if (logNode != null) {
                        logNode.log("Could not unmap ByteBuffer, cleanerResult == null");
                    }
                    return false;
                }
                try {
                    FileUtils.cleanMethod.invoke(method.invoke(obj, new Object[0]), new Object[0]);
                    return true;
                }
                catch (Exception obj2) {
                    if (logNode != null) {
                        logNode.log("Could not unmap ByteBuffer, cleanMethod.invoke(cleanerResult) failed: " + obj2);
                    }
                    return false;
                }
            }
            if (FileUtils.theUnsafe == null) {
                if (logNode != null) {
                    logNode.log("Could not unmap ByteBuffer, theUnsafe == null");
                }
                return false;
            }
            try {
                FileUtils.cleanMethod.invoke(FileUtils.theUnsafe, obj);
                return true;
            }
            catch (IllegalArgumentException ex3) {
                return false;
            }
        }
        catch (ReflectiveOperationException | SecurityException ex4) {
            final SecurityException ex;
            final SecurityException obj3 = ex;
            if (logNode != null) {
                logNode.log("Could not unmap ByteBuffer: " + obj3);
            }
            return false;
        }
    }
    
    public static boolean canReadAndIsFile(final File file) {
        try {
            if (!file.canRead()) {
                return false;
            }
        }
        catch (SecurityException ex) {
            return false;
        }
        return file.isFile();
    }
    
    public static void checkCanReadAndIsDir(final File obj) throws IOException {
        try {
            if (!obj.canRead()) {
                throw new FileNotFoundException("Directory does not exist or cannot be read: " + obj);
            }
        }
        catch (SecurityException obj2) {
            throw new FileNotFoundException("File " + obj + " cannot be accessed: " + obj2);
        }
        if (!obj.isDirectory()) {
            throw new IOException("Not a directory: " + obj);
        }
    }
    
    public static boolean canReadAndIsFile(final Path path) {
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                return false;
            }
        }
        catch (SecurityException ex) {
            return false;
        }
        return Files.isRegularFile(path, new LinkOption[0]);
    }
    
    public static boolean closeDirectByteBuffer(final ByteBuffer byteBuffer, final LogNode logNode) {
        return byteBuffer != null && byteBuffer.isDirect() && AccessController.doPrivileged((PrivilegedAction<Boolean>)new PrivilegedAction<Boolean>() {
            @Override
            public Boolean run() {
                return closeDirectByteBufferPrivileged(byteBuffer, logNode);
            }
        });
    }
    
    public static boolean canReadAndIsDir(final File file) {
        try {
            if (!file.canRead()) {
                return false;
            }
        }
        catch (SecurityException ex) {
            return false;
        }
        return file.isDirectory();
    }
    
    static {
        MAX_BUFFER_SIZE = 2147483639;
        String s = "";
        try {
            final Path absolutePath = Paths.get("", new String[0]).toAbsolutePath();
            s = absolutePath.toString();
            final Path normalize = absolutePath.normalize();
            s = normalize.toString();
            s = normalize.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
            s = FastPathResolver.resolve(s);
        }
        catch (IOException ex) {
            throw ClassGraphException.newClassGraphException("Could not resolve current directory: " + s, ex);
        }
        CURR_DIR_PATH = s;
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                lookupCleanMethodPrivileged();
                return null;
            }
        });
    }
    
    public static String getParentDirPath(final String s) {
        return getParentDirPath(s, '/');
    }
    
    public static boolean canReadAndIsDir(final Path path) {
        try {
            if (!Files.exists(path, new LinkOption[0])) {
                return false;
            }
        }
        catch (SecurityException ex) {
            return false;
        }
        return Files.isDirectory(path, new LinkOption[0]);
    }
    
    public static boolean isClassfile(final String s) {
        final int length = s.length();
        return length > 6 && s.regionMatches(true, length - 6, ".class", 0, 6);
    }
    
    public static String getParentDirPath(final String s, final char ch) {
        final int lastIndex = s.lastIndexOf(ch);
        if (lastIndex <= 0) {
            return "";
        }
        return s.substring(0, lastIndex);
    }
    
    public static boolean canRead(final File file) {
        try {
            return file.canRead();
        }
        catch (SecurityException ex) {
            return false;
        }
    }
    
    private FileUtils() {
    }
    
    private static void lookupCleanMethodPrivileged() {
        while (true) {
            if (VersionFinder.JAVA_MAJOR_VERSION < 9) {
                try {
                    (FileUtils.cleanMethod = Class.forName("sun.misc.Cleaner").getMethod("clean", (Class<?>[])new Class[0])).setAccessible(true);
                    (FileUtils.attachmentMethod = Class.forName("sun.nio.ch.DirectBuffer").getMethod("attachment", (Class<?>[])new Class[0])).setAccessible(true);
                    return;
                }
                catch (SecurityException ex) {
                    throw ClassGraphException.newClassGraphException("You need to grant classgraph RuntimePermission(\"accessClassInPackage.sun.misc\"), RuntimePermission(\"accessClassInPackage.sun.nio.ch\"), and ReflectPermission(\"suppressAccessChecks\")", ex);
                }
                catch (ReflectiveOperationException | LinkageError ex3) {
                    return;
                }
                try {
                    Class<?> clazz;
                    try {
                        clazz = Class.forName("sun.misc.Unsafe");
                    }
                    catch (ReflectiveOperationException | LinkageError ex4) {
                        clazz = Class.forName("jdk.internal.misc.Unsafe");
                    }
                    final Field declaredField = clazz.getDeclaredField("theUnsafe");
                    declaredField.setAccessible(true);
                    FileUtils.theUnsafe = declaredField.get(null);
                    (FileUtils.cleanMethod = clazz.getMethod("invokeCleaner", ByteBuffer.class)).setAccessible(true);
                }
                catch (SecurityException ex2) {
                    throw ClassGraphException.newClassGraphException("You need to grant classgraph RuntimePermission(\"accessClassInPackage.sun.misc\"), RuntimePermission(\"accessClassInPackage.jdk.internal.misc\") and ReflectPermission(\"suppressAccessChecks\")", ex2);
                }
                catch (ReflectiveOperationException ex5) {}
                catch (LinkageError linkageError) {}
                return;
            }
            continue;
        }
    }
    
    public static void checkCanReadAndIsFile(final Path obj) throws IOException {
        try {
            if (!Files.exists(obj, new LinkOption[0])) {
                throw new FileNotFoundException("Path does not exist or cannot be read: " + obj);
            }
        }
        catch (SecurityException obj2) {
            throw new FileNotFoundException("Path " + obj + " cannot be accessed: " + obj2);
        }
        if (!Files.isRegularFile(obj, new LinkOption[0])) {
            throw new IOException("Not a regular file: " + obj);
        }
    }
}
