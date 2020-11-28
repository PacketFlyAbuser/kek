// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event.events;

import me.zoom.xannax.event.Event;

public class PlayerJoinEvent extends Event
{
    private final /* synthetic */ String name;
    
    public String getName() {
        return this.name;
    }
    
    public PlayerJoinEvent(final String name) {
        this.name = name;
    }
}
