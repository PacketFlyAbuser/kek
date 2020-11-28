//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.macro;

import net.minecraft.client.Minecraft;

public class Macro
{
    /* synthetic */ String value;
    /* synthetic */ int key;
    
    public int getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public Macro(final int key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public void onMacro() {
        if (Minecraft.getMinecraft().player != null) {
            Minecraft.getMinecraft().player.sendChatMessage(this.value);
        }
    }
}
