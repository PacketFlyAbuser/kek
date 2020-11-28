// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.Xannax;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.command.Command;

public class FriendCommand extends Command
{
    @Override
    public String getSyntax() {
        return "friend <add | del> <Name>";
    }
    
    @Override
    public void onCommand(final String s, final String[] array) throws Exception {
        if (array[0].equalsIgnoreCase("add")) {
            if (Friends.isFriend(array[1])) {
                Command.sendClientMessage(array[1] + ChatFormatting.GRAY + " is already a friend!");
                return;
            }
            if (!Friends.isFriend(array[1])) {
                Xannax.getInstance().friends.addFriend(array[1]);
                Command.sendClientMessage("Added " + array[1] + " to friends list");
            }
        }
        if (array[0].equalsIgnoreCase("del") || array[0].equalsIgnoreCase("remove")) {
            if (!Friends.isFriend(array[1])) {
                Command.sendClientMessage(array[1] + " is not a friend!");
                return;
            }
            if (Friends.isFriend(array[1])) {
                Xannax.getInstance().friends.delFriend(array[1]);
                Command.sendClientMessage("Removed " + array[1] + " from friends list");
            }
        }
    }
    
    @Override
    public String[] getAlias() {
        return new String[] { "friend", "friends", "f" };
    }
}
