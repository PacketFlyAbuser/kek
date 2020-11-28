// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.code;

import java.util.NoSuchElementException;
import java.util.ListIterator;
import com.google.common.base.Strings;
import org.spongepowered.asm.util.Bytecode;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.IInjectionPointContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.lib.tree.AnnotationNode;
import org.spongepowered.asm.mixin.injection.throwables.InvalidSliceException;
import java.util.Collection;
import org.spongepowered.asm.lib.tree.InsnList;
import org.spongepowered.asm.lib.tree.AbstractInsnNode;
import java.util.LinkedList;
import org.spongepowered.asm.lib.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.InjectionPoint;

public final class MethodSlice
{
    private final /* synthetic */ String id;
    private final /* synthetic */ InjectionPoint from;
    private final /* synthetic */ ISliceContext owner;
    private final /* synthetic */ InjectionPoint to;
    private final /* synthetic */ String name;
    
    private int find(final MethodNode methodNode, final InjectionPoint injectionPoint, final int n, final String s) {
        if (injectionPoint == null) {
            return n;
        }
        final LinkedList<AbstractInsnNode> list = new LinkedList<AbstractInsnNode>();
        final boolean find = injectionPoint.find(methodNode.desc, new ReadOnlyInsnList(methodNode.instructions), list);
        final InjectionPoint.Selector selector = injectionPoint.getSelector();
        if (list.size() != 1 && selector == InjectionPoint.Selector.ONE) {
            throw new InvalidSliceException(this.owner, String.format("%s requires 1 result but found %d", this.describe(s), list.size()));
        }
        if (!find) {
            return n;
        }
        return methodNode.instructions.indexOf((selector == InjectionPoint.Selector.FIRST) ? list.getFirst() : list.getLast());
    }
    
    public static MethodSlice parse(final ISliceContext sliceContext, final AnnotationNode annotationNode) {
        final String s = Annotations.getValue(annotationNode, "id");
        final AnnotationNode annotationNode2 = Annotations.getValue(annotationNode, "from");
        final AnnotationNode annotationNode3 = Annotations.getValue(annotationNode, "to");
        return new MethodSlice(sliceContext, s, (annotationNode2 != null) ? InjectionPoint.parse(sliceContext, annotationNode2) : null, (annotationNode3 != null) ? InjectionPoint.parse(sliceContext, annotationNode3) : null);
    }
    
    public ReadOnlyInsnList getSlice(final MethodNode methodNode) {
        final int i = methodNode.instructions.size() - 1;
        final int find = this.find(methodNode, this.from, 0, this.name + "(from)");
        final int find2 = this.find(methodNode, this.to, i, this.name + "(to)");
        if (find > find2) {
            throw new InvalidSliceException(this.owner, String.format("%s is negative size. Range(%d -> %d)", this.describe(), find, find2));
        }
        if (find < 0 || find2 < 0 || find > i || find2 > i) {
            throw new InjectionError("Unexpected critical error in " + this + ": out of bounds start=" + find + " end=" + find2 + " lim=" + i);
        }
        if (find == 0 && find2 == i) {
            return new ReadOnlyInsnList(methodNode.instructions);
        }
        return new InsnListSlice(methodNode.instructions, find, find2);
    }
    
    public String getId() {
        return this.id;
    }
    
    public static MethodSlice parse(final ISliceContext sliceContext, final Slice slice) {
        final String id = slice.id();
        final At from = slice.from();
        final At to = slice.to();
        return new MethodSlice(sliceContext, id, (from != null) ? InjectionPoint.parse(sliceContext, from) : null, (to != null) ? InjectionPoint.parse(sliceContext, to) : null);
    }
    
    private static String describeSlice(final String s, final ISliceContext sliceContext) {
        final String simpleName = Bytecode.getSimpleName(sliceContext.getAnnotation());
        final MethodNode method = sliceContext.getMethod();
        return String.format("%s->%s(%s)::%s%s", sliceContext.getContext(), simpleName, s, method.name, method.desc);
    }
    
    @Override
    public String toString() {
        return this.describe();
    }
    
    private String describe(final String s) {
        return describeSlice(s, this.owner);
    }
    
