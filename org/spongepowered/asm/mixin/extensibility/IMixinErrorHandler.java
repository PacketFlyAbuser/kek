// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.extensibility;

import org.apache.logging.log4j.Level;

public interface IMixinErrorHandler
{
    ErrorAction onApplyError(final String p0, final Throwable p1, final IMixinInfo p2, final ErrorAction p3);
    
    ErrorAction onPrepareError(final IMixinConfig p0, final Throwable p1, final IMixinInfo p2, final ErrorAction p3);
    
    public enum ErrorAction
    {
        ERROR(Level.FATAL), 
        NONE(Level.INFO);
        
        public final /* synthetic */ Level logLevel;
        
        WARN(Level.WARN);
        
        private ErrorAction(final Level logLevel) {
            this.logLevel = logLevel;
        }
    }
}
