// 
// Decompiled by Procyon v0.5.36
// 

package io.github.classgraph;

import nonapi.io.github.classgraph.scanspec.ScanSpec;
import nonapi.io.github.classgraph.utils.JarUtils;
import nonapi.io.github.classgraph.utils.ReflectionUtils;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.Iterator;
import nonapi.io.github.classgraph.utils.Join;
import java.io.File;
import java.util.List;
import java.util.Set;

public class ModulePathInfo
{
    public final /* synthetic */ Set<String> addOpens;
    public final /* synthetic */ Set<String> addReads;
    public final /* synthetic */ Set<String> addModules;
    private final /* synthetic */ List<Set<String>> fields;
    public final /* synthetic */ Set<String> patchModules;
    private static final /* synthetic */ List<Character> argPartSeparatorChars;
    public final /* synthetic */ Set<String> modulePath;
    private static final /* synthetic */ List<String> argSwitches;
    public final /* synthetic */ Set<String> addExports;
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(1024);
        if (!this.modulePath.isEmpty()) {
            sb.append("--module-path=");
            sb.append(Join.join(File.pathSeparator, this.modulePath));
        }
        if (!this.addModules.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("--add-modules=");
            sb.append(Join.join(",", this.addModules));
        }
        for (final String str : this.patchModules) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("--patch-module=");
            sb.append(str);
        }
        for (final String str2 : this.addExports) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("--add-exports=");
            sb.append(str2);
        }
        for (final String str3 : this.addOpens) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("--add-opens=");
            sb.append(str3);
        }
        for (final String str4 : this.addReads) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append("--add-reads=");
            sb.append(str4);
        }
        return sb.toString();
    }
    
    static {
        argSwitches = Arrays.asList("--module-path=", "--add-modules=", "--patch-module=", "--add-exports=", "--add-opens=", "--add-reads=");
        argPartSeparatorChars = Arrays.asList(File.pathSeparatorChar, ',', '\0', '\0', '\0', '\0');
    }
    
    public ModulePathInfo() {
        this.modulePath = new LinkedHashSet<String>();
        this.addModules = new LinkedHashSet<String>();
        this.patchModules = new LinkedHashSet<String>();
        this.addExports = new LinkedHashSet<String>();
        this.addOpens = new LinkedHashSet<String>();
        this.addReads = new LinkedHashSet<String>();
        this.fields = Arrays.asList(this.modulePath, this.addModules, this.patchModules, this.addExports, this.addOpens, this.addReads);
        final Class<?> classForNameOrNull = ReflectionUtils.classForNameOrNull("java.lang.management.ManagementFactory");
        final Object o = (classForNameOrNull == null) ? null : ReflectionUtils.invokeStaticMethod(classForNameOrNull, "getRuntimeMXBean", false);
        final List list = (o == null) ? null : ((List)ReflectionUtils.invokeMethod(o, "getInputArguments", false));
        if (list != null) {
            for (final String s : list) {
                for (int i = 0; i < this.fields.size(); ++i) {
                    final String prefix = ModulePathInfo.argSwitches.get(i);
                    if (s.startsWith(prefix)) {
                        final String substring = s.substring(prefix.length());
                        final Set<String> set = this.fields.get(i);
                        final char charValue = ModulePathInfo.argPartSeparatorChars.get(i);
                        if (charValue == '\0') {
                            set.add(substring);
                        }
                        else {
                            final String[] smartPathSplit = JarUtils.smartPathSplit(substring, charValue, null);
                            for (int length = smartPathSplit.length, j = 0; j < length; ++j) {
                                set.add(smartPathSplit[j]);
                            }
                        }
                    }
                }
            }
        }
    }
}
