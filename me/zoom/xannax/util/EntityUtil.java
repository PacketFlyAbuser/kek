//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.block.BlockLiquid;
import me.zoom.xannax.util.friend.Friends;
import java.awt.Color;
import net.minecraft.world.World;
import java.util.Objects;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;

public class EntityUtil
{
    public static /* synthetic */ Minecraft mc;
    
    public static void attackEntity(final Entity entity, final boolean b, final boolean b2) {
        if (b) {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            EntityUtil.mc.playerController.attackEntity((EntityPlayer)EntityUtil.mc.player, entity);
        }
        if (b2) {
            EntityUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        else {
            EntityUtil.mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }
    
    public static boolean isAboveBlock(final Entity entity, final BlockPos blockPos) {
        return entity.posY >= blockPos.getY();
    }
    
    public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final Vec3d vec3d) {
        return getInterpolatedAmount(entity, vec3d.x, vec3d.y, vec3d.z);
    }
    
    public static boolean isAlive(final Entity entity) {
        return isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }
    
    public static boolean isLiving(final Entity entity) {
        return entity instanceof EntityLivingBase;
    }
    
    public static boolean isAboveWater(final Entity entity) {
        return isAboveWater(entity, false);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final float n) {
        return getInterpolatedAmount(entity, n, n, n);
    }
    
    public static void setTimer(final float n) {
        Minecraft.getMinecraft().timer.tickLength = 50.0f / n;
    }
    
    private static float getDamageMultiplied(final float n) {
        final int getId = EntityUtil.mc.world.getDifficulty().getId();
        return n * ((getId == 0) ? 0.0f : ((getId == 2) ? 1.0f : ((getId == 1) ? 0.5f : 1.5f)));
    }
    
    public static float getHealth(final Entity entity, final boolean b) {
        if (isLiving(entity)) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return entityLivingBase.getHealth() + (b ? entityLivingBase.getAbsorptionAmount() : 0.0f);
        }
        return 0.0f;
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float n) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, n));
    }
    
    public static boolean isMoving() {
        return EntityUtil.mc.player.moveForward != 0.0 || EntityUtil.mc.player.moveStrafing != 0.0;
    }
    
    public static Vec3d interpolateEntity(final Entity entity, final float n) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * n, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * n, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * n);
    }
    
    public static double[] calculateLookAt(final double n, final double n2, final double n3, final EntityPlayer entityPlayer) {
        final double n4 = entityPlayer.posX - n;
        final double n5 = entityPlayer.posY - n2;
        final double n6 = entityPlayer.posZ - n3;
        final double sqrt = Math.sqrt(n4 * n4 + n5 * n5 + n6 * n6);
        return new double[] { Math.atan2(n6 / sqrt, n4 / sqrt) * 180.0 / 3.141592653589793 + 90.0, Math.asin(n5 / sqrt) * 180.0 / 3.141592653589793 };
    }
    
    public static int totalHealth(final EntityPlayer entityPlayer) {
        return (int)(entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount());
    }
    
    public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5, final double n6) {
        final double n7 = n - n4;
        final double n8 = n2 - n5;
        final double n9 = n3 - n6;
        return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
    }
    
    public static float getBlastReduction(final EntityLivingBase entityLivingBase, float n, final Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
            final DamageSource causeExplosionDamage = DamageSource.causeExplosionDamage(explosion);
            n = CombatRules.getDamageAfterAbsorb(n, (float)entityPlayer.getTotalArmorValue(), (float)entityPlayer.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            n *= 1.0f - MathHelper.clamp((float)EnchantmentHelper.getEnchantmentModifierDamage(entityPlayer.getArmorInventoryList(), causeExplosionDamage), 0.0f, 20.0f) / 25.0f;
            if (entityLivingBase.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                n -= n / 4.0f;
            }
            return n;
        }
        n = CombatRules.getDamageAfterAbsorb(n, (float)entityLivingBase.getTotalArmorValue(), (float)entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return n;
    }
    
    public static float calculateDamage(final double n, final double n2, final double n3, final Entity entity) {
        final float n4 = 12.0f;
        final double n5 = (1.0 - entity.getDistance(n, n2, n3) / n4) * entity.world.getBlockDensity(new Vec3d(n, n2, n3), entity.getEntityBoundingBox());
        final float n6 = (float)(int)((n5 * n5 + n5) / 2.0 * 7.0 * n4 + 1.0);
        double n7 = 1.0;
        if (entity instanceof EntityLivingBase) {
            n7 = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(n6), new Explosion((World)EntityUtil.mc.world, (Entity)null, n, n2, n3, 6.0f, false, true));
        }
        return (float)n7;
    }
    
    public static Color getColor(final Entity entity, final int n, final int n2, final int n3, final int n4, final boolean b) {
        Color color = new Color(n / 255.0f, n2 / 255.0f, n3 / 255.0f, n4 / 255.0f);
        if (entity instanceof EntityPlayer && b && Friends.isFriend(entity.getName())) {
            color = new Color(0.33333334f, 1.0f, 1.0f, n4 / 255.0f);
        }
        return color;
    }
    
    public static Vec3d getInterpolatedRenderPos(final Vec3d vec3d) {
        return new Vec3d(vec3d.x, vec3d.y, vec3d.z).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double n, final double n2, final double n3) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * n, (entity.posY - entity.lastTickPosY) * n2, (entity.posZ - entity.lastTickPosZ) * n3);
    }
    
    public static Vec3d getInterpolatedRenderPos(final Entity entity, final float n) {
        return getInterpolatedPos(entity, n).subtract(EntityUtil.mc.getRenderManager().renderPosX, EntityUtil.mc.getRenderManager().renderPosY, EntityUtil.mc.getRenderManager().renderPosZ);
    }
    
    public static boolean isAboveLiquid(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double n = entity.posY + 0.01;
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (EntityUtil.mc.world.getBlockState(new BlockPos(i, (int)n, j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static float getHealth(final Entity entity) {
        if (isLiving(entity)) {
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }
    
    public static boolean checkForLiquid(final Entity entity, final boolean b) {
        if (entity == null) {
            return false;
        }
        final double posY = entity.posY;
        double n;
        if (b) {
            n = 0.03;
        }
        else if (entity instanceof EntityPlayer) {
            n = 0.2;
        }
        else {
            n = 0.5;
        }
        final double n2 = posY - n;
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (EntityUtil.mc.world.getBlockState(new BlockPos(i, MathHelper.floor(n2), j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isPlayer(final Entity entity) {
        return entity instanceof EntityPlayer;
    }
    
    static {
        EntityUtil.mc = Minecraft.getMinecraft();
    }
    
    public static boolean isDead(final Entity entity) {
        return !isAlive(entity);
    }
    
    public static void resetTimer() {
        Minecraft.getMinecraft().timer.tickLength = 50.0f;
    }
    
    public static boolean isPassive(final Entity entity) {
        return (!(entity instanceof EntityWolf) || !((EntityWolf)entity).isAngry()) && (entity instanceof EntityAgeable || (entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null));
    }
    
    public static boolean isAboveWater(final Entity entity, final boolean b) {
        if (entity == null) {
            return false;
        }
        final double n = entity.posY - (b ? 0.03 : (isPlayer(entity) ? 0.2 : 0.5));
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (Wrapper.getWorld().getBlockState(new BlockPos(i, MathHelper.floor(n), j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
}
