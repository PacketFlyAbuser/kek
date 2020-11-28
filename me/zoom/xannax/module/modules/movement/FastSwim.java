//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class FastSwim extends Module
{
    private /* synthetic */ Setting.Boolean only2b;
    private /* synthetic */ Setting.Boolean sprint;
    private /* synthetic */ Setting.Boolean forward;
    /* synthetic */ int divider;
    private /* synthetic */ Setting.Boolean down;
    private /* synthetic */ Setting.Boolean up;
    
    @Override
    public void onUpdate() {
        if ((boolean)this.only2b.getValue() && !FastSwim.mc.isSingleplayer() && FastSwim.mc.getCurrentServerData() != null && FastSwim.mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org")) {
            if ((boolean)this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && (boolean)this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((!(boolean)this.forward.getValue() || !FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) && !FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() && !FastSwim.mc.gameSettings.keyBindRight.isKeyDown() && !FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
            }
            if (FastSwim.mc.player.isInWater() && (boolean)this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                FastSwim.mc.player.motionY = 2.2 / (this.divider * -1);
            }
            if (FastSwim.mc.player.isInLava() && (boolean)this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                FastSwim.mc.player.motionY = 0.91 / (this.divider * -1);
            }
        }
        if (!(boolean)this.only2b.getValue()) {
            if ((boolean)this.sprint.getValue() && (FastSwim.mc.player.isInLava() || FastSwim.mc.player.isInWater())) {
                FastSwim.mc.player.setSprinting(true);
            }
            if ((FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) && FastSwim.mc.gameSettings.keyBindJump.isKeyDown() && (boolean)this.up.getValue()) {
                FastSwim.mc.player.motionY = 0.725 / this.divider;
            }
            if (FastSwim.mc.player.isInWater() || FastSwim.mc.player.isInLava()) {
                if ((!(boolean)this.forward.getValue() || !FastSwim.mc.gameSettings.keyBindForward.isKeyDown()) && !FastSwim.mc.gameSettings.keyBindLeft.isKeyDown() && !FastSwim.mc.gameSettings.keyBindRight.isKeyDown() && !FastSwim.mc.gameSettings.keyBindBack.isKeyDown()) {
                    FastSwim.mc.player.jumpMovementFactor = 0.0f;
                }
                else {
                    FastSwim.mc.player.jumpMovementFactor = 0.34f / this.divider;
                }
            }
            if (FastSwim.mc.player.isInWater() && (boolean)this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                FastSwim.mc.player.motionY = 2.2 / (this.divider * -1);
            }
            if (FastSwim.mc.player.isInLava() && (boolean)this.down.getValue() && FastSwim.mc.gameSettings.keyBindSneak.isKeyDown()) {
                FastSwim.mc.player.motionY = 0.91 / (this.divider * -1);
            }
        }
    }
    
    public FastSwim() {
        super("FastSwim", "FastSwim", Category.Movement);
        this.divider = 5;
    }
    
    @Override
    public void setup() {
        this.up = this.registerBoolean("FastSwimUp", "FastSwimUp", true);
        this.down = this.registerBoolean("FastSwimDown", "FastSwimDown", true);
        this.forward = this.registerBoolean("FastSwimForward", "FastSwimForward", true);
        this.sprint = this.registerBoolean("AutoSprintInLiquid", "AutoSprintInLiquid", true);
        this.only2b = this.registerBoolean("Only2b", "Only2b", true);
    }
}
