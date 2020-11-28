// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation;

import org.spongepowered.tools.obfuscation.service.ObfuscationServices;
import com.google.common.collect.ImmutableSet;
import java.util.Set;

public final class SupportedOptions
{
    public static Set<String> getAllOptions() {
        final ImmutableSet.Builder builder = ImmutableSet.builder();
        builder.add((Object[])new String[] { "tokens", "outRefMapFile", "disableTargetValidator", "disableTargetExport", "disableOverwriteChecker", "overwriteErrorLevel", "defaultObfuscationEnv", "dependencyTargetsFile" });
        builder.addAll((Iterable)ObfuscationServices.getInstance().getSupportedOptions());
        return (Set<String>)builder.build();
    }
    
    private SupportedOptions() {
    }
    
    static {
        TOKENS = "tokens";
        DISABLE_TARGET_EXPORT = "disableTargetExport";
        DISABLE_OVERWRITE_CHECKER = "disableOverwriteChecker";
        OUT_REFMAP_FILE = "outRefMapFile";
        OVERWRITE_ERROR_LEVEL = "overwriteErrorLevel";
        DEPENDENCY_TARGETS_FILE = "dependencyTargetsFile";
        DISABLE_TARGET_VALIDATOR = "disableTargetValidator";
        DEFAULT_OBFUSCATION_ENV = "defaultObfuscationEnv";
    }
}
