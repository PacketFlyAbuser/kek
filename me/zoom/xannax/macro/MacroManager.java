// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager
{
    /* synthetic */ List<Macro> macros;
    
    public Macro getMacroByValue(final String s) {
        return this.getMacros().stream().filter(macro -> macro.getValue() == s).findFirst().orElse(null);
    }
    
    public List<Macro> getMacros() {
        return this.macros;
    }
    
    public MacroManager() {
        this.macros = new ArrayList<Macro>();
    }
    
    public void addMacro(final Macro macro) {
        this.macros.add(macro);
    }
    
    public void delMacro(final Macro macro) {
        this.macros.remove(macro);
    }
    
    public Macro getMacroByKey(final int n) {
        return this.getMacros().stream().filter(macro -> macro.getKey() == n).findFirst().orElse(null);
    }
}
