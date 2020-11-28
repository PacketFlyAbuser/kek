// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventStage extends Event
{
    private /* synthetic */ int stage;
    
    public void setStage(final int stage) {
        this.stage = stage;
    }
    
    public EventStage() {
    }
    
    public int getStage() {
        return this.stage;
    }
    
    public EventStage(final int stage) {
        this.stage = stage;
    }
}
