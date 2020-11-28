// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import me.zoom.xannax.event.EventStage;

@Cancelable
public class PushEvent extends EventStage
{
    public /* synthetic */ boolean airbone;
    public /* synthetic */ double z;
    public /* synthetic */ double y;
    public /* synthetic */ double x;
    public /* synthetic */ Entity entity;
    
    public PushEvent(final int n, final Entity entity) {
        super(n);
        this.entity = entity;
    }
    
    public PushEvent(final Entity entity, final double x, final double y, final double z, final boolean airbone) {
        super(0);
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }
    
    public PushEvent(final int n) {
        super(n);
    }
}
