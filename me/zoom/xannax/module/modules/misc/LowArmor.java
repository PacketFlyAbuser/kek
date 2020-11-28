//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.item.ItemStack;
import me.zoom.xannax.util.Wrapper;
import java.awt.Color;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class LowArmor extends Module
{
    /* synthetic */ Setting.Integer green;
    /* synthetic */ Setting.Integer red;
    /* synthetic */ Setting.Integer x;
    /* synthetic */ Setting.Integer threshold;
    /* synthetic */ Setting.Integer y;
    /* synthetic */ Setting.Integer blue;
    
    @Override
    public void setup() {
        this.threshold = this.registerInteger("Percent", "Percent", 50, 0, 100);
        this.x = this.registerInteger("X", "X", 255, 0, 960);
        this.y = this.registerInteger("Y", "Y", 255, 0, 530);
        this.red = this.registerInteger("Red", "RedArmor", 255, 0, 255);
        this.green = this.registerInteger("Green", "GreenArmor", 255, 0, 255);
        this.blue = this.registerInteger("Blue", "BlueArmor", 255, 0, 255);
    }
    
    public LowArmor() {
        super("DurabilityWarning", "DurabilityWarning", Category.Misc);
    }
    
    public static int reverseNumber(final int n, final int n2, final int n3) {
        return n3 + n2 - n;
    }
    
    @Override
    public void onRender() {
        Color.HSBtoRGB((new float[] { System.currentTimeMillis() % 11520L / 11520.0f })[0], 1.0f, 1.0f);
        if (this.shouldMend(0) || this.shouldMend(1) || this.shouldMend(2) || this.shouldMend(3)) {
            final String string = "Armor Durability Is Below " + this.threshold.getValue() + "%";
            getScale();
            this.drawStringWithShadow(string, this.x.getValue(), this.y.getValue(), new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue()).getRGB());
        }
    }
    
    public static int getScale() {
        int n = 0;
        int guiScale = Wrapper.getMinecraft().gameSettings.guiScale;
        if (guiScale == 0) {
            guiScale = 1000;
        }
        while (n < guiScale && Wrapper.getMinecraft().displayWidth / (n + 1) >= 320 && Wrapper.getMinecraft().displayHeight / (n + 1) >= 240) {
            ++n;
        }
        if (n == 0) {
            n = 1;
        }
        return n;
    }
    
    private boolean shouldMend(final int n) {
        return ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(n)).getMaxDamage() != 0 && 100 * ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(n)).getItemDamage() / ((ItemStack)LowArmor.mc.player.inventory.armorInventory.get(n)).getMaxDamage() > reverseNumber(this.threshold.getValue(), 1, 100);
    }
    
    private void drawStringWithShadow(final String s, final int n, final int n2, final int n3) {
        if (ModuleManager.isModuleEnabled("CustomFont")) {
            Xannax.fontRenderer.drawStringWithShadow(s, n, n2, n3);
        }
        else {
            LowArmor.mc.fontRenderer.drawStringWithShadow(s, (float)n, (float)n2, n3);
        }
    }
}
