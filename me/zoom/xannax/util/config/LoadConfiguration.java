// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.enemy.Enemies;
import me.zoom.xannax.util.font.CFontRenderer;
import java.awt.Font;
import me.zoom.xannax.macro.Macro;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.clickgui.Frames;
import me.zoom.xannax.clickgui.ClickGUI;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;

public class LoadConfiguration
{
    public void loadGUI() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClickGUI.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String s3 = trim.split(":")[2];
                final String s4 = trim.split(":")[3];
                final int int1 = Integer.parseInt(s2);
                final int int2 = Integer.parseInt(s3);
                final boolean boolean1 = Boolean.parseBoolean(s4);
                final Frames frameByName = ClickGUI.getFrameByName(s);
                if (frameByName != null) {
                    frameByName.x = int1;
                    frameByName.y = int2;
                    frameByName.open = boolean1;
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveGUI();
        }
    }
    
    public void loadBinds() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ModuleBinds.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String anotherString = trim.split(":")[0];
                final String s = trim.split(":")[1];
                for (final Module module : ModuleManager.getModules()) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        module.setBind(Keyboard.getKeyIndex(s));
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveBinds();
        }
    }
    
    public void loadMessages() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Messages.getAbsolutePath(), "ClientMessages.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(",")[0];
                final String s2 = trim.split(",")[1];
                final boolean boolean1 = Boolean.parseBoolean(s);
                Command.cf = ChatFormatting.getByName(s2);
                Command.MsgWaterMark = boolean1;
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveMessages();
        }
    }
    
    public void loadDrawn() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "DrawnModules.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String anotherString = trim.split(":")[0];
                final boolean boolean1 = Boolean.parseBoolean(trim.split(":")[1]);
                for (final Module module : ModuleManager.getModules()) {
                    if (module.getName().equalsIgnoreCase(anotherString)) {
                        module.setDrawn(boolean1);
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveDrawn();
        }
    }
    
    public LoadConfiguration() {
        this.loadBinds();
        this.loadDrawn();
        this.loadEnabled();
        this.loadEnemies();
        this.loadFont();
        this.loadFriends();
        this.loadGUI();
        this.loadMacros();
        this.loadMessages();
        this.loadPrefix();
    }
    
    public void loadMacros() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClientMacros.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                Xannax.getInstance().macroManager.addMacro(new Macro(Keyboard.getKeyIndex(trim.split(":")[0]), trim.split(":")[1].replace("_", " ")));
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveMacros();
        }
    }
    
    public void loadFont() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CustomFont.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String fontName = line.split(":")[0];
                final int int1 = Integer.parseInt(line.split(":")[1]);
                (Xannax.fontRenderer = new CFontRenderer(new Font(fontName, 0, int1), true, false)).setFont(new Font(fontName, 0, int1));
                Xannax.fontRenderer.setAntiAlias(true);
                Xannax.fontRenderer.setFractionalMetrics(false);
                Xannax.fontRenderer.setFontName(fontName);
                Xannax.fontRenderer.setFontSize(int1);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveFont();
        }
    }
    
    public void loadEnemies() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Enemies.json").getAbsolutePath()))));
            Enemies.enemies.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Enemies.addEnemy(line);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveEnemies();
        }
    }
    
    public void loadEnabled() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "EnabledModules.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                for (final Module module : ModuleManager.getModules()) {
                    if (module.getName().equals(line)) {
                        module.enable();
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveEnabled();
        }
    }
    
    public void loadPrefix() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CommandPrefix.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Command.setPrefix(line);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.savePrefix();
        }
    }
    
    public void loadFriends() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Friends.json").getAbsolutePath()))));
            Friends.friends.clear();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Xannax.getInstance().friends.addFriend(line);
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            SaveConfiguration.saveFriends();
        }
    }
}
