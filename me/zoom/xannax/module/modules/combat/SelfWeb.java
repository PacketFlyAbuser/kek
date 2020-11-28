//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import me.zoom.xannax.setting.Setting;
import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.module.Module;

public class SelfWeb extends Module
{
    public static /* synthetic */ float pitch;
    /* synthetic */ BlockPos feet;
    private /* synthetic */ Setting.Integer delay;
    public static /* synthetic */ float yaw;
    private /* synthetic */ Setting.Boolean announceUsage;
    /* synthetic */ int d;
    
    private boolean isStackObby(final ItemStack itemStack) {
        return itemStack != null && itemStack.getItem() == Item.getItemById(30);
    }
    
    public static boolean placeBlockLegit(final BlockPos blockPos) {
        final Vec3d vec3d = new Vec3d(SelfWeb.mc.player.posX, SelfWeb.mc.player.posY + SelfWeb.mc.player.getEyeHeight(), SelfWeb.mc.player.posZ);
        final Vec3d add = new Vec3d((Vec3i)blockPos).add(0.5, 0.5, 0.5);
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            final BlockPos offset = blockPos.offset(enumFacing);
            if (canBeClicked(offset)) {
                final Vec3d add = add.add(new Vec3d(enumFacing.getDirectionVec()).scale(0.5));
                if (vec3d.squareDistanceTo(add) <= 36.0) {
                    SelfWeb.mc.playerController.processRightClickBlock(SelfWeb.mc.player, SelfWeb.mc.world, offset, enumFacing.getOpposite(), add, EnumHand.MAIN_HAND);
                    SelfWeb.mc.player.swingArm(EnumHand.MAIN_HAND);
                    try {
                        TimeUnit.MILLISECONDS.sleep(10L);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    public SelfWeb() {
        super("SelfWeb", "SelfWeb", Category.Combat);
    }
    
    public void onDisable() {
        this.d = 0;
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.RED + "SelfWeb has been toggled on!");
        }
        SelfWeb.yaw = SelfWeb.mc.player.rotationYaw;
        SelfWeb.pitch = SelfWeb.mc.player.rotationPitch;
    }
    
    public void onEnable() {
        if (SelfWeb.mc.player == null) {
            this.disable();
            return;
        }
        if (this.announceUsage.getValue()) {
            Command.sendClientMessage(ChatFormatting.GREEN + "SelfWeb has been toggled on!");
        }
        this.d = 0;
    }
    
    public boolean isInBlockRange(final Entity entity) {
        return entity.getDistance((Entity)SelfWeb.mc.player) <= 4.0f;
    }
    
    private void trap(final EntityPlayer entityPlayer) {
        if (entityPlayer.moveForward == 0.0 && entityPlayer.moveStrafing == 0.0 && entityPlayer.moveForward == 0.0) {
            ++this.d;
        }
        if (entityPlayer.moveForward != 0.0 || entityPlayer.moveStrafing != 0.0 || entityPlayer.moveForward != 0.0) {
            this.d = 0;
        }
        if (!this.doesHotbarHaveWeb()) {
            this.d = 0;
        }
        if (this.d == this.delay.getValue() && this.doesHotbarHaveWeb()) {
            this.feet = new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
            for (int i = 36; i < 45; ++i) {
                final ItemStack getStack = SelfWeb.mc.player.inventoryContainer.getSlot(i).getStack();
                if (getStack != null && this.isStackObby(getStack)) {
                    final int currentItem = SelfWeb.mc.player.inventory.currentItem;
                    if (SelfWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                        SelfWeb.mc.player.inventory.currentItem = i - 36;
                        if (SelfWeb.mc.world.getBlockState(this.feet).getMaterial().isReplaceable()) {
                            placeBlockLegit(this.feet);
                        }
                        SelfWeb.mc.player.inventory.currentItem = currentItem;
                        this.d = 0;
                        break;
                    }
                    this.d = 0;
                }
                this.d = 0;
            }
        }
    }
    
    private boolean doesHotbarHaveWeb() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack getStack = SelfWeb.mc.player.inventoryContainer.getSlot(i).getStack();
            if (getStack != null && this.isStackObby(getStack)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (SelfWeb.mc.player.isHandActive()) {
            return;
        }
        this.trap((EntityPlayer)SelfWeb.mc.player);
    }
    
    public static boolean canBeClicked(final BlockPos blockPos) {
        return SelfWeb.mc.world.getBlockState(blockPos).getBlock().canCollideCheck(SelfWeb.mc.world.getBlockState(blockPos), false);
    }
    
    public static IBlockState getState(final BlockPos blockPos) {
        return SelfWeb.mc.world.getBlockState(blockPos);
    }
    
    public static double roundToHalf(final double n) {
        return Math.round(n * 2.0) / 2.0;
    }
    
    public static Block getBlock(final BlockPos blockPos) {
        return getState(blockPos).getBlock();
    }
    
    public EnumFacing getEnumFacing(final float n, final float n2, final float n3) {
        return EnumFacing.getFacingFromVector(n, n2, n3);
    }
    
    public BlockPos getBlockPos(final double n, final double n2, final double n3) {
        return new BlockPos(n, n2, n3);
    }
    
    @Override
    public void setup() {
        final ArrayList list = new ArrayList();
        this.delay = this.registerInteger("Delay", "Delay", 3, 0, 10);
        this.announceUsage = this.registerBoolean("ToggleMSG", "ToggleMSG", true);
    }
}
