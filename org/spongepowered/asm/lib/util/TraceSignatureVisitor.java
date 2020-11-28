// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.signature.SignatureVisitor;

public final class TraceSignatureVisitor extends SignatureVisitor
{
    private /* synthetic */ int arrayStack;
    private /* synthetic */ boolean seenInterface;
    private /* synthetic */ boolean seenFormalParameter;
    private /* synthetic */ boolean seenParameter;
    private /* synthetic */ String separator;
    private /* synthetic */ StringBuilder exceptions;
    private /* synthetic */ boolean seenInterfaceBound;
    private /* synthetic */ StringBuilder returnType;
    private final /* synthetic */ StringBuilder declaration;
    private /* synthetic */ boolean isInterface;
    private /* synthetic */ int argumentStack;
    
    @Override
    public void visitBaseType(final char c) {
        switch (c) {
            case 'V': {
                this.declaration.append("void");
                break;
            }
            case 'B': {
                this.declaration.append("byte");
                break;
            }
            case 'J': {
                this.declaration.append("long");
                break;
            }
            case 'Z': {
                this.declaration.append("boolean");
                break;
            }
            case 'I': {
                this.declaration.append("int");
                break;
            }
            case 'S': {
                this.declaration.append("short");
                break;
            }
            case 'C': {
                this.declaration.append("char");
                break;
            }
            case 'F': {
                this.declaration.append("float");
                break;
            }
            default: {
                this.declaration.append("double");
                break;
            }
        }
        this.endType();
    }
    
    @Override
    public void visitInnerClassType(final String s) {
        if (this.argumentStack % 2 != 0) {
            this.declaration.append('>');
        }
        this.argumentStack /= 2;
        this.declaration.append('.');
        this.declaration.append(this.separator).append(s.replace('/', '.'));
        this.separator = "";
        this.argumentStack *= 2;
    }
    
    @Override
    public SignatureVisitor visitInterfaceBound() {
        this.separator = (this.seenInterfaceBound ? ", " : " extends ");
        this.seenInterfaceBound = true;
        this.startType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitInterface() {
        this.separator = (this.seenInterface ? ", " : (this.isInterface ? " extends " : " implements "));
        this.seenInterface = true;
        this.startType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitArrayType() {
        this.startType();
        this.arrayStack |= 0x1;
        return this;
    }
    
    @Override
    public void visitTypeArgument() {
        if (this.argumentStack % 2 == 0) {
            ++this.argumentStack;
            this.declaration.append('<');
        }
        else {
            this.declaration.append(", ");
        }
        this.declaration.append('?');
    }
    
    @Override
    public void visitFormalTypeParameter(final String str) {
        this.declaration.append(this.seenFormalParameter ? ", " : "<").append(str);
        this.seenFormalParameter = true;
        this.seenInterfaceBound = false;
    }
    
    @Override
    public SignatureVisitor visitReturnType() {
        this.endFormals();
        if (this.seenParameter) {
            this.seenParameter = false;
        }
        else {
            this.declaration.append('(');
        }
        this.declaration.append(')');
        this.returnType = new StringBuilder();
        return new TraceSignatureVisitor(this.returnType);
    }
    
    private void startType() {
        this.arrayStack *= 2;
    }
    
    private void endFormals() {
        if (this.seenFormalParameter) {
            this.declaration.append('>');
            this.seenFormalParameter = false;
        }
    }
    
    public String getReturnType() {
        return (this.returnType == null) ? null : this.returnType.toString();
    }
    
    @Override
    public void visitEnd() {
        if (this.argumentStack % 2 != 0) {
            this.declaration.append('>');
        }
        this.argumentStack /= 2;
        this.endType();
    }
    
    @Override
    public SignatureVisitor visitClassBound() {
        this.separator = " extends ";
        this.startType();
        return this;
    }
    
    @Override
    public SignatureVisitor visitExceptionType() {
        if (this.exceptions == null) {
            this.exceptions = new StringBuilder();
        }
        else {
            this.exceptions.append(", ");
        }
        return new TraceSignatureVisitor(this.exceptions);
    }
    
    @Override
    public SignatureVisitor visitParameterType() {
        this.endFormals();
        if (this.seenParameter) {
            this.declaration.append(", ");
        }
        else {
            this.seenParameter = true;
            this.declaration.append('(');
        }
        this.startType();
        return this;
    }
    
    public TraceSignatureVisitor(final int n) {
        super(327680);
        this.separator = "";
        this.isInterface = ((n & 0x200) != 0x0);
        this.declaration = new StringBuilder();
    }
    
    public String getExceptions() {
        return (this.exceptions == null) ? null : this.exceptions.toString();
    }
    
    @Override
    public void visitTypeVariable(final String str) {
        this.declaration.append(str);
        this.endType();
    }
    
    private TraceSignatureVisitor(final StringBuilder declaration) {
        super(327680);
        this.separator = "";
        this.declaration = declaration;
    }
    
    @Override
    public SignatureVisitor visitTypeArgument(final char c) {
        if (this.argumentStack % 2 == 0) {
            ++this.argumentStack;
            this.declaration.append('<');
        }
        else {
            this.declaration.append(", ");
        }
        if (c == '+') {
            this.declaration.append("? extends ");
        }
        else if (c == '-') {
            this.declaration.append("? super ");
        }
        this.startType();
        return this;
    }
    
    @Override
    public void visitClassType(final String anObject) {
        if ("java/lang/Object".equals(anObject)) {
            if (this.argumentStack % 2 != 0 || this.seenParameter) {
                this.declaration.append(this.separator).append(anObject.replace('/', '.'));
            }
        }
        else {
            this.declaration.append(this.separator).append(anObject.replace('/', '.'));
        }
        this.separator = "";
        this.argumentStack *= 2;
    }
    
    @Override
    public SignatureVisitor visitSuperclass() {
        this.endFormals();
        this.separator = " extends ";
        this.startType();
        return this;
    }
    
    public String getDeclaration() {
        return this.declaration.toString();
    }
    
    private void endType() {
        if (this.arrayStack % 2 == 0) {
            this.arrayStack /= 2;
        }
        else {
            while (this.arrayStack % 2 != 0) {
                this.arrayStack /= 2;
                this.declaration.append("[]");
            }
        }
    }
}
