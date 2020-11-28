//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.init.Items;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoMend extends Module
{
    /* synthetic */ Setting.Integer ThrowDelay;
    /* synthetic */ int delay;
    private final /* synthetic */ BlockPos[] surroundOffset;
    
    private boolean isSafe() {
        boolean b = true;
        final BlockPos playerPos = getPlayerPos();
        final BlockPos[] surroundOffset = this.surroundOffset;
        for (int length = surroundOffset.length, i = 0; i < length; ++i) {
            final Block getBlock = AutoMend.mc.world.getBlockState(playerPos.add((Vec3i)surroundOffset[i])).getBlock();
            if (getBlock != Blocks.BEDROCK && getBlock != Blocks.OBSIDIAN && getBlock != Blocks.ENDER_CHEST && getBlock != Blocks.ANVIL) {
                b = false;
                break;
            }
        }
        return b;
    }
    
    @Override
    public void onUpdate() {
        final int armorDurability = this.getArmorDurability();
        final boolean safe = this.isSafe();
        final boolean moduleEnabled = ModuleManager.isModuleEnabled("AutoCrystal");
        AutoMend.mc.player.getPosition();
        if (!moduleEnabled && AutoMend.mc.player.isSneaking() && safe && 0 < armorDurability) {
            ++this.delay;
            if (this.delay % this.ThrowDelay.getValue() == 0) {
                AutoMend.mc.player.inventory.currentItem = this.findExpInHotbar();
                AutoMend.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0f, 90.0f, true));
                AutoMend.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            }
        }
        else {
            this.delay = 0;
        }
        super.onUpdate();
    }
    
    @Override
    public void setup() {
        this.ThrowDelay = this.registerInteger("Throw Delay", "throwDelay", 2, 1, 10);
    }
    
    private int getArmorDurability() {
        int n = 0;
        final Iterator iterator = AutoMend.mc.player.inventory.armorInventory.iterator();
        while (iterator.hasNext()) {
            n += iterator.next().getItemDamage();
        }
        return n;
    }
    
    private static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoMend.mc.player.posX), Math.floor(AutoMend.mc.player.posY), Math.floor(AutoMend.mc.player.posZ));
    }
    
    private int findExpInHotbar() {
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            if (AutoMend.mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                n = i;
                break;
            }
        }
        return n;
    }
    
    public AutoMend() {
        super("AutoMend", "AutoMend", Category.Combat);
        this.delay = 0;
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
    }
}
