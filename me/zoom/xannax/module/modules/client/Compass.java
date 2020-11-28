//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.util.math.MathHelper;
import me.zoom.xannax.util.Wrapper;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import me.zoom.xannax.module.Module;

public class Compass extends Module
{
    /* synthetic */ ScaledResolution resolution;
    /* synthetic */ Setting.Double scale;
    
    @Override
    public void setup() {
        this.scale = this.registerDouble("Scale", "Scale", 3.0, 1.0, 5.0);
    }
    
    public Compass() {
        super("Compass", "Compass", Category.Client);
        this.resolution = new ScaledResolution(Compass.mc);
    }
    
    @Override
    public void onRender() {
        final double n = this.resolution.getScaledWidth() * 1.11;
        final double n2 = this.resolution.getScaledHeight_double() * 1.8;
        for (final Direction direction : Direction.values()) {
            final double posOnCompass = getPosOnCompass(direction);
            this.drawStringWithShadow(direction.name(), (int)(n + this.getX(posOnCompass)), (int)(n2 + this.getY(posOnCompass)), (direction == Direction.N) ? new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), 255).getRGB() : new Color(255, 255, 255, 255).getRGB());
        }
    }
    
    private double getY(final double a) {
        return Math.cos(a) * Math.sin(Math.toRadians(MathHelper.clamp(Wrapper.getRenderEntity().rotationPitch + 30.0f, -90.0f, 90.0f))) * (this.scale.getValue() * 10.0);
    }
    
    private void drawStringWithShadow(final String s, final int n, final int n2, final int n3) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            Xannax.fontRenderer.drawStringWithShadow(s, n, n2, n3);
        }
        else {
            Compass.mc.fontRenderer.drawStringWithShadow(s, (float)n, (float)n2, n3);
        }
    }
    
    private double getX(final double a) {
        return Math.sin(a) * (this.scale.getValue() * 10.0);
    }
    
    private static double getPosOnCompass(final Direction direction) {
        return Math.toRadians(MathHelper.wrapDegrees(Wrapper.getRenderEntity().rotationYaw)) + direction.ordinal() * 1.5707963267948966;
    }
    
    private enum Direction
    {
        W, 
        N, 
        S, 
        E;
    }
}
