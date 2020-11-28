//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.client.gui.Gui;
import java.util.function.Predicate;
import java.awt.Color;
import me.zoom.xannax.util.RenderUtil;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.math.Vec3d;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.player.EntityPlayer;
import java.util.HashMap;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Skeleton extends Module
{
    public /* synthetic */ Setting.Integer green;
    private static final /* synthetic */ HashMap<EntityPlayer, float[][]> entities;
    public /* synthetic */ Setting.Integer brightness;
    public /* synthetic */ Setting.Integer alpha;
    public /* synthetic */ Setting.Integer speed;
    public /* synthetic */ Setting.Integer saturation;
    public /* synthetic */ Setting.Integer red;
    public /* synthetic */ Setting.Integer blue;
    private /* synthetic */ ICamera camera;
    public /* synthetic */ Setting.Boolean rainbow;
    
    public static void addEntity(final EntityPlayer key, final ModelPlayer modelPlayer) {
        Skeleton.entities.put(key, new float[][] { { modelPlayer.bipedHead.rotateAngleX, modelPlayer.bipedHead.rotateAngleY, modelPlayer.bipedHead.rotateAngleZ }, { modelPlayer.bipedRightArm.rotateAngleX, modelPlayer.bipedRightArm.rotateAngleY, modelPlayer.bipedRightArm.rotateAngleZ }, { modelPlayer.bipedLeftLeg.rotateAngleX, modelPlayer.bipedLeftLeg.rotateAngleY, modelPlayer.bipedLeftLeg.rotateAngleZ }, { modelPlayer.bipedRightLeg.rotateAngleX, modelPlayer.bipedRightLeg.rotateAngleY, modelPlayer.bipedRightLeg.rotateAngleZ }, { modelPlayer.bipedLeftLeg.rotateAngleX, modelPlayer.bipedLeftLeg.rotateAngleY, modelPlayer.bipedLeftLeg.rotateAngleZ } });
    }
    
    private Vec3d getVec3(final RenderEvent renderEvent, final EntityPlayer entityPlayer) {
        final float partialTicks = renderEvent.getPartialTicks();
        return new Vec3d(entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks, entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * partialTicks, entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks);
    }
    
    public Skeleton() {
        super("Skeleton", "Skeleton", Category.Render);
        this.camera = (ICamera)new Frustum();
    }
    
    private void startEnd(final boolean b) {
        if (b) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GL11.glEnable(2848);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GL11.glHint(3154, 4354);
        }
        else {
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GL11.glDisable(2848);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(!b);
    }
    
    private void drawSkeleton(final RenderEvent renderEvent, final EntityPlayer key) {
        final Color color = this.rainbow.getValue() ? new Color(RenderUtil.getRainbow(this.speed.getValue() * 100, 0, this.saturation.getValue() / 100.0f, this.brightness.getValue() / 100.0f)) : new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue());
        final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue());
        this.camera.setPosition(Skeleton.mc.player.lastTickPosX + (Skeleton.mc.player.posX - Skeleton.mc.player.lastTickPosX) * renderEvent.getPartialTicks(), Skeleton.mc.player.lastTickPosY + (Skeleton.mc.player.posY - Skeleton.mc.player.lastTickPosY) * renderEvent.getPartialTicks(), Skeleton.mc.player.lastTickPosZ + (Skeleton.mc.player.posZ - Skeleton.mc.player.lastTickPosZ) * renderEvent.getPartialTicks());
        final float[][] array = Skeleton.entities.get(key);
        if (array != null && key.isEntityAlive() && this.camera.isBoundingBoxInFrustum(key.getEntityBoundingBox()) && !key.isDead && key != Skeleton.mc.player && !key.isPlayerSleeping()) {
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glLineWidth(1.0f);
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            final Vec3d vec3 = this.getVec3(renderEvent, key);
            GL11.glTranslated(vec3.x - Skeleton.mc.getRenderManager().renderPosX, vec3.y - Skeleton.mc.getRenderManager().renderPosY, vec3.z - Skeleton.mc.getRenderManager().renderPosZ);
            final float n = key.prevRenderYawOffset + (key.renderYawOffset - key.prevRenderYawOffset) * renderEvent.getPartialTicks();
            GL11.glRotatef(-n, 0.0f, 1.0f, 0.0f);
            GL11.glTranslated(0.0, 0.0, key.isSneaking() ? -0.235 : 0.0);
            final float n2 = key.isSneaking() ? 0.6f : 0.75f;
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(-0.125, (double)n2, 0.0);
            if (array[3][0] != 0.0f) {
                GL11.glRotatef(array[3][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (array[3][1] != 0.0f) {
                GL11.glRotatef(array[3][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (array[3][2] != 0.0f) {
                GL11.glRotatef(array[3][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-n2), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.125, (double)n2, 0.0);
            if (array[4][0] != 0.0f) {
                GL11.glRotatef(array[4][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (array[4][1] != 0.0f) {
                GL11.glRotatef(array[4][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (array[4][2] != 0.0f) {
                GL11.glRotatef(array[4][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, (double)(-n2), 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0, 0.0, key.isSneaking() ? 0.25 : 0.0);
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, key.isSneaking() ? -0.05 : 0.0, key.isSneaking() ? -0.01725 : 0.0);
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(-0.375, n2 + 0.55, 0.0);
            if (array[1][0] != 0.0f) {
                GL11.glRotatef(array[1][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (array[1][1] != 0.0f) {
                GL11.glRotatef(array[1][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (array[1][2] != 0.0f) {
                GL11.glRotatef(-array[1][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375, n2 + 0.55, 0.0);
            if (array[2][0] != 0.0f) {
                GL11.glRotatef(array[2][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            if (array[2][1] != 0.0f) {
                GL11.glRotatef(array[2][1] * 57.295776f, 0.0f, 1.0f, 0.0f);
            }
            if (array[2][2] != 0.0f) {
                GL11.glRotatef(-array[2][2] * 57.295776f, 0.0f, 0.0f, 1.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, -0.5, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(n - key.rotationYawHead, 0.0f, 1.0f, 0.0f);
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, n2 + 0.55, 0.0);
            if (array[0][0] != 0.0f) {
                GL11.glRotatef(array[0][0] * 57.295776f, 1.0f, 0.0f, 0.0f);
            }
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.3, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(key.isSneaking() ? 25.0f : 0.0f, 1.0f, 0.0f, 0.0f);
            GL11.glTranslated(0.0, key.isSneaking() ? -0.16175 : 0.0, key.isSneaking() ? -0.48025 : 0.0);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, (double)n2, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.125, 0.0, 0.0);
            GL11.glVertex3d(0.125, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(color2.getRed() / 255.0f, color2.getGreen() / 255.0f, color2.getBlue() / 255.0f, this.alpha.getValue() / 255.0f);
            GL11.glTranslated(0.0, (double)n2, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0, 0.0, 0.0);
            GL11.glVertex3d(0.0, 0.55, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0, n2 + 0.55, 0.0);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375, 0.0, 0.0);
            GL11.glVertex3d(0.375, 0.0, 0.0);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }
    
    static {
        entities = new HashMap<EntityPlayer, float[][]>();
    }
    
    @Override
    public void setup() {
        this.red = this.registerInteger("Red", "Red", 255, 0, 255);
        this.green = this.registerInteger("Green", "Green", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        this.alpha = this.registerInteger("Alpha", "Alpha", 255, 0, 255);
        this.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        this.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        this.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        this.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
    
    private boolean doesntContain(final EntityPlayer entityPlayer) {
        return !Skeleton.mc.world.playerEntities.contains(entityPlayer);
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        if (Skeleton.mc.getRenderManager() == null || Skeleton.mc.getRenderManager().options == null) {
            return;
        }
        this.startEnd(true);
        GL11.glEnable(2903);
        GL11.glDisable(2848);
        Skeleton.entities.keySet().removeIf(this::doesntContain);
        Skeleton.mc.world.playerEntities.forEach(entityPlayer -> this.drawSkeleton(renderEvent, entityPlayer));
        Gui.drawRect(0, 0, 0, 0, 0);
        this.startEnd(false);
    }
}
