//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import java.util.function.Predicate;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import me.zoom.xannax.Xannax;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class Criticals extends Module
{
    @EventHandler
    private /* synthetic */ Listener<PacketEvent.Send> sendListener;
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    public Criticals() {
        super("Criticals", "Criticals", Category.Combat);
        this.sendListener = new Listener<PacketEvent.Send>(send -> {
            if (send.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity)send.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && Criticals.mc.player.onGround) {
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
}
