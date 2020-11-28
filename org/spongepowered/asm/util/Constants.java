// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.util;

import org.spongepowered.asm.mixin.Mixin;
import java.io.File;

public abstract class Constants
{
    public static final /* synthetic */ String MIXIN_PACKAGE;
    
    private Constants() {
    }
    
    static {
        STRING = "Ljava/lang/String;";
        CTOR = "<init>";
        UNICODE_SNOWMAN = '\u2603';
        SYNTHETIC_PACKAGE = "org.spongepowered.asm.synthetic";
        DEBUG_OUTPUT_PATH = ".mixin.out";
        CLASS = "Ljava/lang/Class;";
        OBJECT = "Ljava/lang/Object;";
        CLINIT = "<clinit>";
        IMAGINARY_SUPER = "super$";
        MIXIN_PACKAGE = Mixin.class.getPackage().getName();
        MIXIN_PACKAGE_REF = Constants.MIXIN_PACKAGE.replace('.', '/');
        DEBUG_OUTPUT_DIR = new File(".mixin.out");
    }
    
    public abstract static class ManifestAttributes
    {
        private ManifestAttributes() {
        }
        
        static {
            TWEAKER = "TweakClass";
            COMPATIBILITY = "MixinCompatibilityLevel";
            MIXINCONFIGS = "MixinConfigs";
            MAINCLASS = "Main-Class";
            TOKENPROVIDERS = "MixinTokenProviders";
        }
    }
}
