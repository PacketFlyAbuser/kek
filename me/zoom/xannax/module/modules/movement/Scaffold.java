//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import me.zoom.xannax.util.EntityUtil;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import me.zoom.xannax.module.Module;

public class Scaffold extends Module
{
    private /* synthetic */ List blackList;
    private /* synthetic */ int future;
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
    }
    
    @Override
    public void onUpdate() {
        if (this.isEnabled() && Scaffold.mc.player != null) {
            BlockPos down = new BlockPos(EntityUtil.getInterpolatedPos((Entity)Scaffold.mc.player, (float)this.future)).down();
            final BlockPos down2 = down.down();
            if (Wrapper.getWorld().getBlockState(down).getMaterial().isReplaceable()) {
                int currentItem = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack getStackInSlot = Wrapper.getPlayer().inventory.getStackInSlot(i);
                    if (getStackInSlot != ItemStack.EMPTY && getStackInSlot.getItem() instanceof ItemBlock) {
                        final Block getBlock = ((ItemBlock)getStackInSlot.getItem()).getBlock();
                        if (!this.blackList.contains(getBlock) && !(getBlock instanceof BlockContainer) && Block.getBlockFromItem(getStackInSlot.getItem()).getDefaultState().isFullBlock() && (!(((ItemBlock)getStackInSlot.getItem()).getBlock() instanceof BlockFalling) || !Wrapper.getWorld().getBlockState(down2).getMaterial().isReplaceable())) {
                            currentItem = i;
                            break;
                        }
                    }
                }
                if (currentItem != -1) {
                    final int currentItem2 = Wrapper.getPlayer().inventory.currentItem;
                    Wrapper.getPlayer().inventory.currentItem = currentItem;
                    Label_0306: {
                        if (!this.hasNeighbour(down)) {
                            final EnumFacing[] values = EnumFacing.values();
                            for (int length = values.length, j = 0; j < length; ++j) {
                                final BlockPos offset = down.offset(values[j]);
                                if (this.hasNeighbour(offset)) {
                                    down = offset;
                                    break Label_0306;
                                }
                            }
                            return;
                        }
                    }
                    placeBlockScaffold(down);
                    Wrapper.getPlayer().inventory.currentItem = currentItem2;
                }
            }
        }
    }
    
    public static boolean canBeClicked(final BlockPos blockPos) {
        return getBlock(blockPos).canCollideCheck(getState(blockPos), false);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec3d) {
        final float[] neededRotations2 = getNeededRotations2(vec3d);
        Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketPlayer.Rotation(neededRotations2[0], neededRotations2[1], Wrapper.getPlayer().onGround));
    }
    
    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }
    
    public static boolean placeBlockScaffold(final BlockPos blockPos) {
        final Vec3d vec3d = new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            final EnumFacing getOpposite = enumFacing.getOpposite();
            if (vec3d.squareDistanceTo(new Vec3d((Vec3i)blockPos).add(0.5, 0.5, 0.5)) < vec3d.squareDistanceTo(new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5)) && canBeClicked(offset)) {
                final Vec3d add = new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5).add(new Vec3d(getOpposite.getDirectionVec()).scale(0.5));
                if (vec3d.squareDistanceTo(add) <= 18.0625) {
                    faceVectorPacketInstant(add);
                    processRightClickBlock(offset, getOpposite, add);
                    Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                    Scaffold.mc.rightClickDelayTimer = 4;
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void processRightClickBlock(final BlockPos blockPos, final EnumFacing enumFacing, final Vec3d vec3d) {
        getPlayerController().processRightClickBlock(Wrapper.getPlayer(), Scaffold.mc.world, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
    }
    
    public static IBlockState getState(final BlockPos blockPos) {
        return Wrapper.getWorld().getBlockState(blockPos);
    }
    
    private boolean hasNeighbour(final BlockPos blockPos) {
        final EnumFacing[] values = EnumFacing.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            if (!Wrapper.getWorld().getBlockState(blockPos.offset(values[i])).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }
    
    private static float[] getNeededRotations2(final Vec3d vec3d) {
        final Vec3d eyesPos = getEyesPos();
        final double x = vec3d.x - eyesPos.x;
        final double y = vec3d.y - eyesPos.y;
        final double y2 = vec3d.z - eyesPos.z;
        return new float[] { Wrapper.getPlayer().rotationYaw + MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(y2, x)) - 90.0f - Wrapper.getPlayer().rotationYaw), Wrapper.getPlayer().rotationPitch + MathHelper.wrapDegrees((float)(-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + y2 * y2)))) - Wrapper.getPlayer().rotationPitch) };
    }
    
    public static Block getBlock(final BlockPos blockPos) {
        return getState(blockPos).getBlock();
    }
    
    public Scaffold() {
        super("Scaffold", "Scaffold", Category.Movement);
        this.future = 3;
        this.blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST);
    }
}
