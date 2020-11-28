// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.callback;

public interface Cancellable
{
    boolean isCancellable();
    
    void cancel() throws CancellationException;
    
    boolean isCancelled();
}
