//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import net.minecraft.util.math.MathHelper;
import me.zoom.xannax.Xannax;
import java.util.Arrays;
import java.util.function.Predicate;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import me.zero.alpine.listener.EventHandler;
import me.zoom.xannax.event.events.PacketEvent;
import me.zero.alpine.listener.Listener;

public class TpsUtils
{
    private /* synthetic */ long timeLastTimeUpdate;
    private /* synthetic */ int nextIndex;
    @EventHandler
    /* synthetic */ Listener<PacketEvent.Receive> listener;
    private static final /* synthetic */ float[] tickRates;
    
    static {
        tickRates = new float[20];
    }
    
    public TpsUtils() {
        this.nextIndex = 0;
        this.listener = new Listener<PacketEvent.Receive>(receive -> {
            if (receive.getPacket() instanceof SPacketTimeUpdate) {
                this.onTimeUpdate();
            }
            return;
        }, (Predicate<PacketEvent.Receive>[])new Predicate[0]);
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(TpsUtils.tickRates, 0.0f);
        Xannax.EVENT_BUS.subscribe(this);
    }
    
    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            TpsUtils.tickRates[this.nextIndex % TpsUtils.tickRates.length] = MathHelper.clamp(20.0f / ((System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f), 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
    
    public static float getTickRate() {
        float n = 0.0f;
        float n2 = 0.0f;
        for (final float n3 : TpsUtils.tickRates) {
            if (n3 > 0.0f) {
                n2 += n3;
                ++n;
            }
        }
        return MathHelper.clamp(n2 / n, 0.0f, 20.0f);
    }
}
