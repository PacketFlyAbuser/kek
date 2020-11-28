//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import me.zoom.xannax.module.modules.render.Skeleton;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelPlayer.class })
public class MixinPlayerModel
{
    @Inject(method = { "setRotationAngles" }, at = { @At("RETURN") })
    public void setRotationAngles(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final Entity entity, final CallbackInfo callbackInfo) {
        if (Wrapper.getMinecraft().world != null && Wrapper.getMinecraft().player != null && entity instanceof EntityPlayer) {
            Skeleton.addEntity((EntityPlayer)entity, (ModelPlayer)this);
        }
    }
}
