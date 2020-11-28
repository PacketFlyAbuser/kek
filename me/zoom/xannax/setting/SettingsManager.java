// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.setting;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Iterator;
import me.zoom.xannax.module.Module;
import java.util.List;

public class SettingsManager
{
    private final /* synthetic */ List<Setting> settings;
    
    public Setting getSettingByName(final String s) {
        for (final Setting setting : this.getSettings()) {
            if (setting.getName().equalsIgnoreCase(s)) {
                return setting;
            }
        }
        System.err.println("[XannaX] Error Setting NOT found: '" + s + "'!");
        return null;
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public SettingsManager() {
        this.settings = new ArrayList<Setting>();
    }
    
    public Setting getSettingByNameAndModConfig(final String anotherString, final Module obj) {
        return this.settings.stream().filter(setting -> setting.getParent().equals(obj)).filter(setting2 -> setting2.getConfigName().equalsIgnoreCase(anotherString)).findFirst().orElse(null);
    }
    
    public Setting getSettingByNameAndMod(final String anotherString, final Module obj) {
        return this.settings.stream().filter(setting -> setting.getParent().equals(obj)).filter(setting2 -> setting2.getConfigName().equalsIgnoreCase(anotherString)).findFirst().orElse(null);
    }
    
    public List<Setting> getSettingsByCategory(final Module.Category other) {
        return this.settings.stream().filter(setting -> setting.getCategory().equals(other)).collect((Collector<? super Object, ?, List<Setting>>)Collectors.toList());
    }
    
    public void addSetting(final Setting setting) {
        this.settings.add(setting);
    }
    
    public List<Setting> getSettingsForMod(final Module obj) {
        return this.settings.stream().filter(setting -> setting.getParent().equals(obj)).collect((Collector<? super Object, ?, List<Setting>>)Collectors.toList());
    }
}
