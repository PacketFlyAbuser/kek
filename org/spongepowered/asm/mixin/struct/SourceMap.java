// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.struct;

import java.util.ListIterator;
import org.spongepowered.asm.lib.tree.LineNumberNode;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.lib.tree.ClassNode;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SourceMap
{
    private final /* synthetic */ Map<String, Stratum> strata;
    private final /* synthetic */ String sourceFile;
    private /* synthetic */ String defaultStratum;
    private /* synthetic */ int nextLineOffset;
    
    public SourceMap(final String sourceFile) {
        this.strata = new LinkedHashMap<String, Stratum>();
        this.nextLineOffset = 1;
        this.defaultStratum = "Mixin";
        this.sourceFile = sourceFile;
    }
    
    public String getPseudoGeneratedSourceFile() {
        return this.sourceFile.replace(".java", "$mixin.java");
    }
    
    public File addFile(final String s, final String s2, final int n) {
        return this.addFile(this.defaultStratum, s, s2, n);
    }
    
    public File addFile(final String s, final String s2, final String s3, final int n) {
        Stratum stratum = this.strata.get(s);
        if (stratum == null) {
            stratum = new Stratum(s);
            this.strata.put(s, stratum);
        }
        final File addFile = stratum.addFile(this.nextLineOffset, n, s2, s3);
        this.nextLineOffset += n;
        return addFile;
    }
    
    public String getSourceFile() {
        return this.sourceFile;
    }
    
    static {
        NEWLINE = "\n";
        DEFAULT_STRATUM = "Mixin";
    }
    
    private void appendTo(final StringBuilder sb) {
        sb.append("SMAP").append("\n");
        sb.append(this.getSourceFile()).append("\n");
        sb.append(this.defaultStratum).append("\n");
        final Iterator<Stratum> iterator = this.strata.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().appendTo(sb);
        }
        sb.append("*E").append("\n");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.appendTo(sb);
        return sb.toString();
    }
    
    public File addFile(final String s, final ClassNode classNode) {
        return this.addFile(s, classNode.sourceFile, classNode.name + ".java", Bytecode.getMaxLineNumber(classNode, 500, 50));
    }
    
    public File addFile(final ClassNode classNode) {
        return this.addFile(this.defaultStratum, classNode);
    }
    
    public static class File
    {
        public final /* synthetic */ int lineOffset;
        public final /* synthetic */ String sourceFilePath;
        public final /* synthetic */ int size;
        public final /* synthetic */ int id;
        public final /* synthetic */ String sourceFileName;
        
        public void applyOffset(final MethodNode methodNode) {
            for (final AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                if (abstractInsnNode instanceof LineNumberNode) {
                    final LineNumberNode lineNumberNode = (LineNumberNode)abstractInsnNode;
                    lineNumberNode.line += this.lineOffset - 1;
                }
            }
        }
        
        public void appendLines(final StringBuilder sb) {
            sb.append("1#").append(this.id).append(",").append(this.size).append(":").append(this.lineOffset).append("\n");
        }
        
        public File(final int n, final int n2, final int n3, final String s) {
            this(n, n2, n3, s, null);
        }
        
        void appendFile(final StringBuilder sb) {
            if (this.sourceFilePath != null) {
                sb.append("+ ").append(this.id).append(" ").append(this.sourceFileName).append("\n");
                sb.append(this.sourceFilePath).append("\n");
            }
            else {
                sb.append(this.id).append(" ").append(this.sourceFileName).append("\n");
            }
        }
        
        public File(final int id, final int lineOffset, final int size, final String sourceFileName, final String sourceFilePath) {
            this.id = id;
            this.lineOffset = lineOffset;
            this.size = size;
            this.sourceFileName = sourceFileName;
            this.sourceFilePath = sourceFilePath;
        }
        
        public void applyOffset(final ClassNode classNode) {
            final Iterator<MethodNode> iterator = classNode.methods.iterator();
            while (iterator.hasNext()) {
                this.applyOffset(iterator.next());
            }
        }
    }
    
    static class Stratum
    {
        public final /* synthetic */ String name;
        private final /* synthetic */ Map<String, File> files;
        
        public File addFile(final int n, final int n2, final String s, final String s2) {
            File file = this.files.get(s2);
            if (file == null) {
                file = new File(this.files.size() + 1, n, n2, s, s2);
                this.files.put(s2, file);
            }
            return file;
        }
        
        public Stratum(final String name) {
            this.files = new LinkedHashMap<String, File>();
            this.name = name;
        }
        
        void appendTo(final StringBuilder sb) {
            sb.append("*S").append(" ").append(this.name).append("\n");
            sb.append("*F").append("\n");
            final Iterator<File> iterator = this.files.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().appendFile(sb);
            }
            sb.append("*L").append("\n");
            final Iterator<File> iterator2 = this.files.values().iterator();
            while (iterator2.hasNext()) {
                iterator2.next().appendLines(sb);
            }
        }
        
        static {
            STRATUM_MARK = "*S";
            FILE_MARK = "*F";
            LINES_MARK = "*L";
        }
    }
}
