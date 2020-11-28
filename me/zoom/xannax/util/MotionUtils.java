//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;

public class MotionUtils
{
    public static double getBaseMoveSpeed() {
        double n = 0.2873;
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.isPotionActive(Potion.getPotionById(1))) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier() + 1);
        }
        return n;
    }
    
    public static void setSpeed(final EntityLivingBase entityLivingBase, final double n) {
        final double[] forward = forward(n);
        entityLivingBase.motionX = forward[0];
        entityLivingBase.motionZ = forward[1];
    }
    
    public static double getSpeed(final EntityLivingBase entityLivingBase) {
        return Math.sqrt(entityLivingBase.motionX * entityLivingBase.motionX + entityLivingBase.motionZ * entityLivingBase.motionZ);
    }
    
    public static double[] forward(final double n) {
        float moveForward = Minecraft.getMinecraft().player.movementInput.moveForward;
        float moveStrafe = Minecraft.getMinecraft().player.movementInput.moveStrafe;
        float n2 = Minecraft.getMinecraft().player.prevRotationYaw + (Minecraft.getMinecraft().player.rotationYaw - Minecraft.getMinecraft().player.prevRotationYaw) * Minecraft.getMinecraft().getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                n2 += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                n2 += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(n2 + 90.0f));
        final double cos = Math.cos(Math.toRadians(n2 + 90.0f));
        return new double[] { moveForward * n * cos + moveStrafe * n * sin, moveForward * n * sin - moveStrafe * n * cos };
    }
    
    public static boolean isMoving(final EntityLivingBase entityLivingBase) {
        return entityLivingBase.moveForward != 0.0f || entityLivingBase.moveStrafing != 0.0f;
    }
}
