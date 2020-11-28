// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util.config;

import me.zoom.xannax.Xannax;

public class Stopper extends Thread
{
    public static void saveConfig() {
        Xannax.getInstance().saveModules.saveModules();
        SaveConfiguration.saveBinds();
        SaveConfiguration.saveDrawn();
        SaveConfiguration.saveEnabled();
        SaveConfiguration.saveEnemies();
        SaveConfiguration.saveFont();
        SaveConfiguration.saveFriends();
        SaveConfiguration.saveGUI();
        SaveConfiguration.saveMacros();
        SaveConfiguration.saveMessages();
        SaveConfiguration.savePrefix();
    }
    
    @Override
    public void run() {
        saveConfig();
    }
}
