// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.font;

import java.awt.geom.Rectangle2D;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.Font;

public class CFont
{
    protected /* synthetic */ CharData[] charData;
    protected /* synthetic */ boolean fractionalMetrics;
    protected /* synthetic */ Font font;
    protected /* synthetic */ int charOffset;
    protected /* synthetic */ DynamicTexture tex;
    protected /* synthetic */ boolean antiAlias;
    protected /* synthetic */ int fontHeight;
    
    public boolean isAntiAlias() {
        return this.antiAlias;
    }
    
    public void setAntiAlias(final boolean antiAlias) {
        if (this.antiAlias != antiAlias) {
            this.antiAlias = antiAlias;
            this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
        }
    }
    
    protected void drawQuad(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
        final float n9 = n5 / 512.0f;
        final float n10 = n6 / 512.0f;
        final float n11 = n7 / 512.0f;
        final float n12 = n8 / 512.0f;
        GL11.glTexCoord2f(n9 + n11, n10);
        GL11.glVertex2d((double)(n + n3), (double)n2);
        GL11.glTexCoord2f(n9, n10);
        GL11.glVertex2d((double)n, (double)n2);
        GL11.glTexCoord2f(n9, n10 + n12);
        GL11.glVertex2d((double)n, (double)(n2 + n4));
        GL11.glTexCoord2f(n9, n10 + n12);
        GL11.glVertex2d((double)n, (double)(n2 + n4));
        GL11.glTexCoord2f(n9 + n11, n10 + n12);
        GL11.glVertex2d((double)(n + n3), (double)(n2 + n4));
        GL11.glTexCoord2f(n9 + n11, n10);
        GL11.glVertex2d((double)(n + n3), (double)n2);
    }
    
    public boolean isFractionalMetrics() {
        return this.fractionalMetrics;
    }
    
    public void drawChar(final CharData[] array, final char c, final float n, final float n2) throws ArrayIndexOutOfBoundsException {
        try {
            this.drawQuad(n, n2, (float)array[c].width, (float)array[c].height, (float)array[c].storedX, (float)array[c].storedY, (float)array[c].width, (float)array[c].height);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        if (this.fractionalMetrics != fractionalMetrics) {
            this.fractionalMetrics = fractionalMetrics;
            this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
        }
    }
    
    protected DynamicTexture setupTexture(final Font font, final boolean b, final boolean b2, final CharData[] array) {
        final BufferedImage generateFontImage = this.generateFontImage(font, b, b2, array);
        try {
            return new DynamicTexture(generateFontImage);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public void setFont(final Font font) {
        this.font = font;
        this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
    }
    
    public CFont(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.charData = new CharData[256];
        this.fontHeight = -1;
        this.charOffset = 0;
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
    }
    
    protected BufferedImage generateFontImage(final Font font, final boolean b, final boolean b2, final CharData[] array) {
        this.getClass();
        final int n = 512;
        final BufferedImage bufferedImage = new BufferedImage(n, n, 2);
        final Graphics2D context = (Graphics2D)bufferedImage.getGraphics();
        context.setFont(font);
        context.setColor(new Color(255, 255, 255, 0));
        context.fillRect(0, 0, n, n);
        context.setColor(Color.WHITE);
        context.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, b2 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        context.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, b ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        context.setRenderingHint(RenderingHints.KEY_ANTIALIASING, b ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        final FontMetrics fontMetrics = context.getFontMetrics();
        int height = 0;
        int storedX = 0;
        int storedY = 1;
        for (int i = 0; i < array.length; ++i) {
            final char c = (char)i;
            final CharData charData = new CharData();
            final Rectangle2D stringBounds = fontMetrics.getStringBounds(String.valueOf(c), context);
            charData.width = stringBounds.getBounds().width + 8;
            charData.height = stringBounds.getBounds().height;
            if (storedX + charData.width >= n) {
                storedX = 0;
                storedY += height;
                height = 0;
            }
            if (charData.height > height) {
                height = charData.height;
            }
            charData.storedX = storedX;
            charData.storedY = storedY;
            if (charData.height > this.fontHeight) {
                this.fontHeight = charData.height;
            }
            array[i] = charData;
            context.drawString(String.valueOf(c), storedX + 2, storedY + fontMetrics.getAscent());
            storedX += charData.width;
        }
        return bufferedImage;
    }
    
    public int getStringHeight(final String s) {
        return this.getHeight();
    }
    
    public int getStringWidth(final String s) {
        int n = 0;
        for (final char c : s.toCharArray()) {
            if (c < this.charData.length && c >= '\0') {
                n += this.charData[c].width - 8 + this.charOffset;
            }
        }
        return n / 2;
    }
    
    public Font getFont() {
        return this.font;
    }
    
    public int getHeight() {
        return (this.fontHeight - 8) / 2;
    }
    
    protected class CharData
    {
        public /* synthetic */ int width;
        public /* synthetic */ int storedX;
        public /* synthetic */ int height;
        public /* synthetic */ int storedY;
    }
}
