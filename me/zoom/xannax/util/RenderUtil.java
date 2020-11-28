//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.Tessellator;

public class RenderUtil extends Tessellator
{
    private static final /* synthetic */ Frustum frustrum;
    private static final /* synthetic */ Minecraft mc;
    public static /* synthetic */ RenderUtil INSTANCE;
    
    public RenderUtil() {
        super(2097152);
    }
    
    public static boolean isInViewFrustrum(final AxisAlignedBB axisAlignedBB) {
        final Entity getRenderViewEntity = RenderUtil.mc.getRenderViewEntity();
        RenderUtil.frustrum.setPosition(Objects.requireNonNull(getRenderViewEntity).posX, getRenderViewEntity.posY, getRenderViewEntity.posZ);
        return RenderUtil.frustrum.isBoundingBoxInFrustum(axisAlignedBB);
    }
    
    public static void drawGradientFilledBox(final BlockPos blockPos, final Color color, final Color color2) {
        final IBlockState getBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        final Vec3d interpolateEntity = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());
        drawGradientFilledBox(getBlockState.getSelectedBoundingBox((World)RenderUtil.mc.world, blockPos).grow(0.0020000000949949026).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z), color, color2);
    }
    
    public static void drawGradientFilledBox(final AxisAlignedBB axisAlignedBB, final Color color, final Color color2) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        final float n = color2.getAlpha() / 255.0f;
        final float n2 = color2.getRed() / 255.0f;
        final float n3 = color2.getGreen() / 255.0f;
        final float n4 = color2.getBlue() / 255.0f;
        final float n5 = color.getAlpha() / 255.0f;
        final float n6 = color.getRed() / 255.0f;
        final float n7 = color.getGreen() / 255.0f;
        final float n8 = color.getBlue() / 255.0f;
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n).endVertex();
        getInstance.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static int generateRainbowFadingColor(final int n, final boolean b) {
        return (int)Long.parseLong(Integer.toHexString(Color.HSBtoRGB((System.nanoTime() + (b ? 200000000L : 20000000L) * n) / 4.0E9f % 1.0f, 0.95f, 0.95f)), 16);
    }
    
    public static void drawBlockOutline(final AxisAlignedBB axisAlignedBB, final Color color, final float n) {
        final float n2 = color.getRed() / 255.0f;
        final float n3 = color.getGreen() / 255.0f;
        final float n4 = color.getBlue() / 255.0f;
        final float n5 = color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(n);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    private static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void drawFullBox(final AxisAlignedBB axisAlignedBB, final BlockPos blockPos, final float n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        prepare(7);
        drawBox(blockPos, n2, n3, n4, n5, 63);
        release();
        drawBoundingBox(axisAlignedBB, n, n2, n3, n4, n6);
    }
    
    public static void drawBoundingBoxBottom2(final BlockPos blockPos, final float n, final int n2, final int n3, final int n4, final int n5) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(n);
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final double n6 = blockPos.getX() - getMinecraft.getRenderManager().viewerPosX;
        final double n7 = blockPos.getY() - getMinecraft.getRenderManager().viewerPosY;
        final double n8 = blockPos.getZ() - getMinecraft.getRenderManager().viewerPosZ;
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(n6, n7, n8, n6 + 1.0, n7 + 1.0, n8 + 1.0);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawLine(final float n, final float n2, final float n3, final float n4, final float n5, final int n6) {
        final float n7 = (n6 >> 16 & 0xFF) / 255.0f;
        final float n8 = (n6 >> 8 & 0xFF) / 255.0f;
        final float n9 = (n6 & 0xFF) / 255.0f;
        final float n10 = (n6 >> 24 & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(n5);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos((double)n, (double)n2, 0.0).color(n7, n8, n9, n10).endVertex();
        getBuffer.pos((double)n3, (double)n4, 0.0).color(n7, n8, n9, n10).endVertex();
        getInstance.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    public static void drawSphere(final double n, final double n2, final double n3, final float n4, final int n5, final int n6) {
        final Sphere sphere = new Sphere();
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.2f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        sphere.setDrawStyle(100013);
        GL11.glTranslated(n - RenderUtil.mc.getRenderManager().renderPosX, n2 - RenderUtil.mc.getRenderManager().renderPosY, n3 - RenderUtil.mc.getRenderManager().renderPosZ);
        sphere.draw(n4, n5, n6);
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void drawBox(final BlockPos blockPos, final int n, final int n2) {
        drawBox(blockPos, n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, n >>> 24 & 0xFF, n2);
    }
    
    public static void drawBox(final BufferBuilder bufferBuilder, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final int n8, final int n9, final int n10, final int n11) {
        if ((n11 & 0x1) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x2) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x4) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x8) != 0x0) {
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x10) != 0x0) {
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x20) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 + n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
    }
    
    public static void drawFullBox(final AxisAlignedBB axisAlignedBB, final BlockPos blockPos, final float n, final int n2, final int n3) {
        drawFullBox(axisAlignedBB, blockPos, n, n2 >>> 16 & 0xFF, n2 >>> 8 & 0xFF, n2 & 0xFF, n2 >>> 24 & 0xFF, n3);
    }
    
    public static void begin(final int n) {
        RenderUtil.INSTANCE.getBuffer().begin(n, DefaultVertexFormats.POSITION_COLOR);
    }
    
    public static void render() {
        RenderUtil.INSTANCE.draw();
    }
    
    public static void drawBoxxx(final AxisAlignedBB axisAlignedBB) {
        if (axisAlignedBB == null) {
            return;
        }
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.maxY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
        GlStateManager.glBegin(7);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.minZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.minX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glVertex3f((float)axisAlignedBB.maxX, (float)axisAlignedBB.minY, (float)axisAlignedBB.maxZ);
        GlStateManager.glEnd();
    }
    
    public static void drawESPOutline(final AxisAlignedBB axisAlignedBB, final float n, final float n2, final float n3, final float n4, final float n5) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(n5);
        GL11.glColor4f(n / 255.0f, n2 / 255.0f, n3 / 255.0f, n4 / 255.0f);
        drawOutlinedBox(axisAlignedBB);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawBox2(final BlockPos blockPos, final int n, final int n2) {
        drawDownBox(blockPos, n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, n >>> 24 & 0xFF, n2);
    }
    
    public static void drawBox(final AxisAlignedBB axisAlignedBB, final int n, final int n2) {
        drawBox(RenderUtil.INSTANCE.getBuffer(), axisAlignedBB, n >>> 16 & 0xFF, n >>> 8 & 0xFF, n & 0xFF, n >>> 24 & 0xFF, n2);
    }
    
    public static boolean isInViewFrustrum(final Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    
    public static void color(final int n) {
        GL11.glColor4f((n >> 16 & 0xFF) / 255.0f, (n >> 8 & 0xFF) / 255.0f, (n & 0xFF) / 255.0f, (n >> 24 & 0xFF) / 255.0f);
    }
    
    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    public static void prepare(final int n) {
        prepareGL();
        begin(n);
    }
    
    public static void drawGradientBlockOutline(final BlockPos blockPos, final Color color, final Color color2, final float n) {
        final IBlockState getBlockState = RenderUtil.mc.world.getBlockState(blockPos);
        final Vec3d interpolateEntity = EntityUtil.interpolateEntity((Entity)RenderUtil.mc.player, RenderUtil.mc.getRenderPartialTicks());
        drawGradientBlockOutline(getBlockState.getSelectedBoundingBox((World)RenderUtil.mc.world, blockPos).grow(0.0020000000949949026).offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z), color, color2, n);
    }
    
    public static void drawOutlinedBox(final AxisAlignedBB axisAlignedBB) {
        GL11.glBegin(1);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ);
        GL11.glVertex3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ);
        GL11.glEnd();
    }
    
    static {
        frustrum = new Frustum();
        RenderUtil.INSTANCE = new RenderUtil();
        mc = Wrapper.getMinecraft();
    }
    
    public static int getRainbow(final int n, final int n2, final float s, final float b) {
        return Color.getHSBColor((System.currentTimeMillis() + n2) % n / (float)n, s, b).getRGB();
    }
    
    public static void glBillboardDistanceScaled(final float n, final float n2, final float n3, final EntityPlayer entityPlayer, final float n4) {
        glBillboard(n, n2, n3);
        float n5 = (int)entityPlayer.getDistance((double)n, (double)n2, (double)n3) / 2.0f / (2.0f + (2.0f - n4));
        if (n5 < 1.0f) {
            n5 = 1.0f;
        }
        GlStateManager.scale(n5, n5, n5);
    }
    
    public static void drawBoundingBox(final AxisAlignedBB axisAlignedBB, final float n, final int n2, final int n3, final int n4, final int n5) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(n);
        final BufferBuilder getBuffer = RenderUtil.INSTANCE.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        render();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void glBillboard(final float n, final float n2, final float n3) {
        final float n4 = 0.02666667f;
        GlStateManager.translate(n - Minecraft.getMinecraft().getRenderManager().renderPosX, n2 - Minecraft.getMinecraft().getRenderManager().renderPosY, n3 - Minecraft.getMinecraft().getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Minecraft.getMinecraft().player.rotationPitch, (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-n4, -n4, n4);
    }
    
    public static void drawDownBox(final BlockPos blockPos, final int n, final int n2, final int n3, final int n4, final int n5) {
        drawDownBox2(RenderUtil.INSTANCE.getBuffer(), (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ(), 1.0f, 1.0f, 1.0f, n, n2, n3, n4, n5);
    }
    
    public static void drawDownBox2(final BufferBuilder bufferBuilder, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final int n8, final int n9, final int n10, final int n11) {
        if ((n11 & 0x1) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x2) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x4) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x8) != 0x0) {
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x10) != 0x0) {
            bufferBuilder.pos((double)n, (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)n, (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
        }
        if ((n11 & 0x20) != 0x0) {
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)n2, (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)n3).color(n7, n8, n9, n10).endVertex();
            bufferBuilder.pos((double)(n + n4), (double)(n2 - n5), (double)(n3 + n6)).color(n7, n8, n9, n10).endVertex();
        }
    }
    
    public static void drawBox(final BufferBuilder bufferBuilder, final AxisAlignedBB axisAlignedBB, final int n, final int n2, final int n3, final int n4, final int n5) {
        if ((n5 & 0x1) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        }
        if ((n5 & 0x2) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        }
        if ((n5 & 0x4) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        }
        if ((n5 & 0x8) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        }
        if ((n5 & 0x10) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
        }
        if ((n5 & 0x20) != 0x0) {
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n, n2, n3, n4).endVertex();
            bufferBuilder.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n, n2, n3, n4).endVertex();
        }
    }
    
    public static void fakeGuiRect(double n, double n2, double n3, double n4, final int n5) {
        if (n < n3) {
            final double n6 = n;
            n = n3;
            n3 = n6;
        }
        if (n2 < n4) {
            final double n7 = n2;
            n2 = n4;
            n4 = n7;
        }
        final float n8 = (n5 >> 24 & 0xFF) / 255.0f;
        final float n9 = (n5 >> 16 & 0xFF) / 255.0f;
        final float n10 = (n5 >> 8 & 0xFF) / 255.0f;
        final float n11 = (n5 & 0xFF) / 255.0f;
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(n9, n10, n11, n8);
        getBuffer.begin(7, DefaultVertexFormats.POSITION);
        getBuffer.pos(n, n4, 0.0).endVertex();
        getBuffer.pos(n3, n4, 0.0).endVertex();
        getBuffer.pos(n3, n2, 0.0).endVertex();
        getBuffer.pos(n, n2, 0.0).endVertex();
        getInstance.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawBoundingBox(final AxisAlignedBB axisAlignedBB, final float n, final int n2) {
        drawBoundingBox(axisAlignedBB, n, n2 >>> 16 & 0xFF, n2 >>> 8 & 0xFF, n2 & 0xFF, n2 >>> 24 & 0xFF);
    }
    
    public static void drawBoundingBoxBlockPos2(final BlockPos blockPos, final float n, final int n2, final int n3, final int n4, final int n5) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(n);
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final double n6 = blockPos.getX() - getMinecraft.getRenderManager().viewerPosX;
        final double n7 = blockPos.getY() - getMinecraft.getRenderManager().viewerPosY;
        final double n8 = blockPos.getZ() - getMinecraft.getRenderManager().viewerPosZ;
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(n6, n7, n8, n6 + 1.0, n7 - 1.0, n8 + 1.0);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        getBuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawRect(float n, float n2, final float n3, final float n4, final int n5) {
        float n6 = n + n3;
        float n7 = n2 + n4;
        if (n < n6) {
            final float n8 = n;
            n = n6;
            n6 = n8;
        }
        if (n2 < n7) {
            final float n9 = n2;
            n2 = n7;
            n7 = n9;
        }
        final float n10 = (n5 >> 24 & 0xFF) / 255.0f;
        final float n11 = (n5 >> 16 & 0xFF) / 255.0f;
        final float n12 = (n5 >> 8 & 0xFF) / 255.0f;
        final float n13 = (n5 & 0xFF) / 255.0f;
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(n11, n12, n13, n10);
        getBuffer.begin(7, DefaultVertexFormats.POSITION);
        getBuffer.pos((double)n, (double)n7, 0.0).endVertex();
        getBuffer.pos((double)n6, (double)n7, 0.0).endVertex();
        getBuffer.pos((double)n6, (double)n2, 0.0).endVertex();
        getBuffer.pos((double)n, (double)n2, 0.0).endVertex();
        getInstance.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawBoundingBoxBlockPos(final BlockPos blockPos, final float n, final int n2, final int n3, final int n4, final int n5) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(n);
        final Minecraft getMinecraft = Minecraft.getMinecraft();
        final double n6 = blockPos.getX() - getMinecraft.getRenderManager().viewerPosX;
        final double n7 = blockPos.getY() - getMinecraft.getRenderManager().viewerPosY;
        final double n8 = blockPos.getZ() - getMinecraft.getRenderManager().viewerPosZ;
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(n6, n7, n8, n6 + 1.0, n7 + 1.0, n8 + 1.0);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        getBuffer.begin(1, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawESP(final AxisAlignedBB axisAlignedBB, final float n, final float n2, final float n3, final float n4) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(n / 255.0f, n2 / 255.0f, n3 / 255.0f, n4 / 255.0f);
        drawBoxxx(axisAlignedBB);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawGradientBlockOutline(final AxisAlignedBB axisAlignedBB, final Color color, final Color color2, final float n) {
        final float n2 = color.getRed() / 255.0f;
        final float n3 = color.getGreen() / 255.0f;
        final float n4 = color.getBlue() / 255.0f;
        final float n5 = color.getAlpha() / 255.0f;
        final float n6 = color2.getRed() / 255.0f;
        final float n7 = color2.getGreen() / 255.0f;
        final float n8 = color2.getBlue() / 255.0f;
        final float n9 = color2.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(n);
        final Tessellator getInstance = Tessellator.getInstance();
        final BufferBuilder getBuffer = getInstance.getBuffer();
        getBuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(n6, n7, n8, n9).endVertex();
        getBuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getBuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(n2, n3, n4, n5).endVertex();
        getInstance.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawBorderedRect(final double n, final double n2, final double n3, final double n4, final double n5, final int n6, final int n7) {
        enableGL2D();
        fakeGuiRect(n + n5, n2 + n5, n3 - n5, n4 - n5, n6);
        fakeGuiRect(n + n5, n2, n3 - n5, n2 + n5, n7);
        fakeGuiRect(n, n2, n + n5, n4, n7);
        fakeGuiRect(n3 - n5, n2, n3, n4, n7);
        fakeGuiRect(n + n5, n4 - n5, n3 - n5, n4, n7);
        disableGL2D();
    }
    
    public static void drawBox(final BlockPos blockPos, final int n, final int n2, final int n3, final int n4, final int n5) {
        drawBox(RenderUtil.INSTANCE.getBuffer(), (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ(), 1.0f, 1.0f, 1.0f, n, n2, n3, n4, n5);
    }
    
    public static void release() {
        render();
        releaseGL();
    }
}
