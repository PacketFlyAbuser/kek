//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import me.zoom.xannax.module.Module;

public class AutoArmor extends Module
{
    @Override
    public void onUpdate() {
        if (AutoArmor.mc.player.ticksExisted % 2 == 0) {
            return;
        }
        if (AutoArmor.mc.currentScreen instanceof GuiContainer && !(AutoArmor.mc.currentScreen instanceof InventoryEffectRenderer)) {
            return;
        }
        final int[] array = new int[4];
        final int[] array2 = new int[4];
        for (int i = 0; i < 4; ++i) {
            final ItemStack armorItemInSlot = AutoArmor.mc.player.inventory.armorItemInSlot(i);
            if (armorItemInSlot != null && armorItemInSlot.getItem() instanceof ItemArmor) {
                array2[i] = ((ItemArmor)armorItemInSlot.getItem()).damageReduceAmount;
            }
            array[i] = -1;
        }
        for (int j = 0; j < 36; ++j) {
            final ItemStack getStackInSlot = AutoArmor.mc.player.inventory.getStackInSlot(j);
            if (getStackInSlot.getCount() <= 1) {
                if (getStackInSlot != null) {
                    if (getStackInSlot.getItem() instanceof ItemArmor) {
                        final ItemArmor itemArmor = (ItemArmor)getStackInSlot.getItem();
                        final int n = itemArmor.armorType.ordinal() - 2;
                        if (n != 2 || !AutoArmor.mc.player.inventory.armorItemInSlot(n).getItem().equals(Items.ELYTRA)) {
                            final int damageReduceAmount = itemArmor.damageReduceAmount;
                            if (damageReduceAmount > array2[n]) {
                                array[n] = j;
                                array2[n] = damageReduceAmount;
                            }
                        }
                    }
                }
            }
        }
        for (int k = 0; k < 4; ++k) {
            int n2 = array[k];
            if (n2 != -1) {
                final ItemStack armorItemInSlot2 = AutoArmor.mc.player.inventory.armorItemInSlot(k);
                if (armorItemInSlot2 == null || armorItemInSlot2 != ItemStack.EMPTY || AutoArmor.mc.player.inventory.getFirstEmptyStack() != -1) {
                    if (n2 < 9) {
                        n2 += 36;
                    }
                    AutoArmor.mc.playerController.windowClick(0, 8 - k, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.player);
                    AutoArmor.mc.playerController.windowClick(0, n2, 0, ClickType.QUICK_MOVE, (EntityPlayer)AutoArmor.mc.player);
                    break;
                }
            }
        }
    }
    
    public AutoArmor() {
        super("AutoArmor", "AutoArmor", Category.Combat);
    }
}
