//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.font;

import me.zoom.xannax.Xannax;
import net.minecraft.client.Minecraft;

public class FontUtils
{
    private static final /* synthetic */ Minecraft mc;
    
    public static int getFontHeight(final boolean b) {
        if (b) {
            return Xannax.fontRenderer.getHeight();
        }
        return FontUtils.mc.fontRenderer.FONT_HEIGHT;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static float drawStringWithShadow(final boolean b, final String s, final int n, final int n2, final int n3) {
        if (b) {
            return Xannax.fontRenderer.drawStringWithShadow(s, n, n2, n3);
        }
        return (float)FontUtils.mc.fontRenderer.drawStringWithShadow(s, (float)n, (float)n2, n3);
    }
    
    public static float drawKeyStringWithShadow(final boolean b, final String s, final int n, final int n2, final int n3) {
        if (b) {
            return Xannax.fontRenderer.drawStringWithShadow(s, n, n2, n3);
        }
        return (float)FontUtils.mc.fontRenderer.drawStringWithShadow(s, (float)n, (float)n2, n3);
    }
    
    public static int getStringWidth(final boolean b, final String s) {
        if (b) {
            return Xannax.fontRenderer.getStringWidth(s);
        }
        return FontUtils.mc.fontRenderer.getStringWidth(s);
    }
}
