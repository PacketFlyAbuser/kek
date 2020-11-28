//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.util.NonNullList;
import net.minecraft.entity.player.EntityPlayer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class CrystalUtils
{
    static final /* synthetic */ Minecraft mc;
    
    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int getX = blockPos.getX();
        final int getY = blockPos.getY();
        final int getZ = blockPos.getZ();
        for (int n4 = getX - (int)n; n4 <= getX + n; ++n4) {
            for (int n5 = getZ - (int)n; n5 <= getZ + n; ++n5) {
                for (int n6 = b2 ? (getY - (int)n) : getY; n6 < (b2 ? (getY + n) : ((float)(getY + n2))); ++n6) {
                    final double n7 = (getX - n4) * (getX - n4) + (getZ - n5) * (getZ - n5) + (b2 ? ((getY - n6) * (getY - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }
    
    public static double[] calculateLookAt(final double n, final double n2, final double n3, final EntityPlayer entityPlayer) {
        final double n4 = entityPlayer.posX - n;
        final double n5 = entityPlayer.posY - n2;
        final double n6 = entityPlayer.posZ - n3;
        final double sqrt = Math.sqrt(n4 * n4 + n5 * n5 + n6 * n6);
        return new double[] { Math.atan2(n6 / sqrt, n4 / sqrt) * 180.0 / 3.141592653589793 + 90.0, Math.asin(n5 / sqrt) * 180.0 / 3.141592653589793 };
    }
    
    public static BlockPos getPlayerPos(final EntityPlayer entityPlayer) {
        return new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
    }
    
    public static float getDamageMultiplied(final float n) {
        final int getId = CrystalUtils.mc.world.getDifficulty().getId();
        return n * ((getId == 0) ? 0.0f : ((getId == 2) ? 1.0f : ((getId == 1) ? 0.5f : 1.5f)));
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static List<BlockPos> possiblePlacePositions(final float n, final boolean b, final boolean b2) {
        final NonNullList create = NonNullList.create();
        create.addAll((Collection)getSphere(getPlayerPos((EntityPlayer)CrystalUtils.mc.player), n, (int)n, false, true, 0).stream().filter(blockPos -> canPlaceCrystal(blockPos, b, b2)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)create;
    }
    
    public static float calculateDamage(final double n, final double n2, final double n3, final Entity entity) {
        if (entity == CrystalUtils.mc.player && CrystalUtils.mc.player.capabilities.isCreativeMode) {
            return 0.0f;
        }
        final double n4 = entity.getDistance(n, n2, n3) / 12.0;
        final Vec3d vec3d = new Vec3d(n, n2, n3);
        double n5 = 0.0;
        try {
            n5 = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double n6 = (1.0 - n4) * n5;
        final float n7 = (float)(int)((n6 * n6 + n6) / 2.0 * 7.0 * 12.0 + 1.0);
        double n8 = 1.0;
        if (entity instanceof EntityLivingBase) {
            n8 = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(n7), new Explosion((World)CrystalUtils.mc.world, (Entity)null, n, n2, n3, 6.0f, false, true));
        }
        return (float)n8;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos) {
        final Block getBlock = CrystalUtils.mc.world.getBlockState(blockPos).getBlock();
        if (getBlock == Blocks.OBSIDIAN || getBlock == Blocks.BEDROCK) {
            final Block getBlock2 = CrystalUtils.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock();
            final Block getBlock3 = CrystalUtils.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock();
            if (getBlock2 == Blocks.AIR && getBlock3 == Blocks.AIR && CrystalUtils.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos.add(0, 1, 0))).isEmpty() && CrystalUtils.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean b, final boolean b2) {
        final BlockPos add = blockPos.add(0, 1, 0);
        final BlockPos add2 = blockPos.add(0, 2, 0);
        final BlockPos add3 = blockPos.add(0, 3, 0);
        try {
            if (CrystalUtils.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && CrystalUtils.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (CrystalUtils.mc.world.getBlockState(add).getBlock() != Blocks.AIR || (CrystalUtils.mc.world.getBlockState(add2).getBlock() != Blocks.AIR && !b)) {
                return false;
            }
            if (!b2) {
                return CrystalUtils.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(add)).isEmpty() && CrystalUtils.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(add2)).isEmpty();
            }
            final Iterator iterator = CrystalUtils.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(add)).iterator();
            while (iterator.hasNext()) {
                if (!(iterator.next() instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            final Iterator iterator2 = CrystalUtils.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(add2)).iterator();
            while (iterator2.hasNext()) {
                if (!(iterator2.next() instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            final Iterator iterator3 = CrystalUtils.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(add3)).iterator();
            while (iterator3.hasNext()) {
                if (iterator3.next() instanceof EntityEnderCrystal) {
                    return false;
                }
            }
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public static float calculateDamage(final EntityEnderCrystal entityEnderCrystal, final Entity entity) {
        return calculateDamage(entityEnderCrystal.posX, entityEnderCrystal.posY, entityEnderCrystal.posZ, entity);
    }
    
    public static float getBlastReduction(final EntityLivingBase entityLivingBase, final float n, final Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
            final DamageSource causeExplosionDamage = DamageSource.causeExplosionDamage(explosion);
            final float getDamageAfterAbsorb = CombatRules.getDamageAfterAbsorb(n, (float)entityPlayer.getTotalArmorValue(), (float)entityPlayer.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int getEnchantmentModifierDamage = 0;
            try {
                getEnchantmentModifierDamage = EnchantmentHelper.getEnchantmentModifierDamage(entityPlayer.getArmorInventoryList(), causeExplosionDamage);
            }
            catch (Exception ex) {}
            float a = getDamageAfterAbsorb * (1.0f - MathHelper.clamp((float)getEnchantmentModifierDamage, 0.0f, 20.0f) / 25.0f);
            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                a -= a / 4.0f;
            }
            return Math.max(a, 0.0f);
        }
        return CombatRules.getDamageAfterAbsorb(n, (float)entityLivingBase.getTotalArmorValue(), (float)entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    }
}
