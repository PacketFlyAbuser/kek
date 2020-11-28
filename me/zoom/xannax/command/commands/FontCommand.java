// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.command.commands;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.util.font.CFontRenderer;
import java.awt.Font;
import me.zoom.xannax.command.Command;

public class FontCommand extends Command
{
    @Override
    public String[] getAlias() {
        return new String[] { "font", "setfont" };
    }
    
    @Override
    public String getSyntax() {
        return "font <Name> <Size>";
    }
    
    @Override
    public void onCommand(final String s, final String[] array) throws Exception {
        final String replace = array[0].replace("_", " ");
        final int int1 = Integer.parseInt(array[1]);
        (Xannax.fontRenderer = new CFontRenderer(new Font(replace, 0, int1), true, false)).setFontName(replace);
        Xannax.fontRenderer.setFontSize(int1);
    }
}
