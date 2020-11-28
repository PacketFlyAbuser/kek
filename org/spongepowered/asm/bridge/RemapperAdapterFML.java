// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.bridge;

import org.spongepowered.asm.mixin.extensibility.IRemapper;
import org.objectweb.asm.commons.Remapper;
import java.lang.reflect.Method;

public final class RemapperAdapterFML extends RemapperAdapter
{
    private final /* synthetic */ Method mdUnmap;
    
    static {
        UNMAP_METHOD = "unmap";
        DEOBFUSCATING_REMAPPER_CLASS_FORGE = "net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
        DEOBFUSCATING_REMAPPER_CLASS = "fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
        DEOBFUSCATING_REMAPPER_CLASS_LEGACY = "cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper";
        INSTANCE_FIELD = "INSTANCE";
    }
    
    @Override
    public String unmap(final String s) {
        try {
            return this.mdUnmap.invoke(this.remapper, s).toString();
        }
        catch (Exception ex) {
            return s;
        }
    }
    
    private static Class<?> getFMLDeobfuscatingRemapper() throws ClassNotFoundException {
        try {
            return Class.forName("net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
        }
        catch (ClassNotFoundException ex) {
            return Class.forName("cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper");
        }
    }
    
    private RemapperAdapterFML(final Remapper remapper, final Method mdUnmap) {
        super(remapper);
        this.logger.info("Initialised Mixin FML Remapper Adapter with {}", new Object[] { remapper });
        this.mdUnmap = mdUnmap;
    }
    
    public static IRemapper create() {
        try {
            final Class<?> fmlDeobfuscatingRemapper = getFMLDeobfuscatingRemapper();
            return new RemapperAdapterFML((Remapper)fmlDeobfuscatingRemapper.getDeclaredField("INSTANCE").get(null), fmlDeobfuscatingRemapper.getDeclaredMethod("unmap", String.class));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
