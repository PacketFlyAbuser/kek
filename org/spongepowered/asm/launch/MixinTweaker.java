// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch;

import java.io.File;
import java.util.List;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.launchwrapper.ITweaker;

public class MixinTweaker implements ITweaker
{
    public MixinTweaker() {
        MixinBootstrap.start();
    }
    
    public final void injectIntoClassLoader(final LaunchClassLoader launchClassLoader) {
        MixinBootstrap.inject();
    }
    
    public String[] getLaunchArguments() {
        return new String[0];
    }
    
    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }
    
    public final void acceptOptions(final List<String> list, final File file, final File file2, final String s) {
        MixinBootstrap.doInit(list);
    }
}
