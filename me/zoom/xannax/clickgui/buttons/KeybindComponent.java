//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui.buttons;

import me.zoom.xannax.util.font.FontUtils;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.module.ModuleManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import me.zoom.xannax.module.modules.client.ClickGuiModule;
import me.zoom.xannax.clickgui.Buttons;
import me.zoom.xannax.clickgui.Component;

public class KeybindComponent extends Component
{
    private /* synthetic */ boolean hovered;
    private /* synthetic */ boolean binding;
    private /* synthetic */ int offset;
    private final /* synthetic */ Buttons parent;
    private /* synthetic */ int y;
    private /* synthetic */ int x;
    
    @Override
    public void mouseClicked(final int n, final int n2, final int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
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
    public void setOff(final int offset) {
        this.offset = offset;
    }
    
    public boolean isMouseOnButton(final int n, final int n2) {
        return n > this.x && n < this.x + 88 && n2 > this.y && n2 < this.y + 16;
    }
    
    @Override
    public void renderComponent() {
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 15, this.hovered ? new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB() : new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).getRGB());
        Gui.drawRect(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, new Color(0, 0, 0, ClickGuiModule.opacity.getValue() - 50).darker().darker().getRGB());
        FontUtils.drawKeyStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.binding ? "Key..." : ("Key: " + ChatFormatting.GRAY + Keyboard.getKeyName(this.parent.mod.getBind())), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, -1);
    }
    
    public KeybindComponent(final Buttons parent, final int offset) {
        this.parent = parent;
        this.x = parent.parent.getX() + parent.parent.getWidth();
        this.y = parent.parent.getY() + parent.offset;
        this.offset = offset;
    }
    
    @Override
    public void updateComponent(final int n, final int n2) {
        this.hovered = this.isMouseOnButton(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
    }
}
