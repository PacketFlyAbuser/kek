//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax;

import club.minnced.discord.rpc.DiscordEventHandlers;
import net.minecraft.client.Minecraft;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;

public class XannaxRPC
{
    private static final /* synthetic */ DiscordRPC rpc;
    public static /* synthetic */ DiscordRichPresence presence;
    private static /* synthetic */ String details;
    private static final /* synthetic */ Minecraft mc;
    private static /* synthetic */ String state;
    
    public static void init() {
        final DiscordEventHandlers discordEventHandlers = new DiscordEventHandlers();
        discordEventHandlers.disconnected = ((i, str) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(i) + ", var2: " + str));
        XannaxRPC.rpc.Discord_Initialize("773651992160894978", discordEventHandlers, true, "");
        XannaxRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        XannaxRPC.presence.details = "XannaX0.6";
        XannaxRPC.presence.state = "vibin'";
        XannaxRPC.presence.largeImageKey = "large";
        XannaxRPC.presence.largeImageText = "XannaX 0.6";
        XannaxRPC.presence.smallImageKey = "small";
        XannaxRPC.presence.smallImageText = XannaxRPC.mc.getSession().getUsername();
        XannaxRPC.rpc.Discord_UpdatePresence(XannaxRPC.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    XannaxRPC.rpc.Discord_RunCallbacks();
                    XannaxRPC.details = "XannaX0.6";
                    XannaxRPC.state = "vibin'";
                    if (XannaxRPC.mc.isIntegratedServerRunning()) {
                        XannaxRPC.details = "Singleplayer";
                    }
                    else if (XannaxRPC.mc.getCurrentServerData() != null) {
                        if (!XannaxRPC.mc.getCurrentServerData().serverIP.equals("")) {
                            XannaxRPC.details = XannaxRPC.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        XannaxRPC.details = "Main Menu";
                    }
                    if (!XannaxRPC.details.equals(XannaxRPC.presence.details) || !XannaxRPC.state.equals(XannaxRPC.presence.state)) {
                        XannaxRPC.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    XannaxRPC.presence.details = XannaxRPC.details;
                    XannaxRPC.presence.state = XannaxRPC.state;
                    XannaxRPC.rpc.Discord_UpdatePresence(XannaxRPC.presence);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException ex2) {
                    ex2.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }
    
    static {
        ClientId = "773651992160894978";
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        XannaxRPC.presence = new DiscordRichPresence();
    }
    
    public static void shutdown() {
        XannaxRPC.rpc.Discord_Shutdown();
    }
}
