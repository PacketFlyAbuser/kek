//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import net.minecraft.client.renderer.ItemRenderer;
import me.zoom.xannax.module.Module;

public class LowHands extends Module
{
    /* synthetic */ ItemRenderer itemRenderer;
    /* synthetic */ Setting.Double off;
    
    public LowHands() {
        super("LowOffhand", "LowOffhand", Category.Render);
        this.itemRenderer = LowHands.mc.entityRenderer.itemRenderer;
    }
    
    @Override
    public void onUpdate() {
        this.itemRenderer.equippedProgressOffHand = (float)this.off.getValue();
    }
    
    @Override
    public void setup() {
        this.off = this.registerDouble("Height", "LowOffhandHeight", 0.5, 0.0, 1.0);
    }
}
