//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import net.minecraft.item.ItemBlock;
import java.util.Iterator;
import net.minecraft.init.Items;
import me.zoom.xannax.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.HashMap;
import net.minecraft.item.ItemStack;
import java.util.Map;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoReplanish extends Module
{
    /* synthetic */ Setting.Double tickDelay;
    /* synthetic */ Setting.Double threshold;
    private /* synthetic */ int delayStep;
    
    private static Map<Integer, ItemStack> getHotbar() {
        return getInventorySlots(36, 44);
    }
    
    private static Map<Integer, ItemStack> getInventory() {
        return getInventorySlots(9, 35);
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(int i, final int n) {
        final HashMap<Integer, Object> hashMap = (HashMap<Integer, Object>)new HashMap<Integer, ItemStack>();
        while (i <= n) {
            hashMap.put(i, AutoReplanish.mc.player.inventoryContainer.getInventory().get(i));
            ++i;
        }
        return (Map<Integer, ItemStack>)hashMap;
    }
    
    @Override
    public void onUpdate() {
        if (AutoReplanish.mc.player == null) {
            return;
        }
        if (AutoReplanish.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.delayStep < this.tickDelay.getValue()) {
            ++this.delayStep;
            return;
        }
        this.delayStep = 0;
        final Pair<Integer, Integer> replenishableHotbarSlot = this.findReplenishableHotbarSlot();
        if (replenishableHotbarSlot == null) {
            return;
        }
        final int intValue = replenishableHotbarSlot.getKey();
        final int intValue2 = replenishableHotbarSlot.getValue();
        AutoReplanish.mc.playerController.windowClick(0, intValue, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
        AutoReplanish.mc.playerController.windowClick(0, intValue2, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
        AutoReplanish.mc.playerController.windowClick(0, intValue, 0, ClickType.PICKUP, (EntityPlayer)AutoReplanish.mc.player);
    }
    
    @Override
    public void setup() {
        this.tickDelay = this.registerDouble("TickDelay", "AutoReplenishtickDelay", 4.0, 0.0, 20.0);
        this.threshold = this.registerDouble("Threshold", "Threshold2", 4.0, 0.0, 20.0);
    }
    
    private int findCompatibleInventorySlot(final ItemStack itemStack) {
        int intValue = -1;
        int n = 999;
        for (final Map.Entry<Integer, ItemStack> entry : getInventory().entrySet()) {
            final ItemStack itemStack2 = entry.getValue();
            if (!itemStack2.isEmpty()) {
                if (itemStack2.getItem() == Items.AIR) {
                    continue;
                }
                if (!this.isCompatibleStacks(itemStack, itemStack2)) {
                    continue;
                }
                final int getCount = ((ItemStack)AutoReplanish.mc.player.inventoryContainer.getInventory().get((int)entry.getKey())).getCount();
                if (n <= getCount) {
                    continue;
                }
                n = getCount;
                intValue = entry.getKey();
            }
        }
        return intValue;
    }
    
    private Pair<Integer, Integer> findReplenishableHotbarSlot() {
        Pair<Integer, Integer> pair = null;
        for (final Map.Entry<Integer, ItemStack> entry : getHotbar().entrySet()) {
            final ItemStack itemStack = entry.getValue();
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() == Items.AIR) {
                    continue;
                }
                if (!itemStack.isStackable()) {
                    continue;
                }
                if (itemStack.getCount() >= itemStack.getMaxStackSize()) {
                    continue;
                }
                if (itemStack.getCount() > this.threshold.getValue()) {
                    continue;
                }
                final int compatibleInventorySlot = this.findCompatibleInventorySlot(itemStack);
                if (compatibleInventorySlot == -1) {
                    continue;
                }
                pair = new Pair<Integer, Integer>(compatibleInventorySlot, entry.getKey());
            }
        }
        return pair;
    }
    
    public AutoReplanish() {
        super("Replenish", "Replenish", Category.Player);
        this.delayStep = 0;
    }
    
    private boolean isCompatibleStacks(final ItemStack itemStack, final ItemStack itemStack2) {
        if (!itemStack.getItem().equals(itemStack2.getItem())) {
            return false;
        }
        if (itemStack.getItem() instanceof ItemBlock && itemStack2.getItem() instanceof ItemBlock) {
            ((ItemBlock)itemStack.getItem()).getBlock();
            ((ItemBlock)itemStack2.getItem()).getBlock();
        }
        return itemStack.getDisplayName().equals(itemStack2.getDisplayName()) && itemStack.getItemDamage() == itemStack2.getItemDamage();
    }
}
