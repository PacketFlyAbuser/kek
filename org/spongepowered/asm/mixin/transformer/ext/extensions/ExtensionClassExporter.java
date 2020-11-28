// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.ext.extensions;

import java.util.regex.Pattern;
import org.spongepowered.asm.util.perf.Profiler;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.spongepowered.asm.util.Constants;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionClassExporter implements IExtension
{
    private final /* synthetic */ IDecompiler decompiler;
    private static final /* synthetic */ Logger logger;
    private final /* synthetic */ File classExportDir;
    
    static {
        EXPORT_JAVA_DIR = "java";
        DECOMPILER_CLASS = "org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler";
        EXPORT_CLASS_DIR = "class";
        logger = LogManager.getLogger("mixin");
    }
    
    public ExtensionClassExporter(final MixinEnvironment mixinEnvironment) {
        this.classExportDir = new File(Constants.DEBUG_OUTPUT_DIR, "class");
        this.decompiler = this.initDecompiler(mixinEnvironment, new File(Constants.DEBUG_OUTPUT_DIR, "java"));
        try {
            FileUtils.deleteDirectory(this.classExportDir);
        }
        catch (IOException ex) {
            ExtensionClassExporter.logger.warn("Error cleaning class output directory: {}", new Object[] { ex.getMessage() });
        }
    }
    
    @Override
    public void preApply(final ITargetClassContext targetClassContext) {
    }
    
    public File dumpClass(final String str, final byte[] array) {
        final File file = new File(this.classExportDir, str + ".class");
        try {
            FileUtils.writeByteArrayToFile(file, array);
        }
        catch (IOException ex) {}
        return file;
    }
    
    @Override
    public void export(final MixinEnvironment mixinEnvironment, final String s, final boolean b, final byte[] array) {
        if (b || mixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
            final String optionValue = mixinEnvironment.getOptionValue(MixinEnvironment.Option.DEBUG_EXPORT_FILTER);
            if (b || optionValue == null || this.applyFilter(optionValue, s)) {
                final Profiler.Section begin = MixinEnvironment.getProfiler().begin("debug.export");
                final File dumpClass = this.dumpClass(s.replace('.', '/'), array);
                if (this.decompiler != null) {
                    this.decompiler.decompile(dumpClass);
                }
                begin.end();
            }
        }
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment mixinEnvironment) {
        return true;
    }
    
    @Override
    public void postApply(final ITargetClassContext targetClassContext) {
    }
    
    private IDecompiler initDecompiler(final MixinEnvironment mixinEnvironment, final File file) {
        if (!mixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE)) {
            return null;
        }
        try {
            final boolean option = mixinEnvironment.getOption(MixinEnvironment.Option.DEBUG_EXPORT_DECOMPILE_THREADED);
            ExtensionClassExporter.logger.info("Attempting to load Fernflower decompiler{}", new Object[] { option ? " (Threaded mode)" : "" });
            final IDecompiler decompiler = (IDecompiler)Class.forName("org.spongepowered.asm.mixin.transformer.debug.RuntimeDecompiler" + (option ? "Async" : "")).getDeclaredConstructor(File.class).newInstance(file);
            ExtensionClassExporter.logger.info("Fernflower decompiler was successfully initialised, exported classes will be decompiled{}", new Object[] { option ? " in a separate thread" : "" });
            return decompiler;
        }
        catch (Throwable t) {
            ExtensionClassExporter.logger.info("Fernflower could not be loaded, exported classes will not be decompiled. {}: {}", new Object[] { t.getClass().getSimpleName(), t.getMessage() });
            return null;
        }
    }
    
    private boolean applyFilter(final String s, final String input) {
        return Pattern.compile(this.prepareFilter(s), 2).matcher(input).matches();
    }
    
    private String prepareFilter(String string) {
        string = "^\\Q" + string.replace("**", "\u0081").replace("*", "\u0082").replace("?", "\u0083") + "\\E$";
        return string.replace("\u0081", "\\E.*\\Q").replace("\u0082", "\\E[^\\.]+\\Q").replace("\u0083", "\\E.\\Q").replace("\\Q\\E", "");
    }
}
