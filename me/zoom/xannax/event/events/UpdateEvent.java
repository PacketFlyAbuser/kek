// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import me.zoom.xannax.event.EventStageable;
import me.zoom.xannax.event.EventCancellable;

public class UpdateEvent extends EventCancellable
{
    private /* synthetic */ double x;
    private /* synthetic */ boolean onGround;
    private /* synthetic */ double z;
    private /* synthetic */ double y;
    private /* synthetic */ float yaw;
    private /* synthetic */ float pitch;
    
    public double getY() {
        return this.y;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public UpdateEvent(final EventStage eventStage, final float yaw, final float pitch, final double x, final double y, final double z, final boolean onGround) {
        super(eventStage);
        this.yaw = yaw;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
}
