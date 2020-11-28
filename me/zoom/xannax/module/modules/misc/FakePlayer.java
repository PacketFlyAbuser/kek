//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import me.zoom.xannax.module.Module;

public class FakePlayer extends Module
{
    public void onDisable() {
        FakePlayer.mc.world.removeEntityFromWorld(-100);
    }
    
    public FakePlayer() {
        super("FakePlayer", "FakePlayer", Category.Misc);
    }
    
    public void onEnable() {
        if (FakePlayer.mc.world == null) {
            return;
        }
        final EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("0f75a81d-70e5-43c5-b892-f33c524284f2"), "popbob"));
        entityOtherPlayerMP.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        entityOtherPlayerMP.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        FakePlayer.mc.world.addEntityToWorld(-100, (Entity)entityOtherPlayerMP);
    }
}
