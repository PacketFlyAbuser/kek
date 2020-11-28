// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mcp;

import com.google.common.collect.ImmutableList;
import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;
import org.spongepowered.tools.obfuscation.service.ObfuscationTypeDescriptor;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.spongepowered.tools.obfuscation.service.IObfuscationService;

public class ObfuscationServiceMCP implements IObfuscationService
{
    static {
        REOBF_EXTRA_NOTCH_FILES = "reobfNotchSrgFiles";
        REOBF_EXTRA_SRG_FILES = "reobfSrgFiles";
        REOBF_NOTCH_FILE = "reobfNotchSrgFile";
        OUT_REFMAP_FILE = "outRefMapFile";
        OUT_SRG_SRG_FILE = "outSrgFile";
        OUT_NOTCH_SRG_FILE = "outNotchSrgFile";
        NOTCH = "notch";
        SEARGE = "searge";
        REOBF_SRG_FILE = "reobfSrgFile";
    }
    
    @Override
    public Set<String> getSupportedOptions() {
        return (Set<String>)ImmutableSet.of((Object)"reobfSrgFile", (Object)"reobfSrgFiles", (Object)"reobfNotchSrgFile", (Object)"reobfNotchSrgFiles", (Object)"outSrgFile", (Object)"outNotchSrgFile", (Object[])new String[] { "outRefMapFile" });
    }
    
    @Override
    public Collection<ObfuscationTypeDescriptor> getObfuscationTypes() {
        return (Collection<ObfuscationTypeDescriptor>)ImmutableList.of((Object)new ObfuscationTypeDescriptor("searge", "reobfSrgFile", "reobfSrgFiles", "outSrgFile", ObfuscationEnvironmentMCP.class), (Object)new ObfuscationTypeDescriptor("notch", "reobfNotchSrgFile", "reobfNotchSrgFiles", "outNotchSrgFile", ObfuscationEnvironmentMCP.class));
    }
}
