//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.init.Items;
import me.zoom.xannax.module.Module;

public class EasyPearl extends Module
{
    /* synthetic */ int oldSlot;
    /* synthetic */ int tick;
    
    @Override
    protected void onEnable() {
        this.oldSlot = EasyPearl.mc.player.inventory.currentItem;
        EasyPearl.mc.player.inventory.currentItem = this.findPearlInHotbar();
    }
    
    private int findPearlInHotbar() {
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            if (EasyPearl.mc.player.inventory.getStackInSlot(i).getItem() == Items.ENDER_PEARL) {
                n = i;
                break;
            }
        }
        return n;
    }
    
    @Override
    public void onUpdate() {
        ++this.tick;
        if (EasyPearl.mc.player.inventory.getStackInSlot(EasyPearl.mc.player.inventory.currentItem).getItem() == Items.ENDER_PEARL && this.tick == 3) {
            EasyPearl.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        else if (EasyPearl.mc.player.inventory.getStackInSlot(EasyPearl.mc.player.inventory.currentItem).getItem() != Items.ENDER_PEARL && this.tick == 3) {
            EasyPearl.mc.player.inventory.currentItem = this.findPearlInHotbar();
            EasyPearl.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
        if (EasyPearl.mc.player.inventory.getStackInSlot(EasyPearl.mc.player.inventory.currentItem).getItem() == Items.ENDER_PEARL && this.tick == 6) {
            this.disable();
        }
    }
    
    @Override
    protected void onDisable() {
        EasyPearl.mc.player.inventory.currentItem = this.oldSlot;
        this.tick = 0;
    }
    
    public EasyPearl() {
        super("EasyPearl", "EasyPearl", Category.Combat);
        this.oldSlot = 0;
        this.tick = 0;
    }
}
