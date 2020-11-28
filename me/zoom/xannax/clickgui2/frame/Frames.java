//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.zoom.xannax.clickgui2.frame;

import me.zoom.xannax.util.font.FontUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import me.zoom.xannax.module.modules.client.Rainbow;
import net.minecraft.client.gui.FontRenderer;
import me.zoom.xannax.module.ModuleManager;
import java.util.Iterator;
import me.zoom.xannax.clickgui2.ClickGUI2;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import me.zoom.xannax.module.Module;

public class Frames
{
    /* synthetic */ int mouseY;
    /* synthetic */ boolean hovering;
    public /* synthetic */ int dragY;
    private /* synthetic */ boolean isDragging;
    public /* synthetic */ boolean open;
    public /* synthetic */ int x;
    private /* synthetic */ int height;
    /* synthetic */ int centeredNameCoords;
    public /* synthetic */ int y;
    public /* synthetic */ int buttonHeightProper;
    public /* synthetic */ Module.Category category;
    public /* synthetic */ ArrayList<Component> guicomponents;
    private final /* synthetic */ int width;
    public /* synthetic */ int dragX;
    private final /* synthetic */ int barHeight;
    /* synthetic */ int rainbowOffset;
    /* synthetic */ int buttonHeight;
    /* synthetic */ Minecraft mc;
    /* synthetic */ int mouseX;
    /* synthetic */ int animation;
    /* synthetic */ int nameWidth;
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean isWithinFrame(final int n, final int n2) {
        return this.isWithinHeader(this.mouseX, this.mouseY) || (n >= this.x && n <= this.x + this.width && n2 >= this.y - 1 && n2 <= this.y + this.barHeight + this.buttonHeightProper);
    }
    
    public void updateMouseWheel() {
        final int dWheel = Mouse.getDWheel();
        for (final Frames frames : ClickGUI2.frames) {
            if (dWheel < 0) {
                frames.setY(frames.getY() - 5);
            }
            else {
                if (dWheel <= 0) {
                    continue;
                }
                frames.setY(frames.getY() + 5);
            }
        }
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public boolean isWithinHeader(final int n, final int n2) {
        return n >= this.x && n <= this.x + this.width && n2 >= this.y - 1 && n2 <= this.y + this.barHeight + 1;
    }
    
    public int getMouseX() {
        return this.mouseX;
    }
    
    public boolean isHovering() {
        return this.hovering;
    }
    
    public Frames(final Module.Category category) {
        this.mc = Minecraft.getMinecraft();
        this.guicomponents = new ArrayList<Component>();
        this.category = category;
        this.open = true;
        this.isDragging = false;
        this.x = 10;
        this.y = 34;
        this.dragX = 0;
        this.width = 100;
        this.barHeight = 16;
        int barHeight = this.barHeight;
        this.nameWidth = 0;
        this.centeredNameCoords = 0;
        this.rainbowOffset = 0;
        this.buttonHeight = 0;
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
    
    public void refresh() {
        int barHeight = this.barHeight;
        for (final Component component : this.guicomponents) {
            component.setOff(barHeight);
            barHeight += component.getHeight();
        }
        this.height = barHeight;
    }
    
    public void renderGUIFrame(final FontRenderer fontRenderer) {
        this.hovering = this.isWithinFrame(this.mouseX, this.mouseY);
        if (this.hovering && this.animation > 0) {
            this.animation -= 2;
        }
        if (!this.hovering && this.animation < 50) {
            this.animation += 2;
        }
        this.rainbowOffset = Rainbow.getRainbowOffset();
        this.mc.renderEngine.bindTexture(new ResourceLocation("minecraft:rainbow.png"));
        Renderer.renderImage(this.x - 1, this.y - 1, this.x + 1, this.y + this.rainbowOffset, this.width + 1, this.barHeight, 1920.0f, 1080.0f);
        GlStateManager.pushMatrix();
        this.nameWidth = FontUtils.getStringWidth(false, this.category.name());
        this.centeredNameCoords = (this.width - this.nameWidth) / 2;
        GlStateManager.popMatrix();
        FontUtils.drawStringWithShadow(ModuleManager.isModuleEnabled("CustomFont"), this.category.name(), this.x + this.centeredNameCoords, this.y + 3, Renderer.getFontColor(this.hovering, this.animation).getRGB());
        if (this.open && !this.guicomponents.isEmpty()) {
            for (final Component component : this.guicomponents) {
                component.renderComponent();
                this.buttonHeight += component.getButtonHeight();
            }
            this.buttonHeightProper = this.buttonHeight;
            this.buttonHeight = 0;
        }
    }
    
    public void setDrag(final boolean isDragging) {
        this.isDragging = isDragging;
    }
    
    public int getRainbowOffset() {
        return this.rainbowOffset;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getMouseY() {
        return this.mouseY;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setHovering(final boolean hovering) {
        this.hovering = hovering;
    }
    
    public void updatePosition(final int mouseX, final int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    
    public ArrayList<Component> getComponents() {
        return this.guicomponents;
    }
    
    public int getAnimation() {
        return this.animation;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
    }
    
    public int getY() {
        return this.y;
    }
}
