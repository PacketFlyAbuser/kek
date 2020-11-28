//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import me.zoom.xannax.macro.Macro;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.util.friend.Friend;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.enemy.Enemy;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.clickgui.Frames;
import me.zoom.xannax.clickgui.ClickGUI;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import net.minecraft.client.Minecraft;
import java.io.File;

public class SaveConfiguration
{
    public static /* synthetic */ File Combat;
    public static /* synthetic */ File Render;
    public static /* synthetic */ File Player;
    public static /* synthetic */ File Movement;
    public static /* synthetic */ File Client;
    public static /* synthetic */ File Misc;
    public static /* synthetic */ File Messages;
    public static /* synthetic */ File GameSenseDev;
    /* synthetic */ Minecraft mc;
    public static /* synthetic */ File Modules;
    public static /* synthetic */ File Miscellaneous;
    
    public static void saveBinds() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ModuleBinds.json")));
            for (final Module module : ModuleManager.getModules()) {
                bufferedWriter.write(module.getName() + ":" + Keyboard.getKeyName(module.getBind()));
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveGUI() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClickGUI.json")));
            for (final Frames frames : ClickGUI.frames) {
                bufferedWriter.write(frames.category + ":" + frames.getX() + ":" + frames.getY() + ":" + frames.isOpen());
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveEnemies() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Enemies.json")));
            final Iterator<Enemy> iterator = Enemies.getEnemies().iterator();
            while (iterator.hasNext()) {
                bufferedWriter.write(iterator.next().getName());
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveFriends() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Friends.json")));
            final Iterator<Friend> iterator = Friends.getFriends().iterator();
            while (iterator.hasNext()) {
                bufferedWriter.write(iterator.next().getName());
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveMessages() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Messages.getAbsolutePath(), "ClientMessages.json")));
            bufferedWriter.write(Command.MsgWaterMark + "");
            bufferedWriter.write(",");
            bufferedWriter.write(Command.cf.getName());
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void savePrefix() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CommandPrefix.json")));
            bufferedWriter.write(Command.getPrefix());
            bufferedWriter.write("\r\n");
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveDrawn() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "DrawnModules.json")));
            for (final Module module : ModuleManager.getModules()) {
                bufferedWriter.write(module.getName() + ":" + module.isDrawn());
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveFont() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CustomFont.json")));
            bufferedWriter.write(Xannax.fontRenderer.getFontName() + ":" + Xannax.fontRenderer.getFontSize());
            bufferedWriter.write("\r\n");
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public static void saveEnabled() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "EnabledModules.json")));
            for (final Module module : ModuleManager.getModules()) {
                if (module.isEnabled()) {
                    bufferedWriter.write(module.getName());
                    bufferedWriter.write("\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
    
    public SaveConfiguration() {
        this.mc = Minecraft.getMinecraft();
        SaveConfiguration.GameSenseDev = new File(this.mc.gameDir + File.separator + "XannaX");
        if (!SaveConfiguration.GameSenseDev.exists()) {
            SaveConfiguration.GameSenseDev.mkdirs();
        }
        SaveConfiguration.Modules = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules");
        if (!SaveConfiguration.Modules.exists()) {
            SaveConfiguration.Modules.mkdirs();
        }
        SaveConfiguration.Messages = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Messages");
        if (!SaveConfiguration.Messages.exists()) {
            SaveConfiguration.Messages.mkdirs();
        }
        SaveConfiguration.Miscellaneous = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Miscellaneous");
        if (!SaveConfiguration.Miscellaneous.exists()) {
            SaveConfiguration.Miscellaneous.mkdirs();
        }
        SaveConfiguration.Combat = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Combat");
        if (!SaveConfiguration.Combat.exists()) {
            SaveConfiguration.Combat.mkdirs();
        }
        SaveConfiguration.Player = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Player");
        if (!SaveConfiguration.Player.exists()) {
            SaveConfiguration.Player.mkdirs();
        }
        SaveConfiguration.Client = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Client");
        if (!SaveConfiguration.Client.exists()) {
            SaveConfiguration.Client.mkdirs();
        }
        SaveConfiguration.Misc = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Misc");
        if (!SaveConfiguration.Misc.exists()) {
            SaveConfiguration.Misc.mkdirs();
        }
        SaveConfiguration.Movement = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Movement");
        if (!SaveConfiguration.Movement.exists()) {
            SaveConfiguration.Movement.mkdirs();
        }
        SaveConfiguration.Render = new File(this.mc.gameDir + File.separator + "XannaX" + File.separator + "Modules" + File.separator + "Render");
        if (!SaveConfiguration.Render.exists()) {
            SaveConfiguration.Render.mkdirs();
        }
    }
    
    public static void saveMacros() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClientMacros.json")));
            for (final Macro macro : Xannax.getInstance().macroManager.getMacros()) {
                bufferedWriter.write(Keyboard.getKeyName(macro.getKey()) + ":" + macro.getValue().replace(" ", "_"));
                bufferedWriter.write("\r\n");
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
    }
}
