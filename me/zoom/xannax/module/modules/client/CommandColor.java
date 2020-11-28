// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.client;

import java.util.List;
import java.util.ArrayList;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class CommandColor extends Module
{
    public static /* synthetic */ Setting.Mode CommandColor;
    public static /* synthetic */ Setting.Mode BracketColor;
    
    public static ChatFormatting getBrackets() {
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Black")) {
            return ChatFormatting.BLACK;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Green")) {
            return ChatFormatting.DARK_GREEN;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Red")) {
            return ChatFormatting.DARK_RED;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Gold")) {
            return ChatFormatting.GOLD;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Gray")) {
            return ChatFormatting.DARK_GRAY;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Green")) {
            return ChatFormatting.GREEN;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Red")) {
            return ChatFormatting.RED;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Yellow")) {
            return ChatFormatting.YELLOW;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Blue")) {
            return ChatFormatting.DARK_BLUE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Aqua")) {
            return ChatFormatting.DARK_AQUA;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Dark Purple")) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Gray")) {
            return ChatFormatting.GRAY;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Blue")) {
            return ChatFormatting.BLUE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Light Purple")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("White")) {
            return ChatFormatting.WHITE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.BracketColor.getValue().equalsIgnoreCase("Aqua")) {
            return ChatFormatting.AQUA;
        }
        return null;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Black");
        list.add("Dark Green");
        list.add("Dark Red");
        list.add("Gold");
        list.add("Dark Gray");
        list.add("Green");
        list.add("Red");
        list.add("Yellow");
        list.add("Dark Blue");
        list.add("Dark Aqua");
        list.add("Dark Purple");
        list.add("Gray");
        list.add("Blue");
        list.add("Aqua");
        list.add("Light Purple");
        list.add("White");
        final ArrayList<String> list2 = new ArrayList<String>();
        list2.add("Black");
        list2.add("Dark Green");
        list2.add("Dark Red");
        list2.add("Gold");
        list2.add("Dark Gray");
        list2.add("Green");
        list2.add("Red");
        list2.add("Yellow");
        list2.add("Dark Blue");
        list2.add("Dark Aqua");
        list2.add("Dark Purple");
        list2.add("Gray");
        list2.add("Blue");
        list2.add("Aqua");
        list2.add("Light Purple");
        list2.add("White");
        me.zoom.xannax.module.modules.client.CommandColor.CommandColor = this.registerMode("Text", "Color", list, "Light Purple");
        me.zoom.xannax.module.modules.client.CommandColor.BracketColor = this.registerMode("Brackets", "BracketColor", list2, "Light Purple");
    }
    
    public static ChatFormatting getTextColor() {
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Black")) {
            return ChatFormatting.BLACK;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Green")) {
            return ChatFormatting.DARK_GREEN;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Red")) {
            return ChatFormatting.DARK_RED;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Gold")) {
            return ChatFormatting.GOLD;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Gray")) {
            return ChatFormatting.DARK_GRAY;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Green")) {
            return ChatFormatting.GREEN;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Red")) {
            return ChatFormatting.RED;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Yellow")) {
            return ChatFormatting.YELLOW;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Blue")) {
            return ChatFormatting.DARK_BLUE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Aqua")) {
            return ChatFormatting.DARK_AQUA;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Dark Purple")) {
            return ChatFormatting.DARK_PURPLE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Gray")) {
            return ChatFormatting.GRAY;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Blue")) {
            return ChatFormatting.BLUE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Light Purple")) {
            return ChatFormatting.LIGHT_PURPLE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("White")) {
            return ChatFormatting.WHITE;
        }
        if (me.zoom.xannax.module.modules.client.CommandColor.CommandColor.getValue().equalsIgnoreCase("Aqua")) {
            return ChatFormatting.AQUA;
        }
        return null;
    }
    
    public CommandColor() {
        super("CommandColor", "CommandColor", Category.Client);
    }
}
