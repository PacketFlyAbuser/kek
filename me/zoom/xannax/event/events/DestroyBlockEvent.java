// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.util.math.BlockPos;
import me.zoom.xannax.event.Event;

public class DestroyBlockEvent extends Event
{
    /* synthetic */ BlockPos pos;
    
    public BlockPos getBlockPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public DestroyBlockEvent(final BlockPos pos) {
        this.pos = pos;
    }
}
