// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.service;

import org.spongepowered.asm.util.ReEntranceLock;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.io.InputStream;
import java.util.Collection;

public interface IMixinService
{
    Collection<ITransformer> getTransformers();
    
    void prepare();
    
    void beginPhase();
    
    IClassBytecodeProvider getBytecodeProvider();
    
    InputStream getResourceAsStream(final String p0);
    
    void init();
    
    boolean isClassLoaded(final String p0);
    
    MixinEnvironment.Phase getInitialPhase();
    
    ReEntranceLock getReEntranceLock();
    
    void checkEnv(final Object p0);
    
    Collection<String> getPlatformAgents();
    
    void registerInvalidClass(final String p0);
    
    String getSideName();
    
    IClassProvider getClassProvider();
    
    boolean isValid();
    
    String getName();
}
