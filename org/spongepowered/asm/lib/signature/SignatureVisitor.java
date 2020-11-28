// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.signature;

public abstract class SignatureVisitor
{
    protected final /* synthetic */ int api;
    
    public SignatureVisitor visitSuperclass() {
        return this;
    }
    
    public SignatureVisitor visitParameterType() {
        return this;
    }
    
    public void visitTypeArgument() {
    }
    
    public SignatureVisitor visitTypeArgument(final char c) {
        return this;
    }
    
    public void visitTypeVariable(final String s) {
    }
    
    static {
        EXTENDS = '+';
        SUPER = '-';
        INSTANCEOF = '=';
    }
    
    public SignatureVisitor(final int api) {
        if (api != 262144 && api != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = api;
    }
    
    public void visitInnerClassType(final String s) {
    }
    
    public SignatureVisitor visitArrayType() {
        return this;
    }
    
    public void visitBaseType(final char c) {
    }
    
    public SignatureVisitor visitClassBound() {
        return this;
    }
    
    public SignatureVisitor visitInterface() {
        return this;
    }
    
    public void visitFormalTypeParameter(final String s) {
    }
    
    public void visitEnd() {
    }
    
    public void visitClassType(final String s) {
    }
    
    public SignatureVisitor visitExceptionType() {
        return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        return this;
    }
    
    public SignatureVisitor visitReturnType() {
        return this;
    }
}
