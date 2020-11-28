// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.interfaces;

import org.spongepowered.asm.util.ITokenProvider;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Messager;

public interface IMixinAnnotationProcessor extends IOptionProvider, Messager
{
    IJavadocProvider getJavadocProvider();
    
    CompilerEnvironment getCompilerEnvironment();
    
    ProcessingEnvironment getProcessingEnvironment();
    
    IObfuscationManager getObfuscationManager();
    
    ITypeHandleProvider getTypeProvider();
    
    ITokenProvider getTokenProvider();
    
    public enum CompilerEnvironment
    {
        JAVAC, 
        JDT;
    }
}
