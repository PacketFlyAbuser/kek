//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import java.util.List;
import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class FovSlider extends Module
{
    private final /* synthetic */ Setting.Mode mode;
    private final /* synthetic */ Setting.Integer FOV;
    private /* synthetic */ float fov;
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.fov = FovSlider.mc.gameSettings.fovSetting;
    }
    
    public FovSlider() {
        super("FovSlider", "Better FOV slider", Category.Render);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("FovChanger");
        list.add("HandChanger");
        this.FOV = this.registerInteger("FOV", "FOV", 110, 90, 200);
        this.mode = this.registerMode("Mode", "Mode", list, "FovChanger");
    }
    
    @Override
    public void onUpdate() {
        if (!this.isEnabled() || FovSlider.mc.world == null) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("FovChanger")) {
            FovSlider.mc.gameSettings.fovSetting = (float)this.FOV.getValue();
        }
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        FovSlider.mc.gameSettings.fovSetting = this.fov;
    }
    
    @SubscribeEvent
    public void fovOn(final EntityViewRenderEvent.FOVModifier fovModifier) {
        if (this.mode.getValue().equalsIgnoreCase("HandChanger")) {
            fovModifier.setFOV((float)this.FOV.getValue());
        }
    }
}