    private static String getSliceName(final String s) {
        return String.format("@Slice[%s]", Strings.nullToEmpty(s));
    }
    
    private String describe() {
        return this.describe(this.name);
    }
    
    private MethodSlice(final ISliceContext owner, final String s, final InjectionPoint from, final InjectionPoint to) {
        if (from == null && to == null) {
            throw new InvalidSliceException(owner, String.format("%s is redundant. No 'from' or 'to' value specified", this));
        }
        this.owner = owner;
        this.id = Strings.nullToEmpty(s);
        this.from = from;
        this.to = to;
        this.name = getSliceName(s);
    }
    
    static final class InsnListSlice extends ReadOnlyInsnList
    {
        private final /* synthetic */ int start;
        private final /* synthetic */ int end;
        
        @Override
        public int indexOf(final AbstractInsnNode abstractInsnNode) {
            final int index = super.indexOf(abstractInsnNode);
            return (index >= this.start && index <= this.end) ? (index - this.start) : -1;
        }
        
        @Override
        public AbstractInsnNode getFirst() {
            return super.get(this.start);
        }
        
        @Override
        public boolean contains(final AbstractInsnNode abstractInsnNode) {
            final AbstractInsnNode[] array = this.toArray();
            for (int length = array.length, i = 0; i < length; ++i) {
                if (array[i] == abstractInsnNode) {
                    return true;
                }
            }
            return false;
        }
        
        public int realIndexOf(final AbstractInsnNode abstractInsnNode) {
            return super.indexOf(abstractInsnNode);
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator(final int n) {
            return new SliceIterator(super.iterator(this.start + n), this.start, this.end, this.start + n);
        }
        
        @Override
        public int size() {
            return this.end - this.start + 1;
        }
        
        protected InsnListSlice(final InsnList list, final int start, final int end) {
            super(list);
            this.start = start;
            this.end = end;
        }
        
        @Override
        public AbstractInsnNode[] toArray() {
            final AbstractInsnNode[] array = super.toArray();
            final AbstractInsnNode[] array2 = new AbstractInsnNode[this.size()];
            System.arraycopy(array, this.start, array2, 0, array2.length);
            return array2;
        }
        
        @Override
        public AbstractInsnNode get(final int n) {
            return super.get(this.start + n);
        }
        
        @Override
        public ListIterator<AbstractInsnNode> iterator() {
            return this.iterator(0);
        }
        
        @Override
        public AbstractInsnNode getLast() {
            return super.get(this.end);
        }
        
        static class SliceIterator implements ListIterator<AbstractInsnNode>
        {
            private /* synthetic */ int index;
            private /* synthetic */ int start;
            private /* synthetic */ int end;
            private final /* synthetic */ ListIterator<AbstractInsnNode> iter;
            
            @Override
            public AbstractInsnNode previous() {
                if (this.index <= this.start) {
                    throw new NoSuchElementException();
                }
                --this.index;
                return this.iter.previous();
            }
            
            @Override
            public AbstractInsnNode next() {
                if (this.index > this.end) {
                    throw new NoSuchElementException();
                }
                ++this.index;
                return this.iter.next();
            }
            
            @Override
            public boolean hasNext() {
                return this.index <= this.end && this.iter.hasNext();
            }
            
            @Override
            public void set(final AbstractInsnNode abstractInsnNode) {
                throw new UnsupportedOperationException("Cannot set insn using slice");
            }
            
            @Override
            public void add(final AbstractInsnNode abstractInsnNode) {
                throw new UnsupportedOperationException("Cannot add insn using slice");
            }
            
            public SliceIterator(final ListIterator<AbstractInsnNode> iter, final int start, final int end, final int index) {
                this.iter = iter;
                this.start = start;
                this.end = end;
                this.index = index;
            }
            
            @Override
            public boolean hasPrevious() {
                return this.index > this.start;
            }
            
            @Override
            public int previousIndex() {
                return this.index - this.start - 1;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Cannot remove insn from slice");
            }
            
            @Override
            public int nextIndex() {
                return this.index - this.start;
            }
        }
    }
}
