// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.buttons;

import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.frame.Renderer;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.clickgui2.frame.Buttons;
import me.zoom.xannax.clickgui2.frame.Component;

public class KeybindComponent extends Component
{
    /* synthetic */ int centeredNameCoords;
    /* synthetic */ int nameWidth;
    private /* synthetic */ boolean binding;
    private /* synthetic */ int offset;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ boolean hovered;
    private /* synthetic */ int y;
    private /* synthetic */ int x;
    /* synthetic */ String name;
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
    }
    
    public KeybindComponent(final Buttons parent, final int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.name = null;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 100 && n2 > this.y && n2 < this.y + 12;
    }
    
    @Override
    public void keyTyped(final char c, final int bind) {
        if (this.binding) {
            if (bind == 211) {
                this.parent.mod.setBind(0);
            }
            else if (bind == 1) {
                this.binding = false;
            }
            else {
                this.parent.mod.setBind(bind);
            }
            this.binding = false;
        }
    }
    
    @Override
    public void renderComponent() {
        if (this.binding) {
            this.name = "Key...";
        }
        else {
            this.name = "Key: " + ChatFormatting.GRAY + Keyboard.getKeyName(this.parent.mod.getBind());
        }
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, Renderer.getTransColor(false));
        this.nameWidth = FontUtils.getStringWidth(false, this.name);
        this.centeredNameCoords = (this.parent.parent.getWidth() - this.nameWidth) / 2;
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.binding ? "Key..." : ("Key: " + ChatFormatting.GRAY + Keyboard.getKeyName(this.parent.mod.getBind())), this.parent.parent.getX() + this.centeredNameCoords, this.parent.parent.getY() + this.offset + 2, Renderer.getFontColor(true, 0).getRGB());
    }
}
