// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib;

public class Label
{
    /* synthetic */ int status;
    /* synthetic */ Label next;
    private /* synthetic */ int referenceCount;
    /* synthetic */ Frame frame;
    /* synthetic */ Edge successors;
    private /* synthetic */ int[] srcAndRefPositions;
    /* synthetic */ int inputStackTop;
    /* synthetic */ int position;
    
    static {
        RESOLVED = 2;
        JSR = 128;
        SUBROUTINE = 512;
        PUSHED = 8;
        VISITED2 = 2048;
        RET = 256;
        RESIZED = 4;
        TARGET = 16;
        VISITED = 1024;
        DEBUG = 1;
        STORE = 32;
        REACHABLE = 64;
    }
    
    public int getOffset() {
        if ((this.status & 0x2) == 0x0) {
            throw new IllegalStateException("Label offset position has not been resolved yet");
        }
        return this.position;
    }
    
    void visitSubroutine(final Label label, final long n, final int n2) {
        Label next = this;
        while (next != null) {
            final Label label2 = next;
            next = label2.next;
            label2.next = null;
            if (label != null) {
                if ((label2.status & 0x800) != 0x0) {
                    continue;
                }
                final Label label3 = label2;
                label3.status |= 0x800;
                if ((label2.status & 0x100) != 0x0 && !label2.inSameSubroutine(label)) {
                    final Edge successors = new Edge();
                    successors.info = label2.inputStackTop;
                    successors.successor = label.successors.successor;
                    successors.next = label2.successors;
                    label2.successors = successors;
                }
            }
            else {
                if (label2.inSubroutine(n)) {
                    continue;
                }
                label2.addToSubroutine(n, n2);
            }
            for (Edge edge = label2.successors; edge != null; edge = edge.next) {
                if (((label2.status & 0x80) == 0x0 || edge != label2.successors.next) && edge.successor.next == null) {
                    edge.successor.next = next;
                    next = edge.successor;
                }
            }
        }
    }
    
    void put(final MethodWriter methodWriter, final ByteVector byteVector, final int n, final boolean b) {
        if ((this.status & 0x2) == 0x0) {
            if (b) {
                this.addReference(-1 - n, byteVector.length);
                byteVector.putInt(-1);
            }
            else {
                this.addReference(n, byteVector.length);
                byteVector.putShort(-1);
            }
        }
        else if (b) {
            byteVector.putInt(this.position - n);
        }
        else {
            byteVector.putShort(this.position - n);
        }
    }
    
    boolean inSubroutine(final long n) {
        return (this.status & 0x400) != 0x0 && (this.srcAndRefPositions[(int)(n >>> 32)] & (int)n) != 0x0;
    }
    
    @Override
    public String toString() {
        return "L" + System.identityHashCode(this);
    }
    
    Label getFirst() {
        return (this.frame == null) ? this : this.frame.owner;
    }
    
    private void addReference(final int n, final int n2) {
        if (this.srcAndRefPositions == null) {
            this.srcAndRefPositions = new int[6];
        }
        if (this.referenceCount >= this.srcAndRefPositions.length) {
            final int[] srcAndRefPositions = new int[this.srcAndRefPositions.length + 6];
            System.arraycopy(this.srcAndRefPositions, 0, srcAndRefPositions, 0, this.srcAndRefPositions.length);
            this.srcAndRefPositions = srcAndRefPositions;
        }
        this.srcAndRefPositions[this.referenceCount++] = n;
        this.srcAndRefPositions[this.referenceCount++] = n2;
    }
    
    void addToSubroutine(final long n, final int n2) {
        if ((this.status & 0x400) == 0x0) {
            this.status |= 0x400;
            this.srcAndRefPositions = new int[n2 / 32 + 1];
        }
        final int[] srcAndRefPositions = this.srcAndRefPositions;
        final int n3 = (int)(n >>> 32);
        srcAndRefPositions[n3] |= (int)n;
    }
    
    boolean resolve(final MethodWriter methodWriter, final int position, final byte[] array) {
        boolean b = false;
        this.status |= 0x2;
        this.position = position;
        int i = 0;
        while (i < this.referenceCount) {
            final int n = this.srcAndRefPositions[i++];
            int n2 = this.srcAndRefPositions[i++];
            if (n >= 0) {
                final int n3 = position - n;
                if (n3 < -32768 || n3 > 32767) {
                    final int n4 = array[n2 - 1] & 0xFF;
                    if (n4 <= 168) {
                        array[n2 - 1] = (byte)(n4 + 49);
                    }
                    else {
                        array[n2 - 1] = (byte)(n4 + 20);
                    }
                    b = true;
                }
                array[n2++] = (byte)(n3 >>> 8);
                array[n2] = (byte)n3;
            }
            else {
                final int n5 = position + n + 1;
                array[n2++] = (byte)(n5 >>> 24);
                array[n2++] = (byte)(n5 >>> 16);
                array[n2++] = (byte)(n5 >>> 8);
                array[n2] = (byte)n5;
            }
        }
        return b;
    }
    
    boolean inSameSubroutine(final Label label) {
        if ((this.status & 0x400) == 0x0 || (label.status & 0x400) == 0x0) {
            return false;
        }
        for (int i = 0; i < this.srcAndRefPositions.length; ++i) {
            if ((this.srcAndRefPositions[i] & label.srcAndRefPositions[i]) != 0x0) {
                return true;
            }
        }
        return false;
    }
}
