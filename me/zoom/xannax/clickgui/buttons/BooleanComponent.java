//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui.buttons;

import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import me.zoom.xannax.clickgui.ClickGUI;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.clickgui.Buttons;
import me.zoom.xannax.clickgui.Component;

public class BooleanComponent extends Component
{
    private /* synthetic */ boolean hovered;
    private final /* synthetic */ Buttons parent;
    private final /* synthetic */ Setting.Boolean op;
    private /* synthetic */ int x;
    private /* synthetic */ int y;
    private /* synthetic */ int offset;
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 16;
    }
    
    public BooleanComponent(final Setting.Boolean op, final Buttons parent, final int offset) {
        this.op = op;
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
    }
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.op.setValue(!this.op.getValue());
        }
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
    
    @Override
    public void renderComponent() {
        ClickGUI.color = new Color(ClickGuiModule.red.getValue(), ClickGuiModule.green.getValue(), ClickGuiModule.blue.getValue(), ClickGuiModule.opacity.getValue()).getRGB();
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? (this.op.getValue() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB()) : (this.op.getValue() ? ClickGUI.color : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB()));
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.op.getName(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
    }
}
