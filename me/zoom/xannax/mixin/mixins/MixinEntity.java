//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.movement.Velocity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Entity.class })
public class MixinEntity
{
    @Redirect(method = { "applyEntityCollision" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void velocity(final Entity entity, final double n, final double n2, final double n3) {
        if (!((Velocity)ModuleManager.getModuleByName("Velocity")).noPush.getValue()) {
            entity.motionX += n;
            entity.motionY += n2;
            entity.motionZ += n3;
            entity.isAirBorne = true;
        }
    }
}
