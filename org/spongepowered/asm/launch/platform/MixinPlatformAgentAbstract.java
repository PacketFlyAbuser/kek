// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.launch.platform;

import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.net.URI;
import org.apache.logging.log4j.Logger;

public abstract class MixinPlatformAgentAbstract implements IMixinPlatformAgent
{
    protected final /* synthetic */ URI uri;
    protected final /* synthetic */ File container;
    protected final /* synthetic */ MixinPlatformManager manager;
    protected final /* synthetic */ MainAttributes attributes;
    
    public MixinPlatformAgentAbstract(final MixinPlatformManager manager, final URI uri) {
        this.manager = manager;
        this.uri = uri;
        this.container = ((this.uri != null) ? new File(this.uri) : null);
        this.attributes = MainAttributes.of(uri);
    }
    
    static {
        logger = LogManager.getLogger("mixin");
    }
    
    @Override
    public String getPhaseProvider() {
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("PlatformAgent[%s:%s]", this.getClass().getSimpleName(), this.uri);
    }
}
