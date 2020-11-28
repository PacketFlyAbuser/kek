// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.callback;

public enum LocalCapture
{
    CAPTURE_FAILSOFT;
    
    private final /* synthetic */ boolean captureLocals;
    
    CAPTURE_FAILEXCEPTION, 
    NO_CAPTURE(false, false), 
    PRINT(false, true);
    
    private final /* synthetic */ boolean printLocals;
    
    CAPTURE_FAILHARD;
    
    boolean isCaptureLocals() {
        return this.captureLocals;
    }
    
    private LocalCapture(final boolean captureLocals, final boolean printLocals) {
        this.captureLocals = captureLocals;
        this.printLocals = printLocals;
    }
    
    private LocalCapture() {
        this(true, false);
    }
    
    boolean isPrintLocals() {
        return this.printLocals;
    }
}
