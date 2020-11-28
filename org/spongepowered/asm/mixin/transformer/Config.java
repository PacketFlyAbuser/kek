// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer;

import org.spongepowered.asm.mixin.extensibility.IMixinConfig;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class Config
{
    private final /* synthetic */ String name;
    private final /* synthetic */ MixinConfig config;
    
    public String getName() {
        return this.name;
    }
    
    MixinConfig get() {
        return this.config;
    }
    
    public MixinEnvironment getEnvironment() {
        return this.config.getEnvironment();
    }
    
    public IMixinConfig getConfig() {
        return this.config;
    }
    
    public static Config create(final String s) {
        return MixinConfig.create(s, MixinEnvironment.getDefaultEnvironment());
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Config && this.name.equals(((Config)o).name);
    }
    
    @Override
    public String toString() {
        return this.config.toString();
    }
    
    public boolean isVisited() {
        return this.config.isVisited();
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public Config(final MixinConfig config) {
        this.name = config.getName();
        this.config = config;
    }
    
    @Deprecated
    public static Config create(final String s, final MixinEnvironment mixinEnvironment) {
        return MixinConfig.create(s, mixinEnvironment);
    }
}
