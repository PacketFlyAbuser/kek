// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event;

public class EventStageable
{
    private /* synthetic */ EventStage stage;
    
    public EventStage getStage() {
        return this.stage;
    }
    
    public EventStageable() {
    }
    
    public EventStageable(final EventStage stage) {
        this.stage = stage;
    }
    
    public enum EventStage
    {
        POST, 
        PRE;
    }
}
