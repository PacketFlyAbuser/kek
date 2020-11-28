//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.state.IBlockState;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import java.util.ArrayList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import java.util.List;

public class BlockUtils
{
    static /* synthetic */ Minecraft mc;
    
    public static EnumFacing getPlaceableSide(final BlockPos blockPos) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            if (BlockUtils.mc.world.getBlockState(offset).getBlock().canCollideCheck(BlockUtils.mc.world.getBlockState(offset), false)) {
                if (!BlockUtils.mc.world.getBlockState(offset).getMaterial().isReplaceable()) {
                    return enumFacing;
                }
            }
        }
        return null;
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec3d) {
        final float[] neededRotations2 = getNeededRotations2(vec3d);
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(neededRotations2[0], neededRotations2[1], BlockUtils.mc.player.onGround));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec3d) {
        final Vec3d eyesPos = getEyesPos();
        final double x = vec3d.x - eyesPos.x;
        final double y = vec3d.y - eyesPos.y;
        final double y2 = vec3d.z - eyesPos.z;
        return new float[] { BlockUtils.mc.player.rotationYaw + MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(y2, x)) - 90.0f - BlockUtils.mc.player.rotationYaw), BlockUtils.mc.player.rotationPitch + MathHelper.wrapDegrees((float)(-Math.toDegrees(Math.atan2(y, Math.sqrt(x * x + y2 * y2)))) - BlockUtils.mc.player.rotationPitch) };
    }
    
    static {
        BlockUtils.mc = Minecraft.getMinecraft();
        blackList = Arrays.asList(Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        BlockUtils.mc = Minecraft.getMinecraft();
    }
    
    public static boolean canBeClicked(final BlockPos blockPos) {
        return getBlock(blockPos).canCollideCheck(getState(blockPos), false);
    }
    
    public static List<BlockPos> getCircle(final BlockPos blockPos, final int n, final float n2, final boolean b) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int getX = blockPos.getX();
        final int getZ = blockPos.getZ();
        for (int n3 = getX - (int)n2; n3 <= getX + n2; ++n3) {
            for (int n4 = getZ - (int)n2; n4 <= getZ + n2; ++n4) {
                final double n5 = (getX - n3) * (getX - n3) + (getZ - n4) * (getZ - n4);
                if (n5 < n2 * n2 && (!b || n5 >= (n2 - 1.0f) * (n2 - 1.0f))) {
                    list.add(new BlockPos(n3, n, n4));
                }
            }
        }
        return list;
    }
    
    public static boolean canSeeBlock(final BlockPos blockPos) {
        return BlockUtils.mc.player != null && BlockUtils.mc.world.rayTraceBlocks(new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
    }
    
    public static Block getBlock(final BlockPos blockPos) {
        return getState(blockPos).getBlock();
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float n) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, n));
    }
    
    private static PlayerControllerMP getPlayerController() {
        return BlockUtils.mc.playerController;
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double n, final double n2, final double n3) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * n, (entity.posY - entity.lastTickPosY) * n2, (entity.posZ - entity.lastTickPosZ) * n3);
    }
    
    public static boolean isEntitiesEmpty(final BlockPos blockPos) {
        return ((List)BlockUtils.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos)).stream().filter(entity -> !(entity instanceof EntityItem)).filter(entity2 -> !(entity2 instanceof EntityXPOrb)).collect(Collectors.toList())).isEmpty();
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
    
    public static boolean rayTracePlaceCheck(final BlockPos blockPos, final boolean b, final float n) {
        return !b || BlockUtils.mc.world.rayTraceBlocks(new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)(blockPos.getY() + n), (double)blockPos.getZ()), false, true, false) == null;
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double n) {
        return getInterpolatedAmount(entity, n, n, n);
    }
    
    public static IBlockState getState(final BlockPos blockPos) {
        return BlockUtils.mc.world.getBlockState(blockPos);
    }
    
    public static float[] calcAngle(final Vec3d vec3d, final Vec3d vec3d2) {
        final double x = vec3d2.x - vec3d.x;
        final double y = (vec3d2.y - vec3d.y) * -1.0;
        final double y2 = vec3d2.z - vec3d.z;
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y2, x)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(y, MathHelper.sqrt(x * x + y2 * y2)))) };
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos blockPos, final boolean b) {
        return rayTracePlaceCheck(blockPos, b, 1.0f);
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ);
    }
    
    public static boolean placeBlockScaffold(final BlockPos blockPos, final boolean b) {
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            final EnumFacing getOpposite = enumFacing.getOpposite();
            if (canBeClicked(offset)) {
                final Vec3d add = new Vec3d((Vec3i)offset).add(0.5, 0.5, 0.5).add(new Vec3d(getOpposite.getDirectionVec()).scale(0.5));
                if (b) {
                    faceVectorPacketInstant(add);
                }
                BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                processRightClickBlock(offset, getOpposite, add);
                BlockUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
                BlockUtils.mc.rightClickDelayTimer = 0;
                BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)BlockUtils.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                return true;
            }
        }
        return false;
    }
    
    public static List getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
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
    
    public static void processRightClickBlock(final BlockPos blockPos, final EnumFacing enumFacing, final Vec3d vec3d) {
        getPlayerController().processRightClickBlock(BlockUtils.mc.player, BlockUtils.mc.world, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
    }
    
    public static void placeCrystalOnBlock(final BlockPos blockPos, final EnumHand enumHand) {
        final RayTraceResult rayTraceBlocks = BlockUtils.mc.world.rayTraceBlocks(new Vec3d(BlockUtils.mc.player.posX, BlockUtils.mc.player.posY + BlockUtils.mc.player.getEyeHeight(), BlockUtils.mc.player.posZ), new Vec3d(blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5));
        BlockUtils.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, (rayTraceBlocks == null || rayTraceBlocks.sideHit == null) ? EnumFacing.UP : rayTraceBlocks.sideHit, enumHand, 0.0f, 0.0f, 0.0f));
    }
}
