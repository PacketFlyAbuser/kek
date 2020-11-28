//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.TpsUtils;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.util.EnumHand;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Aura extends Module
{
    /* synthetic */ Setting.Boolean sword;
    /* synthetic */ boolean start_verify;
    /* synthetic */ Setting.Boolean hostile;
    /* synthetic */ double tick;
    /* synthetic */ Setting.Double range;
    /* synthetic */ Setting.Integer delay;
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ EnumHand actual_hand;
    /* synthetic */ Setting.Boolean player;
    /* synthetic */ Setting.Boolean sync_tps;
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Normal");
        list.add("A32k");
        this.mode = this.registerMode("Mode", "Mode", list, "Normal");
        this.player = this.registerBoolean("Player", "Player", true);
        this.hostile = this.registerBoolean("Hostile", "Hostile", false);
        this.sword = this.registerBoolean("Sword", "Sword", true);
        this.sync_tps = this.registerBoolean("SyncTPS", "SyncTPS", true);
        this.range = this.registerDouble("Range", "Range", 5.0, 0.5, 6.0);
        this.delay = this.registerInteger("Delay", "Delay", 2, 0, 10);
    }
    
    public boolean is_compatible(final Entity entity) {
        return (this.player.getValue() && entity instanceof EntityPlayer && entity != Aura.mc.player && !entity.getName().equals(Aura.mc.player.getName())) || (this.hostile.getValue() && entity instanceof IMob) || (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0f && false);
    }
    
    public void attack_entity(final Entity entity) {
        if (this.mode.getValue().equalsIgnoreCase("A32k")) {
            int currentItem = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack getStackInSlot = Aura.mc.player.inventory.getStackInSlot(i);
                if (getStackInSlot != ItemStack.EMPTY) {
                    if (this.checkSharpness(getStackInSlot)) {
                        currentItem = i;
                        break;
                    }
                }
            }
            if (currentItem != -1) {
                Aura.mc.player.inventory.currentItem = currentItem;
            }
        }
        if (Aura.mc.player.getHeldItemOffhand().getItem() == Items.SHIELD) {
            Aura.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Aura.mc.player.getHorizontalFacing()));
        }
        Aura.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        Aura.mc.player.swingArm(this.actual_hand);
        Aura.mc.player.resetCooldown();
    }
    
    public Aura() {
        super("Aura", "aura :D", Category.Combat);
        this.start_verify = true;
        this.actual_hand = EnumHand.MAIN_HAND;
        this.tick = 0.0;
    }
    
    @Override
    public void onUpdate() {
        if (Aura.mc.player != null && Aura.mc.world != null) {
            ++this.tick;
            if (Aura.mc.player.isDead | Aura.mc.player.getHealth() <= 0.0f) {
                return;
            }
            if (this.mode.getValue().equalsIgnoreCase("Normal")) {
                if (!(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && this.sword.getValue()) {
                    this.start_verify = false;
                }
                else if (Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && this.sword.getValue()) {
                    this.start_verify = true;
                }
                else if (!this.sword.getValue()) {
                    this.start_verify = true;
                }
                final Entity find_entity = this.find_entity();
                if (find_entity != null && this.start_verify) {
                    final float n = 20.0f - TpsUtils.getTickRate();
                    if (Aura.mc.player.getCooledAttackStrength(this.sync_tps.getValue() ? (-n) : 0.0f) >= 1.0f) {
                        this.attack_entity(find_entity);
                    }
                }
            }
            else {
                if (!(Aura.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) {
                    return;
                }
                if (this.tick < this.delay.getValue()) {
                    return;
                }
                this.tick = 0.0;
                final Entity find_entity2 = this.find_entity();
                if (find_entity2 != null) {
                    this.attack_entity(find_entity2);
                }
            }
        }
    }
    
    public Entity find_entity() {
        Entity entity = null;
        for (final Entity entity2 : (List)Aura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList())) {
            if (entity2 != null && this.is_compatible(entity2) && Aura.mc.player.getDistance(entity2) <= this.range.getValue()) {
                entity = entity2;
            }
        }
        return entity;
    }
    
    private boolean checkSharpness(final ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            return false;
        }
        final NBTTagList list = (NBTTagList)itemStack.getTagCompound().getTag("ench");
        if (list == null) {
            return false;
        }
        int i = 0;
        while (i < list.tagCount()) {
            final NBTTagCompound getCompoundTagAt = list.getCompoundTagAt(i);
            if (getCompoundTagAt.getInteger("id") == 16) {
                if (getCompoundTagAt.getInteger("lvl") > 5) {
                    return true;
                }
                break;
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    @Override
    protected void onEnable() {
        this.tick = 0.0;
    }
}
