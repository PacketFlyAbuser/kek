//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import java.util.function.Predicate;
import me.zoom.xannax.Xannax;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.WaterPushEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class Velocity extends Module
{
    @EventHandler
    private final /* synthetic */ Listener<WaterPushEvent> waterPushEventListener;
    @EventHandler
    private final /* synthetic */ Listener<PacketEvent.Receive> receiveListener;
    public /* synthetic */ Setting.Boolean noPush;
    /* synthetic */ Setting.Boolean antiKnockBack;
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    @Override
    public void setup() {
        this.noPush = this.registerBoolean("No Push", "NoPush", false);
        this.antiKnockBack = this.registerBoolean("Velocity", "Velocity", false);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    public Velocity() {
        super("Velocity", "Velocity", Category.Movement);
        this.receiveListener = new Listener<PacketEvent.Receive>(receive -> {
            if (this.antiKnockBack.getValue()) {
                if (receive.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)receive.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                    receive.cancel();
                }
                if (receive.getPacket() instanceof SPacketExplosion) {
                    receive.cancel();
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.waterPushEventListener = new Listener<WaterPushEvent>(waterPushEvent -> {
            if (this.noPush.getValue()) {
                waterPushEvent.cancel();
            }
        }, (Predicate<WaterPushEvent>[])new Predicate[0]);
    }
}
