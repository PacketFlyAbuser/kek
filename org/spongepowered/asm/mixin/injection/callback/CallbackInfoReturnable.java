// 
// Decompiled by Procyon v0.5.36
// 

package org.spongepowered.asm.mixin.injection.callback;

import org.spongepowered.asm.lib.Type;

public class CallbackInfoReturnable<R> extends CallbackInfo
{
    private /* synthetic */ R returnValue;
    
    public float getReturnValueF() {
        if (this.returnValue == null) {
            return 0.0f;
        }
        return (float)this.returnValue;
    }
    
    public R getReturnValue() {
        return this.returnValue;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b) {
        super(s, b);
        this.returnValue = null;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final char c) {
        super(s, b);
        this.returnValue = (R)Character.valueOf(c);
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final double d) {
        super(s, b);
        this.returnValue = (R)Double.valueOf(d);
    }
    
    public boolean getReturnValueZ() {
        return this.returnValue != null && (boolean)this.returnValue;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final float f) {
        super(s, b);
        this.returnValue = (R)Float.valueOf(f);
    }
    
    static String getReturnDescriptor(final Type type) {
        if (type.getSort() == 10 || type.getSort() == 9) {
            return String.format("()%s", "Ljava/lang/Object;");
        }
        return String.format("()%s", type.getDescriptor());
    }
    
    public byte getReturnValueB() {
        if (this.returnValue == null) {
            return 0;
        }
        return (byte)this.returnValue;
    }
    
    static String getReturnAccessor(final Type type) {
        if (type.getSort() == 10 || type.getSort() == 9) {
            return "getReturnValue";
        }
        return String.format("getReturnValue%s", type.getDescriptor());
    }
    
    public short getReturnValueS() {
        if (this.returnValue == null) {
            return 0;
        }
        return (short)this.returnValue;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final byte b2) {
        super(s, b);
        this.returnValue = (R)Byte.valueOf(b2);
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final int i) {
        super(s, b);
        this.returnValue = (R)Integer.valueOf(i);
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final short s2) {
        super(s, b);
        this.returnValue = (R)Short.valueOf(s2);
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final long l) {
        super(s, b);
        this.returnValue = (R)Long.valueOf(l);
    }
    
    public long getReturnValueJ() {
        if (this.returnValue == null) {
            return 0L;
        }
        return (long)this.returnValue;
    }
    
    public void setReturnValue(final R returnValue) throws CancellationException {
        super.cancel();
        this.returnValue = returnValue;
    }
    
    public char getReturnValueC() {
        if (this.returnValue == null) {
            return '\0';
        }
        return (char)this.returnValue;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final boolean b2) {
        super(s, b);
        this.returnValue = (R)Boolean.valueOf(b2);
    }
    
    public int getReturnValueI() {
        if (this.returnValue == null) {
            return 0;
        }
        return (int)this.returnValue;
    }
    
    public double getReturnValueD() {
        if (this.returnValue == null) {
            return 0.0;
        }
        return (double)this.returnValue;
    }
    
    public CallbackInfoReturnable(final String s, final boolean b, final R returnValue) {
        super(s, b);
        this.returnValue = returnValue;
    }
}
