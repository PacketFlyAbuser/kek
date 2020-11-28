//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.util.BlockUtils;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class SelfTrap extends Module
{
    private /* synthetic */ Setting.Integer blocksPerTick;
    private /* synthetic */ boolean isSneaking;
    private /* synthetic */ int delayStep;
    private /* synthetic */ int lastHotbarSlot;
    private /* synthetic */ int playerHotbarSlot;
    private /* synthetic */ String lastTickTargetName;
    private /* synthetic */ EntityPlayer closestTarget;
    private /* synthetic */ boolean firstRun;
    private /* synthetic */ int offsetStep;
    private /* synthetic */ Setting.Integer tickDelay;
    /* synthetic */ Setting.Mode mode;
    private /* synthetic */ Setting.Boolean rotate;
    
    public SelfTrap() {
        super("SelfTrap", "SelfTrap", Category.Combat);
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.delayStep = 0;
        this.isSneaking = false;
        this.offsetStep = 0;
    }
    
    private void findClosestTarget() {
        final List playerEntities = SelfTrap.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer closestTarget : playerEntities) {
            if (closestTarget == SelfTrap.mc.player) {
                this.closestTarget = closestTarget;
            }
        }
    }
    
    private boolean placeBlockInRange(final BlockPos blockPos) {
        final Block getBlock = SelfTrap.mc.world.getBlockState(blockPos).getBlock();
        if (!(getBlock instanceof BlockAir) && !(getBlock instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : SelfTrap.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos))) {
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
        final Block getBlock2 = SelfTrap.mc.world.getBlockState(offset).getBlock();
        final int obiInHotbar = this.findObiInHotbar();
        if (obiInHotbar == -1) {
            this.disable();
        }
        if (this.lastHotbarSlot != obiInHotbar) {
            SelfTrap.mc.player.inventory.currentItem = obiInHotbar;
            this.lastHotbarSlot = obiInHotbar;
        }
        if ((!this.isSneaking && BlockUtils.blackList.contains(getBlock2)) || BlockUtils.shulkerList.contains(getBlock2)) {
            SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockUtils.faceVectorPacketInstant(add);
        }
        SelfTrap.mc.playerController.processRightClickBlock(SelfTrap.mc.player, SelfTrap.mc.world, offset, getOpposite, add, EnumHand.MAIN_HAND);
        SelfTrap.mc.player.swingArm(EnumHand.MAIN_HAND);
        SelfTrap.mc.rightClickDelayTimer = 4;
        return true;
    }
    
    @Override
    protected void onDisable() {
        if (SelfTrap.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            SelfTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        if (SelfTrap.mc.player == null) {
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
        final ArrayList<Object> c = new ArrayList<Object>();
        if (this.mode.getValue().equalsIgnoreCase("Normal")) {
            Collections.addAll(c, Offsets.TRAP);
        }
        if (this.mode.getValue().equalsIgnoreCase("NoStep")) {
            Collections.addAll(c, Offsets.TRAPFULLROOF);
        }
        if (this.mode.getValue().equalsIgnoreCase("Simple")) {
            Collections.addAll(c, Offsets.TRAPSIMPLE);
        }
        int i = 0;
        while (i < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= c.size()) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos blockPos = new BlockPos((Vec3d)c.get(this.offsetStep));
            if (this.placeBlockInRange(new BlockPos(this.closestTarget.getPositionVector()).down().add(blockPos.getX(), blockPos.getY(), blockPos.getZ()))) {
                ++i;
            }
            ++this.offsetStep;
        }
        if (i > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                SelfTrap.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                SelfTrap.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)SelfTrap.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
    }
    
    private int findObiInHotbar() {
        int n = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack getStackInSlot = SelfTrap.mc.player.inventory.getStackInSlot(i);
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
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Normal");
        list.add("NoStep");
        list.add("Simple");
        this.mode = this.registerMode("Mode", "Mode", list, "Normal");
        this.rotate = this.registerBoolean("Rotate", "Rotate", true);
        this.blocksPerTick = this.registerInteger("Blocks Per Tick", "BlocksPerTick", 5, 0, 10);
        this.tickDelay = this.registerInteger("Delay", "Delay", 0, 0, 10);
    }
    
    @Override
    protected void onEnable() {
        if (SelfTrap.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = SelfTrap.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    private static class Offsets
    {
        private static final /* synthetic */ Vec3d[] TRAP;
        private static final /* synthetic */ Vec3d[] TRAPFULLROOF;
        private static final /* synthetic */ Vec3d[] TRAPSIMPLE;
        
        static {
            TRAP = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0) };
            TRAPFULLROOF = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(0.0, 4.0, 0.0) };
            TRAPSIMPLE = new Vec3d[] { new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0) };
        }
    }
}
