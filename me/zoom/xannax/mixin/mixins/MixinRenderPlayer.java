//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.module.modules.render.HandColor;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Inject(method = { "renderRightArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderRightArmBegin(final AbstractClientPlayer abstractClientPlayer, final CallbackInfo callbackInfo) {
        if (abstractClientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            final Color color = HandColor.rainbow.getValue() ? new Color(RenderUtil.getRainbow(HandColor.speed.getValue() * 100, 0, HandColor.saturation.getValue() / 100.0f, HandColor.brightness.getValue() / 100.0f)) : new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue());
            final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
            final Color color3 = new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue(), HandColor.alphah.getValue());
            GL11.glColor4f(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, HandColor.alphah.getValue() / 255.0f);
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPlayer;swingProgress:F", opcode = 181) }, cancellable = true)
    public void renderLeftArmBegin(final AbstractClientPlayer abstractClientPlayer, final CallbackInfo callbackInfo) {
        if (abstractClientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glEnable(10754);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
            final Color color = new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue(), HandColor.alphah.getValue());
            final Color color2 = HandColor.rainbow.getValue() ? new Color(RenderUtil.getRainbow(HandColor.speed.getValue() * 100, 0, HandColor.saturation.getValue() / 100.0f, HandColor.brightness.getValue() / 100.0f)) : new Color(HandColor.redh.getValue(), HandColor.greenh.getValue(), HandColor.blueh.getValue());
            final Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue());
            GL11.glColor4f(color3.getRed() / 255.0f, color3.getGreen() / 255.0f, color3.getBlue() / 255.0f, HandColor.alphah.getValue() / 255.0f);
        }
    }
    
    @Inject(method = { "renderRightArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderRightArmReturn(final AbstractClientPlayer abstractClientPlayer, final CallbackInfo callbackInfo) {
        if (abstractClientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
    
    @Inject(method = { "renderLeftArm" }, at = { @At("RETURN") }, cancellable = true)
    public void renderLeftArmReturn(final AbstractClientPlayer abstractClientPlayer, final CallbackInfo callbackInfo) {
        if (abstractClientPlayer == Minecraft.getMinecraft().player && ModuleManager.isModuleEnabled("HandColor")) {
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
    }
    
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderEntityNameHook(final AbstractClientPlayer abstractClientPlayer, final double n, final double n2, final double n3, final String s, final double n4, final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("Nametags")) {
            callbackInfo.cancel();
        }
    }
}
