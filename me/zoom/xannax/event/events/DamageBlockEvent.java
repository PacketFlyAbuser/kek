// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import me.zoom.xannax.event.Event;

public class DamageBlockEvent extends Event
{
    private /* synthetic */ EnumFacing face;
    private /* synthetic */ BlockPos pos;
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public DamageBlockEvent(final BlockPos pos, final EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }
    
    public void setFace(final EnumFacing face) {
        this.face = face;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
