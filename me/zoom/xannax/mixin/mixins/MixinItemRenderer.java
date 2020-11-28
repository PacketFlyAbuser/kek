// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import me.zoom.xannax.module.modules.render.ViewModel;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.event.events.TransformSideFirstPersonEvent;
import me.zoom.xannax.Xannax;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.util.EnumHandSide;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ItemRenderer.class })
public class MixinItemRenderer
{
    @Inject(method = { "transformFirstPerson" }, at = { @At("HEAD") })
    public void transformFirstPerson(final EnumHandSide enumHandSide, final float n, final CallbackInfo callbackInfo) {
        Xannax.EVENT_BUS.post(new TransformSideFirstPersonEvent(enumHandSide));
    }
    
    @Inject(method = { "transformSideFirstPerson" }, at = { @At("HEAD") })
    public void transformSideFirstPerson(final EnumHandSide enumHandSide, final float n, final CallbackInfo callbackInfo) {
        Xannax.EVENT_BUS.post(new TransformSideFirstPersonEvent(enumHandSide));
    }
    
    @Inject(method = { "transformEatFirstPerson" }, at = { @At("HEAD") }, cancellable = true)
    public void transformEatFirstPerson(final float n, final EnumHandSide enumHandSide, final ItemStack itemStack, final CallbackInfo callbackInfo) {
        Xannax.EVENT_BUS.post(new TransformSideFirstPersonEvent(enumHandSide));
        if (ModuleManager.isModuleEnabled("ViewModel") && ((ViewModel)ModuleManager.getModuleByName("ViewModel")).cancelEating.getValue()) {
            callbackInfo.cancel();
        }
    }
}
