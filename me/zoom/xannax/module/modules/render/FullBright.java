//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class FullBright extends Module
{
    /* synthetic */ float old;
    /* synthetic */ Setting.Mode Mode;
    
    public FullBright() {
        super("FullBright", "FullBright", Category.Render);
    }
    
    public void onEnable() {
        this.old = FullBright.mc.gameSettings.gammaSetting;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Gamma");
        list.add("Potion");
        this.Mode = this.registerMode("Mode", "Mode", list, "Gamma");
    }
    
    @Override
    public void onUpdate() {
        if (this.Mode.getValue().equalsIgnoreCase("Gamma")) {
            FullBright.mc.gameSettings.gammaSetting = 666.0f;
            FullBright.mc.player.removePotionEffect(Potion.getPotionById(16));
        }
        else if (this.Mode.getValue().equalsIgnoreCase("Potion")) {
            final PotionEffect potionEffect = new PotionEffect(Potion.getPotionById(16), 123456789, 5);
            potionEffect.setPotionDurationMax(true);
            FullBright.mc.player.addPotionEffect(potionEffect);
        }
    }
    
    public void onDisable() {
        FullBright.mc.gameSettings.gammaSetting = this.old;
        FullBright.mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
