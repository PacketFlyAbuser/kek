// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import org.lwjgl.input.Keyboard;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.command.Command;

public class BindCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "bind", "b" };
    }
    
    @Override
    public void onCommand(final String s, final String[] array) throws Exception {
        final int bind;
        ModuleManager.getModules().forEach(module -> {
            Keyboard.getKeyIndex(array[1].toUpperCase());
            if (array[0].equalsIgnoreCase(module.getName())) {
                module.setBind(bind);
                Command.sendClientMessage(array[0] + " bound to " + array[1].toUpperCase());
            }
        });
    }
    
    @Override
    public String getSyntax() {
        return "bind <Module> <Key>";
    }
}
