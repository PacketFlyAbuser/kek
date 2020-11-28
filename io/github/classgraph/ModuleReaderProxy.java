// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.io.IOException;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.util.List;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.io.Closeable;

public class ModuleReaderProxy implements Closeable
{
    private static /* synthetic */ Class<?> collectorClass;
    private final /* synthetic */ AutoCloseable moduleReader;
    private static /* synthetic */ Object collectorsToList;
    
    @Override
    public void close() {
        try {
            this.moduleReader.close();
        }
        catch (Exception ex) {}
    }
    
    public ByteBuffer read(final String s) throws SecurityException, OutOfMemoryError {
        return (ByteBuffer)this.openOrRead(s, false);
    }
    
    public InputStream open(final String s) throws SecurityException {
        return (InputStream)this.openOrRead(s, true);
    }
    
    public List<String> list() throws SecurityException {
        if (ModuleReaderProxy.collectorsToList == null) {
            throw new IllegalArgumentException("Could not call Collectors.toList()");
        }
        final Object invokeMethod = ReflectionUtils.invokeMethod(this.moduleReader, "list", true);
        if (invokeMethod == null) {
            throw new IllegalArgumentException("Could not call moduleReader.list()");
        }
        final Object invokeMethod2 = ReflectionUtils.invokeMethod(invokeMethod, "collect", ModuleReaderProxy.collectorClass, ModuleReaderProxy.collectorsToList, true);
        if (invokeMethod2 == null) {
            throw new IllegalArgumentException("Could not call moduleReader.list().collect(Collectors.toList())");
        }
        return (List<String>)invokeMethod2;
    }
    
    ModuleReaderProxy(final ModuleRef moduleRef) throws IOException {
        try {
            this.moduleReader = (AutoCloseable)ReflectionUtils.invokeMethod(moduleRef.getReference(), "open", true);
            if (this.moduleReader == null) {
                throw new IllegalArgumentException("moduleReference.open() should not return null");
            }
        }
        catch (SecurityException cause) {
            throw new IOException("Could not open module " + moduleRef.getName(), cause);
        }
    }
    
    private Object openOrRead(final String s, final boolean b) throws SecurityException {
        final String s2 = b ? "open" : "read";
        final Object invokeMethod = ReflectionUtils.invokeMethod(this.moduleReader, s2, String.class, s, true);
        if (invokeMethod == null) {
            throw new IllegalArgumentException("Got null result from moduleReader." + s2 + "(name)");
        }
        final Object invokeMethod2 = ReflectionUtils.invokeMethod(invokeMethod, "get", true);
        if (invokeMethod2 == null) {
            throw new IllegalArgumentException("Got null result from moduleReader." + s2 + "(name).get()");
        }
        return invokeMethod2;
    }
    
    static {
        ModuleReaderProxy.collectorClass = ReflectionUtils.classForNameOrNull("java.util.stream.Collector");
        final Class<?> classForNameOrNull = ReflectionUtils.classForNameOrNull("java.util.stream.Collectors");
        if (classForNameOrNull != null) {
            ModuleReaderProxy.collectorsToList = ReflectionUtils.invokeStaticMethod(classForNameOrNull, "toList", true);
        }
    }
    
    public void release(final ByteBuffer byteBuffer) {
        ReflectionUtils.invokeMethod(this.moduleReader, "release", ByteBuffer.class, byteBuffer, true);
    }
}
