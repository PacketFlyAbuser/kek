//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.Vec3i;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.block.state.IBlockState;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import java.util.ArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraft.client.Minecraft;

public class BlockInteractionHelper
{
    private static final /* synthetic */ Minecraft mc;
    
    private static Vec3d getEyesPos() {
        return new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
    }
    
    public static boolean hotbarSlotCheckEmpty(final ItemStack itemStack) {
        return itemStack != ItemStack.EMPTY;
    }
    
    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int getX = blockPos.getX();
        final int getY = blockPos.getY();
        final int getZ = blockPos.getZ();
        for (int n4 = getX - (int)n; n4 <= getX + n; ++n4) {
            for (int n5 = getZ - (int)n; n5 <= getZ + n; ++n5) {
                for (int n6 = b2 ? (getY - (int)n) : getY; n6 < (b2 ? (getY + n) : ((float)(getY + n2))); ++n6) {
                    final double n7 = (getX - n4) * (getX - n4) + (getZ - n5) * (getZ - n5) + (b2 ? ((getY - n6) * (getY - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }
    
    private static float[] getLegitRotations(final Vec3d vec3d) {
        final Vec3d eyesPos = getEyesPos();
        final double x = vec3d.x - eyesPos.x;
        final double y = vec3d.y - eyesPos.y;
        final double y2 = vec3d.z - eyesPos.z;
        return new float[] { Wrapper.getPlayer().rotationYaw + MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(y2, x)) - 90.0f - Wrapper.getPlayer().rotationYaw), Wrapper.getPlayer().rotationPitch + MathHelper.wrapDegrees((float)(-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + y2 * y2)))) - Wrapper.getPlayer().rotationPitch) };
    }
    
    private static void processRightClickBlock(final BlockPos blockPos, final EnumFacing enumFacing, final Vec3d vec3d) {
        getPlayerController().processRightClickBlock(Wrapper.getPlayer(), BlockInteractionHelper.mc.world, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
    }
    
    public static EnumFacing getPlaceableSide(final BlockPos blockPos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            if (BlockInteractionHelper.mc.world.getBlockState(offset).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.getBlockState(offset), false)) {
                if (!BlockInteractionHelper.mc.world.getBlockState(offset).getMaterial().isReplaceable()) {
                    return enumFacing;
                }
            }
        }
        return null;
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec3d) {
        final float[] legitRotations = getLegitRotations(vec3d);
        Wrapper.getPlayer().connection.sendPacket((Packet)new CPacketPlayer.Rotation(legitRotations[0], legitRotations[1], Wrapper.getPlayer().onGround));
    }
    
    public static boolean blockCheckNonBlock(final ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlock;
    }
    
    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        mc = Minecraft.getMinecraft();
    }
    
    public static boolean checkForNeighbours(final BlockPos blockPos) {
        if (!hasNeighbour(blockPos)) {
            final EnumFacing[] values = EnumFacing.values();
            for (int length = values.length, i = 0; i < length; ++i) {
                if (hasNeighbour(blockPos.offset(values[i]))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private static Block getBlock(final BlockPos blockPos) {
        return getState(blockPos).getBlock();
    }
    
    private static boolean hasNeighbour(final BlockPos blockPos) {
        final EnumFacing[] values = EnumFacing.values();
        for (int length = values.length, i = 0; i < length; ++i) {
            if (!Wrapper.getWorld().getBlockState(blockPos.offset(values[i])).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }
    
    private static IBlockState getState(final BlockPos blockPos) {
        return Wrapper.getWorld().getBlockState(blockPos);
    }
    
    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }
    
    public static boolean canBeClicked(final BlockPos blockPos) {
        return getBlock(blockPos).canCollideCheck(getState(blockPos), false);
    }
    
    public static void placeBlockScaffold(final BlockPos blockPos) {
        final Vec3d vec3d = new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            final EnumFacing getOpposite = enumFacing.getOpposite();
            if (canBeClicked(offset)) {
                final Vec3d add = new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5).add(new Vec3d(getOpposite.getDirectionVec()).rotatePitch(0.5f));
                if (vec3d.squareDistanceTo(add) <= 18.0625) {
                    faceVectorPacketInstant(add);
                    processRightClickBlock(offset, getOpposite, add);
                    Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                    BlockInteractionHelper.mc.rightClickDelayTimer = 4;
                    return;
                }
            }
        }
    }
    
    public static float[] calcAngle(final Vec3d vec3d, final Vec3d vec3d2) {
        final double x = vec3d2.x - vec3d.x;
        final double y = (vec3d2.y - vec3d.y) * -1.0;
        final double y2 = vec3d2.z - vec3d.z;
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y2, x)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y, MathHelper.sqrt(x * x + y2 * y2)))) };
    }
}
