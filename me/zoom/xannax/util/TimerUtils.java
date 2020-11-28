// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

public class TimerUtils
{
    private /* synthetic */ long time;
    
    public long getTime(final long n) {
        return n / 1000000L;
    }
    
    public boolean passed(final long n) {
        return this.getTime(System.nanoTime() - this.time) >= n;
    }
    
    public long getMs(final long n) {
        return n / 1000000L;
    }
    
    public TimerUtils() {
        this.time = -1L;
    }
    
    public boolean passedMs(final long n) {
        return this.getMs(System.nanoTime() - this.time) >= n;
    }
    
    public void reset() {
        this.time = System.nanoTime();
    }
}
