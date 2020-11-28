//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.event.events.WaterPushEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.event.events.PlayerJumpEvent;
import me.zoom.xannax.Xannax;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityPlayer.class })
public abstract class MixinEntityPlayer
{
    @Shadow
    public abstract String getName();
    
    @Inject(method = { "jump" }, at = { @At("HEAD") }, cancellable = true)
    public void onJump(final CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().player.getName() == this.getName()) {
            Xannax.EVENT_BUS.post(new PlayerJumpEvent());
        }
    }
    
    @Inject(method = { "isPushedByWater" }, at = { @At("HEAD") }, cancellable = true)
    private void onPushedByWater(final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final WaterPushEvent waterPushEvent = new WaterPushEvent();
        Xannax.EVENT_BUS.post(waterPushEvent);
        if (waterPushEvent.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
