// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class HandColor extends Module
{
    public static /* synthetic */ Setting.Integer saturation;
    public static /* synthetic */ Setting.Integer redh;
    public static /* synthetic */ Setting.Integer speed;
    public static /* synthetic */ Setting.Integer brightness;
    public static /* synthetic */ Setting.Integer blueh;
    public static /* synthetic */ Setting.Boolean rainbow;
    public static /* synthetic */ Setting.Integer greenh;
    public static /* synthetic */ Setting.Integer alphah;
    
    public HandColor() {
        super("HandColor", "HandColor", Category.Render);
    }
    
    @Override
    public void setup() {
        HandColor.redh = this.registerInteger("Red", "RedH", 255, 0, 255);
        HandColor.greenh = this.registerInteger("Green", "GreenH", 255, 0, 255);
        HandColor.blueh = this.registerInteger("Blue", "BlueH", 255, 0, 255);
        HandColor.alphah = this.registerInteger("Alpha", "AlphaH", 50, 0, 255);
        HandColor.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        HandColor.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        HandColor.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        HandColor.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
}
