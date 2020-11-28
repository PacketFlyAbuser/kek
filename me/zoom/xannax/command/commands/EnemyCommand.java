// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.command.Command;

public class EnemyCommand extends Command
{
    @Override
    public void onCommand(final String s, final String[] array) throws Exception {
        if (array[0].equalsIgnoreCase("add")) {
            if (!Enemies.getEnemies().contains(Enemies.getEnemyByName(array[1]))) {
                Enemies.addEnemy(array[1]);
                Command.sendClientMessage(ChatFormatting.GRAY + "Added enemy with name " + array[1]);
            }
            else {
                Command.sendClientMessage(ChatFormatting.GRAY + array[1] + " is already an enemy!");
            }
        }
        else if (array[0].equalsIgnoreCase("del") || array[0].equalsIgnoreCase("remove")) {
            Enemies.delEnemy(array[1]);
            Command.sendClientMessage(ChatFormatting.GRAY + "Removed enemy with name " + array[1]);
        }
        else {
            Command.sendClientMessage(this.getSyntax());
        }
    }
    
    @Override
    public String[] getAlias() {
        return new String[] { "enemy", "enemies", "e" };
    }
    
    @Override
    public String getSyntax() {
        return "enemy <add | del> <name>";
    }
}
