//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.AxisAlignedBB;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import java.util.Calendar;
import java.math.RoundingMode;
import java.math.BigDecimal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import java.util.Random;
import net.minecraft.client.Minecraft;

public class MathUtil
{
    public static final /* synthetic */ Minecraft mc;
    private static final /* synthetic */ Random random;
    
    public static Vec3d direction(final float n) {
        return new Vec3d(Math.cos(degToRad(n + 90.0f)), 0.0, Math.sin(degToRad(n + 90.0f)));
    }
    
    public static float cos(final float n) {
        return MathHelper.cos(n);
    }
    
    public static float round(final float n, final int newScale) {
        if (newScale < 0) {
            throw new IllegalArgumentException();
        }
        return BigDecimal.valueOf(n).setScale(newScale, RoundingMode.FLOOR).floatValue();
    }
    
    public static double radToDeg(final double n) {
        return n * 57.295780181884766;
    }
    
    public static int getRandom(final int n, final int n2) {
        return n + MathUtil.random.nextInt(n2 - n + 1);
    }
    
    public static double square(final double n) {
        return n * n;
    }
    
    public static float[] calcAngle(final Vec3d vec3d, final Vec3d vec3d2) {
        final double x = vec3d2.x - vec3d.x;
        final double y = (vec3d2.y - vec3d.y) * -1.0;
        final double y2 = vec3d2.z - vec3d.z;
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y2, x)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y, MathHelper.sqrt(x * x + y2 * y2)))) };
    }
    
    public static String getTimeOfDay() {
        final int value = Calendar.getInstance().get(11);
        if (value < 12) {
            return "Good Morning ";
        }
        if (value < 16) {
            return "Good Afternoon ";
        }
        if (value < 21) {
            return "Good Evening ";
        }
        return "Good Night ";
    }
    
    public static int clamp(final int a, final int n, final int b) {
        return (a < n) ? n : Math.min(a, b);
    }
    
    public static double getRandom(final double n, final double n2) {
        return MathHelper.clamp(n + MathUtil.random.nextDouble() * n2, n, n2);
    }
    
    public static boolean areVec3dsAlignedRetarded(final Vec3d vec3d, final Vec3d vec3d2) {
        return new BlockPos(vec3d).equals((Object)new BlockPos(vec3d2.x, vec3d.y, vec3d2.z));
    }
    
    public static double degToRad(final double n) {
        return n * 0.01745329238474369;
    }
    
    public static double wrapDegrees(final double n) {
        return MathHelper.wrapDegrees(n);
    }
    
    public static double clamp(final double a, final double n, final double b) {
        return (a < n) ? n : Math.min(a, b);
    }
    
    public static float wrapDegrees(final float n) {
        return MathHelper.wrapDegrees(n);
    }
    
    public static Vec3d roundVec(final Vec3d vec3d, final int n) {
        return new Vec3d(round(vec3d.x, n), round(vec3d.y, n), round(vec3d.z, n));
    }
    
    public static float clamp(final float a, final float n, final float b) {
        return (a < n) ? n : Math.min(a, b);
    }
    
    public static float sin(final float n) {
        return MathHelper.sin(n);
    }
    
    public static double round(final double val, final int newScale) {
        if (newScale < 0) {
            throw new IllegalArgumentException();
        }
        return BigDecimal.valueOf(val).setScale(newScale, RoundingMode.FLOOR).doubleValue();
    }
    
    public static double getIncremental(final double n, final double n2) {
        final double n3 = 1.0 / n2;
        return Math.round(n * n3) / n3;
    }
    
    public static float getRandom(final float n, final float n2) {
        return MathHelper.clamp(n + MathUtil.random.nextFloat() * n2, n, n2);
    }
    
    static {
        mc = Minecraft.getMinecraft();
        random = new Random();
    }
    
    public static boolean areVec3dsAligned(final Vec3d vec3d, final Vec3d vec3d2) {
        return areVec3dsAlignedRetarded(vec3d, vec3d2);
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float n) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n);
    }
    
    public static List<Vec3d> getBlockBlocks(final Entity entity) {
        final ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        final AxisAlignedBB getEntityBoundingBox = entity.getEntityBoundingBox();
        final double posY = entity.posY;
        final double round = round(getEntityBoundingBox.minX, 0);
        final double round2 = round(getEntityBoundingBox.minZ, 0);
        final double round3 = round(getEntityBoundingBox.maxX, 0);
        final double round4 = round(getEntityBoundingBox.maxZ, 0);
        if (round != round3) {
            list.add(new Vec3d(round, posY, round2));
            list.add(new Vec3d(round3, posY, round2));
            if (round2 != round4) {
                list.add(new Vec3d(round, posY, round4));
                list.add(new Vec3d(round3, posY, round4));
                return list;
            }
        }
        else if (round2 != round4) {
            list.add(new Vec3d(round, posY, round2));
            list.add(new Vec3d(round, posY, round4));
            return list;
        }
        list.add(entity.getPositionVector());
        return list;
    }
    
    public static double[] directionSpeed(final double n) {
        float moveForward = MathUtil.mc.player.movementInput.moveForward;
        float moveStrafe = MathUtil.mc.player.movementInput.moveStrafe;
        float n2 = MathUtil.mc.player.prevRotationYaw + (MathUtil.mc.player.rotationYaw - MathUtil.mc.player.prevRotationYaw) * MathUtil.mc.getRenderPartialTicks();
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
    
    public static float wrap(final float n) {
        float n2 = n % 360.0f;
        if (n2 >= 180.0f) {
            n2 -= 360.0f;
        }
        if (n2 < -180.0f) {
            n2 += 360.0f;
        }
        return n2;
    }
}
