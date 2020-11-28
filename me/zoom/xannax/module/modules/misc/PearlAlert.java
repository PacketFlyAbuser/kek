//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import java.util.Iterator;
import me.zoom.xannax.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import java.util.concurrent.ConcurrentHashMap;
import me.zoom.xannax.module.Module;

public class PearlAlert extends Module
{
    /* synthetic */ ConcurrentHashMap uuidMap;
    
    public String getTitle(final String s) {
        if (s.equalsIgnoreCase("west")) {
            return "east";
        }
        return s.equalsIgnoreCase("east") ? "west" : s;
    }
    
    public PearlAlert() {
        super("PearlAlert", "PearlAlert", Category.Misc);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @Override
    public void onUpdate() {
        for (final Entity entity : PearlAlert.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderPearl) {
                EntityPlayer entityPlayer = null;
                for (final EntityPlayer entityPlayer2 : PearlAlert.mc.world.playerEntities) {
                    if (entityPlayer == null || entity.getDistance((Entity)entityPlayer2) < entity.getDistance((Entity)entityPlayer)) {
                        entityPlayer = entityPlayer2;
                    }
                }
                if (entityPlayer == null || entityPlayer.getDistance(entity) >= 2.0f || this.uuidMap.containsKey(entity.getUniqueID()) || entityPlayer.getName().equalsIgnoreCase(PearlAlert.mc.player.getName())) {
                    continue;
                }
                this.uuidMap.put(entity.getUniqueID(), 200);
                Command.sendClientMessage(String.valueOf(new StringBuilder().append(ChatFormatting.RED).append(entityPlayer.getName()).append(" threw a pearl towards ").append(this.getTitle(entity.getHorizontalFacing().getName())).append("!")));
            }
        }
        this.uuidMap.forEach((o, n) -> {
            if (n <= 0) {
                this.uuidMap.remove(o);
            }
            else {
                this.uuidMap.put(o, n - 1);
            }
        });
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    public void setup() {
        this.uuidMap = new ConcurrentHashMap();
    }
}
