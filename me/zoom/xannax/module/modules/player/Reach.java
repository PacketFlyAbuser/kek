// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Reach extends Module
{
    public static /* synthetic */ Setting.Double reach;
    public static /* synthetic */ Setting.Double add;
    public static /* synthetic */ Setting.Boolean override;
    
    @Override
    public void setup() {
        Reach.override = this.registerBoolean("Override", "Override", false);
        Reach.add = this.registerDouble("Add", "AddR", 1.0, 0.0, 3.0);
        Reach.reach = this.registerDouble("Reach", "Reach", 1.0, 0.0, 6.0);
    }
    
    @Override
    public String getHudInfo() {
        String s;
        if (Reach.override.getValue()) {
            s = "[" + ChatFormatting.WHITE + Reach.reach.getValue() + ChatFormatting.GRAY + "]";
        }
        else {
            s = "[" + ChatFormatting.WHITE + Reach.add.getValue() + ChatFormatting.GRAY + "]";
        }
        return s;
    }
    
    public Reach() {
        super("Reach", "Reach", Category.Player);
    }
}
