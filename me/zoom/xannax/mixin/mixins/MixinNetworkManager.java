// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.PacketEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "channelRead0" }, at = { @At("HEAD") }, cancellable = true)
    private void preChannelRead(final ChannelHandlerContext channelHandlerContext, final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent.Receive receive = new PacketEvent.Receive(packet);
        Xannax.EVENT_BUS.post(receive);
        if (receive.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    private void preSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent.Send send = new PacketEvent.Send(packet);
        Xannax.EVENT_BUS.post(send);
        if (send.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "channelRead0" }, at = { @At("TAIL") }, cancellable = true)
    private void postChannelRead(final ChannelHandlerContext channelHandlerContext, final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent.PostReceive postReceive = new PacketEvent.PostReceive(packet);
        Xannax.EVENT_BUS.post(postReceive);
        if (postReceive.isCancelled()) {
            callbackInfo.cancel();
        }
    }
    
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("TAIL") }, cancellable = true)
    private void postSendPacket(final Packet<?> packet, final CallbackInfo callbackInfo) {
        final PacketEvent.PostSend postSend = new PacketEvent.PostSend(packet);
        Xannax.EVENT_BUS.post(postSend);
        if (postSend.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}
