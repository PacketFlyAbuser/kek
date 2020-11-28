//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module;

import net.minecraft.client.renderer.Tessellator;
import me.zoom.xannax.util.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import me.zoom.xannax.module.modules.render.Tracers;
import me.zoom.xannax.module.modules.render.Trajectories;
import me.zoom.xannax.module.modules.render.HoleESP;
import me.zoom.xannax.module.modules.render.LowHands;
import me.zoom.xannax.module.modules.render.InvPreview;
import me.zoom.xannax.module.modules.render.VoidESP;
import me.zoom.xannax.module.modules.render.BlockHighlight;
import me.zoom.xannax.module.modules.render.ViewModel;
import me.zoom.xannax.module.modules.render.HandColor;
import me.zoom.xannax.module.modules.render.NoRender;
import me.zoom.xannax.module.modules.render.Nametags;
import me.zoom.xannax.module.modules.render.FovSlider;
import me.zoom.xannax.module.modules.render.FullBright;
import me.zoom.xannax.module.modules.render.ShulkerPreview;
import me.zoom.xannax.module.modules.render.SkyColor;
import me.zoom.xannax.module.modules.render.StorageESP;
import me.zoom.xannax.module.modules.render.Skeleton;
import me.zoom.xannax.module.modules.render.ItemESP;
import me.zoom.xannax.module.modules.render.ESP;
import me.zoom.xannax.module.modules.render.Chams;
import me.zoom.xannax.module.modules.render.CameraClip;
import me.zoom.xannax.module.modules.render.BreakESP;
import me.zoom.xannax.module.modules.client.Compass;
import me.zoom.xannax.module.modules.client.Rainbow;
import me.zoom.xannax.module.modules.client.HUD;
import me.zoom.xannax.module.modules.client.CommandColor;
import me.zoom.xannax.module.modules.client.CustomFont;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.module.modules.misc.XCarry;
import me.zoom.xannax.module.modules.misc.NoWeather;
import me.zoom.xannax.module.modules.misc.MiddleClick;
import me.zoom.xannax.module.modules.misc.TotemCounter;
import me.zoom.xannax.module.modules.misc.PearlAlert;
import me.zoom.xannax.module.modules.misc.LowArmor;
import me.zoom.xannax.module.modules.misc.FakePlayer;
import me.zoom.xannax.module.modules.misc.RPCModule;
import me.zoom.xannax.module.modules.misc.ChatSuffix;
import me.zoom.xannax.module.modules.misc.Chat;
import me.zoom.xannax.module.modules.misc.Announcer;
import me.zoom.xannax.module.modules.player.SpeedMine;
import me.zoom.xannax.module.modules.player.Reach;
import me.zoom.xannax.module.modules.player.MultiTask;
import me.zoom.xannax.module.modules.player.NoEntityTrace;
import me.zoom.xannax.module.modules.player.OffhandSwing;
import me.zoom.xannax.module.modules.player.AutoReplanish;
import me.zoom.xannax.module.modules.movement.Velocity;
import me.zoom.xannax.module.modules.movement.Static;
import me.zoom.xannax.module.modules.movement.GuiMove;
import me.zoom.xannax.module.modules.movement.NoSlow;
import me.zoom.xannax.module.modules.movement.IceSpeed;
import me.zoom.xannax.module.modules.movement.ReverseStep;
import me.zoom.xannax.module.modules.movement.Scaffold;
import me.zoom.xannax.module.modules.movement.Step;
import me.zoom.xannax.module.modules.movement.Sprint;
import me.zoom.xannax.module.modules.movement.Speed;
import me.zoom.xannax.module.modules.movement.FastSwim;
import me.zoom.xannax.module.modules.movement.Blink;
import me.zoom.xannax.module.modules.combat.OffhandUtils;
import me.zoom.xannax.module.modules.combat.Surround;
import me.zoom.xannax.module.modules.combat.SelfWeb;
import me.zoom.xannax.module.modules.combat.SelfTrap;
import me.zoom.xannax.module.modules.combat.EasyPearl;
import me.zoom.xannax.module.modules.combat.Criticals;
import me.zoom.xannax.module.modules.combat.EXPFast;
import me.zoom.xannax.module.modules.combat.AutoWeed;
import me.zoom.xannax.module.modules.combat.AutoTotem;
import me.zoom.xannax.module.modules.combat.AutoArmor;
import me.zoom.xannax.module.modules.combat.AutoMend;
import me.zoom.xannax.module.modules.combat.AutoSchoolShooter;
import me.zoom.xannax.module.modules.combat.AutoTrap;
import me.zoom.xannax.module.modules.combat.AutoCrystal;
import me.zoom.xannax.module.modules.combat.Aura;
import net.minecraft.client.Minecraft;
import me.zoom.xannax.event.events.RenderEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class ModuleManager
{
    public static /* synthetic */ ArrayList<Module> modules;
    
    public static ArrayList<Module> getModulesInCategory(final Module.Category other) {
        return getModules().stream().filter(module -> module.getCategory().equals(other)).collect((Collector<? super Object, ?, ArrayList<Module>>)Collectors.toList());
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double n, final double n2, final double n3) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * n, (entity.posY - entity.lastTickPosY) * n2, (entity.posZ - entity.lastTickPosZ) * n3);
    }
    
    public static boolean isModuleEnabled(final Module module) {
        return module.isEnabled();
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double n) {
        return getInterpolatedAmount(entity, n, n, n);
    }
    
    public ModuleManager() {
        ModuleManager.modules = new ArrayList<Module>();
        addMod(new Aura());
        addMod(new AutoCrystal());
        addMod(new AutoTrap());
        addMod(new AutoSchoolShooter());
        addMod(new AutoMend());
        addMod(new AutoArmor());
        addMod(new AutoTotem());
        addMod(new AutoWeed());
        addMod(new EXPFast());
        addMod(new Criticals());
        addMod(new EasyPearl());
        addMod(new SelfTrap());
        addMod(new SelfWeb());
        addMod(new Surround());
        addMod(new OffhandUtils());
        addMod(new Blink());
        addMod(new FastSwim());
        addMod(new Speed());
        addMod(new Sprint());
        addMod(new Step());
        addMod(new Scaffold());
        addMod(new ReverseStep());
        addMod(new IceSpeed());
        addMod(new NoSlow());
        addMod(new GuiMove());
        addMod(new Static());
        addMod(new Velocity());
        addMod(new AutoReplanish());
        addMod(new OffhandSwing());
        addMod(new NoEntityTrace());
        addMod(new MultiTask());
        addMod(new Reach());
        addMod(new SpeedMine());
        addMod(new Announcer());
        addMod(new Chat());
        addMod(new ChatSuffix());
        addMod(new RPCModule());
        addMod(new FakePlayer());
        addMod(new LowArmor());
        addMod(new PearlAlert());
        addMod(new TotemCounter());
        addMod(new MiddleClick());
        addMod(new NoWeather());
        addMod(new XCarry());
        addMod(new ClickGuiModule());
        addMod(new CustomFont());
        addMod(new CommandColor());
        addMod(new HUD());
        addMod(new Rainbow());
        addMod(new Compass());
        addMod(new BreakESP());
        addMod(new CameraClip());
        addMod(new Chams());
        addMod(new ESP());
        addMod(new ItemESP());
        addMod(new Skeleton());
        addMod(new StorageESP());
        addMod(new SkyColor());
        addMod(new ShulkerPreview());
        addMod(new FullBright());
        addMod(new FovSlider());
        addMod(new Nametags());
        addMod(new NoRender());
        addMod(new HandColor());
        addMod(new ViewModel());
        addMod(new BlockHighlight());
        addMod(new VoidESP());
        addMod(new InvPreview());
        addMod(new LowHands());
        addMod(new HoleESP());
        addMod(new Trajectories());
        addMod(new Tracers());
    }
    
    public static void onUpdate() {
        ModuleManager.modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }
    
    public static ArrayList<Module> getModules() {
        return ModuleManager.modules;
    }
    
    public static void onRender() {
        ModuleManager.modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
    }
    
    public static boolean isModuleEnabled(final String anotherString) {
        return getModules().stream().filter(module -> module.getName().equalsIgnoreCase(anotherString)).findFirst().orElse(null).isEnabled();
    }
    
    public static void addMod(final Module e) {
        ModuleManager.modules.add(e);
    }
    
    public static Module getModuleByName(final String anotherString) {
        return getModules().stream().filter(module -> module.getName().equalsIgnoreCase(anotherString)).findFirst().orElse(null);
    }
    
    public static void onWorldRender(final RenderWorldLastEvent renderWorldLastEvent) {
        Minecraft.getMinecraft().profiler.startSection("xannax");
        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        new RenderEvent(RenderUtil.INSTANCE, getInterpolatedPos((Entity)Minecraft.getMinecraft().player, renderWorldLastEvent.getPartialTicks()), renderWorldLastEvent.getPartialTicks()).resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();
        final RenderEvent renderEvent;
        ModuleManager.modules.stream().filter(module -> module.isEnabled()).forEach(module2 -> {
            Minecraft.getMinecraft().profiler.startSection(module2.getName());
            module2.onWorldRender(renderEvent);
            Minecraft.getMinecraft().profiler.endSection();
            return;
        });
        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        RenderUtil.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
        Minecraft.getMinecraft().profiler.endSection();
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float n) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, n));
    }
    
    public static void onBind(final int n) {
        if (n == 0 || n == 0) {
            return;
        }
        ModuleManager.modules.forEach(module -> {
            if (module.getBind() == n) {
                module.toggle();
            }
        });
    }
}
