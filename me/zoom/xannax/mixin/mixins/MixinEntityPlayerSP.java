//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.zoom.xannax.event.events.MoveEvent;
import net.minecraft.entity.MoverType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraftforge.fml.common.eventhandler.Event;
import me.zoom.xannax.event.events.UpdateWalkingPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.entity.AbstractClientPlayer;

@Mixin({ EntityPlayerSP.class })
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") })
    private void preMotion(final CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post((Event)new UpdateWalkingPlayerEvent(0));
    }
    
    @Inject(method = { "swingArm" }, at = { @At("HEAD") }, cancellable = true)
    public void swingArm(final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("OffhandSwing")) {
            callbackInfo.cancel();
            super.swingArm(EnumHand.OFF_HAND);
            Minecraft.getMinecraft().player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.OFF_HAND));
        }
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer abstractClientPlayer, final MoverType moverType, final double n, final double n2, final double n3) {
        final MoveEvent moveEvent = new MoveEvent(0, moverType, n, n2, n3);
        MinecraftForge.EVENT_BUS.post((Event)moveEvent);
        if (!moveEvent.isCanceled()) {
            super.move(moveEvent.getType(), moveEvent.getX(), moveEvent.getY(), moveEvent.getZ());
        }
    }
    
    public MixinEntityPlayerSP() {
        super((World)null, (GameProfile)null);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") })
    private void postMotion(final CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post((Event)new UpdateWalkingPlayerEvent(1));
    }
}
