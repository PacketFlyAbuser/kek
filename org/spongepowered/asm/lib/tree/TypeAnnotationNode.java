// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.tree;

import org.spongepowered.asm.lib.TypePath;

public class TypeAnnotationNode extends AnnotationNode
{
    public /* synthetic */ TypePath typePath;
    public /* synthetic */ int typeRef;
    
    public TypeAnnotationNode(final int n, final int typeRef, final TypePath typePath, final String s) {
        super(n, s);
        this.typeRef = typeRef;
        this.typePath = typePath;
    }
    
    public TypeAnnotationNode(final int n, final TypePath typePath, final String s) {
        this(327680, n, typePath, s);
        if (this.getClass() != TypeAnnotationNode.class) {
            throw new IllegalStateException();
        }
    }
}
