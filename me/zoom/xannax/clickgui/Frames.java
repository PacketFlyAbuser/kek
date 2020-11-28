//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui;

import me.zoom.xannax.util.font.FontUtils;
import me.zoom.xannax.clickgui2.frame.Renderer;
import me.zoom.xannax.module.ModuleManager;
import me.zoom.xannax.Xannax;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;
import java.util.Iterator;
import me.zoom.xannax.module.Module;
import java.util.ArrayList;

public class Frames
{
    /* synthetic */ int mouseX;
    /* synthetic */ int animation;
    /* synthetic */ boolean hovering;
    public /* synthetic */ int dragY;
    public /* synthetic */ boolean open;
    public /* synthetic */ int buttonHeightProper;
    public /* synthetic */ ArrayList<Component> guicomponents;
    public /* synthetic */ int x;
    /* synthetic */ boolean font;
    public /* synthetic */ int y;
    /* synthetic */ int mouseY;
    private /* synthetic */ boolean isDragging;
    public /* synthetic */ Module.Category category;
    private final /* synthetic */ int barHeight;
    public /* synthetic */ int dragX;
    private final /* synthetic */ int width;
    private /* synthetic */ int height;
    
    public int getX() {
        return this.x;
    }
    
    public int getMouseY() {
        return this.mouseY;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setHovering(final boolean hovering) {
        this.hovering = hovering;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public boolean isWithinFrame(final int n, final int n2) {
        return this.isWithinHeader(this.mouseX, this.mouseY) || (n >= this.x && n <= this.x + this.width && n2 >= this.y - 1 && n2 <= this.y + this.barHeight + this.buttonHeightProper);
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void refresh() {
        int barHeight = this.barHeight;
        for (final Component component : this.guicomponents) {
            component.setOff(barHeight);
            barHeight += component.getHeight();
        }
        this.height = barHeight;
    }
    
    public int getMouseX() {
        return this.mouseX;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public void updateMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        for (final Frames frames : ClickGUI.frames) {
            if (dWheel < 0) {
                frames.setY(frames.getY() - 10);
            }
            else {
                if (dWheel <= 0) {
                    continue;
                }
                frames.setY(frames.getY() + 10);
            }
        }
    }
    
    public void setDrag(final boolean isDragging) {
        this.isDragging = isDragging;
    }
    
    public boolean isWithinHeader(final int n, final int n2) {
        return n >= this.x && n <= this.x + this.width && n2 >= this.y && n2 <= this.y + this.barHeight;
    }
    
    public void renderGUIFrame(final FontRenderer fontRenderer) {
        this.hovering = this.isWithinFrame(this.mouseX, this.mouseY);
        if (this.hovering && this.animation > 0) {
            this.animation -= 2;
        }
        if (!this.hovering && this.animation < 50) {
            this.animation += 2;
        }
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.barHeight, ClickGUI.color);
        if (this.font) {
            Xannax.fontRenderer.drawStringWithShadow(this.category.name(), (float)(this.x + 2), (float)(this.y + 3), -1);
        }
        else {
            FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + 2, this.y + 3, Renderer.getFontColor(this.hovering, this.animation).getRGB());
        }
        if (this.open && !this.guicomponents.isEmpty()) {
            final Iterator<Component> iterator = this.guicomponents.iterator();
            while (iterator.hasNext()) {
                iterator.next().renderComponent();
            }
        }
    }
    
    public int getAnimation() {
        return this.animation;
    }
    
    public Frames(final Module.Category category) {
        this.guicomponents = new ArrayList<Component>();
        this.category = category;
        this.open = true;
        this.isDragging = false;
        this.x = 5;
        this.y = 5;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int barHeight = this.barHeight;
        this.buttonHeightProper = 0;
        this.mouseX = 0;
        this.mouseY = 0;
        this.hovering = false;
        this.animation = 50;
        final Iterator<Module> iterator = ModuleManager.getModulesInCategory(category).iterator();
        while (iterator.hasNext()) {
            this.guicomponents.add(new Buttons(iterator.next(), this, barHeight));
            barHeight += 16;
        }
        this.refresh();
    }
    
    public ArrayList<Component> getComponents() {
        return this.guicomponents;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void updatePosition(final int n, final int n2) {
        if (this.isDragging) {
            this.setX(n - this.dragX);
            this.setY(n2 - this.dragY);
        }
    }
}
