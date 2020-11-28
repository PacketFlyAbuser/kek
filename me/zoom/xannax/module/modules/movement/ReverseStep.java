//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import net.minecraft.entity.Entity;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ReverseStep extends Module
{
    /* synthetic */ Setting.Double height;
    
    @Override
    public void onUpdate() {
        if (ReverseStep.mc.world == null || ReverseStep.mc.player == null || ReverseStep.mc.player.isInWater() || ReverseStep.mc.player.isInLava() || ReverseStep.mc.player.isOnLadder() || ReverseStep.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (ReverseStep.mc.player != null && ReverseStep.mc.player.onGround && !ReverseStep.mc.player.isInWater() && !ReverseStep.mc.player.isOnLadder()) {
            for (double n = 0.0; n < this.height.getValue() + 0.5; n += 0.01) {
                if (!ReverseStep.mc.world.getCollisionBoxes((Entity)ReverseStep.mc.player, ReverseStep.mc.player.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty()) {
                    ReverseStep.mc.player.motionY = -10.0;
                    break;
                }
            }
        }
    }
    
    @Override
    public void setup() {
        this.height = this.registerDouble("Height", "Height", 2.5, 0.5, 15.0);
    }
    
    public ReverseStep() {
        super("ReverseStep", "ReverseStep", Category.Movement);
    }
}
