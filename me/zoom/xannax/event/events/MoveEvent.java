// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.entity.MoverType;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.zoom.xannax.event.EventStage;

@Cancelable
public class MoveEvent extends EventStage
{
    private /* synthetic */ double z;
    private /* synthetic */ double y;
    private /* synthetic */ MoverType type;
    private /* synthetic */ double x;
    
    public double getX() {
        return this.x;
    }
    
    public MoverType getType() {
        return this.type;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public MoveEvent(final int n, final MoverType type, final double x, final double y, final double z) {
        super(n);
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setType(final MoverType type) {
        this.type = type;
    }
}
