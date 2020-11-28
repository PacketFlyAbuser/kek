//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.client.renderer.RenderGlobal;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.util.math.AxisAlignedBB;
import me.zoom.xannax.util.EntityUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ItemESP extends Module
{
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Boolean items;
    /* synthetic */ Setting.Integer brightness;
    /* synthetic */ Setting.Boolean xporbs;
    /* synthetic */ Setting.Integer blue;
    /* synthetic */ Setting.Integer green;
    /* synthetic */ Setting.Integer saturation;
    /* synthetic */ Setting.Boolean pearl;
    /* synthetic */ Setting.Integer alpha;
    /* synthetic */ Setting.Boolean xpbottles;
    /* synthetic */ Setting.Integer boxAlpha;
    /* synthetic */ Setting.Boolean rainbow;
    /* synthetic */ Setting.Integer speed;
    
    @Override
    public void setup() {
        this.items = this.registerBoolean("Items", "Items", false);
        this.xporbs = this.registerBoolean("XpOrbs", "XpOrbs", false);
        this.xpbottles = this.registerBoolean("XpBottles", "XpBottles", false);
        this.pearl = this.registerBoolean("Pearls", "Pearls", false);
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.boxAlpha = this.registerInteger("BoxAlpha", "BoxAlpha", 120, 0, 255);
        this.alpha = this.registerInteger("Alpha", "Alpha", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
    
    public ItemESP() {
        super("ItemESP", "ItemESP", Category.Render);
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        if (this.items.getValue()) {
            int n = 0;
            for (final Entity entity : ESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityItem && ESP.mc.player.getDistanceSq(entity) < 2500.0) {
                    final Vec3d interpolatedRenderPos = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(entity.getEntityBoundingBox().minX - 0.05 - entity.posX + interpolatedRenderPos.x, entity.getEntityBoundingBox().minY - 0.0 - entity.posY + interpolatedRenderPos.y, entity.getEntityBoundingBox().minZ - 0.05 - entity.posZ + interpolatedRenderPos.z, entity.getEntityBoundingBox().maxX + 0.05 - entity.posX + interpolatedRenderPos.x, entity.getEntityBoundingBox().maxY + 0.1 - entity.posY + interpolatedRenderPos.y, entity.getEntityBoundingBox().maxZ + 0.05 - entity.posZ + interpolatedRenderPos.z);
                    final Color color = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
                    final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(axisAlignedBB, color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(axisAlignedBB, new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++n >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int n2 = 0;
            for (final Entity entity2 : ESP.mc.world.loadedEntityList) {
                if (entity2 instanceof EntityXPOrb && ESP.mc.player.getDistanceSq(entity2) < 2500.0) {
                    final Vec3d interpolatedRenderPos2 = EntityUtil.getInterpolatedRenderPos(entity2, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(entity2.getEntityBoundingBox().minX - 0.05 - entity2.posX + interpolatedRenderPos2.x, entity2.getEntityBoundingBox().minY - 0.0 - entity2.posY + interpolatedRenderPos2.y, entity2.getEntityBoundingBox().minZ - 0.05 - entity2.posZ + interpolatedRenderPos2.z, entity2.getEntityBoundingBox().maxX + 0.05 - entity2.posX + interpolatedRenderPos2.x, entity2.getEntityBoundingBox().maxY + 0.1 - entity2.posY + interpolatedRenderPos2.y, entity2.getEntityBoundingBox().maxZ + 0.05 - entity2.posZ + interpolatedRenderPos2.z);
                    final Color color3 = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
                    final Color color4 = new Color(color3.getRed(), color3.getGreen(), color3.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(axisAlignedBB2, color4.getRed() / 255.0f, color4.getGreen() / 255.0f, color4.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(axisAlignedBB2, new Color(color4.getRed(), color4.getGreen(), color4.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++n2 >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.pearl.getValue()) {
            int n3 = 0;
            for (final Entity entity3 : ESP.mc.world.loadedEntityList) {
                if (entity3 instanceof EntityEnderPearl && ESP.mc.player.getDistanceSq(entity3) < 2500.0) {
                    final Vec3d interpolatedRenderPos3 = EntityUtil.getInterpolatedRenderPos(entity3, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB axisAlignedBB3 = new AxisAlignedBB(entity3.getEntityBoundingBox().minX - 0.05 - entity3.posX + interpolatedRenderPos3.x, entity3.getEntityBoundingBox().minY - 0.0 - entity3.posY + interpolatedRenderPos3.y, entity3.getEntityBoundingBox().minZ - 0.05 - entity3.posZ + interpolatedRenderPos3.z, entity3.getEntityBoundingBox().maxX + 0.05 - entity3.posX + interpolatedRenderPos3.x, entity3.getEntityBoundingBox().maxY + 0.1 - entity3.posY + interpolatedRenderPos3.y, entity3.getEntityBoundingBox().maxZ + 0.05 - entity3.posZ + interpolatedRenderPos3.z);
                    final Color color5 = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
                    final Color color6 = new Color(color5.getRed(), color5.getGreen(), color5.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(axisAlignedBB3, color6.getRed() / 255.0f, color6.getGreen() / 255.0f, color6.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(axisAlignedBB3, new Color(color6.getRed(), color6.getGreen(), color6.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++n3 >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int n4 = 0;
            for (final Entity entity4 : ESP.mc.world.loadedEntityList) {
                if (entity4 instanceof EntityExpBottle && ESP.mc.player.getDistanceSq(entity4) < 2500.0) {
                    final Vec3d interpolatedRenderPos4 = EntityUtil.getInterpolatedRenderPos(entity4, ESP.mc.getRenderPartialTicks());
                    final AxisAlignedBB axisAlignedBB4 = new AxisAlignedBB(entity4.getEntityBoundingBox().minX - 0.05 - entity4.posX + interpolatedRenderPos4.x, entity4.getEntityBoundingBox().minY - 0.0 - entity4.posY + interpolatedRenderPos4.y, entity4.getEntityBoundingBox().minZ - 0.05 - entity4.posZ + interpolatedRenderPos4.z, entity4.getEntityBoundingBox().maxX + 0.05 - entity4.posX + interpolatedRenderPos4.x, entity4.getEntityBoundingBox().maxY + 0.1 - entity4.posY + interpolatedRenderPos4.y, entity4.getEntityBoundingBox().maxZ + 0.05 - entity4.posZ + interpolatedRenderPos4.z);
                    final Color color7 = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
                    final Color color8 = new Color(color7.getRed(), color7.getGreen(), color7.getBlue());
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
                    GlStateManager.disableTexture2D();
                    GlStateManager.depthMask(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.renderFilledBox(axisAlignedBB4, color8.getRed() / 255.0f, color8.getGreen() / 255.0f, color8.getBlue() / 255.0f, this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.depthMask(true);
                    GlStateManager.enableDepth();
                    GlStateManager.enableTexture2D();
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    RenderUtil.drawBlockOutline(axisAlignedBB4, new Color(color8.getRed(), color8.getGreen(), color8.getBlue(), this.alpha.getValue()), 1.0f);
                    if (++n4 >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
}
