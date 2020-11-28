// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.buttons;

import me.zoom.xannax.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.clickgui2.frame.Buttons;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.clickgui2.frame.Component;

public class ModeComponent extends Component
{
    private /* synthetic */ boolean hovered;
    private /* synthetic */ int offset;
    private final /* synthetic */ Module mod;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ int y;
    private /* synthetic */ int x;
    private /* synthetic */ int modeIndex;
    private final /* synthetic */ Setting.Mode set;
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            final int n4 = this.set.getModes().size() - 1;
            ++this.modeIndex;
            if (this.modeIndex > n4) {
                this.modeIndex = 0;
            }
            this.set.setValue(this.set.getModes().get(this.modeIndex));
        }
    }
    
    public ModeComponent(final Setting.Mode set, final Buttons parent, final Module mod, final int offset) {
        this.set = set;
        this.parent = parent;
        this.mod = mod;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
        this.modeIndex = 0;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 100 && n2 > this.y && n2 < this.y + 12;
    }
    
    @Override
    public void renderComponent() {
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 12, Renderer.getTransColor(this.hovered));
        Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 2, Renderer.getFontColor(true, 0).getRGB());
    }
}
