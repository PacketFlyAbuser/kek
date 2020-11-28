// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.tools.obfuscation.mapping.common;

import java.io.IOException;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.lang.model.element.Element;
import javax.tools.StandardLocation;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.PrintWriter;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Filer;
import org.spongepowered.tools.obfuscation.mapping.IMappingWriter;

public abstract class MappingWriter implements IMappingWriter
{
    private final /* synthetic */ Filer filer;
    private final /* synthetic */ Messager messager;
    
    public MappingWriter(final Messager messager, final Filer filer) {
        this.messager = messager;
        this.filer = filer;
    }
    
    protected PrintWriter openFileWriter(final String pathname, final String s) throws IOException {
        if (pathname.matches("^.*[\\\\/:].*$")) {
            final File file = new File(pathname);
            file.getParentFile().mkdirs();
            this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + s + " to " + file.getAbsolutePath());
            return new PrintWriter(file);
        }
        final FileObject resource = this.filer.createResource(StandardLocation.CLASS_OUTPUT, "", pathname, new Element[0]);
        this.messager.printMessage(Diagnostic.Kind.NOTE, "Writing " + s + " to " + new File(resource.toUri()).getAbsolutePath());
        return new PrintWriter(resource.openWriter());
    }
}
