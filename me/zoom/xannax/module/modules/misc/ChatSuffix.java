//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import me.zoom.xannax.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class ChatSuffix extends Module
{
    @EventHandler
    private final /* synthetic */ Listener<PacketEvent.Send> listener;
    
    public ChatSuffix() {
        super("ChatSuffix", "Show off your client!", Category.Misc);
        final String message;
        this.listener = new Listener<PacketEvent.Send>(send -> {
            if (send.getPacket() instanceof CPacketChatMessage) {
                if (!((CPacketChatMessage)send.getPacket()).getMessage().startsWith("/") && !((CPacketChatMessage)send.getPacket()).getMessage().startsWith(Command.getPrefix()) && !((CPacketChatMessage)send.getPacket()).getMessage().startsWith("!")) {
                    new StringBuilder().append(((CPacketChatMessage)send.getPacket()).getMessage()).append(" \u23d0 " + "X\u1d00\u0274\u0274\u1d00X").toString();
                    if (message.length() <= 255) {
                        ((CPacketChatMessage)send.getPacket()).message = message;
                    }
                }
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
