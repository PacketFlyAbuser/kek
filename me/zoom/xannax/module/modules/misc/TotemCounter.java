//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.network.play.server.SPacketEntityStatus;
import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.TotemPopEvent;
import java.util.HashMap;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class TotemCounter extends Module
{
    @EventHandler
    public /* synthetic */ Listener<PacketEvent.Receive> totemPopListener;
    private /* synthetic */ HashMap<String, Integer> popList;
    @EventHandler
    public /* synthetic */ Listener<TotemPopEvent> totemPopEvent;
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
        this.popList = new HashMap<String, Integer>();
    }
    
    public TotemCounter() {
        super("PopCounter", "PopCounter", Category.Misc);
        this.popList = new HashMap<String, Integer>();
        int n = 0;
        final int n2;
        this.totemPopEvent = new Listener<TotemPopEvent>(totemPopEvent -> {
            if (this.popList == null) {
                this.popList = new HashMap<String, Integer>();
            }
            if (this.popList.get(totemPopEvent.getEntity().getName()) == null) {
                this.popList.put(totemPopEvent.getEntity().getName(), 1);
                Command.sendClientMessage(ChatFormatting.DARK_AQUA + totemPopEvent.getEntity().getName() + ChatFormatting.DARK_RED + " popped " + ChatFormatting.GOLD + 1 + ChatFormatting.GOLD + " totem!");
            }
            else if (this.popList.get(totemPopEvent.getEntity().getName()) != null) {
                this.popList.get(totemPopEvent.getEntity().getName());
                ++n;
                this.popList.put(totemPopEvent.getEntity().getName(), n2);
                Command.sendClientMessage(ChatFormatting.DARK_AQUA + totemPopEvent.getEntity().getName() + ChatFormatting.DARK_RED + " popped " + ChatFormatting.GOLD + n2 + ChatFormatting.GOLD + " totems!");
            }
            return;
        }, (Predicate<TotemPopEvent>[])new Predicate[0]);
        SPacketEntityStatus sPacketEntityStatus;
        this.totemPopListener = new Listener<PacketEvent.Receive>(receive -> {
            if (TotemCounter.mc.world != null && TotemCounter.mc.player != null) {
                if (receive.getPacket() instanceof SPacketEntityStatus) {
                    sPacketEntityStatus = (SPacketEntityStatus)receive.getPacket();
                    if (sPacketEntityStatus.getOpCode() == 35) {
                        Xannax.EVENT_BUS.post(new TotemPopEvent(sPacketEntityStatus.getEntity((World)TotemCounter.mc.world)));
                    }
                }
            }
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        for (final EntityPlayer entityPlayer : TotemCounter.mc.world.playerEntities) {
            if (entityPlayer.getHealth() <= 0.0f && this.popList.containsKey(entityPlayer.getName())) {
                Command.sendClientMessage(ChatFormatting.DARK_AQUA + entityPlayer.getName() + ChatFormatting.DARK_RED + " died after popping " + ChatFormatting.GOLD + this.popList.get(entityPlayer.getName()) + ChatFormatting.GOLD + " totems!");
                this.popList.remove(entityPlayer.getName(), this.popList.get(entityPlayer.getName()));
            }
        }
    }
}
