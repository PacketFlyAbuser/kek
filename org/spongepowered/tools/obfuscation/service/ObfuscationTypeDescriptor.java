// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.service;

import org.spongepowered.tools.obfuscation.ObfuscationEnvironment;

public class ObfuscationTypeDescriptor
{
    private final /* synthetic */ String extraInputFilesArgName;
    private final /* synthetic */ Class<? extends ObfuscationEnvironment> environmentType;
    private final /* synthetic */ String outFileArgName;
    private final /* synthetic */ String inputFileArgName;
    private final /* synthetic */ String key;
    
    public final String getKey() {
        return this.key;
    }
    
    public ObfuscationTypeDescriptor(final String s, final String s2, final String s3, final Class<? extends ObfuscationEnvironment> clazz) {
        this(s, s2, null, s3, clazz);
    }
    
    public String getExtraInputFilesOption() {
        return this.extraInputFilesArgName;
    }
    
    public ObfuscationTypeDescriptor(final String key, final String inputFileArgName, final String extraInputFilesArgName, final String outFileArgName, final Class<? extends ObfuscationEnvironment> environmentType) {
        this.key = key;
        this.inputFileArgName = inputFileArgName;
        this.extraInputFilesArgName = extraInputFilesArgName;
        this.outFileArgName = outFileArgName;
        this.environmentType = environmentType;
    }
    
    public String getInputFileOption() {
        return this.inputFileArgName;
    }
    
    public Class<? extends ObfuscationEnvironment> getEnvironmentType() {
        return this.environmentType;
    }
    
    public String getOutputFileOption() {
        return this.outFileArgName;
    }
}
