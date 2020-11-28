// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import java.nio.ByteBuffer;
import java.io.InputStream;
import java.util.ListIterator;
import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Comparator;
import java.util.AbstractMap;
import java.net.URL;
import java.util.ArrayList;
import java.net.URI;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;

public class ResourceList extends PotentiallyUnmodifiableList<Resource> implements AutoCloseable
{
    private static final /* synthetic */ ResourceFilter CLASSFILE_FILTER;
    static final /* synthetic */ ResourceList EMPTY_LIST;
    
    @Deprecated
    public void forEachInputStream(final InputStreamConsumer inputStreamConsumer, final boolean b) {
        for (final Resource obj : this) {
            try {
                inputStreamConsumer.accept(obj, obj.open());
            }
            catch (IOException cause) {
                if (!b) {
                    throw new IllegalArgumentException("Could not load resource " + obj, cause);
                }
                continue;
            }
            finally {
                obj.close();
            }
        }
    }
    
    public static ResourceList emptyList() {
        return ResourceList.EMPTY_LIST;
    }
    
    public ResourceList(final Collection<Resource> collection) {
        super(collection);
    }
    
    public Map<String, ResourceList> asMap() {
        final HashMap<String, ResourceList> hashMap = (HashMap<String, ResourceList>)new HashMap<Object, ResourceList>();
        for (final Resource resource : this) {
            final String path = resource.getPath();
            ResourceList list = hashMap.get(path);
            if (list == null) {
                list = new ResourceList(1);
                hashMap.put(path, list);
            }
            list.add(resource);
        }
        return hashMap;
    }
    
