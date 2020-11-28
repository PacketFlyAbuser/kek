// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.event;

public class EventCancellable extends EventStageable
{
    private /* synthetic */ boolean canceled;
    
    public EventCancellable() {
    }
    
    public EventCancellable(final EventStage eventStage) {
        super(eventStage);
    }
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public void setCanceled(final boolean canceled) {
        this.canceled = canceled;
    }
}
