//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.util.MotionUtils;
import me.zoom.xannax.event.events.JumpEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Sprint extends Module
{
    /* synthetic */ Setting.Mode Mode;
    
    public Sprint() {
        super("Sprint", "Sprint", Category.Movement);
    }
    
    public void onJump(final JumpEvent jumpEvent) {
        if (this.Mode.getValue().equalsIgnoreCase("Rage")) {
            final double[] forward = MotionUtils.forward(0.01745329238474369);
            jumpEvent.getLocation().setX(forward[0] * 0.20000000298023224);
            jumpEvent.getLocation().setZ(forward[1] * 0.20000000298023224);
        }
    }
    
    @Override
    public void onUpdate() {
        if (Sprint.mc.gameSettings.keyBindSneak.isKeyDown()) {
            Sprint.mc.player.setSprinting(false);
            return;
        }
        if (Sprint.mc.player.getFoodStats().getFoodLevel() > 6 && this.Mode.getValue().equalsIgnoreCase("Rage")) {
            if (Sprint.mc.player.moveForward == 0.0f) {
                if (Sprint.mc.player.moveStrafing == 0.0f) {
                    return;
                }
            }
        }
        else if (Sprint.mc.player.moveForward <= 0.0f) {
            return;
        }
        Sprint.mc.player.setSprinting(true);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Legit");
        list.add("Rage");
        this.Mode = this.registerMode("Mode", "Mode", list, "Legit");
    }
    
    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.Mode.getValue() + ChatFormatting.GRAY + "]";
    }
}
