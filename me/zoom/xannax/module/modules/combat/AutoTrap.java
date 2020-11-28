//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.Collection;
import java.util.Collections;
import me.zoom.xannax.util.EntityUtil;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.util.BlockUtils;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.command.Command;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import java.util.List;
import java.util.ArrayList;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoTrap extends Module
{
    private /* synthetic */ int delayStep;
    private /* synthetic */ Setting.Integer blocksPerTick;
    private /* synthetic */ int offsetStep;
    private /* synthetic */ int lastHotbarSlot;
    private /* synthetic */ Setting.Integer range;
    private /* synthetic */ Setting.Integer tickDelay;
    /* synthetic */ Setting.Boolean toggleMsg;
    private /* synthetic */ Setting.Boolean rotate;
    private /* synthetic */ String lastTickTargetName;
    private /* synthetic */ boolean firstRun;
    /* synthetic */ Setting.Mode mode;
    private /* synthetic */ int playerHotbarSlot;
    private /* synthetic */ boolean isSneaking;
    public /* synthetic */ EntityPlayer closestTarget;
    
    private int findObiInHotbar() {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack getStackInSlot = AutoTrap.mc.player.inventory.getStackInSlot(i);
            if (getStackInSlot != ItemStack.EMPTY) {
                if (getStackInSlot.getItem() instanceof ItemBlock) {
                    if (((ItemBlock)getStackInSlot.getItem()).getBlock() instanceof BlockObsidian) {
                        n = i;
                        break;
                    }
                }
            }
        }
        return n;
    }
    
    @Override
    public String getHudInfo() {
        if (this.closestTarget != null) {
            return "[" + ChatFormatting.WHITE + this.closestTarget.getName() + ChatFormatting.GRAY + "]";
        }
        return "[" + ChatFormatting.WHITE + "No target!" + ChatFormatting.GRAY + "]";
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Normal");
        list.add("NoStep");
        this.mode = this.registerMode("Mode", "Mode", list, "Normal");
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.range = this.registerInteger("Range", "Range", 6, 0, 6);
        this.blocksPerTick = this.registerInteger("Blocks Per Tick", "BlocksPerTick", 5, 0, 10);
        this.tickDelay = this.registerInteger("Delay", "Delay", 0, 0, 10);
        this.toggleMsg = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
    
    @Override
    protected void onDisable() {
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        if (this.toggleMsg.getValue() && AutoTrap.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.RED + "AutoTrap has been toggled off!");
        }
    }
    
    private boolean placeBlockInRange(final BlockPos blockPos, final double n) {
        final Block getBlock = AutoTrap.mc.world.getBlockState(blockPos).getBlock();
        if (!(getBlock instanceof BlockAir) && !(getBlock instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : AutoTrap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing placeableSide = BlockUtils.getPlaceableSide(blockPos);
        if (placeableSide == null) {
            return false;
        }
        final BlockPos offset = blockPos.offset(placeableSide);
        final EnumFacing getOpposite = placeableSide.getOpposite();
        if (!BlockUtils.canBeClicked(offset)) {
            return false;
        }
        final Vec3d add = new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5).add(new Vec3d(getOpposite.getDirectionVec()).scale(0.5));
        final Block getBlock2 = AutoTrap.mc.world.getBlockState(offset).getBlock();
        if (AutoTrap.mc.player.getPositionVector().distanceTo(add) > n) {
            return false;
        }
        final int obiInHotbar = this.findObiInHotbar();
        if (obiInHotbar == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiInHotbar) {
            AutoTrap.mc.player.inventory.currentItem = obiInHotbar;
            this.lastHotbarSlot = obiInHotbar;
        }
        if ((!this.isSneaking && BlockUtils.blackList.contains(getBlock2)) || BlockUtils.shulkerList.contains(getBlock2)) {
            AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockUtils.faceVectorPacketInstant(add);
        }
        AutoTrap.mc.playerController.processRightClickBlock(AutoTrap.mc.player, AutoTrap.mc.world, offset, getOpposite, add, EnumHand.MAIN_HAND);
        AutoTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoTrap.mc.rightClickDelayTimer = 4;
        return true;
    }
    
    private void findClosestTarget() {
        final List playerEntities = AutoTrap.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer entityPlayer : playerEntities) {
            if (entityPlayer == AutoTrap.mc.player) {
                continue;
            }
            if (Friends.isFriend(entityPlayer.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)entityPlayer)) {
                continue;
            }
            if (entityPlayer.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = entityPlayer;
            }
            else {
                if (AutoTrap.mc.player.getDistance((Entity)entityPlayer) >= AutoTrap.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = entityPlayer;
            }
        }
    }
    
    @Override
    protected void onEnable() {
        if (AutoTrap.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = AutoTrap.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
        if (this.toggleMsg.getValue() && AutoTrap.mc.player != null) {
            Command.sendClientMessage(ChatFormatting.GREEN + "AutoTrap has been toggled on!");
        }
    }
    
    public AutoTrap() {
        super("AutoTrap", "AutoTrap", Category.Combat);
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    @Override
    public void onUpdate() {
        if (AutoTrap.mc.player == null) {
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        this.findClosestTarget();
        if (this.closestTarget == null) {
            if (this.firstRun) {
                this.firstRun = false;
            }
            return;
        }
        if (this.firstRun) {
            this.firstRun = false;
            this.lastTickTargetName = this.closestTarget.getName();
        }
        else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
            this.lastTickTargetName = this.closestTarget.getName();
            this.offsetStep = 0;
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            Collections.addAll(list, Offsets.TRAP);
        }
        if (this.mode.getValue().equalsIgnoreCase("NoStep")) {
            Collections.addAll(list, Offsets.TRAPFULLROOF);
        }
        int i = 0;
        while (i < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= list.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos blockPos = new BlockPos((Vec3d)list.get(this.offsetStep));
            if (this.placeBlockInRange(new BlockPos(this.closestTarget.getPositionVector()).down().add(blockPos.getX(), blockPos.getY(), blockPos.getZ()), this.range.getValue())) {
                ++i;
            }
            ++this.offsetStep;
        }
        if (i > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private static class Offsets
    {
        private static final /* synthetic */ Vec3d[] TRAPFULLROOF;
        private static final /* synthetic */ Vec3d[] TRAP;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
        }
    }
}
