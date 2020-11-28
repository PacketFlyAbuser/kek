// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

public class ReEntranceLock
{
    private /* synthetic */ boolean semaphore;
    private final /* synthetic */ int maxDepth;
    private /* synthetic */ int depth;
    
    public ReEntranceLock pop() {
        if (this.depth == 0) {
            throw new IllegalStateException("ReEntranceLock pop() with zero depth");
        }
        --this.depth;
        return this;
    }
    
    public boolean checkAndSet() {
        final boolean semaphore = this.semaphore | this.check();
        this.semaphore = semaphore;
        return semaphore;
    }
    
    public boolean isSet() {
        return this.semaphore;
    }
    
    public ReEntranceLock clear() {
        this.semaphore = false;
        return this;
    }
    
    public boolean check() {
        return this.depth > this.maxDepth;
    }
    
    public ReEntranceLock push() {
        ++this.depth;
        this.checkAndSet();
        return this;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public int getMaxDepth() {
        return this.maxDepth;
    }
    
    public ReEntranceLock set() {
        this.semaphore = true;
        return this;
    }
    
    public ReEntranceLock(final int maxDepth) {
        this.depth = 0;
        this.semaphore = false;
        this.maxDepth = maxDepth;
    }
}
