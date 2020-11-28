// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.spongepowered.asm.lib.TypePath;
import org.spongepowered.asm.lib.Label;
import org.spongepowered.asm.lib.MethodVisitor;
import java.util.List;

public class LocalVariableAnnotationNode extends TypeAnnotationNode
{
    public /* synthetic */ List<LabelNode> end;
    public /* synthetic */ List<Integer> index;
    public /* synthetic */ List<LabelNode> start;
    
    public void accept(final MethodVisitor methodVisitor, final boolean b) {
        final Label[] array = new Label[this.start.size()];
        final Label[] array2 = new Label[this.end.size()];
        final int[] array3 = new int[this.index.size()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = this.start.get(i).getLabel();
            array2[i] = this.end.get(i).getLabel();
            array3[i] = this.index.get(i);
        }
        this.accept(methodVisitor.visitLocalVariableAnnotation(this.typeRef, this.typePath, array, array2, array3, this.desc, true));
    }
    
    public LocalVariableAnnotationNode(final int n, final TypePath typePath, final LabelNode[] array, final LabelNode[] array2, final int[] array3, final String s) {
        this(327680, n, typePath, array, array2, array3, s);
    }
    
    public LocalVariableAnnotationNode(final int n, final int n2, final TypePath typePath, final LabelNode[] a, final LabelNode[] a2, final int[] array, final String s) {
        super(n, n2, typePath, s);
        this.start = new ArrayList<LabelNode>(a.length);
        this.start.addAll(Arrays.asList(a));
        this.end = new ArrayList<LabelNode>(a2.length);
        this.end.addAll(Arrays.asList(a2));
        this.index = new ArrayList<Integer>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            this.index.add(array[i]);
        }
    }
}
