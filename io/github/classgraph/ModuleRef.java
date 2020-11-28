// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.utils.CollectionUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.io.IOException;
import java.io.File;
import java.net.URI;
import java.util.List;

public class ModuleRef implements Comparable<ModuleRef>
{
    private /* synthetic */ String locationStr;
    private final /* synthetic */ String name;
    private final /* synthetic */ Object layer;
    private final /* synthetic */ List<String> packages;
    private final /* synthetic */ Object descriptor;
    private final /* synthetic */ URI location;
    private /* synthetic */ String rawVersion;
    private /* synthetic */ File locationFile;
    private final /* synthetic */ Object reference;
    private final /* synthetic */ ClassLoader classLoader;
    
    @Override
    public int compareTo(final ModuleRef moduleRef) {
        final int compareTo = this.name.compareTo(moduleRef.name);
        return (compareTo != 0) ? compareTo : (this.hashCode() - moduleRef.hashCode());
    }
    
    public String getLocationStr() {
        if (this.locationStr == null && this.location != null) {
            this.locationStr = this.location.toString();
        }
        return this.locationStr;
    }
    
    @Override
    public int hashCode() {
        return this.reference.hashCode() * this.layer.hashCode();
    }
    
    public Object getLayer() {
        return this.layer;
    }
    
    public List<String> getPackages() {
        return this.packages;
    }
    
    public ModuleReaderProxy open() throws IOException {
        return new ModuleReaderProxy(this);
    }
    
    public Object getReference() {
        return this.reference;
    }
    
    public String getRawVersion() {
        return this.rawVersion;
    }
    
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public boolean isSystemModule() {
        return this.name != null && !this.name.isEmpty() && (this.name.startsWith("java.") || this.name.startsWith("jdk.") || this.name.startsWith("javafx.") || this.name.startsWith("oracle."));
    }
    
    public Object getDescriptor() {
        return this.descriptor;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ModuleRef(final Object reference, final Object layer) {
        if (reference == null) {
            throw new IllegalArgumentException("moduleReference cannot be null");
        }
        if (layer == null) {
            throw new IllegalArgumentException("moduleLayer cannot be null");
        }
        this.reference = reference;
        this.layer = layer;
        this.descriptor = ReflectionUtils.invokeMethod(reference, "descriptor", true);
        if (this.descriptor == null) {
            throw new IllegalArgumentException("moduleReference.descriptor() should not return null");
        }
        this.name = (String)ReflectionUtils.invokeMethod(this.descriptor, "name", true);
        final Set c = (Set)ReflectionUtils.invokeMethod(this.descriptor, "packages", true);
        if (c == null) {
            throw new IllegalArgumentException("moduleReference.descriptor().packages() should not return null");
        }
        this.packages = new ArrayList<String>(c);
        CollectionUtils.sortIfNotEmpty(this.packages);
        final Object invokeMethod = ReflectionUtils.invokeMethod(this.descriptor, "rawVersion", true);
        if (invokeMethod != null) {
            final Boolean b = (Boolean)ReflectionUtils.invokeMethod(invokeMethod, "isPresent", true);
            if (b != null && b) {
                this.rawVersion = (String)ReflectionUtils.invokeMethod(invokeMethod, "get", true);
            }
        }
        final Object invokeMethod2 = ReflectionUtils.invokeMethod(reference, "location", true);
        if (invokeMethod2 == null) {
            throw new IllegalArgumentException("moduleReference.location() should not return null");
        }
        final Object invokeMethod3 = ReflectionUtils.invokeMethod(invokeMethod2, "isPresent", true);
        if (invokeMethod3 == null) {
            throw new IllegalArgumentException("moduleReference.location().isPresent() should not return null");
        }
        if (invokeMethod3) {
            this.location = (URI)ReflectionUtils.invokeMethod(invokeMethod2, "get", true);
            if (this.location == null) {
                throw new IllegalArgumentException("moduleReference.location().get() should not return null");
            }
        }
        else {
            this.location = null;
        }
        this.classLoader = (ClassLoader)ReflectionUtils.invokeMethod(layer, "findLoader", String.class, this.name, true);
    }
    
    @Override
    public String toString() {
        return this.reference.toString();
    }
    
    public File getLocationFile() {
        if (this.locationFile == null && this.location != null && "file".equals(this.location.getScheme())) {
            this.locationFile = new File(this.location);
        }
        return this.locationFile;
    }
    
    public URI getLocation() {
        return this.location;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ModuleRef)) {
            return false;
        }
        final ModuleRef moduleRef = (ModuleRef)o;
        return moduleRef.reference.equals(this.reference) && moduleRef.layer.equals(this.layer);
    }
}
