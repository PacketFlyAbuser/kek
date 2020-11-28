//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui.buttons;

import java.math.RoundingMode;
import java.math.BigDecimal;
import me.zoom.xannax.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import me.zoom.xannax.clickgui.ClickGUI;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.clickgui.Buttons;
import me.zoom.xannax.clickgui.Component;

public class DoubleComponent extends Component
{
    private /* synthetic */ boolean dragging;
    private /* synthetic */ int x;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ boolean hovered;
    private final /* synthetic */ Setting.Double set;
    private /* synthetic */ int y;
    private /* synthetic */ double renderWidth;
    private /* synthetic */ int offset;
    
    public boolean isMouseOnButtonI(final int n, final int n2) {
        return n > this.x + this.parent.parent.getWidth() / 2 && n < this.x + this.parent.parent.getWidth() && n2 > this.y && n2 < this.y + 16;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = (this.isMouseOnButtonD(n, n2) || this.isMouseOnButtonI(n, n2));
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        final double n3 = Math.min(100, Math.max(0, n - this.x));
        final double min = this.set.getMin();
        final double max = this.set.getMax();
        this.renderWidth = 100.0 * (this.set.getValue() - min) / (max - min);
        if (this.dragging) {
            if (n3 == 0.0) {
                this.set.setValue(this.set.getMin());
            }
            else {
                this.set.setValue(roundToPlace(n3 / 100.0 * (max - min) + min, 2));
            }
        }
    }
    
    public boolean isMouseOnButtonD(final int n, final int n2) {
        return n > this.x && n < this.x + (this.parent.parent.getWidth() / 2 + 1) && n2 > this.y && n2 < this.y + 16;
    }
    
    public DoubleComponent(final Setting.Double set, final Buttons parent, final int offset) {
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
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        ClickGUI.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.opacity.getValue()).getRGB();
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB() : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        final int n = (int)(this.set.getValue() / this.set.getMax() * this.parent.parent.getWidth());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + (int)this.renderWidth, this.parent.parent.getY() + this.offset + 16, this.hovered ? ClickGUI.color : ClickGUI.color);
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
    }
    
    private static double roundToPlace(final double val, final int newScale) {
        if (newScale < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(val).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
    }
}
