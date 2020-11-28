// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service.mojang;

import net.minecraft.launchwrapper.Launch;
import org.spongepowered.asm.service.IMixinServiceBootstrap;

public class MixinServiceLaunchWrapperBootstrap implements IMixinServiceBootstrap
{
    @Override
    public String getServiceClassName() {
        return "org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper";
    }
    
    @Override
    public String getName() {
        return "LaunchWrapper";
    }
    
    @Override
    public void bootstrap() {
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.service.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.lib.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.mixin.");
        Launch.classLoader.addClassLoaderExclusion("org.spongepowered.asm.util.");
    }
    
    static {
        ASM_PACKAGE = "org.spongepowered.asm.lib.";
        MIXIN_UTIL_PACKAGE = "org.spongepowered.asm.util.";
        SERVICE_PACKAGE = "org.spongepowered.asm.service.";
        MIXIN_PACKAGE = "org.spongepowered.asm.mixin.";
    }
}
