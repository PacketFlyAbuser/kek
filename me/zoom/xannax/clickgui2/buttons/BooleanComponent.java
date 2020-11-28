// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.buttons;

import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.clickgui2.frame.Buttons;
import me.zoom.xannax.clickgui2.frame.Component;

public class BooleanComponent extends Component
{
    private /* synthetic */ int x;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ boolean hovered;
    /* synthetic */ int nameWidth;
    /* synthetic */ int centeredNameCoords;
    private final /* synthetic */ Setting.Boolean op;
    private /* synthetic */ int y;
    private /* synthetic */ int offset;
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.op.setValue(!this.op.getValue());
        }
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 100 && n2 > this.y && n2 < this.y + 12;
    }
    
    public BooleanComponent(final Setting.Boolean op, final Buttons parent, final int offset) {
        this.op = op;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
    }
    
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
    public void renderComponent() {
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + 12 + this.offset, this.op.getValue() ? Renderer.getMainColor() : Renderer.getTransColor(this.hovered));
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
        this.nameWidth = FontUtils.getStringWidth(false, this.op.getName());
        this.centeredNameCoords = (this.parent.parent.getWidth() - this.nameWidth) / 2;
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.op.getName(), this.parent.parent.getX() + this.centeredNameCoords, this.parent.parent.getY() + this.offset + 2, Renderer.getFontColor(true, 0).getRGB());
    }
}
