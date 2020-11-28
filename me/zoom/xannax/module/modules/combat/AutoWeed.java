//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.combat;

import net.minecraft.item.Item;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.init.Items;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class AutoWeed extends Module
{
    /* synthetic */ Setting.Boolean chorus;
    private /* synthetic */ boolean isEating;
    
    private int findGapple() {
        int n = 0;
        for (int i = 0; i < 9; ++i) {
            if (AutoWeed.mc.player.inventory.getStackInSlot(i).getItem() == Items.GOLDEN_APPLE) {
                n = i;
                break;
            }
        }
        return n;
    }
    
    @Override
    public void onUpdate() {
        final Item getItem = AutoWeed.mc.player.getHeldItemMainhand().getItem();
        final Item getItem2 = AutoWeed.mc.player.getHeldItemOffhand().getItem();
        final boolean b = getItem instanceof ItemAppleGold;
        final boolean b2 = getItem2 instanceof ItemAppleGold;
        this.isEating = true;
        KeyBinding.setKeyBindState(AutoWeed.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
        AutoWeed.mc.rightClickMouse();
    }
    
    public void onEnable() {
        AutoWeed.mc.player.inventory.currentItem = this.findGapple();
    }
    
    public AutoWeed() {
        super("AutoWeed", "AutoWeed", Category.Combat);
        this.isEating = false;
    }
    
    @Override
    public void setup() {
        this.chorus = this.registerBoolean("Disable On Chorus", "Disable On Chorus", true);
    }
}
