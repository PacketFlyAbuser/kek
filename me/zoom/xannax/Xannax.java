// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax;

import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import me.zoom.xannax.command.CommandManager;
import me.zoom.xannax.util.config.Stopper;
import me.zoom.xannax.util.TpsUtils;
import java.awt.Font;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import me.zero.alpine.EventManager;
import org.apache.logging.log4j.LogManager;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import me.zoom.xannax.util.font.CFontRenderer;
import me.zoom.xannax.util.friend.Friends;
import me.zoom.xannax.util.config.LoadConfiguration;
import me.zoom.xannax.util.config.SaveModules;
import me.zoom.xannax.macro.MacroManager;
import me.zoom.xannax.clickgui.ClickGUI;
import me.zoom.xannax.event.EventProcessor;
import me.zero.alpine.EventBus;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.setting.SettingsManager;
import me.zoom.xannax.util.config.LoadModules;
import me.zoom.xannax.util.config.SaveConfiguration;
import me.zoom.xannax.util.enemy.Enemies;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "xannax", name = "XannaX", version = "0.6")
public class Xannax
{
    public static /* synthetic */ Enemies enemies;
    public /* synthetic */ SaveConfiguration saveConfiguration;
    public /* synthetic */ LoadModules loadModules;
    public /* synthetic */ SettingsManager settingsManager;
    public /* synthetic */ ModuleManager moduleManager;
    /* synthetic */ EventProcessor eventProcessor;
    public /* synthetic */ ClickGUI clickGUI;
    public /* synthetic */ MacroManager macroManager;
    @Mod.Instance
    private static /* synthetic */ Xannax INSTANCE;
    public /* synthetic */ SaveModules saveModules;
    public /* synthetic */ LoadConfiguration loadConfiguration;
    public /* synthetic */ Friends friends;
    public static /* synthetic */ CFontRenderer fontRenderer;
    public static final /* synthetic */ Logger log;
    
    public static Xannax getInstance() {
        return Xannax.INSTANCE;
    }
    
    @Mod.EventHandler
    public void preinit(final FMLPreInitializationEvent fmlPreInitializationEvent) {
    }
    
    static {
        VERSION = "0.6";
        MOD_ID = "xannax";
        MOD_NAME = "XannaX";
        log = LogManager.getLogger("XannaX");
        EVENT_BUS = new EventManager();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent fmlInitializationEvent) {
        this.eventProcessor = new EventProcessor();
        this.eventProcessor.init();
        Xannax.fontRenderer = new CFontRenderer(new Font("Ariel", 0, 18), true, false);
        final TpsUtils tpsUtils = new TpsUtils();
        this.settingsManager = new SettingsManager();
        Xannax.log.info("Settings initialized!");
        this.friends = new Friends();
        Xannax.enemies = new Enemies();
        Xannax.log.info("Friends and enemies initialized!");
        this.moduleManager = new ModuleManager();
        Xannax.log.info("Modules initialized!");
        this.clickGUI = new ClickGUI();
        Xannax.log.info("ClickGUI initialized!");
        this.macroManager = new MacroManager();
        Xannax.log.info("Macros initialized!");
        this.saveConfiguration = new SaveConfiguration();
        Runtime.getRuntime().addShutdownHook(new Stopper());
        Xannax.log.info("Config Saved!");
        this.loadConfiguration = new LoadConfiguration();
        Xannax.log.info("Config Loaded!");
        this.saveModules = new SaveModules();
        Runtime.getRuntime().addShutdownHook(new Stopper());
        Xannax.log.info("Modules Saved!");
        this.loadModules = new LoadModules();
        Xannax.log.info("Modules Loaded!");
        CommandManager.initCommands();
        Xannax.log.info("Commands initialized!");
        Xannax.log.info("Initialization complete!\n");
    }
    
    @Mod.EventHandler
    public void postinit(final FMLPostInitializationEvent fmlPostInitializationEvent) {
        Display.setTitle("XannaX 0.6");
        Xannax.log.info("PostInitialization complete!\n");
    }
    
    public Xannax() {
        Xannax.INSTANCE = this;
    }
}
