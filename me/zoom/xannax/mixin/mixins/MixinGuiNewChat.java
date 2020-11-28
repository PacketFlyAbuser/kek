//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gui.Gui;
import me.zoom.xannax.module.modules.misc.Chat;
import me.zoom.xannax.module.ModuleManager;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.client.gui.ChatLine;
import java.util.List;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.gui.GuiNewChat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ GuiNewChat.class })
public abstract class MixinGuiNewChat
{
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectBackgroundClean(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (!ModuleManager.isModuleEnabled("Chat") || !((Chat)ModuleManager.getModuleByName("Chat")).clearBkg.getValue()) {
            Gui.drawRect(n, n2, n3, n4, n5);
        }
    }
    
    @Shadow
    public abstract int getLineCount();
}
