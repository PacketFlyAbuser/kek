// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import com.google.common.io.Files;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import org.spongepowered.tools.obfuscation.mirror.TypeHandle;
import java.util.Collection;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import org.spongepowered.tools.obfuscation.mirror.TypeReference;
import java.util.HashMap;

public final class TargetMap extends HashMap<TypeReference, Set<TypeReference>>
{
    private final /* synthetic */ String sessionId;
    
    public Collection<TypeReference> getMixinsTargeting(final TypeElement typeElement) {
        return this.getMixinsTargeting(new TypeHandle(typeElement));
    }
    
    private TargetMap() {
        this(String.valueOf(System.currentTimeMillis()));
    }
    
    public void write(final boolean b) {
        ObjectOutputStream objectOutputStream = null;
        try {
            final File sessionFile = getSessionFile(this.sessionId);
            if (b) {
                sessionFile.deleteOnExit();
            }
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(sessionFile, true));
            objectOutputStream.writeObject(this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                }
                catch (IOException ex3) {
                    ex3.printStackTrace();
                }
            }
        }
    }
    
    public void addMixin(final TypeReference typeReference, final TypeReference typeReference2) {
        this.getMixinsFor(typeReference).add(typeReference2);
    }
    
    public void registerTargets(final List<TypeHandle> list, final TypeHandle typeHandle) {
        final Iterator<TypeHandle> iterator = list.iterator();
        while (iterator.hasNext()) {
            this.addMixin(iterator.next(), typeHandle);
        }
    }
    
    private TargetMap(final String sessionId) {
        this.sessionId = sessionId;
    }
    
    private Set<TypeReference> getMixinsFor(final TypeReference typeReference) {
        Set<TypeReference> value = ((HashMap<K, Set<TypeReference>>)this).get(typeReference);
        if (value == null) {
            value = new HashSet<TypeReference>();
            this.put(typeReference, value);
        }
        return value;
    }
    
    public Collection<TypeReference> getMixinsTargeting(final TypeReference typeReference) {
        return Collections.unmodifiableCollection((Collection<? extends TypeReference>)this.getMixinsFor(typeReference));
    }
    
    private static File getSessionFile(final String s) {
        return new File(new File(System.getProperty("java.io.tmpdir")), String.format("mixin-targetdb-%s.tmp", s));
    }
    
    public void addMixin(final String s, final String s2) {
        this.addMixin(new TypeReference(s), new TypeReference(s2));
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public void readImports(final File file) throws IOException {
        if (!file.isFile()) {
            return;
        }
        final Iterator<String> iterator = Files.readLines(file, Charset.defaultCharset()).iterator();
        while (iterator.hasNext()) {
            final String[] split = iterator.next().split("\t");
            if (split.length == 2) {
                this.addMixin(split[1], split[0]);
            }
        }
    }
    
    public static TargetMap create(final String s) {
        if (s != null) {
            final File sessionFile = getSessionFile(s);
            if (sessionFile.exists()) {
                final TargetMap read = read(sessionFile);
                if (read != null) {
                    return read;
                }
            }
        }
        return new TargetMap();
    }
    
    public Collection<TypeReference> getMixinsTargeting(final TypeHandle typeHandle) {
        return this.getMixinsTargeting(typeHandle.getReference());
    }
    
    public void addMixin(final TypeHandle typeHandle, final TypeHandle typeHandle2) {
        this.addMixin(typeHandle.getReference(), typeHandle2.getReference());
    }
    
    private static TargetMap read(final File file) {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return (TargetMap)objectInputStream.readObject();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
        finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                }
                catch (IOException ex3) {
                    ex3.printStackTrace();
                }
            }
        }
        return null;
    }
    
    public void registerTargets(final AnnotatedMixin annotatedMixin) {
        this.registerTargets(annotatedMixin.getTargets(), annotatedMixin.getHandle());
    }
}
