//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import me.zoom.xannax.util.MathUtil;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.entity.Entity;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class KillAura extends Module
{
    /* synthetic */ Setting.Double range;
    /* synthetic */ Setting.Boolean criticals;
    public static /* synthetic */ EntityPlayer target;
    /* synthetic */ Setting.Mode aimMode;
    /* synthetic */ boolean rotating;
    /* synthetic */ Setting.Boolean toggleMsg;
    /* synthetic */ Setting.Boolean rotate;
    
    public KillAura() {
        super("KillAura", "KillAura", Category.Combat);
    }
    
    public void onDisable() {
        this.rotating = false;
        if (this.toggleMsg.getValue() && KillAura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Killaura has been toggled off!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (KillAura.mc.player != null || KillAura.mc.world != null) {
            for (final EntityPlayer target : KillAura.mc.world.playerEntities) {
                if (target != KillAura.mc.player) {
                    if (KillAura.mc.player.getDistance((Entity)target) < this.range.getValue()) {
                        if (Friends.isFriend(target.getName())) {
                            return;
                        }
                        if (target.isDead || target.getHealth() > 0.0f) {
                            if (this.rotating && this.rotate.getValue()) {
                                final float[] calcAngle = MathUtil.calcAngle(KillAura.mc.player.getPositionEyes(KillAura.mc.getRenderPartialTicks()), target.getPositionVector());
                                KillAura.mc.player.rotationYaw = calcAngle[0];
                                final String value = this.aimMode.getValue();
                                switch (value) {
                                    case "Leg": {
                                        KillAura.mc.player.rotationPitch = calcAngle[1];
                                        break;
                                    }
                                }
                            }
                            this.attackPlayer(target);
                        }
                        KillAura.target = target;
                    }
                    else {
                        this.rotating = false;
                    }
                }
            }
        }
    }
    
    public void attackPlayer(final EntityPlayer entityPlayer) {
        if (entityPlayer != null) {
            if (entityPlayer != KillAura.mc.player && KillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
                this.rotating = true;
                KillAura.mc.playerController.attackEntity((EntityPlayer)KillAura.mc.player, (Entity)entityPlayer);
                KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
        else {
            this.rotating = false;
        }
    }
    
    public void onEnable() {
        if (this.toggleMsg.getValue() && KillAura.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Killaura has been toggled on!");
        }
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Leg");
        this.aimMode = this.registerMode("Mode", "Mode", list, "Leg");
        this.range = this.registerDouble("Range", "Range", 4.5, 0.0, 10.0);
        this.criticals = this.registerBoolean("Criticals", "Criticals", true);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
}
