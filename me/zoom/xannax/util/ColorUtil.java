// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import java.awt.Color;

public class ColorUtil
{
    private /* synthetic */ float m_Alpha;
    public /* synthetic */ Color m_BaseColor;
    private /* synthetic */ float[] m_HSB;
    
    public static Color GetRainbowColorFromArray(final float[] array, final float n) {
        return GetRainbowColor(array[0], array[1], array[2], n);
    }
    
    public static Color GetColorWithHSBArray(final float[] array) {
        return GetRainbowColorFromArray(array, 1.0f);
    }
    
    private static float FutureClientColorCalculation(final float n, final float n2, float n3) {
        if (n3 < 0.0f) {
            ++n3;
        }
        if (n3 > 1.0f) {
            --n3;
        }
        if (6.0f * n3 < 1.0f) {
            return n + (n2 - n) * 6.0f * n3;
        }
        if (2.0f * n3 < 1.0f) {
            return n2;
        }
        if (3.0f * n3 < 2.0f) {
            return n + (n2 - n) * 6.0f * (0.6666667f - n3);
        }
        return n;
    }
    
    public ColorUtil(final float[] hsb, final float alpha) {
        this.m_HSB = hsb;
        this.m_Alpha = alpha;
        this.m_BaseColor = GetRainbowColorFromArray(hsb, alpha);
    }
    
    public ColorUtil(final Color baseColor) {
        this.m_BaseColor = baseColor;
        this.m_HSB = GenerateHSB(baseColor);
        this.m_Alpha = baseColor.getAlpha() / 255.0f;
    }
    
    public static float[] GenerateHSB(final Color color) {
        final float[] rgbColorComponents = color.getRGBColorComponents(null);
        final float n = rgbColorComponents[0];
        final float n2 = rgbColorComponents[1];
        final float n3 = rgbColorComponents[2];
        final float min = Math.min(n, Math.min(n2, n3));
        final float max = Math.max(n, Math.max(n2, n3));
        float n4 = 0.0f;
        float n5;
        if (max == min) {
            n4 = 0.0f;
            n5 = max;
        }
        else if (max == n) {
            n4 = (60.0f * (n2 - n3) / (max - min) + 360.0f) % 360.0f;
            n5 = max;
        }
        else if (max == n2) {
            n4 = 60.0f * (n3 - n) / (max - min) + 120.0f;
            n5 = max;
        }
        else {
            if (max == n3) {
                n4 = 60.0f * (n - n2) / (max - min) + 240.0f;
            }
            n5 = max;
        }
        final float a = (n5 + min) / 2.0f;
        float n6;
        if (max == min) {
            n6 = 0.0f;
        }
        else {
            final float min2 = Math.min(a, 0.5f);
            final float n7 = max;
            if (min2 <= 0.0f) {
                n6 = (n7 - min) / (max + min);
            }
            else {
                n6 = (n7 - min) / (2.0f - max - min);
            }
        }
        return new float[] { n4, n6 * 100.0f, a * 100.0f };
    }
    
    public float GetLightness() {
        return this.m_HSB[2];
    }
    
    public float GetSaturation() {
        return this.m_HSB[1];
    }
    
    public Color GetColorWithHue(final float n) {
        return GetRainbowColor(n, this.m_HSB[1], this.m_HSB[2], this.m_Alpha);
    }
    
    public Color GetColorWithLightnessMin(float min) {
        min = (100.0f + min) / 100.0f;
        min = Math.min(100.0f, this.m_HSB[2] * min);
        return GetRainbowColor(this.m_HSB[0], this.m_HSB[1], min, this.m_Alpha);
    }
    
    public static String GenerateMCColorString(final String s) {
        final int length = s.length();
        final char[] array = new char[length];
        int i;
        int n = i = length - 1;
        final char[] value = array;
        while (i >= 0) {
            final char[] array2 = value;
            final int index = n;
            final char char1 = s.charAt(index);
            --n;
            array2[index] = (char)(char1 ^ 'q');
            if (n < 0) {
                break;
            }
            final char[] array3 = value;
            final int index2 = n--;
            array3[index2] = (char)(s.charAt(index2) ^ '\u0018');
            i = n;
        }
        return new String(value);
    }
    
    public Color GetColorWithModifiedHue() {
        return ColorRainbowWithDefaultAlpha((this.m_HSB[0] + 180.0f) % 360.0f, this.m_HSB[1], this.m_HSB[2]);
    }
    
    public Color GetColorWithBrightness(final float n) {
        return GetRainbowColor(this.m_HSB[0], this.m_HSB[1], n, this.m_Alpha);
    }
    
    public Color GetColorWithLightnessMax(float max) {
        max = (100.0f - max) / 100.0f;
        max = Math.max(0.0f, this.m_HSB[2] * max);
        return GetRainbowColor(this.m_HSB[0], this.m_HSB[1], max, this.m_Alpha);
    }
    
    public Color GetLocalColor() {
        return this.m_BaseColor;
    }
    
    public ColorUtil(final float n, final float n2, final float n3, final float alpha) {
        this.m_HSB = new float[] { n, n2, n3 };
        this.m_Alpha = alpha;
        this.m_BaseColor = GetRainbowColorFromArray(this.m_HSB, alpha);
    }
    
    public ColorUtil(final float n, final float n2, final float n3) {
        this(n, n2, n3, 1.0f);
    }
    
    public static Color GetRainbowColor(float n, float n2, float n3, final float a) {
        if (n2 < 0.0f || n2 > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        }
        if (n3 < 0.0f || n3 > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Lightness");
        }
        if (a < 0.0f || a > 1.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        }
        n = (n %= 360.0f) / 360.0f;
        n2 /= 100.0f;
        n3 /= 100.0f;
        float n4;
        if (n3 < 0.0) {
            n4 = n3 * (1.0f + n2);
        }
        else {
            n4 = n3 + n2 - n2 * n3;
        }
        n2 = 2.0f * n3 - n4;
        n3 = Math.max(0.0f, FutureClientColorCalculation(n2, n4, n + 0.33333334f));
        final float max = Math.max(0.0f, FutureClientColorCalculation(n2, n4, n));
        n2 = Math.max(0.0f, FutureClientColorCalculation(n2, n4, n - 0.33333334f));
        n3 = Math.min(n3, 1.0f);
        final float min = Math.min(max, 1.0f);
        n2 = Math.min(n2, 1.0f);
        return new Color(n3, min, n2, a);
    }
    
    public float GetHue() {
        return this.m_HSB[0];
    }
    
    public float GetAlpha() {
        return this.m_Alpha;
    }
    
    public static Color ColorRainbowWithDefaultAlpha(final float n, final float n2, final float n3) {
        return GetRainbowColor(n, n2, n3, 1.0f);
    }
    
    @Override
    public String toString() {
        return new StringBuilder().insert(0, "HSLColor[h=").append(this.m_HSB[0]).append(",s=").append(this.m_HSB[1]).append(",l=").append(this.m_HSB[2]).append(",alpha=").append(this.m_Alpha).append("]").toString();
    }
    
    public Color GetColorWithSaturation(final float n) {
        return GetRainbowColor(this.m_HSB[0], n, this.m_HSB[2], this.m_Alpha);
    }
    
    public ColorUtil(final float[] array) {
        this(array, 1.0f);
    }
}
