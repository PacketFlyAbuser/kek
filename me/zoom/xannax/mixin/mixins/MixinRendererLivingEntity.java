//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.Overwrite;
import me.zoom.xannax.module.modules.render.Chams;
import me.zoom.xannax.util.OutlineUtils;
import me.zoom.xannax.util.friend.Friends;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.entity.Entity;
import me.zoom.xannax.module.modules.render.ESP;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

@Mixin({ RenderLivingBase.class })
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    @Shadow
    protected /* synthetic */ ModelBase mainModel;
    
    protected MixinRendererLivingEntity() {
        super((RenderManager)null);
    }
    
    @Overwrite
    protected void renderModel(final T t, final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
        boolean b = t instanceof EntityPlayer && t != Minecraft.getMinecraft().player;
        if (ESP.self.getValue()) {
            b = (t instanceof EntityPlayer);
        }
        if (!this.bindEntityTexture((Entity)t)) {
            return;
        }
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final boolean fancyGraphics = getMinecraft.gameSettings.fancyGraphics;
        getMinecraft.gameSettings.fancyGraphics = false;
        final float gammaSetting = getMinecraft.gameSettings.gammaSetting;
        getMinecraft.gameSettings.gammaSetting = 100000.0f;
        if (ModuleManager.isModuleEnabled("ESP")) {
            final String value = ESP.mode.getValue();
            switch (value) {
                case "WireFrame": {
                    if (b) {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        final Color color = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                        final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                        Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue());
                        if (Friends.isFriend(t.getName())) {
                            color3 = new Color(5, 218, 255, 255);
                        }
                        RenderUtil.color(color3.getRGB());
                        GL11.glLineWidth((float)ESP.width.getValue());
                        this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                }
                case "OutLine": {
                    boolean b2 = t instanceof EntityPlayer && t != Minecraft.getMinecraft().player;
                    if (ESP.self.getValue()) {
                        b2 = (t instanceof EntityPlayer);
                    }
                    if (b2) {
                        final Color color4 = ESP.rainbow.getValue() ? new Color(RenderUtil.getRainbow(ESP.speed.getValue() * 100, 0, ESP.saturation.getValue() / 100.0f, ESP.brightness.getValue() / 100.0f)) : new Color(ESP.redd.getValue(), ESP.greenn.getValue(), ESP.bluee.getValue());
                        final Color color5 = new Color(color4.getRed(), color4.getGreen(), color4.getBlue());
                        Color color6 = new Color(color5.getRed(), color5.getGreen(), color5.getBlue());
                        if (Friends.isFriend(t.getName())) {
                            color6 = new Color(5, 218, 255, 255);
                        }
                        OutlineUtils.setColor(color6);
                        this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
                        OutlineUtils.renderOne((float)ESP.width.getValue());
                        this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
                        OutlineUtils.renderTwo();
                        this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
                        OutlineUtils.renderThree();
                        OutlineUtils.renderFour();
                        OutlineUtils.setColor(color6);
                        this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                        break;
                    }
                    break;
                }
            }
        }
        getMinecraft.gameSettings.fancyGraphics = fancyGraphics;
        getMinecraft.gameSettings.gammaSetting = gammaSetting;
        if (ModuleManager.isModuleEnabled("Chams") && b) {
            final Color color7 = Chams.rainbow.getValue() ? new Color(RenderUtil.getRainbow(Chams.speed.getValue() * 100, 0, Chams.saturation.getValue() / 100.0f, Chams.brightness.getValue() / 100.0f)) : new Color(Chams.red.getValue(), Chams.green.getValue(), Chams.blue.getValue());
            final Color color8 = new Color(color7.getRed(), color7.getGreen(), color7.getBlue());
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glEnable(10754);
            GL11.glColor4f(color8.getRed() / 255.0f, color8.getGreen() / 255.0f, color8.getBlue() / 255.0f, Chams.alpha.getValue() / 255.0f);
            this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
        }
        if (!ModuleManager.isModuleEnabled("Chams") && (!ESP.mode.getValue().equalsIgnoreCase("Wireframe") || !ModuleManager.isModuleEnabled("ESP") || !b)) {
            this.mainModel.render((Entity)t, n, n2, n3, n4, n5, n6);
        }
    }
}
