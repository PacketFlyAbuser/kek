//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSword;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class DamageUtils
{
    public static final /* synthetic */ Minecraft mc;
    
    public static float calculateDamage(final BlockPos blockPos, final Entity entity) {
        return calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity);
    }
    
    public static boolean canTakeDamage(final boolean b) {
        return !DamageUtils.mc.player.capabilities.isCreativeMode && !b;
    }
    
    public static boolean canBreakWeakness(final EntityPlayer entityPlayer) {
        int getAmplifier = 0;
        final PotionEffect getActivePotionEffect = DamageUtils.mc.player.getActivePotionEffect(MobEffects.STRENGTH);
        if (getActivePotionEffect != null) {
            getAmplifier = getActivePotionEffect.getAmplifier();
        }
        return !DamageUtils.mc.player.isPotionActive(MobEffects.WEAKNESS) || getAmplifier >= 1 || DamageUtils.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || DamageUtils.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || DamageUtils.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || DamageUtils.mc.player.getHeldItemMainhand().getItem() instanceof ItemSpade;
    }
    
    public static boolean hasDurability(final ItemStack itemStack) {
        final Item getItem = itemStack.getItem();
        return getItem instanceof ItemArmor || getItem instanceof ItemSword || getItem instanceof ItemTool || getItem instanceof ItemShield;
    }
    
    public static float getDamageMultiplied(final float n) {
        final int getId = DamageUtils.mc.world.getDifficulty().getId();
        return n * ((getId == 0) ? 0.0f : ((getId == 2) ? 1.0f : ((getId == 1) ? 0.5f : 1.5f)));
    }
    
    public static boolean isArmorLow(final EntityPlayer entityPlayer, final int n) {
        for (final ItemStack itemStack : entityPlayer.inventory.armorInventory) {
            if (itemStack == null) {
                return true;
            }
            if (getItemDamage(itemStack) < n) {
                return true;
            }
        }
        return false;
    }
    
    public static float getDamageInPercent(final ItemStack itemStack) {
        return getItemDamage(itemStack) / (float)itemStack.getMaxDamage() * 100.0f;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static int getItemDamage(final ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }
    
    public static float calculateDamage(final Entity entity, final Entity entity2) {
        return calculateDamage(entity.posX, entity.posY, entity.posZ, entity2);
    }
    
    public static float calculateDamage(final double n, final double n2, final double n3, final Entity entity) {
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
            n8 = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(n7), new Explosion((World)DamageUtils.mc.world, (Entity)null, n, n2, n3, 6.0f, false, true));
        }
        return (float)n8;
    }
    
    public static int getCooldownByWeapon(final EntityPlayer entityPlayer) {
        final Item getItem = entityPlayer.getHeldItemMainhand().getItem();
        if (getItem instanceof ItemSword) {
            return 600;
        }
        if (getItem instanceof ItemPickaxe) {
            return 850;
        }
        if (getItem == Items.IRON_AXE) {
            return 1100;
        }
        if (getItem == Items.STONE_HOE) {
            return 500;
        }
        if (getItem == Items.IRON_HOE) {
            return 350;
        }
        if (getItem == Items.WOODEN_AXE || getItem == Items.STONE_AXE) {
            return 1250;
        }
        if (getItem instanceof ItemSpade || getItem == Items.GOLDEN_AXE || getItem == Items.DIAMOND_AXE || getItem == Items.WOODEN_HOE || getItem == Items.GOLDEN_HOE) {
            return 1000;
        }
        return 250;
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
    
    public static int getRoundedDamage(final ItemStack itemStack) {
        return (int)getDamageInPercent(itemStack);
    }
}
