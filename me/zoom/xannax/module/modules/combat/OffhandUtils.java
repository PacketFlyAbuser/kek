//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.ArrayList;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.inventory.GuiContainer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class OffhandUtils extends Module
{
    /* synthetic */ boolean returnI;
    /* synthetic */ int crystals;
    public /* synthetic */ int totems;
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ Item item;
    /* synthetic */ Setting.Integer health;
    /* synthetic */ boolean moving;
    
    public Item ittem() {
        Item item;
        if (this.mode.getValue().equalsIgnoreCase("Crystal")) {
            item = Items.END_CRYSTAL;
        }
        else {
            item = Items.GOLDEN_APPLE;
        }
        return item;
    }
    
    private boolean shouldTotem() {
        final boolean b = OffhandUtils.mc.player.getHealth() + OffhandUtils.mc.player.getAbsorptionAmount() <= this.health.getValue();
        final boolean b2 = !this.isCrystalsAABBEmpty();
        return b;
    }
    
    public OffhandUtils() {
        super("OffhandUtils", "OffhandUtils", Category.Combat);
        this.moving = false;
        this.returnI = false;
    }
    
    private boolean isEmpty(final BlockPos blockPos) {
        return ((List)OffhandUtils.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos)).stream().filter(entity -> entity instanceof EntityEnderCrystal).collect(Collectors.toList())).isEmpty();
    }
    
    public void onDisable() {
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (OffhandUtils.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING) {
            if (this.crystals == 0) {
                return;
            }
            int n = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                    n = i;
                    break;
                }
            }
            if (n == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, (n < 9) ? (n + 36) : n, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
        }
    }
    
    @Override
    public String getHudInfo() {
        String s;
        if (this.mode.getValue().equalsIgnoreCase("Crystal")) {
            s = "[" + ChatFormatting.WHITE + "C" + ChatFormatting.GRAY + "]";
        }
        else {
            s = "[" + ChatFormatting.WHITE + "G" + ChatFormatting.GRAY + "]";
        }
        return s;
    }
    
    @Override
    public void onUpdate() {
        this.item = this.ittem();
        if (OffhandUtils.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (this.returnI) {
            int n = -1;
            for (int i = 0; i < 45; ++i) {
                if (OffhandUtils.mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    n = i;
                    break;
                }
            }
            if (n == -1) {
                return;
            }
            OffhandUtils.mc.playerController.windowClick(0, (n < 9) ? (n + 36) : n, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            this.returnI = false;
        }
        this.totems = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        this.crystals = OffhandUtils.mc.player.inventory.mainInventory.stream().filter(itemStack2 -> itemStack2.getItem() == this.item).mapToInt(ItemStack::getCount).sum();
        if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        }
        else if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
            this.crystals += OffhandUtils.mc.player.getHeldItemOffhand().getCount();
        }
        else {
            if (this.moving) {
                OffhandUtils.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                this.moving = false;
                this.returnI = true;
                return;
            }
            if (OffhandUtils.mc.player.inventory.getItemStack().isEmpty()) {
                if (!this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == this.item) {
                    return;
                }
                if (this.shouldTotem() && OffhandUtils.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
                    return;
                }
                if (!this.shouldTotem()) {
                    if (this.crystals == 0) {
                        return;
                    }
                    int n2 = -1;
                    for (int j = 0; j < 45; ++j) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(j).getItem() == this.item) {
                            n2 = j;
                            break;
                        }
                    }
                    if (n2 == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (n2 < 9) ? (n2 + 36) : n2, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                    this.moving = true;
                }
                else {
                    if (this.totems == 0) {
                        return;
                    }
                    int n3 = -1;
                    for (int k = 0; k < 45; ++k) {
                        if (OffhandUtils.mc.player.inventory.getStackInSlot(k).getItem() == Items.TOTEM_OF_UNDYING) {
                            n3 = k;
                            break;
                        }
                    }
                    if (n3 == -1) {
                        return;
                    }
                    OffhandUtils.mc.playerController.windowClick(0, (n3 < 9) ? (n3 + 36) : n3, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
                    this.moving = true;
                }
            }
            else {
                int n4 = -1;
                for (int l = 0; l < 45; ++l) {
                    if (OffhandUtils.mc.player.inventory.getStackInSlot(l).isEmpty()) {
                        n4 = l;
                        break;
                    }
                }
                if (n4 == -1) {
                    return;
                }
                OffhandUtils.mc.playerController.windowClick(0, (n4 < 9) ? (n4 + 36) : n4, 0, ClickType.PICKUP, (EntityPlayer)OffhandUtils.mc.player);
            }
        }
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Gapple");
        list.add("Crystal");
        this.mode = this.registerMode("Mode", "ModeOH", list, "Crystal");
        this.health = this.registerInteger("Health", "Health", 15, 0, 36);
    }
    
    private boolean isCrystalsAABBEmpty() {
        return this.isEmpty(OffhandUtils.mc.player.getPosition().add(1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(-1, 0, 0)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, 1)) && this.isEmpty(OffhandUtils.mc.player.getPosition().add(0, 0, -1)) && this.isEmpty(OffhandUtils.mc.player.getPosition());
    }
}
