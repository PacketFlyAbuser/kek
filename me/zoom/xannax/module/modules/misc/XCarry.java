//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import net.minecraft.network.play.client.CPacketCloseWindow;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class XCarry extends Module
{
    @EventHandler
    private /* synthetic */ Listener<PacketEvent.Send> listener;
    
    public XCarry() {
        super("XCarry", "XCarry", Category.Misc);
        this.listener = new Listener<PacketEvent.Send>(send -> {
            if (send.getPacket() instanceof CPacketCloseWindow && ((CPacketCloseWindow)send.getPacket()).windowId == XCarry.mc.player.inventoryContainer.windowId) {
                send.cancel();
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
