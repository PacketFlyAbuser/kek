// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.network.Packet;
import me.zoom.xannax.event.Event;

public class PacketEvent extends Event
{
    private final /* synthetic */ Packet packet;
    
    public Packet getPacket() {
        return this.packet;
    }
    
    public PacketEvent(final Packet packet) {
        this.packet = packet;
    }
    
    public static class PostReceive extends PacketEvent
    {
        public PostReceive(final Packet packet) {
            super(packet);
        }
    }
    
    public static class PostSend extends PacketEvent
    {
        public PostSend(final Packet packet) {
            super(packet);
        }
    }
    
    public static class Receive extends PacketEvent
    {
        public Receive(final Packet packet) {
            super(packet);
        }
    }
    
    public static class Send extends PacketEvent
    {
        public Send(final Packet packet) {
            super(packet);
        }
    }
}
