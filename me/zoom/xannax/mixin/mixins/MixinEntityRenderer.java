//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import me.zoom.xannax.module.modules.render.CameraClip;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.ArrayList;
import me.zoom.xannax.module.modules.player.NoEntityTrace;
import java.util.List;
import com.google.common.base.Predicate;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.module.modules.render.NoRender;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ EntityRenderer.class })
public class MixinEntityRenderer
{
    @Inject(method = { "hurtCameraEffect" }, at = { @At("HEAD") }, cancellable = true)
    public void hurtCameraEffect(final float n, final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("NoRender") && ((NoRender)ModuleManager.getModuleByName("NoRender")).hurtCam.getValue()) {
            callbackInfo.cancel();
        }
    }
    
    @Redirect(method = { "getMouseOver" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getEntitiesInAABBexcluding(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;"))
    public List<Entity> getEntitiesInAABBexcluding(final WorldClient worldClient, final Entity entity, final AxisAlignedBB axisAlignedBB, final Predicate predicate) {
        if (((NoEntityTrace)ModuleManager.getModuleByName("NoEntityTrace")).noTrace()) {
            return new ArrayList<Entity>();
        }
        return (List<Entity>)worldClient.getEntitiesInAABBexcluding(entity, axisAlignedBB, predicate);
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 3, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double changeCameraDistanceHook(final double n) {
        return (ModuleManager.isModuleEnabled("CameraClip") && CameraClip.extend.getValue()) ? CameraClip.distance.getValue() : n;
    }
    
    @ModifyVariable(method = { "orientCamera" }, ordinal = 7, at = @At(value = "STORE", ordinal = 0), require = 1)
    public double orientCameraHook(final double n) {
        return (ModuleManager.isModuleEnabled("CameraClip") && CameraClip.extend.getValue()) ? CameraClip.distance.getValue() : ((ModuleManager.isModuleEnabled("CameraClip") && !CameraClip.extend.getValue()) ? 4.0 : n);
    }
}
