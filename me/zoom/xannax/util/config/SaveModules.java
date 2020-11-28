// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import java.util.Iterator;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.Xannax;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class SaveModules
{
    public void saveModules() {
        this.saveCombat();
        this.savePlayer();
        this.saveClient();
        this.saveMisc();
        this.saveMovement();
        this.saveRender();
    }
    
    public void saveCombat() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Combat.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Combat)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Combat.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Combat)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Combat.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Combat)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
    
    public void saveRender() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Render.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Render)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Render.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Render)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Render.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Render)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
    
    public void saveClient() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Client.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Client)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Client.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Client)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Client.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Client)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
    
    public void saveMovement() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Movement.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Movement)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Movement.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Movement)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Movement.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Movement)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
    
    public void savePlayer() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Player.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Player)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Player.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Player)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Player.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Player)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
    
    public void saveMisc() {
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Misc.getAbsolutePath(), "Value.json")));
            for (final Setting setting : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Misc)) {
                if (setting.getType() == Setting.Type.DOUBLE) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Double)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
                if (setting.getType() == Setting.Type.INT) {
                    bufferedWriter.write(setting.getConfigName() + ":" + ((Setting.Integer)setting).getValue() + ":" + setting.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter.close();
        }
        catch (Exception ex) {}
        try {
            final BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Misc.getAbsolutePath(), "Boolean.json")));
            for (final Setting setting2 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Misc)) {
                if (setting2.getType() == Setting.Type.BOOLEAN) {
                    bufferedWriter2.write(setting2.getConfigName() + ":" + ((Setting.Boolean)setting2).getValue() + ":" + setting2.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter2.close();
        }
        catch (Exception ex2) {}
        try {
            final BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(new File(SaveConfiguration.Misc.getAbsolutePath(), "String.json")));
            for (final Setting setting3 : Xannax.getInstance().settingsManager.getSettingsByCategory(Module.Category.Misc)) {
                if (setting3.getType() == Setting.Type.MODE) {
                    bufferedWriter3.write(setting3.getConfigName() + ":" + ((Setting.Mode)setting3).getValue() + ":" + setting3.getParent().getName() + "\r\n");
                }
            }
            bufferedWriter3.close();
        }
        catch (Exception ex3) {}
    }
}
