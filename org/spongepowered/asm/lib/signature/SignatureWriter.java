// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.signature;

public class SignatureWriter extends SignatureVisitor
{
    private final /* synthetic */ StringBuilder buf;
    private /* synthetic */ boolean hasParameters;
    private /* synthetic */ int argumentStack;
    private /* synthetic */ boolean hasFormals;
    
    @Override
    public SignatureVisitor visitClassBound() {
        return this;
    }
    
    @Override
    public SignatureVisitor visitParameterType() {
        this.endFormals();
        if (!this.hasParameters) {
            this.hasParameters = true;
            this.buf.append('(');
        }
        return this;
    }
    
    @Override
    public String toString() {
        return this.buf.toString();
    }
    
    @Override
    public SignatureVisitor visitTypeArgument(final char c) {
        if (this.argumentStack % 2 == 0) {
            ++this.argumentStack;
            this.buf.append('<');
        }
        if (c != '=') {
            this.buf.append(c);
        }
        return this;
    }
    
    @Override
    public SignatureVisitor visitExceptionType() {
        this.buf.append('^');
        return this;
    }
    
    public SignatureWriter() {
        super(327680);
        this.buf = new StringBuilder();
    }
    
    @Override
    public void visitInnerClassType(final String str) {
        this.endArguments();
        this.buf.append('.');
        this.buf.append(str);
        this.argumentStack *= 2;
    }
    
    @Override
    public SignatureVisitor visitArrayType() {
        this.buf.append('[');
        return this;
    }
    
    @Override
    public SignatureVisitor visitReturnType() {
        this.endFormals();
        if (!this.hasParameters) {
            this.buf.append('(');
        }
        this.buf.append(')');
        return this;
    }
    
    @Override
    public void visitBaseType(final char c) {
        this.buf.append(c);
    }
    
    @Override
    public void visitFormalTypeParameter(final String str) {
        if (!this.hasFormals) {
            this.hasFormals = true;
            this.buf.append('<');
        }
        this.buf.append(str);
        this.buf.append(':');
    }
    
    @Override
    public void visitEnd() {
        this.endArguments();
        this.buf.append(';');
    }
    
    private void endArguments() {
        if (this.argumentStack % 2 != 0) {
            this.buf.append('>');
        }
        this.argumentStack /= 2;
    }
    
    @Override
    public void visitClassType(final String str) {
        this.buf.append('L');
        this.buf.append(str);
        this.argumentStack *= 2;
    }
    
    @Override
    public void visitTypeVariable(final String str) {
        this.buf.append('T');
        this.buf.append(str);
        this.buf.append(';');
    }
    
    @Override
    public SignatureVisitor visitInterface() {
        return this;
    }
    
    private void endFormals() {
        if (this.hasFormals) {
            this.hasFormals = false;
            this.buf.append('>');
        }
    }
    
    @Override
    public SignatureVisitor visitInterfaceBound() {
        this.buf.append(':');
        return this;
    }
    
    @Override
    public SignatureVisitor visitSuperclass() {
        this.endFormals();
        return this;
    }
    
    @Override
    public void visitTypeArgument() {
        if (this.argumentStack % 2 == 0) {
            ++this.argumentStack;
            this.buf.append('<');
        }
        this.buf.append('*');
    }
}
