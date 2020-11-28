//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.util.function.Predicate;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.world.World;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import me.zoom.xannax.Xannax;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import java.util.Queue;
import me.zoom.xannax.module.Module;

public class Blink extends Module
{
    private final /* synthetic */ Queue<Packet> packets;
    @EventHandler
    private /* synthetic */ Listener<PacketEvent.Send> packetSendListener;
    /* synthetic */ EntityOtherPlayerMP entity;
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
        if (this.entity != null) {
            Blink.mc.world.removeEntity((Entity)this.entity);
        }
        if (this.packets.size() > 0) {
            final Iterator<Packet> iterator = this.packets.iterator();
            while (iterator.hasNext()) {
                Blink.mc.player.connection.sendPacket((Packet)iterator.next());
            }
            this.packets.clear();
        }
    }
    
    @Override
    public String getHudInfo() {
        return "" + this.packets.size();
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
        this.entity = new EntityOtherPlayerMP((World)Blink.mc.world, Blink.mc.getSession().getProfile());
        this.entity.copyLocationAndAnglesFrom((Entity)Blink.mc.player);
        this.entity.rotationYaw = Blink.mc.player.rotationYaw;
        this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
        Blink.mc.world.addEntityToWorld(666, (Entity)this.entity);
    }
    
    public Blink() {
        super("Blink", "Blink", Category.Movement);
        this.packets = new ConcurrentLinkedQueue<Packet>();
        final Packet packet;
        this.packetSendListener = new Listener<PacketEvent.Send>(send -> {
            send.getPacket();
            if (!(packet instanceof CPacketChatMessage) && !(packet instanceof CPacketConfirmTeleport) && !(packet instanceof CPacketKeepAlive) && !(packet instanceof CPacketTabComplete) && !(packet instanceof CPacketClientStatus)) {
                this.packets.add(packet);
                send.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
