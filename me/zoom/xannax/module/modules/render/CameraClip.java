// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class CameraClip extends Module
{
    public static /* synthetic */ Setting.Double distance;
    public static /* synthetic */ Setting.Boolean extend;
    
    @Override
    public void setup() {
        CameraClip.extend = this.registerBoolean("Extend", "Extend", false);
        CameraClip.distance = this.registerDouble("Distance", "Distance", 10.0, 0.0, 50.0);
    }
    
    public CameraClip() {
        super("CameraClip", "CameraClip", Category.Render);
    }
}
