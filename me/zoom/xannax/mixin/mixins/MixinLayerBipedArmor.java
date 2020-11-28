//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.module.modules.render.NoRender;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LayerBipedArmor.class })
public class MixinLayerBipedArmor
{
    @Inject(method = { "setModelSlotVisible" }, at = { @At("HEAD") }, cancellable = true)
    protected void setModelSlotVisible(final ModelBiped modelBiped, final EntityEquipmentSlot entityEquipmentSlot, final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("NoRender") && ((NoRender)ModuleManager.getModuleByName("NoRender")).armor.getValue()) {
            callbackInfo.cancel();
            switch (entityEquipmentSlot) {
                case HEAD: {
                    modelBiped.bipedHead.showModel = false;
                    modelBiped.bipedHeadwear.showModel = false;
                }
                case CHEST: {
                    modelBiped.bipedBody.showModel = false;
                    modelBiped.bipedRightArm.showModel = false;
                    modelBiped.bipedLeftArm.showModel = false;
                }
                case LEGS: {
                    modelBiped.bipedBody.showModel = false;
                    modelBiped.bipedRightLeg.showModel = false;
                    modelBiped.bipedLeftLeg.showModel = false;
                }
                case FEET: {
                    modelBiped.bipedRightLeg.showModel = false;
                    modelBiped.bipedLeftLeg.showModel = false;
                    break;
                }
            }
        }
    }
}
