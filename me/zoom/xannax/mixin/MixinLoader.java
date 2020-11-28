// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.launch.MixinBootstrap;
import me.zoom.xannax.Xannax;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class MixinLoader implements IFMLLoadingPlugin
{
    private static /* synthetic */ boolean isObfuscatedEnvironment;
    
    public String getAccessTransformerClass() {
        return null;
    }
    
    @Nullable
    public String getSetupClass() {
        return null;
    }
    
    public String[] getASMTransformerClass() {
        return new String[0];
    }
    
    public void injectData(final Map<String, Object> map) {
        MixinLoader.isObfuscatedEnvironment = map.get("runtimeDeobfuscationEnabled");
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    static {
        MixinLoader.isObfuscatedEnvironment = false;
    }
    
    public MixinLoader() {
        Xannax.log.info("Mixins initialized");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.xannax.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        Xannax.log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }
}
