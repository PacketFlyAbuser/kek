// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ESP extends Module
{
    public static /* synthetic */ Setting.Integer redd;
    public static /* synthetic */ Setting.Boolean rainbow;
    public static /* synthetic */ Setting.Integer brightness;
    public static /* synthetic */ Setting.Integer speed;
    public static /* synthetic */ Setting.Integer greenn;
    public static /* synthetic */ Setting.Integer bluee;
    public static /* synthetic */ Setting.Double width;
    public static /* synthetic */ Setting.Boolean crystal;
    public static /* synthetic */ Setting.Mode mode;
    public static /* synthetic */ Setting.Integer saturation;
    public static /* synthetic */ Setting.Boolean self;
    
    @Override
    public void setup() {
        ESP.width = this.registerDouble("Width", "Width", 3.0, 0.1, 10.0);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("OutLine");
        list.add("WireFrame");
        ESP.mode = this.registerMode("RenderMode", "RenderMode", list, "WireFrame");
        ESP.self = this.registerBoolean("Self", "Self", false);
        ESP.crystal = this.registerBoolean("Crystal", "Crystal", false);
        ESP.redd = this.registerInteger("Red", "RedESP", 255, 0, 255);
        ESP.greenn = this.registerInteger("Green", "GreenESP", 255, 0, 255);
        ESP.bluee = this.registerInteger("Blue", "BlueESP", 255, 0, 255);
        ESP.rainbow = this.registerBoolean("Rainbow", "Rainbow", false);
        ESP.saturation = this.registerInteger("Saturation", "Saturation", 50, 0, 100);
        ESP.brightness = this.registerInteger("Brightness", "Brightness", 50, 0, 100);
        ESP.speed = this.registerInteger("Speed", "Speed", 50, 1, 100);
    }
    
    public ESP() {
        super("ESP", "ESP", Category.Render);
    }
}
