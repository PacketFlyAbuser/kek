//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import net.minecraft.item.ItemPickaxe;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class NoEntityTrace extends Module
{
    /* synthetic */ boolean isHoldingPickaxe;
    /* synthetic */ Setting.Boolean pickaxeOnly;
    
    @Override
    public void onUpdate() {
        this.isHoldingPickaxe = (NoEntityTrace.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe);
    }
    
    @Override
    public void setup() {
        this.pickaxeOnly = this.registerBoolean("Pickaxe Only", "PickaxeOnly", true);
    }
    
    public boolean noTrace() {
        if (this.pickaxeOnly.getValue()) {
            return this.isEnabled() && this.isHoldingPickaxe;
        }
        return this.isEnabled();
    }
    
    public NoEntityTrace() {
        super("NoEntityTrace", "NoEntityTrace", Category.Player);
        this.isHoldingPickaxe = false;
    }
}
