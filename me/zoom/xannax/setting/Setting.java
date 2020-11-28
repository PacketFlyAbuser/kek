// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.setting;

import java.util.List;
import me.zoom.xannax.module.Module;

public class Setting
{
    private final /* synthetic */ Type type;
    private final /* synthetic */ String configname;
    private final /* synthetic */ Module.Category category;
    private final /* synthetic */ Module parent;
    private final /* synthetic */ String name;
    
    public Module getParent() {
        return this.parent;
    }
    
    public String getConfigName() {
        return this.configname;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public Setting(final String name, final String configname, final Module parent, final Module.Category category, final Type type) {
        this.name = name;
        this.configname = configname;
        this.parent = parent;
        this.type = type;
        this.category = category;
    }
    
    public Module.Category getCategory() {
        return this.category;
    }
    
    public String getName() {
        return this.name;
    }
    
    public enum Type
    {
        MODE, 
        BOOLEAN, 
        DOUBLE, 
        STRING, 
        INT;
    }
    
    public static class Double extends Setting
    {
        private /* synthetic */ double value;
        private final /* synthetic */ double min;
        private final /* synthetic */ double max;
        
        public double getValue() {
            return this.value;
        }
        
        public double getMax() {
            return this.max;
        }
        
        public void setValue(final double value) {
            this.value = value;
        }
        
        public Double(final String s, final String s2, final Module module, final Module.Category category, final double value, final double min, final double max) {
            super(s, s2, module, category, Type.DOUBLE);
            this.value = value;
            this.min = min;
            this.max = max;
        }
        
        public double getMin() {
            return this.min;
        }
    }
    
    public static class Mode extends Setting
    {
        private /* synthetic */ String value;
        private final /* synthetic */ List<String> modes;
        
        public Mode(final String s, final String s2, final Module module, final Module.Category category, final List<String> modes, final String value) {
            super(s, s2, module, category, Type.MODE);
            this.value = value;
            this.modes = modes;
        }
        
        public void setValue(final String value) {
            this.value = value;
        }
        
        public List<String> getModes() {
            return this.modes;
        }
        
        public String getValue() {
            return this.value;
        }
    }
    
    public static class Boolean extends Setting
    {
        private /* synthetic */ boolean value;
        
        public void setValue(final boolean value) {
            this.value = value;
        }
        
        public boolean getValue() {
            return this.value;
        }
        
        public Boolean(final String s, final String s2, final Module module, final Module.Category category, final boolean value) {
            super(s, s2, module, category, Type.BOOLEAN);
            this.value = value;
        }
    }
    
    public static class Integer extends Setting
    {
        private final /* synthetic */ int max;
        private final /* synthetic */ int min;
        private /* synthetic */ int value;
        
        public int getValue() {
            return this.value;
        }
        
        public int getMax() {
            return this.max;
        }
        
        public void setValue(final int value) {
            this.value = value;
        }
        
        public Integer(final String s, final String s2, final Module module, final Module.Category category, final int value, final int min, final int max) {
            super(s, s2, module, category, Type.INT);
            this.value = value;
            this.min = min;
            this.max = max;
        }
        
        public int getMin() {
            return this.min;
        }
    }
}
