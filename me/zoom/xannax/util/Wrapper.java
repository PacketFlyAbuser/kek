//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import org.lwjgl.input.Keyboard;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Wrapper
{
    private static /* synthetic */ FontRenderer fontRenderer;
    public static /* synthetic */ Minecraft mc;
    
    public static Entity getRenderEntity() {
        return Wrapper.mc.getRenderViewEntity();
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }
    
    public static World getWorld() {
        return (World)getMinecraft().world;
    }
    
    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }
    
    public static FontRenderer getFontRenderer() {
        return Wrapper.fontRenderer;
    }
    
    public static int getKey(final String s) {
        return Keyboard.getKeyIndex(s.toUpperCase());
    }
    
    static {
        Wrapper.mc = Minecraft.getMinecraft();
    }
}
