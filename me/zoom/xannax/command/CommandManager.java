// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command;

import me.zoom.xannax.command.commands.DrawnCommand;
import me.zoom.xannax.command.commands.FriendCommand;
import me.zoom.xannax.command.commands.EnemyCommand;
import me.zoom.xannax.command.commands.BindCommand;
import me.zoom.xannax.command.commands.FontCommand;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;

public class CommandManager
{
    private static /* synthetic */ ArrayList<Command> commands;
    /* synthetic */ boolean b;
    
    public static void addCommand(final Command e) {
        CommandManager.commands.add(e);
    }
    
    public void callCommand(final String s) {
        s.substring(s.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")[0].length()).trim();
        this.b = false;
        final String[] array;
        int length;
        int i = 0;
        final String anotherString;
        final String s2;
        CommandManager.commands.forEach(command -> {
            command.getAlias();
            for (length = array.length; i < length; ++i) {
                if (array[i].equalsIgnoreCase(anotherString)) {
                    this.b = true;
                    try {
                        command.onCommand(s2, s2.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                    }
                    catch (Exception ex) {
                        Command.sendClientMessage(ChatFormatting.GRAY + command.getSyntax());
                    }
                }
            }
            return;
        });
        if (!this.b) {
            Command.sendClientMessage(ChatFormatting.GRAY + "Unknown command!");
        }
    }
    
    public static ArrayList<Command> getCommands() {
        return CommandManager.commands;
    }
    
    public static void initCommands() {
        CommandManager.commands = new ArrayList<Command>();
        addCommand(new FontCommand());
        addCommand(new BindCommand());
        addCommand(new EnemyCommand());
        addCommand(new FriendCommand());
        addCommand(new DrawnCommand());
    }
}
