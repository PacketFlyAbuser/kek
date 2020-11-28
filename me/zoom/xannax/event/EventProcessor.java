//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event;

import net.minecraftforge.event.entity.living.LivingDeathEvent;
import me.zoom.xannax.macro.Macro;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.function.Predicate;
import me.zoom.xannax.event.events.PlayerJoinEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import net.minecraftforge.client.event.ClientChatEvent;
import org.lwjgl.input.Mouse;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import me.zoom.xannax.event.events.PlayerLeaveEvent;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.common.MinecraftForge;
import me.zoom.xannax.module.ModuleManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.zoom.xannax.Xannax;
import net.minecraftforge.event.world.WorldEvent;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import me.zoom.xannax.command.CommandManager;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;
import java.util.Map;

public class EventProcessor
{
    public static /* synthetic */ EventProcessor INSTANCE;
    private final /* synthetic */ Map<String, String> uuidNameCache;
    @EventHandler
    private final /* synthetic */ Listener<PacketEvent.Receive> receiveListener;
    /* synthetic */ CommandManager commandManager;
    /* synthetic */ Minecraft mc;
    /* synthetic */ int speed;
    /* synthetic */ int rgb;
    /* synthetic */ float hue;
    /* synthetic */ Color c;
    
    @SubscribeEvent
    public void onWorldUnload(final WorldEvent.Unload unload) {
        Xannax.EVENT_BUS.post(unload);
    }
    
    public int getRgb() {
        return this.rgb;
    }
    
    @SubscribeEvent
    public void onRenderBlockOverlay(final RenderBlockOverlayEvent renderBlockOverlayEvent) {
        Xannax.EVENT_BUS.post(renderBlockOverlayEvent);
    }
    
    @SubscribeEvent
    public void onWorldRender(final RenderWorldLastEvent renderWorldLastEvent) {
        if (renderWorldLastEvent.isCanceled()) {
            return;
        }
        ModuleManager.onWorldRender(renderWorldLastEvent);
    }
    
    public int getRainbowSpeed() {
        return this.speed;
    }
    
