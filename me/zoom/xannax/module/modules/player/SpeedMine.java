//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import net.minecraft.potion.PotionEffect;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.init.MobEffects;
import me.zoom.xannax.Xannax;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.DamageBlockEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class SpeedMine extends Module
{
    @EventHandler
    private final /* synthetic */ Listener<DamageBlockEvent> listener;
    /* synthetic */ Setting.Mode mode;
    /* synthetic */ Setting.Boolean haste;
    
    @Override
    public String getHudInfo() {
        return "[" + ChatFormatting.WHITE + this.mode.getValue() + ChatFormatting.GRAY + "]";
    }
    
    private boolean canBreak(final BlockPos blockPos) {
        final IBlockState getBlockState = SpeedMine.mc.world.getBlockState(blockPos);
        return getBlockState.getBlock().getBlockHardness(getBlockState, (World)SpeedMine.mc.world, blockPos) != -1.0f;
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
        SpeedMine.mc.player.removePotionEffect(MobEffects.HASTE);
    }
    
    public SpeedMine() {
        super("SpeedMine", "SpeedMine", Category.Player);
        this.listener = new Listener<DamageBlockEvent>(damageBlockEvent -> {
            if (SpeedMine.mc.world != null && SpeedMine.mc.player != null) {
                if (this.canBreak(damageBlockEvent.getPos())) {
                    if (this.mode.getValue().equalsIgnoreCase("Packet")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, damageBlockEvent.getPos(), damageBlockEvent.getFace()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, damageBlockEvent.getPos(), damageBlockEvent.getFace()));
                        damageBlockEvent.cancel();
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Damage") && SpeedMine.mc.playerController.curBlockDamageMP >= 0.7f) {
                        SpeedMine.mc.playerController.curBlockDamageMP = 1.0f;
                    }
                    if (this.mode.getValue().equalsIgnoreCase("Instant")) {
                        SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, damageBlockEvent.getPos(), damageBlockEvent.getFace()));
                        SpeedMine.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, damageBlockEvent.getPos(), damageBlockEvent.getFace()));
                        SpeedMine.mc.playerController.onPlayerDestroyBlock(damageBlockEvent.getPos());
                        SpeedMine.mc.world.setBlockToAir(damageBlockEvent.getPos());
                    }
                }
            }
        }, (Predicate<DamageBlockEvent>[])new Predicate[0]);
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Packet");
        list.add("Damage");
        list.add("Instant");
        this.mode = this.registerMode("Mode", "Mode", list, "Packet");
        this.haste = this.registerBoolean("Haste", "Haste", false);
    }
    
    @Override
    public void onUpdate() {
        Minecraft.getMinecraft().playerController.blockHitDelay = 0;
        if (this.haste.getValue()) {
            SpeedMine.mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.HASTE, 80950, 1, false, false)));
        }
        if (!this.haste.getValue() && SpeedMine.mc.player.isPotionActive(MobEffects.HASTE)) {
            SpeedMine.mc.player.removePotionEffect(MobEffects.HASTE);
        }
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
}
