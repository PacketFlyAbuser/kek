// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service.mojang;

import java.util.Iterator;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.Set;

final class LaunchClassLoaderUtil
{
    private final /* synthetic */ Set<String> transformerExceptions;
    private final /* synthetic */ Set<String> classLoaderExceptions;
    private final /* synthetic */ LaunchClassLoader classLoader;
    private final /* synthetic */ Set<String> invalidClasses;
    private final /* synthetic */ Map<String, Class<?>> cachedClasses;
    
    Set<String> getTransformerExceptions() {
        if (this.transformerExceptions != null) {
            return this.transformerExceptions;
        }
        return Collections.emptySet();
    }
    
    boolean isClassLoaded(final String s) {
        return this.cachedClasses.containsKey(s);
    }
    
    private static <T> T getField(final LaunchClassLoader obj, final String name) {
        try {
            final Field declaredField = LaunchClassLoader.class.getDeclaredField(name);
            declaredField.setAccessible(true);
            return (T)declaredField.get(obj);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    void registerInvalidClass(final String s) {
        if (this.invalidClasses != null) {
            this.invalidClasses.add(s);
        }
    }
    
    Set<String> getClassLoaderExceptions() {
        if (this.classLoaderExceptions != null) {
            return this.classLoaderExceptions;
        }
        return Collections.emptySet();
    }
    
    LaunchClassLoaderUtil(final LaunchClassLoader classLoader) {
        this.classLoader = classLoader;
        this.cachedClasses = getField(classLoader, "cachedClasses");
        this.invalidClasses = getField(classLoader, "invalidClasses");
        this.classLoaderExceptions = getField(classLoader, "classLoaderExceptions");
        this.transformerExceptions = getField(classLoader, "transformerExceptions");
    }
    
    static {
        CLASS_LOADER_EXCEPTIONS_FIELD = "classLoaderExceptions";
        CACHED_CLASSES_FIELD = "cachedClasses";
        INVALID_CLASSES_FIELD = "invalidClasses";
        TRANSFORMER_EXCEPTIONS_FIELD = "transformerExceptions";
    }
    
    LaunchClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    boolean isClassExcluded(final String s, final String s2) {
        for (final String s3 : this.getClassLoaderExceptions()) {
            if (s2.startsWith(s3) || s.startsWith(s3)) {
                return true;
            }
        }
        for (final String s4 : this.getTransformerExceptions()) {
            if (s2.startsWith(s4) || s.startsWith(s4)) {
                return true;
            }
        }
        return false;
    }
}
