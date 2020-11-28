// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class ColorHolder
{
    /* synthetic */ int r;
    /* synthetic */ int g;
    /* synthetic */ int a;
    /* synthetic */ int b;
    
    public ColorHolder(final int r, final int g, final int b, final int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }
    
    public int toHex() {
        return toHex(this.r, this.g, this.b);
    }
    
    public int getR() {
        return this.r;
    }
    
    public ColorHolder setB(final int b) {
        this.b = b;
        return this;
    }
    
    public void setGLColour(final int n, final int n2, final int n3, final int n4) {
        GL11.glColor4f(((n == -1) ? this.r : n) / 255.0f, ((n2 == -1) ? this.g : n2) / 255.0f, ((n3 == -1) ? this.b : n3) / 255.0f, ((n4 == -1) ? this.a : n4) / 255.0f);
    }
    
    public static ColorHolder fromHex(final int n) {
        final ColorHolder colorHolder = new ColorHolder(0, 0, 0);
        colorHolder.becomeHex(n);
        return colorHolder;
    }
    
    public void becomeHex(final int n) {
        this.setR((n & 0xFF0000) >> 16);
        this.setG((n & 0xFF00) >> 8);
        this.setB(n & 0xFF);
        this.setA(255);
    }
    
    public ColorHolder clone() {
        return new ColorHolder(this.r, this.g, this.b, this.a);
    }
    
    public int getA() {
        return this.a;
    }
    
    public ColorHolder brighter() {
        return new ColorHolder(Math.min(this.r + 10, 255), Math.min(this.g + 10, 255), Math.min(this.b + 10, 255), this.getA());
    }
    
    public ColorHolder setR(final int r) {
        this.r = r;
        return this;
    }
    
    public static int toHex(final int n, final int n2, final int n3) {
        return 0xFF000000 | (n & 0xFF) << 16 | (n2 & 0xFF) << 8 | (n3 & 0xFF);
    }
    
    public ColorHolder setA(final int a) {
        this.a = a;
        return this;
    }
    
    public ColorHolder setG(final int g) {
        this.g = g;
        return this;
    }
    
    public void setGLColour() {
        this.setGLColour(-1, -1, -1, -1);
    }
    
    public int getB() {
        return this.b;
    }
    
    public Color toJavaColour() {
        return new Color(this.r, this.g, this.b, this.a);
    }
    
    public ColorHolder darker() {
        return new ColorHolder(Math.max(this.r - 10, 0), Math.max(this.g - 10, 0), Math.max(this.b - 10, 0), this.getA());
    }
    
    public ColorHolder(final int r, final int g, final int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
    }
    
    public void becomeGLColour() {
    }
    
    public int getG() {
        return this.g;
    }
}
