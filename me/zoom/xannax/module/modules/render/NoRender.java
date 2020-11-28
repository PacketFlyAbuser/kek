//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.render;

import net.minecraft.init.MobEffects;
import net.minecraft.block.material.Material;
import java.util.function.Predicate;
import me.zoom.xannax.Xannax;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import me.zoom.xannax.event.events.BossbarEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class NoRender extends Module
{
    public /* synthetic */ Setting.Boolean hurtCam;
    public /* synthetic */ Setting.Boolean armor;
    @EventHandler
    public /* synthetic */ Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
    /* synthetic */ Setting.Boolean fire;
    /* synthetic */ Setting.Boolean nausea;
    /* synthetic */ Setting.Boolean noBossBar;
    public /* synthetic */ Setting.Boolean noOverlay;
    @EventHandler
    private final /* synthetic */ Listener<BossbarEvent> bossbarEventListener;
    /* synthetic */ Setting.Boolean blind;
    @EventHandler
    private final /* synthetic */ Listener<RenderGameOverlayEvent> renderGameOverlayEventListener;
    @EventHandler
    private final /* synthetic */ Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener;
    @EventHandler
    private final /* synthetic */ Listener<EntityViewRenderEvent.FogDensity> fogDensityListener;
    
    @Override
    public void setup() {
        this.armor = this.registerBoolean("Armor", "Armor", false);
        this.fire = this.registerBoolean("Fire", "Fire", false);
        this.blind = this.registerBoolean("Blind", "Blind", false);
        this.nausea = this.registerBoolean("Nausea", "Nausea", false);
        this.hurtCam = this.registerBoolean("HurtCam", "HurtCam", false);
        this.noOverlay = this.registerBoolean("No Overlay", "NoOverlay", false);
        this.noBossBar = this.registerBoolean("No Boss Bar", "NoBossBar", false);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
    
    public NoRender() {
        super("NoRender", "NoRender", Category.Render);
        this.blockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(renderBlockOverlayEvent2 -> {
            if (this.fire.getValue() && renderBlockOverlayEvent2.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
                renderBlockOverlayEvent2.setCanceled(true);
            }
            if (this.noOverlay.getValue() && renderBlockOverlayEvent2.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER) {
                renderBlockOverlayEvent2.setCanceled(true);
            }
            if (this.noOverlay.getValue() && renderBlockOverlayEvent2.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK) {
                renderBlockOverlayEvent2.setCanceled(true);
            }
            return;
        }, (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
        this.fogDensityListener = new Listener<EntityViewRenderEvent.FogDensity>(fogDensity -> {
            if (this.noOverlay.getValue() && (fogDensity.getState().getMaterial().equals(Material.WATER) || fogDensity.getState().getMaterial().equals(Material.LAVA))) {
                fogDensity.setDensity(0.0f);
                fogDensity.setCanceled(true);
            }
            return;
        }, (Predicate<EntityViewRenderEvent.FogDensity>[])new Predicate[0]);
        this.renderBlockOverlayEventListener = new Listener<RenderBlockOverlayEvent>(renderBlockOverlayEvent -> renderBlockOverlayEvent.setCanceled(true), (Predicate<RenderBlockOverlayEvent>[])new Predicate[0]);
        this.renderGameOverlayEventListener = new Listener<RenderGameOverlayEvent>(renderGameOverlayEvent -> {
            if (this.noOverlay.getValue()) {
                if (renderGameOverlayEvent.getType().equals((Object)RenderGameOverlayEvent.ElementType.HELMET)) {
                    renderGameOverlayEvent.setCanceled(true);
                }
                if (renderGameOverlayEvent.getType().equals((Object)RenderGameOverlayEvent.ElementType.PORTAL)) {
                    renderGameOverlayEvent.setCanceled(true);
                }
            }
            return;
        }, (Predicate<RenderGameOverlayEvent>[])new Predicate[0]);
        this.bossbarEventListener = new Listener<BossbarEvent>(bossbarEvent -> {
            if (this.noBossBar.getValue()) {
                bossbarEvent.cancel();
            }
        }, (Predicate<BossbarEvent>[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.blind.getValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() && NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
        }
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
}
