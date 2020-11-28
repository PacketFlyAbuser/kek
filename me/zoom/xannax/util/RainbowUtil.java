// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.util;

import java.util.ArrayList;

public class RainbowUtil
{
    private /* synthetic */ int m_Timer;
    private /* synthetic */ TimerUtils RainbowSpeed;
    private /* synthetic */ ArrayList<Integer> RainbowArrayList;
    private /* synthetic */ ArrayList<Integer> CurrentRainbowIndexes;
    
    public int GetGradientRainbowColorAt(int index, final int n) {
        index += n;
        if (index > this.CurrentRainbowIndexes.size() - 1) {
            index = this.CurrentRainbowIndexes.size() - 1;
        }
        return this.RainbowArrayList.get(this.CurrentRainbowIndexes.get(index));
    }
    
    public void SetTimer(final int timer) {
        this.m_Timer = timer;
    }
    
    private void MoveListToNextColor() {
        if (this.CurrentRainbowIndexes.isEmpty()) {
            return;
        }
        this.CurrentRainbowIndexes.remove(this.CurrentRainbowIndexes.get(0));
        int i = this.CurrentRainbowIndexes.get(this.CurrentRainbowIndexes.size() - 1) + 1;
        if (i >= this.RainbowArrayList.size() - 1) {
            i = 0;
        }
        this.CurrentRainbowIndexes.add(i);
    }
    
    public RainbowUtil(final int timer) {
        this.CurrentRainbowIndexes = new ArrayList<Integer>();
        this.RainbowArrayList = new ArrayList<Integer>();
        this.RainbowSpeed = new TimerUtils();
        this.m_Timer = timer;
        for (int i = 0; i < 360; ++i) {
            this.RainbowArrayList.add(ColorUtil.GetRainbowColor((float)i, 50.0f, 50.0f, 1.0f).getRGB());
            this.CurrentRainbowIndexes.add(i);
        }
    }
    
    public void OnRender() {
        if (this.RainbowSpeed.passed(this.m_Timer)) {
            this.RainbowSpeed.reset();
            this.MoveListToNextColor();
        }
    }
    
    public int GetRainbowColorAt(int index) {
        if (index > this.CurrentRainbowIndexes.size() - 1) {
            index = this.CurrentRainbowIndexes.size() - 1;
        }
        return this.RainbowArrayList.get(this.CurrentRainbowIndexes.get(index));
    }
}
