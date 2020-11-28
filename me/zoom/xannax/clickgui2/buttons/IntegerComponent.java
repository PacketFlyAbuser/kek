// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.buttons;

import me.zoom.xannax.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.clickgui2.frame.Renderer;
import java.math.RoundingMode;
import java.math.BigDecimal;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.clickgui2.frame.Buttons;
import me.zoom.xannax.clickgui2.frame.Component;

public class IntegerComponent extends Component
{
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ int x;
    private /* synthetic */ boolean dragging;
    private /* synthetic */ int offset;
    private /* synthetic */ double renderWidth;
    private final /* synthetic */ Setting.Integer set;
    private /* synthetic */ int y;
    private /* synthetic */ boolean hovered;
    
    public IntegerComponent(final Setting.Integer set, final Buttons parent, final int offset) {
        this.dragging = false;
        this.set = set;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
    }
    
    @Override
    public void mouseReleased(final int n, final int n2, final int n3) {
        this.dragging = false;
    }
    
    private static double roundToPlace(final double val, final int newScale) {
        if (newScale < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(val).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
    }
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButtonD(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
        }
        if (this.isMouseOnButtonI(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
        }
    }
    
    @Override
    public void renderComponent() {
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, Renderer.getTransColor(this.hovered));
        final int n = this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth();
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 12, Renderer.getMainColor());
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 2, Renderer.getFontColor(true, 0).getRGB());
    }
    
    public boolean isMouseOnButtonI(final int n, final int n2) {
        return n > this.x + this.parent.parent.getWidth() / 2 && n < this.x + this.parent.parent.getWidth() && n2 > this.y && n2 < this.y + 12;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = (this.isMouseOnButtonD(n, n2) || this.isMouseOnButtonI(n, n2));
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double n3 = Math.min(100, Math.max(0, n - this.x));
        final int min = this.set.getMin();
        final int max = this.set.getMax();
        this.renderWidth = 100 * (this.set.getValue() - min) / (max - min);
        if (this.dragging) {
            if (n3 == 0.0) {
                this.set.setValue(this.set.getMin());
            }
            else {
                this.set.setValue((int)roundToPlace(n3 / 100.0 * (max - min) + min, 2));
            }
        }
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    public boolean isMouseOnButtonD(final int n, final int n2) {
        return n > this.x && n < this.x + (this.parent.parent.getWidth() / 2 + 1) && n2 > this.y && n2 < this.y + 12;
    }
}
