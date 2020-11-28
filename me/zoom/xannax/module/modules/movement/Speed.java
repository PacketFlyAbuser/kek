//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import me.zoom.xannax.util.EntityUtil;
import net.minecraft.entity.Entity;
import me.zoom.xannax.event.events.MoveEvent;
import java.util.List;
import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import me.zoom.xannax.command.Command;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.zoom.xannax.event.events.UpdateWalkingPlayerEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Speed extends Module
{
    /* synthetic */ Setting.Integer acceleration;
    /* synthetic */ Setting.Integer specialMoveSpeed;
    /* synthetic */ Setting.Boolean potion;
    private /* synthetic */ double lastDist;
    private /* synthetic */ double moveSpeed;
    /* synthetic */ Setting.Boolean limiter2;
    private /* synthetic */ int cooldownHops;
    /* synthetic */ Setting.Boolean limiter;
    /* synthetic */ Setting.Integer potionSpeed2;
    private /* synthetic */ int stage;
    /* synthetic */ Setting.Integer potionSpeed;
    /* synthetic */ Setting.Boolean step;
    /* synthetic */ Setting.Boolean toggleMsg;
    /* synthetic */ Setting.Mode mode;
    
    public static double getBaseMoveSpeed() {
        double n = 0.272;
        if (Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            n *= 1.0 + 0.2 * Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
        }
        return n;
    }
    
    public static double round(final double val, final int newScale) {
        if (newScale < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(val).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
    }
    
    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.mode.getValue() + ChatFormatting.GRAY + "]";
    }
    
    public Speed() {
        super("Speed", "AirControl etc.", Category.Movement);
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent updateWalkingPlayerEvent) {
        if (updateWalkingPlayerEvent.getStage() == 0) {
            this.lastDist = Math.sqrt((Speed.mc.player.posX - Speed.mc.player.prevPosX) * (Speed.mc.player.posX - Speed.mc.player.prevPosX) + (Speed.mc.player.posZ - Speed.mc.player.prevPosZ) * (Speed.mc.player.posZ - Speed.mc.player.prevPosZ));
        }
    }
    
    public void onDisable() {
        this.moveSpeed = 0.0;
        this.stage = 2;
        if (this.toggleMsg.getValue() && Speed.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "Speed has been toggled off!");
        }
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    private float getMultiplier() {
        float n = (float)this.specialMoveSpeed.getValue();
        if (this.potion.getValue() && Speed.mc.player.isPotionActive(MobEffects.SPEED)) {
            if (Objects.requireNonNull(Speed.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1 >= 2) {
                n = (float)this.potionSpeed2.getValue();
            }
            else {
                n = (float)this.potionSpeed.getValue();
            }
        }
        return n / 100.0f;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("NONE");
        list.add("NCP");
        list.add("Strafe");
        this.mode = this.registerMode("Mode", "Mode", list, "NCP");
        this.limiter = this.registerBoolean("SetGround", "SetGround", true);
        this.limiter2 = this.registerBoolean("Bhop", "Bhop", false);
        this.specialMoveSpeed = this.registerInteger("Speed", "Speed", 100, 0, 150);
        this.potionSpeed = this.registerInteger("Speed1", "Speed1", 130, 0, 150);
        this.potionSpeed2 = this.registerInteger("Speed2", "Speed2", 125, 0, 150);
        this.acceleration = this.registerInteger("Accel", "Accel", 2149, 1000, 2500);
        this.potion = this.registerBoolean("Potion", "Potion", false);
        this.step = this.registerBoolean("SetStep", "SetStep", true);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
        this.stage = 1;
        this.cooldownHops = 0;
    }
    
    private void doNCP(final MoveEvent moveEvent) {
        if (!this.limiter.getValue() && Speed.mc.player.onGround) {
            this.stage = 2;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            case 2: {
                double motionY = 0.40123128;
                if ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) && Speed.mc.player.onGround) {
                    if (Speed.mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                        motionY += (Speed.mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                    }
                    moveEvent.setY(Speed.mc.player.motionY = motionY);
                    this.moveSpeed *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - getBaseMoveSpeed());
                break;
            }
            default: {
                if (((this.limiter2.getValue() && Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0) || Speed.mc.player.collidedVertically) && this.stage > 0) {
                    this.stage = ((Speed.mc.player.moveForward != 0.0f || Speed.mc.player.moveStrafing != 0.0f) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
        double n = Speed.mc.player.movementInput.moveForward;
        double n2 = Speed.mc.player.movementInput.moveStrafe;
        final double n3 = Speed.mc.player.rotationYaw;
        if (n == 0.0 && n2 == 0.0) {
            moveEvent.setX(0.0);
            moveEvent.setZ(0.0);
        }
        else if (n != 0.0 && n2 != 0.0) {
            n *= Math.sin(0.7853981633974483);
            n2 *= Math.cos(0.7853981633974483);
        }
        moveEvent.setX((n * this.moveSpeed * -Math.sin(Math.toRadians(n3)) + n2 * this.moveSpeed * Math.cos(Math.toRadians(n3))) * 0.99);
        moveEvent.setZ((n * this.moveSpeed * Math.cos(Math.toRadians(n3)) - n2 * this.moveSpeed * -Math.sin(Math.toRadians(n3))) * 0.99);
        ++this.stage;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent moveEvent) {
        if (moveEvent.getStage() != 0) {
            return;
        }
        if (this.mode.getValue().equalsIgnoreCase("NCP")) {
            this.doNCP(moveEvent);
        }
        else if (this.mode.getValue().equalsIgnoreCase("Strafe")) {
            float moveForward = Speed.mc.player.movementInput.moveForward;
            float moveStrafe = Speed.mc.player.movementInput.moveStrafe;
            float rotationYaw = Speed.mc.player.rotationYaw;
            if (this.limiter2.getValue() && Speed.mc.player.onGround) {
                this.stage = 2;
            }
            if (this.limiter.getValue() && round(Speed.mc.player.posY - (int)Speed.mc.player.posY, 3) == round(0.138, 3)) {
                final EntityPlayerSP player = Speed.mc.player;
                player.motionY -= 0.13;
                moveEvent.setY(moveEvent.getY() - 0.13);
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.posY -= 0.13;
            }
            if (this.stage == 1 && EntityUtil.isMoving()) {
                this.stage = 2;
                this.moveSpeed = this.getMultiplier() * getBaseMoveSpeed() - 0.01;
            }
            else if (this.stage == 2) {
                this.stage = 3;
                if (EntityUtil.isMoving()) {
                    moveEvent.setY(Speed.mc.player.motionY = 0.4);
                    if (this.cooldownHops > 0) {
                        --this.cooldownHops;
                    }
                    this.moveSpeed *= this.acceleration.getValue() / 1000.0;
                }
            }
            else if (this.stage == 3) {
                this.stage = 4;
                this.moveSpeed = this.lastDist - 0.66 * (this.lastDist - getBaseMoveSpeed());
            }
            else {
                if (Speed.mc.world.getCollisionBoxes((Entity)Speed.mc.player, Speed.mc.player.getEntityBoundingBox().offset(0.0, Speed.mc.player.motionY, 0.0)).size() > 0 || Speed.mc.player.collidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, getBaseMoveSpeed());
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                moveEvent.setX(0.0);
                moveEvent.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            if (this.cooldownHops == 0) {
                moveEvent.setX(moveForward * this.moveSpeed * cos + moveStrafe * this.moveSpeed * sin);
                moveEvent.setZ(moveForward * this.moveSpeed * sin - moveStrafe * this.moveSpeed * cos);
            }
            if (this.step.getValue()) {
                Speed.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                moveEvent.setX(0.0);
                moveEvent.setZ(0.0);
            }
        }
    }
    
    public void onEnable() {
        this.moveSpeed = getBaseMoveSpeed();
        if (this.toggleMsg.getValue() && Speed.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Speed has been toggled on!");
        }
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
}
