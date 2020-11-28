// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.bridge;

import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.commons.Remapper;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IRemapper;
import org.spongepowered.asm.util.ObfuscationUtil;

public abstract class RemapperAdapter implements ObfuscationUtil.IClassRemapper, IRemapper
{
    protected final /* synthetic */ Logger logger;
    protected final /* synthetic */ Remapper remapper;
    
    @Override
    public String mapFieldName(final String s, final String anObject, final String s2) {
        this.logger.debug("{} is remapping field {}{} for {}", new Object[] { this, anObject, s2, s });
        final String mapFieldName = this.remapper.mapFieldName(s, anObject, s2);
        if (!mapFieldName.equals(anObject)) {
            return mapFieldName;
        }
        final String unmap = this.unmap(s);
        final String unmapDesc = this.unmapDesc(s2);
        this.logger.debug("{} is remapping obfuscated field {}{} for {}", new Object[] { this, anObject, unmapDesc, unmap });
        return this.remapper.mapFieldName(unmap, anObject, unmapDesc);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String mapDesc(final String s) {
        return this.remapper.mapDesc(s);
    }
    
    @Override
    public String mapMethodName(final String s, final String anObject, final String s2) {
        this.logger.debug("{} is remapping method {}{} for {}", new Object[] { this, anObject, s2, s });
        final String mapMethodName = this.remapper.mapMethodName(s, anObject, s2);
        if (!mapMethodName.equals(anObject)) {
            return mapMethodName;
        }
        final String unmap = this.unmap(s);
        final String unmapDesc = this.unmapDesc(s2);
        this.logger.debug("{} is remapping obfuscated method {}{} for {}", new Object[] { this, anObject, unmapDesc, unmap });
        return this.remapper.mapMethodName(unmap, anObject, unmapDesc);
    }
    
    public RemapperAdapter(final Remapper remapper) {
        this.logger = LogManager.getLogger("mixin");
        this.remapper = remapper;
    }
    
    @Override
    public String unmap(final String s) {
        return s;
    }
    
    @Override
    public String map(final String s) {
        this.logger.debug("{} is remapping class {}", new Object[] { this, s });
        return this.remapper.map(s);
    }
    
    @Override
    public String unmapDesc(final String s) {
        return ObfuscationUtil.unmapDescriptor(s, this);
    }
}
