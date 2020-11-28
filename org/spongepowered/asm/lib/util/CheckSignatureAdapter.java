// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.lib.util;

import org.spongepowered.asm.lib.signature.SignatureVisitor;

public class CheckSignatureAdapter extends SignatureVisitor
{
    private final /* synthetic */ SignatureVisitor sv;
    private final /* synthetic */ int type;
    private /* synthetic */ int state;
    private /* synthetic */ boolean canBeVoid;
    
    @Override
    public void visitClassType(final String s) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkInternalName(s, "class name");
        this.state = 128;
        if (this.sv != null) {
            this.sv.visitClassType(s);
        }
    }
    
    @Override
    public SignatureVisitor visitParameterType() {
        if (this.type != 1 || (this.state & 0x17) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 16;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitParameterType());
    }
    
    @Override
    public SignatureVisitor visitSuperclass() {
        if (this.type != 0 || (this.state & 0x7) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 8;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitSuperclass());
    }
    
    @Override
    public void visitFormalTypeParameter(final String s) {
        if (this.type == 2 || (this.state != 1 && this.state != 2 && this.state != 4)) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(s, "formal type parameter");
        this.state = 2;
        if (this.sv != null) {
            this.sv.visitFormalTypeParameter(s);
        }
    }
    
    @Override
    public SignatureVisitor visitTypeArgument(final char ch) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if ("+-=".indexOf(ch) == -1) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitTypeArgument(ch));
    }
    
    @Override
    public void visitTypeArgument() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if (this.sv != null) {
            this.sv.visitTypeArgument();
        }
    }
    
    @Override
    public void visitInnerClassType(final String s) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(s, "inner class name");
        if (this.sv != null) {
            this.sv.visitInnerClassType(s);
        }
    }
    
    @Override
    public void visitEnd() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        this.state = 256;
        if (this.sv != null) {
            this.sv.visitEnd();
        }
    }
    
    @Override
    public SignatureVisitor visitExceptionType() {
        if (this.state != 32) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitExceptionType());
    }
    
    public CheckSignatureAdapter(final int n, final SignatureVisitor signatureVisitor) {
        this(327680, n, signatureVisitor);
    }
    
    @Override
    public SignatureVisitor visitReturnType() {
        if (this.type != 1 || (this.state & 0x17) == 0x0) {
            throw new IllegalArgumentException();
        }
        this.state = 32;
        final CheckSignatureAdapter checkSignatureAdapter = new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitReturnType());
        checkSignatureAdapter.canBeVoid = true;
        return checkSignatureAdapter;
    }
    
    protected CheckSignatureAdapter(final int n, final int type, final SignatureVisitor sv) {
        super(n);
        this.type = type;
        this.state = 1;
        this.sv = sv;
    }
    
    @Override
    public void visitTypeVariable(final String s) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(s, "type variable");
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitTypeVariable(s);
        }
    }
    
    @Override
    public SignatureVisitor visitInterfaceBound() {
        if (this.state != 2 && this.state != 4) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitInterfaceBound());
    }
    
    @Override
    public SignatureVisitor visitInterface() {
        if (this.state != 8) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitInterface());
    }
    
    @Override
    public void visitBaseType(final char ch) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        if (ch == 'V') {
            if (!this.canBeVoid) {
                throw new IllegalArgumentException();
            }
        }
        else if ("ZCBSIFJD".indexOf(ch) == -1) {
            throw new IllegalArgumentException();
        }
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitBaseType(ch);
        }
    }
    
    static {
        BOUND = 4;
        METHOD_SIGNATURE = 1;
        SUPER = 8;
        PARAM = 16;
        RETURN = 32;
        FORMAL = 2;
        SIMPLE_TYPE = 64;
        END = 256;
        EMPTY = 1;
        TYPE_SIGNATURE = 2;
        CLASS_SIGNATURE = 0;
        CLASS_TYPE = 128;
    }
    
    @Override
    public SignatureVisitor visitClassBound() {
        if (this.state != 2) {
            throw new IllegalStateException();
        }
        this.state = 4;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitClassBound());
    }
    
    @Override
    public SignatureVisitor visitArrayType() {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        this.state = 64;
        return new CheckSignatureAdapter(2, (this.sv == null) ? null : this.sv.visitArrayType());
    }
}
