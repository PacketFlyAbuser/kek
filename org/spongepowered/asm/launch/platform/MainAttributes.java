// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch.platform;

import java.util.jar.Manifest;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.HashMap;
import java.io.File;
import java.util.jar.Attributes;
import java.net.URI;
import java.util.Map;

final class MainAttributes
{
    private static final /* synthetic */ Map<URI, MainAttributes> instances;
    protected final /* synthetic */ Attributes attributes;
    
    public static MainAttributes of(final File file) {
        return of(file.toURI());
    }
    
    static {
        instances = new HashMap<URI, MainAttributes>();
    }
    
    public final String get(final String name) {
        if (this.attributes != null) {
            return this.attributes.getValue(name);
        }
        return null;
    }
    
    public static MainAttributes of(final URI uri) {
        MainAttributes mainAttributes = MainAttributes.instances.get(uri);
        if (mainAttributes == null) {
            mainAttributes = new MainAttributes(new File(uri));
            MainAttributes.instances.put(uri, mainAttributes);
        }
        return mainAttributes;
    }
    
    private static Attributes getAttributes(final File file) {
        if (file == null) {
            return null;
        }
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            final Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                return manifest.getMainAttributes();
            }
        }
        catch (IOException ex) {}
        finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
            }
            catch (IOException ex2) {}
        }
        return new Attributes();
    }
    
    private MainAttributes() {
        this.attributes = new Attributes();
    }
    
    private MainAttributes(final File file) {
        this.attributes = getAttributes(file);
    }
}
