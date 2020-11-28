//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.movement;

import me.zoom.xannax.Xannax;
import net.minecraft.util.MovementInput;
import java.util.function.Predicate;
import me.zoom.xannax.setting.Setting;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.InputUpdateEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.module.Module;

public class NoSlow extends Module
{
    @EventHandler
    private final /* synthetic */ Listener<InputUpdateEvent> eventListener;
    public /* synthetic */ Setting.Boolean noSlow;
    
    @Override
    public void setup() {
        this.noSlow = this.registerBoolean("SoulSand", "SoulSand", false);
    }
    
    public NoSlow() {
        super("NoSlow", "NoSlow", Category.Movement);
        final MovementInput movementInput;
        final MovementInput movementInput2;
        this.eventListener = new Listener<InputUpdateEvent>(inputUpdateEvent -> {
            if (NoSlow.mc.player.isHandActive() && !NoSlow.mc.player.isRiding()) {
                inputUpdateEvent.getMovementInput();
                movementInput.moveStrafe *= 5.0f;
                inputUpdateEvent.getMovementInput();
                movementInput2.moveForward *= 5.0f;
            }
        }, (Predicate<InputUpdateEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