    public List<URI> getURIs() {
        final ArrayList<URI> list = new ArrayList<URI>(this.size());
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getURI());
        }
        return list;
    }
    
    public List<URL> getURLs() {
        final ArrayList<URL> list = new ArrayList<URL>(this.size());
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getURL());
        }
        return list;
    }
    
    public ResourceList(final int n) {
        super(n);
    }
    
    public ResourceList() {
    }
    
    static {
        (EMPTY_LIST = new ResourceList()).makeUnmodifiable();
        CLASSFILE_FILTER = new ResourceFilter() {
            @Override
            public boolean accept(final Resource resource) {
                final String path = resource.getPath();
                if (!path.endsWith(".class") || path.length() < 7) {
                    return false;
                }
                final char char1 = path.charAt(path.length() - 7);
                return char1 != '/' && char1 != '.';
            }
        };
    }
    
    public void forEachInputStreamThrowingIOException(final InputStreamConsumerThrowsIOException ex) throws IOException {
        for (final Resource resource : this) {
            try {
                ex.accept(resource, resource.open());
            }
            finally {
                resource.close();
            }
        }
    }
    
    public void forEachInputStreamIgnoringIOException(final InputStreamConsumer inputStreamConsumer) {
        for (final Resource resource : this) {
            try {
                inputStreamConsumer.accept(resource, resource.open());
            }
            catch (IOException ex) {}
            finally {
                resource.close();
            }
        }
    }
    
    public List<String> getPaths() {
        final ArrayList<String> list = new ArrayList<String>(this.size());
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getPath());
        }
        return list;
    }
    
    public ResourceList filter(final ResourceFilter resourceFilter) {
        final ResourceList list = new ResourceList();
        for (final Resource resource : this) {
            if (resourceFilter.accept(resource)) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public void forEachByteArrayThrowingIOException(final ByteArrayConsumerThrowsIOException ex) throws IOException {
        for (final Resource resource : this) {
            try {
                ex.accept(resource, resource.load());
            }
            finally {
                resource.close();
            }
        }
    }
    
    public void forEachByteBufferThrowingIOException(final ByteBufferConsumerThrowsIOException ex) throws IOException {
        for (final Resource resource : this) {
            try {
                ex.accept(resource, resource.read());
            }
            finally {
                resource.close();
            }
        }
    }
    
    @Deprecated
    public void forEachByteBuffer(final ByteBufferConsumer byteBufferConsumer, final boolean b) {
        for (final Resource obj : this) {
            try {
                byteBufferConsumer.accept(obj, obj.read());
            }
            catch (IOException cause) {
                if (!b) {
                    throw new IllegalArgumentException("Could not load resource " + obj, cause);
                }
                continue;
            }
            finally {
                obj.close();
            }
        }
    }
    
    @Deprecated
    public void forEachInputStream(final InputStreamConsumer inputStreamConsumer) {
        this.forEachInputStream(inputStreamConsumer, false);
    }
    
    public ResourceList nonClassFilesOnly() {
        return this.filter(new ResourceFilter() {
            @Override
            public boolean accept(final Resource resource) {
                return !ResourceList.CLASSFILE_FILTER.accept(resource);
            }
        });
    }
    
    @Deprecated
    public void forEachByteArray(final ByteArrayConsumer byteArrayConsumer, final boolean b) {
        for (final Resource obj : this) {
            try {
                byteArrayConsumer.accept(obj, obj.load());
            }
            catch (IOException cause) {
                if (!b) {
                    throw new IllegalArgumentException("Could not load resource " + obj, cause);
                }
                continue;
            }
            finally {
                obj.close();
            }
        }
    }
    
    public List<Map.Entry<String, ResourceList>> findDuplicatePaths() {
        final ArrayList<Object> list = new ArrayList<Object>();
        for (final Map.Entry<String, ResourceList> entry : this.asMap().entrySet()) {
            if (entry.getValue().size() > 1) {
                list.add(new AbstractMap.SimpleEntry<Object, ResourceList>(entry.getKey(), entry.getValue()));
            }
        }
        CollectionUtils.sortIfNotEmpty(list, (Comparator<? super Object>)new Comparator<Map.Entry<String, ResourceList>>() {
            @Override
            public int compare(final Map.Entry<String, ResourceList> entry, final Map.Entry<String, ResourceList> entry2) {
                return entry.getKey().compareTo((String)entry2.getKey());
            }
        });
        return (List<Map.Entry<String, ResourceList>>)list;
    }
    
    @Deprecated
    public void forEachByteArray(final ByteArrayConsumer byteArrayConsumer) {
        this.forEachByteArray(byteArrayConsumer, false);
    }
    
    public List<String> getPathsRelativeToClasspathElement() {
        final ArrayList<String> list = new ArrayList<String>(this.size());
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getPath());
        }
        return list;
    }
    
    public ResourceList get(final String s) {
        boolean b = false;
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getPath().equals(s)) {
                b = true;
                break;
            }
        }
        if (!b) {
            return ResourceList.EMPTY_LIST;
        }
        final ResourceList list = new ResourceList(2);
        for (final Resource resource : this) {
            if (resource.getPath().equals(s)) {
                list.add(resource);
            }
        }
        return list;
    }
    
    public void forEachByteBufferIgnoringIOException(final ByteBufferConsumer byteBufferConsumer) {
        for (final Resource resource : this) {
            try {
                byteBufferConsumer.accept(resource, resource.read());
            }
            catch (IOException ex) {}
            finally {
                resource.close();
            }
        }
    }
    
    @Deprecated
    public void forEachByteBuffer(final ByteBufferConsumer byteBufferConsumer) {
        this.forEachByteBuffer(byteBufferConsumer, false);
    }
    
    public ResourceList classFilesOnly() {
        return this.filter(ResourceList.CLASSFILE_FILTER);
    }
    
    public void forEachByteArrayIgnoringIOException(final ByteArrayConsumer byteArrayConsumer) {
        for (final Resource resource : this) {
            try {
                byteArrayConsumer.accept(resource, resource.load());
            }
            catch (IOException ex) {}
            finally {
                resource.close();
            }
        }
    }
    
    @Override
    public void close() {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next().close();
        }
    }
    
    @FunctionalInterface
    public interface InputStreamConsumerThrowsIOException
    {
        void accept(final Resource p0, final InputStream p1) throws IOException;
    }
    
    @FunctionalInterface
    public interface ByteArrayConsumer
    {
        void accept(final Resource p0, final byte[] p1);
    }
    
    @FunctionalInterface
    public interface ByteBufferConsumerThrowsIOException
    {
        void accept(final Resource p0, final ByteBuffer p1) throws IOException;
    }
    
    @FunctionalInterface
    public interface InputStreamConsumer
    {
        void accept(final Resource p0, final InputStream p1);
    }
    
    @FunctionalInterface
    public interface ResourceFilter
    {
        boolean accept(final Resource p0);
    }
    
    @FunctionalInterface
    public interface ByteBufferConsumer
    {
        void accept(final Resource p0, final ByteBuffer p1);
    }
    
    @FunctionalInterface
    public interface ByteArrayConsumerThrowsIOException
    {
        void accept(final Resource p0, final byte[] p1) throws IOException;
    }
}
