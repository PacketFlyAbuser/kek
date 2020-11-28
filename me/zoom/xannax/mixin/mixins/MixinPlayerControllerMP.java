// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.zoom.xannax.event.events.DestroyBlockEvent;
import me.zoom.xannax.module.modules.player.Reach;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.event.events.DamageBlockEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ PlayerControllerMP.class })
public class MixinPlayerControllerMP
{
    @Inject(method = { "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private void onPlayerDamageBlock(final BlockPos blockPos, final EnumFacing enumFacing, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        final DamageBlockEvent damageBlockEvent = new DamageBlockEvent(blockPos, enumFacing);
        Xannax.EVENT_BUS.post(damageBlockEvent);
        if (damageBlockEvent.isCancelled()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
    
    @Inject(method = { "getBlockReachDistance" }, at = { @At("RETURN") }, cancellable = true)
    private void getReachDistanceHook(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
        if (ModuleManager.isModuleEnabled("Reach")) {
            final float floatValue = callbackInfoReturnable.getReturnValue();
            callbackInfoReturnable.setReturnValue(Reach.override.getValue() ? ((float)Reach.reach.getValue()) : (floatValue + (float)Reach.add.getValue()));
        }
    }
    
    @Inject(method = { "onPlayerDestroyBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playEvent(ILnet/minecraft/util/math/BlockPos;I)V") }, cancellable = true)
    private void onPlayerDestroyBlock(final BlockPos blockPos, final CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Xannax.EVENT_BUS.post(new DestroyBlockEvent(blockPos));
    }
    
    @Inject(method = { "resetBlockRemoving" }, at = { @At("HEAD") }, cancellable = true)
    private void resetBlock(final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("MultiTask")) {
            callbackInfo.cancel();
        }
    }
}
