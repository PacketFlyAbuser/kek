//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.module.modules.misc;

import me.zoom.xannax.Xannax;
import java.util.function.Predicate;
import java.util.List;
import java.util.ArrayList;
import me.zoom.xannax.command.Command;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Date;
import java.text.SimpleDateFormat;
import me.zoom.xannax.module.modules.client.CommandColor;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.EventHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import me.zero.alpine.listener.Listener;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;

public class Chat extends Module
{
    @EventHandler
    private final /* synthetic */ Listener<ClientChatReceivedEvent> chatReceivedEventListener;
    /* synthetic */ Setting.Boolean greentext;
    /* synthetic */ Setting.Boolean space;
    /* synthetic */ Setting.Boolean chattimestamps;
    public /* synthetic */ Setting.Boolean clearBkg;
    /* synthetic */ Setting.Mode format;
    @EventHandler
    private final /* synthetic */ Listener<PacketEvent.Send> listener;
    
    public String toUnicode(final String s) {
        return s.toLowerCase().replace("a", "\u1d00").replace("b", "\u0299").replace("c", "\u1d04").replace("d", "\u1d05").replace("e", "\u1d07").replace("f", "\ua730").replace("g", "\u0262").replace("h", "\u029c").replace("i", "\u026a").replace("j", "\u1d0a").replace("k", "\u1d0b").replace("l", "\u029f").replace("m", "\u1d0d").replace("n", "\u0274").replace("o", "\u1d0f").replace("p", "\u1d18").replace("q", "\u01eb").replace("r", "\u0280").replace("s", "\ua731").replace("t", "\u1d1b").replace("u", "\u1d1c").replace("v", "\u1d20").replace("w", "\u1d21").replace("x", "\u02e3").replace("y", "\u028f").replace("z", "\u1d22");
    }
    
    @Override
    public void setup() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("H24:mm");
        list.add("H12:mm");
        list.add("H12:mm a");
        list.add("H24:mm:ss");
        list.add("H12:mm:ss");
        list.add("H12:mm:ss a");
        this.clearBkg = this.registerBoolean("Clear Chat", "ClearChat", false);
        this.greentext = this.registerBoolean("Green Text", "GreenText", false);
        this.chattimestamps = this.registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
        this.format = this.registerMode("Format", "Format", list, "H24:mm");
        this.space = this.registerBoolean("Space", "Space", false);
    }
    
    public Chat() {
        super("Chat", "Chat", Category.Misc);
        final TextComponentString textComponentString;
        final String str;
        this.chatReceivedEventListener = new Listener<ClientChatReceivedEvent>(clientChatReceivedEvent -> {
            if (this.chattimestamps.getValue()) {
                new SimpleDateFormat(this.format.getValue().replace("H24", "k").replace("H12", "h")).format(new Date());
                new TextComponentString(CommandColor.getTextColor() + "<" + str + ">" + ChatFormatting.RESET);
                clientChatReceivedEvent.setMessage(textComponentString.appendSibling(clientChatReceivedEvent.getMessage()));
            }
            return;
        }, (Predicate<ClientChatReceivedEvent>[])new Predicate[0]);
        final String message;
        this.listener = new Listener<PacketEvent.Send>(send -> {
            if (this.greentext.getValue() && send.getPacket() instanceof CPacketChatMessage) {
                if (!((CPacketChatMessage)send.getPacket()).getMessage().startsWith("/") && !((CPacketChatMessage)send.getPacket()).getMessage().startsWith(Command.getPrefix())) {
                    new StringBuilder().append(">").append(((CPacketChatMessage)send.getPacket()).getMessage()).toString();
                    if (message.length() <= 255) {
                        ((CPacketChatMessage)send.getPacket()).message = message;
                    }
                }
            }
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
    }
    
    public void onEnable() {
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    public void onDisable() {
        Xannax.EVENT_BUS.unsubscribe(this);
    }
}
