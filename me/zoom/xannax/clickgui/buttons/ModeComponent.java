//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui.buttons;

import me.zoom.xannax.util.font.FontUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.clickgui.Buttons;
import me.zoom.xannax.setting.Setting;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.clickgui.Component;

public class ModeComponent extends Component
{
    private final /* synthetic */ Module mod;
    private /* synthetic */ int offset;
    private /* synthetic */ boolean hovered;
    private final /* synthetic */ Setting.Mode set;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ int y;
    private /* synthetic */ int x;
    private /* synthetic */ int modeIndex;
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
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
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 16;
    }
    
    @Override
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, this.hovered ? new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB() : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
    }
}
