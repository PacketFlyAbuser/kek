//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.Iterator;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;
import me.zoom.xannax.util.BlockInteractionHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Surround extends Module
{
    private /* synthetic */ Setting.Boolean triggerable;
    private /* synthetic */ boolean isSneaking;
    private /* synthetic */ Setting.Integer timeoutTicks;
    private /* synthetic */ int playerHotbarSlot;
    private final /* synthetic */ Vec3d[] surroundTargets;
    private /* synthetic */ Setting.Boolean hybrid;
    private /* synthetic */ Setting.Boolean announceUsage;
    private /* synthetic */ int offsetStep;
    private /* synthetic */ int yHeight;
    private /* synthetic */ Setting.Integer blocksPerTick;
    private /* synthetic */ int totalTickRuns;
    private /* synthetic */ int lastHotbarSlot;
    private /* synthetic */ boolean flag;
    private /* synthetic */ Setting.Boolean rotate;
    
    @Override
    protected void onEnable() {
        this.flag = false;
        if (Surround.mc.player == null) {
            this.disable();
            return;
        }
        this.playerHotbarSlot = Wrapper.getPlayer().inventory.currentItem;
        this.lastHotbarSlot = -1;
        this.yHeight = (int)Math.round(Surround.mc.player.posY);
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.GREEN + "Surround has been toggled on!");
        }
    }
    
    @Override
    protected void onDisable() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "Surround has been toggled off!");
        }
    }
    
    private int findObiInHotbar() {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack getStackInSlot = Wrapper.getPlayer().inventory.getStackInSlot(i);
            if (getStackInSlot != ItemStack.EMPTY && getStackInSlot.getItem() instanceof ItemBlock && ((ItemBlock)getStackInSlot.getItem()).getBlock() instanceof BlockObsidian) {
                n = i;
                break;
            }
        }
        return n;
    }
    
    public Surround() {
        super("Surround", "Surround", Category.Combat);
        this.surroundTargets = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.offsetStep = 0;
        this.totalTickRuns = 0;
        this.isSneaking = false;
        this.flag = false;
    }
    
    @Override
    public void setup() {
        this.triggerable = this.registerBoolean("Triggerable", "Triggerable", true);
        this.timeoutTicks = this.registerInteger("TimeoutTicks", "TimeoutTicks", 20, 1, 100);
        this.blocksPerTick = this.registerInteger("Blocks per Tick", "BlocksperTick", 4, 1, 9);
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.hybrid = this.registerBoolean("Hybrid", "Hybrid", true);
        this.announceUsage = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    private boolean placeBlock(final BlockPos blockPos) {
        if (!Surround.mc.world.getBlockState(blockPos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return false;
        }
        final EnumFacing[] values = EnumFacing.values();
        final int length = values.length;
        int i = 0;
        while (i < length) {
            final EnumFacing enumFacing = values[i];
            final BlockPos offset = blockPos.offset(enumFacing);
            final EnumFacing getOpposite = enumFacing.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(offset)) {
                ++i;
            }
            else {
                final int obiInHotbar = this.findObiInHotbar();
                if (obiInHotbar == -1) {
                    this.disable();
                    return false;
                }
                if (this.lastHotbarSlot != obiInHotbar) {
                    Wrapper.getPlayer().inventory.currentItem = obiInHotbar;
                    this.lastHotbarSlot = obiInHotbar;
                }
                if (BlockInteractionHelper.blackList.contains(Surround.mc.world.getBlockState(offset).getBlock())) {
                    Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.isSneaking = true;
                }
                final Vec3d add = new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5).add(new Vec3d(getOpposite.getDirectionVec()).scale(0.5));
                if (this.rotate.getValue()) {
                    BlockInteractionHelper.faceVectorPacketInstant(add);
                }
                Surround.mc.playerController.processRightClickBlock(Surround.mc.player, Surround.mc.world, offset, getOpposite, add, EnumHand.MAIN_HAND);
                Surround.mc.player.swingArm(EnumHand.MAIN_HAND);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (Surround.mc.player == null) {
            return;
        }
        if (this.hybrid.getValue() && (int)Math.round(Surround.mc.player.posY) != this.yHeight) {
            this.disable();
        }
        if (this.triggerable.getValue() && this.totalTickRuns >= this.timeoutTicks.getValue()) {
            this.totalTickRuns = 0;
            this.disable();
            return;
        }
        int i = 0;
        while (i < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= this.surroundTargets.length) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos blockPos = new BlockPos(this.surroundTargets[this.offsetStep]);
            final BlockPos add = new BlockPos(Surround.mc.player.getPositionVector()).add(blockPos.x, blockPos.y, blockPos.z);
            boolean b = true;
            if (!Wrapper.getWorld().getBlockState(add).getMaterial().isReplaceable()) {
                b = false;
            }
            for (final Entity entity : Surround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(add))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    b = false;
                    break;
                }
            }
            if (b && this.placeBlock(add)) {
                ++i;
            }
            ++this.offsetStep;
        }
        if (i > 0 && this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.playerHotbarSlot;
            this.lastHotbarSlot = this.playerHotbarSlot;
        }
        ++this.totalTickRuns;
    }
}
