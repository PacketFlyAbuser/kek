//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Static extends Module
{
    /* synthetic */ Setting.Boolean ySpeed;
    /* synthetic */ Setting.Boolean disabler;
    /* synthetic */ Setting.Double height;
    /* synthetic */ Setting.Double speed;
    /* synthetic */ Setting.Mode mode;
    
    @Override
    public void onUpdate() {
        final String value = this.mode.getValue();
        switch (value) {
            case "STATIC": {
                Static.mc.player.capabilities.isFlying = false;
                Static.mc.player.motionX = 0.0;
                Static.mc.player.motionY = 0.0;
                Static.mc.player.motionZ = 0.0;
                if (!this.ySpeed.getValue()) {
                    break;
                }
                Static.mc.player.jumpMovementFactor = (float)this.speed.getValue();
                if (Static.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = Static.mc.player;
                    player.motionY += this.speed.getValue();
                }
                if (Static.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2 = Static.mc.player;
                    player2.motionY -= this.speed.getValue();
                    break;
                }
                break;
            }
            case "ROOF": {
                Static.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Static.mc.player.posX, 10000.0, Static.mc.player.posZ, Static.mc.player.onGround));
                if (this.disabler.getValue()) {
                    this.disable();
                    break;
                }
                break;
            }
            case "NOVOID": {
                if (Static.mc.player.noClip) {
                    break;
                }
                if (Static.mc.player.posY > this.height.getValue()) {
                    break;
                }
                final RayTraceResult rayTraceBlocks = Static.mc.world.rayTraceBlocks(Static.mc.player.getPositionVector(), new Vec3d(Static.mc.player.posX, 0.0, Static.mc.player.posZ), false, false, false);
                if (rayTraceBlocks != null && rayTraceBlocks.typeOfHit == RayTraceResult.Type.BLOCK) {
                    return;
                }
                Static.mc.player.setVelocity(0.0, 0.0, 0.0);
                if (Static.mc.player.getRidingEntity() != null) {
                    Static.mc.player.getRidingEntity().setVelocity(0.0, 0.0, 0.0);
                    break;
                }
                break;
            }
        }
    }
    
    public Static() {
        super("Static", "Static", Category.Movement);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("STATIC");
        list.add("ROOF");
        list.add("NOVOID");
        this.mode = this.registerMode("Mode", "Mode", list, "NOVOID");
        this.disabler = this.registerBoolean("Disable", "Disable", true);
        this.ySpeed = this.registerBoolean("YSpeed", "YSpeed", false);
        this.speed = this.registerDouble("Speed", "Speed", 0.10000000149011612, 0.0, 10.0);
        this.height = this.registerDouble("Height", "Height", 3.0, 0.0, 256.0);
    }
}
