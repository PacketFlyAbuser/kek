// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.command.Command;

public class DrawnCommand extends Command
{
    /* synthetic */ boolean found;
    
    @Override
    public String getSyntax() {
        return "drawn <Module>";
    }
    
    @Override
    public String[] getAlias() {
        return new String[] { "drawn", "visible", "d" };
    }
    
    @Override
    public void onCommand(final String s, final String[] array) throws Exception {
        this.found = false;
        ModuleManager.getModules().forEach(module -> {
            if (module.getName().equalsIgnoreCase(array[0])) {
                if (module.isDrawn()) {
                    module.setDrawn(false);
                    this.found = true;
                    Command.sendClientMessage(module.getName() + ChatFormatting.RED + " drawn = false");
                }
                else if (!module.isDrawn()) {
                    module.setDrawn(true);
                    this.found = true;
                    Command.sendClientMessage(module.getName() + ChatFormatting.GREEN + " drawn = true");
                }
            }
            return;
        });
        if (!this.found && array.length == 1) {
            Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!");
        }
    }
}
