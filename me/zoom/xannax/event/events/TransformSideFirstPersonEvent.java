// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.util.EnumHandSide;
import me.zoom.xannax.event.Event;

public class TransformSideFirstPersonEvent extends Event
{
    private final /* synthetic */ EnumHandSide handSide;
    
    public EnumHandSide getHandSide() {
        return this.handSide;
    }
    
    public TransformSideFirstPersonEvent(final EnumHandSide handSide) {
        this.handSide = handSide;
    }
}
