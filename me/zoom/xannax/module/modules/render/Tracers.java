//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.List;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.entity.player.EntityPlayer;
import me.zoom.xannax.util.enemy.Enemies;
import java.awt.Color;
import me.zoom.xannax.util.friend.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Tracers extends Module
{
    /* synthetic */ Setting.Mode pointsTo;
    /* synthetic */ Setting.Integer renderDistance;
    /* synthetic */ int tracerColor;
    
    public void drawLineToEntityPlayer(final Entity entity, final int n, final int n2) {
        final double[] interpolate = interpolate(entity);
        this.drawLine1(interpolate[0], interpolate[1], interpolate[2], entity.height, (float)(n >>> 16 & 0xFF), (float)(n >>> 8 & 0xFF), (float)(n & 0xFF), (float)n2);
    }
    
    public void drawLine1(final double n, final double n2, final double n3, final double n4, final float n5, final float n6, final float n7, final float n8) {
        final Vec3d rotateYaw = new Vec3d(0.0, 0.0, 1.0).rotatePitch(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationPitch)).rotateYaw(-(float)Math.toRadians(Minecraft.getMinecraft().player.rotationYaw));
        if (this.pointsTo.getValue().equalsIgnoreCase("Head")) {
            renderLine1(rotateYaw.x, rotateYaw.y + Tracers.mc.player.getEyeHeight(), rotateYaw.z, n, n2, n3, n4, n5, n6, n7, n8);
        }
        else {
            renderLine2(rotateYaw.x, rotateYaw.y + Tracers.mc.player.getEyeHeight(), rotateYaw.z, n, n2, n3, n4, n5, n6, n7, n8);
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent renderEvent) {
        Tracers.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).filter(entityPlayerSP -> entityPlayerSP != Tracers.mc.player).forEach(entity2 -> {
            if (Tracers.mc.player.getDistance(entity2) <= this.renderDistance.getValue()) {
                if (Friends.isFriend(entity2.getName())) {
                    this.tracerColor = new Color(5, 218, 255).getRGB();
                }
                else if (Enemies.isEnemy(entity2.getName())) {
                    this.tracerColor = new Color(255, 0, 0).getRGB();
                }
                else {
                    if (Tracers.mc.player.getDistance(entity2) < 20.0f) {
                        this.tracerColor = Color.RED.getRGB();
                    }
                    if (Tracers.mc.player.getDistance(entity2) >= 20.0f && Tracers.mc.player.getDistance(entity2) < 50.0f) {
                        this.tracerColor = Color.YELLOW.getRGB();
                    }
                    if (Tracers.mc.player.getDistance(entity2) >= 50.0f) {
                        this.tracerColor = Color.GREEN.getRGB();
                    }
                }
                if (this.pointsTo.getValue().equalsIgnoreCase("Head")) {
                    this.drawLineToEntityPlayer(entity2, this.tracerColor, 255);
                }
                else if (this.pointsTo.getValue().equalsIgnoreCase("Feet")) {
                    this.drawLineToEntityPlayer(entity2, this.tracerColor, 255);
                }
            }
        });
    }
    
    public static void renderLine1(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final float n8, final float n9, final float n10, final float n11) {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(n8, n9, n10, n11);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(n, n2, n3);
        GL11.glVertex3d(n4, n5 + n7, n6);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
    
    @Override
    public void setup() {
        this.renderDistance = this.registerInteger("Distance", "Distance", 100, 10, 260);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Head");
        list.add("Feet");
        this.pointsTo = this.registerMode("Draw To", "DrawTo", list, "Feet");
    }
    
    public static double[] interpolate(final Entity entity) {
        return new double[] { interpolate(entity.posX, entity.lastTickPosX) - Tracers.mc.getRenderManager().renderPosX, interpolate(entity.posY, entity.lastTickPosY) - Tracers.mc.getRenderManager().renderPosY, interpolate(entity.posZ, entity.lastTickPosZ) - Tracers.mc.getRenderManager().renderPosZ };
    }
    
    public static double interpolate(final double n, final double n2) {
        return n2 + (n - n2) * Tracers.mc.getRenderPartialTicks();
    }
    
    public static void renderLine2(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final float n8, final float n9, final float n10, final float n11) {
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(n8, n9, n10, n11);
        GlStateManager.disableLighting();
        GL11.glLoadIdentity();
        Tracers.mc.entityRenderer.orientCamera(Tracers.mc.getRenderPartialTicks());
        GL11.glBegin(1);
        GL11.glVertex3d(n, n2, n3);
        GL11.glVertex3d(n4, n5, n6);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor3d(1.0, 1.0, 1.0);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
    
    public Tracers() {
        super("Tracers", "Tracers", Category.Render);
    }
}
