// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.fileslice.reader.ClassfileReader;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;
import java.io.File;
import java.net.URISyntaxException;
import nonapi.io.github.classgraph.utils.URLPathEncoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import nonapi.io.github.classgraph.utils.LogNode;
import java.io.Closeable;

public abstract class Resource implements Closeable, Comparable<Resource>
{
    private final /* synthetic */ ClasspathElement classpathElement;
    protected /* synthetic */ InputStream inputStream;
    protected /* synthetic */ long length;
    private /* synthetic */ String toString;
    
    public abstract InputStream open() throws IOException;
    
    public Resource(final ClasspathElement classpathElement, final long length) {
        this.classpathElement = classpathElement;
        this.length = length;
    }
    
    public abstract String getPath();
    
    public String getContentAsString() throws IOException {
        try {
            return new String(this.load(), StandardCharsets.UTF_8);
        }
        finally {
            this.close();
        }
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public abstract long getLastModified();
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof Resource && this.toString().equals(o.toString()));
    }
    
    public abstract String getPathRelativeToClasspathElement();
    
    @Override
    public String toString() {
        if (this.toString != null) {
            return this.toString;
        }
        final String string = this.getURI().toString();
        this.toString = string;
        return string;
    }
    
    private static URL uriToURL(final URI uri) {
        if (uri.getScheme().equals("jrt")) {
            throw new IllegalArgumentException("Could not create URL from URI with \"jrt:\" scheme: " + uri);
        }
        try {
            return uri.toURL();
        }
        catch (MalformedURLException obj) {
            throw new IllegalArgumentException("Could not create URL from URI: " + uri + " -- " + obj);
        }
    }
    
    public URL getURL() {
        return uriToURL(this.getURI());
    }
    
    public URI getURI() {
        final String string = this.getClasspathElementURI().toString();
        final String pathRelativeToClasspathElement = this.getPathRelativeToClasspathElement();
        final boolean endsWith = string.endsWith("/");
        try {
            return new URI(((endsWith || string.startsWith("jar:") || string.startsWith("jrt:")) ? "" : "jar:") + string + (endsWith ? "" : (string.startsWith("jrt:") ? "/" : "!/")) + URLPathEncoder.encodePath(pathRelativeToClasspathElement));
        }
        catch (URISyntaxException obj) {
            throw new IllegalArgumentException("Could not form URL for classpath element: " + string + " ; path: " + pathRelativeToClasspathElement + " : " + obj);
        }
    }
    
    public File getClasspathElementFile() {
        return this.classpathElement.getFile();
    }
    
    @Override
    public void close() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            }
            catch (IOException ex) {}
            this.inputStream = null;
        }
    }
    
    public abstract byte[] load() throws IOException;
    
    @Override
    public int compareTo(final Resource resource) {
        return this.toString().compareTo(resource.toString());
    }
    
    public abstract Set<PosixFilePermission> getPosixFilePermissions();
    
    public URI getClasspathElementURI() {
        return this.classpathElement.getURI();
    }
    
    public ModuleRef getModuleRef() {
        return (this.classpathElement instanceof ClasspathElementModule) ? ((ClasspathElementModule)this.classpathElement).moduleRef : null;
    }
    
    abstract ClassfileReader openClassfile() throws IOException;
    
    public abstract ByteBuffer read() throws IOException;
    
    public long getLength() {
        return this.length;
    }
    
    public URL getClasspathElementURL() {
        return uriToURL(this.getClasspathElementURI());
    }
}
