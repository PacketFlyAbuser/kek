// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import net.minecraft.entity.Entity;
import me.zoom.xannax.event.Event;

public class TotemPopEvent extends Event
{
    private final /* synthetic */ Entity entity;
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public TotemPopEvent(final Entity entity) {
        this.entity = entity;
    }
}
