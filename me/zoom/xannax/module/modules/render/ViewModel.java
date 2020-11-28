//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import java.util.function.Predicate;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import me.zoom.xannax.Xannax;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.TransformSideFirstPersonEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class ViewModel extends Module
{
    /* synthetic */ Setting.Double xLeft;
    /* synthetic */ Setting.Double zLeft;
    @EventHandler
    private final /* synthetic */ Listener<TransformSideFirstPersonEvent> eventListener;
    /* synthetic */ Setting.Double yRight;
    /* synthetic */ Setting.Double zRight;
    public /* synthetic */ Setting.Boolean cancelEating;
    /* synthetic */ Setting.Double xRight;
    /* synthetic */ Setting.Double yLeft;
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    public ViewModel() {
        super("ViewModel", "ViewModel", Category.Render);
        this.eventListener = new Listener<TransformSideFirstPersonEvent>(transformSideFirstPersonEvent -> {
            if (transformSideFirstPersonEvent.getHandSide() == EnumHandSide.RIGHT) {
                GlStateManager.translate(this.xRight.getValue(), this.yRight.getValue(), this.zRight.getValue());
            }
            else if (transformSideFirstPersonEvent.getHandSide() == EnumHandSide.LEFT) {
                GlStateManager.translate(this.xLeft.getValue(), this.yLeft.getValue(), this.zLeft.getValue());
            }
        }, (Predicate<TransformSideFirstPersonEvent>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    @Override
    public void setup() {
        this.cancelEating = this.registerBoolean("No Eat", "NoEat", false);
        this.xLeft = this.registerDouble("Left X", "LeftX", 0.0, -2.0, 2.0);
        this.yLeft = this.registerDouble("Left Y", "LeftY", 0.2, -2.0, 2.0);
        this.zLeft = this.registerDouble("Left Z", "LeftZ", -1.2, -2.0, 2.0);
        this.xRight = this.registerDouble("Right X", "RightX", 0.0, -2.0, 2.0);
        this.yRight = this.registerDouble("Right Y", "RightY", 0.2, -2.0, 2.0);
        this.zRight = this.registerDouble("Right Z", "RightZ", -1.2, -2.0, 2.0);
    }
}
