// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.transformer.ext.extensions;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.spongepowered.asm.util.Constants;
import com.google.common.collect.HashMultimap;
import java.util.Set;
import org.spongepowered.asm.util.SignaturePrinter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import java.io.PrintStream;
import java.io.FileOutputStream;
import org.spongepowered.asm.util.PrettyPrinter;
import java.io.IOException;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.MixinEnvironment;
import java.io.File;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import com.google.common.collect.Multimap;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;

public class ExtensionCheckInterfaces implements IExtension
{
    private final /* synthetic */ Multimap<ClassInfo, ClassInfo.Method> interfaceMethods;
    private static final /* synthetic */ Logger logger;
    private /* synthetic */ boolean strict;
    private final /* synthetic */ File report;
    private final /* synthetic */ File csv;
    
    @Override
    public void export(final MixinEnvironment mixinEnvironment, final String s, final boolean b, final byte[] array) {
    }
    
    static {
        IMPL_REPORT_TXT_FILENAME = "mixin_implementation_report.txt";
        IMPL_REPORT_CSV_FILENAME = "mixin_implementation_report.csv";
        IMPL_REPORT_FILENAME = "mixin_implementation_report";
        AUDIT_DIR = "audit";
        logger = LogManager.getLogger("mixin");
    }
    
    private void appendToCSVReport(final String s, final ClassInfo.Method method, final String s2) {
        try {
            Files.append((CharSequence)String.format("%s,%s,%s,%s\n", s, method.getName(), method.getDesc(), s2), this.csv, Charsets.ISO_8859_1);
        }
        catch (IOException ex) {}
    }
    
    private void appendToTextReport(final PrettyPrinter prettyPrinter) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(this.report, true);
            final PrintStream printStream = new PrintStream(out);
            printStream.print("\n");
            prettyPrinter.print(printStream);
        }
        catch (Exception ex) {}
        finally {
            IOUtils.closeQuietly(out);
        }
    }
    
    @Override
    public void preApply(final ITargetClassContext targetClassContext) {
        final ClassInfo classInfo = targetClassContext.getClassInfo();
        final Iterator<ClassInfo.Method> iterator = classInfo.getInterfaceMethods(false).iterator();
        while (iterator.hasNext()) {
            this.interfaceMethods.put((Object)classInfo, (Object)iterator.next());
        }
    }
    
    @Override
    public void postApply(final ITargetClassContext targetClassContext) {
        final ClassInfo classInfo = targetClassContext.getClassInfo();
        if (classInfo.isAbstract() && !this.strict) {
            ExtensionCheckInterfaces.logger.info("{} is skipping abstract target {}", new Object[] { this.getClass().getSimpleName(), targetClassContext });
            return;
        }
        final String replace = classInfo.getName().replace('/', '.');
        int i = 0;
        final PrettyPrinter prettyPrinter = new PrettyPrinter();
        prettyPrinter.add("Class: %s", replace).hr();
        prettyPrinter.add("%-32s %-47s  %s", "Return Type", "Missing Method", "From Interface").hr();
        final Set<ClassInfo.Method> interfaceMethods = classInfo.getInterfaceMethods(true);
        final HashSet set = new HashSet(classInfo.getSuperClass().getInterfaceMethods(true));
        set.addAll(this.interfaceMethods.removeAll((Object)classInfo));
        for (final ClassInfo.Method method : interfaceMethods) {
            final ClassInfo.Method methodInHierarchy = classInfo.findMethodInHierarchy(method.getName(), method.getDesc(), ClassInfo.SearchType.ALL_CLASSES, ClassInfo.Traversal.ALL);
            if (methodInHierarchy != null && !methodInHierarchy.isAbstract()) {
                continue;
            }
            if (set.contains(method)) {
                continue;
            }
            if (i > 0) {
                prettyPrinter.add();
            }
            final SignaturePrinter setModifiers = new SignaturePrinter(method.getName(), method.getDesc()).setModifiers("");
            final String replace2 = method.getOwner().getName().replace('/', '.');
            ++i;
            prettyPrinter.add("%-32s%s", setModifiers.getReturnType(), setModifiers);
            prettyPrinter.add("%-80s  %s", "", replace2);
            this.appendToCSVReport(replace, method, replace2);
        }
        if (i > 0) {
            prettyPrinter.hr().add("%82s%s: %d", "", "Total unimplemented", i);
            prettyPrinter.print(System.err);
            this.appendToTextReport(prettyPrinter);
        }
    }
    
    public ExtensionCheckInterfaces() {
        this.interfaceMethods = (Multimap<ClassInfo, ClassInfo.Method>)HashMultimap.create();
        final File file = new File(Constants.DEBUG_OUTPUT_DIR, "audit");
        file.mkdirs();
        this.csv = new File(file, "mixin_implementation_report.csv");
        this.report = new File(file, "mixin_implementation_report.txt");
        try {
            Files.write((CharSequence)"Class,Method,Signature,Interface\n", this.csv, Charsets.ISO_8859_1);
        }
        catch (IOException ex) {}
        try {
            Files.write((CharSequence)("Mixin Implementation Report generated on " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n"), this.report, Charsets.ISO_8859_1);
        }
        catch (IOException ex2) {}
    }
    
    @Override
    public boolean checkActive(final MixinEnvironment mixinEnvironment) {
        this.strict = mixinEnvironment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS_STRICT);
        return mixinEnvironment.getOption(MixinEnvironment.Option.CHECK_IMPLEMENTS);
    }
}
