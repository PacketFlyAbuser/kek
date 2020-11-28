// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import java.util.Iterator;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.module.Module;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;

public class LoadModules
{
    public void loadCombat() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Combat.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Combat.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Combat.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Combat)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    public void loadPlayer() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Player.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Player)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Player.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Player)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Player.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Player)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    public void loadRender() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Render.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Render)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Render.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Render)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Render.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Render)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    public LoadModules() {
        this.loadCombat();
        this.loadPlayer();
        this.loadClient();
        this.loadMisc();
        this.loadMovement();
        this.loadRender();
    }
    
    public void loadClient() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Client.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Client)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Client.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Client)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Client.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Client)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    public void loadMisc() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Misc.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Misc.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Misc.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Misc)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    public void loadMovement() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Movement.getAbsolutePath(), "Value.json").getAbsolutePath()))));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final String trim = line.trim();
                final String s = trim.split(":")[0];
                final String s2 = trim.split(":")[1];
                final String anotherString = trim.split(":")[2];
                for (final Module module : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
                    if (module != null && module.getName().equalsIgnoreCase(anotherString)) {
                        final Setting settingByNameAndModConfig = Xannax.getInstance().settingsManager.getSettingByNameAndModConfig(s, module);
                        if (settingByNameAndModConfig instanceof Setting.Integer) {
                            ((Setting.Integer)settingByNameAndModConfig).setValue(Integer.parseInt(s2));
                        }
                        else {
                            if (!(settingByNameAndModConfig instanceof Setting.Double)) {
                                continue;
                            }
                            ((Setting.Double)settingByNameAndModConfig).setValue(Double.parseDouble(s2));
                        }
                    }
                }
            }
            bufferedReader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Movement.getAbsolutePath(), "Boolean.json").getAbsolutePath()))));
            String line2;
            while ((line2 = bufferedReader2.readLine()) != null) {
                final String trim2 = line2.trim();
                final String s3 = trim2.split(":")[0];
                final String s4 = trim2.split(":")[1];
                final String anotherString2 = trim2.split(":")[2];
                for (final Module module2 : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
                    if (module2 != null && module2.getName().equalsIgnoreCase(anotherString2)) {
                        ((Setting.Boolean)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s3, module2)).setValue(Boolean.parseBoolean(s4));
                    }
                }
            }
            bufferedReader2.close();
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(SaveConfiguration.Movement.getAbsolutePath(), "String.json").getAbsolutePath()))));
            String line3;
            while ((line3 = bufferedReader3.readLine()) != null) {
                final String trim3 = line3.trim();
                final String s5 = trim3.split(":")[0];
                final String value = trim3.split(":")[1];
                final String anotherString3 = trim3.split(":")[2];
                for (final Module module3 : ModuleManager.getModulesInCategory(Module.Category.Movement)) {
                    if (module3 != null && module3.getName().equalsIgnoreCase(anotherString3)) {
                        ((Setting.Mode)Xannax.getInstance().settingsManager.getSettingByNameAndMod(s5, module3)).setValue(value);
                    }
                }
            }
            bufferedReader3.close();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
}
