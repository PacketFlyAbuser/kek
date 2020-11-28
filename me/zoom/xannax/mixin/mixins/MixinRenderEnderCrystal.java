//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import me.zoom.xannax.util.OutlineUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import me.zoom.xannax.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.zoom.xannax.module.modules.render.ESP;
import me.zoom.xannax.module.modules.render.Chams;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public abstract class MixinRenderEnderCrystal
{
    @Final
    @Shadow
    private static /* synthetic */ ResourceLocation ENDER_CRYSTAL_TEXTURES;
    @Shadow
    public /* synthetic */ ModelBase modelEnderCrystal;
    @Shadow
    public /* synthetic */ ModelBase modelEnderCrystalNoBase;
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 1))
    private void render2(final ModelBase modelBase, final Entity entity, final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        if ((!ModuleManager.isModuleEnabled("Chams") || !Chams.crystal.getValue()) && (!ModuleManager.isModuleEnabled("ESP") || (boolean)ESP.mode.getValue().equalsIgnoreCase("Outline"))) {
            modelBase.render(entity, n, n2, n3, n4, n5, n6);
        }
    }
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void render1(final ModelBase modelBase, final Entity entity, final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        if ((!ModuleManager.isModuleEnabled("Chams") || !Chams.crystal.getValue()) && (!ModuleManager.isModuleEnabled("ESP") || (boolean)ESP.mode.getValue().equalsIgnoreCase("Outline"))) {
            modelBase.render(entity, n, n2, n3, n4, n5, n6);
        }
    }
    
    @Shadow
    public abstract void doRender(final EntityEnderCrystal p0, final double p1, final double p2, final double p3, final float p4, final float p5);
    
    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal entityEnderCrystal, final double n, final double n2, final double n3, final float n4, final float n5, final CallbackInfo callbackInfo) {
        if (ModuleManager.isModuleEnabled("ESP")) {
            if (ESP.crystal.getValue() && ESP.mode.getValue().equalsIgnoreCase("Outline")) {
                final float n6 = entityEnderCrystal.innerRotation + n5;
                GlStateManager.pushMatrix();
                GlStateManager.translate(n, n2, n3);
                Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                final float n7 = MathHelper.sin(n6 * 0.2f) / 2.0f + 0.5f;
                final float n8 = n7 + n7 * n7;
                GL11.glLineWidth(5.0f);
                if (entityEnderCrystal.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderOne((float)ESP.width.getValue());
                if (entityEnderCrystal.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderTwo();
                if (entityEnderCrystal.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                final Color color = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                final Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue());
                OutlineUtils.renderThree();
                OutlineUtils.renderFour();
                OutlineUtils.setColor(color3);
                if (entityEnderCrystal.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n6 * 3.0f, n8 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                OutlineUtils.renderFive();
                GlStateManager.popMatrix();
            }
            else if (ESP.crystal.getValue() && ESP.mode.getValue().equalsIgnoreCase("Wireframe")) {
                final float n9 = entityEnderCrystal.innerRotation + n5;
                GlStateManager.pushMatrix();
                GlStateManager.translate(n, n2, n3);
                Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                final float n10 = MathHelper.sin(n9 * 0.2f) / 2.0f + 0.5f;
                final float n11 = n10 + n10 * n10;
                GL11.glPushAttrib(1048575);
                GL11.glPolygonMode(1032, 6913);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDisable(2929);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final Color color4 = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                final Color color5 = new Color(color4.getRed(), color4.getGreen(), color4.getBlue());
                OutlineUtils.setColor(new Color(color5.getRed(), color5.getGreen(), color5.getBlue()));
                GL11.glLineWidth((float)ESP.width.getValue());
                if (entityEnderCrystal.shouldShowBottom()) {
                    this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n9 * 3.0f, n11 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                else {
                    this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n9 * 3.0f, n11 * 0.2f, 0.0f, 0.0f, 0.0625f);
                }
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
        }
        if (ModuleManager.isModuleEnabled("Chams") && Chams.crystal.getValue()) {
            final Color color6 = Chams.rainbow.getValue() ? new Color(RenderUtil.getRainbow(Chams.speed.getValue() * 100, 0, Chams.saturation.getValue() / 100.0f, Chams.brightness.getValue() / 100.0f)) : new Color(Chams.red.getValue(), Chams.green.getValue(), Chams.blue.getValue());
            final Color color7 = new Color(color6.getRed(), color6.getGreen(), color6.getBlue());
            GL11.glPushMatrix();
            final float n12 = entityEnderCrystal.innerRotation + n5;
            GlStateManager.translate(n, n2, n3);
            Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            final float n13 = MathHelper.sin(n12 * 0.2f) / 2.0f + 0.5f;
            final float n14 = n13 + n13 * n13;
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!(boolean)Chams.lines.getValue()) {
                GL11.glPolygonMode(1028, 6914);
            }
            else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(color7.getRed() / 255.0f, color7.getGreen() / 255.0f, color7.getBlue() / 255.0f, Chams.alpha.getValue() / 255.0f);
            if ((boolean)Chams.lines.getValue()) {
                GL11.glLineWidth((float)Chams.width.getValue());
            }
            if (entityEnderCrystal.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)entityEnderCrystal, 0.0f, n12 * 3.0f, n14 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)entityEnderCrystal, 0.0f, n12 * 3.0f, n14 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
}
