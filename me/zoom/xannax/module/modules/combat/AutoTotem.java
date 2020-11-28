//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.function.ToIntFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoTotem extends Module
{
    /* synthetic */ Setting.Integer TotemHealth;
    /* synthetic */ Setting.Mode OffhandItem;
    /* synthetic */ boolean returnI;
    /* synthetic */ boolean moving;
    /* synthetic */ Setting.Boolean soft;
    /* synthetic */ Setting.Boolean AuraGap;
    /* synthetic */ Setting.Boolean CrystalAura;
    /* synthetic */ int totems;
    
    public Item getItem() {
        Item item = Items.TOTEM_OF_UNDYING;
        final boolean b = false;
        if (AutoTotem.mc.player.getHealth() < this.TotemHealth.getValue()) {
            item = Items.TOTEM_OF_UNDYING;
        }
        else if (this.CrystalAura.getValue() && ModuleManager.isModuleEnabled("AutoCrystal")) {
            item = Items.END_CRYSTAL;
        }
        else if (this.AuraGap.getValue() && ModuleManager.isModuleEnabled("KillAura") && !b) {
            item = Items.GOLDEN_APPLE;
        }
        else if (this.OffhandItem.getValue() == "totem") {
            item = Items.TOTEM_OF_UNDYING;
            final Item TOTEM_OF_UNDYING = Items.TOTEM_OF_UNDYING;
        }
        else if (this.OffhandItem.getValue() == "crystal") {
            item = Items.END_CRYSTAL;
            final Item END_CRYSTAL = Items.END_CRYSTAL;
        }
        else if (this.OffhandItem.getValue() == "golden apple") {
            item = Items.GOLDEN_APPLE;
            final Item GOLDEN_APPLE = Items.GOLDEN_APPLE;
        }
        return item;
    }
    
    @Override
    public void setup() {
        this.soft = this.registerBoolean("Soft", "Soft", true);
        this.TotemHealth = this.registerInteger("Health", "health", 10, 1, 20);
        this.CrystalAura = this.registerBoolean("Crystal on CA", "crystalaura", false);
        this.AuraGap = this.registerBoolean("Gap on Aura", "auraGap", true);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("totem");
        list.add("bed");
        list.add("crystal");
        list.add("golden apple");
        this.OffhandItem = this.registerMode("Item", "item", list, "totem");
    }
    
    public AutoTotem() {
        super("AutoTotem", "AutoTotem", Category.Combat);
        this.moving = false;
        this.returnI = false;
    }
    
    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.OffhandItem.getValue() + ChatFormatting.GRAY + "]";
    }
    
    @Override
    public void onUpdate() {
        final Item item = this.getItem();
        if (AutoTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int n = -1;
            for (int i = 0; i < 45; ++i) {
                if (AutoTotem.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    n = i;
                    break;
                }
            }
            if (n == -1) {
                return;
            }
            AutoTotem.mc.playerController.windowClick(0, (n < 9) ? (n + 36) : n, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            this.returnI = false;
        }
        this.totems = AutoTotem.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == item).mapToInt(ItemStack::getCount).sum();
        if (AutoTotem.mc.player.getHeldItemOffhand().getItem() == item) {
            ++this.totems;
        }
        else {
            if (this.soft.getValue() && !AutoTotem.mc.player.getHeldItemOffhand().isEmpty()) {
                return;
            }
            if (this.moving) {
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.moving = false;
                if (!AutoTotem.mc.player.inventory.getItemStack().isEmpty()) {
                    this.returnI = true;
                }
                return;
            }
            if (AutoTotem.mc.player.inventory.getItemStack().isEmpty()) {
                if (this.totems == 0) {
                    return;
                }
                int n2 = -1;
                for (int j = 0; j < 45; ++j) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(j).getItem() == item) {
                        n2 = j;
                        break;
                    }
                }
                if (n2 == -1) {
                    return;
                }
                AutoTotem.mc.playerController.windowClick(0, (n2 < 9) ? (n2 + 36) : n2, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
                this.moving = true;
            }
            else if (!this.soft.getValue()) {
                int n3 = -1;
                for (int k = 0; k < 45; ++k) {
                    if (AutoTotem.mc.player.inventory.getStackInSlot(k).isEmpty()) {
                        n3 = k;
                        break;
                    }
                }
                if (n3 == -1) {
                    return;
                }
                AutoTotem.mc.playerController.windowClick(0, (n3 < 9) ? (n3 + 36) : n3, 0, ClickType.PICKUP, (EntityPlayer)AutoTotem.mc.player);
            }
        }
    }
}
