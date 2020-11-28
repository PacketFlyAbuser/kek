//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.frame;

import net.minecraft.client.gui.Gui;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;

public class Renderer
{
    public static void RenderBoxOutline(final double n, final int n2, final int n3, final int n4, final int n5, final Color color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glEnable(2848);
        GL11.glColor3f((float)(color.getRed() / 255), (float)(color.getGreen() / 255), (float)(color.getBlue() / 255));
        GL11.glLineWidth((float)n);
        GL11.glBegin(2);
        GL11.glVertex2i(n2, n3);
        GL11.glVertex2i(n2, n5);
        GL11.glVertex2i(n4, n5);
        GL11.glVertex2i(n4, n3);
        GL11.glEnd();
        GlStateManager.popMatrix();
    }
    
    public static Color getTransColor(final boolean b) {
        final Color color = new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50);
        if (b) {
            return new Color(0, 0, 0, ClickGuiModule.opacity.getValue());
        }
        return color;
    }
    
    public static void renderImage(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float n7, final float n8) {
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(n, n2, (float)n3, (float)n4, n5, n6, n7, n8);
    }
    
    public static Color getFontColor(final boolean b, final int n) {
        if (b && n == 0) {
            return new Color(255, 255, 255);
        }
        return new Color(255 - n, 255 - n, 255 - n);
    }
    
    public static Color getMainColor() {
        return new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue());
    }
    
    public static void drawRectStatic(final int n, final int n2, final int n3, final int n4, final Color color) {
        Gui.drawRect(n, n2, n3, n4, color.getRGB());
    }
}
