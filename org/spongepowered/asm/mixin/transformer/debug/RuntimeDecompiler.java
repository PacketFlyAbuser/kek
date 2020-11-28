// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.debug;

import org.jetbrains.java.decompiler.main.Fernflower;
import org.jetbrains.java.decompiler.util.InterpreterUtil;
import org.jetbrains.java.decompiler.main.extern.IBytecodeProvider;
import java.util.jar.Manifest;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import java.util.Map;
import java.io.File;
import org.spongepowered.asm.mixin.transformer.ext.IDecompiler;
import org.jetbrains.java.decompiler.main.extern.IResultSaver;
import org.jetbrains.java.decompiler.main.extern.IFernflowerLogger;

public class RuntimeDecompiler extends IFernflowerLogger implements IResultSaver, IDecompiler
{
    private final /* synthetic */ File outputPath;
    private final /* synthetic */ Map<String, Object> options;
    private static final /* synthetic */ Level[] SEVERITY_LEVELS;
    protected final /* synthetic */ Logger logger;
    
    public void saveDirEntry(final String s, final String s2, final String s3) {
    }
    
    public void saveClassFile(final String s, final String str, final String s2, final String s3, final int[] array) {
        final File obj = new File(this.outputPath, str + ".java");
        obj.getParentFile().mkdirs();
        try {
            this.logger.info("Writing {}", new Object[] { obj.getAbsolutePath() });
            Files.write((CharSequence)s3, obj, Charsets.UTF_8);
        }
        catch (IOException ex) {
            this.writeMessage("Cannot write source file " + obj, ex);
        }
    }
    
    public void copyFile(final String s, final String s2, final String s3) {
    }
    
    public void writeMessage(final String s, final IFernflowerLogger.Severity severity, final Throwable t) {
        this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[severity.ordinal()], s, t);
    }
    
    public RuntimeDecompiler(final File outputPath) {
        this.options = (Map<String, Object>)ImmutableMap.builder().put((Object)"din", (Object)"0").put((Object)"rbr", (Object)"0").put((Object)"dgs", (Object)"1").put((Object)"asc", (Object)"1").put((Object)"den", (Object)"1").put((Object)"hdc", (Object)"1").put((Object)"ind", (Object)"    ").build();
        this.logger = LogManager.getLogger("fernflower");
        this.outputPath = outputPath;
        if (this.outputPath.exists()) {
            try {
                FileUtils.deleteDirectory(this.outputPath);
            }
            catch (IOException ex) {
                this.logger.warn("Error cleaning output directory: {}", new Object[] { ex.getMessage() });
            }
        }
    }
    
    public void closeArchive(final String s, final String s2) {
    }
    
    public void saveClassEntry(final String s, final String s2, final String s3, final String s4, final String s5) {
    }
    
    public void writeMessage(final String s, final Throwable t) {
        this.logger.warn("{} {}: {}", new Object[] { s, t.getClass().getSimpleName(), t.getMessage() });
    }
    
    public void createArchive(final String s, final String s2, final Manifest manifest) {
    }
    
    public void saveFolder(final String s) {
    }
    
    public void startReadingClass(final String s) {
        this.logger.info("Decompiling {}", new Object[] { s });
    }
    
    public void writeMessage(final String s, final IFernflowerLogger.Severity severity) {
        this.logger.log(RuntimeDecompiler.SEVERITY_LEVELS[severity.ordinal()], s);
    }
    
    static {
        SEVERITY_LEVELS = new Level[] { Level.TRACE, Level.INFO, Level.WARN, Level.ERROR };
    }
    
    public void copyEntry(final String s, final String s2, final String s3, final String s4) {
    }
    
    public void decompile(final File file) {
        try {
            final Fernflower fernflower = new Fernflower((IBytecodeProvider)new IBytecodeProvider() {
                private /* synthetic */ byte[] byteCode;
                
                public byte[] getBytecode(final String pathname, final String s) throws IOException {
                    if (this.byteCode == null) {
                        this.byteCode = InterpreterUtil.getBytes(new File(pathname));
                    }
                    return this.byteCode;
                }
            }, (IResultSaver)this, (Map)this.options, (IFernflowerLogger)this);
            fernflower.getStructContext().addSpace(file, true);
            fernflower.decompileContext();
        }
        catch (Throwable t) {
            this.logger.warn("Decompilation error while processing {}", new Object[] { file.getName() });
        }
    }
}
