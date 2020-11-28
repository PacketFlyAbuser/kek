//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.modules.misc.Announcer;
import net.minecraft.client.gui.GuiScreen;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ClickGuiModule extends Module
{
    public static /* synthetic */ Setting.Integer red;
    public static /* synthetic */ Setting.Integer green;
    public static /* synthetic */ Setting.Integer blue;
    public static /* synthetic */ Setting.Integer opacity;
    
    public void onEnable() {
        ClickGuiModule.mc.displayGuiScreen((GuiScreen)Xannax.getInstance().clickGUI);
        if (((Announcer)ModuleManager.getModuleByName("Announcer")).clickGui.getValue() && ModuleManager.isModuleEnabled("Announcer") && ClickGuiModule.mc.player != null) {
            if (((Announcer)ModuleManager.getModuleByName("Announcer")).clientSide.getValue()) {
                Command.sendClientMessage(Announcer.guiMessage);
            }
            else {
                ClickGuiModule.mc.player.sendChatMessage(Announcer.guiMessage);
            }
        }
        this.disable();
    }
    
    @Override
    public void setup() {
        ClickGuiModule.red = this.registerInteger("Red", "RedHUD", 255, 0, 255);
        ClickGuiModule.green = this.registerInteger("Green", "GreenHUD", 255, 0, 255);
        ClickGuiModule.blue = this.registerInteger("Blue", "BlueHUD", 255, 0, 255);
        ClickGuiModule.opacity = this.registerInteger("Opacity", "Opacity", 200, 50, 255);
    }
    
    public ClickGuiModule() {
        super("ClickGUI", "ClickGUI", Category.Client);
        this.setDrawn(false);
        this.setBind(23);
    }
}
