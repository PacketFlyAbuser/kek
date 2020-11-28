//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.text.DecimalFormat;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zoom.xannax.util.MotionUtils;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.EntityUtil;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Step extends Module
{
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ Setting.Boolean toggleMsg;
    /* synthetic */ Setting.Double height;
    /* synthetic */ Setting.Boolean timer;
    /* synthetic */ Setting.Boolean reverse;
    private /* synthetic */ int ticks;
    
    public Step() {
        super("Step", "Step", Category.Movement);
        this.ticks = 0;
    }
    
    @Override
    public String getHudInfo() {
        String s = "";
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            s = "[" + ChatFormatting.WHITE + "Normal" + ChatFormatting.GRAY + "]";
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
            s = "[" + ChatFormatting.WHITE + "Vanilla" + ChatFormatting.GRAY + "]";
        }
        return s;
    }
    
    public void onDisable() {
        Step.mc.player.stepHeight = 0.5f;
        if (this.toggleMsg.getValue() && Step.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Step has been toggled off!");
        }
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Normal");
        list.add("Vanilla");
        this.height = this.registerDouble("Height", "Height", 2.5, 0.5, 2.5);
        this.timer = this.registerBoolean("Timer", "Timer", false);
        this.reverse = this.registerBoolean("Reverse", "Reverse", false);
        this.mode = this.registerMode("Modes", "Modes", list, "Normal");
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    @Override
    public void onUpdate() {
        if (Step.mc.world == null || Step.mc.player == null || Step.mc.player.isInWater() || Step.mc.player.isInLava() || Step.mc.player.isOnLadder() || Step.mc.gameSettings.keyBindJump.isKeyDown()) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            if (this.timer.getValue()) {
                if (this.ticks == 0) {
                    EntityUtil.resetTimer();
                }
                else {
                    --this.ticks;
                }
            }
            if (Step.mc.player != null && Step.mc.player.onGround && !Step.mc.player.isInWater() && !Step.mc.player.isOnLadder() && this.reverse.getValue()) {
                for (double n = 0.0; n < this.height.getValue() + 0.5; n += 0.01) {
                    if (!Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty()) {
                        Step.mc.player.motionY = -10.0;
                        break;
                    }
                }
            }
            final double[] forward = MotionUtils.forward(0.1);
            boolean b = false;
            boolean b2 = false;
            boolean b3 = false;
            boolean b4 = false;
            if (Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.6, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.4, forward[1])).isEmpty()) {
                b = true;
            }
            if (Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 2.1, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.9, forward[1])).isEmpty()) {
                b2 = true;
            }
            if (Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.6, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.4, forward[1])).isEmpty()) {
                b3 = true;
            }
            if (Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 1.0, forward[1])).isEmpty() && !Step.mc.world.getCollisionBoxes((Entity)Step.mc.player, Step.mc.player.getEntityBoundingBox().offset(forward[0], 0.6, forward[1])).isEmpty()) {
                b4 = true;
            }
            if (Step.mc.player.collidedHorizontally && (Step.mc.player.moveForward != 0.0f || Step.mc.player.moveStrafing != 0.0f) && Step.mc.player.onGround) {
                if (b4 && this.height.getValue() >= 1.0) {
                    final double[] array = { 0.42, 0.753 };
                    for (int i = 0; i < array.length; ++i) {
                        Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + array[i], Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.6f);
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.0, Step.mc.player.posZ);
                    this.ticks = 1;
                }
                if (b3 && this.height.getValue() >= 1.5) {
                    final double[] array2 = { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
                    for (int j = 0; j < array2.length; ++j) {
                        Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + array2[j], Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.35f);
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 1.5, Step.mc.player.posZ);
                    this.ticks = 1;
                }
                if (b2 && this.height.getValue() >= 2.0) {
                    final double[] array3 = { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
                    for (int k = 0; k < array3.length; ++k) {
                        Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + array3[k], Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.25f);
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.0, Step.mc.player.posZ);
                    this.ticks = 2;
                }
                if (b && this.height.getValue() >= 2.5) {
                    final double[] array4 = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                    for (int l = 0; l < array4.length; ++l) {
                        Step.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Step.mc.player.posX, Step.mc.player.posY + array4[l], Step.mc.player.posZ, Step.mc.player.onGround));
                    }
                    if (this.timer.getValue()) {
                        EntityUtil.setTimer(0.15f);
                    }
                    Step.mc.player.setPosition(Step.mc.player.posX, Step.mc.player.posY + 2.5, Step.mc.player.posZ);
                    this.ticks = 2;
                }
            }
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
            Step.mc.player.stepHeight = Float.parseFloat(new DecimalFormat("#").format(this.height.getValue()));
        }
    }
    
    public void onEnable() {
        if (this.toggleMsg.getValue() && Step.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Step has been toggled on!");
        }
    }
}
