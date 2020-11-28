// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.ext;

import java.util.HashMap;
import java.util.ArrayList;
import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;

public final class Extensions
{
    private final /* synthetic */ MixinTransformer transformer;
    private final /* synthetic */ List<IExtension> extensions;
    private final /* synthetic */ List<IClassGenerator> generatorsView;
    private /* synthetic */ List<IExtension> activeExtensions;
    private final /* synthetic */ Map<Class<? extends IClassGenerator>, IClassGenerator> generatorMap;
    private final /* synthetic */ List<IClassGenerator> generators;
    private final /* synthetic */ Map<Class<? extends IExtension>, IExtension> extensionMap;
    
    private static <T> T lookup(final Class<? extends T> clazz, final Map<Class<? extends T>, T> map, final List<T> list) {
        T value = map.get(clazz);
        if (value == null) {
            for (final T next : list) {
                if (clazz.isAssignableFrom(next.getClass())) {
                    value = next;
                    break;
                }
            }
            if (value == null) {
                throw new IllegalArgumentException("Extension for <" + clazz.getName() + "> could not be found");
            }
            map.put(clazz, value);
        }
        return value;
    }
    
    public List<IExtension> getExtensions() {
        return Collections.unmodifiableList((List<? extends IExtension>)this.extensions);
    }
    
    public void preApply(final ITargetClassContext targetClassContext) {
        final Iterator<IExtension> iterator = this.activeExtensions.iterator();
        while (iterator.hasNext()) {
            iterator.next().preApply(targetClassContext);
        }
    }
    
    public MixinTransformer getTransformer() {
        return this.transformer;
    }
    
    public List<IClassGenerator> getGenerators() {
        return this.generatorsView;
    }
    
    public List<IExtension> getActiveExtensions() {
        return this.activeExtensions;
    }
    
    public void select(final MixinEnvironment mixinEnvironment) {
        final ImmutableList.Builder builder = ImmutableList.builder();
        for (final IExtension extension : this.extensions) {
            if (extension.checkActive(mixinEnvironment)) {
                builder.add((Object)extension);
            }
        }
        this.activeExtensions = (List<IExtension>)builder.build();
    }
    
    public <T extends IExtension> T getExtension(final Class<? extends IExtension> clazz) {
        return lookup((Class<? extends T>)clazz, (Map<Class<? extends T>, T>)this.extensionMap, (List<T>)this.extensions);
    }
    
    public void add(final IExtension extension) {
        this.extensions.add(extension);
        this.extensionMap.put(extension.getClass(), extension);
    }
    
    public <T extends IClassGenerator> T getGenerator(final Class<? extends IClassGenerator> clazz) {
        return lookup((Class<? extends T>)clazz, (Map<Class<? extends T>, T>)this.generatorMap, (List<T>)this.generators);
    }
    
    public void export(final MixinEnvironment mixinEnvironment, final String s, final boolean b, final byte[] array) {
        final Iterator<IExtension> iterator = this.activeExtensions.iterator();
        while (iterator.hasNext()) {
            iterator.next().export(mixinEnvironment, s, b, array);
        }
    }
    
    public Extensions(final MixinTransformer transformer) {
        this.extensions = new ArrayList<IExtension>();
        this.extensionMap = new HashMap<Class<? extends IExtension>, IExtension>();
        this.generators = new ArrayList<IClassGenerator>();
        this.generatorsView = Collections.unmodifiableList((List<? extends IClassGenerator>)this.generators);
        this.generatorMap = new HashMap<Class<? extends IClassGenerator>, IClassGenerator>();
        this.activeExtensions = Collections.emptyList();
        this.transformer = transformer;
    }
    
    public void postApply(final ITargetClassContext targetClassContext) {
        final Iterator<IExtension> iterator = this.activeExtensions.iterator();
        while (iterator.hasNext()) {
            iterator.next().postApply(targetClassContext);
        }
    }
    
    public void add(final IClassGenerator classGenerator) {
        this.generators.add(classGenerator);
        this.generatorMap.put(classGenerator.getClass(), classGenerator);
    }
}