    public void init() {
        Xannax.EVENT_BUS.subscribe(this);
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void setRainbowSpeed(final int speed) {
        this.speed = speed;
    }
    
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent mouseInputEvent) {
        if (Mouse.getEventButtonState()) {
            Xannax.EVENT_BUS.post(mouseInputEvent);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(final ClientChatEvent clientChatEvent) {
        if (clientChatEvent.getMessage().startsWith(Command.getPrefix())) {
            clientChatEvent.setCanceled(true);
            try {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(clientChatEvent.getMessage());
                this.commandManager.callCommand(clientChatEvent.getMessage().substring(1));
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Command.sendClientMessage(ChatFormatting.DARK_RED + "Error: " + ex.getMessage());
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(final InputEvent.KeyInputEvent keyInputEvent) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0 || Keyboard.getEventKey() == 0) {
                return;
            }
            ModuleManager.onBind(Keyboard.getEventKey());
            Xannax.getInstance().macroManager.getMacros().forEach(macro -> {
                if (macro.getKey() == Keyboard.getEventKey()) {
                    macro.onMacro();
                }
            });
        }
    }
    
    @SubscribeEvent
    public void onLivingEntityUseItemFinish(final LivingEntityUseItemEvent.Finish finish) {
        Xannax.EVENT_BUS.post(finish);
    }
    
    @SubscribeEvent
    public void onPlayerPush(final PlayerSPPushOutOfBlocksEvent playerSPPushOutOfBlocksEvent) {
        Xannax.EVENT_BUS.post(playerSPPushOutOfBlocksEvent);
    }
    
    @SubscribeEvent
    public void onWorldLoad(final WorldEvent.Load load) {
        Xannax.EVENT_BUS.post(load);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent attackEntityEvent) {
        Xannax.EVENT_BUS.post(attackEntityEvent);
    }
    
    @SubscribeEvent
    public void onChatReceived(final ClientChatReceivedEvent clientChatReceivedEvent) {
        Xannax.EVENT_BUS.post(clientChatReceivedEvent);
    }
    
    public EventProcessor() {
        this.mc = Minecraft.getMinecraft();
        this.commandManager = new CommandManager();
        this.hue = 0.0f;
        this.speed = 2;
        SPacketPlayerListItem sPacketPlayerListItem;
        final Iterator<SPacketPlayerListItem.AddPlayerData> iterator;
        SPacketPlayerListItem.AddPlayerData addPlayerData;
        final String s;
        final Iterator<SPacketPlayerListItem.AddPlayerData> iterator2;
        SPacketPlayerListItem.AddPlayerData addPlayerData2;
        final String s2;
        this.receiveListener = new Listener<PacketEvent.Receive>(receive -> {
            if (receive.getPacket() instanceof SPacketPlayerListItem) {
                sPacketPlayerListItem = (SPacketPlayerListItem)receive.getPacket();
                if (sPacketPlayerListItem.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                    sPacketPlayerListItem.getEntries().iterator();
                    while (iterator.hasNext()) {
                        addPlayerData = iterator.next();
                        if (addPlayerData.getProfile().getId() != this.mc.session.getProfile().getId()) {
                            new Thread(() -> {
                                this.resolveName(addPlayerData.getProfile().getId().toString());
                                if (s != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                                    Xannax.EVENT_BUS.post(new PlayerJoinEvent(s));
                                }
                                return;
                            }).start();
                        }
                    }
                }
                if (sPacketPlayerListItem.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                    sPacketPlayerListItem.getEntries().iterator();
                    while (iterator2.hasNext()) {
                        addPlayerData2 = iterator2.next();
                        if (addPlayerData2.getProfile().getId() != this.mc.session.getProfile().getId()) {
                            new Thread(() -> {
                                this.resolveName(addPlayerData2.getProfile().getId().toString());
                                if (s2 != null && this.mc.player != null && this.mc.player.ticksExisted >= 1000) {
                                    Xannax.EVENT_BUS.post(new PlayerLeaveEvent(s2));
                                }
                            }).start();
                        }
                    }
                }
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.uuidNameCache = (Map<String, String>)Maps.newConcurrentMap();
        EventProcessor.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onInputUpdate(final InputUpdateEvent inputUpdateEvent) {
        Xannax.EVENT_BUS.post(inputUpdateEvent);
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent clientTickEvent) {
        final float[] array = { System.currentTimeMillis() % 11520L / 11520.0f };
        final int hsBtoRGB = Color.HSBtoRGB(array[0], 1.0f, 1.0f);
        final int r = hsBtoRGB >> 16 & 0xFF;
        final int g = hsBtoRGB >> 8 & 0xFF;
        final int b = hsBtoRGB & 0xFF;
        final float[] array2 = array;
        final int n = 0;
        array2[n] += 0.02f;
        this.c = new Color(r, g, b);
        if (this.mc.player != null) {
            ModuleManager.onUpdate();
        }
    }
    
    @SubscribeEvent
    public void onRenderScreen(final RenderGameOverlayEvent.Text text) {
        Xannax.EVENT_BUS.post(text);
    }
    
    public String resolveName(String replace) {
        replace = replace.replace("-", "");
        if (this.uuidNameCache.containsKey(replace)) {
            return this.uuidNameCache.get(replace);
        }
        final String string = "https://api.mojang.com/user/profiles/" + replace + "/names";
        try {
            final String string2 = IOUtils.toString(new URL(string));
            if (string2 != null && string2.length() > 0) {
                final JSONArray jsonArray = (JSONArray)JSONValue.parseWithException(string2);
                if (jsonArray != null) {
                    final JSONObject jsonObject = jsonArray.get(jsonArray.size() - 1);
                    if (jsonObject != null) {
                        return jsonObject.get("name").toString();
                    }
                }
            }
        }
        catch (IOException | ParseException ex) {
            final Throwable t;
            t.printStackTrace();
        }
        return null;
    }
    
    @SubscribeEvent
    public void onLivingDamage(final LivingDamageEvent livingDamageEvent) {
        Xannax.EVENT_BUS.post(livingDamageEvent);
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post post) {
        Xannax.EVENT_BUS.post(post);
        if (post.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            ModuleManager.onRender();
        }
    }
    
    @SubscribeEvent
    public void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        Xannax.EVENT_BUS.post(playerRespawnEvent);
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent drawBlockHighlightEvent) {
        Xannax.EVENT_BUS.post(drawBlockHighlightEvent);
    }
    
    public Color getC() {
        return this.c;
    }
    
    @SubscribeEvent
    public void onLivingDeath(final LivingDeathEvent livingDeathEvent) {
        Xannax.EVENT_BUS.post(livingDeathEvent);
    }
}
