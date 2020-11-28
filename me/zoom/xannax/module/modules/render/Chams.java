// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Chams extends Module
{
    public static /* synthetic */ Setting.Integer green;
    public static /* synthetic */ Setting.Boolean lines;
    public static /* synthetic */ Setting.Boolean crystal;
    public static /* synthetic */ Setting.Integer blue;
    public static /* synthetic */ Setting.Integer brightness;
    public static /* synthetic */ Setting.Boolean rainbow;
    public static /* synthetic */ Setting.Integer speed;
    public static /* synthetic */ Setting.Integer saturation;
    public static /* synthetic */ Setting.Integer width;
    public static /* synthetic */ Setting.Integer alpha;
    public static /* synthetic */ Setting.Integer red;
    
    public Chams() {
        super("Chams", "Chams", Category.Render);
    }
    
    @Override
    public void setup() {
        Chams.crystal = this.registerBoolean("Crystal", "Crystal", false);
        Chams.lines = this.registerBoolean("Lines", "Lines", false);
        Chams.width = this.registerInteger("Width", "Width", 1, 0, 10);
        Chams.red = this.registerInteger("Red", "Red", 255, 0, 255);
        Chams.green = this.registerInteger("Green", "Green", 255, 0, 255);
        Chams.blue = this.registerInteger("Blue", "Blue", 255, 0, 255);
        Chams.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        Chams.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        Chams.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        Chams.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
        Chams.alpha = this.registerInteger("Alpha", "Alpha", 50, 0, 255);
    }
}
