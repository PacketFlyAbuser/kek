//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import me.zoom.xannax.module.modules.client.CommandColor;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.gui.ChatFormatting;

public abstract class Command
{
    public static /* synthetic */ ChatFormatting cf;
    public static /* synthetic */ String prefix;
    public static /* synthetic */ boolean MsgWaterMark;
    static /* synthetic */ Minecraft mc;
    
    public abstract String getSyntax();
    
    public static Color getColorFromChatFormatting(final ChatFormatting chatFormatting) {
        if (chatFormatting == ChatFormatting.BLACK) {
            return Color.BLACK;
        }
        if (chatFormatting == ChatFormatting.GRAY) {
            return Color.GRAY;
        }
        if (chatFormatting == ChatFormatting.AQUA) {
            return Color.CYAN;
        }
        if (chatFormatting == ChatFormatting.BLUE || chatFormatting == ChatFormatting.DARK_BLUE || chatFormatting == ChatFormatting.DARK_AQUA) {
            return Color.BLUE;
        }
        if (chatFormatting == ChatFormatting.DARK_GRAY) {
            return Color.DARK_GRAY;
        }
        if (chatFormatting == ChatFormatting.DARK_GREEN || chatFormatting == ChatFormatting.GREEN) {
            return Color.GREEN;
        }
        if (chatFormatting == ChatFormatting.DARK_PURPLE) {
            return Color.MAGENTA;
        }
        if (chatFormatting == ChatFormatting.RED || chatFormatting == ChatFormatting.DARK_RED) {
            return Color.RED;
        }
        if (chatFormatting == ChatFormatting.LIGHT_PURPLE) {
            return Color.PINK;
        }
        if (chatFormatting == ChatFormatting.YELLOW) {
            return Color.YELLOW;
        }
        if (chatFormatting == ChatFormatting.GOLD) {
            return Color.ORANGE;
        }
        return Color.WHITE;
    }
    
    static {
        Command.mc = Minecraft.getMinecraft();
        Command.prefix = ";";
        Command.MsgWaterMark = true;
        Command.cf = ChatFormatting.GRAY;
    }
    
    public static String getPrefix() {
        return Command.prefix;
    }
    
    public abstract String[] getAlias();
    
    public static void sendClientMessage(final String s) {
        if (Command.MsgWaterMark) {
            Command.mc.player.sendMessage((ITextComponent)new TextComponentString(CommandColor.getBrackets() + "[" + CommandColor.getTextColor() + "XannaX" + CommandColor.getBrackets() + "] " + ChatFormatting.RESET + Command.cf + s));
        }
        else {
            Command.mc.player.sendMessage((ITextComponent)new TextComponentString(Command.cf + s));
        }
    }
    
    public static void setPrefix(final String prefix) {
        Command.prefix = prefix;
    }
    
    public abstract void onCommand(final String p0, final String[] p1) throws Exception;
    
    public static void sendRawMessage(final String s) {
        Command.mc.player.sendMessage((ITextComponent)new TextComponentString(s));
    }
}
