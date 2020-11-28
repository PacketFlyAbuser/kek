//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module;

import java.util.List;
import me.zoom.xannax.event.events.RenderEvent;
import me.zoom.xannax.Xannax;
import me.zoom.xannax.setting.Setting;
import net.minecraft.client.Minecraft;

public class Module
{
    /* synthetic */ String description;
    /* synthetic */ boolean enabled;
    /* synthetic */ boolean drawn;
    /* synthetic */ int bind;
    /* synthetic */ String name;
    /* synthetic */ Category category;
    
    public boolean isDrawn() {
        return this.drawn;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    protected Setting.Double registerDouble(final String s, final String s2, final double n, final double n2, final double n3) {
        final Setting.Double double1 = new Setting.Double(s, s2, this, this.getCategory(), n, n2, n3);
        Xannax.getInstance().settingsManager.addSetting(double1);
        return double1;
    }
    
    public void setCategory(final Category category) {
        this.category = category;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setDrawn(final boolean drawn) {
        this.drawn = drawn;
    }
    
    protected void onDisable() {
    }
    
    public Module(final String name, final String description, final Category category) {
        this.name = name;
        this.category = category;
        this.bind = 0;
        this.enabled = false;
        this.drawn = true;
        this.description = description;
        this.setup();
    }
    
    public int getBind() {
        return this.bind;
    }
    
    protected void onEnable() {
    }
    
    protected Setting.Integer registerInteger(final String s, final String s2, final int n, final int n2, final int n3) {
        final Setting.Integer integer = new Setting.Integer(s, s2, this, this.getCategory(), n, n2, n3);
        Xannax.getInstance().settingsManager.addSetting(integer);
        return integer;
    }
    
    public String getHudInfo() {
        return "";
    }
    
    protected Setting.Boolean registerBoolean(final String s, final String s2, final boolean b) {
        final Setting.Boolean boolean1 = new Setting.Boolean(s, s2, this, this.getCategory(), b);
        Xannax.getInstance().settingsManager.addSetting(boolean1);
        return boolean1;
    }
    
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        }
        else if (!this.isEnabled()) {
            this.enable();
        }
    }
    
    public void onWorldRender(final RenderEvent renderEvent) {
    }
    
    public void onUpdate() {
    }
    
    protected Setting.Mode registerMode(final String s, final String s2, final List<String> list, final String s3) {
        final Setting.Mode mode = new Setting.Mode(s, s2, this, this.getCategory(), list, s3);
        Xannax.getInstance().settingsManager.addSetting(mode);
        return mode;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
    }
    
    public void setup() {
    }
    
    public void disable() {
        this.setEnabled(false);
        this.onDisable();
    }
    
    public void enable() {
        this.setEnabled(true);
        this.onEnable();
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void onRender() {
    }
    
    public enum Category
    {
        Combat, 
        Render, 
        Client, 
        Player, 
        Movement, 
        Misc;
    }
}
